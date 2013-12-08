package imarcus.android.karkortet;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {

	private String cardNr;
	private final String CARD_INFO_URI = "http://kortladdning.chalmerskonferens.se/bgw.aspx?type=getCardAndArticles&card=";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		final Button confirmButton = (Button) findViewById(R.id.ConfirmCardNumberButton);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	EditText cardNrEditText = (EditText) findViewById(R.id.CardNumberEditText);
                cardNr = cardNrEditText.getText().toString();
                new Chalmrest().execute(cardNr);
            }
        });
	}
	
	private class Chalmrest extends AsyncTask<String, Void, String[]> {

        @Override
        protected String[] doInBackground(String... strings) {
        	String[] results = getCardInformation(strings[0]);
        	
        	return results;
        }
        
        protected String[] getCardInformation(String cardNr){
        	try {

                HttpClient httpclient = new DefaultHttpClient();
                HttpGet httpget = new HttpGet(CARD_INFO_URI + cardNr);
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

                String[] results = new String[2];

                results[0] = name;
                results[1] = value;

                return results;

            }
            catch (Exception e)
            {
                e.printStackTrace();
                cancel(true);
                return null;
            }

        	
        }

        @Override
        protected void onPostExecute(String[] results) {
        	
        	Intent i = new Intent(getApplicationContext(), CardActivity.class);
        	System.out.println(results[0] + " " + results[1]);
        	i.putExtra("name", results[0]);
        	i.putExtra("value", results[1]);
        	i.putExtra("cardNr", cardNr);
        	startActivity(i);
        	
        	/*TextView name = (TextView) findViewById(R.id.TextViewName);
            name.setText(result[0]);

            TextView cardNumber = (TextView) findViewById(R.id.TextViewCardNumber);
            cardNumber.setText(cardNr);

            TextView accountBalance = (TextView) findViewById(R.id.TextViewAccountBalance);
            accountBalance.setText(result[1] + " kr");*/

        }
    }

}
