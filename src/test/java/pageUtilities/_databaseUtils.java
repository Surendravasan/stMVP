package pageUtilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

public class _databaseUtils {
	
	private static _databaseUtils instance;
	private static final Object lock = new Object();
	public static Connection con;
	public static Statement st;
	
	public static _databaseUtils getInstance() {
		if (instance == null) {
			synchronized (lock) {
				instance = new _databaseUtils();
				instance.openDbConn();
			}
		}
		return instance;
	}
	
	private void openDbConn() {
		String DB_URL ="jdbc:sqlserver://35.167.107.202:3421;databaseName=StorTrack_Optimize_Staging";
		
		try {
			//Class.forName("net.sourceforge.jtds.jdbc.Driver");	
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			con = DriverManager.getConnection(DB_URL, "surendravasan364", "WD2wmu8b");
			
			//Creating statement object
			} catch (Exception e) {
				System.out.println("OpenDbConn: Database Connection Failed "+ e);
			}
		}
	
	public static HashMap<String, String> getColumnValues(String query) {
		HashMap<String, String> map = new HashMap<String, String>();
		
		
		try {
		st = con.createStatement();
		ResultSet rs = st.executeQuery(query);
		int count = rs.getMetaData().getColumnCount();
		while(rs.next()) {

			for(int i=1; i<=count; i++) {
				map.put(rs.getMetaData().getColumnName(i), rs.getString(i));
			}
		} 
		} catch (Exception e) {
			System.out.println("getColumnValues: Database Connection Failed "+e);
			
			
		}
		return map;
	}
//	
//	public static ArrayList<Integer> getRowValues(String query) {
//		ArrayList<Integer> list = new ArrayList<Integer>();
//		
//		
//		try {
//		st = con.createStatement();
//		String selectquery = query;
//		//Executing the SQL Query and store the results in ResultSet
//		ResultSet rs = st.executeQuery(selectquery);
//		//While loop to iterate through all data and print results
//		while(rs.next()) {
//			int result;
//
//			result = rs.getInt(1); 
//	        list.add(result);
//			}
//		} catch (Exception e) {
//			System.out.println("getRowValues: Database Connection Failed "+e);
//			
//			
//		}
//		
//		return list;
//	}
//	
	public static HashMap<String, String> getMapString(String query) {
		HashMap<String, String> map = new HashMap<String, String>();
		
		try {
		st = con.createStatement();
		String selectquery = query;
		ResultSet rs = st.executeQuery(selectquery);
		while(rs.next()) {
				map.put(rs.getString(1), rs.getString(2));
		} 
		} catch (Exception e) {
			System.out.println("getColumnValues: Database Connection Failed "+e);
		}
		return map;
		
		
	}
//	
//	public static TreeSet<Integer> getValue(String query) {
//		TreeSet<Integer> ts = new TreeSet<Integer>();
//		try {
//			st = con.createStatement();
//			ResultSet rs = st.executeQuery(query);
//			
//			while(rs.next()) {
//				ts.add(rs.getInt(1));
//			}
//		} catch (Exception e) {
//			System.out.println("getSingleValue: Database Connection Failed "+e);
//		}
//		return ts;
//	}
//	
//	
////	testing
//	public static List<String> getList(String query) {
//		List<String> ts = new LinkedList<>();
//		Float f = 0f;
//		try {
//			st = con.createStatement();
////			st.setQueryTimeout(5);
////			Stopwatch stopwatch = Stopwatch.createStarted();
//			ResultSet rs = st.executeQuery(query);
////			stopwatch.stop();
////			System.out.println("Time elapsed: "+ stopwatch.elapsed(TimeUnit.SECONDS));
//			while(rs.next()) {
//				f = rs.getFloat(1);
//				String s = String.valueOf(f); 
//				ts.add((s.endsWith(".0") ? s.replace(".0", "") : s.substring(0)));
//			}
//		} catch (Exception e) {
//			
//			System.out.println("getSingleValue: Database Connection Failed "+e);
//		}
//		return ts;
//	}
//	
//	
	public static Date getDate(String query) {
		Date d = null;
		try {
			st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			while(rs.next()) {
				d = rs.getDate(1);
			}
		} catch (Exception e) {
			System.out.println("getSingleValue: Database Connection Failed "+e);
		}
		return d;
	}
//	
//	public static int getIntValue(String query) {
//		int value = 0;
//		try {
//			_databaseUtils.st = _databaseUtils.con.createStatement();
//			ResultSet rs = _databaseUtils.st.executeQuery(query);
//			while(rs.next()) {
//				value = rs.getInt(1);
//			}
//		} catch (Exception e) {
//			System.out.println("getSingleValue: Database Connection Failed "+e);
//		}
//		return value;
//	}
//	
//	
	public static String getStringValue(String query) {
		String db = "";
		String s = "";
		try {
			_databaseUtils.st = _databaseUtils.con.createStatement();
			ResultSet rs = _databaseUtils.st.executeQuery(query);
			while(rs.next()) {
				db = rs.getString(1);
				s = s+" "+db;
			}
		} catch (Exception e) {
			System.out.println("getSingleValue: Database Connection Failed "+e);
		}
		return s.trim();
	}
//	
//	
//	
//	public static TreeSet<Float> getFloatSet(String query) {
//		TreeSet<Float> set = new TreeSet<>();
//		
//		try {
//			_databaseUtils.st = _databaseUtils.con.createStatement();
//			ResultSet rs = _databaseUtils.st.executeQuery(query);
//			while(rs.next()) {
//				set.add(rs.getFloat(1));
//			}
//		} catch (Exception e) {
//			System.out.println("getSingleValue: Database Connection Failed "+e);
//		}
//		return set;
//	}
	
}
