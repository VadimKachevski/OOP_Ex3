package MydataStructure;

import org.json.JSONArray;
import org.json.JSONObject;

import utils.Point3D;

public class fruit {
	edge_data edge;
	Point3D pos;
	int value;
	int type;

	public fruit() {
		// TODO Auto-generated constructor stub
		this.edge=null;
		this.pos=null;
		this.value = 0;
		this.type = -1;
	}
	public fruit(edge_data edge,Point3D pos,int val,int type)
	{
		this.edge = new edges(edge);
		this.pos = new Point3D(pos);
		this.value = val;
		this.type = type;
	}
	public void setType(int type)
	{
		this.type = type;
	}
	public int getType()
	{
		return type;
	}
	public edge_data getEdge() {
		return edge;
	}
	public void setEdge(edge_data edge) {
		this.edge = new edges(edge);
	}
	public Point3D getPos() {
		return pos;
	}
	public void setPos(Point3D pos) {
		this.pos = new Point3D(pos);
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public void initFromJson(String json)
	{
		if(!json.isEmpty())
		{
			try
			{
				JSONObject obj = new JSONObject(json);
				//			JSONArray fruits = obj.getJSONArray("Fruit");
				//			for (int i = 0; i < fruits.length(); i++)
				//			{
				//				JSONObject CurrFruit = (JSONObject)fruits.get(i);
				JSONObject CurrFruit = (JSONObject) obj.get("Fruit");
				String pos = CurrFruit.getString("pos");
				String[] arr = pos.split(",");
				double x = Double.parseDouble(arr[0]);
				double y = Double.parseDouble(arr[1]);
				double z = Double.parseDouble(arr[2]);
				this.pos = new Point3D(x, y, z);
				int value = CurrFruit.getInt("value");
				this.value = value;
				int type = CurrFruit.getInt("type");
				this.type = type;
				//			}
			}
			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}

}
