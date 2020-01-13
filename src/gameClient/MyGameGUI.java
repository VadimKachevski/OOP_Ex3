package gameClient;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

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
import utils.StdDrawGraphGUI;
import utils.StdDraw_gameGUI;

public class MyGameGUI  {

	graph graph;
	Hashtable<Point3D,fruit> fruits;
	Hashtable<Integer,bot> bots;
	double minx=Integer.MAX_VALUE;
	double maxx=Integer.MIN_VALUE;
	double miny=Integer.MAX_VALUE;
	double maxy=Integer.MIN_VALUE;
	Thread t;
	Thread r;
	double x=0;
	double y=0;
	game_service game;
	boolean isBotChooser = true;
	int botidtoMove;

	public MyGameGUI(graph g)  {
		// TODO Auto-generated constructor stub
		this.graph = g;
		//this.GA.init(g);
		this.fruits = new Hashtable<Point3D, fruit>();
		this.bots = new Hashtable<Integer, bot>();
		initGUI();
	}
	public MyGameGUI() {
		// TODO Auto-generated constructor stub
		graph = new myDGraph();
		//Play_manual();
		//GA.init(graph);
		this.fruits = new Hashtable<Point3D, fruit>();
		this.bots = new Hashtable<Integer, bot>();
		//Play_manual();
		initGUI();
	}

	public void setXY(double xpos,double ypos)
	{
		this.x= xpos;
		this.y = ypos;
	}
	public void ThreadPaint(game_service game)
	{

		t = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(game.isRunning())
				{
					if(graph!=null)
					{
						try {
							Thread.sleep(50);
//							Iterator<String> f_iter = game.getFruits().iterator();
//							fruits.clear();
//							if(f_iter.hasNext())
//							{
//								while(f_iter.hasNext())
//								{
//									String json = f_iter.next();
//									fruit n = new fruit(graph);
//									n.initFromJson(json);
//									fruits.put(n.getPos(),n);
//								}
//
//							}
//							//bots.clear();
//							List<String> botsStr = game.getRobots();
//							for (String string : botsStr) {
//								bot ber = new bot();
//								ber.setGrap(graph);
//								ber.botFromJSON(string);
//
//								bots.put(ber.getId(), ber);
//							}
							paint();
						}

					 catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			t.interrupt();
		}
	});
		t.start();
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
		StdDraw_gameGUI.show();
		paint();

	}
}

