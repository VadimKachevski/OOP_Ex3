package gameClient;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.json.JSONObject;

import MydataStructure.bot;
import MydataStructure.edge_data;
import MydataStructure.fruit;
import MydataStructure.node_data;
import MydataStructure.robotInterface;
import Server.game_service;
import utils.Point3D;

public class manualGame {

	MyGameGUI mgg;
	/**
	 * a constructor which gets an already build up object
	 * of MyGameGui and saves it as its own.
	 * @param mgg
	 */
	public manualGame(MyGameGUI mgg) {
		this.mgg = mgg;
	}
	/**
	 * this method receives a string which represents the scenario of the
	 *  game (which level the user chose to play) and if it’s a valid input 
	 *  it calls the startGame method for running the auto phase.
	 * @param gameNumStr
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
	 *  this function starts the game by setting up the server and painting the game.
	 *  In addition this method updates the valid position of the
	 *  fruits and Bots as they change while the game is running by the user.
	 * @param numberOfGame
	 */
	private void startGame(int numberOfGame)
	{
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
	/**
	 * This method moves the Bot by using the assistance of the
	 * next method (nextNodeManuel method), and sends it to the
	 * server for updating the data. 
	 */
	private void move(game_service game)
	{
		int dest = nextNodeManual(game);
		if(dest!= -1)
		{
			robotInterface b = mgg.bots.get(mgg.botidtoMove);
			if(b!=null)
			{

				System.out.println(mgg.botidtoMove);
				System.out.println(b.getPos().toString());
				game.chooseNextEdge(b.getId(), dest);
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
	/**
	 * This methos returns the integer which represents the Node that the Bot must 
	 * advanced to.
	 * @param game
	 * @return
	 */
	private int nextNodeManual(game_service game)
	{
		if(mgg.isBotChooser)
		{
			Set<Integer> sss =mgg.bots.keySet();
			for (Integer integer : sss) {
				robotInterface b = mgg.bots.get(integer);
				Point3D p = b.getPos();
				double dist = p.distance2D(new Point3D(mgg.x, mgg.y));
				if(dist <= (mgg.maxx-mgg.minx)*0.008)
				{
					mgg.x = 0;
					mgg.y = 0;
					mgg.botidtoMove = integer;
					mgg.isBotChooser= false;
					System.out.println("Bot is choosen " + mgg.botidtoMove);
					return -1;
				}
			}
			return -1;
		}
		else
		{
			Collection<node_data> nd = mgg.graph.getV();
			for (node_data node_data : nd) {
				Point3D p = node_data.getLocation();
				double dist = p.distance2D(new Point3D(mgg.x, mgg.y));
				if(dist <= (mgg.maxx-mgg.minx)*0.008)
				{
					mgg.x = 0;
					mgg.y = 0;
					node_data src = mgg.bots.get(mgg.botidtoMove).getCurrNode();
					Collection<edge_data> cE = mgg.graph.getE(src.getKey());
					for (edge_data edgess : cE) {
						if(edgess.getDest() == node_data.getKey())
						{
							mgg.isBotChooser = true;
							System.out.println("Node is choosen "+ node_data.getKey());
							return node_data.getKey();
						}
					}
					return -1;
				}
			}
			return -1;
		}
	}

}
