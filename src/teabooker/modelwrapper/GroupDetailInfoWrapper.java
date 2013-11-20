package teabooker.modelwrapper;

import java.util.List;

import teabooker.model.*;

public class GroupDetailInfoWrapper extends CommonObjectWrapper {

	private List<GroupDetailInfo> groupDetailList;

	public List<GroupDetailInfo> getGroupDetailList() {
		return groupDetailList;
	}

	public void setGroupDetailList(List<GroupDetailInfo> groupDetailList) {
		this.groupDetailList = groupDetailList;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	private int size;

}
