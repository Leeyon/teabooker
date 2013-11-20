package teabooker.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import teabooker.dao.*;
import teabooker.model.FoodInfo;
import teabooker.modelwrapper.*;

import org.apache.log4j.Logger;

import teabooker.ILogable;

/**
 * Servlet implementation class Food
 */
@WebServlet("/food")
public class Food extends CommonServlet implements ILogable {
	private static final long serialVersionUID = 1L;

	/**
	 * @see CommonServlet#CommonServlet()
	 */
	public Food() {
		super();
		// TODO Auto-generated constructor stub
		initLogger();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String outputJson = "";
		PrintWriter out = response.getWriter();
		try {
			String method = request.getParameter("method").toLowerCase(
					Locale.ENGLISH);
			switch (CommandType.convertFormString(method)) {
			// Get all shops
			case GetShopFood: {
				int shopId = Integer.parseInt(request.getParameter("sid")
						.toString().trim());
				outputJson = doGetShopFood(shopId);
				break;
			}
			default: {
				break;
			}
			}
		} catch (Exception ex) {
			AppLogger.error(ex);
			CommonObjectWrapper jsonResult = handlerException(ex.toString());
			outputJson = Gson.toJson(jsonResult);
		}
		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");
		out.print(outputJson);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	@Override
	public void initLogger() {
		// TODO Auto-generated method stub
		AppLogger = Logger.getLogger(Food.class);
	}

	// get all food from shop
	private String doGetShopFood(int shopId) {
		String outputJsonStr = "";
		FoodInfoWrapper foodInfoWrapper = new FoodInfoWrapper();
		List<FoodInfo> list = FoodDAO.getShopFoodList(shopId);
		if (list != null && list.size() > 0) {
			foodInfoWrapper.setFoodList(list);
			foodInfoWrapper.setSize(list.size());
			foodInfoWrapper.setStatus(true);
			foodInfoWrapper.setMessage("Success to get all shops");
		} else {
			foodInfoWrapper.setFoodList(null);
			foodInfoWrapper.setSize(0);
			foodInfoWrapper.setStatus(false);
			foodInfoWrapper.setMessage("Faild to get all shops");
		}
		outputJsonStr = Gson.toJson(foodInfoWrapper);
		return outputJsonStr;
	}

}
