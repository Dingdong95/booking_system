package jobs.reserve;

import java.util.ArrayList;


import javax.servlet.http.HttpServletRequest;

import beans.Action;
import beans.ReservedInfo;

public class Reserve {
	DataAccessObject dao = null;
	
	public Reserve() {	
	}
	
	public Action controller(int serviceCode, HttpServletRequest req) {
		Action ac = new Action();
		
		
		switch(serviceCode) {
		case 1: 
			this.reserveOrderCtl(req);
			break;
		default:
			break;
		}
		
		return ac;
	}

	private Action reserveOrderCtl(HttpServletRequest req) {
		dao = new DataAccessObject();
		Action ac = new Action();
		ReservedInfo ri = new ReservedInfo();
		String[] orderList =null;
		String[][] orderList2=null;
		
		
	
		
		dao.dbOpen();
		ri.setReCode((req.getParameter("rCode")));
		ri.setCuId((req.getParameter("uCode")));
		ri.setrDate((req.getParameter("rDate")));
		//os에 성공적으로 data올라가면 
		if(this.reserveOs(ri)) {
			orderList = req.getParameter("menuList").split(":");
			for(int i = 0; i<orderList2.length; i++) {
				orderList2[i] = orderList[i].split(",");
				ri.setmCode(orderList2[i][0]);
				ri.setmCount(Integer.parseInt(orderList2[i][1]));
				//od에 성공적으로 data 올라가면 
				if(this.reserveOd(ri,orderList2.length)) {
					//선택한 메뉴를 저장 
					req.setAttribute("orderList", this.makeHtml(this.orderList(ri)));
					req.setAttribute("rCode", ri.getReCode());
					req.setAttribute("rDate", ri.getrDate());
					req.setAttribute("uCode", ri.getCuId());
					
				}
			}
			
		}
		dao.dbClose();
		
		ac.setPage("cStep3.jsp");
		ac.setRedirect(false);
		
		return ac;
	
	}
	
	private String makeHtml(ArrayList<ReservedInfo> orderList) {
		StringBuffer sb = new StringBuffer();
		return sb.toString();
	}
	
	private ArrayList<ReservedInfo> orderList(ReservedInfo ri){
		return this.dao.orderList(ri);
	}
	
	private boolean reserveOd(ReservedInfo ri,int row) {
		return this.typeConvert(this.dao.reserveOd(ri), row);
	}

	
	private boolean reserveOs(ReservedInfo ri){
		return this.typeConvert(this.dao.reserveOs(ri),1);
	}
	
	private boolean typeConvert(int data, int row) {
		return (data==row)?true:false;
	}
}
