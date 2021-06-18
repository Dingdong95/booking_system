package services;

import java.io.Console;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import beans.Member;
import oracle.jdbc.driver.DBConversion;

public class Authentification {
	
	//member에서 bean에서 가져온 정보들 저장 가능 
	private Member member;
	private DataAccessObject dao;
	private HttpSession session;

	public Authentification() {
		
	}
	
	public String backController(int serviceCode, HttpServletRequest req ) {
		
		String page =null;
		
		switch(serviceCode) {	
		case 1:  //login
			page = this.logInCtl(req);
			break;
			
		case 4:  //logout
			this.logOutCtl(req);
			break;
			
		case 3: //DupCheck
			page = this.dupCheckCtl(req);
			break;
			
		case 2:  //"join"
			page = this.joinCtl(req);
			break;
			
		default:
			break;
			
		}
		return page;
	}
	
	private String logInCtl(HttpServletRequest req) {
		//실패하면가는 기본값
		String page = "access.jsp";
		dao = new DataAccessObject();
		dao.dbOpen();
		
		String message = "아이디나 패스워드를 확인해 주세요";
		//아이디 존재여부 
		member = new Member();
		member.setAccessType(req.getParameter("accessType"));
		member.setMemberId(req.getParameter("uCode"));
		member.setMemberPwd(req.getParameter("aCode"));
		
		if(this.isUserId()) {
			if(this.isAccess()) {
				//history table >> 로그인 정보 저장
				//HttpSession 처리
				session = req.getSession();
				session.setAttribute("access", true);
				
				member.setMemberPwd(null);
				this.getUserInfo();
				req.setAttribute("info", this.member);
				page =(member.getAccessType().equals("G"))? "main.jsp":"main_res.jsp";
			}else {
				req.setAttribute("message",message);
			}
		}else {
			req.setAttribute("message",message);
		}
		
		dao.dbClose();
		
		return page;
		
	}
	// 아이디 패스워드 일치여부 
	private boolean isUserId() {
		//DAO 요청
		//응답 1,0 >> 1: true 0: false 
		return this.convertData(member.getAccessType().equals("G")? this.dao.isUserId(member) : 0);
	}
	
	private boolean isAccess() {
		//DAO 요청
		//응답
		return this.convertData(member.getAccessType().equals("G")? this.dao.isAccess(member):0);
	}
	
	private void  getUserInfo() {
		//DAO 요청 
		//응답 
		if(member.getAccessType().equals("G")) {
			this.dao.getUserInfo(member);
		}else {
			this.dao.getResInfo(member);
		}
	
	}
	
	private boolean convertData(int data) {
		return (data==1)? true:false;
	}
	
	
	private void logOutCtl(HttpServletRequest req) {
		
	}
	
	private String dupCheckCtl(HttpServletRequest req) {
	// member bean에 id injection
		//isuserid(member)? true: 
		String page = "join.jsp";
		String message = "사용 불가능한 아이디입니다";
		
		dao = new DataAccessObject();
		dao.dbOpen();
		
		
		member = new Member();
		member.setAccessType(req.getParameter("accessType"));
		member.setMemberId(req.getParameter("uCode"));
		
		
		if(!this.isUserId()) {        
			//backend resposne  
			message = "사용 가능한 아이디입니다";
			req.setAttribute("uCode", member.getMemberId());
		}
		req.setAttribute("message", message);
		
		
		dao.dbClose();
		return page;
	}
	
	private String joinCtl(HttpServletRequest req) {
		String page = "join.jsp";
		String message = "회원가입이 완료되었습니다.";
		//member bean에 client로부터 전송된 req담기
		member = new Member();
		member.setAccessType(req.getParameter("accessType"));
		member.setMemberId(req.getParameter("uCode"));
		member.setMemberPwd(req.getParameter("aCode"));
		member.setMemberName(req.getParameter("uName"));
		member.setMemberEtc(req.getParameter("uPhone"));
		if(member.getAccessType().equals("R")) {
			member.setCategoryCode(req.getParameter("rType"));
			member.setLocation(req.getParameter("location"));
		}
		// dao 활성화
		dao = new DataAccessObject();
		// db open
		dao.dbOpen();
		//setNewMember()
		if(this.setNewMember()) {
			page= "access.jsp";
		}
		
		dao.dbClose();
		//dbclose
		return page;
	}
	
	private boolean setNewMember() {
		int result;
		if(member.getAccessType().equals("G")) {
			result = dao.setNewMember(member);
		}else{result = dao.setNewMember(member);}
		return this.convertData(result);
	}
}

/* 원활한 데이터의 흐름 및 관리를 위해 데이터의 패턴화 
 * VO(value object) ~ DTO(data transfer object) 
 * 			--> 여러개의 분리된 데이터를 하나의 공간에 모아 저장하는 기술
 *          --> Bean : 타입이 다른 여러개의 ,이터를 하나의 클래스에 저장하고 활용하는 기술
 * 
 * 활용 
 *   ~Ctl() : bean 생성 --> 클라이언트로 부터 전달 받은 데이터를 저장
 *                     --> 해당 잡의 모든 프로세스에서 필요한 데이터는 bean을 이용함.
 * */
