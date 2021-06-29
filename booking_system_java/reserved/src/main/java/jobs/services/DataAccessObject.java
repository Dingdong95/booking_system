package jobs.services;

import java.sql.SQLException;
import java.util.ArrayList;

import beans.ReservedInfo;
import beans.RestaurantInfo;
import beans.UserReservedInfo;
import beans.UserReservedMenuInfo;


class DataAccessObject extends controller.DataAccessObject{
	DataAccessObject(){
		super();
	}

	ArrayList<RestaurantInfo> getRestaurantInfo(RestaurantInfo ri) {
		ArrayList<RestaurantInfo> restaurantList = new ArrayList<RestaurantInfo>();
		String query = "SELECT * FROM SEARCHLIST WHERE SWORD LIKE '%' || ? || '%'";

		try {
			pstmt = connection.prepareStatement(query);
			pstmt.setNString(1, ri.getWord());
			rs = pstmt.executeQuery();

			while(rs.next()) {
				RestaurantInfo record = new RestaurantInfo();
				record.setReCode(rs.getNString("RECODE"));
				record.setRestaurant(rs.getNString("RESTAURANT"));
				record.setLocation(rs.getNString("LOCATION"));
				record.setCatagory(rs.getNString("CATEGORY"));
				record.setMenuCode(rs.getNString("MENUCODE"));
				record.setMenu(rs.getNString("MENU"));
				record.setPrice(rs.getInt("PRICE"));
				record.setGpa(rs.getInt("GPA"));
				record.setCount(rs.getInt("COUNT"));
				record.setComment(rs.getNString("COMMENTS"));
				restaurantList.add(record);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return restaurantList;
	}

	public ArrayList<ReservedInfo> getWaitingInfo(ReservedInfo ri){
		ArrayList<ReservedInfo> list = new ArrayList<ReservedInfo>();
		String query = "SELECT  RECODE, RE.RE_NAME AS RNAME, CUID, CNAME, TO_CHAR(RL.RDATE, 'YYYYMMDDHH24MISS') AS DBDATE, "
				+ "TO_CHAR(RDATE, \'YYYY-MM-DD HH24:MI:SS\') AS RDATE, COUNT AS MCOUNT "
				+ " FROM RESERVELIST RL INNER JOIN RE ON RL.RECODE = RE.RE_CODE "
				+ " WHERE RECODE = ? AND PROCESS = ?";

		try {
			pstmt = connection.prepareStatement(query);
			pstmt.setNString(1, ri.getReCode());
			pstmt.setNString(2, ri.getProcess());

			rs = pstmt.executeQuery();
			while(rs.next()) {
				ReservedInfo rInfo = new ReservedInfo();
				rInfo.setReCode(rs.getNString("RECODE"));
				rInfo.setReName(rs.getNString("RNAME"));
				rInfo.setCuId(rs.getNString("CUID"));
				rInfo.setCuName(rs.getNString("CNAME"));
				rInfo.setrDate(rs.getNString("RDATE"));
				rInfo.setDbDate(rs.getNString("DBDATE"));
				rInfo.setmCount(rs.getInt("MCOUNT"));

				list.add(rInfo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public ArrayList<ReservedInfo> getTodayReservedInfo(ReservedInfo ri){
		ArrayList<ReservedInfo> list = new ArrayList<ReservedInfo>();
		String query = "SELECT * FROM RLISTBYDATE WHERE RECODE = ? AND TO_CHAR(ODDATE, 'YYYYMMDD') = TO_CHAR(SYSDATE, 'YYYYMMDD')";

		try {
			pstmt = connection.prepareStatement(query);
			pstmt.setNString(1, ri.getReCode());

			rs = pstmt.executeQuery();
			while(rs.next()) {
				ReservedInfo rInfo = new ReservedInfo();
				rInfo.setReCode(rs.getNString("RECODE"));
				rInfo.setReName(rs.getNString("RENAME"));
				rInfo.setCuId(rs.getNString("CUID"));
				rInfo.setCuName(rs.getNString("CUNAME"));
				rInfo.setPhone(rs.getNString("PHONE"));
				rInfo.setmCode(rs.getNString("MNCODE"));
				rInfo.setmName(rs.getNString("MENU"));
				rInfo.setQuantity(rs.getInt("QUANTITY"));
				rInfo.setrDate(rs.getNString("ODDATE"));

				list.add(rInfo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	int confirmReserve(ReservedInfo ri) {
		int result = 0;
		String query = "UPDATE OS SET OS_PROCESS = 'C' WHERE OS_RECODE = ? AND OS_CUID = ? AND TO_CHAR(OS_DATE, 'YYYYMMDDHH24MISS') = ?";

		try {
			pstmt = connection.prepareStatement(query);
			pstmt.setNString(1, ri.getReCode());
			pstmt.setNString(2, ri.getCuId());
			pstmt.setNString(3, ri.getDbDate());

			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}


	ArrayList<String> getReservedDate(ReservedInfo ri){
		ArrayList<String> reservedList = new ArrayList<String>();
		String query = "SELECT TO_CHAR(OS_DATE, 'YYYYMMDD') AS RESERVED "
				+ "		FROM OS WHERE OS_RECODE = ? AND OS_PROCESS = ? AND "
				+ "		(TO_CHAR(OS_DATE, 'YYYYMMDD') > TO_CHAR(SYSDATE, 'YYYYMMDD') AND "
				+ "		TO_CHAR(OS_DATE, 'YYYYMMDD') <= TO_CHAR(SYSDATE + 7, 'YYYYMMDD'))";
		try {
			pstmt = connection.prepareStatement(query);
			pstmt.setNString(1, ri.getReCode());
			pstmt.setNString(2, ri.getProcess());

			rs = pstmt.executeQuery();
			while(rs.next()) {
				reservedList.add(rs.getNString("RESERVED"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return reservedList;
	}


	ArrayList<RestaurantInfo> getMenu(ReservedInfo ri){
		ArrayList<RestaurantInfo> menu = new ArrayList<RestaurantInfo>();
		String query = "SELECT * FROM MENUINFO WHERE RECODE = ?";

		try {
			pstmt = connection.prepareStatement(query);
			pstmt.setNString(1, ri.getReCode());
			rs = pstmt.executeQuery();

			while(rs.next()) {
				RestaurantInfo resi = new RestaurantInfo();
				resi.setReCode(rs.getNString("RECODE"));
				resi.setRestaurant(rs.getNString("RESTAURANT"));
				resi.setMenuCode(rs.getNString("MNCODE"));
				resi.setMenu(rs.getNString("MENUNAME"));
				resi.setPrice(rs.getInt("PRICE"));
				menu.add(resi);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return menu;

	}

	int insOrder(ReservedInfo ri) {
		int result = 0;
		String query = "INSERT INTO OS(OS_RECODE, OS_DATE,OS_PROCESS,OS_CUID)" +
				" VALUES(?, TO_DATE(?,''YYYYMMDD),?,?)";
		try {
			pstmt = connection.prepareStatement(query);
			pstmt.setNString(1, ri.getReCode());
			pstmt.setNString(2, ri.getrDate());
			pstmt.setNString(3, ri.getProcess());
			pstmt.setNString(4, ri.getCuId());
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	int insMenu(ReservedInfo ri) {
		int result = 0;
		String query = "INSERT INTO OD(OD_RECODE, OD_CUCODE, OD_DATE, OD_MNCODE, OD_COUNT, OD_GRADE)" +
				" VALUES(?,?, TO_DATE(?,''YYYYMMDD),?,?,0)";
		try {
			pstmt = connection.prepareStatement(query);
			pstmt.setNString(1, ri.getReCode());
			pstmt.setNString(2, ri.getCuId());
			pstmt.setNString(3, ri.getrDate());
			pstmt.setNString(4, ri.getmCode());
			pstmt.setInt(5, ri.getmCount());
			result=pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;

	}
	
	ArrayList<UserReservedInfo> getUserReservedInfo(String userId){
		ArrayList<UserReservedInfo> list = new ArrayList<UserReservedInfo>();
		String query = "SELECT OS.OS_RECODE || OS.OS_DATE AS PRIMARYKEY,"
				+ "RE.RE_NAME AS RESTAURANT,"
				+ "TO_CHAR(OS.OS_DATE, 'YYYY-MM-DD') AS RDATE,"
				+ "DECODE(OS.OS_PROCESS,'W','예약대기','예약확정') AS PROCESS,"
				+ "RE.RE_LOCATE AS LOCATE"
				+ "FROM OS INNER JOIN RE ON OS.OS_RECODE = RE.RE_CODE"
				+ "WHERE OS.OS_CUID = ? AND "
				+ "(TO_CHAR(OS.OS_DATE,'YYYYMMDD') >= TO_CHAR(SYSDATE,'YYYYMMDD'))";
		
		try {
			pstmt = connection.prepareStatement(query);
			pstmt.setNString(1, userId);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				UserReservedInfo uriBean = new UserReservedInfo();
				uriBean.setPrimaryKey(rs.getNString("PRIMARYKEY"));
				uriBean.setRestaurant(rs.getNString("RESTAURANT"));
				uriBean.setrDate(rs.getNString("RDATE"));
				uriBean.setProcess(rs.getNString("PROCESS"));
				uriBean.setLocate(rs.getNString("LOCATE"));
				
				list.add(uriBean);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return list;
	}
	
	ArrayList<UserReservedMenuInfo> getUserReservedMenuInfo(String userId){
		ArrayList<UserReservedMenuInfo> list = new ArrayList<UserReservedMenuInfo>();
		String query = "SELECT OD.OD_RECODE || OD.OD_DATE AS PRIMARYKEY,\r\n"
				+ "MN.MN_NAME AS MNAME,"
				+ "MC.MC_PRICE AS PRICE,"
				+ "OD.OD_COUNT AS QUANTITY,"
				+ "MC.MC_PRICE * OD.OD_COUNT AS AMOUNT"
				+ "FROM OD"
				+ "INNER JOIN MC"
				+ "ON OD.OD_RECODE = MC.MC_RECODE AND"
				+ "OD.OD_MNCODE = MC.MC_MNCODE"
				+ "INNER JOIN MN"
				+ "ON MC.MC_MNCODE = MN.MN_CODE"
				+ "WHERE (OD.OD_RECODE, OD.OD_CUCODE, TO_CHAR(OD.OD_DATE,'YYYYMMDD'))"
				+ "IN (SELECT OD.OD_RECODE,"
				+ "OS_CUID,"
				+ "TO_CHAR(OS.OS_DATE, 'YYYYMMDD')"
				+ "FROM OS WHERE OS.OS_CUID = ? AND"
				+ "(TO_CHAR(OS.OS_DATE,'YYYYMMDD') >= TO_CHAR(SYSDATE,'YYYYMMDD')))";
		
		try {
			pstmt=connection.prepareStatement(query);
			pstmt.setNString(1, userId);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				UserReservedMenuInfo urmiBean = new UserReservedMenuInfo();
				urmiBean.setPrimarykey(rs.getNString("PRIMARYKEY"));
				urmiBean.setMenu(rs.getNString("MNAME"));
				urmiBean.setPrice(rs.getInt("PRICE"));
				urmiBean.setQuantity(rs.getInt("QUANTITY"));
				urmiBean.setAmount(rs.getInt("AMOUNT"));
				
				list.add(urmiBean);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return list;
	}
}
