#!/bin/bash

#remove other files from path
find ./ -maxdepth 1 ! -name 'concatenate.sh' ! -name 'ARI.py' -type f -exec rm -fv {} \;


#used to generate input files to PISA
for dir in ./*/     # list directories in the form "/tmp/dirname/"
do
    dir=${dir%*/}      # remove the trailing "/"
    # echo ${dir##*/}    # print everything after the final "/"
    for dir2 in ./${dir##*/}/*/
    do
        dir2=${dir2%*/}
        # echo ${dir2##*/} 
        for counter in $(seq 0 29); do (cat ./${dir##*/}/${dir2##*/}/"FUN$counter.tsv"; echo) >> ${dir2##*/}_${dir##*/}.30 ; done
        for counter in $(seq 0 29); do (cat ./${dir##*/}/${dir2##*/}/"ARI$counter.tsv"; echo) >> "ARI_"${dir2##*/}_${dir##*/}.30 ; done
    done
done

#datasets that will be executed by PISA
datasets=('D31' 'ds2c2sc13_V1' 'ds2c2sc13_V2' 'ds2c2sc13_V3' 'ds3c3sc6_V1' 'ds3c3sc6_V2' 'frogs_V1' 'frogs_V2' 'frogs_V3' 'golub_V1' 'golub_V3' 'leukemia_V1' 'leukemia_V2' 'libras' 'optdigits' 'seeds' 'spiralsquare' 'tevc_20_60_1' 'UKC1')
# datasets=('ds2c2sc13_V3')

for dataset in "${datasets[@]}"
do
    mv ./$dataset*.30 ../../../PISA/runs/
    cd ../../../PISA/
    mkdir ./tests
    ksh ./run.ksh compare $dataset
    rm ./runs/*
    cd ./zz_BoxPlots
    python3 BoxPlot.py $dataset hyp
    python3 BoxPlot.py $dataset eps
    # python3 BoxPlot_CR.py $dataset ARI
    python3 PairwisePlot.py $dataset hyp
    python3 PairwisePlot.py $dataset eps

    #save te results folder
    rm -r ../../experimentResultsProof/testes/${dataset}_test
    mv ../tests ../../experimentResultsProof/testes/${dataset}_test

    #come back to .30 files folder
    cd ../../experimentResultsProof/Experiment0/data

done


