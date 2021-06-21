package search.service;

import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import beans.Action;
import beans.ResInfo;

public class Search {
	DataAccessObject dao;
	
	
	public Search(){
		this.dao = null;
		
	}
	public Action controller(int serviceCode, HttpServletRequest req){
		Action action =null;
		switch(serviceCode) {
		case 1: 
			action = this.searchCtl(req);
			break;
		default:
			break;
		}
		return action;
	}
	
	private Action searchCtl(HttpServletRequest req) {
		ArrayList<ResInfo> list = null;
		ResInfo ri = new ResInfo();
		ri.setWord(req.getParameter("word"));
		
		Action action = new Action();
		
		dao = new DataAccessObject();
		dao.dbOpen();
		
		list = this.search(ri);
		
		req.setAttribute("list", this.makeHtml(list)); 
		
		dao.dbClose();
		
		action.setPage("cMain.jsp");
		action.setRedirect(false);
		
		return action;
	}
	
	private ArrayList<ResInfo> search(ResInfo ri){
		return dao.getResInfo(ri);
	}
	
	private String makeHtml(ArrayList<ResInfo> list) {
		StringBuffer sb = new StringBuffer();
		sb.append("<table>");
		sb.append("<tr><th>레스토랑</th><th>분류</th><th>위치</th><th>메뉴</th><th>가격</th><th>평점</th><th>주문수</th><th>설명</th><tr>");
		for(ResInfo ri : list) {
			sb.append("<tr onClick=\'reserve()\'>");
			sb.append("<td>" + ri.getRestaurant() + "<input type = \'hidden\' name =\'rCode\' value=\'"+ ri.getReCode() +"\' </td>");
			sb.append("<td>" + ri.getCategory() + "</td>");
			sb.append("<td>" + ri.getLocation() + "</td>");
			sb.append("<td>" + ri.getMenu() + "</td>");
			sb.append("<td>" + ri.getPrice() + "</td>");
			sb.append("<td>" + (ri.getGpa()/10.0) + "</td>");
			sb.append("<td>" + ri.getCount() + "</td>");
			sb.append("<td>" + ri.getCommnets() + "</td>");
			sb.append("</tr>");
		}
		sb.append("</table>");
		
		System.out.println(sb.toString());
		
		return sb.toString();
		
		
		
	}
	
}
