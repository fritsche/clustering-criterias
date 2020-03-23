import matplotlib.pyplot as plt
import numpy as np

path = "../../experimentResults_V1/Experiment0/data/"

dataset_pool = ["D31"]
# moea_pool = ["HypE", "NSGAII", "MOEADD", "MOEAD", "MOEADSTM", "MOMBI2", "IBEA", "NSGAIII", "SPEA2", "SPEA2SDE", "ThetaDEA"]
moea_pool = ["NSGAII","NSGAII_M2","MOCK","MOCKb"]
# fmts = iter(['1','4','3','x','-o','-o','-o','-o','d','>','p', 'D'])
fmts = iter(['-o',' o','-o','-o', 'x','-o','-o','-o','-o','d','>','p', 'D'])
z = []
w = []
a = []
b = []


# # used for ploting each front
# # for dataset in dataset_pool:
# #     for moea in moea_pool:
# #         # x,y = np.genfromtxt(path+dataset+'_'+moea+'.30', delimiter=' ', unpack=True)
# #         with open(path+dataset+'_'+moea+'.30') as f:
# #             content = f.readlines()
# #             content = [x.strip() for x in content]
# #             for i in range(len(content)):
# #                 if(content[i]!=''):
# #                     x,y=np.array(content[i].split(' '),dtype=float)
# #                     z.append(x)
# #                     w.append(y)
# #                 else:
# #                     a.append(z)
# #                     b.append(w)
# #                     z = []
# #                     w = []
# #used to plot each front
# # plt.plot(a[0],b[0], next(fmts),label=moea, alpha=0.5)
# # plt.plot(a[1],b[1], next(fmts),label=moea, alpha=0.5)
# # plt.plot(np.hstack(a),np.hstack(b),'o')

a,b = np.genfromtxt(path+dataset_pool[0]+'_'+moea_pool[0]+'.30', delimiter=' ', unpack=True)
c,d = np.genfromtxt(path+dataset_pool[0]+'_'+moea_pool[1]+'.30', delimiter=' ', unpack=True)
e,f = np.genfromtxt(path+dataset_pool[0]+'_'+moea_pool[2]+'.30', delimiter=' ', unpack=True)
g,h = np.genfromtxt(path+dataset_pool[0]+'_'+moea_pool[3]+'.30', delimiter=' ', unpack=True)
# i,j = np.genfromtxt(path+dataset_pool[0]+'_'+moea_pool[4]+'.30', delimiter=' ', unpack=True)
# k,l = np.genfromtxt(path+dataset_pool[0]+'_'+moea_pool[5]+'.30', delimiter=' ', unpack=True)
# m,n = np.genfromtxt(path+dataset_pool[0]+'_'+moea_pool[6]+'.30', delimiter=' ', unpack=True)
# o,p = np.genfromtxt(path+dataset_pool[0]+'_'+moea_pool[7]+'.30', delimiter=' ', unpack=True)
# q,r = np.genfromtxt(path+dataset_pool[0]+'_'+moea_pool[8]+'.30', delimiter=' ', unpack=True)
# s,t = np.genfromtxt(path+dataset_pool[0]+'_'+moea_pool[9]+'.30', delimiter=' ', unpack=True)
# u,v = np.genfromtxt(path+dataset_pool[0]+'_'+moea_pool[10]+'.30', delimiter=' ', unpack=True)
x,z = np.genfromtxt("./zz_figuras/"+dataset_pool[0]+'_TP.tsv', delimiter=' ', unpack=True)
x1,z1 = np.genfromtxt("./zz_figuras/"+dataset_pool[0]+'_KM.tsv', delimiter=' ', unpack=True)
x2,z2 = np.genfromtxt("./zz_figuras/"+dataset_pool[0]+'_WL.tsv', delimiter=' ', unpack=True)
x3,z3 = np.genfromtxt("./zz_figuras/"+dataset_pool[0]+'_SNN.tsv', delimiter=' ', unpack=True)
x4,z4 = np.genfromtxt("./zz_figuras/"+dataset_pool[0]+'_HDBSCAN.tsv', delimiter=' ', unpack=True)


#used to normalize ---------------------------------------------------------
max_dev = np.max(np.hstack((a,c,e,g,x,x1,x2,x3,x4)))
min_dev = np.min(np.hstack((a,c,e,g,x,x1,x2,x3,x4)))

max_conn = np.max(np.hstack((b,d,f,h,z,z1,z2,z3,z4)))
min_conn = np.min(np.hstack((b,d,f,h,z,z1,z2,z3,z4)))


