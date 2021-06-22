package jobs.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import beans.Action;
import beans.Member;
import beans.ReservedInfo;
import beans.RestaurantInfo;

public class Search {
	private DataAccessObject dao;

	public Search() {
		this.dao = null;
	}

	public Action backController(int serviceCode, HttpServletRequest req) {
		Action action = null;

		switch(serviceCode) {
		case 1: 
			action = searchCtl(req);
			break;
		case 2:
			action = reserveDateCtl(req);
			break;
		case 3:
			action = menuChartCtl(req);
			break;
		default:
		}

		return action;
	}

	private ArrayList<RestaurantInfo> menuChart(RestaurantInfo ri) {
		return this.dao.getMenuChartInfo(ri);
		
	}

	private Action menuChartCtl(HttpServletRequest req) {
		Action ac =new Action();
		RestaurantInfo ri = new RestaurantInfo();
		ReservedInfo rInfo = new ReservedInfo();
		ArrayList<RestaurantInfo> mList = new ArrayList<RestaurantInfo>();
		dao = new DataAccessObject();
		dao.dbOpen();
		
		ri.setReCode(req.getParameter("rCode"));
		rInfo.setCuId(req.getParameter("uCode"));
		rInfo.setDbDate(req.getParameter("rDate"));
		req.setAttribute("uCode", rInfo.getCuId());
		req.setAttribute("rDate", rInfo.getDbDate());
		req.setAttribute("rCode", ri.getReCode());
		req.setAttribute("menuList", this.makeHtmlMenu(this.menuChart(ri)));
		dao.dbClose();
		ac.setPage("cStep2.jsp");
		ac.setRedirect(false);
		
		return ac;
	}

	private Action reserveDateCtl(HttpServletRequest req) {
		Action ac = new Action();
		ReservedInfo ri = new ReservedInfo();
		
		Date today = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		ri.setDbDate(sdf.format(today));
		today.setDate(today.getDate()+1);
		StringBuffer date = new StringBuffer();
		String compareDate = null;

		ri.setReCode(req.getParameter("rCode"));
		ri.setCuId(req.getParameter("uCode"));


		dao = new DataAccessObject();
		dao.dbOpen();
		//.equals(ri.getDbDate());
		ArrayList<ReservedInfo> temp = new ArrayList<ReservedInfo>();
		temp.add(ri);
		ArrayList<ReservedInfo> dList = resDate(ri);
		dList=(dList.size()==0?temp:dList);
		for(ReservedInfo item : dList) {
			//System.out.println(item.getDbDate()+"날짜 확인");
			for(int i=0; i<7; i++) {
				compareDate = sdf.format(today);
				//System.out.println(item.getDbDate()+"날짜 확인");
				if(!resDateCheck(item.getDbDate(), compareDate, date)) {
					date.append((date.length()==0)?compareDate: "," +compareDate);
				}
				today.setDate(today.getDate()+1);
			}
			
		}
		String[] dateList = date.toString().split(",");
		req.setAttribute("list",this.makeHtmlDate(dList,dateList)) ;
		req.setAttribute("uCode", ri.getCuId());
		req.setAttribute("rCode", ri.getReCode());
		
		dao.dbClose();

		ac.setPage("cStep1.jsp");
		ac.setRedirect(false);

		return ac;
	}
	private ArrayList<ReservedInfo> resDate(ReservedInfo ri){
		return this.dao.getResDate(ri);
	}
	private boolean resDateCheck(String rootDate ,String compareDate, StringBuffer date) {
		boolean isResDate = false;

		//			System.out.println(rootDate);
		//			System.out.println(compareDate);
		if(rootDate.equals(compareDate)) {
			if(date.toString().contains(compareDate))
				isResDate = true;
		}
		return isResDate;
	}

