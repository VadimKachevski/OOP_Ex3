package MydataStructure;

import java.util.List;

import utils.Point3D;

public interface robotInterface {

	
	/**
	 * With a given list of nodes will set the path for the bot to take
	 * @param path
	 */
	public void setPath(List<node_data> path);
	/**
	 * returns the current path
	 * @return List<node_data> path
	 */
	public List<node_data> getPath();
	/**
	 * Sets the the graph for the bot
	 * @param g
	 */
	public void setGrap(graph g);
	/**
	 * returns the ID of the bot
	 * @return int ID
	 */
	public int getId();
	/**
	 * returns the current node that the bot is staying on
	 * @return node_data
	 */
	public node_data getCurrNode();
	/**
	 * Sets the current node to the bot
	 * @param currNode
	 */
	public void setCurrNode(node_data currNode);
	/**
	 * return the money of the bot
	 * @return int money
	 */
	public int getMoney();
	/**
	 * Sets the value of the bot
	 * @param money
	 */
	public void setMoney(int money);
	/**
	 * Sets the money for the bot
	 * @return
	 */
	public Point3D getPos();
	/**
	 * Sets the Current position of the bot
	 * @param pos
	 */
	public void setPos(Point3D pos);
	/**
	 * Returns the current speed of the bot
	 * @return double speed
	 */
	public double getSpeed();
	/**
	 * Set the speed of the bot
	 * @param speed
	 */
	public void setSpeed(double speed);
	/**
	 * With a given string tries to parse it and set the data from the json to the bot
	 * @param json
	 */
	public void botFromJSON(String json);
	
	
	
}
