package gameClient;

import java.util.Collection;

import MydataStructure.graph;
import MydataStructure.node_data;
import utils.Point3D;
import utils.StdDraw_gameGUI;

public class MyGameGUI {

	graph graph;
	double minx=Integer.MAX_VALUE;
	double maxx=Integer.MIN_VALUE;
	double miny=Integer.MAX_VALUE;
	double maxy=Integer.MIN_VALUE;
	
	public MyGameGUI(graph g) {
		// TODO Auto-generated constructor stub
		this.graph = g;
		//this.GA.init(g);
		initGUI();
	}
	public MyGameGUI() {
		// TODO Auto-generated constructor stub
		graph = null;
		//GA.init(graph);
		initGUI();
	}
	private void initGUI()
	{
		StdDraw_gameGUI.setCanvasSize(800, 600);
		
		if(graph != null)
		{
			Collection<node_data> nd = graph.getV();
			for (node_data node_data : nd) {
				Point3D s = node_data.getLocation();
				if(s.ix() < minx)
				{
					minx = s.ix();
				}
				if(s.ix() > maxx)
				{
					maxx = s.ix();
				}
				if(s.iy() > maxy)
				{
					maxy = s.iy();
				}
				if(s.iy() < miny)
				{
					miny = s.iy();
				}
			}
			StdDraw_gameGUI.setXscale(minx-(minx/10), maxx+(maxx/10));
			StdDraw_gameGUI.setYscale(miny-(miny/10),maxy+(maxy/10));
			StdDraw_gameGUI.setG_GUI(this);
			paint();
		
		}
	}
}
