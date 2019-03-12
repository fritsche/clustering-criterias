# libmetis.a must be installed
# To install METIS
# 1. Download metis latest stable release (http://glaros.dtc.umn.edu/gkhome/metis/metis/download)
# 2. After downloading METIS you need to uncompress it and untar it. 
# 3. Install using makefile
# 3.1 make config
# 3.2 make
# 3.3 sudo make install
# 4. make log will print lib path, probably /usr/local/lib/libmetis.a

METIS_PATH=/usr/local/lib/libmetis.a

build:
	gcc -o ../resources/lib/libpartition.so partition.c $(METIS_PATH) -lm -shared -fPIC
