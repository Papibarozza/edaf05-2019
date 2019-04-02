#!/bin/bash
# make exacutable: chmod +x check_solution.sh
# run: ./check_solution.sh pypy A.py
# sh -x check_solution.sh java -cp ../bin/ lab1.GS <--- run this. From this dir.
# or
# ./check_solution.sh java solution
# ./check_solution.sh ./a.out

 
F="data/secret/4testhuge.in";
echo $F
pre=${F%.in}
out=$pre.out
verdict=$pre.verd
START_TIME=`echo $(($(date +%s%N)/1000000))`
$* < $F > $out
END_TIME=`echo $(($(date +%s%N)/1000000))`
ELAPSED_TIME=$(($END_TIME - $START_TIME))
echo $ELAPSED_TIME
echo 'Checking...'
python output_validator/output_validator.py $F < $out > $verdict
if grep -Fxq "success" $verdict
then 
    echo Correct!
else
    echo $F Incorrect!
    exit 1
fi

