package interview.mea.yawa;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


import java.util.ArrayList;
import java.util.List;


public class YAWA extends Activity implements View.OnClickListener {

    private final static int dayCountMax = 16;
    private final static int defaultDays = 7;

    public final static String KEY_CITYNAME = "KEY_CITYNAME";
    public final static String KEY_DAYS = "KEY_DAYS";

    private EditText cityNameEditText;
    private Spinner daySpinner;
    private Button checkWeatherButton;

    private List<String> dayList = new ArrayList<String>();
    private String cityName = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yawa);
        findViews();
        initDaySpinner();
        setListeners();
    }

    private View.OnClickListener checkWeather = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            cityName = cityNameEditText.getText().toString();
            if (initCheck()) {
                goToWeatherInfo();
            }
        }
    };

    //hide keyboard when click screen
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Activity_YAWA_main:
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                break;
        }

    }

    public Boolean checkInternetAccess() {
        ConnectivityManager con = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        boolean wifi = con.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
        boolean internet = con.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
        return wifi | internet;
    }

    private Boolean initCheck() {
        if (!checkInternetAccess()) {
            new AlertDialog.Builder(YAWA.this).setMessage(R.string.hint_checknetwork).setPositiveButton(R.string.button_ok, null).show();
            return false;
        } else if (cityName.equals("")) {
            new AlertDialog.Builder(YAWA.this).setMessage(R.string.hint_checkcityname).setPositiveButton(R.string.button_ok, null).show();
            return false;
        } else {
            return true;
        }
    }

    private void goToWeatherInfo() {
        Intent intent = new Intent();
        intent.setClass(YAWA.this, WeatherInfo.class);
        Bundle bundle = new Bundle();
        bundle.putString(KEY_CITYNAME, cityName);
        bundle.putInt(KEY_DAYS, daySpinner.getSelectedItemPosition() + 1);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * initialize the day spinner
     */
    private void initDaySpinner() {

        //init a day list
        for (int i = 1; i <= dayCountMax; i++) {
            if (i == 1) {
                dayList.add(i + getString(R.string.day_singular));
            } else {
                dayList.add(i + getString(R.string.day_plural));
            }
        }

        ArrayAdapter<String> dayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, dayList);
        daySpinner.setAdapter(dayAdapter);
        daySpinner.setSelection(defaultDays - 1, true);
    }

    private void findViews() {
        cityNameEditText = (EditText) findViewById(R.id.EditText_cityName);
        daySpinner = (Spinner) findViewById(R.id.Spinner_Days);
        checkWeatherButton = (Button) findViewById(R.id.Button_CheckWeather);
    }

    private void setListeners() {
        findViewById(R.id.Activity_YAWA_main).setOnClickListener(this);
        checkWeatherButton.setOnClickListener(checkWeather);
    }


}
