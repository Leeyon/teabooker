package teabooker.dao;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Query;

import java.util.*;

import org.hibernate.HibernateException;

import teabooker.model.*;

public class OrderDAO {

	private static Logger logger = Logger.getLogger(OrderDAO.class);

	// Create orders
	public static boolean createOrders(List<OrderInfo> orders, int eventId,
			int userId) throws HibernateException {
		boolean isCreateOrdersSuccessful = false;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			// Begin unit of work
			session.beginTransaction();

			Calendar calendar = new GregorianCalendar();

			// Check the old available orders for per user
			String hql = "select o from OrderInfo as o where o.orderStatus>0 and o.userId="
					+ userId
					+ " and o.eventId="
					+ eventId
					+ " and DAYOFMONTH(o.createDate)="
					+ calendar.get(Calendar.DAY_OF_MONTH)
					+ " and MONTH(o.createDate)="
					+ (calendar.get(Calendar.MONTH) + 1)
					+ " and YEAR(o.createDate)="
					+ calendar.get(Calendar.YEAR)
					+ " order by o.foodId";

			Query query = session.createQuery(hql);

			@SuppressWarnings("unchecked")
			List<OrderInfo> oldOrders = query.list();

			// User has already ordered in this event, then do the update old
			// orders by count and comment
			if (oldOrders.size() > 0) {
				Collections.sort(orders);
				for (int i = 0; i < orders.size(); i++) {
					boolean isMatched = false;
					for (int j = i; j < oldOrders.size(); j++) {
						OrderInfo oldOrder = oldOrders.get(j);
						OrderInfo newOrder = orders.get(i);
						if (oldOrder.getFoodId() == newOrder.getFoodId()) {
							oldOrder.setFoodCount(newOrder.getFoodCount());
							oldOrder.setComment(newOrder.getComment());
							isMatched = true;
							session.save(oldOrder);
							break;
						}
					}
					if (!isMatched) {
						OrderInfo newOrder = orders.get(i);
						newOrder.setOrderStatus(1);
						session.save(newOrder);
					}
				}
				// End unit of work
				session.getTransaction().commit();
			}
			// User hasn't ordered yet, then save the whole orders
			else {
				for (int i = 0; i < orders.size(); i++) {
					session.save(orders.get(i));
				}
				EventDAO.addUserToEvent(eventId, userId);
			}
			isCreateOrdersSuccessful = true;
		} catch (HibernateException hex) {
			logger.error(hex.toString());
			HibernateUtil.getSessionFactory().getCurrentSession()
					.getTransaction().rollback();
		} catch (Exception ex) {
			logger.error(ex.toString());
			HibernateUtil.getSessionFactory().getCurrentSession()
					.getTransaction().rollback();
		}
		return isCreateOrdersSuccessful;
	}

	// Get shopping cart orders by user
	public static List<ShoppingCartInfo> getUserOrders(int userId)
			throws HibernateException {
		List<ShoppingCartInfo> shoppingCartInfoList = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			// Begin unit of work
			session.beginTransaction();

			Calendar calendar = new GregorianCalendar();

			String hql = "select o,o.orderFood from OrderInfo as o where o.orderStatus>0"
					+ " and DAYOFMONTH(o.createDate)="
					+ calendar.get(Calendar.DAY_OF_MONTH)
					+ " and MONTH(o.createDate)="
					+ (calendar.get(Calendar.MONTH) + 1)
					+ " and YEAR(o.createDate)="
					+ calendar.get(Calendar.YEAR)
					+ " and o.userId=" + userId;

			Query query = session.createQuery(hql);

			@SuppressWarnings("unchecked")
			List<Object[]> orders = query.list();

			if (orders.size() > 0) {
				shoppingCartInfoList = new ArrayList<ShoppingCartInfo>();
				for (int i = 0; i < orders.size(); i++) {
					Object[] orderInfo = orders.get(i);
					OrderInfo orderEntity = (OrderInfo) orderInfo[0];
					FoodInfo foodEntity = (FoodInfo) orderInfo[1];
					ShoppingCartInfo cartInfo = new ShoppingCartInfo();
					cartInfo.setOrderInfo(new OrderInfo(userId, orderEntity
							.getFoodId(), orderEntity.getFoodCount(),
							orderEntity.getEventId(), orderEntity
									.getOrderStatus(), orderEntity
									.getCreateDate()));
					cartInfo.setFoodInfo(foodEntity);
					cartInfo.setOrderTotalPrice();
					shoppingCartInfoList.add(cartInfo);
				}
			}

			// End of task work
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
		return shoppingCartInfoList;
	}

	// Get shopping cart orders by user and event
	public static List<ShoppingCartInfo> getUserOrders(int userId, int eventId)
			throws HibernateException {
		List<ShoppingCartInfo> shoppingCartInfoList = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			// Begin unit of work
			session.beginTransaction();

			Calendar calendar = new GregorianCalendar();

			String hql = "select o,o.orderFood from OrderInfo as o where o.orderStatus>0"
					+ " and DAYOFMONTH(o.createDate)="
					+ calendar.get(Calendar.DAY_OF_MONTH)
					+ " and MONTH(o.createDate)="
					+ (calendar.get(Calendar.MONTH) + 1)
					+ " and YEAR(o.createDate)="
					+ calendar.get(Calendar.YEAR)
					+ " and o.userId=" + userId + " and o.eventId=" + eventId;

			Query query = session.createQuery(hql);

			@SuppressWarnings("unchecked")
			List<Object[]> orders = query.list();

			if (orders.size() > 0) {
				shoppingCartInfoList = new ArrayList<ShoppingCartInfo>();
				for (int i = 0; i < orders.size(); i++) {
					Object[] orderInfo = orders.get(i);
					OrderInfo orderEntity = (OrderInfo) orderInfo[0];
					FoodInfo foodEntity = (FoodInfo) orderInfo[1];
					ShoppingCartInfo cartInfo = new ShoppingCartInfo();
					cartInfo.setOrderInfo(new OrderInfo(userId, orderEntity
							.getFoodId(), orderEntity.getFoodCount(),
							orderEntity.getEventId(), orderEntity
									.getOrderStatus(), orderEntity
									.getCreateDate()));
					cartInfo.setFoodInfo(foodEntity);
					cartInfo.setOrderTotalPrice();
					shoppingCartInfoList.add(cartInfo);
				}
			}

			// End of task work
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
		return shoppingCartInfoList;
	}

	// Get shopping cart orders by user and event
	public static List<ShoppingCartInfo> getEventOrders(int eventId)
			throws HibernateException {
		List<ShoppingCartInfo> shoppingCartInfoList = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			// Begin unit of work
			session.beginTransaction();

			Calendar calendar = new GregorianCalendar();

			String hql = "select o,o.orderFood from OrderInfo as o where o.orderStatus>0"
					+ " and DAYOFMONTH(o.createDate)="
					+ calendar.get(Calendar.DAY_OF_MONTH)
					+ " and MONTH(o.createDate)="
					+ (calendar.get(Calendar.MONTH) + 1)
					+ " and YEAR(o.createDate)="
					+ calendar.get(Calendar.YEAR)
					+ " and o.eventId=" + eventId;

			Query query = session.createQuery(hql);

			@SuppressWarnings("unchecked")
			List<Object[]> orders = query.list();

			if (orders.size() > 0) {
				shoppingCartInfoList = new ArrayList<ShoppingCartInfo>();
				for (int i = 0; i < orders.size(); i++) {
					Object[] orderInfo = orders.get(i);
					OrderInfo orderEntity = (OrderInfo) orderInfo[0];
					FoodInfo foodEntity = (FoodInfo) orderInfo[1];
					ShoppingCartInfo cartInfo = new ShoppingCartInfo();
					cartInfo.setOrderInfo(new OrderInfo(
							orderEntity.getUserId(), orderEntity.getFoodId(),
							orderEntity.getFoodCount(), orderEntity
									.getEventId(),
							orderEntity.getOrderStatus(), orderEntity
									.getCreateDate()));
					cartInfo.setFoodInfo(foodEntity);
					cartInfo.setOrderTotalPrice();
					shoppingCartInfoList.add(cartInfo);
				}
			}

			// End of task work
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
		return shoppingCartInfoList;
	}

	// Update order status
	public static boolean updateOrderStatus(int orderId, int statusCode)
			throws HibernateException {
		boolean isUpdateOrderStatusSuccessful = false;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			// Begin unit of work
			session.beginTransaction();

			Calendar calendar = new GregorianCalendar();

			// Check the old available orders for per user
			String hql = "select o from OrderInfo as o where o.orderId="
					+ orderId + " and DAYOFMONTH(o.createDate)="
					+ calendar.get(Calendar.DAY_OF_MONTH)
					+ " and MONTH(o.createDate)="
					+ (calendar.get(Calendar.MONTH) + 1)
					+ " and YEAR(o.createDate)=" + calendar.get(Calendar.YEAR);

			Query query = session.createQuery(hql);

			@SuppressWarnings("unchecked")
			List<OrderInfo> orders = query.list();

			if (orders.size() > 0) {
				OrderInfo orderInfoEntity = orders.get(0);
				if (orderInfoEntity.getOrderStatus() != statusCode) {
					orderInfoEntity.setOrderStatus(statusCode);
					session.save(orderInfoEntity);
				}
				isUpdateOrderStatusSuccessful = true;
			}

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
		return isUpdateOrderStatusSuccessful;

	}
}
