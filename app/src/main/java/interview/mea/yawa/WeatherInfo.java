package interview.mea.yawa;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import interview.mea.yawa.weather.WeatherJSONEntity;


/**
 * Created by Ben_Hasee on 12/03/2015.
 */
public class WeatherInfo extends Activity {

    private String cityName;
    private int days;
    private TextView cityNameLabel;
    private TextView weatherDays;
    private static final String TAG = WeatherInfo.class.getSimpleName();
    private ProgressBar progressBar;
    private ListView weatherInfo;
    private WeatherJSONEntity weatherResult;
    public final static String WEATHER_RESULT_SER_KEY = "WEATHER_RESULT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weathinfo);

        cityNameLabel = (TextView) findViewById(R.id.TextView_cityNameLabel);
        progressBar = (ProgressBar) findViewById(R.id.ProgressBar_checkWeather);
        weatherInfo = (ListView) findViewById(R.id.ListView_weatherInfo);
        weatherDays = (TextView) findViewById(R.id.TextView_weatherDays);

        Bundle bundle = this.getIntent().getExtras();
        cityName = bundle.getString("KEY_CITYNAME");
        days = bundle.getInt("KEY_DAYS");
        cityNameLabel.setText("Checking Weather");
        attemptDownload();


    }


    /**
     * Attempts to download the Jason file of the weather.
     */
    public void attemptDownload() {
        showProgress(true);
        //sample url: http://api.openweathermap.org/data/2.5/forecast/daily?mode=json&units=metric&q=auckland&cnt=7
        String url = "http://api.openweathermap.org/data/2.5/forecast/daily";
        String parameters = "mode=json&units=metric";
        parameters = parameters + "&q=" + cityName;
        parameters = parameters + "&cnt=" + days;
        url = url + "?" + parameters;

        CheckWeatherTask checkWeatherTask = new CheckWeatherTask();
        checkWeatherTask.execute(url);
    }


    /**
     * Shows the progress UI and hides the weather info list view..
     */
    private void showProgress(final boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        weatherInfo.setVisibility(show ? View.GONE : View.VISIBLE);

    }


    public class CheckWeatherTask extends AsyncTask<String, Void, String> {

        String weatherData = "";

        @Override
        protected String doInBackground(String... url) {
            try {
                weatherData = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d(TAG, e.toString());
                e.printStackTrace();
            }

            return weatherData;

        }

        @Override
        protected void onPostExecute(final String result) {

            JSONObject weatherObject = null;
            if (weatherData == "") {
                {
                    Toast.makeText(WeatherInfo.this, "TimeOut", Toast.LENGTH_LONG).show();
                }
            } else {
                int cod = 0;
                try {
                    weatherObject = new JSONObject(weatherData);
                    cod = weatherObject.getInt("cod");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (cod == 404) {
                    Toast.makeText(WeatherInfo.this, "Check city name", Toast.LENGTH_LONG).show();
                } else if (cod == 200) {
                    try {
                        Gson gson = new Gson();
                        weatherResult = gson.fromJson(weatherData, WeatherJSONEntity.class);
                        showProgress(false);
                        showWeather();

                    } catch (Exception e) {
                        Log.d(TAG, e.toString());
                        e.printStackTrace();
                    }
                }
            }
        }

        @Override
        protected void onCancelled() {
            showProgress(false);
        }
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";

        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();


            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            br.close();

        } catch (Exception e) {
            Log.d(TAG, e.toString());
            e.printStackTrace();
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }

        return data;
    }

    private void showWeather() {
        cityNameLabel.setText(weatherResult.city.name + " Weather");
        Calendar currentDate = Calendar.getInstance();
        ArrayList<Map<String, Object>> basicWeatherLists = new ArrayList<Map<String, Object>>();
        SimpleDateFormat myFmt = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < weatherResult.cnt; i++) {
            currentDate.add(Calendar.DAY_OF_MONTH, 1);
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("title", myFmt.format(currentDate.getTime()).toString());
            item.put("text", weatherResult.list.get(i).temp.eve.toString() + "℃, " + weatherResult.list.get(i).weather.get(0).description);
            basicWeatherLists.add(item);
        }

        SimpleAdapter adapter = new SimpleAdapter(this, basicWeatherLists, android.R.layout.simple_list_item_2,
                new String[]{"title", "text"}, new int[]{android.R.id.text1, android.R.id.text2});
        weatherInfo.setAdapter(adapter);
        weatherDays.setText("Next " + weatherResult.cnt + (weatherResult.cnt == 1 ? " Day (click for more info)" : " Days (click for more info)"));

        weatherInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position,
                                    long id) {
                goToWeatherDetail(position);
            }
        });
    }


    private void goToWeatherDetail(int day) {
        Intent intent = new Intent(this, WeatherDetail.class);
        Bundle bundle = new Bundle();
        bundle.putInt("DAY", day);
        bundle.putSerializable(WEATHER_RESULT_SER_KEY, weatherResult);
        intent.putExtras(bundle);

        startActivity(intent);


    }


}
