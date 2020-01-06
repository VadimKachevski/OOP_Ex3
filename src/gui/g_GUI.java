package gui;

import algorithms.Graph_Algo;
import utils.Point3D;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;

import MydataStructure.edge_data;
import MydataStructure.graph;
import MydataStructure.node_data;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;



public class g_GUI extends JFrame implements ActionListener,MouseListener, MouseMotionListener  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7377758160791730941L;
	Object mouseLock = new Object();
	graph graph;
	//Graph_Algo GA;
	public g_GUI(graph g)
	{
		this.graph = g;
		//this.GA.init(g);
		initGUI();

	}
	public g_GUI()
	{
		graph = null;
		//GA.init(graph);
		initGUI();
	}
	/**
	 * Initiates the GUI with 1000x1000 and all the menu bars
	 */
	private void initGUI()
	{
		//JFrame j = new JFrame();
		this.setSize(1000, 800);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		
		double minx=Integer.MAX_VALUE;
		double maxx=Integer.MIN_VALUE;
		double miny=Integer.MAX_VALUE;
		double maxy=Integer.MIN_VALUE;
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
		
		MenuBar menuBar = new MenuBar();
		Menu menu = new Menu("Options");
		menuBar.add(menu);
		this.setMenuBar(menuBar);
		MenuItem item1 = new MenuItem("Draw graph");
		MenuItem item2 = new MenuItem("Draw from file");
		MenuItem item3 = new MenuItem("Save to file");
		MenuItem item4 = new MenuItem("find Shortest path");
		MenuItem item5 = new MenuItem("find Shortest path distance");
		MenuItem item6 = new MenuItem("TSP enter all the nodes");
		MenuItem item7 = new MenuItem("TSP from file");
		item1.addActionListener(this);
		item2.addActionListener(this);
		item3.addActionListener(this);
		item4.addActionListener(this);
		item5.addActionListener(this);
		item6.addActionListener(this);
		item7.addActionListener(this);
		menu.add(item1);
		menu.add(item2);
		menu.add(item3);
		menu.add(item4);
		menu.add(item5);
		menu.add(item6);
		menu.add(item7);
	}

	/**
	 * Custom paint function to paint the Graph
	 */
	public void paint(Graphics g)
	{
		
		super.paint(g);
//		Graphics2D gsd = (Graphics2D)g;
//		gsd.sc
		if(graph != null)
		{
			Collection<node_data> nodes = graph.getV();
			for (node_data node_data : nodes) {
				g.setColor(Color.BLUE);
				Point3D p = node_data.getLocation();
				if(p != null)
				{
					g.fillOval(p.ix(), p.iy(), 12, 12);
					g.drawString(node_data.getKey()+"", p.ix(), p.iy()-1);
					Collection<edge_data> edges = graph.getE(node_data.getKey());
					for (edge_data edge : edges) {
						Graphics2D g2d = (Graphics2D)g;
						Stroke bs = g2d.getStroke();
						if(edge.getTag() == 999)
						{
							edge.setTag(0);
							g2d.setStroke(new BasicStroke(5));
							g.setColor(Color.GREEN);
						}
						else
						{
							g.setColor(Color.RED);
						}
						node_data dest = graph.getNode(edge.getDest());
						Point3D pd = dest.getLocation();
						if(pd!=null)
						{
							g.drawLine(p.ix()+5, p.iy()+5, pd.ix()+5, pd.iy()+5);
							g.drawString(edge.getWeight()+"", (p.ix()+pd.ix())/2, (p.iy()+pd.iy())/2);
							g.setColor(Color.YELLOW);
							int xhalf = (((p.ix()+pd.ix())/2)+pd.ix())/2;
							int yhalf = (((p.iy()+pd.iy())/2)+pd.iy())/2;
							g.fillOval((xhalf+pd.ix())/2, (yhalf+pd.iy())/2, 5, 5);
						}
						g2d.setStroke(bs);
					}
				}
			}
		}
	}

	/**
	 * With a given txt file creating a Graph, should be serializable from a Graph. 
	 */
	private void DrawFromFile() {
		Graph_Algo ga = new Graph_Algo();
		JFileChooser jf = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		int returnV = jf.showOpenDialog(null);
		if (returnV == JFileChooser.APPROVE_OPTION) {
			File selectedFile = jf.getSelectedFile();
			ga.init(selectedFile.getAbsolutePath());
			this.graph = ga.copy();
			repaint();
		}
	}
	/**
	 * Saving the current graph into a unreable deserializable file.
	 */
	private void saveToFile()
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
	private void shortestPath()
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
			repaint();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Asks the user a From and a TO and tries to re-create the shortest path
	 * Will show the shortest path length in a Message dialog
	 */
	private void ShortestPathDist()
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
	private void TSPEnterNodes()
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
		repaint();
	}
	/**
	 * with a given file in the format x,y,z,t,y...  will try to create the shortest path 
	 * through all the nodes in the file
	 */
	private void TSPfromFile()
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
					repaint();
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		String act = e.getActionCommand();
		switch (act) {
		case "Draw graph":
			repaint();
			break;
		case "Draw from file":DrawFromFile();
		break;
		case "Save to file" :saveToFile();
		break;
		case "find Shortest path" : shortestPath();
		break;
		case "find Shortest path distance" : ShortestPathDist();
		break;
		case "TSP enter all the nodes" : TSPEnterNodes();
		break;
		case "TSP from file" : TSPfromFile();
		break;
		default:
			break;
		}
	}
	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
