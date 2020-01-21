package gameClient;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class dbConnector {
	public static final String jdbcUrl="jdbc:mysql://db-mysql-ams3-67328-do-user-4468260-0.db.ondigitalocean.com:25060/oop?useUnicode=yes&characterEncoding=UTF-8&useSSL=false";
	public static final String jdbcUser="student";
	public static final String jdbcUserPassword="OOP2020student";


	public static Object[][] GetData() {
		ResultSet rs=null;
		Object[][] data = new Object[50][6];
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = 
					DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
			Statement statement = connection.createStatement();
			String allCustomersQuery = "SELECT * FROM Logs WHERE UserID=321711061;";
			//String allCustomersQuery = "SELECT * FROM Logs";
			rs = statement.executeQuery(allCustomersQuery);
			try {
				int counter = 0;
				//rs.
				while(rs.next() && counter<50)
				{
					//LogObj = new Log...
					data[counter][0] = rs.getInt("levelID");
					data[counter][1] = rs.getInt("moves");
					data[counter][2] = rs.getDate("time");
					data[counter][3] = rs.getDouble("score");
					data[counter][4] = rs.getInt("UserID");
					data[counter][5] = rs.getInt("logID");
					counter++;
				}
			}
			catch (Exception e) {
				// TODO: handle exception
			}

			rs.close();
			statement.close();		
			connection.close();

		}

		catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("Vendor Error: " + sqle.getErrorCode());
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return data;
	}
	public static int[] getNumbergames(int id)
	{
		ResultSet rs=null;
		int[] ans = new int[2];
		int max = Integer.MIN_VALUE;
		int counter = 0;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = 
					DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
			Statement statement = connection.createStatement();
			String allCustomersQuery = "SELECT * FROM Logs WHERE UserID="+id+";";
			//String allCustomersQuery = "SELECT * FROM Logs";
			rs = statement.executeQuery(allCustomersQuery);
			try {

				//rs.
				while(rs.next())
				{
					int temp = rs.getInt("levelID");
					if(temp>max)
					{
						max = temp;
					}
					counter++;
				}
			}
			catch (Exception e) {
				// TODO: handle exception
			}

			rs.close();
			statement.close();		
			connection.close();

		}

		catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("Vendor Error: " + sqle.getErrorCode());
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		ans[0] = counter;
		ans[1] = max;
		return ans;
	}
	public static double[][] bestResult(int id)
	{
		ResultSet rs=null;
		double[][] ans = new double[12][4];
		for (int i = 0; i < ans.length; i++) {

			if(i!=10)
			{
				ans[i][0] = Integer.MIN_VALUE;
				ans[i][1] = amountScore(i);
				ans[i][3] = amountMovesOther(i);
			}
		}
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = 
					DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
			Statement statement = connection.createStatement();
			String allCustomersQuery = "SELECT * FROM Logs WHERE UserID="+id+";";
			//String allCustomersQuery = "SELECT * FROM Logs";
			rs = statement.executeQuery(allCustomersQuery);
			try {

				//rs.
				while(rs.next())
				{
					int LevelID = rs.getInt("levelID");
					int moves = rs.getInt("moves");
					if(moves<= amountMoves(LevelID))
					{
						double Score = rs.getDouble("score");
						if(Score > ans[whichStageReverse(LevelID)][0])
						{
							ans[whichStageReverse(LevelID)][0] = Score;
							ans[whichStageReverse(LevelID)][2] = moves;
						}
					}

				}
			}
			catch (Exception e) {
				// TODO: handle exception
			}

			rs.close();
			statement.close();		
			connection.close();

		}

		catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("Vendor Error: " + sqle.getErrorCode());
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return ans;
	}

	public static int[] findRank(double[][] helpArr)
	{
		ResultSet rs=null;
		int[] ans =new int[12];
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = 
					DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
			Statement statement = connection.createStatement();
			String base = "SELECT * FROM Logs WHERE ";
			for (int i = 0; i <= 11; i++) {
				if(i!=10)
				{
					if(i==0)
					{
						base += "(LevelID="+whichStage(i)+" AND score > "+helpArr[i][0]+" AND moves <= "+helpArr[i][3]+")";
					}
					else
					{
						base+= " OR (LevelID="+whichStage(i)+" AND score > "+helpArr[i][0]+" AND moves <= "+helpArr[i][3]+")";
					}
				}
			}
			base+=";";
//			String allCustomersQuery = "SELECT * FROM Logs WHERE "
//					+ "(LevelID=0 AND score > "+helpArr[0][0]+" AND moves <= "+helpArr[0][3]+")"
//					+ " OR (LevelID=1 AND score > "+helpArr[1][0]+" AND moves <= "+helpArr[1][3]+")"
//					+ " OR (LevelID=3 AND score > "+helpArr[2][0]+" AND moves <= "+helpArr[2][3]+")"
//					+ " OR (LevelID=5 AND score > "+helpArr[3][0]+" AND moves <= "+helpArr[3][3]+")" 
//					+ " OR (LevelID=9 AND score > "+helpArr[4][0]+" AND moves <= "+helpArr[4][3]+")"
//					+ " OR (LevelID=11 AND score > "+helpArr[5][0]+" AND moves <= "+helpArr[5][3]+")"
//					+ " OR (LevelID=13 AND score > "+helpArr[6][0]+" AND moves <= "+helpArr[6][3]+")"
//					+ " OR (LevelID=16 AND score > "+helpArr[7][0]+" AND moves <= "+helpArr[7][3]+")"
//					+ " OR (LevelID=19 AND score > "+helpArr[8][0]+" AND moves <= "+helpArr[8][3]+")"
//					+ " OR (LevelID=20 AND score > "+helpArr[9][0]+" AND moves <= "+helpArr[9][3]+")"
//					+ " OR (LevelID=23 AND score > "+helpArr[11][0]+" AND moves <= "+helpArr[11][3]+");";
			String allCustomersQuery = base;
			//String allCustomersQuery = "SELECT * FROM Logs";
			rs = statement.executeQuery(allCustomersQuery);
			try {
				while(rs.next())
				{
					int LevelID = rs.getInt("levelID");
					ans[whichStageReverse(LevelID)]++;
				}
			}
			catch (Exception e) {
			}
			rs.close();
			statement.close();		
			connection.close();
		}

		catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("Vendor Error: " + sqle.getErrorCode());
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return ans;
	}
	private static double amountMovesOther(int i) {
		switch (i) {
		case 0 : 
			return 290;
		case 1 : 
			return 580;
		case 2 : 
			return 580;
		case 3 : 
			return 500;
		case 4 : 
			return 580;
		case 5 : 
			return 580;
		case 6 : 
			return 580;
		case 7 : 
			return 290;
		case 8 : 
			return 580;
		case 9 : 
			return 290;
		case 11 : 
			return 1140;
		default :
			return -1;
		}
	}
	private static int amountScore(int index) {
		switch (index) {
		case 0 : 
			return 145;
		case 1 : 
			return 450;
		case 2 : 
			return 720;
		case 3 : 
			return 570;
		case 4 : 
			return 510;
		case 5 : 
			return 1050;
		case 6 : 
			return 310;
		case 7 : 
			return 235;
		case 8 : 
			return 250;
		case 9 : 
			return 200;
		case 11 : 
			return 1000;
		default :
			return -1;
		}
	}
	private static int amountMoves(int index) {
		switch (index) {
		case 0 : 
			return 290;
		case 1 : 
			return 580;
		case 3 : 
			return 580;
		case 5 : 
			return 500;
		case 9 : 
			return 580;
		case 11 : 
			return 580;
		case 13 : 
			return 580;
		case 16 : 
			return 290;
		case 19 : 
			return 580;
		case 20 : 
			return 290;
		case 23 : 
			return 1140;
		default :
			return -1;
		}
	}
	private static int whichStageReverse(int index) {
		switch (index) {
		case 0 : 
			return 0;
		case 1 : 
			return 1;
		case 3 : 
			return 2;
		case 5 : 
			return 3;
		case 9 : 
			return 4;
		case 11 : 
			return 5;
		case 13 : 
			return 6;
		case 16 : 
			return 7;
		case 19 : 
			return 8;
		case 20 : 
			return 9;
		case 23 : 
			return 11;
		default :
			return -1;
		}
	}
	private static int whichStage(int index) {
		switch (index) {
		case 0 : 
			return 0;
		case 1 : 
			return 1;
		case 2 : 
			return 3;
		case 3 : 
			return 5;
		case 4 : 
			return 9;
		case 5 : 
			return 11;
		case 6 : 
			return 13;
		case 7 : 
			return 16;
		case 8 : 
			return 19;
		case 9 : 
			return 20;
		case 11 : 
			return 23;
		default :
			return -1;
		}
	}
}
