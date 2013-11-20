package teabooker.dao;

import org.hibernate.Session;
import org.hibernate.Query;
import java.util.*;
import org.hibernate.HibernateException;
import teabooker.model.UserInfo;
import org.apache.log4j.Logger;

public class UserDAO{

	private static Logger logger = Logger.getLogger(UserDAO.class);

	// GetUserInfo by User Id
	public static UserInfo getUserInfo(int uId) throws HibernateException {
		UserInfo userinfo = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			// Begin unit of work
			session.beginTransaction();

			String hqlString = "from UserInfo as u where u.userId=" + uId;
			Query query = session.createQuery(hqlString);

			@SuppressWarnings("unchecked")
			List<UserInfo> list = query.list();

			if (list.size() > 0) {
				userinfo = list.get(0);
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
		return userinfo;
	}

	// GetUserInfo by User Name
	public static UserInfo getUserInfo(String userName)
			throws HibernateException {
		UserInfo userinfo = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			// Begin unit of work
			session.beginTransaction();

			String hqlString = "from UserInfo as u where u.userName='"
					+ userName+"'";
			Query query = session.createQuery(hqlString);

			@SuppressWarnings("unchecked")
			List<UserInfo> list = query.list();

			if (list.size() > 0) {
				userinfo = list.get(0);
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
		return userinfo;
	}

	// Validate user login
	public static UserInfo validateUserInfo(String userName, String userPass)
			throws HibernateException {
		UserInfo userinfo = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			// Begin unit of work
			session.beginTransaction();

			String hqlString = "from UserInfo as u where u.userName='"
					+ userName + "'and u.userPass='" + userPass+"'";
			Query query = session.createQuery(hqlString);

			@SuppressWarnings("unchecked")
			List<UserInfo> list = query.list();

			if (list.size() > 0) {
				userinfo = list.get(0);
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
		return userinfo;

	}

	// Create a new user
	public static boolean createUser(String userName, String userPass,
			String nickName, int userType, String userPhone, String profileImage)
			throws HibernateException {
		boolean isCreateUserSuccessful = false;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			// Begin unit of work
			session.beginTransaction();

			UserInfo userEntity = new UserInfo();
			userEntity.setUserName(userName);
			userEntity.setUserPass(userPass);
			userEntity.setUserNickName(nickName);
			userEntity.setType(userType);
			userEntity.setUserPhone(userPhone);
			userEntity.setProfileImg(profileImage);

			session.save(userEntity);

			// End unit of work
			session.getTransaction().commit();
			isCreateUserSuccessful = true;
		} catch (HibernateException hex) {
			logger.error(hex.toString());
			HibernateUtil.getSessionFactory().getCurrentSession()
					.getTransaction().rollback();
		} catch (Exception ex) {
			logger.error(ex.toString());
			HibernateUtil.getSessionFactory().getCurrentSession()
					.getTransaction().rollback();
		}
		return isCreateUserSuccessful;
	}

	// Update user
	public static boolean updateUser(int userId, String userName,
			String userPhone, String userNickName) throws HibernateException {
		boolean isUpdateUserSuccessful = false;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			// Begin unit of work
			session.beginTransaction();

			UserInfo userEntity = null;

			String hqlString = "from UserInfo as u where u.userId=" + userId;
			Query query = session.createQuery(hqlString);

			@SuppressWarnings("unchecked")
			List<UserInfo> list = query.list();

			if (list.size() > 0) {
				userEntity = list.get(0);
				userEntity.setUserName(userName);
				userEntity.setUserNickName(userNickName);
				userEntity.setUserPhone(userPhone);
				session.save(userEntity);
				isUpdateUserSuccessful = true;
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
		return isUpdateUserSuccessful;
	}

	// Reset User password
	public static boolean resetUserPass(int userId, String oldPass,
			String newPass) {
		boolean isRestUserPassSuccessful = false;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			// Begin unit of work
			session.beginTransaction();

			UserInfo userEntity = null;

			String hqlString = "from UserInfo as u where u.userId=" + userId
					+ "and u.userPass='" + oldPass+"'";
			Query query = session.createQuery(hqlString);

			@SuppressWarnings("unchecked")
			List<UserInfo> list = query.list();

			if (list.size() > 0) {
				userEntity = list.get(0);
				userEntity.setUserPass(newPass);
				session.save(userEntity);
				isRestUserPassSuccessful = true;
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
		return isRestUserPassSuccessful;
	}


}
