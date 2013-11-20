package teabooker.modelwrapper;

import teabooker.model.*;

//For userinfo entity, add Josn message.
public class UserinfoWrapper extends CommonObjectWrapper  {
	private UserInfo userInfo;

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}
}
