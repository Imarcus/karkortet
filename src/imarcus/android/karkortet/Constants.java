package imarcus.android.karkortet;

import java.util.ArrayList;
import java.util.HashMap;

public class Constants {
	
	//Real world restaurant names
	public final static String REAL_KARRESTAURANGEN = "Kårrestaurangen";
	public final static String REAL_BYSSJAN = "Byssjan";
	public final static String REAL_HYLLAN = "Hyllan";
	public final static String REAL_LINSEN = "Linsen";
	public final static String REAL_LSKITCHEN = "L's Kitchen";
	public final static String REAL_RESTAURANGR = "Restaurang R";
	public final static String REAL_KOKBOKEN = "Kokboken";
	
	//URI restaurant names
	public final static String URI_KARRESTAURANGEN = "karrestaurangen";
	public final static String URI_BYSSJAN = "byssjan";
	public final static String URI_HYLLAN = "hyllan";
	public final static String URI_LINSEN = "linsen";
	public final static String URI_LSKITCHEN = "foodcourt";
	public final static String URI_RESTAURANGR = "restaurang";
	public final static String URI_KOKBOKEN = "kokboken";
	
	public final static String CARD_NUMBER_FILE_NAME = "CARD_NUMBER_FILE_NAME";
	
	public final static String CARD_INFO_URI = "http://kortladdning.chalmerskonferens.se/bgw.aspx?type=getCardAndArticles&card=";
	
	public final static String LOGIN_ERROR_MESSAGE = "Card number not valid";
	
	public final static String[] allRestaurants = new String[] { URI_KARRESTAURANGEN, URI_BYSSJAN, URI_HYLLAN,
		URI_LINSEN, URI_LSKITCHEN, URI_RESTAURANGR, URI_KOKBOKEN};
	
	public final static ArrayList<String> johannebergRestaurantsArray = new ArrayList<String>();
	
	public final static ArrayList<String> lindholmenRestaurantsArray = new ArrayList<String>();
		
	public final static String RESTAURANT_PREFS = "RESTAURANT_PREFS";
	
	public final static ArrayList<String> allRestaurantsArray = new ArrayList<String>();
	
	public final static HashMap<String,String> regularToUriRestNames = new HashMap<String,String>();
	
	public final static HashMap<String,String> uriToRegularRestNames = new HashMap<String,String>();
	
	
    
	static {
    	allRestaurantsArray.add(URI_KARRESTAURANGEN);
    	allRestaurantsArray.add(URI_BYSSJAN);
    	allRestaurantsArray.add(URI_HYLLAN);
    	allRestaurantsArray.add(URI_LINSEN);
    	allRestaurantsArray.add(URI_LSKITCHEN);
    	allRestaurantsArray.add(URI_RESTAURANGR);
    	allRestaurantsArray.add(URI_KOKBOKEN);
    	
    	johannebergRestaurantsArray.add(URI_KARRESTAURANGEN);
    	johannebergRestaurantsArray.add(URI_BYSSJAN);
    	johannebergRestaurantsArray.add(URI_HYLLAN);
    	johannebergRestaurantsArray.add(URI_LINSEN);
    	
    	lindholmenRestaurantsArray.add(URI_LSKITCHEN);
    	lindholmenRestaurantsArray.add(URI_RESTAURANGR);
    	lindholmenRestaurantsArray.add(URI_KOKBOKEN);
    	
    	regularToUriRestNames.put(REAL_KARRESTAURANGEN, URI_KARRESTAURANGEN);
    	regularToUriRestNames.put(REAL_BYSSJAN, URI_BYSSJAN);
    	regularToUriRestNames.put(REAL_HYLLAN, URI_HYLLAN);
    	regularToUriRestNames.put(REAL_LINSEN, URI_LINSEN);
    	regularToUriRestNames.put(REAL_LSKITCHEN, URI_LSKITCHEN);
    	regularToUriRestNames.put(REAL_RESTAURANGR, URI_RESTAURANGR);
    	regularToUriRestNames.put(REAL_KOKBOKEN, URI_KOKBOKEN);
    	
    	uriToRegularRestNames.put(URI_KARRESTAURANGEN, REAL_KARRESTAURANGEN);
    	uriToRegularRestNames.put(URI_BYSSJAN, REAL_BYSSJAN);
    	uriToRegularRestNames.put(URI_HYLLAN, REAL_HYLLAN);
    	uriToRegularRestNames.put(URI_LINSEN, REAL_LINSEN);
    	uriToRegularRestNames.put(URI_LSKITCHEN, REAL_LSKITCHEN);
    	uriToRegularRestNames.put(URI_RESTAURANGR, REAL_RESTAURANGR);
    	uriToRegularRestNames.put(URI_KOKBOKEN, REAL_KOKBOKEN);
    	
    }


}
