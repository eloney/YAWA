package interview.mea.yawa;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
import java.util.Locale;

import interview.mea.yawa.weather.WeatherJSONEntity;


public class WeatherDetail extends Activity {

    private static final String TAG = WeatherDetail.class.getSimpleName();

    private TextView cityName;
    private TextView weatherDate;
    private ProgressBar progressBar;
    private ImageView weatherIcon;
    private TextView noImage;
    private TextView tempLabel;
    private TextView tempEve;
    private TextView tempMax;
    private TextView tempMin;
    private TextView des;

    private int day;
    private WeatherJSONEntity weatherResult;
    private WeatherJSONEntity.List weatherList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weatherdetail);
        findViews();
        extractFromBundle();
        initViews();
        downloadIcon();
    }

    private void findViews() {
        cityName = (TextView) findViewById(R.id.TextView_cityName);
        weatherDate = (TextView) findViewById(R.id.TextView_date);
        progressBar = (ProgressBar) findViewById(R.id.ProgressBar_weatherIcon);
        weatherIcon = (ImageView) findViewById(R.id.ImageView_weatherIcon);
        noImage = (TextView) findViewById(R.id.TextView_noImage);
        tempLabel = (TextView) findViewById(R.id.TextView_temperture);
        tempEve = (TextView) findViewById(R.id.TextView_tempEve);
        tempMax = (TextView) findViewById(R.id.TextView_tempMax);
        tempMin = (TextView) findViewById(R.id.TextView_tempMin);
        des = (TextView) findViewById(R.id.TextView_des);
    }

    private void extractFromBundle() {
        weatherResult = (WeatherJSONEntity) getIntent().getSerializableExtra(WeatherInfo.KEY_WEATHER_RESULT_SER);
        day = getIntent().getIntExtra(WeatherInfo.KEY_DAY, 0);
        weatherList = weatherResult.list.get(day);
    }

    private void initViews() {
        //get matching date
        Calendar currentDate = Calendar.getInstance();
        SimpleDateFormat myFmt = new SimpleDateFormat(getString(R.string.format_date), Locale.getDefault());
        currentDate.add(Calendar.DAY_OF_MONTH, day + 1);

        //setting the text
        cityName.setText(weatherResult.city.name + " " + getString(R.string.weather));
        weatherDate.setText(day == 0 ? getString(R.string.tmr) : myFmt.format(currentDate.getTime()));
        tempEve.setText(weatherList.temp.eve + getString(R.string.tempsymbol));
        tempMax.setText(getString(R.string.max) + " " + weatherList.temp.max + getString(R.string.tempsymbol));
        tempMin.setText(getString(R.string.min) + " " + weatherList.temp.min + getString(R.string.tempsymbol));
        des.setText(weatherList.weather.get(0).description);
    }

    /**
     * Shows the progress UI and hides the weather icon
     */
    private void showProgress(final boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
        weatherIcon.setVisibility(show ? View.INVISIBLE : View.VISIBLE);

    }

    private void downloadIcon() {
        DownloadIconTask downloadIcon = new DownloadIconTask();
        //Using substring() to fix a bug from http://openweathermap.org/ (It replies 04dd for broken clouds weather and 03dd for scattered clouds)
        String parameters = weatherList.weather.get(0).icon.substring(0,3);
        //example: "http://openweathermap.org/img/w/" + parameters + ".png";
        String url = getString(R.string.url_weathericon_basic) + parameters + getString(R.string.url_weathericon_append);
        downloadIcon.execute(url);
    }


    /**
     * AsyncTask to download weather icon
     */
    class DownloadIconTask extends AsyncTask<String, Integer, Bitmap> {
        Bitmap bm = null;

        @Override
        protected Bitmap doInBackground(String... params) {
            HttpClient hc = new DefaultHttpClient();
            HttpGet hg = new HttpGet(params[0]);
            int ch;
            byte[] buffer = new byte[128];
            InputStream is;
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
                Log.d(TAG, e.toString());
                e.printStackTrace();
            }
            return bm;
        }


        @Override
        protected void onCancelled() {
            super.onCancelled();
            showProgress(false);
        }


        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            if (result != null) {
                showProgress(false);
                weatherIcon.setImageBitmap(bm);
            } else {
                showProgress(false);
                noImage.setVisibility(View.VISIBLE);
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
