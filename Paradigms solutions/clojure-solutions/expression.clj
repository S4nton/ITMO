(def constant constantly)
(defn variable [name] (fn [vals] (get vals name)))

(defn operation [f] (fn
                      ([& operands] (fn [variables]
                                        (apply f ((apply juxt operands) variables))))
                      ([] (fn [variable] (f)))))

(def add (operation +))
(def subtract (operation -))
(def negate subtract)
(def multiply (operation *))

(defn divide-realiz ([operand] (/ 1 (double operand)))
      ([numer & operands] (/ numer (double (apply * operands)))))

(def divide (operation divide-realiz))
(def sum add)
(def avg (operation #(/ (apply + %&) (count %&))))

(def operations {
                 '+ add
                 '- subtract
                 '* multiply
                 '/ divide
                 'sum sum
                 'avg avg
                 'negate negate
                 })

(defn parser [c v opers expr] (cond
                                (number? expr) (c expr)
                                (symbol? expr) (v (str expr))
                                :else (apply (opers (first expr)) (map (partial parser c v opers) (rest expr)))))

(defn parseFunction [expr] (parser constant variable operations (read-string expr)))









; :NOTE: proto.clj
(defn proto-get [this key] (cond
                             (contains? this key) (this key)
                             (contains? this :prototype) (proto-get (this :prototype) key)))

(defn proto-call [this key & args] (apply (proto-get this key) this args))

(defn field [key] (fn [obj] (proto-get obj key)))

(defn method [key] (fn [this & args] (apply proto-call this key args)))

(def evaluate (method :evaluate))
(def toString (method :toString))
(def parseObject (method :parseObject))
(def diff (method :diff))

(defn constructor [ctor prototype] (fn [& args] (apply ctor {:prototype prototype} args)))

(defn constructor-with-fields [prototype & fields] (constructor
                                                     (fn [this & vals]
                                                         (reduce (fn [this [field val]] (assoc this field val))
                                                                 this (mapv vector fields vals))) prototype))

(def Operation-proto
  (let [f (field :f)
        inString (field :inString)
        operands (field :operands)
        diff-func (field :diff-func)]
       {
        :evaluate (fn [this vals] (apply (f this) (mapv #(evaluate % vals) (operands this))))
        :toString (fn [this] (str "(" (inString this) " " (clojure.string/join " " (mapv toString (operands this))) ")"))
        :diff (fn [this var] ((diff-func this) (operands this) (mapv #(diff % var) (operands this))))
        }))

(defn operation-construct [f inString diff-func] (constructor (fn [this & vals] (assoc this :operands vals))
                                                              ((constructor-with-fields Operation-proto :f :inString :diff-func) f inString diff-func)))

(declare Add)
(declare Multiply)
(declare Constant)
(declare Divide)
(declare Negate)
(declare Subtract)

(defn multi-diff-func [oper diff-oper] (second (reduce (fn [[a diff-a] [b diff-b]] [(Multiply a b) (Add (Multiply diff-a b) (Multiply diff-b a))]) [(Constant 1) (Constant 0)]
                                                       (mapv vector oper diff-oper))))

(defn div-diff-func [oper diff-oper] (if (== (count oper) 1)
                                       (Divide (Negate (first diff-oper)) (Multiply (first oper) (first oper)))
                                       (let [b (apply Multiply (rest oper))
                                             diff-b (multi-diff-func (rest oper) (rest diff-oper))]
                                            (Divide (Subtract (Multiply (first diff-oper) b) (Multiply diff-b (first oper)))
                                                    (Multiply b b)))))

(def Add (operation-construct + '+ (fn [_ diff-oper] (apply Add diff-oper))))
(def Subtract (operation-construct - '- (fn [_ diff-oper] (apply Subtract diff-oper))))
(def Negate (operation-construct - 'negate (fn [_ diff-oper] (apply Negate diff-oper))))
(def Multiply (operation-construct * '* multi-diff-func))
(def Divide (operation-construct divide-realiz '/ div-diff-func))
(def Sum (operation-construct + 'sum (fn [_ diff-oper] (apply Sum diff-oper))))
(def Avg (operation-construct #(/ (apply + %&) (count %&)) 'avg (fn [oper diff-oper] (Divide (apply Add diff-oper) (Constant (count oper))))))

(def Constant-proto
  (let [val (field :val)]
       {
        :evaluate (fn [this _] (val this))
        :toString (fn [this] (str (double (val this))))
        :diff (fn [_ _] (Constant 0))
        }))

(def Constant (constructor-with-fields Constant-proto :val))

(def Variable-proto
  (let [name (field :name)]
       {
        :evaluate (fn [this vals] (vals (name this)))
        :toString (fn [this] (name this))
        :diff (fn [this var] (if (= var (toString this))
                               (Constant 1)
                               (Constant 0)))
        }))

(def Variable (constructor-with-fields Variable-proto :name))

(def operations-obj {
                     '+ Add
                     '- Subtract
                     '* Multiply
                     '/ Divide
                     'sum Sum
                     'avg Avg
                     'negate Negate
                     })

(defn parseObject [expr] (parser Constant Variable operations-obj (read-string expr)))