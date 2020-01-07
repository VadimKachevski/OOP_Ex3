package gameClient;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileSystemView;

import MydataStructure.bot;
import MydataStructure.edge_data;
import MydataStructure.fruit;
import MydataStructure.graph;
import MydataStructure.myDGraph;
import MydataStructure.node_data;
import Server.Game_Server;
import Server.game_service;
import algorithms.Graph_Algo;
import utils.Point3D;
import utils.StdDrawGraphGUI;
import utils.StdDraw_gameGUI;

public class MyGameGUI {

	graph graph;
	ArrayList<fruit> fruits;
	ArrayList<bot> bots;
	double minx=Integer.MAX_VALUE;
	double maxx=Integer.MIN_VALUE;
	double miny=Integer.MAX_VALUE;
	double maxy=Integer.MIN_VALUE;

	public MyGameGUI(graph g) {
		// TODO Auto-generated constructor stub
		this.graph = g;
		//this.GA.init(g);
		this.fruits = new ArrayList<fruit>();
		initGUI();
	}
	public MyGameGUI() {
		// TODO Auto-generated constructor stub
		graph = new myDGraph();
		//Play_manual();
		//GA.init(graph);
		this.fruits = new ArrayList<fruit>();
		initGUI();
	}






	private void initGUI()
	{

		if(StdDraw_gameGUI.getDrawed() == false)
		{
			StdDraw_gameGUI.setCanvasSize(800, 600);
			StdDraw_gameGUI.setDrawed();
		}

		if(graph != null)
		{
			Collection<node_data> nd = graph.getV();
			for (node_data node_data : nd) {
				Point3D s = node_data.getLocation();
				if(s.x() < minx)
				{
					minx = s.x();
				}
				if(s.x() > maxx)
				{
					maxx = s.x();
				}
				if(s.y() > maxy)
				{
					maxy = s.y();
				}
				if(s.y() < miny)
				{
					miny = s.y();
				}
			}

			//double rangex = maxx-minx;
			//double rangey = maxy-miny;
			//System.out.println(rangex);
			//System.out.println(rangey);
			StdDraw_gameGUI.setXscale(minx, maxx);
			StdDraw_gameGUI.setYscale(miny,maxy);
			StdDraw_gameGUI.setG_GUI(this);
			paint();
			//StdDraw_gameGUI.show();
		}
	}


	public void paint()
	{
		StdDraw_gameGUI.clear();
		Collection<node_data> nodes = graph.getV();
		for (node_data node_data : nodes) {
			StdDraw_gameGUI.setPenColor(Color.BLUE);
			Point3D p = node_data.getLocation();
			if(p!=null)
			{
				//StdDraw_gameGUI.filledEllipse(p.x(), p.y(), semiMajorAxis, semiMinorAxis);
				StdDraw_gameGUI.filledCircle(p.x(), p.y(),(maxx-minx)*0.005	);
				StdDraw_gameGUI.text(p.x(), p.y()+(maxy-miny)*0.03, node_data.getKey()+"");
				Collection<edge_data> edges = graph.getE(node_data.getKey());
				for (edge_data edge : edges) {
					if(edge.getTag() == 999)
					{
						edge.setTag(0);
						StdDraw_gameGUI.setPenRadius(0.0008);
						StdDraw_gameGUI.setPenColor(Color.GREEN);
					}
					else
					{
						StdDraw_gameGUI.setPenColor(Color.RED);
					}
					node_data dest = graph.getNode(edge.getDest());
					Point3D pd = dest.getLocation();
					if(pd!=null)
					{
						StdDraw_gameGUI.line(p.x(), p.y(), pd.x(), pd.y());
						double xhalf = (((p.x()+pd.x())/2)+pd.x())/2;
						double yhalf = (((p.y()+pd.y())/2)+pd.y())/2;
						String weg = edge.getWeight()+"";
						int dotIndex = weg.indexOf('.');
						weg=weg.substring(0, dotIndex+2);
						StdDraw_gameGUI.text((xhalf+pd.x())/2, ((yhalf+pd.y())/2)+(maxy-miny)*0.03 ,weg);
						StdDraw_gameGUI.setPenColor(Color.YELLOW);

						StdDraw_gameGUI.filledCircle((xhalf+pd.x())/2, (yhalf+pd.y())/2, (maxx-minx)*0.004);
					}
					//g2d.setStroke(bs);
					StdDraw_gameGUI.setPenRadius();
				}
			}
		}
		if(!fruits.isEmpty())
		{
			for (int i = 0; i < fruits.size(); i++) {
				fruit currF = fruits.get(i);
				Point3D p = currF.getPos();
				
			}
		}
		//StdDraw_gameGUI.show();
	}





