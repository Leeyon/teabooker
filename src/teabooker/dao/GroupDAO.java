package teabooker.dao;

import org.hibernate.Session;
import org.hibernate.Query;

import java.util.*;

import org.hibernate.HibernateException;
import org.apache.log4j.Logger;

import teabooker.model.*;

public class GroupDAO {

	private static Logger logger = Logger.getLogger(GroupDAO.class);

	// Create a new group
	public static boolean createGroup(String groupName, String groupDesc,
			int ownerId) throws HibernateException {
		boolean isCreateGroupSuccessful = false;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			// Begin unit of work
			session.beginTransaction();

			// create a new group
			GroupInfo groupEntity = new GroupInfo();

			groupEntity.setGroupName(groupName);
			groupEntity.setGroupDescription(groupDesc);
			groupEntity.setGroupStatus(1);
			groupEntity.setGroupCreateDate(new Date());

			session.save(groupEntity);

			// Save group owner into group user relation
			int groupId = groupEntity.getGroupId();

			GroupUserInfo guEntity = new GroupUserInfo();
			guEntity.setGroupId(groupId);
			guEntity.setIsOwner(1);
			guEntity.setRequestMsg("");
			guEntity.setUserId(ownerId);
			guEntity.setUserStatus(1);

			session.save(guEntity);

			// End unit of work
			session.getTransaction().commit();
			isCreateGroupSuccessful = true;
		} catch (HibernateException hex) {
			logger.error(hex.toString());
			HibernateUtil.getSessionFactory().getCurrentSession()
					.getTransaction().rollback();
		} catch (Exception ex) {
			logger.error(ex.toString());
			HibernateUtil.getSessionFactory().getCurrentSession()
					.getTransaction().rollback();
		}
		return isCreateGroupSuccessful;
	}

	// Update group info
	public static boolean updateGroup(int groupId, String groupName,
			String groupDesc) throws HibernateException {
		boolean isUpdateGroupSuccessful = false;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			// Begin unit of work
			session.beginTransaction();

			// get a group
			GroupInfo groupEntity = null;

			String hqlString = "from GroupInfo as g where g.groupId=" + groupId
					+ "and g.groupStatus=1";

			Query query = session.createQuery(hqlString);

			@SuppressWarnings("unchecked")
			List<GroupInfo> list = query.list();

			if (list.size() > 0) {
				groupEntity = list.get(0);
				groupEntity.setGroupName(groupName);
				groupEntity.setGroupDescription(groupDesc);

				session.save(groupEntity);
				isUpdateGroupSuccessful = true;
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
		return isUpdateGroupSuccessful;
	}

	// User join a group
	public static boolean joinGroup(int groupId, int userId, String requestMsg)
			throws HibernateException {
		boolean isJoinGroupSuccessful = false;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			// Begin unit of work
			session.beginTransaction();
			GroupUserInfo guEntity = tryGetGroupUser(userId, groupId, session);
			if (guEntity != null) {
				guEntity.setUserStatus(0);
				guEntity.setRequestMsg(requestMsg);
				session.save(guEntity);
				isJoinGroupSuccessful = true;
			} else {
				guEntity = new GroupUserInfo(groupId, userId, 0, 0, requestMsg);
				session.save(guEntity);
				isJoinGroupSuccessful = true;
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
		return isJoinGroupSuccessful;
	}

	// User join groups
	public static boolean joinGroups(int[] groupIds, int userId,
			String requestMsg) throws HibernateException {
		boolean isJoinGroupsSuccessful = false;
		for (int id : groupIds) {
			boolean result = joinGroup(id, userId, requestMsg);
			if (result == false) {
				return isJoinGroupsSuccessful;
			}
		}
		isJoinGroupsSuccessful = true;
		return isJoinGroupsSuccessful;
	}

	// Update group user status
	// -2 means, user has quit the group by himself,
	// 0 means not reviewed,
	// -1 means user permission apply has been rejected.
	// 1 means user permission apply has been approved
	public static boolean updateGroupUserStatus(int groupId, int userId,
			int statusCode) throws HibernateException {
		boolean isJoinGroupSuccessful = false;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			// Begin unit of work
			session.beginTransaction();
			GroupUserInfo guEntity = tryGetGroupUser(userId, groupId, session);
			if (guEntity != null) {
				guEntity.setUserStatus(statusCode);
				session.save(guEntity);
				isJoinGroupSuccessful = true;
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
		return isJoinGroupSuccessful;
	}

	// Search groups by query
	public static List<GroupDetailInfo> getGroupDetailByQuery(String keyword)
			throws HibernateException {
		List<GroupDetailInfo> queryResultList = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			// Begin unit of work
			session.beginTransaction();

			String hqlString = "select distinct g from GroupInfo as g"
					+ " left join g.groupUsers as gu" + " where gu.isOwner=1"
					+ " and (g.groupName like '%" + keyword
					+ "%' or g.groupDescription like '%" + keyword + "%')";

			Query query = session.createQuery(hqlString);

			@SuppressWarnings("unchecked")
			List<GroupInfo> list = query.list();

			if (list.size() > 0) {
				queryResultList = new ArrayList<GroupDetailInfo>();
				for (int i = 0; i < list.size(); i++) {
					GroupInfo groupInfo = list.get(i);
					GroupDetailInfo groupDetail = new GroupDetailInfo();
					groupDetail.setGroupInfo(groupInfo);

					Set<GroupUserInfo> groupUsers = groupInfo.getGroupUsers();
					int ownerId = groupUsers.iterator().next().getUserId();
					String hqlString2 = "from UserInfo as u where u.userId="
							+ ownerId;
					Query query2 = session.createQuery(hqlString2);
					@SuppressWarnings("unchecked")
					List<UserInfo> list2 = query2.list();
					if (list2.size() > 0) {
						groupDetail.setOwnerInfo(list2.get(0));
					} else {
						groupDetail.setOwnerInfo(null);
					}
					queryResultList.add(groupDetail);
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
		return queryResultList;
	}

	// Get groups by user includes the group user created and user joined.
	public static List<GroupDetailInfo> getGroupsByUser(int userId)
			throws HibernateException {
		List<GroupDetailInfo> userGroupsList = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			// Begin unit of work
			session.beginTransaction();

			String hqlString = "select distinct g from GroupInfo as g"
					+ " left join g.groupUsers as gu" + " where gu.userId="
					+ userId + " and gu.userStatus>=0";

			Query query = session.createQuery(hqlString);

			@SuppressWarnings("unchecked")
			List<GroupInfo> list = query.list();

			if (list.size() > 0) {
				userGroupsList = new ArrayList<GroupDetailInfo>();
				for (int i = 0; i < list.size(); i++) {
					GroupInfo groupInfo = list.get(i);
					GroupDetailInfo groupDetail = new GroupDetailInfo();
					groupDetail.setGroupInfo(groupInfo);

					Set<GroupUserInfo> groupUsers = groupInfo.getGroupUsers();
					int ownerId = groupUsers.iterator().next().getUserId();
					String hqlString2 = "from UserInfo as u where u.userId="
							+ ownerId;
					Query query2 = session.createQuery(hqlString2);
					@SuppressWarnings("unchecked")
					List<UserInfo> list2 = query2.list();
					if (list2.size() > 0) {
						groupDetail.setOwnerInfo(list2.get(0));
					} else {
						groupDetail.setOwnerInfo(null);
					}
					userGroupsList.add(groupDetail);
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
		return userGroupsList;
	}

	// Get group members
	public static List<GroupMemeberInfo> getGroupMembers(int groupId) {
		List<GroupMemeberInfo> groupMemberList = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			// Begin unit of work
			session.beginTransaction();

			String hql = "select gu,u from GroupUserInfo as gu"
					+ " left join gu.userInfo as u" + " where gu.groupId="
					+ groupId;

			Query query = session.createQuery(hql);

			@SuppressWarnings("unchecked")
			List<Object[]> list = query.list();

			if (list.size() > 0) {

				groupMemberList = new ArrayList<GroupMemeberInfo>();
				Iterator<Object[]> it = list.iterator();

				while (it.hasNext()) {
					GroupMemeberInfo memebrInfo = new GroupMemeberInfo();
					Object[] item = it.next();
					GroupUserInfo guInfo = (GroupUserInfo) item[0];
					UserInfo uInfo = (UserInfo) item[1];
					memebrInfo.setGroupMemeberInfo(guInfo);
					memebrInfo.setUserInfo(uInfo);
					groupMemberList.add(memebrInfo);
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
		return groupMemberList;
	}

	// Get group member by user status
	public static List<GroupMemeberInfo> getGroupMembers(int groupId,
			int statusCode) throws HibernateException {
		List<GroupMemeberInfo> groupMemberList = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			// Begin unit of work
			session.beginTransaction();

			String hql = "select gu,u from GroupUserInfo as gu"
					+ " left join gu.userInfo as u" + " where gu.groupId="
					+ groupId + " and gu.userStatus=" + statusCode;

			Query query = session.createQuery(hql);

			@SuppressWarnings("unchecked")
			List<Object[]> list = query.list();

			if (list.size() > 0) {

				groupMemberList = new ArrayList<GroupMemeberInfo>();
				Iterator<Object[]> it = list.iterator();

				while (it.hasNext()) {
					GroupMemeberInfo memebrInfo = new GroupMemeberInfo();
					Object[] item = it.next();
					GroupUserInfo guInfo = (GroupUserInfo) item[0];
					UserInfo uInfo = (UserInfo) item[1];
					memebrInfo.setGroupMemeberInfo(guInfo);
					memebrInfo.setUserInfo(uInfo);
					groupMemberList.add(memebrInfo);
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
		return groupMemberList;
	}

	// Check the user has joined the group or not.
	private static GroupUserInfo tryGetGroupUser(int userId, int groupId,
			Session session) {
		GroupUserInfo guEntity = null;
		String hqlQuery = "from GroupUserInfo as gu where gu.userId=" + userId
				+ "and gu.groupId=" + groupId;
		Query query = session.createQuery(hqlQuery);

		@SuppressWarnings("unchecked")
		List<GroupUserInfo> list = query.list();

		if (list.size() > 0) {
			guEntity = list.get(0);
		}

		return guEntity;
	}

}
