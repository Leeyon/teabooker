package teabooker.modelwrapper;

import teabooker.model.ShopAddressInfo;
import java.util.List;

public class ShopAddressInfoWrapper extends CommonObjectWrapper {
	private List<ShopAddressInfo> shopAddressList;

	public List<ShopAddressInfo> getShopAddressList() {
		return shopAddressList;
	}

	public void setShopAddressList(List<ShopAddressInfo> shopAddressList) {
		this.shopAddressList = shopAddressList;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	private int size;
}
