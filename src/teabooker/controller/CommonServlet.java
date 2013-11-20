package teabooker.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import teabooker.modelwrapper.CommonObjectWrapper;

import org.apache.log4j.Logger;

import com.google.gson.*;

/**
 * Servlet implementation class CommonServlet
 */
@WebServlet("/CommonServlet")
public class CommonServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected static Gson Gson = new Gson();
	protected static Logger AppLogger;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CommonServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	protected CommonObjectWrapper handlerException(String errMsg) {
		CommonObjectWrapper jsonResult = new CommonObjectWrapper();
		jsonResult.setStatus(false);
		jsonResult.setMessage(errMsg);
		return jsonResult;
	}
}
