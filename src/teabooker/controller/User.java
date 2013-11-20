package teabooker.controller;

import java.io.*;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import teabooker.modelwrapper.*;
import teabooker.dao.UserDAO;
import teabooker.model.UserInfo;
import teabooker.DESUtil;
import teabooker.ILogable;
import teabooker.SiteSettings;

import org.apache.log4j.Logger;

/**
 * Servlet implementation class User
 */
@WebServlet("/user")
public class User extends CommonServlet implements ILogable {

	private static final long serialVersionUID = 1L;
	private static DESUtil desUtil = new DESUtil(SiteSettings.DescKey);

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public User() {
		super();
		initLogger();
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
			// Get user by user Id
			case GetUserById: {
				int uId = Integer.parseInt(request.getParameter("uid")
						.toString().trim());
				outputJson = doGetUserById(uId);
				break;
			}
			// Get user by user name
			case GetUserByName: {
				String userName = request.getParameter("uname").toString()
						.trim();
				outputJson = doGetUserByName(userName);
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
			// Create a new user
			case CreateNewUser: {
				boolean result;
				String userName = request.getParameter("uname").toString()
						.trim();
				String userPass = request.getParameter("upass").toString()
						.trim();
				String nickName = request.getParameter("nickName").toString();
				String phone = request.getParameter("phone").toString().trim();
				int userType = Integer.parseInt(request.getParameter("type")
						.toString().trim());
				String profileImg = request.getParameter("profileImg")
						.toString().trim();
				if (userName == "" || userPass == "" || nickName == ""
						|| phone == "") {
					result = false;
				} else {
					String encryptedPass = desUtil.encryptStr(userPass);
					result = UserDAO.createUser(userName, encryptedPass,
							nickName, userType, phone, profileImg);
				}
				CommonObjectWrapper jsonResult = new CommonObjectWrapper();
				String message = "success to create a new user, The user name is:"
						+ userName;
				if (!result) {
					message = "Excpetion occurs, faild to ceate a new user";
				}
				jsonResult.setStatus(result);
				jsonResult.setMessage(message);
				outputJson = Gson.toJson(jsonResult);
				break;
			}
			// User Login
			case UserLogin: {
				String userName = request.getParameter("uname").toString()
						.trim();
				String userPass = request.getParameter("upass").toString()
						.trim();
				String encryptedUserPass = desUtil.encryptStr(userPass).trim();
				outputJson = doUserLogin(userName, encryptedUserPass);
				break;
			}
			// Update user
			case UpdateUser: {
				boolean result;
				int userId = Integer.parseInt(request.getParameter("uid")
						.toString().trim());
				String userName = request.getParameter("uname").toString()
						.trim();
				String nickName = request.getParameter("nickName").toString();
				String phone = request.getParameter("phone").toString().trim();
				if (userName == "" || nickName == "" || phone == "") {
					result = false;
				} else {
					result = UserDAO.updateUser(userId, userName, phone,
							nickName);
				}
				CommonObjectWrapper jsonResult = new CommonObjectWrapper();
				String message = "success to update the user, The user name is:"
						+ userName;
				if (!result) {
					message = "Excpetion occurs, faild to update the user";
				}
				jsonResult.setStatus(result);
				jsonResult.setMessage(message);
				outputJson = Gson.toJson(jsonResult);
				break;
			}
			// Reset user password
			case RestUserPass: {
				boolean result;
				int userId = Integer.parseInt(request.getParameter("uid")
						.toString().trim());
				String oldPass = request.getParameter("oldpass").toString()
						.trim();
				String newPass = request.getParameter("newpass").toString()
						.trim();
				if (oldPass == "" || newPass == "") {
					result = false;
				} else {
					String encryptedOldUserPass = desUtil.encryptStr(oldPass)
							.trim();
					String encryptedNewUserPass = desUtil.encryptStr(newPass)
							.trim();
					result = UserDAO.resetUserPass(userId,
							encryptedOldUserPass, encryptedNewUserPass);
				}
				CommonObjectWrapper jsonResult = new CommonObjectWrapper();
				String message = "success to reset the user pass, The user id is:"
						+ userId;
				if (!result) {
					message = "Excpetion occurs, faild to reset the user pass";
				}
				jsonResult.setStatus(result);
				jsonResult.setMessage(message);
				outputJson = Gson.toJson(jsonResult);
				break;
			}
			default:
				break;
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

	private String doGetUserById(int uId) {
		String outputJson = "";
		UserInfo userInfo = UserDAO.getUserInfo(uId);
		boolean status = userInfo == null ? false : true;
		String message = userInfo == null ? "failed to get the user"
				: "success to get the user";
		UserinfoWrapper userOuput = new UserinfoWrapper();
		userOuput.setUserInfo(userInfo);
		userOuput.setStatus(status);
		userOuput.setMessage(message);
		outputJson = Gson.toJson(userOuput);
		return outputJson;
	}

	private String doGetUserByName(String userName) {
		String outputJson = "";
		UserInfo userInfo = UserDAO.getUserInfo(userName);
		boolean status = userInfo == null ? false : true;
		String message = userInfo == null ? "failed to get the user"
				: "success to get the user";
		UserinfoWrapper userOuput = new UserinfoWrapper();
		userOuput.setUserInfo(userInfo);
		userOuput.setStatus(status);
		userOuput.setMessage(message);
		outputJson = Gson.toJson(userOuput);
		return outputJson;
	}

	private String doUserLogin(String userName, String userPass) {
		String outputJson = "";
		UserInfo userInfo = UserDAO.validateUserInfo(userName, userPass);
		boolean status = userInfo == null ? false : true;
		String message = userInfo == null ? "failed to login"
				: "success to login";
		UserinfoWrapper userOuput = new UserinfoWrapper();
		userOuput.setUserInfo(userInfo);
		userOuput.setStatus(status);
		userOuput.setMessage(message);
		outputJson = Gson.toJson(userOuput);
		return outputJson;
	}

	@Override
	public void initLogger() {
		// TODO Auto-generated method stub
		AppLogger=Logger.getLogger(User.class);
	}
}
