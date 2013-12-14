package imarcus.android.karkortet;

import android.app.Activity;
import android.content.Intent;
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
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CardActivity extends FragmentActivity {

	private String cardNr;
	private String name;
	private String value;
	SlidePageAdapter pageAdapter;
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

		final Button backButton = (Button) findViewById(R.id.BackButton);
		backButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						LoginActivity.class);
				intent.putExtra("cardNr", cardNr);
				startActivity(intent);
			}
		});

		new ChalmFood().execute();

	}

	private List<Fragment> getFragments() {
		List<Fragment> fList = new ArrayList<Fragment>();

		for (Restaurant restaurant : restaurants) {
			fList.add(SlideFragment.newInstance(restaurant.getRestaurantName(),
					restaurant.getTodaysFood(), restaurant.getDishName()));
		}
		return fList;
	}

	private void setCardInformation(String name, String value, String cardNr) {
		TextView nameView = (TextView) findViewById(R.id.TextViewName);
		nameView.setText(name);

		TextView cardNumber = (TextView) findViewById(R.id.TextViewCardNumber);
		cardNumber.setText(cardNr);

		TextView accountBalance = (TextView) findViewById(R.id.TextViewAccountBalance);
		accountBalance.setText(value + " kr");
	}

	private class ChalmFood extends
			AsyncTask<String, Void, ArrayList<Restaurant>> {

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

				ArrayList<String> restaurants = new ArrayList<String>();
				
				restaurants.add("karrestaurangen");
				restaurants.add("linsen");
				

				Calendar today = Calendar.getInstance();

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
				
				

				//Loop through all chosen restaurants
				for (String r : restaurants) {
					
					language = getResources().getConfiguration().locale
							.getLanguage();
					if(r.equals("linsen")){
						language = "sv";
					}
					if (!language.equals("sv")) {
						language = "en";
					}
					
					Document doc = builder
							.parse("http://cm.lskitchen.se/johanneberg/" + r + "/"
									+ language
									+ "/"
									+ today.get(Calendar.YEAR)
									+ "-"
									+ today.get(Calendar.MONTH)
									+ "-"
									+ today.get(Calendar.DAY_OF_MONTH) + ".rss");

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
			restaurants = result;
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
