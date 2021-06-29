package controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jobs.services.UserService;

/**
 * Servlet implementation class AsyncController
 */
@WebServlet({"/Step2", "/Step3"})
public class AsyncController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public UserService us = null;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AsyncController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		//response.setContentType("text/html UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter writer = response.getWriter();
		
		
		writer.print("확인");
		writer.close();
		
		System.out.println(request.getParameter("reCode")+
				request.getParameter("date"));
		
		
		/*
		switch () {
		case "Step2":
			asyncContext.getResponse().getWriter();
			writer.print("응답");
			writer.close();
			break;

		default:
			break;
		}
		*/
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		
		String jsonData = null;
		String jobCode = 
				request.getRequestURI().substring(request.getContextPath().length()+1);
		
		if(jobCode.equals("Step2")) {
			us = new UserService();
			jsonData = us.backController('A', request);
		}else if (jobCode.equals("Step2")) {
			us = new UserService();
			jsonData = us.backController('B', request);
		}else {
			
		}
		
		
		
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(jsonData);
		out.close();
	}

}