	public void DrawFromFile() {
		Graph_Algo ga = new Graph_Algo();
		JFileChooser jf = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		int returnV = jf.showOpenDialog(null);
		if (returnV == JFileChooser.APPROVE_OPTION) {
			File selectedFile = jf.getSelectedFile();
			ga.init(selectedFile.getAbsolutePath());
			this.graph = ga.copy();
			paint();
		}
	}
	/**
	 * Saving the current graph into a unreable deserializable file.
	 */
	public void saveToFile()
	{
		String sb = "TEST CONTENT";
		Graph_Algo ga = new Graph_Algo();
		ga.init(graph);
		JFileChooser jf = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		int returnV = jf.showOpenDialog(null);
		if (returnV == JFileChooser.APPROVE_OPTION) {
			try {
				ga.save(jf.getSelectedFile()+".txt");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	/**
	 * Asks the user a From and a TO and tries to re-create the shortest path
	 * will mark the path in green if a path is possible else nothing will happen
	 */
	public void shortestPath()
	{
		JFrame jinput = new JFrame();
		String fromS = JOptionPane.showInputDialog(jinput,"Enter From");		
		String to = JOptionPane.showInputDialog(jinput,"Enter To");
		fromS = fromS.trim();
		to = to.trim();
		try
		{
			int fromN = Integer.parseInt(fromS);
			int toN = Integer.parseInt(to);
			Graph_Algo ga = new Graph_Algo();
			ga.init(graph);
			List<node_data> ans = ga.shortestPath(fromN, toN);
			for (int j = 0; j < ans.size()-1; j++) {
				graph.getEdge(ans.get(j).getKey(), ans.get(j+1).getKey()).setTag(999);
			}
			paint();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Asks the user a From and a TO and tries to re-create the shortest path
	 * Will show the shortest path length in a Message dialog
	 */
	public void ShortestPathDist()
	{
		JFrame jinput = new JFrame();
		String fromS = JOptionPane.showInputDialog(jinput,"Enter From");		
		String to = JOptionPane.showInputDialog(jinput,"Enter To");
		try
		{
			int fromN = Integer.parseInt(fromS);
			int toN = Integer.parseInt(to);
			Graph_Algo ga = new Graph_Algo();
			ga.init(graph);
			double x = ga.shortestPathDist(fromN, toN);
			JOptionPane.showMessageDialog(jinput, "the shortest distance is:" + x);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Asks the user through which nodes he should pass when the user is done 
	 * he need to write DONE and it will try to create the relative shortest path through
	 * all the nodes
	 */
	public void TSPEnterNodes()
	{
		JFrame jinput = new JFrame();
		JOptionPane.showMessageDialog(jinput, "To get TSP enter all the nodes from start node to end node \n after you are done enter DONE");
		ArrayList<Integer> arrayTSP = new ArrayList<Integer>();
		String ans;
		do {
			ans = JOptionPane.showInputDialog(jinput, "Enter node or DONE when it is the last node");
			if(ans.equalsIgnoreCase("done"))
			{
				break;
			}
			try
			{
				arrayTSP.add(Integer.parseInt(ans));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		} while (!ans.equalsIgnoreCase("done"));
		Graph_Algo ga = new Graph_Algo();
		ga.init(graph);
		List<node_data> ansTSP = ga.TSP(arrayTSP);
		for (int j = 0;j < ansTSP.size()-1; j++) {
			graph.getEdge(ansTSP.get(j).getKey(), ansTSP.get(j+1).getKey()).setTag(999);
		}
		paint();
	}
	/**
	 * with a given file in the format x,y,z,t,y...  will try to create the shortest path 
	 * through all the nodes in the file
	 */
	public void TSPfromFile()
	{
		Graph_Algo ga = new Graph_Algo();
		JFileChooser jf = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		int returnV = jf.showOpenDialog(null);
		String ans;
		if (returnV == JFileChooser.APPROVE_OPTION) {
			File selectedFile = jf.getSelectedFile();
			try
			{
				Scanner sc = new Scanner(selectedFile);
				if(sc.hasNextLine())
				{
					ans = sc.nextLine();
					String[] seg = ans.split(",");
					List<Integer> ListNodes = new ArrayList<Integer>();
					for (int i = 0; i < seg.length; i++) {
						seg[i] = seg[i].trim();
						ListNodes.add(Integer.parseInt(seg[i]));
					}
					ga.init(graph);
					List<node_data> ansTSP = ga.TSP(ListNodes);
					for (int j = 0;j < ansTSP.size()-1; j++) {
						graph.getEdge(ansTSP.get(j).getKey(), ansTSP.get(j+1).getKey()).setTag(999);
					}
					paint();
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public void Play_manual()
	{
		game_service game = Game_Server.getServer(1);
		String g = game.getGraph();
		myDGraph gg = new myDGraph();
		gg.init(g);
		this.graph = gg;
		Iterator<String> f_iter = game.getFruits().iterator();
		if(f_iter.hasNext())
		{
			fruits = new ArrayList<fruit>();
			while(f_iter.hasNext())
			{
				String json = f_iter.next();
				fruit n = new fruit();
				n.initFromJson(json);
				fruits.add(n);
				//Point3D p = n.getPos();

			}
			
		}
		initGUI();


	}
	public void Play_Automaticly()
	{

	}
	public void play()
	{

	}
	public void stop()
	{

	}
}
