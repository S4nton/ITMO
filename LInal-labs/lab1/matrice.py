def toMat(m):
    mat = []
    row = []
    getMat = list(map(float, fileInput.readline().split()))
    for i in range(len(getMat)):
        if (i % m == 0):
            mat.append(row)
            row = []
        row.append(getMat[i])
    mat.append(row)
    mat.pop(0)
    return mat

def matAndNum(num, mat):
    for i in range(len(mat)):
        for j in range(len(mat[i])):
            mat[i][j] *= num
    return mat

def Sum(a, b):
    if len(a) != len(b) or len(b[0]) != len(a[0]):
        return None
    mat = [[float(0) for i in range(len(a[0]))] for j in range(len(a))]
    for i in range(len(a)):
        for j in range(len(a[i])):
            mat[i][j] = a[i][j] + b[i][j]
    return mat

def Dif(a, b):
    if len(a) != len(b) or len(b[0]) != len(a[0]):
        return None
    mat = [[0 for i in range(len(a[0]))] for j in range(len(a))]
    for i in range(len(a)):
        for j in range(len(a[i])):
            mat[i][j] = a[i][j] - b[i][j]
    return mat

def Mult(a, b):
    if (len(a[0]) != len(b)):
        return None
    mat = [[0 for i in range(len(b[0]))] for j in range(len(a))]
    for i in range(len(a)):
        for j in range(len(b[0])):
            for k in range(len(b)):
                mat[i][j] += a[i][k] * b[k][j]
    return mat

def Tran(a):
    mat = [[0 for i in range(len(a))] for j in range(len(a[0]))]
    for j in range(len(a[0])):
        for i in range(len(a)):
            mat[j][i] = a[i][j]
    return mat

def getX(alpha, beta, a, b, c, d, f):
    mat = Sum(matAndNum(alpha, a), matAndNum(beta, Tran(b)))
    if (mat == None):
        return None
    mat = Mult(c, Tran(mat))
    if (mat == None):
        return None
    mat = Mult(mat, d)
    if (mat == None):
        return None
    mat = Dif(mat, f)
    return mat

fileInput = open('input.txt')
fileOutput = open('output.txt', 'w')

alpha, beta = map(float, fileInput.readline().split())

na, ma = map(int, fileInput.readline().split())
a = toMat(ma)

nb, mb = map(int, fileInput.readline().split())
b = toMat(mb)

nc, mc = map(int, fileInput.readline().split())
c = toMat(mc)

nd, md = map(int, fileInput.readline().split())
d = toMat(md)

nf, mf = map(int, fileInput.readline().split())
f = toMat(mf)

x = getX(alpha, beta, a, b, c, d, f)
if (x == None):
    fileOutput.write("0")
else:
    fileOutput.write("1\n")
    fileOutput.write(str(len(x)))
    fileOutput.write(" ")
    fileOutput.write(str(len(x[0])))
    fileOutput.write("\n")
    for row in x:
        for elem in row:
            fileOutput.write(str(elem))
            fileOutput.write(" ")
