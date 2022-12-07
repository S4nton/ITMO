#!/bin/bash

while true
do
read str
if [[ $str != 'q' ]]
then res=$res$str
else break
fi
done

echo $res
