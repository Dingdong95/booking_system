package search.service;

import java.sql.SQLException;
import java.util.ArrayList;

import beans.ResInfo;

public class DataAccessObject extends controller.DataAccessObject {
	DataAccessObject(){
		super();
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
}
