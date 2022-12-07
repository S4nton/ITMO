#!/bin/bash

home="/home/user"
currentDate=$(date '+%F')

for dirPath in $(ls -d $home/*)
do
	dir=$(echo $dirPath | rev | cut -d "/" -f 1 | rev)
	if [[ $dir =~ ^(Backup-[0-9]{4}-[0-9]{2}-[0-9]{2})$ ]]
	then
		dirCreateDate=$(echo $dir | cut -d "-" -f 2-)
		dirCreateDate=$(date -d $dirCreateDate '+%s')
		(( dirCreateDate += 60*20*24*7 ))

		if [[ $(date -d $currentDate '+%s') -le $dirCreateDate ]]
		then
			workingDir=$dir
		fi
	fi
done

if [[ -z $workingDir ]]
then
	echo "upback: no backup dir in "$home"/ directory"

else
	if [[ ! -d $home/restore/ ]]
	then
		mkdir $home/restore
	fi

	for filePath in $(ls $home/source/*)
	do
		filename=$(echo $filePath | rev | cut -d "/" -f 1 | rev)
		if [[ ! $filename =~ ^(*.[0-9]{4}-[0-9]{2}-[0-9]{2})$ ]]
		then
			cp $filePath $home/restore/$filename
		fi
	done
fi
