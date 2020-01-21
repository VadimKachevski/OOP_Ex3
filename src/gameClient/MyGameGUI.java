package gameClient;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.sql.ResultSet;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

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
	dbConnector dbCon;
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
	int val=0;
	int moveM=0;
	game_service game;
	boolean isBotChooser = true;
	int botidtoMove;
	KML_Logger k;

	/**
	 * constructor - receives a graph
	 * @param g
	 */
	public MyGameGUI(graph g)  {
		this.graph = g;
		this.fruits = new Hashtable<Point3D, fruitInterface>();
		this.bots = new Hashtable<Integer, robotInterface>();
		initGUI();
	}
	/**
	 * default constructor
	 */
	public MyGameGUI() {

		//graph = new myDGraph();
		//this.fruits = new Hashtable<Point3D, fruitInterface>();
		//		this.bots = new Hashtable<Integer, robotInterface>();
		//defaultCont = StdDraw_gameGUI.getJFrame().getContentPane();
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
	 * Setting canvas size, enabling double buffering, building a graph,
	 *  building the KML file base and setting the scale.
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
		else
		{
			minx = 0;
			miny = 0;
			maxx = 600;
			maxy = 800;
			StdDraw_gameGUI.setXscale(minx, maxx);
			StdDraw_gameGUI.setYscale(miny,maxy);
			StdDraw_gameGUI.setG_GUI(this);
			StdDraw_gameGUI.show();
		}

	}
	/**
	 * This method visually paints the graph, Bots & Fruits 
	 */
	public void paint()
	{
		StdDraw_gameGUI.clear();
		StdDraw_gameGUI.setPenColor(Color.black);
		StdDraw_gameGUI.text(maxx, maxy, "Seconds:"+timeGame);
		StdDraw_gameGUI.text(maxx, maxy-(maxy-miny)*0.03, "Value: " +val);
		StdDraw_gameGUI.text(maxx, maxy-(maxy-miny)*0.06, "Moves: " +moveM);
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
						StdDraw_gameGUI.picture(point3d.x(), point3d.y(),"images/kiwi.png");
					}
					else
					{
						StdDraw_gameGUI.picture(point3d.x(), point3d.y(),"images/strawberry.png");
					}
				}
			}
			if(!bots.isEmpty())
			{
				Set<Integer> botset = bots.keySet();
				for (Integer integer : botset) {
					robotInterface currB = bots.get(integer);
					Point3D p = currB.getPos();
					StdDraw_gameGUI.picture(p.x(), p.y(),"images/robot3.png");
				}
			}
		}
		StdDraw_gameGUI.show();
	}
	/**
	 * This method receives a string  and initiates an object from the type
	 * of manuelGame and activates it
	 * @param fromS
	 */
	public void Play_manual(String fromS)
	{
		if(game != null && game.isRunning())
		{
			JFrame jinput = new JFrame();
			JOptionPane.showMessageDialog(jinput,"Game is allready running please wait untill the end");
			jinput.dispose();
		}
		else
		{
			manualGame mG = new manualGame(this);
			mG.play(fromS);
		}
	}
	/**
	 * This method initiates the game by clearing the game and 
	 * then adding all of the game elements again (fruis bots & graph)
	 * @param gameNum
	 */
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
			ArrayList<fruitInterface> testSmarter = new ArrayList<fruitInterface>();
			for (int i = 0; i < fruitSmart.size(); i++) {
				for (int j = i+1; j < fruitSmart.size(); j++) {
					int iSrc = fruitSmart.get(i).getEdge().getSrc();
					int iDest = fruitSmart.get(i).getEdge().getDest();
					int jSrc = fruitSmart.get(j).getEdge().getSrc();
					int jDest = fruitSmart.get(j).getEdge().getDest();
					if(iSrc == jSrc || iSrc == jDest || iDest == jSrc || iDest == jDest)
					{
						testSmarter.add(fruitSmart.get(i));
					}
				}
			}
			//int runner=0;
			while(!testSmarter.isEmpty() && counter < amountRob)
			{
				//System.out.println("FOUND ONE");
				game.addRobot(testSmarter.get(0).getEdge().getSrc());
				System.out.println(testSmarter.get(0).getEdge().getSrc());
				testSmarter.remove(0);
				counter++;
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
				//System.out.println("adding random shit");
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
	/**
	 * This method receives a string initiates an object from the 
	 * type of manuelGame and activates it
	 * @param S
	 */
	public void Play_Automaticly(String S)
	{
		if(game!=null && game.isRunning())
		{
			JFrame jinput = new JFrame();
			JOptionPane.showMessageDialog(jinput,"Game is allready running please wait untill the end");
			jinput.dispose();
		}
		else
		{
			autoGame aG = new autoGame(this);
			aG.play(S);
		}
	}
	public void Init_Scores() {
		int id = 321711061;
		try {
		JFrame jinput = new JFrame();
		String fromS = JOptionPane.showInputDialog(jinput,"Enter ID or Leave blank for default");
		jinput.dispose();
		
		id = Integer.parseInt(fromS);
		}
		catch (Exception e) {
			
		}
		
		// TODO Auto-generated method stub
		StdDraw_gameGUI.setFont(new Font("TimeRoman", Font.BOLD,15 ));
		int[] levelInfo = dbConnector.getNumbergames(id);
		StdDraw_gameGUI.text(maxx/2, maxy, "ID:"+id);
		StdDraw_gameGUI.text(maxx/2, maxy-35,"Current Game : "+ levelInfo[1] +" Total amount of games: " + levelInfo[0]);
		
		StdDraw_gameGUI.text(minx, maxy-85, "Games");
		StdDraw_gameGUI.text(minx+125, maxy-85, "Best result");
		StdDraw_gameGUI.text(minx+225, maxy-85, "Score goal");
		StdDraw_gameGUI.text(minx+300, maxy-85, "Moves");
		StdDraw_gameGUI.text(minx+375, maxy-85, "Max moves");
		StdDraw_gameGUI.text(minx+450, maxy-85, "Rank");
		int scale = 135;
		double[][] bestRes = dbConnector.bestResult(id);
		int rank[] = dbConnector.findRank(bestRes);
		for (int i = 0; i <= 11; i++) {
			if (i != 10) {
				int game = whichStage(i);
				StdDraw_gameGUI.text(minx, maxy-scale, "Game " + game + " - Case " + i);
				StdDraw_gameGUI.text(minx+125, maxy-scale, "Best resualt: "+ bestRes[i][0]);
				StdDraw_gameGUI.text(minx+225, maxy-scale, bestRes[i][1]+"");
				StdDraw_gameGUI.text(minx+300, maxy-scale, bestRes[i][2]+"");
				StdDraw_gameGUI.text(minx+375, maxy-scale, bestRes[i][3]+"");
				StdDraw_gameGUI.text(minx+450, maxy-scale, rank[i]+1+"");
				scale += 50;
			}		
		}


		StdDraw_gameGUI.show();
	}

	public int whichStage(int index) {
		switch (index) {
		case 0 : 
			return 0;
		case 1 : 
			return 1;
		case 2 : 
			return 3;
		case 3 : 
			return 5;
		case 4 : 
			return 9;
		case 5 : 
			return 11;
		case 6 : 
			return 13;
		case 7 : 
			return 16;
		case 8 : 
			return 19;
		case 9 : 
			return 20;
		case 11 : 
			return 23;
		default :
			return -1;
		}
	}



}
