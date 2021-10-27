(defn checking-size-vectors [& vectors] (apply == (mapv count vectors)))
(defn checking-size-matrixs [& matrixs] (and (apply checking-size-vectors matrixs) (apply checking-size-vectors (mapv first matrixs))))

(defn is-vector [vector] (and (vector? vector) (every? number? vector)))
(defn is-matrix [matrix] (and (vector? matrix) (every? is-vector matrix) (apply checking-size-vectors matrix)))

(defn checking-size-simplexs [& simplexs] (if (every? is-vector simplexs)
                                            (apply checking-size-vectors simplexs)
                                            (and (apply checking-size-vectors simplexs) (every? (partial = true) (apply (partial mapv checking-size-simplexs) simplexs)))))

(defn check-correct-simplex-size [n simplex] (if (every? number? simplex)
                                               (== (count simplex) n)
                                               (and (== (count simplex) n) (every? (fn [x] (check-correct-simplex-size (- n (.indexOf simplex x)) x)) simplex))))

(defn is-simplex [simplex] (check-correct-simplex-size (count simplex) simplex))

(defn vector-function [f] (fn [& vectors] {
                                           :pre [(every? is-vector vectors) (apply checking-size-vectors vectors)]
                                           }
                              (apply (partial mapv f) vectors)))

(defn matrix-function [f](fn [& matrixs] {
                                          :pre [(every? is-matrix matrixs) (apply checking-size-matrixs matrixs)]
                                          }
                             (apply (partial mapv f) matrixs)))

(defn simplex-function [f] (fn [& simplexs] {
                                             :pre [(every? is-simplex simplexs) (apply checking-size-simplexs simplexs)]
                                             }
                               (if (every? is-vector simplexs)
                                 (apply (partial mapv f) simplexs)
                                 (apply (partial mapv (simplex-function f)) simplexs))))

(def v+ (vector-function +))
(def v- (vector-function -))
(def v* (vector-function *))
(def vd (vector-function /))

(def m+ (matrix-function v+))
(def m- (matrix-function v-))
(def m* (matrix-function v*))
(def md (matrix-function vd))

(def x+ (simplex-function +))
(def x- (simplex-function -))
(def x* (simplex-function *))
(def xd (simplex-function /))

(defn scalar [& vectors] {
                          :pre [(every? is-vector vectors) (apply checking-size-vectors vectors)]
                          }
      (apply + (apply v* vectors)))

(defn vect [& vectors] {
                        :pre [(every? is-vector vectors) (apply checking-size-vectors vectors) (== 3 (count (first vectors)))]
                        }
      (reduce (fn [v1 v2] (vector (- (* (nth v1 1) (nth v2 2)) (* (nth v1 2) (nth v2 1)))
                                  (- (* (nth v1 2) (nth v2 0)) (* (nth v1 0) (nth v2 2)))
                                  (- (* (nth v1 0) (nth v2 1)) (* (nth v1 1) (nth v2 0))))) vectors))

(defn v*s [vector & scalar] {
                             :pre [(is-vector vector) (every? number? scalar)]
                             }
      (mapv (partial * (apply * scalar)) vector))

(defn transpose [matrix] {
                          :pre [(is-matrix matrix)]
                          }
      (apply mapv vector matrix))

(defn m*s [matrix & scalar] {
                             :pre [(is-matrix matrix) (every? number? scalar)]
                             }
      (mapv (fn [vector] (v*s vector (apply * scalar))) matrix))

(defn m*v [matrix & vector] {
                             :pre [(is-matrix matrix) (every? is-vector vector) (mapv (fn [vector-m] (checking-size-vectors vector vector-m)) matrix)]
                             }
      (mapv (fn [vector-m] (apply scalar vector-m vector)) matrix))

(defn correct-multiply-matrixs [[a b]] (or (== (count a) (count (first b))) (== (count b) (count (first a)))))

(defn m*m [& matrixs] {
                       :pre [(every? is-matrix matrixs) (or (== 1 (count matrixs)) (correct-multiply-matrixs matrixs))]
                       }
      (reduce (fn [m1 m2] (mapv (fn [vector-m1] (mapv (fn [vector-m2] (apply + (v* vector-m1 vector-m2))) (transpose m2))) m1)) matrixs))