package teabooker.dao;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Query;

import java.util.*;

import org.hibernate.HibernateException;

import teabooker.model.*;

public class EventDAO {

	private static Logger logger = Logger.getLogger(EventDAO.class);

	// User create a new event
	public static boolean createEvent(String title, int shopId, int addressId,
			Date eventEndTime, int ownerId, int groupId)
			throws HibernateException {
		boolean isCreateEventSuccessful = false;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			// Begin unit of work
			session.beginTransaction();

			// create a new event
			EventInfo eventEntity = new EventInfo();

			eventEntity.setEventName(title);
			eventEntity.setEventStartTime(new Date());
			eventEntity.setEventEndTime(eventEndTime);
			eventEntity.setOwnerId(ownerId);
			eventEntity.setEventStatus(1);
			eventEntity.setPhase(0);

			session.save(eventEntity);

			// End unit of work
			session.getTransaction().commit();

			// Save group owner into group user relation
			int eventId = eventEntity.getEventId();

			addUserToEvent(eventId, ownerId);

			addGroupToEvent(eventId, groupId);

			addShopToEvent(eventId, shopId, addressId);

			isCreateEventSuccessful = true;
		} catch (HibernateException hex) {
			logger.error(hex.toString());
			HibernateUtil.getSessionFactory().getCurrentSession()
					.getTransaction().rollback();
		} catch (Exception ex) {
			logger.error(ex.toString());
			HibernateUtil.getSessionFactory().getCurrentSession()
					.getTransaction().rollback();
		}
		return isCreateEventSuccessful;
	}

	// Get event detail by event id
	public static EventDetailInfo getEventDetail(int eventId)
			throws HibernateException {
		EventDetailInfo eventDetail = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			// Begin unit of work
			session.beginTransaction();

			// get the event
			EventInfo eventInfo = getEventInfoById(eventId);

			if (eventInfo != null) {
				eventDetail = new EventDetailInfo();
				eventDetail.setEventInfo(eventInfo);
				int ownerId = eventInfo.getOwnerId();
				eventDetail.setOnwerInfo(getEventOwnerInfo(ownerId));
				eventDetail.setEventGroup(getEventGroupInfo(eventId));
				eventDetail.setShopInfo(getEventShopInfo(eventId));

				List<UserInfo> eventUserList = getEventMemebers(eventId);

				eventDetail.setTotalMemeberCount(eventUserList == null ? 0
						: eventUserList.size());
			}
		} catch (HibernateException hex) {
			logger.error(hex.toString());
			HibernateUtil.getSessionFactory().getCurrentSession()
					.getTransaction().rollback();
		} catch (Exception ex) {
			logger.error(ex.toString());
			HibernateUtil.getSessionFactory().getCurrentSession()
					.getTransaction().rollback();
		}
		return eventDetail;
	}

	// Get event members
	public static List<UserInfo> getEventMemebers(int eventId)
			throws HibernateException {
		List<UserInfo> eventUserList = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			// Begin unit of work
			session.beginTransaction();

			// get the event users
			String hql = "select eu.eventUser from EventUserInfo as eu where eu.userStatus=1 and eu.eventId="
					+ eventId;

			Query query = session.createQuery(hql);

			@SuppressWarnings("unchecked")
			List<UserInfo> list = query.list();

			if (list.size() > 0) {
				eventUserList = new ArrayList<UserInfo>();
				for (int i = 0; i < list.size(); i++) {
					eventUserList.add(list.get(i));
				}
			}

			// End unit of work
			session.getTransaction().commit();
		} catch (HibernateException hex) {
			logger.error(hex.toString());
			HibernateUtil.getSessionFactory().getCurrentSession()
					.getTransaction().rollback();
		} catch (Exception ex) {
			logger.error(ex.toString());
			HibernateUtil.getSessionFactory().getCurrentSession()
					.getTransaction().rollback();
		}
		return eventUserList;
	}

	// Get today's events
	public static List<EventDetailInfo> getEvents() throws HibernateException {
		List<EventDetailInfo> userEventsList = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			// Begin unit of work
			session.beginTransaction();

			Calendar calendar = new GregorianCalendar();

			// get the event
			String hql = "select e from EventInfo as e where DAYOFMONTH(e.eventStartTime)="
					+ calendar.get(Calendar.DAY_OF_MONTH)
					+ " and MONTH(e.eventStartTime)="
					+ (calendar.get(Calendar.MONTH) + 1)
					+ " and YEAR(e.eventStartTime)="
					+ calendar.get(Calendar.YEAR);

			Query query = session.createQuery(hql);

			@SuppressWarnings("unchecked")
			List<EventInfo> list = query.list();

			// End unit of work
			session.getTransaction().commit();

			if (list.size() > 0) {
				userEventsList = new ArrayList<EventDetailInfo>();
				for (int i = 0; i < list.size(); i++) {
					int eventId = list.get(0).getEventId();
					userEventsList.add(getEventDetail(eventId));
				}
			}
		} catch (HibernateException hex) {
			logger.error(hex.toString());
			HibernateUtil.getSessionFactory().getCurrentSession()
					.getTransaction().rollback();
		} catch (Exception ex) {
			logger.error(ex.toString());
			HibernateUtil.getSessionFactory().getCurrentSession()
					.getTransaction().rollback();
		}
		return userEventsList;
	}

	// Get user's event include user created and joined
	public static List<EventDetailInfo> getEvents(int userId)
			throws HibernateException {
		List<EventDetailInfo> userEventsList = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			// Begin unit of work
			session.beginTransaction();

			Calendar calendar = new GregorianCalendar();

			String hql = "select distinct eu.eventId from EventUserInfo as eu,EventInfo as e"
					+ " where e.eventId=eu.eventId "
					+ " and DAYOFMONTH(e.eventStartTime)="
					+ calendar.get(Calendar.DAY_OF_MONTH)
					+ " and MONTH(e.eventStartTime)="
					+ (calendar.get(Calendar.MONTH) + 1)
					+ " and YEAR(e.eventStartTime)="
					+ calendar.get(Calendar.YEAR)
					+ " and eu.userStatus=1 and eu.userId=" + userId;

			Query query = session.createQuery(hql);

			@SuppressWarnings("unchecked")
			List<Integer> list = query.list();

			// End unit of work
			session.getTransaction().commit();

			if (list.size() > 0) {
				userEventsList = new ArrayList<EventDetailInfo>();
				for (int i = 0; i < list.size(); i++) {
					userEventsList.add(getEventDetail(list.get(i)));
				}
			}

		} catch (HibernateException hex) {
			logger.error(hex.toString());
			HibernateUtil.getSessionFactory().getCurrentSession()
					.getTransaction().rollback();
		} catch (Exception ex) {
			logger.error(ex.toString());
			HibernateUtil.getSessionFactory().getCurrentSession()
					.getTransaction().rollback();
		}
		return userEventsList;
	}

	// Update event status
	// If the user is owner, then change the event status
	public static boolean updateEventStatus(int eventId, int userId,
			int statusCode) throws HibernateException {
		boolean isUpdateEventStatusSuccessful = false;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			// Begin unit of work
			session.beginTransaction();

			// update event status
			String hql = "select e from EventInfo as e where e.ownerId="
					+ userId + " and e.eventId=" + eventId;

			Query query = session.createQuery(hql);

			@SuppressWarnings("unchecked")
			List<EventInfo> list = query.list();

			if (list.size() > 0) {
				// Is owner, then change the event status
				EventInfo eventEntity = list.get(0);
				if (eventEntity.getEventStatus() != statusCode) {
					eventEntity.setEventStatus(statusCode);
					session.save(eventEntity);
				}
				isUpdateEventStatusSuccessful = true;
			}

			// End unit of work
			session.getTransaction().commit();
		} catch (HibernateException hex) {
			logger.error(hex.toString());
			HibernateUtil.getSessionFactory().getCurrentSession()
					.getTransaction().rollback();
		} catch (Exception ex) {
			logger.error(ex.toString());
			HibernateUtil.getSessionFactory().getCurrentSession()
					.getTransaction().rollback();
		}
		return isUpdateEventStatusSuccessful;
	}

	// Update event user's status
	// If user join the group, then the status code is 1
	// Else ,like quit the event ,the status code is 0
	public static boolean updateEventUserStatus(int eventId, int userId,
			int statusCode) throws HibernateException {
		boolean isUpdateEventUserStatusSuccessful = false;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			// Begin unit of work
			session.beginTransaction();

			// update event status
			String hql = "select eu from EventUserInfo as eu where eu.userId="
					+ userId + " and eu.eventId=" + eventId;

			Query query = session.createQuery(hql);

			@SuppressWarnings("unchecked")
			List<EventUserInfo> list = query.list();

			if (list.size() > 0) {
				EventUserInfo eventUserEntity = list.get(0);
				if (eventUserEntity.getUserStatus() != statusCode) {
					eventUserEntity.setUserStatus(statusCode);
					session.save(eventUserEntity);
				}
				isUpdateEventUserStatusSuccessful = true;
			}

			// End unit of work
			session.getTransaction().commit();

		} catch (HibernateException hex) {
			logger.error(hex.toString());
			HibernateUtil.getSessionFactory().getCurrentSession()
					.getTransaction().rollback();
		} catch (Exception ex) {
			logger.error(ex.toString());
			HibernateUtil.getSessionFactory().getCurrentSession()
					.getTransaction().rollback();
		}
		return isUpdateEventUserStatusSuccessful;

	}

	// Add a user to event
	public static boolean addUserToEvent(int eventId, int userId)
			throws HibernateException {
		boolean isJoinEventSuccessful = false;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			// Begin unit of work
			session.beginTransaction();

			String hql = "select eu from EventUserInfo as eu where eu.userId="
					+ userId + " and eu.eventId=" + eventId;

			Query query = session.createQuery(hql);

			@SuppressWarnings("unchecked")
			List<EventUserInfo> list = query.list();

			if (list.size() > 0) {
				// User has joined the same event before ,just update the user
				// status

				EventUserInfo eventUserEntity = list.get(0);
				eventUserEntity.setUserStatus(1);
				session.save(eventUserEntity);
				isJoinEventSuccessful = true;

			} else {

				// User hasn't joined the event yet, create a new record
				// create a new event user relation
				EventUserInfo euInfo = new EventUserInfo();

				euInfo.setEventId(eventId);
				euInfo.setUserId(userId);
				euInfo.setUserStatus(1);
				session.save(euInfo);
				isJoinEventSuccessful = true;
			}

			// End unit of work
			session.getTransaction().commit();
		} catch (HibernateException hex) {
			logger.error(hex.toString());
			HibernateUtil.getSessionFactory().getCurrentSession()
					.getTransaction().rollback();
		} catch (Exception ex) {
			logger.error(ex.toString());
			HibernateUtil.getSessionFactory().getCurrentSession()
					.getTransaction().rollback();
		}
		return isJoinEventSuccessful;
	}

	// Add group to event
	private static void addGroupToEvent(int eventId, int groupId)
			throws HibernateException {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			// Begin unit of work
			session.beginTransaction();

			// create a new event group relation
			EventGroupInfo egInfo = new EventGroupInfo();

			egInfo.setCreatedDate(new Date());
			egInfo.setEventId(eventId);
			egInfo.setGroupId(groupId);

			session.save(egInfo);

			session.getTransaction().commit();
		} catch (HibernateException hex) {
			logger.error(hex.toString());
			HibernateUtil.getSessionFactory().getCurrentSession()
					.getTransaction().rollback();
		} catch (Exception ex) {
			logger.error(ex.toString());
			HibernateUtil.getSessionFactory().getCurrentSession()
					.getTransaction().rollback();
		}
	}

	// Add shop to event
	private static void addShopToEvent(int eventId, int shopId, int addressId)
			throws HibernateException {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			// Begin unit of work
			session.beginTransaction();

			// create a new event user relation
			EventShopInfo esInfo = new EventShopInfo();

			esInfo.setEventId(eventId);
			esInfo.setShopId(shopId);
			esInfo.setShopAddressId(addressId);

			session.save(esInfo);

			session.getTransaction().commit();
		} catch (HibernateException hex) {
			logger.error(hex.toString());
			HibernateUtil.getSessionFactory().getCurrentSession()
					.getTransaction().rollback();
		} catch (Exception ex) {
			logger.error(ex.toString());
			HibernateUtil.getSessionFactory().getCurrentSession()
					.getTransaction().rollback();
		}
	}

	// Get event info by eventId
	private static EventInfo getEventInfoById(int eventId)
			throws HibernateException {
		EventInfo eventInfo = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			// Begin unit of work
			session.beginTransaction();

			// get the event
			String hql = "select e from EventInfo as e where e.eventId="
					+ eventId;

			Query query = session.createQuery(hql);

			@SuppressWarnings("unchecked")
			List<EventInfo> list = query.list();

			if (list.size() > 0) {
				eventInfo = list.get(0);
			}

			// End unit of work
			session.getTransaction().commit();
		} catch (HibernateException hex) {
			logger.error(hex.toString());
			HibernateUtil.getSessionFactory().getCurrentSession()
					.getTransaction().rollback();
		} catch (Exception ex) {
			logger.error(ex.toString());
			HibernateUtil.getSessionFactory().getCurrentSession()
					.getTransaction().rollback();
		}
		return eventInfo;
	}

	// Get event OnwerInfo
	private static UserInfo getEventOwnerInfo(int ownerId)
			throws HibernateException {
		UserInfo ownerInfo = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			// Begin unit of work
			session.beginTransaction();

			// get the event
			String hql = "select u from UserInfo as u where u.userId="
					+ ownerId;

			Query query = session.createQuery(hql);

			@SuppressWarnings("unchecked")
			List<UserInfo> list = query.list();

			if (list.size() > 0) {
				ownerInfo = list.get(0);
			}

			// End unit of work
			session.getTransaction().commit();
		} catch (HibernateException hex) {
			logger.error(hex.toString());
			HibernateUtil.getSessionFactory().getCurrentSession()
					.getTransaction().rollback();
		} catch (Exception ex) {
			logger.error(ex.toString());
			HibernateUtil.getSessionFactory().getCurrentSession()
					.getTransaction().rollback();
		}
		return ownerInfo;
	}

	// Get event GroupInfo
	private static GroupInfo getEventGroupInfo(int eventId)
			throws HibernateException {
		GroupInfo groupInfo = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			// Begin unit of work
			session.beginTransaction();

			// get the event
			String hql = "select eg.eventGroup from EventGroupInfo as eg where eg.eventId="
					+ eventId;

			Query query = session.createQuery(hql);

			@SuppressWarnings("unchecked")
			List<GroupInfo> list = query.list();

			if (list.size() > 0) {
				groupInfo = list.get(0);
			}

			// End unit of work
			session.getTransaction().commit();
		} catch (HibernateException hex) {
			logger.error(hex.toString());
			HibernateUtil.getSessionFactory().getCurrentSession()
					.getTransaction().rollback();
		} catch (Exception ex) {
			logger.error(ex.toString());
			HibernateUtil.getSessionFactory().getCurrentSession()
					.getTransaction().rollback();
		}
		return groupInfo;
	}

	// Get event shop and shop address info
	private static EventDetailShopInfo getEventShopInfo(int eventId)
			throws HibernateException {
		EventDetailShopInfo eventDetailShopInfo = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			// Begin unit of work
			session.beginTransaction();

			// get the event
			String hql = "select es.shopInfo,es.addressInfo from EventShopInfo as es where es.eventId="
					+ eventId;

			Query query = session.createQuery(hql);

			@SuppressWarnings("unchecked")
			List<Object[]> list = query.list();

			if (list.size() > 0) {

				Iterator<Object[]> it = list.iterator();
				while (it.hasNext()) {
					eventDetailShopInfo = new EventDetailShopInfo();
					Object[] item = it.next();
					ShopInfo shopInfo = (ShopInfo) item[0];
					ShopAddressInfo addressInfo = (ShopAddressInfo) item[1];
					eventDetailShopInfo.setShopInfo(shopInfo);
					eventDetailShopInfo.setAddresssInfo(addressInfo);
				}
			}

			// End unit of work
			session.getTransaction().commit();
		} catch (HibernateException hex) {
			logger.error(hex.toString());
			HibernateUtil.getSessionFactory().getCurrentSession()
					.getTransaction().rollback();
		} catch (Exception ex) {
			logger.error(ex.toString());
			HibernateUtil.getSessionFactory().getCurrentSession()
					.getTransaction().rollback();
		}
		return eventDetailShopInfo;
	}

}
