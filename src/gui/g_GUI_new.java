package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileSystemView;

import MydataStructure.edge_data;
import MydataStructure.graph;
import MydataStructure.node_data;
import algorithms.Graph_Algo;
import utils.Point3D;
import utils.StdDrawGraphGUI;


public class g_GUI_new {
	graph graph;
	double minx=Integer.MAX_VALUE;
	double maxx=Integer.MIN_VALUE;
	double miny=Integer.MAX_VALUE;
	double maxy=Integer.MIN_VALUE;

	public g_GUI_new(graph g)
	{
		this.graph = g;
		//this.GA.init(g);
		initGUI();

	}
	public g_GUI_new()
	{
		graph = null;
		//GA.init(graph);
		initGUI();
	}
	private void initGUI()
	{
		
		StdDrawGraphGUI.setCanvasSize(800, 600);
		
		if(graph != null)
		{
			Collection<node_data> nd = graph.getV();
			for (node_data node_data : nd) {
				Point3D s = node_data.getLocation();
				if(s.ix() < minx)
				{
					minx = s.ix();
				}
				if(s.ix() > maxx)
				{
					maxx = s.ix();
				}
				if(s.iy() > maxy)
				{
					maxy = s.iy();
				}
				if(s.iy() < miny)
				{
					miny = s.iy();
				}
			}
			StdDrawGraphGUI.setXscale(minx-(minx/10), maxx+(maxx/10));
			StdDrawGraphGUI.setYscale(miny-(miny/10),maxy+(maxy/10));
			StdDrawGraphGUI.setG_GUI(this);
			paint();
		
		}
	}
		
