package teabooker.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import teabooker.ILogable;
import teabooker.dao.ShopDAO;
import teabooker.model.ShopAddressInfo;
import teabooker.model.ShopInfo;
import teabooker.modelwrapper.CommonObjectWrapper;
import teabooker.modelwrapper.ShopAddressInfoWrapper;
import teabooker.modelwrapper.ShopInfoWrapper;

/**
 * Servlet implementation class Shop
 */
@WebServlet("/shop")
public class Shop extends CommonServlet implements ILogable {
	private static final long serialVersionUID = 1L;

	/**
	 * @see CommonServlet#CommonServlet()
	 */
	public Shop() {
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
			case GetShops: {
				outputJson = doGetShops();
				break;
			}
			// Get groups by user
			case GetShopAddress: {
				int shopId = Integer.parseInt(request.getParameter("sid")
						.toString().trim());
				outputJson = doGetShopAddress(shopId);
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
		AppLogger = Logger.getLogger(Shop.class);
	}

	// Get all shops
	private String doGetShops() {
		String outputJsonStr = "";
		ShopInfoWrapper groupMemebrWrapper = new ShopInfoWrapper();
		List<ShopInfo> list = ShopDAO.getAllShops();
		if (list != null && list.size() > 0) {
			groupMemebrWrapper.setShopList(list);
			groupMemebrWrapper.setSize(list.size());
			groupMemebrWrapper.setStatus(true);
			groupMemebrWrapper.setMessage("Success to get all shops");
		} else {
			groupMemebrWrapper.setShopList(null);
			groupMemebrWrapper.setSize(0);
			groupMemebrWrapper.setStatus(false);
			groupMemebrWrapper.setMessage("Faild to get all shops");
		}
		outputJsonStr = Gson.toJson(groupMemebrWrapper);
		return outputJsonStr;
	}

	// Get shop address
	private String doGetShopAddress(int shopId) {
		String outputJsonStr = "";
		ShopAddressInfoWrapper groupMemebrWrapper = new ShopAddressInfoWrapper();
		List<ShopAddressInfo> list = ShopDAO.getShopAddresses(shopId);
		if (list != null && list.size() > 0) {
			groupMemebrWrapper.setShopAddressList(list);
			groupMemebrWrapper.setSize(list.size());
			groupMemebrWrapper.setStatus(true);
			groupMemebrWrapper.setMessage("Success to get all shops");
		} else {
			groupMemebrWrapper.setShopAddressList(null);
			groupMemebrWrapper.setSize(0);
			groupMemebrWrapper.setStatus(false);
			groupMemebrWrapper.setMessage("Faild to get all shops");
		}
		outputJsonStr = Gson.toJson(groupMemebrWrapper);
		return outputJsonStr;
	}

}
