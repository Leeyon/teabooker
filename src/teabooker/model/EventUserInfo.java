package teabooker.model;

// Generated Nov 8, 2013 4:19:18 PM by Hibernate Tools 3.4.0.CR1


/**
 * EventUser generated by hbm2java
 */
public class EventUserInfo implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1985899869294069695L;
	private Integer id;
	private int userId;
	private int eventId;
	private int userStatus;
	private UserInfo eventUser;

	public UserInfo getEventUser() {
		return eventUser;
	}

	public void setEventUser(UserInfo eventUser) {
		this.eventUser = eventUser;
	}

	public EventUserInfo() {
	}

	public EventUserInfo(int userId, int eventId, int userStatus) {
		this.userId = userId;
		this.eventId = eventId;
		this.userStatus = userStatus;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getEventId() {
		return this.eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

	public int getUserStatus() {
		return this.userStatus;
	}

	public void setUserStatus(int userStatus) {
		this.userStatus = userStatus;
	}

}
