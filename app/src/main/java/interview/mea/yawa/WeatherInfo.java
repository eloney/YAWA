package interview.mea.yawa;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import java.util.Locale;
import java.util.Map;

import interview.mea.yawa.weather.WeatherJSONEntity;


public class WeatherInfo extends Activity {

    private static final String TAG = WeatherInfo.class.getSimpleName();

    public final static String KEY_WEATHER_RESULT_SER = "KEY_WEATHER_RESULT";
    public final static String KEY_DAY = "KEY_DAY";

    private TextView cityNameLabel;
    private TextView weatherDays;
    private ProgressBar progressBar;
    private ListView weatherInfo;

    private String cityName;
    private int days;
    private WeatherJSONEntity weatherResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weathinfo);
        findViews();
        extractFromBundle();
        checkWeather();
    }


    private void findViews() {
        cityNameLabel = (TextView) findViewById(R.id.TextView_cityNameLabel);
        progressBar = (ProgressBar) findViewById(R.id.ProgressBar_checkWeather);
        weatherInfo = (ListView) findViewById(R.id.ListView_weatherInfo);
        weatherDays = (TextView) findViewById(R.id.TextView_weatherDays);
    }

    private void extractFromBundle() {
        Bundle bundle = this.getIntent().getExtras();
        cityName = bundle.getString(YAWA.KEY_CITYNAME);
        days = bundle.getInt(YAWA.KEY_DAYS);
    }

    /**
     * Shows the progress UI and hides the weather info list view..
     */
    private void showProgress(final boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        weatherInfo.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    /**
     * Attempts to download the Jason file of the weather.
     */
    public void checkWeather() {
        //generating the url to download Jason file
        //sample url: http://api.openweathermap.org/data/2.5/forecast/daily?mode=json&units=metric&q=auckland&cnt=7
//        String url = "http://api.openweathermap.org/data/2.5/forecast/daily";
//        String parameters = "mode=json&units=metric";
//        parameters = parameters + "&q=" + cityName;
//        parameters = parameters + "&cnt=" + days;
//        url = url + "?" + parameters;
        String url = getString(R.string.url_weather_basic);
        String parameters = getString(R.string.url_weather_parameters_basic);
        parameters = parameters + getString(R.string.url_weather_parameters_city) + cityName;
        parameters = parameters + getString(R.string.url_weather_parameters_days) + days;
        url = url + parameters;

        //AsyncTask
        CheckWeatherTask checkWeatherTask = new CheckWeatherTask();
        checkWeatherTask.execute(url);
    }

    /**
     * AsyncTask to check weather
     */
    public class CheckWeatherTask extends AsyncTask<String, Void, String> {

        private String weatherData = "";

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

            if (weatherData.equals("")) {
                {
                    new AlertDialog.Builder(WeatherInfo.this).setMessage(R.string.hint_timeout).setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).show();
                }
            } else {
                convertJSONToEntity(weatherData);
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true);
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

            // Setting timeout
            urlConnection.setConnectTimeout(10000);

            // Connecting to url
            urlConnection.connect();


            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            br.close();

        } catch (Exception e) {
            Log.d(TAG, e.toString());
            e.printStackTrace();
        } finally {
            if (iStream != null)
                iStream.close();
            if (urlConnection != null)
                urlConnection.disconnect();
        }

        return data;
    }

    private void convertJSONToEntity(String weatherData) {
        int cod = 0;

        try {
            JSONObject weatherObject;
            weatherObject = new JSONObject(weatherData);
            cod = weatherObject.getInt("cod");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (cod == 404) {
            new AlertDialog.Builder(WeatherInfo.this).setMessage(R.string.hint_noresult).setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            }).show();

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

    private void showWeather() {
        cityNameLabel.setText(weatherResult.city.name + " " + getString(R.string.weather));
        ArrayList<Map<String, Object>> basicWeatherLists = new ArrayList<Map<String, Object>>();

        //get current date
        Calendar currentDate = Calendar.getInstance();
        SimpleDateFormat myFmt = new SimpleDateFormat(getString(R.string.format_date), Locale.getDefault());

        //ArrayList to show in the list view
        for (int i = 0; i < weatherResult.cnt; i++) {
            //get matching date
            currentDate.add(Calendar.DAY_OF_MONTH, 1);
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("title", myFmt.format(currentDate.getTime()));
            //example: "12.20℃, moderate rain"
            item.put("text", weatherResult.list.get(i).temp.day.toString() + getString(R.string.tempsymbol) + getString(R.string.comma) + " " + weatherResult.list.get(i).weather.get(0).description);
            basicWeatherLists.add(item);
        }

        SimpleAdapter adapter = new SimpleAdapter(this, basicWeatherLists, android.R.layout.simple_list_item_2,
                new String[]{"title", "text"}, new int[]{android.R.id.text1, android.R.id.text2});
        weatherInfo.setAdapter(adapter);
        //example: "Next 7 Days (click for more info)"
        weatherDays.setText(getString(R.string.next) + " " + weatherResult.cnt
                + (weatherResult.cnt == 1 ?
                " " + getString(R.string.day_singular) + " " + getString(R.string.weatherinfo_clickformore)
                :
                " " + getString(R.string.day_plural) + " " + getString(R.string.weatherinfo_clickformore)));

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
        bundle.putInt(KEY_DAY, day);
        bundle.putSerializable(KEY_WEATHER_RESULT_SER, weatherResult);
        intent.putExtras(bundle);

        startActivity(intent);
    }
}
