package MydataStructureTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import MydataStructure.bot;
import MydataStructure.graph;
import MydataStructure.node_data;
import MydataStructure.vertex;
import utils.Point3D;

class botTest {

	@Test
	void testBot() {
		bot b = new bot();

		assertEquals(-1, b.getMoney());
		assertEquals(-1, b.getId());
		assertEquals(-1, b.getSpeed());
	}
	@Test
	void testConstructor() {
		int id = 2;
		int money = 30;
		int speed = 6;
		Point3D p1 = new Point3D(2,3,2);
		node_data node = null;
		graph g = null;

		bot b = new bot(id, node, money, p1, speed, g);

		assertEquals(30, b.getMoney());
		assertEquals(2, b.getId());
		assertEquals(6, b.getSpeed());
	}
	@Test
	void testSetPath() {
		bot b = new bot();
		node_data n1 = new vertex(0, 1, 2);
		node_data n2 = new vertex(1, 2, 3);
		ArrayList<node_data> list = new ArrayList<>();
		list.add(n1);
		list.add(n2);
		b.setPath(list);

		assertEquals(1, b.getPath().get(0).getLocation().ix());
		assertEquals(3, b.getPath().get(1).getLocation().iy());

	}


}
