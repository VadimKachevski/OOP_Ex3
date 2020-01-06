package MydataStructure;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import MydataStructure.edge_data;
import MydataStructure.graph;
import MydataStructure.node_data;
import utils.Point3D;

public class myDGraph implements graph,Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 568702754958880042L;
	int countEdges = 0;
	int MC = 0;
	Hashtable<Integer, node_data> vertexs;
	// src					DEST
	Hashtable<node_data, Hashtable<Integer, edge_data>> edgesPerVertex;


	public myDGraph() {
		vertexs = new Hashtable<Integer, node_data>();
		edgesPerVertex = new Hashtable<node_data, Hashtable<Integer,edge_data>>();
		countEdges = 0;
		MC = 0;
	}

	@Override
	public node_data getNode(int key) {
		//MC++;
		return vertexs.get(key);

	}

	@Override
	public edge_data getEdge(int src, int dest) {
		//MC++;
		node_data vert = vertexs.get(src);
		edge_data edg = edgesPerVertex.get(vert).get(dest);
		return edg;
	}

	@Override
	public void addNode(node_data n) {
		if(n!= null) {
            MC++;
            vertexs.put( n.getKey( ), n );
            edgesPerVertex.put( n, new Hashtable<Integer, edge_data>( ) );
        }
	}

	@Override
	public void connect(int src, int dest, double w) {
		if(src == dest)
		{
			return;
		}
		if(w < 0 )
		{
			throw new RuntimeException( "Weight cant be negative" );
		}
		MC++;
		node_data vert = vertexs.get(src);
		edgesPerVertex.get(vert).put(dest, new edges(src, dest, w));
		countEdges++;
	}

	@Override
	public Collection<node_data> getV() {
		return vertexs.values();
	}

	@Override
	public Collection<edge_data> getE(int node_id) {
		node_data vert = vertexs.get(node_id);
		return edgesPerVertex.get(vert).values();
	}

	@Override
	public node_data removeNode(int key) {
		MC++;
		node_data vert = vertexs.get(key);
		Set<node_data> sets = edgesPerVertex.keySet();
		for (node_data node_data : sets) {
			edge_data edg = edgesPerVertex.get(node_data).remove(key);
			if(edg != null)
			{
				countEdges--;
			}
		}
		countEdges -= edgesPerVertex.get(vert).size();
		edgesPerVertex.remove(vert);
		return vertexs.remove(key);

	}

	@Override
	public edge_data removeEdge(int src, int dest) {
		//MC++;
		node_data vert = vertexs.get(src);
		edge_data ans =  edgesPerVertex.get(vert).remove(dest);
		if(ans!= null){
			countEdges--;
			MC++;
		}
		return ans;
	}

	@Override
	public int nodeSize() {
		return vertexs.size();
	}

	@Override
	public int edgeSize() {
		return countEdges;
	}

	@Override
	public int getMC() {
		return MC;
	}
	
	public void init(String Json)
	{
		try {

			JSONObject obj = new JSONObject(Json);
		JSONArray nodes = obj.getJSONArray("Nodes");
		JSONArray edges = obj.getJSONArray("Edges");
		for (int i = 0; i < nodes.length(); i++) {
			JSONObject CurrNode = (JSONObject)nodes.get(i);
			String pos = CurrNode.getString("pos");
			//System.out.println(pos);
			int id = CurrNode.getInt("id");
			//System.out.println(id);
			//int dest = CurrNode.getInt("dest");
			String[] arr = pos.split(",");
			double x = Double.parseDouble(arr[0]);
			double y = Double.parseDouble(arr[1]);
			double z = Double.parseDouble(arr[2]);
			node_data n = new vertex(id, new Point3D(x, y, z));
			this.addNode(n);
			
		}
		for (int i = 0; i < edges.length(); i++) {
			JSONObject CurrNode = (JSONObject)edges.get(i);
			int src = CurrNode.getInt("src");
			double w = CurrNode.getDouble("w");
			int dest = CurrNode.getInt("dest");
			this.connect(src, dest, w);
		}
		
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
