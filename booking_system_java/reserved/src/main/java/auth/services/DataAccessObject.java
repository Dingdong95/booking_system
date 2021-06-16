package auth.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import beans.Member;

class DataAccessObject {
	Connection connection;
	PreparedStatement pstmt;
	ResultSet rs;
	
	DataAccessObject() {
		this.connection = null;
		this.pstmt = null;
		this.rs = null;
	}

	/*Process 1 ~ Process 2*/
	void dbOpen() {
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@192.168.0.182:1521:xe";
		String id = "DINGDONG";
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
	void dbClose() {
		try {
			if(!connection.isClosed()) {
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/* 아이디 존재 여부 */
	int isUserId(Member member) {
		int result = 0;
		String query = "SELECT COUNT(*) AS ISID FROM TB_CON WHERE CON_ID = ?";
				
		try {
			pstmt = connection.prepareStatement(query);
			pstmt.setNString(1, member.getMemberId());
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				result = rs.getInt("ISID");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	int isRestaurantCode(Member member) {
		int result = 0;
		String query = "SELECT COUNT(*) AS ISID FROM TB_ST WHERE ST_CODE = ?";
				
		try {
			pstmt = connection.prepareStatement(query);
			pstmt.setNString(1, member.getMemberId());
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				result = rs.getInt("ISID");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	/* 아이디 패스워드 일치 여부 */
	int isAccess(Member member) {
		int result = 0;
		String query = "SELECT COUNT(*) AS ISUSER FROM TB_CON WHERE CON_ID = ? AND CON_PASSWORD = ?";
		try {
			pstmt = connection.prepareStatement(query);
			pstmt.setNString(1, member.getMemberId());
			pstmt.setNString(2, member.getMemberPassword());
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				result = rs.getInt("ISUSER");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	int isRestaurantAccess(Member member) {
		int result = 0;
		String query = "SELECT COUNT(*) AS ISUSER FROM TB_ST WHERE ST_CODE = ? AND ST_ACCESS = ?";
		try {
			pstmt = connection.prepareStatement(query);
			pstmt.setNString(1, member.getMemberId());
			pstmt.setNString(2, member.getMemberPassword());
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				result = rs.getInt("ISUSER");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	/* 로그인된 사용자 정보 가져오기*/
	void getUserInfo(Member member) {
		String result = null;
		String query = "SELECT CU_NAME AS CUNAME FROM TB_CON WHERE CON_ID = ?";
		try {
			pstmt = connection.prepareStatement(query);
			pstmt.setNString(1, member.getMemberId());
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				member.setMemberName(rs.getNString("CUNAME"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	void getRestaurantInfo(Member member) {
		String query = "SELECT * FROM REINFO WHERE RCODE = ?";
		try {
			pstmt = connection.prepareStatement(query);
			pstmt.setNString(1, member.getMemberId());
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				member.setMemberName(rs.getNString("RNAME"));
				member.setMemberEtc(rs.getNString("RCEO"));
				member.setCategoryCode(rs.getNString("RCATE"));
				member.setCategory(rs.getNString("FCNAME"));
				member.setLocation(rs.getNString("RLOCATE"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	int setNewMember(Member member) {
		int result = 0;
		String query = member.getMemberEtc() != null? "INSERT INTO CU(CU_ID, CU_PASSWORD, CU_NAME, CU_PHONE) VALUES(?, ?, ?, ?)": "INSERT INTO CU(CU_ID, CU_PASSWORD, CU_NAME, CU_PHONE) VALUES(?, ?, ?, DEFAULT)";
		
		try {
			pstmt = connection.prepareStatement(query);
			pstmt.setNString(1, member.getMemberId());
			pstmt.setNString(2, member.getMemberPassword());
			pstmt.setNString(3, member.getMemberName());
			if(member.getMemberEtc() != null) {
				pstmt.setNString(4, member.getMemberEtc());
			}
						
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	int setNewRestaurantMember(Member member) {
		int result = 0;
		String query = "INSERT INTO RE(RE_CODE, RE_NAME, RE_CEO, RE_CATE, RE_ACCESS, RE_LOCATE) "
				+ "VALUES(?, ?, ?, ?, ?, ?)"; 
		
		try {
			pstmt = connection.prepareStatement(query);
			pstmt.setNString(1, member.getMemberId());
			pstmt.setNString(5, member.getMemberPassword());
			pstmt.setNString(2, member.getMemberName());
			pstmt.setNString(3, member.getMemberEtc());
			pstmt.setNString(4, member.getCategoryCode());
			pstmt.setNString(6, member.getLocation());
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
}

/* JAVA :: JDBC + ORACLE :: OJDBC6.JAR
 * *** Tomcat\lib\ojdbc6
 * 1. Driver Loading >> Class.forName(driver) >> oracle.jdbc.driver.OracleDriver 
 * 2. Driver Manager를 통한 Oracle 접속 >> Connection, DriverManger
 * 3. DML | Query작성
 * 4. Statement | PreparedStatement 에 query전달
 * 5. Execute >> DML:: executeUpdate     Query :: ExecuteQuery 
 * 6. Oracle >> return >> Query :: ResultSet    DML :: Integer
 * (7). ResultSet :: fetch --> java object에 저장
 * 8. Connection close
 *     
 */







