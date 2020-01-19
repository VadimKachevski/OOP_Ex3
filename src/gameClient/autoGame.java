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
	public autoGame(MyGameGUI mgg) {
		// TODO Auto-generated constructor stub
		this.mgg = mgg;
	}
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
	private void startGame(int numberOfGame) {
		mgg.game.startGame();
		mgg.k.setGame(mgg.game);
		mgg.ThreadKML();
		Long timeTestThread = mgg.game.timeToEnd();
		while(mgg.game.isRunning()) {
			if(timeTestThread - mgg.game.timeToEnd() > 100)
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
		try {
			JFrame jinput = new JFrame();
			JSONObject obj = new JSONObject(results);
			JSONObject CurrRes = (JSONObject) obj.get("GameServer");
			int grade = CurrRes.getInt("grade");
			int moves = CurrRes.getInt("moves");
			JOptionPane.showMessageDialog(jinput,"The game ended \n The score : "+grade +"\n" +"Amount of moves: "+moves );
			jinput.dispose();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
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
