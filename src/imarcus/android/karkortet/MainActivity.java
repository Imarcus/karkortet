package imarcus.android.karkortet;

import android.app.Activity;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.view.View;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {

    private String cardNr;
    SlidePageAdapter pageAdapter;
    private ArrayList<Restaurant> restaurants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        cardNr = "3819273365782141";

        new Chalmrest().execute(cardNr);
        
        new ChalmFood().execute();
        
    }
    
    private List<Fragment> getFragments(){
    	
		  List<Fragment> fList = new ArrayList<Fragment>();

    	
    	for(Restaurant restaurant : restaurants){
    		fList.add(SlideFragment.newInstance(restaurant.getTodaysFood()));
    	}

		return fList;

    }


    public static void updateText(String[] array){

    }

   private class Chalmrest extends AsyncTask<String, Void, String[]> {

        @Override
        protected String[] doInBackground(String... strings) {
            try {


                String cardNr = strings[0];

                HttpClient httpclient = new DefaultHttpClient();
                HttpGet httpget = new HttpGet("http://kortladdning.chalmerskonferens.se/bgw.aspx?type=getCardAndArticles&card=" + cardNr);
                HttpResponse response = httpclient.execute(httpget);
                HttpEntity entity = response.getEntity();
                if (entity == null)
                    throw new Exception("Couldn't connect!");

                String s1 = EntityUtils.toString(entity);
                Pattern pattern = Pattern.compile("<ExtendedInfo Name=\"Kortvarde\" Type=\"System.Double\" >(.*?)</ExtendedInfo>");
                Matcher matcher = pattern.matcher(s1);

                if (!matcher.find())
                    throw new Exception("Couldn't parse value!");

                String value = matcher.group(1);

                StringBuilder sb = new StringBuilder();
                int last = 0;
                Matcher match = Pattern.compile("_x([0-9A-Fa-f]{4})_").matcher(value);
                while (match.find()) {
                    sb.append(value.substring(last, Math.max(match.start() - 1, 0)));
                    int i = Integer.parseInt(match.group(1), 16);
                    sb.append((char)i);
                    last = match.end();
                }
                sb.append(value.substring(last));
                value = sb.toString();

                matcher = Pattern.compile("<CardInfo id=\"" + cardNr + "\" Name=\"(.*?)\"").matcher(s1);
                if (!matcher.find())
                    throw new Exception();
                String name = matcher.group(1);

                String[] array = new String[2];

                array[0] = name;
                array[1] = value;

                return array;

            }
            catch (Exception e)
            {
                e.printStackTrace();
                cancel(true);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] result) {

            TextView cardNumber = (TextView) findViewById(R.id.cardNumber);
            cardNumber.setText(cardNr);

            TextView accountBalance = (TextView) findViewById(R.id.accountBalance);
            accountBalance.setText(result[1] + " kr");

        }
    }
   
   private class ChalmFood extends AsyncTask<String, Void, ArrayList<Restaurant>> {

	@Override
	protected ArrayList<Restaurant> doInBackground(String... strings) {
		try{
			//Create xml document from uri
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse("http://cm.lskitchen.se/johanneberg/karrestaurangen/sv/2013-11-19.rss");
			
			//Build expression for finding xml elements
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			XPathExpression expr = xpath.compile("//item");
			//Fetch the item nodes from the document
			NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			
			ArrayList<Restaurant> result = new ArrayList<Restaurant>();
			Node currentNode;
			NodeList childNodes;
			String currentRestName = "No Rest name found1";
			String currentFood = "No dish found";
			for(int i = 0; i < nodes.getLength(); i++){
				currentNode = nodes.item(i);
				childNodes = currentNode.getChildNodes();
				for(int j = 0; j < childNodes.getLength(); j++){
					System.out.println("---------CHILDNODES------------" + childNodes.item(j).getNodeName() + "   " + childNodes.item(j).getNodeValue());
					if(childNodes.item(j).getNodeName().equals("title")){
						System.out.println("---------TITLE------------  " + childNodes.item(j).getFirstChild().getNodeValue());	
						currentRestName = childNodes.item(j).getFirstChild().getNodeValue();
					} else if(childNodes.item(j).getNodeName().equals("description")){
						System.out.println("---------DESCRIPTION------------  " +   childNodes.item(j).getFirstChild().getNodeValue());
						currentFood = childNodes.item(j).getFirstChild().getNodeValue();
					}
					System.out.println("----------LISTLENGTH--" + result.size() + "-------------");
				}
				result.add(new Restaurant(currentRestName, currentFood));
			}
			return result;
			
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}
		
	}
	
	 @Override
     protected void onPostExecute(ArrayList<Restaurant> result) {
		 restaurants = result;
		 List<Fragment> fragments = getFragments();
		 pageAdapter = new SlidePageAdapter(getSupportFragmentManager(), fragments);
		 View viewPager = findViewById(R.id.viewpager);
		 ViewPager pager = (ViewPager)viewPager.findViewById(R.id.viewpager);
		 pager.setAdapter(pageAdapter);

     }
   
   }

}
