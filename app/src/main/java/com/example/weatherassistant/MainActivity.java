package com.example.weatherassistant;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    Button button;
    EditText city;
    TextView result;
    TextView result2;
    //http://api.openweathermap.org/data/2.5/weather?q=paris&appid=21eec890d62fec0379499329c942e176
    String baseURL = "http://api.openweathermap.org/data/2.5/weather?q=";
    String apiKey = "&appid=e2eaa43530c453e5f48d79f76f620aed";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);
        city = (EditText) findViewById(R.id.getCity);
        result = (TextView) findViewById(R.id.result);
        result2 = (TextView) findViewById(R.id.result2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String myURL = baseURL + city.getText().toString() + apiKey;

                try  {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {

                }

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, myURL, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject jsonObject) {
                                Log.i("JSON", "JSON: " + jsonObject);

                                try {
                                    String info = jsonObject.getString("weather");
                                    Log.i("INFO", "INFO: "+ info);

                                    JSONArray ar = new JSONArray(info);

                                    for (int i = 0; i < ar.length(); i++){
                                        JSONObject parObj = ar.getJSONObject(i);
                                        String myWeather = parObj.getString("main" );
                                        result.setText(myWeather);
                                        Log.i("ID", "ID: " + parObj.getString("id"));
                                        Log.i("MAIN", "MAIN: " + parObj.getString("main"));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    String tempinfo = jsonObject.getString("main");
                                    JSONObject mytemp = new JSONObject(tempinfo);
                                    String temperature = mytemp.getString("temp");
                                    Double tmp = Double.parseDouble(temperature);
                                    tmp = tmp - 273.15;
                                    tmp = Math.round(tmp *100)/100.0d;
                                    temperature = String.valueOf(tmp);
                                    result2.setText(temperature + " ° celcius");
                                    //Log.i("tmp", "tmp is " + mytemp.getString("temp"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                Log.i("Error", "Something went wrong" + volleyError);

                            }
                        }
                );
                MySingleton.getInstance(MainActivity.this).addToRequestQue(jsonObjectRequest);
            }
        });

    }
}
