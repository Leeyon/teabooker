package teabooker.modelwrapper;

import teabooker.model.*;
import java.util.List;

public class EventMemberWrapper extends CommonObjectWrapper {

	private List<UserInfo> eventMemebers;
	
	public List<UserInfo> getEventMemebers() {
		return eventMemebers;
	}
	public void setEventMemebers(List<UserInfo> eventMemebers) {
		this.eventMemebers = eventMemebers;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	
	private int size;
	
}
