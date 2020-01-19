package gameClient;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.json.JSONObject;

import MydataStructure.bot;
import MydataStructure.fruit;
import MydataStructure.fruitInterface;
import MydataStructure.node_data;
import MydataStructure.robotInterface;
import Server.game_service;
import algorithms.Graph_Algo;
import utils.Point3D;

public class autoGame {

	MyGameGUI mgg;
	/**
	 * a constructor which gets an already build up object of MyGameGui.
	 * @param mgg
	 */
	public autoGame(MyGameGUI mgg) {
		this.mgg = mgg;
	}
	/**
	 * this method receives a string which represents the scenario
	 * of the game (which level the user chose to play) and if it’s a 
	 * valid input it calls the startGame method for running the auto phase.
	 * @param gameNumStr the level which was chosen
	 */
	public void play(String gameNumStr)
	{
		try
		{  
			int number = Integer.parseInt(gameNumStr);
			if(number>=0 && number<=23)
			{
				mgg.gameInit(number);
				startGame(number);				
			}
			else
			{
				JFrame jinput = new JFrame();
				JOptionPane.showMessageDialog(jinput,"Bad input");
				jinput.dispose();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * by a given of a valid number verified by the previous method,
	 * this function starts the game by setting up the server and
	 * starts the KML Thread which uploads to the KML information 
	 * about the amount of moves and grades collected so far.
	 * @param numberOfGame
	 */
	private void startGame(int numberOfGame) {
		mgg.game.startGame();
		mgg.k.setGame(mgg.game);
		mgg.ThreadKML();
		Long timeTestThread = mgg.game.timeToEnd();
		while(mgg.game.isRunning()) {
			if(timeTestThread - mgg.game.timeToEnd() > 70)
			{
				mgg.game.move();
				timeTestThread = mgg.game.timeToEnd();
			}
			try {
			String results = mgg.game.toString();
			JSONObject obj = new JSONObject(results);
			JSONObject CurrRes = (JSONObject) obj.get("GameServer");
			mgg.val = CurrRes.getInt("grade");
			mgg.moveM = CurrRes.getInt("moves");
			}catch (Exception e) {
				e.printStackTrace();
			}
			mgg.timeGame = mgg.game.timeToEnd()/1000;
			move(mgg.game);
		}
		String results = mgg.game.toString();
		mgg.k.saveToFile(""+numberOfGame,results);
		System.out.println("Game Over: "+results);

	}
	/**
	 * by receiving the server (which holds the data and updates it as
	 * the game is running), this algorithm calculates the next step of
	 * the robot and sends the server the information of which Bot to move and to which index.
	 * @param game
	 */
	private void move(game_service game)  
	{
		long t = game.timeToEnd();
		ArrayList<robotInterface> botsToMove = new ArrayList<robotInterface>();
		ArrayList<fruitInterface> fruitsWithoutBots = new ArrayList<fruitInterface>();
		Set<Integer> botS = mgg.bots.keySet();
		for (Integer integer : botS) {
			robotInterface b = mgg.bots.get(integer);
			if(b.getPath() == null)
			{
				botsToMove.add(b);
			}
		}
		Set<Point3D> fruitSet = mgg.fruits.keySet();
		for (Point3D point3d : fruitSet) {
			fruitInterface currF = mgg.fruits.get(point3d);
			if(!currF.getOccupied())
			{
				fruitsWithoutBots.add(currF);
			}
		}
		Graph_Algo GA = new Graph_Algo(mgg.graph);
		while(!botsToMove.isEmpty() && !fruitsWithoutBots.isEmpty())
		{
			int srcIndex = 0;
			robotInterface SrcFrom=null;
			fruitInterface DestTo=null;
			int destIndex =0;
			double dist = Integer.MAX_VALUE;
			for(int i=0;i< botsToMove.size();i++)
			{
				for(int j=0;j<fruitsWithoutBots.size();j++)
				{
					if(GA.shortestPathDist(botsToMove.get(i).getCurrNode().getKey()	, fruitsWithoutBots.get(j).getEdge().getSrc()) + GA.shortestPathDist(fruitsWithoutBots.get(j).getEdge().getSrc()	, fruitsWithoutBots.get(j).getEdge().getDest() ) < dist)
					{
						srcIndex = i;
						destIndex = j;
						SrcFrom = botsToMove.get(i);
						DestTo = fruitsWithoutBots.get(j);
						dist = GA.shortestPathDist(botsToMove.get(i).getCurrNode().getKey()	, fruitsWithoutBots.get(j).getEdge().getSrc()) + GA.shortestPathDist(fruitsWithoutBots.get(j).getEdge().getSrc()	, fruitsWithoutBots.get(j).getEdge().getDest() );
					}
				}
			}
			List<node_data> path = GA.shortestPath(SrcFrom.getCurrNode().getKey(), DestTo.getEdge().getSrc());
			path.add(mgg.graph.getNode(DestTo.getEdge().getDest()));
			path.remove(0);
			SrcFrom.setPath(path);
			botsToMove.remove(srcIndex);
			DestTo.setOccupied(true);
			fruitsWithoutBots.remove(destIndex);
		}
		for (Integer integer : botS)
		{
			robotInterface b = mgg.bots.get(integer);
			if(b.getPath() != null)
			{
				if(b.getCurrNode().getLocation().distance2D(b.getPos())<= 0.000001)
				{
					List<node_data> path = b.getPath();
					game.chooseNextEdge(b.getId(), path.get(0).getKey());
					b.setCurrNode(path.get(0));
					path.remove(0);
					if(path.size() == 0)
					{
						b.setPath(null);
					}
				}
			}
		}
		Iterator<String> f_iter = game.getFruits().iterator();
		mgg.fruits.clear();
		if(f_iter.hasNext())
		{
			while(f_iter.hasNext())
			{
				String json = f_iter.next();
				fruit n = new fruit(mgg.graph);
				n.initFromJson(json);
				mgg.fruits.put(n.getPos(),n);
			}

		}
		List<String> botsStr = game.getRobots();
		for (String string : botsStr) {
			bot ber = new bot();
			ber.setGrap(mgg.graph);
			ber.botFromJSON(string);

			mgg.bots.put(ber.getId(), ber);
		}
		mgg.paint();
	}
}
