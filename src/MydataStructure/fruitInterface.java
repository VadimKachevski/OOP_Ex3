package MydataStructure;

import utils.Point3D;

public interface fruitInterface {

	/**
	 * Sets if the fruit is targeted by a bot
	 * @param boolean occupied
	 */
	public void setOccupied(boolean occupied);
	/**
	 * returns if the fruit is targeted by a bot
	 * @return boolean
	 */
	public boolean getOccupied();
	/**
	 * Sets the graph for the fruit
	 * @param graph g
	 */
	public void setGraph(graph g);
	/**
	 * sets the type of the fruit -1 or 1
	 * @param int type
	 */
	public void setType(int type);
	/**
	 * return the type of the fruit -1 or 1
	 * @return int type
	 */
	public int getType();
	/**
	 * return the edge that the fruit is currently on
	 * @return edge_data
	 */
	public edge_data getEdge();
	/**
	 * sets the edge
	 * @param edge_data edge
	 */
	public void setEdge(edge_data edge);
	/**
	 * retuns the position of the fruit using Point3D
	 * @return Point3D pos
	 */
	public Point3D getPos();
	/**
	 * Sets the position of the fruit
	 * @param Point3D pos
	 */
	public void setPos(Point3D pos);
	/**
	 * return the value of the fruit
	 * @return int value
	 */
	public int getValue();
	/**
	 * sets the value of the fruit
	 * @param value
	 */
	public void setValue(int value);
	/**
	 * with a given String tries to init the fruit of the json data
	 * @param String json
	 */
	public void initFromJson(String json);
}