	public void paint()
	{
		Collection<node_data> nodes = graph.getV();
		for (node_data node_data : nodes) {
			StdDrawGraphGUI.setPenColor(Color.BLUE);
			Point3D p = node_data.getLocation();
			if(p!=null)
			{
				StdDrawGraphGUI.filledCircle(p.ix(), p.iy(),2);
				StdDrawGraphGUI.text(p.ix(), p.iy()+((miny+maxy)/100), node_data.getKey()+"");
				Collection<edge_data> edges = graph.getE(node_data.getKey());
				for (edge_data edge : edges) {
					if(edge.getTag() == 999)
					{
						edge.setTag(0);
						StdDrawGraphGUI.setPenRadius(0.008);
						StdDrawGraphGUI.setPenColor(Color.GREEN);
					}
					else
					{
						StdDrawGraphGUI.setPenColor(Color.RED);
					}
					node_data dest = graph.getNode(edge.getDest());
					Point3D pd = dest.getLocation();
					if(pd!=null)
					{
						StdDrawGraphGUI.line(p.ix(), p.iy(), pd.ix(), pd.iy());
						StdDrawGraphGUI.text((p.ix()+pd.ix())/2, (p.iy()+pd.iy())/2,edge.getWeight()+"");
						StdDrawGraphGUI.setPenColor(Color.YELLOW);
						int xhalf = (((p.ix()+pd.ix())/2)+pd.ix())/2;
						int yhalf = (((p.iy()+pd.iy())/2)+pd.iy())/2;
						StdDrawGraphGUI.filledCircle((xhalf+pd.ix())/2, (yhalf+pd.iy())/2, 1);
					}
					//g2d.setStroke(bs);
					StdDrawGraphGUI.setPenRadius();
				}
			}
		}
		StdDrawGraphGUI.show();
	}
	/**
	 * With a given txt file creating a Graph, should be serializable from a Graph. 
	 */
	public void DrawFromFile() {
		Graph_Algo ga = new Graph_Algo();
		JFileChooser jf = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		int returnV = jf.showOpenDialog(null);
		if (returnV == JFileChooser.APPROVE_OPTION) {
			File selectedFile = jf.getSelectedFile();
			ga.init(selectedFile.getAbsolutePath());
			this.graph = ga.copy();
			paint();
		}
	}
	/**
	 * Saving the current graph into a unreable deserializable file.
	 */
	public void saveToFile()
	{
		String sb = "TEST CONTENT";
		Graph_Algo ga = new Graph_Algo();
		ga.init(graph);
		JFileChooser jf = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		int returnV = jf.showOpenDialog(null);
		if (returnV == JFileChooser.APPROVE_OPTION) {
			try {
				ga.save(jf.getSelectedFile()+".txt");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	/**
	 * Asks the user a From and a TO and tries to re-create the shortest path
	 * will mark the path in green if a path is possible else nothing will happen
	 */
	public void shortestPath()
	{
		JFrame jinput = new JFrame();
		String fromS = JOptionPane.showInputDialog(jinput,"Enter From");		
		String to = JOptionPane.showInputDialog(jinput,"Enter To");
		fromS = fromS.trim();
		to = to.trim();
		try
		{
			int fromN = Integer.parseInt(fromS);
			int toN = Integer.parseInt(to);
			Graph_Algo ga = new Graph_Algo();
			ga.init(graph);
			List<node_data> ans = ga.shortestPath(fromN, toN);
			for (int j = 0; j < ans.size()-1; j++) {
				graph.getEdge(ans.get(j).getKey(), ans.get(j+1).getKey()).setTag(999);
			}
			paint();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Asks the user a From and a TO and tries to re-create the shortest path
	 * Will show the shortest path length in a Message dialog
	 */
	public void ShortestPathDist()
	{
		JFrame jinput = new JFrame();
		String fromS = JOptionPane.showInputDialog(jinput,"Enter From");		
		String to = JOptionPane.showInputDialog(jinput,"Enter To");
		try
		{
			int fromN = Integer.parseInt(fromS);
			int toN = Integer.parseInt(to);
			Graph_Algo ga = new Graph_Algo();
			ga.init(graph);
			double x = ga.shortestPathDist(fromN, toN);
			JOptionPane.showMessageDialog(jinput, "the shortest distance is:" + x);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Asks the user through which nodes he should pass when the user is done 
	 * he need to write DONE and it will try to create the relative shortest path through
	 * all the nodes
	 */
	public void TSPEnterNodes()
	{
		JFrame jinput = new JFrame();
		JOptionPane.showMessageDialog(jinput, "To get TSP enter all the nodes from start node to end node \n after you are done enter DONE");
		ArrayList<Integer> arrayTSP = new ArrayList<Integer>();
		String ans;
		do {
			ans = JOptionPane.showInputDialog(jinput, "Enter node or DONE when it is the last node");
			if(ans.equalsIgnoreCase("done"))
			{
				break;
			}
			try
			{
				arrayTSP.add(Integer.parseInt(ans));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		} while (!ans.equalsIgnoreCase("done"));
		Graph_Algo ga = new Graph_Algo();
		ga.init(graph);
		List<node_data> ansTSP = ga.TSP(arrayTSP);
		for (int j = 0;j < ansTSP.size()-1; j++) {
			graph.getEdge(ansTSP.get(j).getKey(), ansTSP.get(j+1).getKey()).setTag(999);
		}
		paint();
	}
	/**
	 * with a given file in the format x,y,z,t,y...  will try to create the shortest path 
	 * through all the nodes in the file
	 */
	public void TSPfromFile()
	{
		Graph_Algo ga = new Graph_Algo();
		JFileChooser jf = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		int returnV = jf.showOpenDialog(null);
		String ans;
		if (returnV == JFileChooser.APPROVE_OPTION) {
			File selectedFile = jf.getSelectedFile();
			try
			{
				Scanner sc = new Scanner(selectedFile);
				if(sc.hasNextLine())
				{
					ans = sc.nextLine();
					String[] seg = ans.split(",");
					List<Integer> ListNodes = new ArrayList<Integer>();
					for (int i = 0; i < seg.length; i++) {
						seg[i] = seg[i].trim();
						ListNodes.add(Integer.parseInt(seg[i]));
					}
					ga.init(graph);
					List<node_data> ansTSP = ga.TSP(ListNodes);
					for (int j = 0;j < ansTSP.size()-1; j++) {
						graph.getEdge(ansTSP.get(j).getKey(), ansTSP.get(j+1).getKey()).setTag(999);
					}
					paint();
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
