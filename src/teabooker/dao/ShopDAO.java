/**
 * 
 */
package teabooker.dao;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Query;
import java.util.*;
import org.hibernate.HibernateException;
import teabooker.model.*;

/**
 * @author v-liyyu
 * 
 */
public class ShopDAO {

	private static Logger logger = Logger.getLogger(ShopDAO.class);

	// Get all shops
	public static List<ShopInfo> getAllShops() throws HibernateException {
		List<ShopInfo> shopList = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			// Begin unit of work
			session.beginTransaction();

			String hqlString = "select s from ShopInfo as s";

			Query query = session.createQuery(hqlString);

			@SuppressWarnings("unchecked")
			List<ShopInfo> list = query.list();

			if (list.size() > 0) {
				shopList = new ArrayList<ShopInfo>();
				for (int i = 0; i < list.size(); i++) {
					shopList.add(list.get(i));
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
		return shopList;
	}

	// Get address for shop
	public static List<ShopAddressInfo> getShopAddresses(int shopId)throws HibernateException {
		List<ShopAddressInfo> shopAddressList = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			// Begin unit of work
			session.beginTransaction();

			String hqlString = "select sa from ShopAddressInfo as sa where sa.shopId="
					+ shopId;

			Query query = session.createQuery(hqlString);

			@SuppressWarnings("unchecked")
			List<ShopAddressInfo> list = query.list();

			if (list.size() > 0) {
				shopAddressList = new ArrayList<ShopAddressInfo>();
				for (int i = 0; i < list.size(); i++) {
					shopAddressList.add(list.get(i));
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
		return shopAddressList;
	}

}
