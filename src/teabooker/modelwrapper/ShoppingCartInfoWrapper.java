package teabooker.modelwrapper;

import java.util.List;

import teabooker.model.ShoppingCartInfo;

public class ShoppingCartInfoWrapper extends CommonObjectWrapper {

	private List<ShoppingCartInfo> shoppingCartInfoList;

	public List<ShoppingCartInfo> getShoppingCartInfoList() {
		return shoppingCartInfoList;
	}

	public void setShoppingCartInfoList(
			List<ShoppingCartInfo> shoppingCartInfoList) {
		this.shoppingCartInfoList = shoppingCartInfoList;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public float getUserTotalPrice() {
		return userTotalPrice;
	}

	public void setUserTotalPrice() {
		userTotalPrice = 0;
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				ShoppingCartInfo cartInfo = shoppingCartInfoList.get(i);
				userTotalPrice += cartInfo.getOrderTotalPrice();
			}
		}
	}

	private int size;
	private float userTotalPrice;

}
