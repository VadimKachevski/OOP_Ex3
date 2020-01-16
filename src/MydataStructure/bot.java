package MydataStructure;

import java.util.List;

import org.json.JSONObject;

import utils.Point3D;

public class bot implements robotInterface{

	int id;
	node_data currNode;
	int money;
	Point3D pos;
	double speed;
	graph gg;
	List<node_data> path;
	
	
	public bot() {
		this.id = -1;
		this.gg = null;
		this.currNode = null;
		this.money = -1;
		this.pos = null;
		this.speed = -1;
	}
	public bot(int id,node_data currNode,int money,Point3D pos,double speed,graph gg)
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
		this.money = money;
		this.pos = new Point3D(pos);
		this.speed = speed;
	}
	@Override
	public void setPath(List<node_data> path)
	{
		this.path = path;
	}
	@Override
	public List<node_data> getPath()
	{
		return this.path;
	}
	@Override
	public void setGrap(graph g)
	{
		this.gg = g;
	}
	@Override
	public int getId() {
		return id;
	}
	@Override
	public node_data getCurrNode() {
		return currNode;
	}
	@Override
	public void setCurrNode(node_data currNode) {
		this.currNode = currNode;
	}
	@Override
	public int getMoney() {
		return money;
	}
	@Override
	public void setMoney(int money) {
		this.money = money;
	}
	@Override
	public Point3D getPos() {
		return pos;
	}
	@Override
	public void setPos(Point3D pos) {
		this.pos = pos;
	}
	@Override
	public double getSpeed() {
		return speed;
	}
	@Override
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	@Override
	public void botFromJSON(String json)
	{
		if(!json.isEmpty())
		{
			try
			{
				JSONObject obj = new JSONObject(json);
				JSONObject CurrBot = (JSONObject) obj.get("Robot");
				String pos = CurrBot.getString("pos");
				String[] arr = pos.split(",");
				double x = Double.parseDouble(arr[0]);
				double y = Double.parseDouble(arr[1]);
				double z = Double.parseDouble(arr[2]);
				this.pos = new Point3D(x, y, z);
				int id = CurrBot.getInt("id");
				this.id = id;
				int value = CurrBot.getInt("value");
				this.money = value;
				int speed = CurrBot.getInt("speed");
				this.speed = speed;
				if(this.gg != null)
				{
					int src = CurrBot.getInt("src");
					this.currNode = gg.getNode(src);
				}
				
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}