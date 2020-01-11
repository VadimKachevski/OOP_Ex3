package MydataStructure;

import org.json.JSONObject;

import utils.Point3D;

public class bot {

	int id;
	node_data currNode;
	edge_data currEdge;
	int money;
	Point3D pos;
	double speed;
	graph gg;
	
	
	
	public bot() {
		this.id = -1;
		this.gg = null;
		this.currEdge = null;
		this.currNode = null;
		this.money = -1;
		this.pos = null;
		this.speed = -1;
	}
	public bot(int id,node_data currNode,edge_data currEdge,int money,Point3D pos,double speed,graph gg)
	{
		if(gg != null)
		{
			this.gg = gg;
		}
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
	
	public void setGrap(graph g)
	{
		this.gg = g;
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
	public void botFromJSON(String json)
	{
		if(!json.isEmpty())
		{
			try
			{
				JSONObject obj = new JSONObject(json);
				JSONObject CurrFruit = (JSONObject) obj.get("Robot");
				String pos = CurrFruit.getString("pos");
				String[] arr = pos.split(",");
				double x = Double.parseDouble(arr[0]);
				double y = Double.parseDouble(arr[1]);
				double z = Double.parseDouble(arr[2]);
				this.pos = new Point3D(x, y, z);
				int id = CurrFruit.getInt("id");
				this.id = id;
				int value = CurrFruit.getInt("value");
				this.money = value;
				int speed = CurrFruit.getInt("speed");
				this.speed = speed;
				
			
				
				if(this.gg != null)
				{
					int src = CurrFruit.getInt("src");
					this.currNode = gg.getNode(src);
				}
				
			}
			catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
	

}

