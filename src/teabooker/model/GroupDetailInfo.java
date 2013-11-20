package teabooker.model;

public class GroupDetailInfo {

	private GroupInfo groupInfo;

	public GroupInfo getGroupInfo() {
		return groupInfo;
	}

	public void setGroupInfo(GroupInfo groupInfo) {
		this.groupInfo = groupInfo;
	}

	public UserInfo getOwnerInfo() {
		return ownerInfo;
	}

	public void setOwnerInfo(UserInfo ownerInfo) {
		this.ownerInfo = ownerInfo;
	}

	private UserInfo ownerInfo;

}
