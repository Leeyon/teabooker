package teabooker.model;

public class EventDetailInfo {

	public EventInfo getEventInfo() {
		return eventInfo;
	}

	public void setEventInfo(EventInfo eventInfo) {
		this.eventInfo = eventInfo;
	}

	public UserInfo getOnwerInfo() {
		return onwerInfo;
	}

	public void setOnwerInfo(UserInfo onwerInfo) {
		this.onwerInfo = onwerInfo;
	}

	public GroupInfo getEventGroup() {
		return eventGroup;
	}

	public void setEventGroup(GroupInfo eventGroup) {
		this.eventGroup = eventGroup;
	}

	public int getTotalMemeberCount() {
		return totalMemeberCount;
	}

	public void setTotalMemeberCount(int totalMemeberCount) {
		this.totalMemeberCount = totalMemeberCount;
	}

	private EventInfo eventInfo;
	private UserInfo onwerInfo;
	private GroupInfo eventGroup;
	private EventDetailShopInfo shopInfo;

	public EventDetailShopInfo getShopInfo() {
		return shopInfo;
	}

	public void setShopInfo(EventDetailShopInfo shopInfo) {
		this.shopInfo = shopInfo;
	}

	private int totalMemeberCount;

}
