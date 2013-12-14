package imarcus.android.karkortet;

public class Restaurant {
	
	private String restaurantName;
	private String dishName;
	private String todaysFood;
	
	public Restaurant(String restaurantName, String todaysFood, String dishName){
		this.restaurantName = restaurantName;
		this.todaysFood = todaysFood;
		this.dishName = dishName;
	}

	public String getDishName() {
		return dishName;
	}

	public void setDishName(String dishName) {
		this.dishName = dishName;
	}

	public String getRestaurantName() {
		return restaurantName;
	}

	public void setRestaurantName(String restaurantName) {
		this.restaurantName = restaurantName;
	}

	public String getTodaysFood() {
		return todaysFood;
	}

	public void setTodaysFood(String todaysFood) {
		this.todaysFood = todaysFood;
	}

}