	private Action searchCtl(HttpServletRequest req) {
		RestaurantInfo ri = new RestaurantInfo();
		Member mem = new Member();
		mem.setMemberId(req.getParameter("uCode"));
		ri.setWord(req.getParameter("word"));

		Action action = new Action();

		dao = new DataAccessObject();
		dao.dbOpen();
		req.setAttribute("list", this.makeHtml(this.search(ri)));
		req.setAttribute("info", mem.getMemberId());
		dao.dbClose();

		action.setPage("cMain.jsp");
		action.setRedirect(false);

		return action;
	}

	private ArrayList<RestaurantInfo> search(RestaurantInfo ri) {
		return dao.getRestaurantInfo(ri);
	}
	
	private String makeHtmlDate(ArrayList<ReservedInfo> list,String[] dateList) {
		StringBuffer sb = new StringBuffer();
		sb.append("<table>");
		for(int i=0; i<dateList.length; i++) {
			sb.append("<tr onClick=\'resDate("+ i +")\'>");
			sb.append("<td>" + dateList[i] + "<input type=\'hidden\' name=\'rDate\' value=\'"+ dateList[i] +"\'</td>");
			sb.append("</tr>");
		}
		sb.append("</table>");
		
		return sb.toString();
	}
	
	private String makeHtml(ArrayList<RestaurantInfo> list) {
		StringBuffer sb = new StringBuffer();
		sb.append("<table>");
		sb.append("<tr><th>레스토랑</th><th>분류</th><th>위치</th><th>메뉴</th><th>가격</th><th>평점</th><th>주문수</th><th>비고</th></tr>");
		int counter = 0;
		for(RestaurantInfo ri : list) {
			sb.append("<tr onClick=\'reserve("+counter+")\'>");
			sb.append("<td>" + ri.getRestaurant() + "<input type=\'hidden\' name=\'rCode\' value=\'"+ ri.getReCode() +"\'</td>");
			sb.append("<td>" + ri.getCatagory() + "</td>");
			sb.append("<td>" + ri.getLocation() + "</td>");
			sb.append("<td>" + ri.getMenu() + "</td>");
			sb.append("<td>" + ri.getPrice() + "</td>");
			sb.append("<td>" + (ri.getGpa()/10.0) + "</td>");
			sb.append("<td>" + ri.getCount() + "</td>");
			sb.append("<td>" + ri.getComments() + "</td>");
			sb.append("</tr>");
			counter++;
		}
		sb.append("</table>");

		return sb.toString();
	}

	private String makeHtmlMenu(ArrayList<RestaurantInfo> list) {
		StringBuffer sb = new StringBuffer();
		sb.append("<table>");
		sb.append("<tr><th>레스토랑</th><th>분류</th><th>위치</th><th>메뉴</th><th>가격</th><th>평점</th><th>주문수</th><th>비고</th></tr>");
		int counter = 0;
		for(RestaurantInfo ri : list) {
			sb.append("<tr>");
			sb.append("<td>" + ri.getRestaurant() + "<input type=\'hidden\' name=\'meCode\' value=\'"+ ri.getMenuCode() +"\'</td>");
			sb.append("<td>" + ri.getCatagory() + "</td>");
			sb.append("<td>" + ri.getLocation() + "</td>");
			sb.append("<td>" + ri.getMenu() + "</td>");
			sb.append("<td>" + ri.getPrice() + "</td>");
			sb.append("<td>" + (ri.getGpa()/10.0) + "</td>");
			sb.append("<td>" + ri.getCount() + "</td>");
			sb.append("<td>" + ri.getComments() + "</td>");
			sb.append("<td>"+  "<input value=\'" + ri.getMenuCode()+ "\' type=\'checkbox\' name =\'checkbox\' />" +"</td>");
			sb.append("<td>"+ "<input type=\'number\' name=\'QTY\' placeholder=\'수량을 입력해 주세요.\' >" +"</td>");
			sb.append("</tr>");
			counter++;
		}
		sb.append("</table>");
		sb.append("<button name=\'checkOut\' id=\'checkOutBtn\' onClick=\'menuSelect()\'>");
		
		return sb.toString();
	}
	
}
