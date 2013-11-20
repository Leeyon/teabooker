package teabooker.modelwrapper;

import java.util.List;

import teabooker.model.*;

public class EventsWrapper extends CommonObjectWrapper {

	private List<EventDetailInfo> eventDetial;

	public List<EventDetailInfo> getEventDetial() {
		return eventDetial;
	}

	public void setEventDetial(List<EventDetailInfo> eventDetial) {
		this.eventDetial = eventDetial;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	private int size;
}
