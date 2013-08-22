#   Shell Script to remove all occurances of ".svn" folder from a given directory.
#   Script will recursively remove all ".svn" folders from subdirectories as well.

#   To Use:
#       Using a terinal, navigate to the directory where purgeSvn.sh is stored then
#       type: sh purgeSvn.sh path/to/directory/to/purge

if [ $1 ]
then
    files=`ls $1 -a`
    for name in $files
    do
        #echo $1$name
        if [ "$name" = ".svn" ]
        then
            echo "removing: $1/$name"
            rm -rf "$1/$name"
        elif [ -d "$1/$name" ]
        then
            if [ "$name" != "." ] && [ "$name" != ".." ]
            then
                #echo "d $1/$name"
                sh purgeSvn.sh "$1/$name"
            fi
        fi
    done
else
    echo "Usage: sh purgeSvn.sh path/to/directory/to/purge"
fi
