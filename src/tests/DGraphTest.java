package tests;

import algorithms.Graph_Algo;
import utils.Point3D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import MydataStructure.myDGraph;
import MydataStructure.node_data;
import MydataStructure.vertex;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DGraphTest {

	private myDGraph graph;
	
	@BeforeEach
	void setup()
	{
		graph = createGraphBefore();
	}
	
	@Test
	void testgetNode() {
		node_data a = graph.getNode(1);
		node_data b = graph.getNode(3);
		node_data c = graph.getNode(4);
		node_data d = graph.getNode(7);
		assertEquals(1, a.getKey());
		assertEquals(3, b.getKey());
		assertEquals(4, c.getKey());
		if(d!=null)
		{
			fail("d should be null");
		}
	}
	@Test
	void testRemoveNode()
	{
		assertEquals(5, graph.edgeSize());
		graph.connect(6, 1, 20);
		graph.connect(5, 1, 20);
		graph.connect(4, 1, 4);
		graph.connect(3, 1, 8);
		graph.connect(2, 1,8);
		graph.connect(1, 3, 20);
		graph.connect(1, 4, 4);
		graph.connect(1, 5, 8);
		graph.connect(1, 6,8);
		assertEquals(14, graph.edgeSize());
		graph.removeNode(1);
		assertEquals(4, graph.edgeSize());
		graph.removeNode(4);
		assertEquals(2, graph.edgeSize());
	}
	@Test
	void testAddNode()
	{
		assertEquals(6, graph.getV().size());
		graph.addNode(new vertex(7,new Point3D(10,10)));
		assertEquals(7, graph.getV().size());
		assertEquals(7, graph.getNode(7).getKey());
	}
	@Test
	void removeEdge()
	{
		Graph_Algo ga = new Graph_Algo();
		ga.init(graph);
		double ans = ga.shortestPathDist(1, 6);
		if(ans == Integer.MAX_VALUE)
		{
			fail("Should'nt be MAX Value");
		}
		graph.removeEdge(3, 4);
		ans = ga.shortestPathDist(1, 6);
		if(ans != Integer.MAX_VALUE)
		{
			fail("There should'nt be any path to 6 therefor should be MAX VALUE");
		}
	}
	@Test
	void testMC()
	{
		assertEquals(11, graph.getMC());
		graph.addNode(new vertex(7));
		graph.addNode(new vertex(8));
		graph.addNode(new vertex(9));
		graph.connect(7, 8, 10);
		assertEquals(15, graph.getMC());
		graph.removeEdge(7, 8);
		assertEquals(16, graph.getMC());
	}
	myDGraph createGraphBefore()
	{
		myDGraph graph = new myDGraph();
		graph.addNode(new vertex(1, new Point3D(250 ,120)));
		graph.addNode(new vertex(2, new Point3D(20,140)));
		graph.addNode(new vertex(3,new Point3D(190,10)));
		graph.addNode(new vertex(4,new Point3D(130,210)));
		graph.addNode(new vertex(5,new Point3D(340,50)));
		graph.addNode(new vertex(6,new Point3D(200,400)));
		graph.connect(1, 2, 20);
		graph.connect(2, 3, 20);
		graph.connect(3, 4, 4);
		graph.connect(4, 5, 8);
		graph.connect(5, 6,8);
		return graph;
	}
}
