#include <stdio.h>
#include <metis.h>

/**
@output 
int: METIS_PartGraphKway output status

@params
nvtxs: number of vertices
xadj: CSR index array
adjncy: CSR adjacency array
nparts: number of output partitions
part: array of size nvtxs to store the result partitions

@example call
    int xadj[] = {0, 2, 4, 6, 8, 10, 12, 14, 17, 21, 25, 28};
    int adjncy[] = {
        7, 9, // 0
        7, 9, // 1
        8, 9, // 2
        8, 10, // 3
        8, 10, // 4
        8, 10, // 5
        7, 9,  // 6
        0, 1, 6,
        2, 3, 4, 5,
        0, 1, 2, 6,
        3, 4, 5
    };
    int part[11];    
    int res = partition(11, xadj, adjncy, 2, part);
*/
int partition(int nvtxs, int xadj[], int adjncy[], int nparts, int part[]){
    int ncon = 1;
    int objval;
    return METIS_PartGraphKway(&nvtxs, &ncon, xadj, adjncy, 
        NULL, NULL, NULL, &nparts, NULL, NULL, NULL, &objval, part);
}

