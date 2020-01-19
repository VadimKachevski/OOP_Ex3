package gameClient;

import java.awt.Color;
import java.io.File;
import java.time.LocalTime;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
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
import MydataStructure.fruitInterface;
import MydataStructure.graph;
import MydataStructure.myDGraph;
import MydataStructure.node_data;
import MydataStructure.robotInterface;
import Server.Game_Server;
import Server.game_service;
import utils.Point3D;
import utils.StdDraw_gameGUI;

public class MyGameGUI  {

	graph graph;
	Hashtable<Point3D,fruitInterface> fruits;
	Hashtable<Integer,robotInterface> bots;
	double minx=Integer.MAX_VALUE;
	double maxx=Integer.MIN_VALUE;
	double miny=Integer.MAX_VALUE;
	double maxy=Integer.MIN_VALUE;
	Thread t;
	Thread r;
	double x=0;
	double y=0;
	double timeGame=0;
	game_service game;
	boolean isBotChooser = true;
	int botidtoMove;
	KML_Logger k;


	public MyGameGUI(graph g)  {
		// TODO Auto-generated constructor stub
		this.graph = g;
		this.fruits = new Hashtable<Point3D, fruitInterface>();
		this.bots = new Hashtable<Integer, robotInterface>();
		initGUI();
	}
	public MyGameGUI() {

		// TODO Auto-generated constructor stub
		graph = new myDGraph();
		this.fruits = new Hashtable<Point3D, fruitInterface>();
		this.bots = new Hashtable<Integer, robotInterface>();
		initGUI();
	}
	
	/**
	 * Sets the X and Y position of the mouse on click
	 * @param xpos
	 * @param ypos
	 */
	public void setXY(double xpos,double ypos)
	{
		this.x= xpos;
		this.y = ypos;
	}
	/**
	 * A KML thread to save the game status in a KML file every 100 miliseconds
	 */
	public void ThreadKML()
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
							long timeToSleep = 100;
							Thread.sleep(timeToSleep);
							String Starttime  = java.time.LocalDate.now()+"T"+java.time.LocalTime.now();
							LocalTime test = LocalTime.now();
							//test = test.plusSeconds(timeToSleep/1000);
							test= test.plusNanos(timeToSleep*1000000);
							String endTime = java.time.LocalDate.now()+"T"+test;
							k.setFruits(Starttime,endTime);
							k.setBots(Starttime,endTime);
						}

						catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				t.interrupt();
			}
		});
		t.start();
	}
	
	/**
	 * Initiates the GUI and preparing it work with a given graph
	 * Setting canvas size, enabling double buffering, building a graph, building the KML file base and setting the scale.
	 */
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
			k=new KML_Logger(graph);
			k.BuildGraph();
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
	
	/**
	 * 
	 */
	public void paint()
	{
		StdDraw_gameGUI.clear();
		StdDraw_gameGUI.text(maxx, maxy, "Seconds:"+timeGame);
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
					fruitInterface currF = fruits.get(point3d);
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
					robotInterface currB = bots.get(integer);
					Point3D p = currB.getPos();
					StdDraw_gameGUI.setPenColor(Color.BLACK);
					StdDraw_gameGUI.filledCircle(p.x(), p.y(), (maxx-minx)*0.006);
				}
			}

		}
		StdDraw_gameGUI.show();
	}
	public void Play_manual(String fromS)
	{
		manualGame mG = new manualGame(this);
		mG.play(fromS);
	}
	public void gameInit(int gameNum)
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
				fruits = new Hashtable<Point3D, fruitInterface>();
			}
			while(f_iter.hasNext())
			{
				String json = f_iter.next();
				fruit n = new fruit(graph);
				n.initFromJson(json);
				fruits.put(n.getPos(),n);
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
				bots = new Hashtable<Integer, robotInterface>();
			}
			ArrayList<fruitInterface> fruitSmart = new ArrayList<fruitInterface>();
			Set<Point3D> setForFuritLoc = fruits.keySet();
			for (Point3D point3d : setForFuritLoc) 
			{
				fruitInterface CurrFruit = fruits.get(point3d);
				fruitSmart.add(CurrFruit);
			}
			while(!fruitSmart.isEmpty() && counter < amountRob) // TO fix later better AI with Weight
			{
				int Max = Integer.MIN_VALUE;
				fruitInterface maxFruit=null;
				int fruitID= 0;
				for(int i=0;i<fruitSmart.size();i++)
				{
					fruitInterface theFruit = fruitSmart.get(i);
					if(theFruit.getValue() > Max) 
					{
						Max = theFruit.getValue();
						maxFruit = theFruit;
						fruitID = i;
					}
				}
				int RobPos = maxFruit.getEdge().getSrc();
				game.addRobot(RobPos);
				counter++;
				fruitSmart.remove(fruitID);
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
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void Play_Automaticly(String S)
	{
		autoGame aG = new autoGame(this);
		aG.play(S);
	}
}
