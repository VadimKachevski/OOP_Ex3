package gameClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;

import MydataStructure.graph;
import MydataStructure.node_data;
import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Folder;
import de.micromata.opengis.kml.v_2_2_0.Icon;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import de.micromata.opengis.kml.v_2_2_0.Style;
public class KML_Logger {
	
	
	
	graph g;
	
	public KML_Logger(graph g) {
		// TODO Auto-generated constructor stub
		this.g = g;
	}
	
	public void test()
	{
		Kml k = new Kml();
		Document doc = k.createAndSetDocument().withName("TestOne").withOpen(true);
		Folder folder = doc.createAndAddFolder();
		folder.withName("TestName ???????").withOpen(true);
		
		
		Collection<node_data> nd = g.getV();
		for (node_data node_data : nd) {
			//createPlacemarkWithChart(doc, folder, node_data.getLocation().x(), node_data.getLocation().y(), node_data.getKey()+"", 20);
			Placemark p = doc.createAndAddPlacemark();
			p.withName(node_data.getKey()+"");
			p.createAndSetPoint().addToCoordinates(node_data.getLocation().x(), node_data.getLocation().y());
			
		}
		try {
			k.marshal(new File("test1.kml"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	private void createPlacemarkWithChart(Document document, Folder folder, double longitude, double latitude, 
		    String continentName, int coveredLandmass)
	{
		int remainingLand = 100-coveredLandmass;
		Icon icon = new Icon().withHref("http://chart.apis.google.com/chart?chs=380x200&chd=t:" + coveredLandmass + "," + remainingLand + "&cht=p&chf=bg,s,ffffff00");
		Style style = document.createAndAddStyle();
		style.withId("style_" + continentName) // set the stylename to use this style from the placemark
	    .createAndSetIconStyle().withScale(5.0).withIcon(icon); // set size and icon
	style.createAndSetLabelStyle().withColor("ff43b3ff").withScale(5.0); // set color and size of the continent name

	Placemark placemark = folder.createAndAddPlacemark();
	// use the style for each continent
	placemark.withName(continentName)
	    .withStyleUrl("#style_" + continentName)
	    // 3D chart imgae
	    .withDescription(
	        "<![CDATA[<img src=\"http://chart.apis.google.com/chart?chs=430x200&chd=t:" + coveredLandmass + "," + remainingLand + "&cht=p3&chl=" + continentName + "|remaining&chtt=Earth's surface\" />")
	    // coordinates and distance (zoom level) of the viewer
	    .createAndSetLookAt().withLongitude(longitude).withLatitude(latitude).withAltitude(0).withRange(12000000);
	
	placemark.createAndSetPoint().addToCoordinates(longitude, latitude); // set coordinates
	}
}
