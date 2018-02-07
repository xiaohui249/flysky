package com.sean.flysky.sqlite;

import java.sql.*;

public class SQLiteJDBC {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Connection c = null;   
        try {   
            Class.forName("org.sqlite.JDBC");   
            c = DriverManager.getConnection("jdbc:sqlite:D:/sqlite/mydb.db");   
            c.setAutoCommit(false);   
  
            Statement st = c.createStatement();   
            int count = st.executeUpdate("update user set sex=1 where id<=5"); 
            System.out.println(count);
               
            ResultSet rs = st.executeQuery("SELECT * FROM user");   
            while (rs.next()) {   
                int id = rs.getInt(1);   
                String name = rs.getString(2);
                int sex = rs.getInt(3);
                System.out.println("id=" + id + ", name=" + name + ", sex=" + sex);   
            }   
            rs.close();   
            st.close();    
  
            c.commit();   
            c.close();   
        } catch (Exception e) {   
            e.printStackTrace();   
            System.exit(1);   
            try {   
                if (c != null && !c.isClosed()) {   
                    c.rollback();   
                    c.close();   
                }   
            } catch (SQLException sql) {   
                // do nothing   
            }   
        }

	}

}
