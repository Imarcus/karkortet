package imarcus.android.karkortet;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;

/*
 * An array adapter for handling the restaurant gui list elements
 */
public class RestaurantListAdapter extends ArrayAdapter<String> {

	private ArrayList<String> restaurants;
	private Context context;
	private boolean restaurantPreviouslyChosen; 	//If the user has checked this list element.
	private SharedPreferences sharedPref;
	
	public RestaurantListAdapter(Context context, int resource, ArrayList<String> restaurants) {
	    super(context, resource, restaurants);
	    this.restaurants = restaurants;
	    this.context = context;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	   
		//Get choosen restaurants
		sharedPref = context.getSharedPreferences(Constants.RESTAURANT_PREFS, 0);
		
		
		LayoutInflater inflater = (LayoutInflater) context
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.restaurant_list_view_element, parent, false);
		
		
		CheckedTextView checkedTV = (CheckedTextView) rowView.findViewById(R.id.CheckedTextViewRestaurant);
		CheckBox checkBox = (CheckBox) rowView.findViewById(R.id.CheckBoxRestaurant);
	
		//Set the right restaurant name for this list element
	    checkedTV.setText(Constants.uriToRegularRestNames.get(restaurants.get(position)));
	    
	    //Set previously checked or not
	    restaurantPreviouslyChosen = sharedPref.getBoolean(restaurants.get(position), false);
		checkBox.setChecked(restaurantPreviouslyChosen);
	
	    return rowView;
	}
}