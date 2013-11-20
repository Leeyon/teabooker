package teabooker.modelwrapper;

import teabooker.model.ShopInfo;
import java.util.List;

public class ShopInfoWrapper extends CommonObjectWrapper {
	private List<ShopInfo> shopList;

	public List<ShopInfo> getShopList() {
		return shopList;
	}

	public void setShopList(List<ShopInfo> shopList) {
		this.shopList = shopList;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	private int size;
}
