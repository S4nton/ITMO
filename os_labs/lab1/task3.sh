#!/bin/bash

echo "1. nano"
echo "2. vi"
echo "3. links"
echo "4. Exit menu"

while true
do
read num
case $num in
1 )
nano
;;
2 )
vi
;;
3 )
links
;;
4 )
exit 0
;;
* )
echo "Wrong number!!!"
echo "1. nano"
echo "2. vi"
echo "3. links"
echo "4. Exit menu"
;;
esac
done
