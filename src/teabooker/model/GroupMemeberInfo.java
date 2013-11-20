package teabooker.model;

public class GroupMemeberInfo {

	private GroupUserInfo groupMemeberInfo;

	public GroupUserInfo getGroupMemeberInfo() {
		return groupMemeberInfo;
	}

	public void setGroupMemeberInfo(GroupUserInfo groupMemeberInfo) {
		this.groupMemeberInfo = groupMemeberInfo;
	}

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	private UserInfo userInfo;

}
