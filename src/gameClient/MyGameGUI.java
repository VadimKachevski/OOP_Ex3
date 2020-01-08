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

import org.json.JSONException;
import org.json.JSONObject;

import MydataStructure.bot;
import MydataStructure.edge_data;
import MydataStructure.fruit;
import MydataStructure.graph;
import MydataStructure.myDGraph;
import MydataStructure.node_data;
import Server.Fruit;
import Server.Game_Server;
import Server.RobotG;
import Server.game_service;
import algorithms.Graph_Algo;
import utils.Point3D;
import utils.StdDraw;
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
		this.bots = new ArrayList<bot>();
		initGUI();
	}
	public MyGameGUI() {
		// TODO Auto-generated constructor stub
		graph = new myDGraph();
		//Play_manual();
		//GA.init(graph);
		this.fruits = new ArrayList<fruit>();
		this.bots = new ArrayList<bot>();
		initGUI();
	}






	private void initGUI()
	{

		if(StdDraw_gameGUI.getDrawed() == false)
		{
			StdDraw_gameGUI.setCanvasSize(800, 600);
			StdDraw_gameGUI.enableDoubleBuffering();
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
				if(currF.getType() == 1)
				{
					StdDraw_gameGUI.setPenColor(Color.GREEN);
				}
				else
				{
					StdDraw_gameGUI.setPenColor(Color.MAGENTA);
				}
				StdDraw_gameGUI.filledCircle(p.x(), p.y(),(maxx-minx)*0.006	);
			}
		}
		if(!bots.isEmpty())
		{
			for (int i = 0; i < bots.size(); i++) {
				bot currB = bots.get(i);
				Point3D p = currB.getPos();
				StdDraw_gameGUI.setPenColor(Color.BLACK);
				StdDraw_gameGUI.filledCircle(p.x(), p.y(), (maxx-minx)*0.006);
			}
		}
		StdDraw_gameGUI.show();
		StdDraw_gameGUI.pause(30);
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
		JFrame jinput = new JFrame();
		String fromS = JOptionPane.showInputDialog(jinput,"Which game to run? 0-23");
		try
		{
			int number = Integer.parseInt(fromS);
			if(number>=0 && number<=23)
			{

				game_service game = Game_Server.getServer(number);
				String g = game.getGraph();
				myDGraph gg = new myDGraph();
				gg.init(g);
				this.graph = gg;
				Iterator<String> f_iter = game.getFruits().iterator();
				fruits = new ArrayList<fruit>();
				if(f_iter.hasNext())
				{
					
					while(f_iter.hasNext())
					{
						String json = f_iter.next();
						fruit n = new fruit();
						n.initFromJson(json);
						fruits.add(n);
						//Point3D p = n.getPos();
					}

				}
				String gameString = game.toString();
				JSONObject obj = new JSONObject(gameString);
				JSONObject CurrGame = (JSONObject) obj.get("GameServer");
				int amountRob = CurrGame.getInt("robots");
				int counter = 0;
				bots = new ArrayList<bot>();
				while(counter < amountRob)
				{
					int fruitsAm = (int)(Math.random()* graph.nodeSize());
					
					bots.add(new bot(counter, graph.getNode(fruitsAm), null, 0, graph.getNode(fruitsAm).getLocation(), 1));
					game.addRobot(fruitsAm);
					counter++;
				}

				initGUI();
				StdDraw_gameGUI.pause(30);
				play(game,gg);				
				
			}
			else
			{
				JOptionPane.showMessageDialog(jinput,"Bad input");
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}


	}
	private int nextNode(graph g, int src) {
		int ans = -1;
		Collection<edge_data> ee = g.getE(src);
		Iterator<edge_data> itr = ee.iterator();
		int s = ee.size();
		int r = (int)(Math.random()*s);
		int i=0;
		while(i<r) {itr.next();i++;}
		ans = itr.next().getDest();
		return ans;
	}

	public void Play_Automaticly()
	{

	}
	public void play(game_service game,myDGraph gg)
	{
		////
		paint();
		game.startGame();
		int i=0;
		while(game.isRunning()) {
			//initGUI();
			
			long t = game.timeToEnd();
			//System.out.println("roung: "+i+"  seconds to end:"+(t/1000));
			List<String> log = game.move();
			if(log!=null) {
				
				String robot_json = log.get(0);
			//	System.out.println(robot_json);
				JSONObject line;
				try {
					line = new JSONObject(robot_json);
					JSONObject ttt = line.getJSONObject("Robot");
					int rid = ttt.getInt("id");
					int src = ttt.getInt("src");
					int dest = ttt.getInt("dest");
					
					if(dest==-1) {	
						dest = nextNode(gg, src);
						game.chooseNextEdge(rid, dest);
						bot b = bots.get(0);
						b.setPos(graph.getNode(dest).getLocation());
						System.out.println("Turn to node: "+dest);
						System.out.println(ttt);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	
				
				}
			i++;
		}
		//ther.stop();
		//////
	}
	public void stop() {
		// TODO Auto-generated method stub
		
	}
	




}
