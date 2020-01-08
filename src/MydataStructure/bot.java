package MydataStructure;

import utils.Point3D;

public class bot {

	int id;
	node_data currNode;
	edge_data currEdge;
	int money;
	Point3D pos;
	double speed;
	
	
	
	public bot() {
		this.id = -1;
		this.currEdge = null;
		this.currNode = null;
		this.money = -1;
		this.pos = null;
		this.speed = -1;
	}
	public bot(int id,node_data currNode,edge_data currEdge,int money,Point3D pos,double speed)
	{
		this.id = id;
		if(currNode != null)
		{
		this.currNode = new vertex(currNode);
		}
		if(currEdge != null)
		{
		this.currEdge = new edges(currEdge);
		}
		this.money = money;
		this.pos = new Point3D(pos);
		this.speed = speed;
	}
	
	public int getId() {
		return id;
	}
	public node_data getCurrNode() {
		return currNode;
	}
	public void setCurrNode(node_data currNode) {
		this.currNode = currNode;
	}
	public edge_data getCurrEdge() {
		return currEdge;
	}
	public void setCurrEdge(edge_data currEdge) {
		this.currEdge = currEdge;
	}
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
	}
	public Point3D getPos() {
		return pos;
	}
	public void setPos(Point3D pos) {
		this.pos = pos;
	}
	public double getSpeed() {
		return speed;
	}
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	
	

}

