package gameClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

import org.json.JSONObject;


import MydataStructure.edge_data;
import MydataStructure.graph;
import MydataStructure.node_data;
import Server.game_service;
import de.micromata.opengis.kml.v_2_2_0.Data;
import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.ExtendedData;
import de.micromata.opengis.kml.v_2_2_0.Feature;
import de.micromata.opengis.kml.v_2_2_0.Folder;
import de.micromata.opengis.kml.v_2_2_0.Geometry;
import de.micromata.opengis.kml.v_2_2_0.Icon;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.LineString;
import de.micromata.opengis.kml.v_2_2_0.NetworkLink;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import de.micromata.opengis.kml.v_2_2_0.Style;
import de.micromata.opengis.kml.v_2_2_0.TimeStamp;
import utils.Point3D;
public class KML_Logger {

	game_service game;
	graph g;
	Kml k; 
	Document doc;
	/**
	 * constructor 
	 * @param g
	 */
	public KML_Logger(graph g) {
		this.g = g;
	}
	/**
	 * sets the game server
	 * @param game
	 */
	public void setGame(game_service game)
	{
		this.game = game;
	}
	/**
	 * this method creates and sets a KML document which through it,
	 * it allows to build the graph for the KML file.
	 */
	public void BuildGraph()
	{
		k = new Kml();
		doc = k.createAndSetDocument().withName("KML").withOpen(true);
		Folder folder = doc.createAndAddFolder();
		folder.withName("Folder").withOpen(true);

		Icon icon = new Icon().withHref("http://maps.google.com/mapfiles/kml/shapes/shaded_dot.png");
		Style placeMarkStyle = doc.createAndAddStyle();
		placeMarkStyle.withId("placemarkid").createAndSetIconStyle().withIcon(icon).withScale(1.2).withColor("ffff0000");

		Collection<node_data> nd = g.getV();
		for (node_data node_data : nd) {
			Placemark p = doc.createAndAddPlacemark();
			p.withName(node_data.getKey()+"");
			p.withStyleUrl("#placemarkid");
			p.createAndSetPoint().addToCoordinates(node_data.getLocation().x(), node_data.getLocation().y());

			Style redStyle= doc.createAndAddStyle();
			redStyle.withId("redstyle").createAndSetLineStyle().withColor("ff0000ff").setWidth(3.0);;
			Collection<edge_data> ed = g.getE(node_data.getKey());
			for (edge_data edgess : ed) {
				Placemark p2 = doc.createAndAddPlacemark();
				p2.withStyleUrl("#redstyle");

				Point3D loc  =g.getNode(edgess.getSrc()).getLocation();
				Point3D locNext = g.getNode(edgess.getDest()).getLocation();

				p2.createAndSetLineString().withTessellate(true).addToCoordinates(loc.x(),loc.y()).addToCoordinates(locNext.x(),locNext.y());
			}
		}
	}
	/**
	 * by a given name and result this function saves the file
	 * @param nameS
	 * @param resault
	 */
	public void saveToFile(String nameS,String resault)
	{
		try {

			int prevGrade = 0;
			int prevMoves = 0;
			File tmpDir = new File("data/"+nameS+".kml");
			boolean exists = tmpDir.exists();
			if(exists)
			{
				Kml prevKml = Kml.unmarshal(tmpDir);
				Feature feat = prevKml.getFeature();

				Hashtable<String, Integer> ans = reader(feat);
				prevGrade = ans.get("grade");
				prevMoves = ans.get("moves");

				JSONObject obj = new JSONObject(resault);
				JSONObject CurrRes = (JSONObject) obj.get("GameServer");
				int grade = CurrRes.getInt("grade");
				int moves = CurrRes.getInt("moves");
				ExtendedData ed = doc.createAndSetExtendedData();
				ed.createAndAddData(grade+"").setName("grade");
				ed.createAndAddData(moves+"").setName("moves");

				if(grade>prevGrade)
				{
					System.out.println("grade:"+grade + " prev grade :"+prevGrade);
					k.marshal(tmpDir);
				}
				if(grade==prevGrade)
				{
					if(moves<prevMoves)
					{
						System.out.println("Moves " + moves + " prev moves" + prevMoves);
						k.marshal(tmpDir);
					}
				}
			}
			else {
				JSONObject obj = new JSONObject(resault);
				JSONObject CurrRes = (JSONObject) obj.get("GameServer");
				int grade = CurrRes.getInt("grade");
				int moves = CurrRes.getInt("moves");
				ExtendedData ed = doc.createAndSetExtendedData();
				ed.createAndAddData(grade+"").setName("grade");
				ed.createAndAddData(moves+"").setName("moves");
				k.marshal(tmpDir);
			}
		}
		catch (Exception e) {
			e.printStackTrace();		}
	}
	/**
	 * by a given start string and the end of the string this method sets the
	 *  fruit by its time of appearance as it will be shown in the KML file. 
	 * @param time
	 * @param end
	 */
	public void setFruits(String time,String end)
	{
		Icon iconGreen = new Icon().withHref("https://i.ibb.co/BcqrFGX/strawberry.png");
		Style greenStyle = doc.createAndAddStyle();
		greenStyle.withId("greenI").createAndSetIconStyle().withIcon(iconGreen).withScale(1.2);
		Icon iconYellow = new Icon().withHref("https://i.ibb.co/1Qt9xfw/kiwi.png");
		Style yelloStyle = doc.createAndAddStyle();
		yelloStyle.withId("yellowI").createAndSetIconStyle().withIcon(iconYellow).withScale(1.2);
		List<String> frus = game.getFruits();
		for (String json : frus) {
			try {
				JSONObject obj = new JSONObject(json);
				JSONObject CurrFruit = (JSONObject) obj.get("Fruit");
				String pos = CurrFruit.getString("pos");
				String[] arr = pos.split(",");
				double x = Double.parseDouble(arr[0]);
				double y = Double.parseDouble(arr[1]);
				double z = Double.parseDouble(arr[2]);
				Point3D p = new Point3D(x, y, z);
				int type = CurrFruit.getInt("type");

				Placemark fr = doc.createAndAddPlacemark();
				if(type == -1)
				{
					fr.setStyleUrl("#greenI");
				}
				else
				{
					fr.setStyleUrl("#yellowI");
				}
				fr.createAndSetPoint().addToCoordinates(x, y);
				fr.createAndSetTimeSpan().withBegin(time).withEnd(end);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * by a given start string and the end of the string this method 
	 * sets the Bots by its time of appearance as it will be shown in the KML file.
	 * @param time
	 * @param end
	 */
	public void setBots(String time,String end)
	{	
		Icon BusIcon = new Icon().withHref("https://i.ibb.co/BLpqcfY/robot3.png");
		Style busStyle = doc.createAndAddStyle();
		busStyle.withId("Bus").createAndSetIconStyle().withIcon(BusIcon).withScale(1.2);
		List<String> robos = game.getRobots();
		for (String string : robos) {
			try {
				JSONObject obj = new JSONObject(string);
				JSONObject CurrBot = (JSONObject) obj.get("Robot");
				String pos = CurrBot.getString("pos");
				String[] arr = pos.split(",");
				double x = Double.parseDouble(arr[0]);
				double y = Double.parseDouble(arr[1]);
				double z = Double.parseDouble(arr[2]);
				Point3D posP = new Point3D(x, y, z);
				int id = CurrBot.getInt("id");
				Placemark bot = doc.createAndAddPlacemark();
				bot.setStyleUrl("#Bus");
				bot.createAndSetPoint().addToCoordinates(x, y);
				bot.createAndSetTimeSpan().withBegin(time).withEnd(end);
			}
			catch (Exception e) {
				e.printStackTrace();
			}	
		}
	}
	/**
	 * this method allows us to have a record of the best result as it 
	 * takes the result from the prior KML file and that’s how we keep 
	 * track of the highest result so far.
	 * @param feat
	 * @return a Hashtable updated with highest results for each level
	 */
	private Hashtable<String, Integer> reader(Feature feat)
	{
		Hashtable<String, Integer> ans = new Hashtable<String, Integer>();
		if(feat!=null)
		{
			if(feat instanceof Document)
			{
				Document document = (Document) feat;
				ExtendedData  dd = document.getExtendedData();
				if(dd!=null)
				{
					List<Data> ld = dd.getData();
					for (Data data : ld) {
						ans.put(data.getName(), Integer.parseInt(data.getValue()));
					}
				}
			}
		}
		return ans;
	}

}
