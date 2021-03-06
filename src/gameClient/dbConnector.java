package gameClient;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;

public class dbConnector {
	public static final String jdbcUrl="jdbc:mysql://db-mysql-ams3-67328-do-user-4468260-0.db.ondigitalocean.com:25060/oop?useUnicode=yes&characterEncoding=UTF-8&useSSL=false";
	public static final String jdbcUser="student";
	public static final String jdbcUserPassword="OOP2020student";

	/**
	 * by a given id this method returns an array amount of games we 
	 * have played and the maximum level reached. 
	 * @param id
	 * @return
	 */
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
			rs = statement.executeQuery(allCustomersQuery);
			try {

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
	/**
	 * by a given id this method returns a matrix [12][4] which  the rows 
	 * represents the levels and the columns represents the score to beat, 
	 * the score that the user achieved, the maximum moves, and the moves 
	 * the user reached. 
	 * @param id
	 * @return
	 */
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
			rs = statement.executeQuery(allCustomersQuery);
			try {

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
/**
by a given a matrix with the information about the moves and scores of the user this method will return 
an array filled with the rank of each level in relation towards the rest of the participants in this 
project. Each position in the array represents the level.
In this method we used a hashtable of a hsastable to help us keep the data of the users. 
The key of the first map represents the level and the key of the second map representys the user ID

 * @param helpArr
 * @return
 */
	public static int[] findRank(double[][] helpArr)
	{
		Hashtable<Integer, Hashtable<Integer, Integer>> hashIDs = new Hashtable<Integer, Hashtable<Integer,Integer>>();
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
		
			String allCustomersQuery = base;
			rs = statement.executeQuery(allCustomersQuery);
			try {
				while(rs.next())
				{

					int LevelID = rs.getInt("levelID");
					if(hashIDs.get(LevelID) == null)
					{
						hashIDs.put(LevelID, new Hashtable<Integer, Integer>());
					}
					Hashtable<Integer, Integer> tempHashIdByLevelID = hashIDs.get(LevelID);
					int userID = rs.getInt("UserID");
					if(tempHashIdByLevelID.get(userID) == null)
					{
						ans[whichStageReverse(LevelID)]++;
						tempHashIdByLevelID.put(userID, userID);
					}
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
	/**
	 * – By a given case number this method helps us translate the case number to its 
	 * max moves allowed for that specific case and returns the value.
	 * @param i
	 * @return
	 */
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
	/**
	 * By a given case number this method helps us translate the case number to 
	 * its minimum score allowed for that specific case and returns the score.
	 * @param index
	 * @return
	 */
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
	/**
	 * By a given level this method helps us translate the level to its max 
	 * moves allowed for that specific level and returns the amount of moves.
	 * @param index
	 * @return
	 */
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
	/**
	 * By a given level this method helps us translate the level to its 
	 * case number which represents the given level and returns it.
	 * @param index
	 * @return
	 */
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
	/**
	 * By a given a case number this method helps us translate the case number 
	 * to its level number which represents the given case number and returns it.
	 * @param index
	 * @return
	 */
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
