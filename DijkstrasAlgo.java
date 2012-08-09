public class DijkstrasAlgo {		public int vertices; // the total number of nodes	public int s[][];//permanent nodes,vector;the first dimension is the nodenumber of Origin, the second dimension is number of element	public int vs[][];//remaining nodes,array;first dimension the same, the second dimesnion is the number of node	public double d[][];//generalized cost,array;same with vs	public int pi[][];//predecessor,array; same with vs//	public int TemporaryNode[];//	public int ElementsOfPermanent;	float tollrate;	float lengthrate;	float  speedrate;	float valueofTime;	boolean symmetry;		public DijkstrasAlgo(float vot,  float  trate, float lrate, float srate, DirectedGraph dg, boolean symmetry) {		valueofTime = vot;		tollrate = trate;		lengthrate = lrate;		speedrate = srate;		vertices = dg.Vertices();		this.symmetry =symmetry;	}		public void dijkstrasalgo(DirectedGraph  dgraph) {				//System.out.println("vertices="+vertices);		s=new int[vertices][vertices];		vs=new int[vertices][vertices];		d=new double[vertices][vertices];		pi=new int[vertices][vertices];							float tt, toll;				////   Changing DistMatrix[][] of Graph class		for(int i=0; i<vertices; i++ ) {			for(int j=0; j<dgraph.NoofLinks(i+1); j++ ) {				if(dgraph.Speed(i+1,  j+1)<0.001) dgraph.Speed[i].replace(j,(float)0.001);				//System.out.print(dgraph.Speed[i].access( j)+"\n");				tt = (float)(dgraph.Read_Distance(i+1, dgraph.EndNodeNumbers(i+1, j+1))/dgraph.Speed(i+1,  j+1) );				toll = (float) (tollrate * Math.pow(  dgraph.Read_Distance(i+1, dgraph.EndNodeNumbers(i+1, j+1)), lengthrate   )* Math.pow( dgraph.Speed(i+1,  j+1), speedrate ));				//System.out.print(" toll="+toll);}				//System.out.print("tt="+tt+"\ttoll="+toll+"\n");				dgraph.change_DistMatrix(i+1, j+1, valueofTime*tt+(float) (toll)   );				//System.out.println(" dist="+dgraph.DistMatrix(i+1,j+1 ));				}		}						//System.out.println("Shortest path is calculates according to the matrix given below\n");		//dgraph.printDMatrix();				//finding the shortest path...		//the permanent elements along the shortest path are stored into s vector, element by element		//the status of each node (permanent:-1/available:0/unavailabe:1) are stored in vs		// the generalized cost(distance) from the origin to each node are  stored in d		//the precedecessor of each node on the shortest path is stored in pi				for(int ithNode=0;ithNode<vertices;ithNode++){//given an origin node...						for(int j=0;j<vertices;j++)				{	//initializing..					s[ithNode][j]=-1;vs[ithNode][j]=1;d[ithNode][j]=1.0E25;pi[ithNode][j]=-1;				}				//the first element..							s[ithNode][0]=ithNode+1;			d[ithNode][ithNode]=0;				vs[ithNode][ithNode]=-1;			//System.out.println("test2\n");								for(int jthElement=0;jthElement<vertices-1;jthElement++){				//in each step, we will find the following element of the jth element along the shortest path)				 					int previousNode=s[ithNode][jthElement];					int NoofLinks=dgraph.NoofLinks(previousNode);					for(int kthLink=0;kthLink<NoofLinks;kthLink++){						int EndNodeNumber=dgraph.EndNodeNumbers( previousNode, kthLink+1 );						//relaxing (make the unavailable nodes connected to the recent permanent elment available)...						if(vs[ithNode][EndNodeNumber-1]!=-1)	// if the node has not been labelled permanent							{								if(d[ithNode][EndNodeNumber-1]>d[ithNode][previousNode-1]+dgraph.DistMatrix( previousNode, kthLink+1))									//if the node is unavailabe, the statement will be true for sure									//if the node has already avaible, this statement compared the previous cost and the cost through previous node																		{										//if the cost is smaller through the previous node										//we will change the cost corresponding to the node as well as its predecessor										d[ithNode][EndNodeNumber-1]=d[ithNode][previousNode-1]+dgraph.DistMatrix( previousNode, kthLink+1);							 			pi[ithNode][EndNodeNumber-1]=previousNode;							 			vs[ithNode][EndNodeNumber-1]=0;									}									//System.out.println(EndNodeNumber+"\n"+d[ithNode][EndNodeNumber-1]);							}							 					}						//find the next permanent element					//among all the available nodes,we will find the one with smallest cost and label it as the next permanent				double dtemp=1.0E99;				int nodetemp=-1;													//if(symmetry==false){		  				if(dgraph.speed1==10){							 		for(int j=0;j<vertices;j++){							if(vs[ithNode][j]==0)							{								if(dtemp>d[ithNode][j]){																	dtemp=d[ithNode][j];									nodetemp=j+1;																	}									else if(dtemp==d[ithNode][j])									{										if(j<nodetemp-1)											nodetemp=j+1;																		}							}						}				}					else{					for(int m=0;m<=jthElement;m++){						int startnode=s[ithNode][m];						for(int n=0;n<dgraph.NoofLinks(startnode);n++){							int endnode=dgraph.EndNodeNumbers(startnode,n+1 );							if(vs[ithNode][endnode-1]==0)								{									if(dtemp>d[ithNode][endnode-1]){										dtemp=d[ithNode][endnode-1];										nodetemp=endnode;									}										}						}														}				}						//System.out.println("nodetemp="+nodetemp+"\n");								s[ithNode][jthElement+1]=nodetemp;				vs[ithNode][nodetemp-1]=-1;			}												}//		for (int i=0;i<vertices;i++)//		{		//			System.out.println("i="+i+"\n");//			for(int j=0;j<vertices;j++)//				System.out.println(pLabel(i+1,j+1));//		}				//System.out.println("ElementsOfPermanent="+ElementsOfPermanent+"\n");				//for(int i=0;i<ElementsOfPermanent;i++)				//	System.out.println(i+" "+PermanentVector[0][i][0]+PermanentVector[0][i][1]+"\n");								}				////////////////////////////////			public double pLabel(int node1, int node2) {//return the generanized cost between node1 and node2		double retValue = 0;		if((node1>=1 &&  node1 <=vertices)  && (node2>=1 &&  node2<=vertices) ) {			retValue=this.d[node1-1][node2-1];		}		else 			System.out.println("From class DijkstrasAlgo: Error in node numbers passed to pLabel function, returning zero!!!");		return retValue;				}				}