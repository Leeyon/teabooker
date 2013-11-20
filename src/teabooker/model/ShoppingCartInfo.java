package teabooker.model;

import java.math.BigDecimal;

//Shopping Cart Info
public class ShoppingCartInfo {

	private OrderInfo orderInfo;

	public OrderInfo getOrderInfo() {
		return orderInfo;
	}

	public void setOrderInfo(OrderInfo orderInfo) {
		this.orderInfo = orderInfo;
	}

	public FoodInfo getFoodInfo() {
		return foodInfo;
	}

	public void setFoodInfo(FoodInfo foodInfo) {
		this.foodInfo = foodInfo;
	}

	private FoodInfo foodInfo;

	private float orderTotalPrice;

	public float getOrderTotalPrice() {
		return orderTotalPrice;
	}

	public void setOrderTotalPrice() {
		float foodPrice = Float.parseFloat(foodInfo.getFoodPrice());
		int foodCount = orderInfo.getFoodCount();
		BigDecimal bigDecimal = new BigDecimal(foodPrice);
		float foodPriceValue = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP)
				.floatValue();
		this.orderTotalPrice = foodPriceValue * foodCount;
	}

}
