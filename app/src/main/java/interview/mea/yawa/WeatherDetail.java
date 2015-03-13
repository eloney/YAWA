package interview.mea.yawa;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import interview.mea.yawa.weather.WeatherJSONEntity;

/**
 * Created by Ben_Hasee on 13/03/2015.
 */
public class WeatherDetail extends Activity {

    private int day;
    private TextView cityName;
    private TextView weatherDate;
    private ProgressBar progressBar;
    private ImageView weatherIcon;
    private TextView tempLabel;
    private TextView tempEve;
    private TextView tempMax;
    private TextView tempMin;
    private TextView des;
    private static final String TAG = WeatherDetail.class.getSimpleName();

    private WeatherJSONEntity weatherResult;


    MyAsynctask myTask = new MyAsynctask();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weatherdetail);
        weatherResult = (WeatherJSONEntity) getIntent().getSerializableExtra(WeatherInfo.WEATHER_RESULT_SER_KEY);
        day = getIntent().getIntExtra("DAY", 0);
        WeatherJSONEntity.List weatherList = weatherResult.list.get(day);

        Calendar currentDate = Calendar.getInstance();
        SimpleDateFormat myFmt = new SimpleDateFormat("yyyy-MM-dd");
        currentDate.add(Calendar.DAY_OF_MONTH, day + 1);

        cityName = (TextView) findViewById(R.id.TextView_cityName);
        weatherDate = (TextView) findViewById(R.id.TextView_date);
        progressBar = (ProgressBar) findViewById(R.id.ProgressBar_weatherIcon);
        weatherIcon = (ImageView) findViewById(R.id.ImageView_weatherIcon);
        tempLabel = (TextView) findViewById(R.id.TextView_temperture);
        tempEve = (TextView) findViewById(R.id.TextView_tempEve);
        tempMax = (TextView) findViewById(R.id.TextView_tempMax);
        tempMin = (TextView) findViewById(R.id.TextView_tempMin);
        des = (TextView) findViewById(R.id.TextView_des);

        cityName.setText(weatherResult.city.name.toString() + " Weather");
        weatherDate.setText(day == 0 ? "Tomorrow" : myFmt.format(currentDate.getTime()).toString());
        tempEve.setText(weatherList.temp.eve + "℃");
        tempMax.setText("Max " + weatherList.temp.max + "℃");
        tempMin.setText("Min " + weatherList.temp.min + "℃");
        des.setText(weatherList.weather.get(0).description);


        myTask = new MyAsynctask();

        myTask.execute("http://openweathermap.org/img/w/" + weatherList.weather.get(0).icon + ".png");
    }


        /**
     * Shows the progress UI and hides the weather info list view..
     */
    private void showProgress(final boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
        weatherIcon.setVisibility(show ? View.INVISIBLE : View.VISIBLE);

    }


    class MyAsynctask extends AsyncTask<String, Integer, Bitmap> {
        Bitmap bm = null;


        @Override
        protected Bitmap doInBackground(String... params) {
            HttpClient hc = new DefaultHttpClient();
            HttpGet hg = new HttpGet(params[0]);
            int ch = -1;
            byte[] buffer = new byte[128];
            String s = null;
            InputStream is = null;
            try {
                HttpResponse hr = hc.execute(hg);
                HttpEntity entity = hr.getEntity();
                long length = entity.getContentLength();
                is = entity.getContent();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                if (length > 0) {
                    while ((ch = is.read(buffer)) != -1) {
                        baos.write(buffer, 0, ch);
                    }
                    bm = BitmapFactory.decodeByteArray(baos.toByteArray(), 0,
                            baos.toByteArray().length);
                    is.close();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            return bm;
        }


        @Override
        protected void onCancelled() {
            super.onCancelled();
        }


        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            if (result != null) {
                showProgress(false);
                weatherIcon.setImageBitmap(bm);


            } else {
            }


        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weatherIcon.setImageBitmap(null);
            showProgress(true);
        }

    }


}
