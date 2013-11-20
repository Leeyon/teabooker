package teabooker.modelwrapper;

import teabooker.model.GroupMemeberInfo;
import java.util.List;

public class GroupMemberWrapper extends CommonObjectWrapper {
	private List<GroupMemeberInfo> memeberList;

	public List<GroupMemeberInfo> getMemeberList() {
		return memeberList;
	}

	public void setMemeberList(List<GroupMemeberInfo> memeberList) {
		this.memeberList = memeberList;
	}

	private int size;

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
}
