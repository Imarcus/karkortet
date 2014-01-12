package imarcus.android.karkortet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CardActivity extends FragmentActivity {

	private String cardNr;
	private String name;
	private String value;
	SlidePageAdapter pageAdapter;
	//Restaurants displayed in the slide fragments
	private ArrayList<Restaurant> restaurants;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card);

		// cardNr = "3819273365782141";

		Bundle extras = getIntent().getExtras();

		cardNr = extras.getString("cardNr");
		name = extras.getString("name");
		value = extras.getString("value");

		setCardInformation(name, value, cardNr);

		initButtons();

		new ChalmFood().execute();
		findViewById(R.id.TextViewLoading).setVisibility(View.VISIBLE);


	}
	
	private void initButtons(){
		//Setup of the back button
		final Button backButton = (Button) findViewById(R.id.BackButton);
		backButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						LoginActivity.class);
				intent.putExtra("cardNr", cardNr);
				startActivity(intent);
			}
		});
		
		//Setup of the refresh button
		final Button refreshButton = (Button) findViewById(R.id.RefreshButton);
		refreshButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				new ChalmFood().execute();
				findViewById(R.id.TextViewLoading).setVisibility(View.VISIBLE);
				
			}
		});
	}
	
	private List<Fragment> getFragments() {
		List<Fragment> fList = new ArrayList<Fragment>();
		
		if(restaurants.size() == 0){
			fList.add(SlideFragment.newInstance("",getString(R.string.no_dishes_found),""));
		} else {
			for (Restaurant restaurant : restaurants) {
				fList.add(SlideFragment.newInstance(restaurant.getRestaurantName(),
						restaurant.getTodaysFood(), restaurant.getDishName()));
			}
		}
		return fList;
	}

	//Sets card information shown in this activity
	private void setCardInformation(String name, String value, String cardNr) {
		
		TextView nameView = (TextView) findViewById(R.id.TextViewName);
		TextView cardNumber = (TextView) findViewById(R.id.TextViewCardNumber);
		TextView accountBalance = (TextView) findViewById(R.id.TextViewAccountBalance);

		if(cardNr.equals("")){ //If no card number was entered
			nameView.setText(R.string.no_name);
			cardNumber.setText(R.string.no_card_number);
			accountBalance.setText("0 kr");
		} else {
			nameView.setText(name);
			cardNumber.setText(cardNr);
			accountBalance.setText(value + " kr");
		}
	}

	/*
	 * A class for fetching menus of the choosen restaurants
	 * Uses xml-files from: http://www.chalmerskonferens.se/rss-2/
	 *
	 */
	private class ChalmFood extends
			AsyncTask<String, Void, ArrayList<Restaurant>> {

		//Do nothing
		protected void onPreExecute() {
		}

		@Override
		protected ArrayList<Restaurant> doInBackground(String... strings) {
			try {
				// Create xml document from uri
				DocumentBuilderFactory factory = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();

				//If device language is not Swedish, choose English
				String language = getResources().getConfiguration().locale
						.getLanguage();
				if (!language.equals("sv")) {
					language = "en";
				}

				//Get choosen restaurants from the restaurant gui list
				SharedPreferences sharedPref = getSharedPreferences(Constants.RESTAURANT_PREFS, 0);
				ArrayList<String> choosenRestaurants = new ArrayList<String>();
				for(String restaurant : Constants.allRestaurantsArray){
					if(sharedPref.getBoolean(restaurant, false)){
						choosenRestaurants.add(restaurant);
					}
				}	
				
				if(choosenRestaurants.size() == 0){
					return null;
				}

				// Build expression for finding xml elements
				XPathFactory xPathfactory = XPathFactory.newInstance();
				XPath xpath = xPathfactory.newXPath();
				XPathExpression expr;

				//Variables used in the loop
				NodeList nodes;
				ArrayList<Restaurant> result = new ArrayList<Restaurant>();
				NodeList channelNodes;
				Node currentNode;
				NodeList childNodes;
				String currentRestName = "No restaurant name";
				String currentDishName = "No dish name found";
				String currentFood = "No dish found";
				String location = "";
				
				//Set language, if it is not Swedish, set it to English.
				language = getResources().getConfiguration().locale
						.getLanguage();
				if (!language.equals("sv")) {
					language = "en";
				}
				
				
				//Loop through all chosen restaurants and add their dishes as slide fragments
				for (String restaurant : choosenRestaurants) {
					
					if(Constants.johannebergRestaurantsArray.contains(restaurant)){
						location = "johanneberg";
					} else if(Constants.lindholmenRestaurantsArray.contains(restaurant)){
						location = "lindholmen";
					}
					
					
					
					Document doc = builder
							.parse("http://cm.lskitchen.se/" + location + "/" + restaurant + "/"
					+ language
					+ "/"
					+ "today" + ".rss");

					expr = xpath.compile("//channel");

					// Fetch the channel nodes from the document
					nodes = (NodeList) expr.evaluate(doc,
							XPathConstants.NODESET);
					channelNodes = nodes.item(0).getChildNodes();
					
					for(int k = 0; k < channelNodes.getLength(); k++){
						if(channelNodes.item(k).getNodeName().equals("title")){
							currentRestName = channelNodes.item(k).getFirstChild().getNodeValue();
						}
					}
					

					expr = xpath.compile("//item");
					
					// Fetch the item nodes from the document
					nodes = (NodeList) expr.evaluate(doc,
							XPathConstants.NODESET);

					for (int i = 0; i < nodes.getLength(); i++) {
						currentNode = nodes.item(i);
						childNodes = currentNode.getChildNodes();
						for (int j = 0; j < childNodes.getLength(); j++) {
							if (childNodes.item(j).getNodeName()
									.equals("title")) {
								currentDishName = childNodes.item(j)
										.getFirstChild().getNodeValue();
							} else if (childNodes.item(j).getNodeName()
									.equals("description")) {
								currentFood = childNodes.item(j)
										.getFirstChild().getNodeValue();
							}
						}
						result.add(new Restaurant(currentRestName, currentFood
							.split("@")[0], currentDishName)); //Removing the '@' sign from the price
					}
				}

				return result;

			} catch (Exception e) { //What do?
				e.printStackTrace();
				return null;
			}

		}

		@Override
		protected void onPostExecute(ArrayList<Restaurant> result) {
			findViewById(R.id.TextViewLoading).setVisibility(View.INVISIBLE);
			if(result == null){
				restaurants  = new ArrayList<Restaurant>();
			} else {
				restaurants = result;
			}
			List<Fragment> fragments = getFragments();
			pageAdapter = new SlidePageAdapter(getSupportFragmentManager(),
					fragments);
			View viewPager = findViewById(R.id.viewpager);
			ViewPager pager = (ViewPager) viewPager
					.findViewById(R.id.viewpager);
			pager.setAdapter(pageAdapter);
		}

	}

}
