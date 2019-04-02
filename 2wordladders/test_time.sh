#!/bin/bash
# make exacutable: chmod +x check_solution.sh
# run: ./check_solution.sh pypy A.py
# sh test_time.sh java -cp ../bin/ lab2.WordLadders <--- run this. From this dir.
# or
# ./check_solution.sh java solution
# ./check_solution.sh ./a.out

f="./data/secret/6large2.in"
echo $f
pre=${f%.in}
out=$pre.out
ans=$pre.ans
START_TIME=`echo $(($(date +%s%N)/1000000))`
$* < $f > $out
END_TIME=`echo $(($(date +%s%N)/1000000))`
ELAPSED_TIME=$(($END_TIME - $START_TIME))
echo $ELAPSED_TIME
DIFF=$(diff $ans $out)
if [ "$DIFF" == "" ]
then 
    echo Correct!
else
    echo $f Incorrect!
    exit 1
fi

