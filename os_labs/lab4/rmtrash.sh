#!/bin/bash

filename=$1
home="/home/user"

printError() {
	echo -e "rmtrash: error\nUsage: rmtrash FILENAME\nError: "$1
	exit 1
}

if (( $# != 1 ))
then
	printError "expected exactly one argument"
fi

if [[ ! $filename =~ ^([0-9a-zA-Z_.\-])*([0-9a-zA-Z _.\-])$ ]]
then
	printError "incorrect filename"
fi

if [[ ! -f $filename ]]
then
	printError "no such file '$filename'"
fi


if [[ ! -d $home/.trash ]]
then
	mkdir $home/.trash
fi

if [[ ! -f $home/.trash/.counter ]]
then
	echo 0 > $home/.trash/.counter
fi

id=$(cat $home/.trash/.counter)
(( id++ ))
echo $id > $home/.trash/.counter

ln "$filename" $home/.trash/$id
rm "$filename"

echo $(pwd)"/"$filename"+"$id >> $home/.trash.log
