package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import auth.services.Authentication;
import beans.Action;
import search.service.RestaurantService;
import search.service.Search;

@WebServlet({"/LogIn", "/DupCheck", "/Join", "/LogOut", "/JoinForm", "/LogInForm", "/Search", "/Waiting", "/TodayReserved", "/DashBoard", "/ConfirmReserve"})
public class FrontController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Authentication auth;
	private Search search;
	private RestaurantService rService;
	
    public FrontController() {
        super();
    }
    
    //보안 X 실제 파일이 들어옴 dispatcher는 사라지고 get방시근 들어옴 
    // jobcode로 한이유는 tomcat에서 jobCode를 못찾을시 serverlet이 tomcat에게 넘겨서 tomcat이 src에서 해당 파일을 load해줌 
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Action action = null;
		String message = null;
		String jobCode = 
				request.getRequestURI().substring(request.getContextPath().length()+1);
		
		if(jobCode.equals("JoinForm")) {
			action=new Action();
			action.setPage("join.jsp");
			action.setRedirect(true);
		}else if(jobCode.equals("LogInForm")) {
			action=new Action();
			action.setPage("access.jsp");
			action.setRedirect(true);
		}else if(jobCode.equals("Search")){
			search = new Search();
			action = search.controller(1, request);
		}else {
			action=new Action();
			action.setPage("index.html");
			action.setRedirect(true);
		}
		
		if(action.isRedirect()) {
			response.sendRedirect(action.getPage());
		}else {
			//서버작업에 대한 요청을 실어서 보내줄때
			RequestDispatcher rd = request.getRequestDispatcher(action.getPage());
			rd.forward(request, response);
		}
	}
	
	//보안 O 
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		
		Action action = new Action();
		action = null;
		String jobCode = 
				request.getRequestURI().substring(request.getContextPath().length()+1);
		
		if(jobCode.equals("LogIn")){
			auth = new Authentication();
			action = auth.backController(1, request);
		}else if(jobCode.equals("DupCheck")) {
			auth = new Authentication();
			action =auth.backController(3, request);
		}else if(jobCode.equals("Join")) {
			auth = new Authentication();
			action =auth.backController(2, request);
		}else if(jobCode.equals("LogOut")) {
			auth = new Authentication();
			action =auth.backController(-1, request);
		}else if (jobCode.equals("DashBoard")) {
			rService = new RestaurantService();
			action = rService.backController(1, request);
		}else if  (jobCode.equals("Waiting")){
			rService = new RestaurantService();
			action = rService.backController(2, request);
		}else if(jobCode.equals("TodayReserved")) {
			rService = new RestaurantService();
			action = rService.backController(3, request);
		}else if(jobCode.equals("ConfirmReserve")) {
			rService = new RestaurantService();
			action = rService.backController(4, request);
		}else {
			
		}
		
		//서버작업에 대한 요청을 싫지않고 보내줄때 
		if(action.isRedirect()) {
			response.sendRedirect(action.getPage());
		}else {
			//서버작업에 대한 요청을 실어서 보내줄때
			RequestDispatcher rd = request.getRequestDispatcher(action.getPage());
			rd.forward(request, response);
		}
	}

}
