The iris dataset is from UCI repository (http://archive.ics.uci.edu/ml/datasets.html). 

These files are provided as an example so you can easily get started using MOCLE and include:
- iris-dataset.txt - dataset pre-processed and formated to run with MOCLE. It is a text file with the data, where the first row contains columns labels and first column indicates the object labels.
- initialPartitions - collection of partitions to be used as MOCLE's initia partitions. This folder contains text files with the extension .clu, each representing one partition. Each file contains 2 columns: the first column represents objects' labels (as in the dataset) and the second column represents clusters' labels.
- truePartition - contains a .clu file representing the known partition of the dataset. It has the same structure as those in initialPartitions.

* There is no guarantee that all files presents the objects in the same order.
