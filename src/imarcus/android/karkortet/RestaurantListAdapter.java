package imarcus.android.karkortet;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;

public class RestaurantListAdapter extends ArrayAdapter<String> {

	private ArrayList<String> restaurants;
	private Context context;
	
	public RestaurantListAdapter(Context context, int resource, ArrayList<String> restaurants) {
	    super(context, resource, restaurants);
	    this.restaurants = restaurants;
	    this.context = context;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	   
		LayoutInflater inflater = (LayoutInflater) context
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.restaurant_list_view_element, parent, false);
		
		CheckedTextView checkedTV = (CheckedTextView) rowView.findViewById(R.id.CheckedTextViewRestaurant);
	
	    checkedTV.setText(restaurants.get(position));
	
	    return rowView;
	}
}