package teabooker.modelwrapper;

import teabooker.model.FoodInfo;
import java.util.List;

public class FoodInfoWrapper extends CommonObjectWrapper {

	private List<FoodInfo> foodList;

	public List<FoodInfo> getFoodList() {
		return foodList;
	}

	public void setFoodList(List<FoodInfo> foodList) {
		this.foodList = foodList;
	}

	private int size;

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
}