x = (x-min_dev)/(max_dev-min_dev)
x1 = (x1-min_dev)/(max_dev-min_dev)
x2 = (x2-min_dev)/(max_dev-min_dev)
x3 = (x3-min_dev)/(max_dev-min_dev)
x4 = (x4-min_dev)/(max_dev-min_dev)
a = (a-min_dev)/(max_dev-min_dev)
c = (c-min_dev)/(max_dev-min_dev)
e = (e-min_dev)/(max_dev-min_dev)
g = (g-min_dev)/(max_dev-min_dev)


z = (z-min_conn)/(max_conn-min_conn)
z1 = (z1-min_conn)/(max_conn-min_conn)
z2 = (z2-min_conn)/(max_conn-min_conn)
z3 = (z3-min_conn)/(max_conn-min_conn)
z4 = (z4-min_conn)/(max_conn-min_conn)
b = (b-min_conn)/(max_conn-min_conn)
d = (d-min_conn)/(max_conn-min_conn)
f = (f-min_conn)/(max_conn-min_conn)
h = (h-min_conn)/(max_conn-min_conn)
#-----------------------------------------------------------------------------

#used to sort print order for a better visualization
a,b= zip( *sorted( zip(a, b) ) )
c,d= zip( *sorted( zip(c, d) ) )
e,f= zip( *sorted( zip(e, f) ) )
g,h= zip( *sorted( zip(g, h) ) )


x1,z1= zip( *sorted( zip(x1, z1) ) )
x2,z2= zip( *sorted( zip(x2, z2) ) )
x3,z3= zip( *sorted( zip(x3, z3) ) )
x4,z4= zip( *sorted( zip(x4, z4) ) )

# plt.legend()
# plt.show()


"""
Enable picking on the legend to toggle the original line on and off
"""


fig, ax = plt.subplots(figsize=(12, 8))

line1, = ax.plot(a, b, next(fmts), label=moea_pool[0], markersize=3, drawstyle='steps-post')
line2, = ax.plot(c, d, next(fmts), label=moea_pool[1], markersize=3, drawstyle='steps-post')
line3, = ax.plot(e, f, next(fmts), label=moea_pool[2], markersize=3, drawstyle='steps-post')
line4, = ax.plot(g, h, next(fmts), label=moea_pool[3], markersize=3, drawstyle='steps-post')
# line4, = ax.plot(g, h, next(fmts), label=moea_pool[3])
# line5, = ax.plot(i, j, next(fmts), label=moea_pool[4])
# line6, = ax.plot(k, l, next(fmts), label=moea_pool[5])
# line7, = ax.plot(m, n, next(fmts), label=moea_pool[6])
# line8, = ax.plot(o, p, next(fmts), label=moea_pool[7])
# line9, = ax.plot(q, r, next(fmts), label=moea_pool[8])
# line10, = ax.plot(s, t, next(fmts), label=moea_pool[9])
# line11, = ax.plot(u, v, next(fmts), label=moea_pool[10])
line12, = ax.plot(x, z, next(fmts), label="TRUE PARTITION", markersize=15)
line13, = ax.plot(x1, z1, next(fmts), label="KMeans", markersize=12, mfc='none')
line14, = ax.plot(x2, z2, next(fmts), label="Ward link", markersize=11, mfc='none')
line15, = ax.plot(x3, z3, next(fmts), label="SNN", markersize=10, mfc='none')
line16, = ax.plot(x4, z4, next(fmts), label="HDBSCAN", markersize=9, mfc='none')

leg = ax.legend(loc='upper right', fancybox=True, shadow=True)
leg.get_frame().set_alpha(0.8)


# we will set up a dict mapping legend line to orig line, and enable
# picking on the legend line
# lines = [line1, line2, line3, line4, line5, line6, line7, line8, line9, line10, line11, line12]
lines = [line1, line2, line3, line4, line12, line13, line14, line15, line16]


lined = dict()
for legline, origline in zip(leg.get_texts(), lines):
    legline.set_picker(5)  # 5 pts tolerance
    lined[legline] = origline


def onpick(event):
    # on the pick event, find the orig line corresponding to the
    # legend proxy line, and toggle the visibility
    legline = event.artist
    origline = lined[legline]
    print(origline)
    vis = not origline.get_visible()
    origline.set_visible(vis)
    # Change the alpha on the line in the legend so we can see what lines
    # have been toggled
    if vis:
        legline.set_alpha(1.0)
    else:
        legline.set_alpha(0.2)
    fig.canvas.draw()

fig.canvas.mpl_connect('pick_event', onpick)

plt.title(dataset_pool[0])
plt.xlabel('deviation')
plt.ylabel('connectivity')
plt.show()