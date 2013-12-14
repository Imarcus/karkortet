package imarcus.android.karkortet;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends Activity {

	private String cardNr;
	private EditText cardNrEditText;
	private TextView errorTextView;	
	private Bundle extras;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		//Get the data from the card activity
		extras = getIntent().getExtras();
		
		SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
		//Get the saved card number, if there is none then the string is set to ""
		String savedCardNr = sharedPref.getString(getString(R.string.CARD_NUMBER), "");

		//If we didn't get here by the back button from the card activity //If there is a saved card number
		if(extras == null && !savedCardNr.equals("")){
			//Go directly to the card activity with the saved card nr
			setContentView(R.layout.loading_screen);
			cardNr = savedCardNr;
			new Chalmrest().execute(savedCardNr);
		} else {	
			initLayout();
		}
	}
	
	private void initLayout(){
		setContentView(R.layout.activity_login);
		
		cardNrEditText = (EditText) findViewById(R.id.CardNumberEditText);
		
		if(extras != null) {
			cardNrEditText.setText(extras.getString("cardNr"));
		}
    	
		final Button confirmButton = (Button) findViewById(R.id.ConfirmCardNumberButton);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	cardNrEditText = (EditText) findViewById(R.id.CardNumberEditText);
                cardNr = cardNrEditText.getText().toString();
                new Chalmrest().execute(cardNr);
            }
        });
        
        errorTextView = (TextView) findViewById(R.id.ErrorTextView);
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
                HttpGet httpget = new HttpGet(Constants.CARD_INFO_URI + cardNr);
                HttpResponse response = httpclient.execute(httpget);
                HttpEntity entity = response.getEntity();
                if (entity == null){
                	throw new ClientProtocolException("Could not connect");
                }

                String s1 = EntityUtils.toString(entity);
                Pattern pattern = Pattern.compile("<ExtendedInfo Name=\"Kortvarde\" Type=\"System.Double\" >(.*?)</ExtendedInfo>");
                Matcher matcher = pattern.matcher(s1);

                if (!matcher.find()){
                	throw new IllegalArgumentException("Card number not valid");
                }

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
                if (!matcher.find()){
                	throw new IllegalArgumentException("Card number not valid");
                }
                
                String name = matcher.group(1);

                String[] results = new String[2];

                results[0] = name;
                results[1] = value;

                return results;

            } catch (IllegalArgumentException e){
            	e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    	errorTextView.setText("Invalid card number");
                    }
                });
                cancel(true);
                return null;
                
            } catch (ClientProtocolException e) {
            	e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    	errorTextView.setText("Could not connect");
                    }
                });
                cancel(true);
                return null;
                
			} catch (IOException e) {
            	e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    	errorTextView.setText("Could not connect");
                    }
                });
                cancel(true);
                return null;
			}

        	
        }

        @Override
        protected void onPostExecute(String[] results) {
        	
        	if(results == null){
        		return;
        	}
        	
        	SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        	SharedPreferences.Editor editor = sharedPref.edit();
        	editor.putString(getString(R.string.CARD_NUMBER), cardNr);
        	editor.commit();
        	
        	Intent intent = new Intent(getApplicationContext(), CardActivity.class);
        	intent.putExtra("name", results[0]);
        	intent.putExtra("value", results[1]);
        	intent.putExtra("cardNr", cardNr);
        	startActivity(intent);

        }
    }

}
