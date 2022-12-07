#!bin/bash

if (( $(bc <<< "$1 > $2") ))
then max=$1
else max=$2
fi
if (( $(bc <<< "$3 > max") ))
then max=$3
fi
echo $max
