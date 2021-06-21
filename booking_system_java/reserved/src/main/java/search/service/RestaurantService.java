package search.service;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.tomcat.dbcp.dbcp2.PStmtKey;

import beans.Action;
import beans.ResListInfo;


public class RestaurantService {
	DataAccessObject dao;
	
	public RestaurantService() {
		this.dao = null;
	}
	
	public Action backController(int serviceCode, HttpServletRequest req) {
		Action action = null;
		
		
		switch(serviceCode) {
		case 1:
			action = this.restaurantMainServiceCtl(req);
			break;
		case 2:
			action = this.waitingInfoCtl(req);
			break;
		case 3:
			action = this.todayReservedCtl(req);
			break;
		case 4:
			this.confirmReserveCtl(req);
		default:
			break;
		
		}
		
		return action;
	}
	
	private Action confirmReserveCtl(HttpServletRequest req) {
		dao = new DataAccessObject();
		Action action = null;
		ResListInfo rli = new ResListInfo();
		
		rli.setReCode(req.getParameter("reCode"));
		rli.setConId(req.getParameter("conCode"));
		rli.setDbdate(req.getParameter("dbDate"));
		
		this.dao.dbOpen();
		if(this.confirmReserve(rli)) {
			
		}else {
			System.out.println("예약확정 오류발생");
		}
		
		action.setPage("rMain.jsp");
		action.setRedirect(false);
		
		this.dao.dbClose();
		
		return action; 
	}
	
	private boolean confirmReserve(ResListInfo rli) {
		return (dao.confirmReserve(rli) == 1)? true:false;
	}
	
	private Action restaurantMainServiceCtl(HttpServletRequest req) {
		Action action = null;
		ResListInfo rli = new ResListInfo();
		//dispatcher방식은 처음에 넘겨진 정보가 계속 넘거간 상태로 담겨있다. 
		//그래서 pw 초기화 시켜줘야함 
		
		rli.setReCode(req.getParameter("uCode"));
		
		dao = new DataAccessObject();
		
		this.dao.dbOpen();
		
		req.setAttribute("watingList", this.makeHtml(this.getWaitingInfo(rli),true));
		req.setAttribute("todayList", this.makeHtml(this.getTodayReservedInfo(rli),false));
		
		this.dao.dbClose();
		
		action.setPage("rMain.jsp");
		action.setRedirect(false);
		this.getWaitingInfo(rli);
		return action;
	}
	
	private Action waitingInfoCtl(HttpServletRequest req) {
		return null;
	}
	
	private Action todayReservedCtl(HttpServletRequest req) {
		return null;
	}
	
	
	private ArrayList<ResListInfo> getWaitingInfo(ResListInfo rli) {
		
		
		return dao.getWaitingInfo(rli);
	}
	
	private ArrayList<ResListInfo> getTodayReservedInfo(ResListInfo rli) {
		return dao.getTodayReservedInfo(rli);
	}
	
	private String makeHtml(ArrayList<ResListInfo> rli, boolean type) {
		StringBuffer sb = new StringBuffer();
		
		sb.append("<div class=\'info\' >");
		sb.append("<div class=\'col\'>레스토랑</div>");
		sb.append("<div class=\'col\'>예약자명</div>");
		if(type) {
			//for getWaitingInfo
			sb.append("<div class=\'col\'>예약일정</div>");
			sb.append("<div class=\'col\'>예약인원</div>");	
		}else {
			//for getTodayReservedInfo 
			sb.append("<div class=\'col\'>연락처</div>");
			sb.append("<div class=\'col\'>예약메뉴</div>");
			sb.append("<div class=\'col\'>메뉴 수량</div>");	
		}
		
		
		for(ResListInfo list: rli) {
			if(type) {
				sb.append("<div name=\'list\' class =\'record\' onClick=selectRestaurant(\'"+ list.getReCode() + "\',\'" + list.getConId() + "\',\'" + list.getDbdate() + "\')>");	
			}else {
				sb.append("<div name=\'list\' class =\'record\' onClick=selectRestaurant(\'"+ list.getReCode() + "\',\'" + list.getConId() + "\',\'" + list.getDbdate() + "\',\'"+ list.getmCode() + "\')>");
			}
			
			sb.append("</div> class =\'col\'" + list.getReName() + "</div>");
			sb.append("</div> class =\'col\'" + list.getConName()+ "(" + list.getConId() + ")" + "</div>");
			if(type) {
				sb.append("</div> class =\'col\'" + list.getrDate() + "</div>");
				sb.append("</div> class =\'col\'" + list.getoCount() + "</div>");	
			}else {
				sb.append("</div> class =\'col\'" + list.getConPhone() + "</div>");
				sb.append("</div> class =\'col\'" + list.getmName() + "</div>");
				sb.append("</div> class =\'col\'" + list.getQty() + "</div>");
			}
			sb.append("</div>");	
		}
		sb.append("</div>");
		
		return sb.toString();
	}
}
