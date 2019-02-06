Golub dataset is based in the original data used in
Golub TR, D K Slonim and PT, Huard C, Gaasenbeek M, Mesirov JP, Coller H, Loh M, Downing JR, Caligiuri MA, Bloomfield CD, Lander ES (1999) Molecular classification of cancer: Class discovery and class prediction by gene expression monitoring. Science 286(5439):531–537

These files are provided as an example so you can easily get started using MOCLE and include:
- golubDataset.txt - dataset pre-processed and formated to run with MOCLE. It is a text file with the data, where the first row contains columns labels and first column indicates the object labels.
- initialPartitions - collection of partitions to be used as MOCLE's initia partitions. This folder contains text files with the extension .clu, each representing one partition. Each file contains 2 columns: the first column represents objects' labels (as in the dataset) and the second column represents clusters' labels.
- truePartitions - contains two .clu files representing two known partitions of the dataset. Each file has the same structure as those in initialPartitions.

* There is no guarantee that all files presents the objects in the same order.