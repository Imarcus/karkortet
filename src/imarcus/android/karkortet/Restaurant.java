package imarcus.android.karkortet;

public class Restaurant {
	
	private String restaurantName;
	private String todaysFood;
	
	public Restaurant(String restaurantName, String todaysFood){
		this.restaurantName = restaurantName;
		this.todaysFood = todaysFood;
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
