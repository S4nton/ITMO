from math import sqrt
from math import acos
from math import pi
import sys

fileInput = open('input.txt')
fileOutput = open('output.txt', 'w')

a1x, a1y = map(float, fileInput.readline().split())
a2x, a2y = map(float, fileInput.readline().split())
mx, my = map(float, fileInput.readline().split())
b1x, b1y = map(float, fileInput.readline().split())

a2x = a1x + a2x
a2y = a1y + a2y
ax = a2x - a1x
ay = a2y - a1y

bx = b1x - a1x
by = b1y - a1y

cos = (ax * bx + ay * by) / (sqrt(ax * ax + ay * ay) * sqrt(bx * bx + by * by))
angle1 = acos(cos) * (180 / pi)

if (angle1 < 30 or angle1 > 150):
    fileOutput.write("0")
    fileOutput.write("\n")
    fileOutput.write("GLHF")
    sys.exit(0)

cos = 1 / sqrt(mx * mx + my * my + 1)
angle2 = acos(cos) * (180 / pi)

if (angle2 > 60):
    fileOutput.write("0")
    fileOutput.write("\n")
    fileOutput.write("GLHF")
    sys.exit(0)

cur = ax * by - ay * bx
mx = a1x + mx
my = a1y + my
cur1 = ax * (my - a1y) - ay * (mx - a1x)

if (cur > 0):
    fileOutput.write("1")
else:
    fileOutput.write("-1")
fileOutput.write("\n")
fileOutput.write(str(90 - angle1))
fileOutput.write("\n")
if (cur1 >= 0):
    fileOutput.write(str(angle2))
else:
    fileOutput.write(str(-angle2))
fileOutput.write("\n")
fileOutput.write("GLHF")
