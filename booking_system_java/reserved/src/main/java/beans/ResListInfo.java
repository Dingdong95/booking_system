package beans;

import java.util.Date;

public class ResListInfo {
//사용자이름 번호 예약날짜 주문건수 예약 체크 
	private Date todayDate;
	private String userName;
	private String phoneNo;
	private String resDate;
	private int orderCount;
	private String resCheck;
	
	
	
	public Date getTodayDate() {
		return todayDate;
	}
	public void setTodayDate(Date todayDate) {
		this.todayDate = todayDate;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public String getResDate() {
		return resDate;
	}
	public void setResDate(String resDate) {
		this.resDate = resDate;
	}
	public int getOrderCount() {
		return orderCount;
	}
	public void setOrderCount(int orderCount) {
		this.orderCount = orderCount;
	}
	public String getResCheck() {
		return resCheck;
	}
	public void setResCheck(String resCheck) {
		this.resCheck = resCheck;
	}
	

}