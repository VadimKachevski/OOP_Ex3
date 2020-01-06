package algorithms;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import MydataStructure.*;
import MydataStructure.myDGraph;
import MydataStructure.edge_data;
import MydataStructure.graph;
import MydataStructure.node_data;
/**
 * This empty class represents the set of graph-theory algorithms
 * which should be implemented as part of Ex2 - Do edit this class.
 * @author 
 *
 */
public class Graph_Algo implements graph_algorithms,Serializable{



	/**
	 * 
	 */
	private static final long serialVersionUID = -6198391308540997339L;
	graph graph;


	public Graph_Algo(graph g)
	{
		this.graph = g;
	}


	public Graph_Algo() {
		
		this.graph = new myDGraph();
	}

	@Override
	public void init(graph g) {
		this.graph =g;

	}
	@Override
	public void init(String file_name) {
		// TODO Auto-generated method stub
		try
		{
			FileInputStream f = new FileInputStream(file_name);
			ObjectInputStream o = new ObjectInputStream(f);
			//o.writeObject(this.graph);
			Graph_Algo ee = (Graph_Algo)o.readObject();
			this.graph = ee.graph;
			o.close();
			f.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void save(String file_name) {

		try
		{
			FileOutputStream f = new FileOutputStream(file_name);
			ObjectOutputStream o = new ObjectOutputStream(f);
			o.writeObject(this);
			o.close();
			f.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Counts the amount of nodes this node can get too
	 * @param n
	 * @return
	 */
	private int amountNodes(node_data n)
	{
		int counter=0;
		ArrayList<node_data> nd = new ArrayList<node_data>();
		nd.add(n);
		while(!nd.isEmpty())
		{
			node_data temp = nd.get(0);
			if(temp.getTag()==0)
			{
				temp.setTag(1);
				counter++;
				nd.remove(0);
				Collection<edge_data> edge = graph.getE(temp.getKey());

				for (edge_data edge_data : edge) {
					node_data other = graph.getNode(edge_data.getDest());
					if(other.getTag() == 0)
					{
						nd.add(0,other);
					}
				}
			}
			else
			{
				nd.remove(0);
			}
		}
		return counter;
	}
	// Should be faster then the org algo
	@Override
	public boolean isConnected()
	{
		HashSet<node_data> allreayConnected = new HashSet<node_data>();
		Collection<node_data> s = graph.getV();
		boolean firstNode = true;
		node_data firstN=null;
		boolean ansBool=true;
		for (node_data node_data : s) {
			cleanTags();
			if(firstNode)
			{
				firstN=node_data;
				firstNode= false;
				int ans = amountNodes(node_data);
				if(ans != s.size())
				{
					return false;
				}
			}
			else
			{
				if( !canOthersGeToThis( node_data, firstN, allreayConnected ) )
				{
					return false;
				}
			}
		}
		return ansBool;
	}
	/**
	 * Iterates over all the nodes and checks if there any way to get to the first node
	 * @param n
	 * @param first
	 * @param allreadtconnected
	 * @return
	 */
	private boolean canOthersGeToThis(node_data n,node_data first,HashSet<node_data> allreadtconnected)
	{
		if(n.getKey() == first.getKey())
		{
			allreadtconnected.add(n);
			return true;
		}
		ArrayList<node_data> nodesToCheck = new ArrayList<node_data>();
		nodesToCheck.add(n);
		while(!nodesToCheck.isEmpty())
		{
			node_data currN = nodesToCheck.remove(0);
			if(currN.getTag() == 0)
			{
				currN.setTag(1);
				Collection<edge_data> edge = graph.getE(currN.getKey());
				for (edge_data edge_data : edge) {
					node_data dest = graph.getNode(edge_data.getDest());
					if(first.getKey() == edge_data.getDest() || allreadtconnected.contains(dest))
					{
						allreadtconnected.add(currN);
						return true;
					}
					else {
						if(dest.getTag() == 0)
						{
							nodesToCheck.add(0,dest);
						}
					}
				}
			}
		}
		return false;
	}
	/**
	 * Sets tag to 0 on all nodes
	 */
	private void cleanTags()
	{
		Collection<node_data> s = graph.getV();
		for (node_data node_data : s) {
			node_data.setTag(0);
		}
	}
	/**
	 * sets tag to 0 and weight and MAX_VALUE on all nodes
	 */
	private void cleanTagsSetweight()
	{
		Collection<node_data> s = graph.getV();
		for (node_data node_data : s) {
			node_data.setTag(0);
			node_data.setWeight(Integer.MAX_VALUE);
			node_data.setInfo("");
		}
	}
	/**
	 * Dijkstra algorithm from source 
	 * @param src
	 */
	private void Dijksta(int src)
	{
		cleanTagsSetweight();
		ArrayList<node_data> Mins = new ArrayList<node_data>();
		Mins.add(graph.getNode(src));
		Mins.get(0).setWeight(0);
		while(!Mins.isEmpty())
		{
			node_data currNode = Mins.get(0);
			if(currNode.getTag() ==0)
			{
				currNode.setTag(1);
				Mins.remove(0);
				Collection<edge_data> edges = graph.getE(currNode.getKey());
				for (edge_data edge_data : edges) {
					node_data destNode = graph.getNode(edge_data.getDest());
					if(destNode.getWeight() > currNode.getWeight()+edge_data.getWeight())
					{
						destNode.setWeight(currNode.getWeight()+edge_data.getWeight());
						destNode.setInfo(currNode.getKey()+"");
						if(destNode.getTag() == 0)
						{

							Mins.add(getPosInArray(Mins, destNode.getWeight()),destNode);
						}
					}
				}
			}
			else
			{
				Mins.remove(0);
			}
		}
	}
	/**
	 * get the insert position in sorted array
	 * @param Mins
	 * @param destNodeW
	 * @return
	 */
	private int getPosInArray(ArrayList<node_data> Mins,double destNodeW)
	{
		int minIndex = 0;
		int maxIndex = Mins.size()-1;
		int middle = minIndex + (maxIndex-minIndex)/2;

		while(minIndex <= maxIndex)
		{
			if(Mins.get(middle).getWeight() == destNodeW)
			{
				return middle;
			}
			if(destNodeW > Mins.get(middle).getWeight())
			{
				minIndex = middle +1;
			}
			if(destNodeW < Mins.get(middle).getWeight())
			{
				maxIndex = middle -1;
			}
			middle = minIndex+(maxIndex-minIndex)/2;
		}
		return middle;
	}

	@Override
	public double shortestPathDist(int src, int dest) {

		Dijksta(src);
		return graph.getNode(dest).getWeight();
	}
	@Override
	public List<node_data> shortestPath(int src, int dest) {
		Dijksta(src);
		List<node_data> ans = new ArrayList<node_data>();
		node_data currNode = graph.getNode(dest);
		while(!currNode.getInfo().isEmpty())
		{
			ans.add(0, currNode);
			currNode = graph.getNode(Integer.parseInt(currNode.getInfo()));
		}
		ans.add(0, currNode);
		return ans;
	}
	/**
	 * Special shortest path for TSP algorithm
	 * @param src
	 * @param dest
	 * @param wasNotThere
	 * @return
	 */
	private List<node_data> shortestPathForTSP(int src, int dest,HashSet<Integer> wasNotThere) {
		List<node_data> ans = new ArrayList<node_data>();
		Dijksta(src);
		node_data currNode = graph.getNode(dest);
		while(!currNode.getInfo().isEmpty())
		{
			ans.add(0, currNode);
			currNode = graph.getNode(Integer.parseInt(currNode.getInfo()));
			if(wasNotThere.contains(currNode.getKey()))
			{
				wasNotThere.remove(currNode.getKey());
			}
		}
		ans.add(0, currNode);
		return ans;
	}
	@Override
	public List<node_data> TSP(List<Integer> targets)
	{
		List<node_data> ans = new ArrayList<node_data>();
		if(targets.size() == 0)
		{
			return ans;
		}
		if(targets.size() == 1)
		{
			ans.add(graph.getNode(targets.get(0)));
			return ans;
		}
		HashSet<Integer> wasNotThere = new HashSet<Integer>();
		for (Integer integer : targets) {
			wasNotThere.add(integer);
		}
		int dest = 1;
		int from = 0;
		boolean firstRun = true;
		while(dest<targets.size())
		{
			if(wasNotThere.contains(targets.get(from)))
			{
				if(wasNotThere.contains(targets.get(dest)))
				{
					if(firstRun)
					{
						ans.addAll(shortestPathForTSP(targets.get(from), targets.get(dest),wasNotThere));
						firstRun=false;
					}
					else
					{
						List<node_data> pre =shortestPathForTSP(targets.get(from), targets.get(dest),wasNotThere);
						pre.remove(0);
						ans.addAll(pre);
					}
					dest++;
					from++;
				}
				else
				{
					dest++;
				}
			}
			else
			{
				from++;
			}
		}
		return ans;
	}

	@Override
	public graph copy() {
		graph ans = new myDGraph();
		Collection<node_data> nodes = graph.getV();
		for (node_data node_data : nodes) {
			ans.addNode(new vertex(node_data));
			Collection<edge_data> edges = graph.getE(node_data.getKey());
			for (edge_data edge : edges) {
				ans.connect(edge.getSrc(), edge.getDest(), edge.getWeight());
			}
		}
		return ans;
	}

}
