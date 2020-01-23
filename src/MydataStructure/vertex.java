package MydataStructure;


import java.io.Serializable;

import MydataStructure.node_data;
import utils.Point3D;

public class vertex implements node_data,Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1589796957066318516L;
	int key;
	Point3D location;
	double weight;
	String metadata;
	int tag;

	public vertex()
	{
		this.key = 0;
		this.location = null;
		this.weight = 0;
		this.metadata = "";
		this.tag = 0;
	}

	public vertex(int key) {
		this.key = key;
		this.location = null;
		this.weight = 0;
		this.metadata = "";
		this.tag = 0;
	}
	public vertex(int key,int x,int y)
	{
		this.key = key;
		this.location = new Point3D(x, y);
		this.weight= 0;
		this.metadata = "";
		this.tag = 0;
	}
	public vertex(int key,Point3D location) {
		this.key = key;
		if(location != null)
		{
			this.location = new Point3D(location);
		}
		else
		{
			this.location = null;
		}
		this.weight = 0;
		this.metadata = "";
		this.tag = 0;
	}
	public vertex(int key,Point3D location,double weight) {
		this.key = key;
		if(location != null)
		{
			this.location = new Point3D(location);
		}
		else
		{
			this.location = null;
		}
		this.weight = weight;
		this.metadata = "";
	}
	public vertex(int key,Point3D location,double weight,int tag) {
		this.key = key;
		if(location != null)
		{
			this.location = new Point3D(location);
		}
		else
		{
			this.location = null;
		}
		this.weight = weight;
		this.tag = tag;
		this.metadata = "";
	}
	public vertex(vertex other)
	{
		this(other.key,other.location,other.weight,other.tag);
		this.metadata = other.metadata;

	}
	public vertex(node_data other)
	{
		this(other.getKey(),other.getLocation(),other.getWeight(),other.getTag());
		this.metadata = other.getInfo();

	}
	@Override
	public int getKey() {
		return this.key;
	}

	@Override
	public Point3D getLocation() {
		return this.location;
	}

	@Override
	public void setLocation(Point3D p) {
		////// Check if i need to change weight later
		this.location  = new Point3D(p);
	}

	@Override
	public double getWeight() {
		return this.weight;
	}

	@Override
	public void setWeight(double w) {
		this.weight = w;

	}

	@Override
	public String getInfo() {
		return this.metadata;
	}

	@Override
	public void setInfo(String s) {
		this.metadata = s;

	}

	@Override
	public int getTag() {
		return tag;
	}

	@Override
	public void setTag(int t) {
		this.tag = t;

	}
	public String toString() {
		return ("key: " + key + " location: " + location + " weight: "
				+ weight + " info: " + metadata + " tag: " + tag);
	}
}
