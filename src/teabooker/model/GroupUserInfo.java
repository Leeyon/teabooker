package teabooker.model;

// Generated Nov 4, 2013 4:57:12 PM by Hibernate Tools 3.4.0.CR1

/**
 * GroupUser generated by hbm2java
 */
public class GroupUserInfo implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5671055332626996076L;
	private Integer id;
	private int groupId;
	private int userId;
	private int isOwner;
	private Integer userStatus;
	private String requestMsg;

	//Exclude this for JSON serialize 
	private transient GroupInfo groupInfo;

	private transient UserInfo userInfo;
	
	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	public GroupInfo getGroupInfo() {
		return groupInfo;
	}

	public void setGroupInfo(GroupInfo groupInfo) {
		this.groupInfo = groupInfo;
	}

	public GroupUserInfo() {
	}

	public GroupUserInfo(int groupId, int userId, int isOwner) {
		this.groupId = groupId;
		this.userId = userId;
		this.isOwner = isOwner;
	}

	public GroupUserInfo(int groupId, int userId, int isOwner,
			Integer userStatus, String requestMsg) {
		this.groupId = groupId;
		this.userId = userId;
		this.isOwner = isOwner;
		this.userStatus = userStatus;
		this.requestMsg = requestMsg;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getGroupId() {
		return this.groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getIsOwner() {
		return this.isOwner;
	}

	public void setIsOwner(int isOwner) {
		this.isOwner = isOwner;
	}

	public Integer getUserStatus() {
		return this.userStatus;
	}

	public void setUserStatus(Integer userStatus) {
		this.userStatus = userStatus;
	}

	public String getRequestMsg() {
		return this.requestMsg;
	}

	public void setRequestMsg(String requestMsg) {
		this.requestMsg = requestMsg;
	}

}
