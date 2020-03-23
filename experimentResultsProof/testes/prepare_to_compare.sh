#!/bin/bash

#used to create HV files to compare using statistics from Gian 
#execute with ./ not sh

mkdir clustering
algs=('HypE' 'MOEAD' "MOEADSTM" "NSGAII" "SPEA2" "ThetaDEA" "IBEA" "MOEADD" "MOMBI2" "NSGAIII" "SPEA2SDE" )
datasets=('D31' 'ds2c2sc13_V1' 'ds2c2sc13_V2' 'ds2c2sc13_V3' 'ds3c3sc6_V1' 'ds3c3sc6_V2' 'frogs_V1' 'frogs_V2' 'frogs_V3' 'golub_V1' 'golub_V3' 'leukemia_V1' 'leukemia_V2' 'libras' 'optdigits' 'seeds' 'spiralsquare' 'tevc_20_60_1' 'UKC1')
# datasets=('ds2c2sc13_V1' 'ds2c2sc13_V2' 'ds2c2sc13_V3' 'frogs_V1' 'frogs_V2' 'frogs_V3')


for alg in "${algs[@]}"
do
    mkdir ./clustering/$alg
    for dataset in "${datasets[@]}"
    do
        mkdir ./clustering/${alg}/$dataset
    done
done

# #used to generate input files to PISA
# for dir in ./*/     # list directories in the form "/tmp/dirname/"
# do
#     dir=${dir%*/}      # remove the trailing "/"
#     echo ${dir##*/}    # print everything after the final "/"

# done

    for dataset in "${datasets[@]}"
    do
        for alg in "${algs[@]}"
        do
            for file in $dir/${dataset}_${alg}_hyp.30
            do
            echo ${file##*/}
            cp ./${dataset}_test/${file##*/} ./clustering/$alg/$dataset/HV
            sed -i '$d' ./clustering/$alg/$dataset/HV
            done
        done
    done


