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

	public KML_Logger(graph g) {
		// TODO Auto-generated constructor stub
		this.g = g;
	}
	public void setGame(game_service game)
	{
		this.game = game;
	}
	public void BuildGraph()
	{
		k = new Kml();
		doc = k.createAndSetDocument().withName("KML").withOpen(true);
		Folder folder = doc.createAndAddFolder();
		folder.withName("Folder").withOpen(true);


		Icon icon = new Icon().withHref("http://maps.google.com/mapfiles/kml/shapes/parking_lot.png");
		Style placeMarkStyle = doc.createAndAddStyle();
		placeMarkStyle.withId("placemarkid").createAndSetIconStyle().withIcon(icon).withScale(1.2);

		Collection<node_data> nd = g.getV();
		for (node_data node_data : nd) {
			//createPlacemarkWithChart(doc, folder, node_data.getLocation().x(), node_data.getLocation().y(), node_data.getKey()+"", 20);
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
		//		saveToFile("test1");
	}
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
				// 	prevKml.unmarshal(new File("data/"+nameS+".kml"));
				//Document docTest = prevKml.
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
				else
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

	public void setFruits(String time)
	{
		//Placemark p = doc.createAndAddPlacemark();
		//ArrayList<Placemark> currP = new ArrayList<Placemark>();
		Icon iconGreen = new Icon().withHref("http://maps.google.com/mapfiles/kml/paddle/grn-stars.png");
		Style greenStyle = doc.createAndAddStyle();
		greenStyle.withId("greenI").createAndSetIconStyle().withIcon(iconGreen).withScale(1.2);
		Icon iconYellow = new Icon().withHref("http://maps.google.com/mapfiles/kml/paddle/ylw-stars.png");
		Style yelloStyle = doc.createAndAddStyle();
		yelloStyle.withId("yellowI").createAndSetIconStyle().withIcon(iconYellow).withScale(1.2);

		Icon testRmove = new Icon().withHref("http://maps.google.com/mapfiles/kml/shapes/shaded_dot.png");
		Style removeStyle = doc.createAndAddStyle();
		removeStyle.withId("removeS").createAndSetIconStyle().withIcon(testRmove).withScale(0.0);
		List<String> frus = game.getFruits();
		for (String json : frus) {
			try {
				JSONObject obj = new JSONObject(json);
				//			JSONArray fruits = obj.getJSONArray("Fruit");
				//			for (int i = 0; i < fruits.length(); i++)
				//			{
				//				JSONObject CurrFruit = (JSONObject)fruits.get(i);
				JSONObject CurrFruit = (JSONObject) obj.get("Fruit");
				String pos = CurrFruit.getString("pos");
				String[] arr = pos.split(",");
				double x = Double.parseDouble(arr[0]);
				double y = Double.parseDouble(arr[1]);
				double z = Double.parseDouble(arr[2]);
				Point3D p = new Point3D(x, y, z);
				int type = CurrFruit.getInt("type");

				Placemark fr = doc.createAndAddPlacemark();
				//			fr.withName("fru");
				if(type == -1)
				{
					fr.setStyleUrl("#greenI");
				}
				else
				{
					fr.setStyleUrl("#yellowI");
				}
				fr.createAndSetPoint().addToCoordinates(x, y);
				fr.createAndSetTimeStamp().withWhen(time);
				//currP.add(fr);
				//	fr.setVisibility(false);
			}

			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}
	public void setBots(String time)
	{	
		Icon BusIcon = new Icon().withHref("http://maps.google.com/mapfiles/kml/shapes/bus.png");
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
				bot.createAndSetTimeStamp().withWhen(time);
			}
			catch (Exception e) {
				e.printStackTrace();
			}	
		}
	}
	private Hashtable<String, Integer> reader(Feature feat)
	{
		//ArrayList<Integer> ans = new ArrayList<Integer>();
		Hashtable<String, Integer> ans = new Hashtable<String, Integer>();
		System.out.println("Test1");
		if(feat!=null)
		{
			System.out.println("Test2");
			if(feat instanceof Document)
			{
				System.out.println("Test3");
				Document document = (Document) feat;
				ExtendedData  dd = document.getExtendedData();
				if(dd!=null)
				{
					List<Data> ld = dd.getData();
					for (Data data : ld) {
						// System.out.println(data.getName()+" :" + data.getValue());
						ans.put(data.getName(), Integer.parseInt(data.getValue()));
					}
				}

			}
		}
		return ans;
	}


}
