package teabooker.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import teabooker.ILogable;
import teabooker.dao.EventDAO;
import teabooker.model.EventDetailInfo;
import teabooker.model.UserInfo;
import teabooker.modelwrapper.CommonObjectWrapper;
import teabooker.modelwrapper.EventDetailWrapper;
import teabooker.modelwrapper.EventMemberWrapper;
import teabooker.modelwrapper.EventsWrapper;

/**
 * Servlet implementation class Event
 */
@WebServlet("/event")
public class Event extends CommonServlet implements ILogable {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Event() {
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
			// Get single event
			case GetEvent: {
				int eventId = Integer.parseInt(request.getParameter("eid")
						.toString().trim());
				outputJson = doGetEvent(eventId);
				break;
			}
			// Get multiple events
			case GetEvents: {
				outputJson = doGetEvents();
				break;
			}
			// Get user's events
			case GetUserEvents: {
				int userId = Integer.parseInt(request.getParameter("uid")
						.toString().trim());
				outputJson = doGetEvents(userId);
				break;
			}
			// Get event members
			case GetEventMembers: {
				int eventId = Integer.parseInt(request.getParameter("eid")
						.toString().trim());
				outputJson = doGetEventMembers(eventId);
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
			// Get groups by query
			case CreateEvent: {
				boolean result;
				int shopId = Integer.parseInt(request.getParameter("sid")
						.toString().trim());
				int addressId = Integer.parseInt(request.getParameter("aid")
						.toString().trim());
				Date eventEndTime = (Date) new SimpleDateFormat(
						"yyyy/MM/dd HH:mm:ss").parseObject(request
						.getParameter("endtime").toString());
				int ownerId = Integer.parseInt(request.getParameter("oid")
						.toString().trim());
				int groupId = Integer.parseInt(request.getParameter("gid")
						.toString().trim());
				String title = request.getParameter("t").toString();
				if (title == "") {
					result = false;
				} else {
					result = EventDAO.createEvent(title, shopId, addressId,
							eventEndTime, ownerId, groupId);
				}
				CommonObjectWrapper jsonResult = new CommonObjectWrapper();
				String message = "success to create a new event, The event is:"
						+ title;
				if (!result) {
					message = "Excpetion occurs, faild to ceate a new event";
				}
				jsonResult.setStatus(result);
				jsonResult.setMessage(message);
				outputJson = Gson.toJson(jsonResult);
				break;
			}
			// Update event status
			case UpdateEventStatus: {
				boolean result;
				int eventId = Integer.parseInt(request.getParameter("eid")
						.toString().trim());
				int userId = Integer.parseInt(request.getParameter("uid")
						.toString().trim());
				int statusCode = Integer.parseInt(request
						.getParameter("status").toString().trim());
				result = EventDAO
						.updateEventStatus(eventId, userId, statusCode);
				CommonObjectWrapper jsonResult = new CommonObjectWrapper();
				String message = "success to update a the event status, The event id is:"
						+ eventId;
				if (!result) {
					message = "Excpetion occurs, faild to update the event status";
				}
				jsonResult.setStatus(result);
				jsonResult.setMessage(message);
				outputJson = Gson.toJson(jsonResult);
				break;
			}
			// Update event user status
			case UpdateEventUserStatus: {
				boolean result;
				int eventId = Integer.parseInt(request.getParameter("eid")
						.toString().trim());
				int userId = Integer.parseInt(request.getParameter("uid")
						.toString().trim());
				int statusCode = Integer.parseInt(request
						.getParameter("status").toString().trim());
				result = EventDAO.updateEventUserStatus(eventId, userId,
						statusCode);
				CommonObjectWrapper jsonResult = new CommonObjectWrapper();
				String message = "success to update a the event user status, The user id is:"
						+ userId;
				if (!result) {
					message = "Excpetion occurs, faild to update the event user status";
				}
				jsonResult.setStatus(result);
				jsonResult.setMessage(message);
				outputJson = Gson.toJson(jsonResult);
				break;
			}
			// User join event
			case JoinEvent: {
				boolean result;
				int eventId = Integer.parseInt(request.getParameter("eid")
						.toString().trim());
				int userId = Integer.parseInt(request.getParameter("uid")
						.toString().trim());
				result = EventDAO.addUserToEvent(eventId, userId);
				CommonObjectWrapper jsonResult = new CommonObjectWrapper();
				String message = "success to add user to event, The event id is:"
						+ eventId;
				if (!result) {
					message = "Excpetion occurs, faild to add user to event";
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
		AppLogger = Logger.getLogger(Event.class);
	}

	// Get single event
	private String doGetEvent(int eventId) {
		String outputJsonStr = "";
		EventDetailInfo eventDetail = EventDAO.getEventDetail(eventId);
		EventDetailWrapper eventDetailWrapper = new EventDetailWrapper();
		if (eventDetail == null) {
			eventDetailWrapper.setStatus(false);
			eventDetailWrapper.setMessage("failed to get the event");
		} else {
			eventDetailWrapper.setStatus(true);
			eventDetailWrapper.setMessage("Success to get the event");
		}
		eventDetailWrapper.setEventDetial(eventDetail);
		outputJsonStr = Gson.toJson(eventDetailWrapper);
		return outputJsonStr;
	}

	// Get multiple events
	private String doGetEvents() {
		String outputJsonStr = "";
		List<EventDetailInfo> events = EventDAO.getEvents();
		EventsWrapper eventsWrapper = new EventsWrapper();
		if (events == null) {
			eventsWrapper.setStatus(false);
			eventsWrapper.setMessage("failed to get events");
		} else {
			eventsWrapper.setStatus(true);
			eventsWrapper.setMessage("Success to get events");
		}
		eventsWrapper.setEventDetial(events);
		outputJsonStr = Gson.toJson(eventsWrapper);
		return outputJsonStr;
	}

	// Get multiple events by user
	private String doGetEvents(int userId) {
		String outputJsonStr = "";
		List<EventDetailInfo> events = EventDAO.getEvents(userId);
		EventsWrapper eventsWrapper = new EventsWrapper();
		if (events == null) {
			eventsWrapper.setStatus(false);
			eventsWrapper.setMessage("failed to get events");
		} else {
			eventsWrapper.setStatus(true);
			eventsWrapper.setMessage("Success to get events");
		}
		eventsWrapper.setEventDetial(events);
		outputJsonStr = Gson.toJson(eventsWrapper);
		return outputJsonStr;
	}

	// Get event members
	private String doGetEventMembers(int eventId) {
		String outputJsonStr = "";
		List<UserInfo> eventMembers = EventDAO.getEventMemebers(eventId);
		EventMemberWrapper eventMembersWrapper = new EventMemberWrapper();
		if (eventMembers == null) {
			eventMembersWrapper.setStatus(false);
			eventMembersWrapper.setMessage("failed to get event members");
		} else {
			eventMembersWrapper.setStatus(true);
			eventMembersWrapper.setMessage("Success to get event members");
		}
		eventMembersWrapper.setEventMemebers(eventMembers);
		outputJsonStr = Gson.toJson(eventMembersWrapper);
		return outputJsonStr;
	}

}
