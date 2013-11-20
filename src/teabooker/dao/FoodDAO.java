package teabooker.dao;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Query;

import java.util.*;

import org.hibernate.HibernateException;

import teabooker.model.*;

public class FoodDAO {

	private static Logger logger = Logger.getLogger(FoodDAO.class);

	public static List<FoodInfo> getShopFoodList(int shopId)
			throws HibernateException {
		List<FoodInfo> shopFoodList = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			// Begin unit of work
			session.beginTransaction();

			String hqlString = "select f from FoodInfo as f where f.foodStatus=1 and f.shopId="
					+ shopId;

			Query query = session.createQuery(hqlString);

			@SuppressWarnings("unchecked")
			List<FoodInfo> list = query.list();

			if (list.size() > 0) {
				shopFoodList = new ArrayList<FoodInfo>();
				for (int i = 0; i < list.size(); i++) {
					shopFoodList.add(list.get(i));
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
		return shopFoodList;
	}

}
