package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataAccessObject {
	protected Connection connection;
	protected PreparedStatement pstmt;
	protected ResultSet rs;
	
	public DataAccessObject() {
		this.connection = null;
		this.pstmt = null;
		this.rs = null;
	}

	/*Process 1 ~ Process 2*/
	public void dbOpen() {
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@141.164.39.76:1521:xe";
		String id = "HOONZZANG";
		String pwd = "1234";

		try {
			Class.forName(driver);
			connection = DriverManager.getConnection(url, id, pwd);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/* Process 8 */
	public void dbClose() {
		try {
			if(!connection.isClosed()) {
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/* Transaction Configuration */
	public void setTranConf(boolean tran) {
		try {
			connection.setAutoCommit(tran);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/* Transaction */
	public void setTran(boolean tran) {
		try {
			if(tran) {
				connection.commit();
			}else {
				connection.rollback();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
