package algorithmsTest;

import utils.Point3D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import MydataStructure.myDGraph;
import MydataStructure.graph;
import MydataStructure.node_data;
import MydataStructure.vertex;
import algorithms.Graph_Algo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class Graph_AlgoTest {

	Graph_Algo a = new Graph_Algo();
	@BeforeEach
	void setup()
	{
		a.init(createGraphBefore());
	}
	@Test
	void testInitString() {
		a.init("avi.txt");
		System.out.println();
	}

	@Test
	void testSave() {
		a.save("avi.txt");
		Graph_Algo b = new Graph_Algo();
		b.init("avi.txt");
		double a1_to_6 = a.shortestPathDist(1, 6);
		double b1_to_6 = b.shortestPathDist(1, 6);
		assertEquals(a1_to_6, b1_to_6,0.001);
		
	}

	@Test
	void testIsConnected() {
		graph s = new myDGraph();
		s.addNode(new vertex(1));
		s.addNode(new vertex(2));
		s.addNode(new vertex(3));
		s.addNode(new vertex(4));
		s.addNode(new vertex(5));
		s.addNode(new vertex(6));
		s.addNode(new vertex(7));
		s.connect(1, 2, 0);
		s.connect(1, 3, 0);
		s.connect(2, 4, 0);
		s.connect(2, 5, 0);
		s.connect(3, 2, 0);
		s.connect(4, 6, 0);
		s.connect(4, 5, 0);
		s.connect(5, 6, 0);
		s.connect(5, 7, 0);
		s.connect(6, 7, 0);
		s.connect(7, 1, 0);
////		s.addNode(new vertex(1));
////		s.addNode(new vertex(2));
////		s.connect(1, 2, 0);
		Graph_Algo e = new Graph_Algo();
		e.init(s);
		assertEquals(true, e.isConnected());
		
		graph g2 = new myDGraph();
		g2.addNode(new vertex(1));
		g2.addNode(new vertex(2));
		g2.addNode(new vertex(3));
		g2.addNode(new vertex(4));
		g2.addNode(new vertex(5));
		g2.connect(1, 5, 30);
		g2.connect(5, 1, 30);
		g2.connect(2, 5, 30);
		g2.connect(5, 2, 30);
		g2.connect(3, 5, 30);
		g2.connect(5, 3, 30);
		g2.connect(4, 5, 30);
		g2.connect(5, 4, 30);
		Graph_Algo ga2 = new Graph_Algo();
		ga2.init(g2);
		assertEquals(true, ga2.isConnected());
		
		graph g3 = new myDGraph();
		g3.addNode(new vertex(1));
		g3.addNode(new vertex(2));
		g3.addNode(new vertex(3));
		g3.addNode(new vertex(4));
		g3.addNode(new vertex(5));
		g3.connect(1, 2, 30);
		g3.connect(2, 3, 30);
		g3.connect(3, 4, 30);
		g3.connect(4, 5, 30);
		Graph_Algo ga3 = new Graph_Algo();
		ga3.init(g3);
		assertEquals(false, ga3.isConnected());
		
//		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//		System.out.println(timestamp);
//		graph test = MillionVertex();
//		Timestamp timestamp2 = new Timestamp(System.currentTimeMillis());
//		System.out.println(timestamp2);
//		Graph_Algo gM = new Graph_Algo();
//		gM.init(test);
//		Timestamp timestamp3 = new Timestamp(System.currentTimeMillis());
//		System.out.println(timestamp3);
//		System.out.println(gM.isConnected());
//		Timestamp timestamp4 = new Timestamp(System.currentTimeMillis());
//		System.out.println(timestamp4);
	}

	@Test
	void testShortestPathDist() {
		graph s = new myDGraph();
		s.addNode(new vertex(1));
		s.addNode(new vertex(2));
		s.addNode(new vertex(3));
		s.addNode(new vertex(4));
		s.addNode(new vertex(5));
		s.addNode(new vertex(6));
		s.addNode(new vertex(7));
		s.addNode(new vertex(8));
		s.connect(1, 2, 1);
		s.connect(1, 3, 7);
		s.connect(1, 4, 4);
		s.connect(2, 3, 2);
		s.connect(3, 4, 4);
		s.connect(3, 5, 3);
		s.connect(4, 5, 5);
		s.connect(5, 6, 4);
		s.connect(5, 7, 13);
		s.connect(6, 7, 8);
		s.connect(6, 8, 10);
		s.connect(7, 8, 3);
		Graph_Algo e = new Graph_Algo();
		e.init(s);
		assertEquals(20.0, e.shortestPathDist(1, 8),0.001);
	}

	@Test
	void testShortestPath() {
		graph s = new myDGraph();
		s.addNode(new vertex(1));
		s.addNode(new vertex(2));
		s.addNode(new vertex(3));
		s.addNode(new vertex(4));
		s.addNode(new vertex(5));
		s.addNode(new vertex(6));
		s.addNode(new vertex(7));
		s.addNode(new vertex(8));
		s.connect(1, 2, 1);
		s.connect(1, 3, 7);
		s.connect(1, 4, 4);
		s.connect(2, 3, 2);
		s.connect(3, 4, 4);
		s.connect(3, 5, 3);
		s.connect(4, 5, 5);
		s.connect(5, 6, 4);
		s.connect(5, 7, 13);
		s.connect(6, 7, 8);
		s.connect(6, 8, 10);
		s.connect(7, 8, 3);
		Graph_Algo e = new Graph_Algo();
		e.init(s);
		List<node_data> ans = e.shortestPath(1, 8);
		//System.out.println(ans.toString());
	}

	@Test
	void testTSP() {
		graph s = new myDGraph();
		s.addNode(new vertex(1));
		s.addNode(new vertex(2));
		s.addNode(new vertex(3));
		s.addNode(new vertex(4));
		s.addNode(new vertex(5));
		s.addNode(new vertex(6));
		s.addNode(new vertex(7));
		s.addNode(new vertex(8));
		s.connect(1, 2, 1);
		s.connect(1, 3, 7);
		s.connect(3, 1, 7);
		s.connect(1, 4, 4);
		s.connect(2, 3, 2);
		s.connect(3, 4, 4);
		s.connect(3, 5, 3);
		s.connect(4, 5, 5);
		s.connect(5, 6, 4);
		s.connect(5, 7, 13);
		s.connect(6, 7, 8);
		s.connect(6, 8, 10);
		s.connect(7, 8, 3);
		Graph_Algo e = new Graph_Algo();
		e.init(s);
		//assertEquals(false, e.isConnectedNew());
		//List<node_data> ans = e.shortestPath(1, 8);
		//System.out.println(ans.toString());
		List<Integer> t = new ArrayList<Integer>();
		t.add(1);
		t.add(3);
		t.add(2);
		t.add(5);
		t.add(7);
		t.add(6);
		t.add(8);
		//List<node_data> ans = e.TSP(t);
		List<node_data> ans2 = e.TSP(t);
		List<Integer> expec = new ArrayList<Integer>();
		expec.add(1);
		expec.add(2);
		expec.add(3);
		expec.add(5);
		expec.add(6);
		expec.add(7);
		expec.add(8);
		for (int i = 0; i < ans2.size(); i++) {
			if(ans2.get(i).getKey() != expec.get(i))
			{
				fail("Should't not fail");
			}
		}
	//	System.out.println(ans);
	}

	@Test
	void testCopy() {
		graph s = new myDGraph();
		s.addNode(new vertex(1));
		s.addNode(new vertex(2));
		s.addNode(new vertex(3));
		s.addNode(new vertex(4));
		s.addNode(new vertex(5));
		s.addNode(new vertex(6));
		s.addNode(new vertex(7));
		s.addNode(new vertex(8));
		s.connect(1, 2, 1);
		s.connect(1, 3, 7);
		s.connect(1, 4, 4);
		s.connect(2, 3, 2);
		s.connect(3, 4, 4);
		s.connect(3, 5, 3);
		s.connect(4, 5, 5);
		s.connect(5, 6, 4);
		s.connect(5, 7, 13);
		s.connect(6, 7, 8);
		s.connect(6, 8, 10);
		s.connect(7, 8, 3);
		Graph_Algo e = new Graph_Algo();
		e.init(s);
		
		graph n = e.copy();
		s.getNode(1).setWeight(30);
		if(n.getNode(1).getWeight() == s.getNode(1).getWeight())
		{
			fail("Should be diff");
		}
	}
	@Test
	void test()
	{
		graph g = graphFactory();
		
	}
	
	graph createGraphBefore()
	{
		graph graph = new myDGraph();
		graph.addNode(new vertex(1, new Point3D(50 ,20), 30));
		graph.addNode(new vertex(2));
		graph.addNode(new vertex(3));
		graph.addNode(new vertex(4));
		graph.addNode(new vertex(5));
		graph.addNode(new vertex(6));
		graph.connect(1, 2, 20);
		graph.connect(2, 3, 20);
		graph.connect(3, 4, 4);
		graph.connect(4, 5, 8);
		graph.connect(5, 6,8);
		return graph;
		
	}
	graph graphFactory()
	{
		graph graph = new myDGraph();
		Random rand = new Random();
		for (int i = 0; i < 100; i++) {
			
			Point3D p = new Point3D(rand.nextInt(600), rand.nextInt(600));
			graph.addNode(new vertex(i,p));
		}
		Collection<node_data> nd = graph.getV();
		for (node_data node_data : nd) {
			int amount_of_edegs = rand.nextInt(20);
			for (int i = 0; i < amount_of_edegs; i++) {
				int dest = rand.nextInt(100);
				while(dest != node_data.getKey())
				{
					dest = rand.nextInt(100);
				}
				double weight = rand.nextDouble();
				weight*= 100;
				graph.connect(node_data.getKey(), dest, weight);
			}
		}
		return graph;
	}
	graph MillionVertex()
	{
		graph graph = new myDGraph();
		Random rand = new Random();
		for (int i = 0; i < 1000000; i++) {
			Point3D p = new Point3D(rand.nextInt(600), rand.nextInt(600));
			graph.addNode(new vertex(i,p));
		}
		for (int i = 0; i < 1000000-10; i++) {
			graph.connect(i, i+1, 30);
			graph.connect(i, i+2, 30);
			graph.connect(i, i+3, 30);
			graph.connect(i, i+4, 30);
			graph.connect(i, i+5, 30);
			graph.connect(i, i+6, 30);
			graph.connect(i, i+7, 30);
			graph.connect(i, i+8, 30);
			graph.connect(i, i+9, 30);
			graph.connect(i, i+10, 30);
			
		}
		graph.connect(999996, 0, 30);
		graph.connect(999997, 0, 30);
		graph.connect(999998, 0, 30);
		graph.connect(999999, 0, 30);
//		Collection<node_data> nd = graph.getV();
//		for (node_data node_data : nd) {
//			//System.out.println(node_data.getKey());
//			for(int i=0;i<5;i++)
//			{
//				int dest = rand.nextInt(100000);
//				while(dest != node_data.getKey())
//				{
//					dest = rand.nextInt(100000);
//				}
//				double weight = rand.nextDouble();
//				weight*= 100;
//				graph.connect(node_data.getKey(), dest, weight);
//			}
//		}
		
		return graph;
	}

}
