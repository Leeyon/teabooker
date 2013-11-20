package teabooker.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import teabooker.ILogable;
import teabooker.dao.OrderDAO;
import teabooker.model.OrderInfo;
import teabooker.model.ShoppingCartInfo;
import teabooker.modelwrapper.CommonObjectWrapper;
import teabooker.modelwrapper.ShoppingCartInfoWrapper;

import com.google.gson.Gson;

/**
 * Servlet implementation class Order
 */
@WebServlet("/order")
public class Order extends CommonServlet implements ILogable {
	private static final long serialVersionUID = 1L;
	private static Gson Gson = new GsonBuilder()
			.enableComplexMapKeySerialization().create();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Order() {
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
		String outputJson = "";
		PrintWriter out = response.getWriter();
		try {
			String method = request.getParameter("method").toLowerCase(
					Locale.ENGLISH);

			switch (CommandType.convertFormString(method)) {
			// Get orders by user.
			case GetUserOrders: {
				int userId = Integer.parseInt(request.getParameter("uid")
						.toString().trim());
				outputJson = doGetUserOrders(userId);
				break;
			}
			// Get orders for user in event
			case GetEventUserOrders: {
				int userId = Integer.parseInt(request.getParameter("uid")
						.toString().trim());
				int eventId = Integer.parseInt(request.getParameter("eid")
						.toString().trim());
				outputJson = doGetUserOrders(userId, eventId);
				break;
			}
			// Get event orders
			case GetEventOrders: {
				int eventId = Integer.parseInt(request.getParameter("eid")
						.toString().trim());
				outputJson = doGetEventOrders(eventId);
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
		String outputJson = "";
		PrintWriter out = response.getWriter();
		try {
			String method = request.getParameter("method").toLowerCase(
					Locale.ENGLISH);
			switch (CommandType.convertFormString(method)) {
			// Get a new group
			case CreateOrders: {
				boolean result;
				String orderListJsonStr = request.getParameter("orders")
						.toString().trim();
				int userId = Integer.parseInt(request.getParameter("uid")
						.toString().trim());
				int eventId = Integer.parseInt(request.getParameter("eid")
						.toString().trim());
				if (orderListJsonStr == "") {
					result = false;
				} else {
					Type ordersType = new TypeToken<List<OrderInfo>>() {
					}.getType();
					List<OrderInfo> orders = Gson.fromJson(orderListJsonStr,
							ordersType);
					if (orders.size() > 0) {
						result = OrderDAO.createOrders(orders, eventId, userId);
					} else {
						result = false;
					}
				}
				CommonObjectWrapper jsonResult = new CommonObjectWrapper();
				String message = ("success to create a new orders");
				if (!result) {
					message = "Excpetion occurs, failed to ceate new orders";
				}
				jsonResult.setStatus(result);
				jsonResult.setMessage(message);
				outputJson = Gson.toJson(jsonResult);
				break;
			}
			// Update order status
			case UpdateOrderStatus: {
				boolean result;
				int orderId = Integer.parseInt(request.getParameter("oid")
						.toString().trim());
				int statusCode = Integer.parseInt(request
						.getParameter("status").toString().trim());
				result = OrderDAO.updateOrderStatus(orderId, statusCode);
				CommonObjectWrapper jsonResult = new CommonObjectWrapper();
				String message = ("success to update the order status");
				if (!result) {
					message = "Excpetion occurs, failed to update the order status";
				}
				jsonResult.setStatus(result);
				jsonResult.setMessage(message);
				outputJson = Gson.toJson(jsonResult);
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

	@Override
	public void initLogger() {
		// TODO Auto-generated method stub
		AppLogger = Logger.getLogger(Order.class);
	}

	// Get user orders
	private String doGetUserOrders(int userId) {
		String outputJsonStr = "";
		ShoppingCartInfoWrapper shoppingCartInfoWrapper = new ShoppingCartInfoWrapper();
		List<ShoppingCartInfo> list = OrderDAO.getUserOrders(userId);
		if (list != null && list.size() > 0) {
			shoppingCartInfoWrapper.setShoppingCartInfoList(list);
			shoppingCartInfoWrapper.setSize(list.size());
			shoppingCartInfoWrapper.setUserTotalPrice();
			shoppingCartInfoWrapper.setStatus(true);
			shoppingCartInfoWrapper
					.setMessage("Success to get the user orders");
		} else {
			shoppingCartInfoWrapper.setShoppingCartInfoList(null);
			shoppingCartInfoWrapper.setSize(0);
			shoppingCartInfoWrapper.setStatus(false);
			shoppingCartInfoWrapper.setMessage("Faild to get user orders");
		}
		outputJsonStr = CommonServlet.Gson.toJson(shoppingCartInfoWrapper);
		return outputJsonStr;
	}

	// Get user orders for per event and user
	private String doGetUserOrders(int userId, int eventId) {
		String outputJsonStr = "";
		ShoppingCartInfoWrapper shoppingCartInfoWrapper = new ShoppingCartInfoWrapper();
		List<ShoppingCartInfo> list = OrderDAO.getUserOrders(userId, eventId);
		if (list != null && list.size() > 0) {
			shoppingCartInfoWrapper.setShoppingCartInfoList(list);
			shoppingCartInfoWrapper.setSize(list.size());
			shoppingCartInfoWrapper.setUserTotalPrice();
			shoppingCartInfoWrapper.setStatus(true);
			shoppingCartInfoWrapper
					.setMessage("Success to get the user orders");
		} else {
			shoppingCartInfoWrapper.setShoppingCartInfoList(null);
			shoppingCartInfoWrapper.setSize(0);
			shoppingCartInfoWrapper.setStatus(false);
			shoppingCartInfoWrapper.setMessage("Faild to get user orders");
		}
		outputJsonStr = CommonServlet.Gson.toJson(shoppingCartInfoWrapper);
		return outputJsonStr;
	}

	// Get user orders for per event
	private String doGetEventOrders(int eventId) {
		String outputJsonStr = "";
		ShoppingCartInfoWrapper shoppingCartInfoWrapper = new ShoppingCartInfoWrapper();
		List<ShoppingCartInfo> list = OrderDAO.getEventOrders(eventId);
		if (list != null && list.size() > 0) {
			shoppingCartInfoWrapper.setShoppingCartInfoList(list);
			shoppingCartInfoWrapper.setSize(list.size());
			shoppingCartInfoWrapper.setUserTotalPrice();
			shoppingCartInfoWrapper.setStatus(true);
			shoppingCartInfoWrapper
					.setMessage("Success to get the user orders");
		} else {
			shoppingCartInfoWrapper.setShoppingCartInfoList(null);
			shoppingCartInfoWrapper.setSize(0);
			shoppingCartInfoWrapper.setStatus(false);
			shoppingCartInfoWrapper.setMessage("Faild to get user orders");
		}
		outputJsonStr = CommonServlet.Gson.toJson(shoppingCartInfoWrapper);
		return outputJsonStr;
	}
}
