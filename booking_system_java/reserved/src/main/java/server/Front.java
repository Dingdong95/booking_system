package server;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import services.Authentification;

/**
 * Servlet implementation class Front
 */
@WebServlet({"/LogIn", "/DupCheck", "/SendInfo", "/LogOut"})
public class Front extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Authentification auth;
   
    public Front() {
        super();
        
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");
		auth = new Authentification();
		String page =null;
		String jobCode = request.getRequestURI().substring(request.getContextPath().length()+1);
		
		if(jobCode.equals("LogIn")) {
			page = auth.backController(1, request);
		}else if (jobCode.equals("DupCheck")) {
			page = auth.backController(3, request);
		}else if (jobCode.equals("SendInfo")) {
			page = auth.backController(2, request);
		}else if (jobCode.equals("LogOut")) {
			page = auth.backController(4, request);
		}else {
			
		}
		
		RequestDispatcher rd = request.getRequestDispatcher(page);
		rd.forward(request, response);
		/*
		System.out.println(request.getParameter("uCode"));//각 하나씩 있기 떄문에 위에처럼 하지 않아도 된다		
		System.out.println(request.getParameter("uName"));
		System.out.println(request.getParameter("aCode"));
		System.out.println(request.getParameter("uPhone"));
		System.out.println(request.getParameter("accessType"));
		if(request.getParameter("location") !=null) {
			System.out.println(request.getParameter("location"));
		}
		if(request.getParameter("rType")!=null) {
			System.out.println(request.getParameter("rType"));
		}
		*/
		
	}

}
