package imarcus.android.karkortet;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SlideFragment extends Fragment {

	public static final String FOOD_MESSAGE = "FOOD_MESSAGE";
	public static final String DISH_MESSAGE = "DISH_MESSAGE";
	public static final String RESTAURANT_MESSAGE = "RESTAURANT_MESSAGE";



	public static final SlideFragment newInstance(String restaurantName, String food, String dishName)

	{
		SlideFragment f = new SlideFragment();
		Bundle bdl = new Bundle(1);
		bdl.putString(FOOD_MESSAGE, food);
		bdl.putString(DISH_MESSAGE, dishName);
		bdl.putString(RESTAURANT_MESSAGE, restaurantName);
		f.setArguments(bdl);
		return f;

	}



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		//Display food
		String message = getArguments().getString(FOOD_MESSAGE);
		View v = inflater.inflate(R.layout.slidefragment_layout, container, false);
		TextView textView = (TextView)v.findViewById(R.id.TextViewRestaurantFood);
		textView.setText(message);
		
		//Display the restaurant name
		message = getArguments().getString(DISH_MESSAGE) + " - " + getArguments().getString(RESTAURANT_MESSAGE);
		textView = (TextView)v.findViewById(R.id.TextViewRestaurantName);
		textView.setText(message);

		return v;

	}

}
