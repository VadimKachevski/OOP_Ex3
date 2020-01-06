package MydataStructure;

import java.io.Serializable;

import MydataStructure.edge_data;

public class edges implements edge_data,Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7222420243279380576L;
	int srcVertex;
	int destVertx;
	double weight;
	String metadata;
	int tag;
	
	
	public edges()
	{
		this.srcVertex = 0;
		this.destVertx = 0;
		this.weight = 0;
		this.metadata = "";
		this.tag = 0;
	}
	public edges(int srcVertex,int destVerex,double weight) {
		this.srcVertex = srcVertex;
		this.destVertx = destVerex;
		this.weight = weight;
		this.tag = 0;
		
	}
	public edges(int srcVertex,int destVerex,double weight,int tag) {
		this.srcVertex = srcVertex;
		this.destVertx = destVerex;
		this.weight = weight;
		this.tag = tag;
	}
	public edges(edges other)
	{
		this(other.srcVertex,other.destVertx,other.weight,other.tag);
		this.metadata = other.metadata;
	}
	@Override
	public int getSrc() {
		return srcVertex;
	}

	@Override
	public int getDest() {
		return destVertx;
	}

	@Override
	public double getWeight() {
		return weight;
	}

	@Override
	public String getInfo() {
		return metadata;
	}

	@Override
	public void setInfo(String s) {
		this.metadata = s;

	}

	@Override
	public int getTag() {
		return this.tag;
	}

	@Override
	public void setTag(int t) {
		this.tag = t;
	}

}
