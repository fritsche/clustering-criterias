import matplotlib.pyplot as plt
import os
import numpy as np
import matplotlib as mpl 
import sys

## agg backend is used to create plot as a .png file
# mpl.use('agg')

value = []
labels = []

dataset_pool = ['ds2c2sc13_V1', 'ds2c2sc13_V2', 'ds2c2sc13_V3', 'frogs_V1', 'frogs_V2', 'frogs_V3', 'golub_V3', 'leukemia_V1', 'leukemia_V2', 'libras', 'optdigits', 'spiralsquare']
moea_pool = ["HypE", "IBEA", "MOEADD", "MOEAD", "MOEADSTM", "MOMBI2", "NSGAII", "NSGAIII", "SPEA2", "SPEA2SDE", "ThetaDEA"]
indicator_pool = ["ARI"]

tests_path = './'

for moea in moea_pool:
    for dataset in dataset_pool:
        file1 = open(tests_path+moea+"/"+dataset+"/ARI","w") 
        for file in sorted(os.listdir(tests_path+moea+"/"+dataset)):
            if file.startswith("ARI") & file.endswith(".tsv"):
                print(np.max(np.loadtxt(tests_path+moea+"/"+dataset+"/"+file)))
                file1.write(str(np.max(np.loadtxt(tests_path+moea+"/"+dataset+"/"+file))))
                file1.write("\n")
        file1.close() 

