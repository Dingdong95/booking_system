package jobs.services;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.spi.TransactionalWriter;

import org.apache.tomcat.dbcp.dbcp2.PStmtKey;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.jdi.connect.spi.Connection;

import beans.Action;
import beans.Member;
import beans.ReservedInfo;
import beans.RestaurantInfo;
import beans.UserReservedInfo;
import beans.UserReservedMenuInfo;


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
			System.out.println("여기들어옴 userbackcontroller");
			action = insOrderCtl(req);
			break;

		default:
			break;
		}

		return action;
	}

	public String backController(char serviceCode, HttpServletRequest req) {
		String jsonData = null;



		switch(serviceCode) {
		case 'A':
			jsonData = step2Ctl(req);
			break;
		case 'B':
			jsonData = step3Ctl(req);
			break;
		default:
			break;
		}

		return jsonData;
	}
	
	private Action insOrderCtl(HttpServletRequest req) {
		
		Action action =new Action();
		boolean tran = false;
		ArrayList<ReservedInfo> list = null;
		Gson gson = new Gson();
		
		list = gson.fromJson(req.getParameter("data"), new TypeToken<ArrayList<ReservedInfo>>() {}.getType());
		
		
		dao = new DataAccessObject();
		dao.dbOpen(); dao.setTranConf(false);
		
		/* ReservedIfno에 데이터 추가 입력*/
		for(ReservedInfo ri : list) {
			ri.setCuId((String)req.getSession().getAttribute("user"));
			ri.setProcess("W");
		}
		
		/*OS 테이블에 insert*/
		if(this.insOrder(list.get(0))) {
			/*OD 테이블에 insert*/
			if(this.insMenu(list)) {
				tran = true;
			}
		}
		
		
		dao.setTran(false);
		dao.setTranConf(true); dao.dbClose();
		
		action.setPage("cMain.jsp");
		action.setRedirect(false);
		
		
		return action;
	}





	private String step2Ctl(HttpServletRequest req) {
		String jsonData = null;
		ReservedInfo ri = new ReservedInfo();
		ri.setReCode(req.getParameter("reCode"));
		ri.setrDate(req.getParameter("day"));
		
		this.dao = new DataAccessObject();
		dao.dbOpen();
		
		jsonData = this.pStep2(ri);
		
		dao.dbClose();
		
		return jsonData;

	}

	private String pStep2(ReservedInfo ri) {
		String jsonData = null;
		Gson gson = new Gson();
		jsonData = gson.toJson(dao.getMenu(ri));
		return jsonData;
	}
	
	private String step3Ctl(HttpServletRequest req) {
		String jsonData = null;
		return jsonData;
	}

	private Action availableDaysCtl(HttpServletRequest req) {
		//여기서 모든 dao제얻권을 가지고가야 db에 부화가안걸림
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
		
		return this.convertToBoolean(dao.insOrder(ri));
	}
	
	private boolean insMenu(ArrayList<ReservedInfo> list) {
		boolean result = false;
		for(ReservedInfo ri: list) {
			result = this.convertToBoolean(dao.insMenu(ri));
				if(!result) {
					break;
				}
			}
		return result;
		}
		
	

	private boolean convertToBoolean(int value) {
		// TODO Auto-generated method stub
		return (value>0)? true:false;
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
		//사용자 예약 정보 (HTML: ReservedInfo)
		req.setAttribute("ReservedInfo",this.makeHtmlReservedInfo(
				this.getUserReservedInfo((String)req.getSession().getAttribute("user"))));
		
		// 예약 메뉴 정보 (JSON: MenuInfo)
		Gson gson = new Gson();
		req.setAttribute("MenuInfo", gson.toJson(getUserReservedMenuInfo((String)req.getSession().getAttribute("user"))));
		
		// reservedList 추가
		if(req.getParameter("word") != null) {
			ri.setWord(req.getParameter("word"));
		}
		req.setAttribute("list", this.makeHtml(this.search(ri)));
		
		dao.dbClose();

		action.setPage("cMain.jsp");
		action.setRedirect(false);

		return action;
	}
	
	private ArrayList<UserReservedInfo> getUserReservedInfo(String userId){
		return dao.getUserReservedInfo(userId);
	}
	
	private ArrayList<UserReservedMenuInfo> getUserReservedMenuInfo(String userId){
		return dao.getUserReservedMenuInfo(userId);
	}
	
	private String makeHtmlReservedInfo(ArrayList<UserReservedInfo> list) {
		StringBuffer sb = new StringBuffer();
		sb.append("<div id =\'reservedInfo\'>");
		sb.append("<table>");
		sb.append("<tr><th>레스토랑</th><th>예약일정</th><th>예약상태</th><th>예약위치</th></tr>");
		for(UserReservedInfo uri: list) {
			sb.append("<tr onMouseOut = \'trOut(this)\' onMouseOver =\'trOver(\'\',this)\'" + "onClick=\'menuInfo(\'"+uri.getPrimaryKey()+"\'\">");
			sb.append("<td>" + uri.getRestaurant()+"</td>");
			sb.append("<td>" + uri.getrDate()+"</td>");
			sb.append("<td>" + uri.getProcess()+"</td>");
			sb.append("<td>" + uri.getLocate()+"</td>");
			sb.append("</tr>");
		}
		sb.append("</table>");
		sb.append("</div>");
		
		
		sb.append("</div>");
		return sb.toString();
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
		sb.append("<div class= \'step\'>예약 가능 일자</div>");
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
