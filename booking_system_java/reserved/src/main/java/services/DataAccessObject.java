package services;

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


	//process 1 ~ process 2 
	void dbOpen() {
		String driver = "oracle.jdbc.driver.OracleDriver";
		//DBA 주소
		String url = "jdbc:oracle:thin:@192.168.0.182:1521:xe";
		//개발자 계정
		String id = "DINGDONG";
		String pwd = "1234";


		try {
			Class.forName(driver);
			connection = DriverManager.getConnection(url,id,pwd);
		}catch (Exception e) {
			e.printStackTrace();
		}

	}

	//process 8 
	void dbClose() {
		try {
			if(!connection.isClosed()) {
				connection.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	//아이디 존재 여부 확인 
	int isUserId(Member member) {
		int result =0;
		String query = "SELECT COUNT(*) AS ISID FROM TB_CON WHERE CON_ID = ?";
		try {
			pstmt = connection.prepareStatement(query);
			//db는 index가 0이 아니라 1번부터 시작함 
			pstmt.setNString(1, member.getMemberId());
			rs = pstmt.executeQuery();
			//netxt()는 다음 record가 없으면 return false 
			while(rs.next()) {
				result = rs.getInt("ISID");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}

	int isRestaurantCode(Member member) {
		int result =0;
		String query = "SELECT COUNT(*) AS ISID FROM RE WHERE RE_CODE = ?";

		try {
			pstmt = connection.prepareStatement(query);
			pstmt.setNString(1,  member.getMemberId());
			rs =pstmt.executeQuery();
			while(rs.next()) {
				result = rs.getInt("ISID");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	int isAccess(Member member) {
		int result = 0;
		String query = "SELECT COUNT(*) AS ISUSER FROM TB_CON WHERE CON_ID = ? AND CON_PASSWORD = ?";
		try {
			pstmt = connection.prepareStatement(query);
			//db는 index가 0이 아니라 1번부터 시작함 
			pstmt.setNString(1, member.getMemberId());
			pstmt.setNString(2, member.getMemberPwd());
			rs = pstmt.executeQuery();
			//netxt()는 다음 record가 없으면 return false 
			while(rs.next()) {
				result = rs.getInt("ISUSER");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}



		return result;
	}

	int isRestaurantAccess(Member member) {
		int result =0;
		String query = "SELECT COUNT(*) AS ISUSER FROM RE WHERE RE_CODE =? AND RE_ACCESS =?";

		try {
			pstmt = connection.prepareStatement(query);
			pstmt.setNString(1,member.getMemberId());
			pstmt.setNString(2,member.getMemberPwd());
			rs= pstmt.executeQuery();

			while(rs.next()) {
				//result = rs.getInt("ISUSER");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;


	}


	void getUserInfo(Member member) {
		String result = null;
		String query = "SELECT CON_NAME AS CONNAME FROM TB_CON WHERE CON_ID = ?";
		try {
			pstmt = connection.prepareStatement(query);
			//db는 index가 0이 아니라 1번부터 시작함 
			pstmt.setNString(1, member.getMemberId());
			rs = pstmt.executeQuery();
			//netxt()는 다음 record가 없으면 return false 
			while(rs.next()) {
				member.setMemberName(rs.getNString("CONNAME"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}


	}

	void getRestaurantInfo(Member member) {
		String query = "SELECT * FROM REINFO WHERE RCODE = ?";
		try {
			pstmt = connection.prepareStatement(query);
			pstmt.setNString(1,member.getMemberId());
			rs = pstmt.executeQuery();

			while(rs.next()) {
				member.setMemberName(rs.getNString(1));
				member.setMemberEtc(rs.getNString(2));
				member.setCategoryCode(rs.getNString(3));
				member.setCategory(rs.getNString(4));
				member.setLocation(rs.getNString(5));
				}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}



	//아이디 존재 여부 확인 
	int isResId(Member member) {
		int result =0;
		String query ="SELECT COUNT(*) AS ISID FROM REINFO WHERE RCODE = ?";
		try {
			pstmt = connection.prepareStatement(query);
			//db는 index가 0이 아니라 1번부터 시작함 
			pstmt.setNString(1, member.getMemberId());
			rs = pstmt.executeQuery();
			//netxt()는 다음 record가 없으면 return false 
			while(rs.next()) {
				result = rs.getInt("ISID");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}

	int isResAccess(Member member) {
		int result = 0;
		String query = "SELECT COUNT(*) AS ISUSER FROM TB_ST WHERE ST_CODE = ? AND ST_ACCESS = ?";
		try {
			pstmt = connection.prepareStatement(query);
			//db는 index가 0이 아니라 1번부터 시작함 
			pstmt.setNString(1, member.getMemberId());
			pstmt.setNString(2, member.getMemberPwd());
			rs = pstmt.executeQuery();
			//netxt()는 다음 record가 없으면 return false 
			while(rs.next()) {
				result = rs.getInt("ISUSER");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}



		return result;
	}

	void getResInfo(Member member) {
		String result = null;
		//
		String query = "SELECT * FROM REINFO WHERE RCODE = ?";
		try {
			pstmt = connection.prepareStatement(query);
			//db는 index가 0이 아니라 1번부터 시작함 
			pstmt.setNString(1, member.getMemberId());
			rs = pstmt.executeQuery();
			//netxt()는 다음 record가 없으면 return false 
			while(rs.next()) {
				member.setMemberName(rs.getNString("RNAME"));
				member.setMemberEtc(rs.getNString("RCEO"));
				member.setCategoryCode(rs.getNString("RCATE"));
				member.setCategory(rs.getNString("CATNAME"));
				member.setLocation(rs.getNString("RLOCATEd"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	int setNewMember(Member member) {
		int result = 0;
		String query = member.getMemberEtc() != null? 
				"INSERT INTO TB_CON(CON_ID,CON_PASSWORD, CON_NAME, CON_PHONE) VALUES(?,?,?,?)":
					"INSERT INTO TB_CON(CON_ID,CON_PASSWORD, CON_NAME, CON_PHONE) VALUES(?,?,?,DEFAULT)";

		try {
			pstmt = connection.prepareStatement(query);
			pstmt.setNString(1, member.getMemberId());
			pstmt.setNString(2, member.getMemberPwd());
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
		String query = "INSERT INTO TB_ST(ST_CODE, ST_NAME, ST_CEO, ST_CATE, ST_ACCESS, ST_LOCATE) "
				+ "VALUES(?, ?, ?, ?, ?, ?)"; 
		
		try {
			pstmt = connection.prepareStatement(query);
			pstmt.setNString(1, member.getMemberId());
			pstmt.setNString(5, member.getMemberPwd());
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
