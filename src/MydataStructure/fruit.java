package MydataStructure;

import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONObject;


import utils.Point3D;

public class fruit implements fruitInterface {
	static double EPS = 0.0000001;
	graph g;
	edge_data edge;
	Point3D pos;
	int value;
	int type;
	boolean occupied=false; 

	public fruit() {
		this.g = null;
		this.edge=null;
		this.pos=null;
		this.value = 0;
		this.type = -1;
	}
	public fruit(graph g)
	{
		this.g = g;
		this.edge=null;
		this.pos=null;
		this.value = 0;
		this.type = -1;
	}
	public fruit(edge_data edge,Point3D pos,int val,int type,graph g)
	{
		this.g = g;
		this.edge = new edges(edge);
		this.pos = new Point3D(pos);
		this.value = val;
		this.type = type;
	}
	@Override
	public void setOccupied(boolean occupied)
	{
		this.occupied = occupied;
	}
	@Override
	public boolean getOccupied()
	{
		return this.occupied;
	}
	@Override
	public void setGraph(graph g)
	{
		this.g = g;
	}
	@Override
	public void setType(int type)
	{
		this.type = type;
	}
	@Override
	public int getType()
	{
		return type;
	}
	@Override
	public edge_data getEdge()
	{
		return edge;
	}
	@Override
	public void setEdge(edge_data edge)
	{
		this.edge = new edges(edge);
	}
	@Override
	public Point3D getPos() {
		return pos;
	}
	@Override
	public void setPos(Point3D pos) {
		this.pos = new Point3D(pos);
	}
	@Override
	public int getValue() {
		return value;
	}
	@Override
	public void setValue(int value) {
		this.value = value;
	}
	@Override
	public void initFromJson(String json)
	{
		if(!json.isEmpty())
		{
			try
			{
				JSONObject obj = new JSONObject(json);
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
				findEdge();
				//			}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	private void findEdge()
	{
		if(g!=null)
		{
			Collection<node_data> nd = g.getV();
			for (node_data node_data : nd) {
				Collection<edge_data> ed = g.getE(node_data.getKey());
				for (edge_data edges : ed) {
					int src = edges.getSrc();
					int dest = edges.getDest();
					if( (src-dest) * this.type < 0 )
					{
						node_data srcN = g.getNode(src);
						node_data destN = g.getNode(dest);
						Point3D srcD = srcN.getLocation();
						Point3D destD = destN.getLocation();
						double allDist = srcD.distance2D(destD);
						double srcToPos = srcD.distance2D(pos);
						double posToDest = pos.distance2D(destD);
						if(Math.abs(allDist - (srcToPos + posToDest)) <= EPS)
						{
							this.edge = edges;
							return;
						}
					}
				}
			}
		}
	}
}
