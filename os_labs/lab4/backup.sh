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
	workingDir="Backup-"$currentDate
	mkdir $home/$workingDir

	cp $home/source/* $home/$workingDir
	echo "backup: Created new backup catalog '"$workingDir"' in "$currentDate" with next content: "$(ls $home/$workingDir -1) >> $home/backup-report

else
	for filePath in $(ls $home/source/*)
	do
		filename=$(echo $filePath | rev | cut -d "/" -f 1 | rev)

		backupedFile=$home/$workingDir/$filename
		if [[ ! -f $backupedFile ]]
		then
			cp $filePath $backupedFile
			copied=$copied"\n"$filename
		else
			if [[ ! $(wc -c $filePath | awk '{print $1}') == $(wc -c $backupedFile | awk '{print $1}') ]]
			then
				mv $backupedFile $backupedFile"."$currentDate
				cp $filePath $backupedFile
				replaced=$replaced"\n"$filename" "$filename"."$currentDate
			fi
		fi
	done
	echo -e "backup: Changes in '"$workingDir"' by "$currentDate" with following files" >> $home/backup-report
	if [[ ! -z $copied ]]
	then
		echo -e "Copy files:" $copied >> $home/backup-report
	fi
	if [[ ! -z $replaced ]]
	then
		echo -e "Replace files:" $replaced >> $home/backup-report
	fi
fi