public void paint()
{
	StdDraw_gameGUI.clear();
	if(graph!= null)
	{
		Collection<node_data> nodes = graph.getV();
		for (node_data node_data : nodes) {
			StdDraw_gameGUI.setPenColor(Color.BLUE);
			Point3D p = node_data.getLocation();
			if(p!=null)
			{
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
					StdDraw_gameGUI.setPenRadius();
				}
			}
		}
		if(!fruits.isEmpty())
		{
			Set<Point3D> sets = fruits.keySet();
			for (Point3D point3d : sets) {
				fruit currF = fruits.get(point3d);
				if(currF.getType() == 1)
				{
					StdDraw_gameGUI.setPenColor(Color.GREEN);
				}
				else
				{
					StdDraw_gameGUI.setPenColor(Color.MAGENTA);
				}
				StdDraw_gameGUI.filledCircle(point3d.x(), point3d.y(),(maxx-minx)*0.006	);
			}
		}
		if(!bots.isEmpty())
		{
			Set<Integer> botset = bots.keySet();
			for (Integer integer : botset) {
				bot currB = bots.get(integer);
				Point3D p = currB.getPos();
				StdDraw_gameGUI.setPenColor(Color.BLACK);
				StdDraw_gameGUI.filledCircle(p.x(), p.y(), (maxx-minx)*0.006);
			}
		}
		
	}
	StdDraw_gameGUI.show();
	//		StdDraw_gameGUI.pause(30);
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
public void Play_manual(String fromS)
{
	try
	{
		int number = Integer.parseInt(fromS);
		if(number>=0 && number<=23)
		{
			gameInit(number);
			playSolo(game);				

		}
		else
		{
			JFrame jinput = new JFrame();
			JOptionPane.showMessageDialog(jinput,"Bad input");
			jinput.dispose();
		}
	}
	catch (Exception e) {
		// TODO: handle exception
		e.printStackTrace();
	}


}
private  int nextNode(graph g, int src) {
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


private void gameInit(int gameNum)
{
	StdDraw_gameGUI.clear();
	StdDraw_gameGUI.show();
	
	try
	{
		t = null;
		if(game != null)
		{
			if(game.isRunning())
			{
				game.stopGame();
			}
		}
		graph = null;
		fruits = null;
		bots = null;
		minx=Integer.MAX_VALUE;
		maxx=Integer.MIN_VALUE;
		miny=Integer.MAX_VALUE;
		maxy=Integer.MIN_VALUE;
		game = Game_Server.getServer(gameNum);
		x=0;
		y=0;
		isBotChooser = true;
		botidtoMove=0;
		//CurrGame = game;
		String g = game.getGraph();
		myDGraph gg = new myDGraph();
		gg.init(g);
		this.graph = gg;
		Iterator<String> f_iter = game.getFruits().iterator();
		if(fruits!= null)
		{
			fruits.clear();
		}
		else
		{
			fruits = new Hashtable<Point3D, fruit>();
		}
		while(f_iter.hasNext())
		{
			String json = f_iter.next();
			fruit n = new fruit(graph);
			n.initFromJson(json);
			//n.setGraph(graph);
			fruits.put(n.getPos(),n);
			//Point3D p = n.getPos();
		}

		String gameString = game.toString();
		JSONObject obj = new JSONObject(gameString);
		JSONObject CurrGame = (JSONObject) obj.get("GameServer");
		int amountRob = CurrGame.getInt("robots");
		int counter = 0;
		if(bots != null)
		{
			bots.clear();
		}
		else
		{
			bots = new Hashtable<Integer, bot>();
		}
		Set<Point3D> setForFuritLoc = fruits.keySet();
		for (Point3D point3d : setForFuritLoc) {
			if(counter>= amountRob)
			{
				break;
			}
			fruit CurrFruit = fruits.get(point3d);
			int RobPos = CurrFruit.getEdge().getSrc();
			game.addRobot(RobPos);
			counter++;
		}
		while(counter < amountRob)
		{
			int pos = (int)(Math.random()*graph.nodeSize());
			game.addRobot(pos);
			counter++;
		}
		List<String> strBots = game.getRobots();
		for (String string : strBots) {
			bot b = new bot();
			b.setGrap(gg);
			b.botFromJSON(string);

			bots.put(b.getId(), b);
		}

		initGUI();
		StdDraw_gameGUI.pause(30);
	}
	catch (Exception e) {
		e.printStackTrace();
	}
	//ThreadMouse(game);


}


private void playSolo(game_service game)
{
	game.startGame();
	//ThreadPaint(game);
	//ThreadMouse(game);
	while(game.isRunning()) {
		//initGUI();
		moveRobotsManual(game);
	}
	String results = game.toString();
	System.out.println("Game Over: "+results);


}
private void moveRobotsManual(game_service game)
{
	List<String> log = game.move();
	if(log!=null)
	{
		long t = game.timeToEnd();
		int dest = nextNodeManual(game);
		if(dest!= -1)
		{
			bot b = bots.get(botidtoMove);
			if(b!=null)
			{
				
				System.out.println(botidtoMove);
				System.out.println(b.getPos().toString());
				game.chooseNextEdge(b.getId(), dest);
				game.move();

			}
		}
		Iterator<String> f_iter = game.getFruits().iterator();
		fruits.clear();
		if(f_iter.hasNext())
		{
			while(f_iter.hasNext())
			{
				String json = f_iter.next();
				fruit n = new fruit(graph);
				n.initFromJson(json);
				fruits.put(n.getPos(),n);
			}

		}
		//bots.clear();
		List<String> botsStr = game.getRobots();
		for (String string : botsStr) {
			bot ber = new bot();
			ber.setGrap(graph);
			ber.botFromJSON(string);

			bots.put(ber.getId(), ber);
		}
		paint();
	}
}
private int nextNodeManual(game_service game)
{
	if(isBotChooser)
	{
		Set<Integer> sss =bots.keySet();
		for (Integer integer : sss) {
			bot b = bots.get(integer);
			Point3D p = b.getPos();
			double dist = p.distance2D(new Point3D(x, y));
			if(dist <= (maxx-minx)*0.006)
			{
				//System.out.println("x " + x + " y " + y);
				x = 0;
				y = 0;
				botidtoMove = integer;
				//System.out.println(b.getId());
				isBotChooser= false;
				System.out.println("Bot is choosen " + botidtoMove);
				return -1;
			}
		}
		return -1;

	}
	else
	{
		Collection<node_data> nd = graph.getV();
		for (node_data node_data : nd) {
			Point3D p = node_data.getLocation();
			double dist = p.distance2D(new Point3D(x, y));
			if(dist <= (maxx-minx)*0.005)
			{
				x = 0;
				y = 0;
				node_data src = bots.get(botidtoMove).getCurrNode();

				Collection<edge_data> cE = graph.getE(src.getKey());
				for (edge_data edgess : cE) {
					if(edgess.getDest() == node_data.getKey())
					{
						isBotChooser = true;
						System.out.println("Node is choosen "+ node_data.getKey());
						return node_data.getKey();
					}
				}
				return -1;
				//					game.chooseNextEdge(botidtoMove, node_data.getKey());

			}
		}
		return -1;
	}
}

private  void moveRobots(game_service game, graph gg) {
	List<String> log = game.move();
	if(log!=null) {
		long t = game.timeToEnd();
		for(int i=0;i<log.size();i++) {
			String robot_json = log.get(i);
			try {

				JSONObject line = new JSONObject(robot_json);
				JSONObject ttt = line.getJSONObject("Robot");
				int rid = ttt.getInt("id");
				int src = ttt.getInt("src");
				int dest = ttt.getInt("dest");

				if(dest==-1) {	
					dest = nextNode(gg, src);
					game.chooseNextEdge(rid, dest);
					bot b=null;
					for (int j = 0; j < bots.size(); j++) {
						if(bots.get(j).getId() == rid)
						{
							b = bots.get(j);
							break;
						}
					}
					Iterator<String> f_iter = game.getFruits().iterator();
					fruits.clear();
					if(f_iter.hasNext())
					{

						while(f_iter.hasNext())
						{
							String json = f_iter.next();
							fruit n = new fruit(graph);
							n.initFromJson(json);
							//	n.setGraph(graph);
							fruits.put(n.getPos(),n);
							//Point3D p = n.getPos();
						}

					}
					if(b!=null)
					{
						b.setPos(graph.getNode(src).getLocation());
					}
					System.out.println("Turn to node: "+dest+"  time to end:"+(t/1000));
					System.out.println(ttt);
				}
			} 
			catch (JSONException e) {e.printStackTrace();}
		}
	}
}



public void Play_Automaticly(String S)
{
	try
	{
		int number = Integer.parseInt(S);
		if(number>=0 && number<=23)
		{
			gameInit(number);
			playAuto(game);				

		}
		else
		{
			JFrame jinput = new JFrame();
			JOptionPane.showMessageDialog(jinput,"Bad input");
			jinput.dispose();
		}
	}
	catch (Exception e) {
		// TODO: handle exception
		e.printStackTrace();
	}
}
private void playAuto(game_service game2) {
	game.startGame();
	//ThreadPaint(game);
	//ThreadMouse(game);
	while(game.isRunning()) {
		//initGUI();
		//System.out.println(game.toString());
		List<String> tre = game.move();
		for (String string : tre) {
			System.out.println(string);
		}
		moveRobotsAuto(game);
	}
	String results = game.toString();
	System.out.println("Game Over: "+results);
	
}
private void moveRobotsAuto(game_service game) {
	List<String> log = game.move();
	if(log!=null)
	{
		long t = game.timeToEnd();
		//int dest = nextNodeManual(game);
		ArrayList<bot> botsToMove = new ArrayList<bot>();
		ArrayList<fruit> fruitsWithoutBots = new ArrayList<fruit>();
		Set<Integer> botS = bots.keySet();
		for (Integer integer : botS) {
			bot b = bots.get(integer);
			if(b.getPath() == null)
			{
				botsToMove.add(b);
			}
		}
		Set<Point3D> fruitSet = fruits.keySet();
		for (Point3D point3d : fruitSet) {
			fruit currF = fruits.get(point3d);
			if(!currF.getOccupied())
			{
				fruitsWithoutBots.add(currF);
			}
		}
		Graph_Algo GA = new Graph_Algo(graph);
		while(!botsToMove.isEmpty() && !fruitsWithoutBots.isEmpty())
		{
			int srcIndex = 0;
			bot SrcFrom=null;
			fruit DestTo=null;
			int destIndex =0;
			double dist = Integer.MAX_VALUE;
			for(int i=0;i< botsToMove.size();i++)
			{
				//double distTemp = Integer.MAX_VALUE;
				for(int j=0;j<fruitsWithoutBots.size();j++)
				{
					if(GA.shortestPathDist(botsToMove.get(i).getCurrNode().getKey(), fruitsWithoutBots.get(j).getEdge().getSrc()) + fruitsWithoutBots.get(j).getEdge().getWeight() < dist)
					{
						srcIndex = i;
						destIndex = j;
						SrcFrom = botsToMove.get(i);
						DestTo = fruitsWithoutBots.get(j);
						dist = GA.shortestPathDist(botsToMove.get(i).getCurrNode().getKey(), fruitsWithoutBots.get(j).getEdge().getSrc()) + fruitsWithoutBots.get(j).getEdge().getWeight();
					}
				}
			}
			List<node_data> path = GA.shortestPath(SrcFrom.getCurrNode().getKey(), DestTo.getEdge().getSrc());
			path.add(graph.getNode(DestTo.getEdge().getDest()));
			path.remove(0);
			SrcFrom.setPath(path);
			botsToMove.remove(srcIndex);
			DestTo.setOccupied(true);
			fruitsWithoutBots.remove(destIndex);
		}
		
		
		
		for (Integer integer : botS) {
			bot b = bots.get(integer);
			if(b.getPath() != null)
			{
				
				if(b.getCurrNode().getLocation().distance2D(b.getPos())<= 0.00001)
				{
					
					//System.out.println(botidtoMove);
					//System.out.println(b.getPos().toString());
					List<node_data> path = b.getPath();
					//System.out.println(b.getId() + " " +path.get(0).getKey());
					game.chooseNextEdge(b.getId(), path.get(0).getKey());
					b.setCurrNode(path.get(0));
					path.remove(0);
					if(path.size() == 0)
					{
						b.setPath(null);
					}
					game.move();
				}
			}
		}
		
		
	}
	Iterator<String> f_iter = game.getFruits().iterator();
	fruits.clear();
	if(f_iter.hasNext())
	{
		while(f_iter.hasNext())
		{
			String json = f_iter.next();
			fruit n = new fruit(graph);
			n.initFromJson(json);
			//System.out.println(n.getEdge().getSrc() + " " + n.getEdge().getDest());
			fruits.put(n.getPos(),n);
		}

	}
	//bots.clear();
	List<String> botsStr = game.getRobots();
	for (String string : botsStr) {
		bot ber = new bot();
		ber.setGrap(graph);
		ber.botFromJSON(string);

		bots.put(ber.getId(), ber);
	}
	paint();

}
public void play(game_service game,myDGraph gg)
{
	////
	//paint();
	game.startGame();
	int i=0;
	while(game.isRunning()) {
		//initGUI();
		moveRobots(game, gg);
	}
	String results = game.toString();
	System.out.println("Game Over: "+results);
}
public void stop() {
	// TODO Auto-generated method stub

}





}
