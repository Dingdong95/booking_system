package resList.service;

import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import beans.Action;
import beans.ResInfo;
import beans.ResListInfo;

public class SearchResList {
	DataAccessObject dao;
	
	
	public SearchResList() {
		this.dao = null;
	}
	
	public Action controller(int serviceCode, HttpServletRequest req) {
		Action action =null;
		
		switch(serviceCode) {
		case 1:
			this.searchResListCtl(req);
			break;
		default:
			break;
		}
		
		return action;
	} 
	
	private Action searchResListCtl(HttpServletRequest req) {
		ArrayList orderList = null;
		ResListInfo rli = new ResListInfo();
		Date today = new Date();
		rli.setTodayDate(today);
		Action action = new Action();
		dao = new DataAccessObject();
		
		this.dao.dbOpen();
		
		orderList = this.getResList(rli);
		req.setAttribute("list", this.makeHtml(orderList));
				
		this.dao.dbClose();
		
		
		action.setPage("rMain.jsp");
		action.setRedirect(false);
		
		
		
		return null;
	}

	
	private ArrayList getResList(ResListInfo rli) {
		return dao.getResList(rli);
	}
	
	private String makeHtml(ArrayList orderList) {
		StringBuffer sb = new StringBuffer();
		sb.append("<table>");
		sb.append("<tr><th>예약자 이름</th><th>예약자 연락처</th><th>예약일자</th><th>예약 수량</th><tr>");
		for(ResListInfo rli : orderList) {
			sb.append("<tr onClick=\'reserve()\'>");
			sb.append("<td>" + rli.getUserName() + "<input type = \'hidden\' name =\'resCheck\' value=\'"+ rli.getResCheck() +"\'> "
					+ "<input type = \'hidden\' name =\'rCode\' value=\'\" + rli.getResCheck() + \'>" + "</td>");
			sb.append("<td>" + rli.getPhoneNo() + "</td>");
			sb.append("<td>" + rli.getResDate() + "</td>");
			sb.append("<td>" + rli.getOrderCount() + "</td>");
			sb.append("</tr>");
		}
		sb.append("</table>");
		
		System.out.println(sb.toString());
		
		return sb.toString();
	}
}