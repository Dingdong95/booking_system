package jobs.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import beans.Action;
import beans.Member;
import beans.ReservedInfo;
import beans.RestaurantInfo;

public class UserService {
	private DataAccessObject dao;

	public UserService() {
		this.dao = null;
	}
	
	public Action backController(int serviceCode, HttpServletRequest req) {
		Action action = null;

		switch(serviceCode) {
		case 1: 
			action = searchCtl(req);
			break;
			
		case 2: 
			action = availableDaysCtl(req);
			break;
			
		case 3:
			break;
			
		default:
			break;
		}
		
		return action;
	}
	
	private Action availableDaysCtl(HttpServletRequest req) {
		ArrayList<String> dayList = null;
		ReservedInfo ri = null;
		HttpSession session = req.getSession();
		
		Action action = new Action();
		action.setPage("index.html");
		action.setRedirect(true);
		
		// 요청한 유저의 세션이 유지되고 있고 세션에 유저정보가 남아있을때만 예약 진행
		if(!session.isNew() && session.getAttribute("user") != null) {
			ri = new ReservedInfo();
			ri.setReCode(req.getParameter("reCode"));
			ri.setProcess("C");
			
			dao = new DataAccessObject();
			dao.dbOpen();
			dayList = this.getDate(7);
			this.compareDate(dayList, this.getReservedDate(ri));
			dao.dbClose();
			
			req.setAttribute("dayList", makeHtml(dayList, ri.getReCode()));
			
			action.setPage("cStep1.jsp");
			action.setRedirect(false);
		}
		
		return action;
	}
	
	private boolean insOrder(ReservedInfo ri) {
		
		return false;
	}
	
	private ArrayList<String> getReservedDate(ReservedInfo ri){
		return dao.getReservedDate(ri);
	}
	
	private Action searchCtl(HttpServletRequest req) {
		RestaurantInfo ri = new RestaurantInfo();
		ri.setWord(req.getParameter("word"));
		
		Action action = new Action();
		
		dao = new DataAccessObject();
		dao.dbOpen();
		// reservedList 추가
		req.setAttribute("list", this.makeHtml(this.search(ri)));
		dao.dbClose();
		
		action.setPage("cMain.jsp");
		action.setRedirect(false);
		
		return action;
	}
	
	private ArrayList<RestaurantInfo> search(RestaurantInfo ri) {
		return dao.getRestaurantInfo(ri);
	}
	
	private String makeHtml(ArrayList<RestaurantInfo> list) {
		StringBuffer sb = new StringBuffer();
		sb.append("<table>");
		sb.append("<tr><th>레스토랑</th><th>분류</th><th>위치</th><th>메뉴</th><th>가격</th><th>평점</th><th>주문수</th></tr>");
		for(RestaurantInfo ri : list) {
			sb.append("<tr onMouseOut= \"trOut(this)\" onMouseOver= \"trOver(\'"+ri.getComments()+"\', this)\""+ "onClick=\"reserve(\'" +ri.getReCode()+ "\')\">");
			sb.append("<td>" + ri.getRestaurant() + "</td>");
			sb.append("<td>" + ri.getCatagory() + "</td>");
			sb.append("<td>" + ri.getLocation() + "</td>");
			sb.append("<td>" + ri.getMenu() + "</td>");
			sb.append("<td>" + ri.getPrice() + "</td>");
			sb.append("<td>" + (ri.getGpa()/10.0) + "</td>");
			sb.append("<td>" + ri.getCount() + "</td>");
			sb.append("</tr>");
		}
		sb.append("</table>");
		
		return sb.toString();
	}
	
	private String makeHtml(ArrayList<String> list, String reCode) {
		StringBuffer sb = new StringBuffer();
		sb.append("<div id=\"dayList\">");
		for(String day : list) {
			sb.append("<div class=\"col\" onClick=\"callMenu(\'"+ reCode +"\', \'"+ day +"\')\">" + day + "</div>");
		}
		sb.append("</div>");
		return sb.toString();
	}
	
	// 정책에 따른 예약 범위내 일자 추출
	private ArrayList<String> getDate(int days) {
		ArrayList<String> dayList = new ArrayList<String>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		for(int day = 1; day <= days; day++) {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_MONTH, day);
			dayList.add(sdf.format(calendar.getTime()));
		}
		
		return dayList;
	}
	
	private void compareDate(ArrayList<String> dayList, ArrayList<String> reserved) {
		if(reserved.size() > 0) {
			for(int listIndex = (dayList.size()-1) ; listIndex >= 0; listIndex--) {
				for(int resIndex = 0; resIndex < reserved.size(); resIndex++) {
					if(dayList.get(listIndex).equals(reserved.get(resIndex))) {
						//arrayList에서는 중간 index가 지워지면 해당 index를 index+1이 replace 
						dayList.remove(listIndex);
						break;
					}
				}
			}
		}
	}
}
