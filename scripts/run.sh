#!/usr/bin/env bash

set -e

source scripts/seeds.sh


if [ "$#" -ne 2 ]; then
	echo "Expected <objectives> [2|4a|4b|7] and <normalize> [true|false]"
	exit 1
fi

dir=$(pwd)
# run locally using package 'at'
execute="bash $dir/scripts/addbatch.sh"

# # package using 1 thread per core and skip tests
# mvn package -T 1C -DskipTests

# execution configuration
algorithm=SPEA2SDE
problems=(D31 ds2c2sc13_V1 ds2c2sc13_V2 ds2c2sc13_V3 ds3c3sc6_V1 ds3c3sc6_V2 spiralsquare tevc_20_60_1 frogs_V1 frogs_V2 frogs_V3 golub_V1 golub_V3 leukemia_V1 leukemia_V2 optdigits libras seeds UKC1)
m=$1
normalize=$2
runs=30

jar=target/ClusteringCriterias-1.0-SNAPSHOT-jar-with-dependencies.jar
main=br.ufpr.inf.cbio.clusteringcriterias.runner.Main
javacommand="java -Duser.language=en -cp $jar -Xms4098m -Xmx4098m $main"

seed_index=0
if [ "$normalize" = true ]; then
    group="normalized"
else 
    group="notnormalized"
fi

for problem in "${problems[@]}"; do
    for (( id = 0; id < $runs; id++ )); do
        # problem and independent run (id) use a different seed
        seed=${seeds[$seed_index]}
        # different algorithms on the same problem instance uses the same seed
        output="$dir/experiment/$m/$group/$algorithm/$problem/"
        file="$output/FUN$id.tsv"
        if [ ! -s $file ] || [ "$replace" = true ]; then
            params="-a $algorithm -id $id -m $m -n $normalize -P $output -p $problem -s $seed"
            echo $execute "$javacommand $params 2> $algorithm.$seed.log"
        fi
        seed_index=$((seed_index+1))
    done
done
