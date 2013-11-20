package teabooker.model;

public class EventDetailShopInfo {

	private ShopInfo shopInfo;

	public ShopInfo getShopInfo() {
		return shopInfo;
	}

	public void setShopInfo(ShopInfo shopInfo) {
		this.shopInfo = shopInfo;
	}

	public ShopAddressInfo getAddresssInfo() {
		return addresssInfo;
	}

	public void setAddresssInfo(ShopAddressInfo addresssInfo) {
		this.addresssInfo = addresssInfo;
	}

	private ShopAddressInfo addresssInfo;

}
