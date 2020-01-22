package MydataStructureTest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import MydataStructure.edge_data;
import MydataStructure.edges;
import MydataStructure.fruit;
import MydataStructure.graph;
import MydataStructure.myDGraph;
import utils.Point3D;

class fruitTest {

	@Test
	void testFruit() {
		fruit f1 = new fruit();
	
		assertEquals(null, f1.getPos());
		assertEquals(null, f1.getEdge());
		assertEquals(false, f1.getOccupied());
		assertEquals(0, f1.getValue());
		assertEquals(-1, f1.getType());
	}
	@Test
	void testFruitConstructor() {
		int value = 5;
		int type = 1;
		Point3D p1 = new Point3D(1,2,3);
		graph g = null;
		edge_data edge = new edges();
		fruit f1 = new fruit(edge, p1, value, type, g);

		assertEquals(5, f1.getValue());
		assertEquals(1, f1.getType());
		assertEquals(new Point3D(1,2,3), f1.getPos());
	}
	@Test
	void testSetGet() {
		fruit f1 = new fruit();
		Point3D p1 = new Point3D(4,5,6);

		f1.setOccupied(true);
		f1.setPos(p1);
		f1.setValue(20);
		f1.setType(1);
		
		assertEquals(20, f1.getValue());
		assertEquals(1, f1.getType());
		assertEquals(new Point3D(4,5,6), f1.getPos());
		assertEquals(true, f1.getOccupied());
	}


}













