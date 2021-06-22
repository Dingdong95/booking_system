package jobs.reserve;

import java.sql.SQLException;
import java.util.ArrayList;

import beans.ReservedInfo;

public class DataAccessObject extends controller.DataAccessObject{
	public DataAccessObject() {
		super();
	}

	ArrayList<ReservedInfo> orderList(ReservedInfo ri){
		ArrayList<ReservedInfo> oList= new ArrayList<ReservedInfo>();

		String query = "SELECT * FROM RESERVEDCHECK WHERE RECODE = ?  " +
		"AND MNCODE =? AND CUID=? AND TO_CHAR(DATE, 'YYYYMMDDHH24MISS)=?";
		
		try {
			pstmt = connection.prepareStatement(query);
			pstmt.setNString(1, ri.getReCode());
			pstmt.setNString(3, ri.getCuId());
			pstmt.setNString(4, ri.getrDate());
			pstmt.setNString(2, ri.getmCode());
			rs=pstmt.executeQuery();
			
			while(rs.next()) {
				ReservedInfo record = new ReservedInfo();
				record.setReCode(rs.getNString("RECODE"));
				record.setReCode(rs.getNString("RENAME"));
				record.setReCode(rs.getNString("MNCODE"));
				record.setReCode(rs.getNString("MNNAME"));
				record.setReCode(rs.getNString("DATE"));
				record.setReCode(rs.getNString("QUANTITY"));
				record.setReCode(rs.getNString("PRICE"));
				record.setReCode(rs.getNString("PROCESS"));
				oList.add(record);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return oList;
	}


	int reserveOd(ReservedInfo ri) {
		int result=0;

		String query ="INSERT INTO OD (OD_RECODE,OD_CUCODE,OD_DATE,OD_MNCODE,OD_COUNT,OD_GRADE) VALUES(?,?,?,?,?,0)";

		try {
			pstmt = connection.prepareStatement(query);
			pstmt.setNString(1, ri.getReCode());
			pstmt.setNString(2, ri.getCuId());
			pstmt.setNString(3, ri.getrDate());
			pstmt.setNString(4, ri.getmCode());
			pstmt.setInt(5, ri.getmCount());
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return result;
	}

	int reserveOs(ReservedInfo ri){
		int result=0;

		String query ="INSERT INTO OS (OS_RECODE,OS_DATE,OS_CUID,OS_PROCESS) VALUES(?,?,?,'B')";

		try {
			pstmt = connection.prepareStatement(query);
			pstmt.setNString(1, ri.getReCode());
			pstmt.setNString(2, ri.getrDate());
			pstmt.setNString(3, ri.getCuId());
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return result;
	}



}
