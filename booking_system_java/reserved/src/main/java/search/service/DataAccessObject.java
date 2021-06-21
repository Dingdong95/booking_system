package search.service;

import java.sql.SQLException;
import java.util.ArrayList;

import beans.ResInfo;
import beans.ResListInfo;

public class DataAccessObject extends controller.DataAccessObject {
	DataAccessObject(){
		super();
	}
	
	int confirmReserve(ResListInfo rli){
		int result = 0;
		String query = "UPDATE TB_OR SET(OR_CHECK='C') WHERE OR_ST_CODE =? "
				+ "AND OR_CON_CODE = ? AND OR_DATE=?";
		
		try {
			pstmt=connection.prepareStatement(query);
			pstmt.setNString(1, rli.getReCode());
			pstmt.setNString(2, rli.getConId());
			pstmt.setNString(3, rli.getDbdate());
			result=pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
	
	
	ArrayList<ResInfo> getResInfo(ResInfo data) {
		//generic을 사용하면 bean에 있는 값을 ArrayList에 넣을때 형변환을 할필요가 없다 
		ArrayList<ResInfo> restaurantList = new ArrayList<ResInfo>();
		String query = "SELECT * FROM SEARCH_MENU WHERE SWORD LIKE '%' || ? || '%'";
		try {
			pstmt = connection.prepareStatement(query);
			pstmt.setNString(1, data.getWord());
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				//DB에서 날아온 colum명과 bean이름을 일치시켜라 > 나중에 spring 
				//하나의 record마다 bean 하나에 만들고 
				ResInfo record = new ResInfo();
				record.setReCode(rs.getNString("RCODE"));
				record.setRestaurant(rs.getNString("RESTAURANT"));
				record.setLocation(rs.getNString("LOCATION"));
				record.setCategory(rs.getNString("CATEGORY"));
				record.setMenuCode(rs.getNString("MENUCODE"));
				record.setMenu(rs.getNString("MENU"));
				record.setPrice(rs.getInt("PRICE"));
				record.setGpa(rs.getInt("GPA"));
				record.setCount(rs.getInt("COUNT"));
				record.setCommnets(rs.getNString("COMMENTS"));
				//다음 record로 넘어가기전에 arrayList에 저장 하고 넘어가라. 
				restaurantList.add(record);
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return restaurantList;
		
	}
	
	public ArrayList<ResListInfo> getTodayReservedInfo(ResListInfo rli){
		ArrayList<ResListInfo> list = new ArrayList<ResListInfo>();
		String query = "where rcode =? AND process = ?";
		
		try {
			pstmt = connection.prepareStatement(query);
			pstmt.setNString(1, query);
			pstmt.setNString(2, query);
			
			rs=pstmt.executeQuery();
			while(rs.next()) {
				ResListInfo rInfo = new ResListInfo();
				rInfo.setReCode(rs.getNString("RECODE"));
				rInfo.setReName(rs.getNString("RNAME"));
				rInfo.setConId(rs.getNString("CONID"));
				rInfo.setConName(rs.getNString("CONNAME"));
				rInfo.setConPhone(rs.getNString("CONPHONE"));
				rInfo.setrDate(rs.getNString("RDATE"));
				rInfo.setmCode(rs.getNString("MECODE"));
				rInfo.setmName(rs.getNString("MENAME"));
				rInfo.setoCount(rs.getInt("MCOUNT"));
				list.add(rInfo);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return list;
	}
	
	public ArrayList<ResListInfo> getWaitingInfo(ResListInfo rli){
		ArrayList<ResListInfo> list = new ArrayList<ResListInfo>();
		//첫번째 캡쳐 화면 query 
		String query = "SELECT RECODE, RE.RE_NAME AS RNAME, CUID, CNAME, RDATE, TO_CHAR(DBDATE,'YYYYMMDDHH24MISS') AS DBDATE, COUNT" + 
		" FROM RESERVElIST RL INNER JOIN RE ON RL.RECODE = "+
				"where rcode =? and process = ?";
		
		
		try {
			pstmt = connection.prepareStatement(query);
			pstmt.setNString(1, rli.getReCode());
			pstmt.setNString(2, rli.getProcess());
			
			rs=pstmt.executeQuery();
			while(rs.next()) {
				ResListInfo rInfo = new ResListInfo();
				rInfo.setReCode(rs.getNString("RECODE"));
				rInfo.setReName(rs.getNString("RNAME"));
				rInfo.setConId(rs.getNString("CONID"));
				rInfo.setConName(rs.getNString("CONNAME"));
				rInfo.setrDate(rs.getNString("RDATE"));
				rInfo.setDbdate(rs.getNString("DBDATE"));
				rInfo.setoCount(rs.getInt("MCOUNT"));
				list.add(rInfo);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return list;
		
	}
	
	
}


/* -------------------------
 -- 예약을 대기중인것
 --(상점코드), 상점이름, 사용자아이디, 사용자이름, 사용자폰번호, (메뉴코드), 메뉴이름, 예약날짜, 수량
 --------------------------
 --UPDATE TB_OR SET OR_CHECK ='C' WHERE OR_ST_CODE AND OR_CON_CODE AND OR_DATE
 SELECT * FROM TB_OR;
 SELECT * FROM TB_CON;
 SELECT * FROM TB_MU;
 
 CREATE OR REPLACE VIEW OR_VIEW
 AS
 SELECT 
     ODL_ST_CODE AS ORCODE
    ,ODL_CON_CODE AS ORCONCODE
    ,ODL_OR_DATE AS ORDATE
    ,COUNT(*) AS RESCOUNT
 FROM 
    TB_ODL INNER JOIN TB_OR ON ODL_ST_CODE = OR_ST_CODE AND ODL_CON_CODE = OR_CON_CODE AND ODL_OR_DATE = OR_DATE
 WHERE OR_CHECK = 'W' AND ODL_OR_DATE > SYSDATE
 GROUP BY ODL_ST_CODE , ODL_CON_CODE, ODL_OR_DATE;
 ---------------------------------------------------------------------------
CREATE OR REPLACE VIEW ODL_VIEW
AS
SELECT 
     ODL_ST_CODE
    ,ST_NAME AS "상점 이름"
    ,ODL_CON_CODE
    ,CON_NAME AS "사용자 이름"
    ,CON_PHONE AS "사용자 폰번호"
    ,ODL_ME_CODE
    ,ME_NAME AS "메뉴이름"
    ,ODL_OR_DATE
    ,RESCOUNT
FROM TB_ODL T1 INNER JOIN OR_VIEW T2 ON T1.ODL_ST_CODE = T2.ORCODE AND T1.ODL_CON_CODE = T2.ORCONCODE AND T1.ODL_OR_DATE = T2.ORDATE
               INNER JOIN TB_ST ON ODL_ST_CODE = ST_CODE
               INNER JOIN TB_CON ON ODL_CON_CODE = CON_ID
               INNER JOIN TB_MU ON ODL_ME_CODE = ME_CODE;

SELECT * FROM ODL_VIEW; --예약대기 리스트
-------------------------------------------------------------------------------------
CREATE OR REPLACE VIEW TD_OR_VIEW
AS
 SELECT 
     ODL_ST_CODE AS ORCODE
    ,ODL_CON_CODE AS ORCONCODE
    ,ODL_OR_DATE AS ORDATE
    ,ODL_ME_CODE AS MECODE
    ,ODL_QTY
 FROM 
    TB_ODL INNER JOIN TB_OR ON ODL_ST_CODE = OR_ST_CODE AND ODL_CON_CODE = OR_CON_CODE AND ODL_OR_DATE = OR_DATE
 WHERE OR_CHECK = 'W' AND ODL_OR_DATE = TO_CHAR(SYSDATE,'YYYY-MM-DD');
 ----------------------------------------------------------------------
 CREATE OR REPLACE VIEW TD_ODL_VIEW
AS
 SELECT 
     ODL_ST_CODE
    ,ST_NAME 
    ,ODL_CON_CODE
    ,CON_NAME 
    ,CON_PHONE 
    ,ODL_ME_CODE
    ,ME_NAME 
    ,ODL_OR_DATE
    ,T1.ODL_QTY 
FROM TB_ODL T1 INNER JOIN TD_OR_VIEW T2 ON T1.ODL_ST_CODE = T2.ORCODE AND T1.ODL_CON_CODE = T2.ORCONCODE AND T1.ODL_OR_DATE = T2.ORDATE AND T1.ODL_ME_CODE = T2.MECODE
               INNER JOIN TB_ST ON ODL_ST_CODE = ST_CODE
               INNER JOIN TB_CON ON ODL_CON_CODE = CON_ID
               INNER JOIN TB_MU ON ODL_ME_CODE = ME_CODE;
SELECT * FROM TD_ODL_VIEW; --오늘 날짜 리스트              
 * 
 * */
