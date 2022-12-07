#!/bin/bash

filename=$1
home="/home/user"

printError() {
	echo -e "untrash: error\nUsage: untrash FILENAME\nError: "$1
	exit 1
}

if (( $# != 1 ))
then
	printError "expected exactly one argument"
fi

log=$(cat $home/.trash.log)
for line in $log
do
	path=$(awk -F "+" '{print $1}' <<< "$line")
	id=$(awk -F "+" '{print $2}'<<< "$line")
	pathFile=$(awk -F "/" '{print $NF}' <<< "$path")
	pathDir=$(echo "$path" | rev | cut -d "/" -f 2- | rev)

	if [[ ! -f $home/.trash/$id ]]; then continue; fi

	if [[ "$pathFile" == "$filename" ]]
	then
		echo -e "Recover file '$path'? [y/N]"
		while [[ true ]]
		do
			read ans
			if [[ $ans == "y" || $ans == "N" ]]; then break; fi
			echo -e "Usage: [y/N]"
		done

		if [[ $ans == "y" ]]
		then
			if [[ ! -d $pathDir ]]
			then
				echo "Dir '"$pathDir"' does not exist, recovering dir is'"$home"'"
				pathDir=$home
			fi

			while [[ true ]]
			do
				if [[ -f $pathDir/"$pathFile" ]]
				then
					echo "File '"$filename"' has already exist, enter another name for recovering file:"
					read pathFile
					continue
				fi
				ln $home/.trash/$id $pathDir/"$pathFile"
				rm $home/.trash/$id
				echo "File '"$path"' recovered as '"$pathFile"'"
				break
			done

			echo "Continue scanning for '"$filename"' entries? [y/N]"
			while [[ true ]]
			do
				read ans
				if [[ $ans == "y" || $ans == "N" ]]; then break; fi
				echo "Usage: [y/N]"
			done
			if [[ $ans == "N" ]]; then break; fi
		fi
	fi
done
