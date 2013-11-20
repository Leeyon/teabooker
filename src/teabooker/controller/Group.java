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

import teabooker.modelwrapper.*;
import teabooker.dao.*;
import teabooker.model.*;
import teabooker.ILogable;

/**
 * Servlet implementation class Group
 */
@WebServlet("/group")
public class Group extends CommonServlet implements ILogable {
	private static final long serialVersionUID = 1L;

	/**
	 * @see CommonServlet#CommonServlet()
	 */
	public Group() {
		super();
		// TODO Auto-generated constructor stub
		initLogger();
	}

	/**
	 * @see ILogable#initLogger()
	 */
	public void initLogger() {
		AppLogger = Logger.getLogger(Group.class);
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
			// Get groups by query
			case GetGroupByQuery: {
				String queryStr = request.getParameter("q").toString().trim();
				outputJson = doGetGroupByQuery(queryStr);
				break;
			}
			// Get groups by user
			case GetGroupsByUser: {
				int userId = Integer.parseInt(request.getParameter("uid")
						.toString().trim());
				outputJson = doGetGroupsByUser(userId);
				break;
			}
			// Get groups members
			case GetGroupMembers: {
				int groupId = Integer.parseInt(request.getParameter("gid")
						.toString().trim());
				outputJson = doGetGroupMembers(groupId);
				break;
			}
			case GetGroupMemebersByStatus: {
				int groupId = Integer.parseInt(request.getParameter("gid")
						.toString().trim());
				int statusCode = Integer.parseInt(request
						.getParameter("status").toString().trim());
				outputJson = doGetGroupMembers(groupId, statusCode);
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
			case CreateGroup: {
				boolean result;
				String groupName = request.getParameter("gname").toString()
						.trim();
				String groupDesc = request.getParameter("gdesc").toString()
						.trim();
				int userId = Integer.parseInt(request.getParameter("uid")
						.toString().trim());
				if (groupName == "") {
					result = false;
				} else {
					result = GroupDAO.createGroup(groupName, groupDesc, userId);
				}
				CommonObjectWrapper jsonResult = new CommonObjectWrapper();
				String message = "success to create a new group, The group name is:"
						+ groupName;
				if (!result) {
					message = "Excpetion occurs, faild to ceate a new group";
				}
				jsonResult.setStatus(result);
				jsonResult.setMessage(message);
				outputJson = Gson.toJson(jsonResult);
				break;
			}
			// Owner update a group
			case UpdateGroup: {
				boolean result;
				String groupName = request.getParameter("gname").toString()
						.trim();
				String groupDesc = request.getParameter("gdesc").toString()
						.trim();
				int gourpId = Integer.parseInt(request.getParameter("gid")
						.toString().trim());
				if (groupName == "") {
					result = false;
				} else {
					result = GroupDAO
							.updateGroup(gourpId, groupName, groupDesc);
				}
				CommonObjectWrapper jsonResult = new CommonObjectWrapper();
				String message = "success to update the group, The group name is:"
						+ groupName;
				if (!result) {
					message = "Excpetion occurs, faild to update the group";
				}
				jsonResult.setStatus(result);
				jsonResult.setMessage(message);
				outputJson = Gson.toJson(jsonResult);
				break;
			}
			// User join a group
			case JoinGroup: {
				boolean result;
				int userId = Integer.parseInt(request.getParameter("uid")
						.toString().trim());
				int groupId = Integer.parseInt(request.getParameter("gid")
						.toString().trim());
				String requestMsg = request.getParameter("rmsg").toString()
						.trim();
				result = GroupDAO.joinGroup(groupId, userId, requestMsg);
				CommonObjectWrapper jsonResult = new CommonObjectWrapper();
				String message = "success to join the group, the group id is:"
						+ groupId;
				if (!result) {
					message = "Excpetion occurs, faild to join the group";
				}
				jsonResult.setStatus(result);
				jsonResult.setMessage(message);
				outputJson = Gson.toJson(jsonResult);
				break;
			}
			// User join groups
			case JoinGroups: {
				boolean result;
				String[] groupIdsStr = request.getParameter("gids").toString()
						.trim().split(",");
				int[] groupIds = new int[groupIdsStr.length];
				for (int i = 0; i < groupIdsStr.length; i++) {
					groupIds[i] = Integer.parseInt(groupIdsStr[i]);
				}
				int userId = Integer.parseInt(request.getParameter("uid")
						.toString().trim());
				String requestMsg = request.getParameter("rmsg").toString()
						.trim();
				result = GroupDAO.joinGroups(groupIds, userId, requestMsg);
				CommonObjectWrapper jsonResult = new CommonObjectWrapper();
				String message = "success to join the groups";
				if (!result) {
					message = "Excpetion occurs, faild to join groups";
				}
				jsonResult.setStatus(result);
				jsonResult.setMessage(message);
				outputJson = Gson.toJson(jsonResult);
				break;
			}
			case UpdateGroupUserStatus: {
				boolean result;
				int userId = Integer.parseInt(request.getParameter("uid")
						.toString().trim());
				int groupId = Integer.parseInt(request.getParameter("gid")
						.toString().trim());
				int statusCode = Integer.parseInt(request
						.getParameter("status").toString().trim());
				result = GroupDAO.updateGroupUserStatus(groupId, userId,
						statusCode);
				CommonObjectWrapper jsonResult = new CommonObjectWrapper();
				String message = "success to update the group user status, the user id is:"
						+ userId;
				if (!result) {
					message = "Excpetion occurs, faild to update the group user status";
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

	// Get group by Query
	private String doGetGroupByQuery(String queryStr) {

		String outputJsonStr = "";
		GroupDetailInfoWrapper groupDetailWrapper = new GroupDetailInfoWrapper();
		List<GroupDetailInfo> list = GroupDAO.getGroupDetailByQuery(queryStr);
		if (list != null && list.size() > 0) {
			groupDetailWrapper.setGroupDetailList(list);
			groupDetailWrapper.setSize(list.size());
			groupDetailWrapper.setStatus(true);
			groupDetailWrapper
					.setMessage("Success to query the speicifc group");
		} else {
			groupDetailWrapper.setGroupDetailList(null);
			groupDetailWrapper.setSize(0);
			groupDetailWrapper.setStatus(false);
			groupDetailWrapper.setMessage("Faild to query the speicifc group");
		}
		outputJsonStr = Gson.toJson(groupDetailWrapper);
		return outputJsonStr;
	}

	// Get groups by User
	private String doGetGroupsByUser(int userId) {
		String outputJsonStr = "";
		GroupDetailInfoWrapper groupDetailWrapper = new GroupDetailInfoWrapper();
		List<GroupDetailInfo> list = GroupDAO.getGroupsByUser(userId);
		if (list != null && list.size() > 0) {
			groupDetailWrapper.setGroupDetailList(list);
			groupDetailWrapper.setSize(list.size());
			groupDetailWrapper.setStatus(true);
			groupDetailWrapper.setMessage("Success to get the groups");
		} else {
			groupDetailWrapper.setGroupDetailList(null);
			groupDetailWrapper.setSize(0);
			groupDetailWrapper.setStatus(false);
			groupDetailWrapper.setMessage("Faild to get groups");
		}
		outputJsonStr = Gson.toJson(groupDetailWrapper);
		return outputJsonStr;
	}

	// Get groups members by groupId
	private String doGetGroupMembers(int groupId) {
		String outputJsonStr = "";
		GroupMemberWrapper groupMemebrWrapper = new GroupMemberWrapper();
		List<GroupMemeberInfo> list = GroupDAO.getGroupMembers(groupId);
		if (list != null && list.size() > 0) {
			groupMemebrWrapper.setMemeberList(list);
			groupMemebrWrapper.setSize(list.size());
			groupMemebrWrapper.setStatus(true);
			groupMemebrWrapper.setMessage("Success to get the group memebers");
		} else {
			groupMemebrWrapper.setMemeberList(null);
			groupMemebrWrapper.setSize(0);
			groupMemebrWrapper.setStatus(false);
			groupMemebrWrapper.setMessage("Faild to get group memebers");
		}
		outputJsonStr = Gson.toJson(groupMemebrWrapper);
		return outputJsonStr;
	}

	// Get groups members by groupId ans user status code
	private String doGetGroupMembers(int groupId, int statusCode) {
		String outputJsonStr = "";
		GroupMemberWrapper groupMemebrWrapper = new GroupMemberWrapper();
		List<GroupMemeberInfo> list = GroupDAO.getGroupMembers(groupId,
				statusCode);
		if (list != null && list.size() > 0) {
			groupMemebrWrapper.setMemeberList(list);
			groupMemebrWrapper.setSize(list.size());
			groupMemebrWrapper.setStatus(true);
			groupMemebrWrapper.setMessage("Success to get the group memebers");
		} else {
			groupMemebrWrapper.setMemeberList(null);
			groupMemebrWrapper.setSize(0);
			groupMemebrWrapper.setStatus(false);
			groupMemebrWrapper.setMessage("Faild to get group memebers");
		}
		outputJsonStr = Gson.toJson(groupMemebrWrapper);
		return outputJsonStr;
	}
}
