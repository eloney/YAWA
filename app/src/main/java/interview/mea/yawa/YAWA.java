package interview.mea.yawa;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


import java.util.ArrayList;
import java.util.List;


public class YAWA extends Activity implements View.OnClickListener {

    private List<String> dayList = new ArrayList<String>();
    private int dayCountMax = 16;
    private int defaultDays = 7;
    private EditText cityNameEditText;
    private Spinner daySpinner;
    private ArrayAdapter<String> dayAdapter;
    private Button checkWeatherButton;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yawa);


        //initialize dayList
        for (int i = 1;i <= dayCountMax; i++){
            if (i == 1){
                dayList.add(i + "day");
            } else {
                dayList.add(i + "days");
            }
        }

        cityNameEditText = (EditText)findViewById(R.id.EditText_cityName);
        daySpinner = (Spinner)findViewById(R.id.Spinner_Days);
        checkWeatherButton = (Button)findViewById(R.id.Button_CheckWeather);


        dayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,dayList);
        daySpinner.setAdapter(dayAdapter);
        daySpinner.setSelection(defaultDays-1,true);



        findViewById(R.id.Activity_YAWA_main).setOnClickListener(this);
        checkWeatherButton.setOnClickListener(checkWeather);

    }

    private View.OnClickListener checkWeather = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String cityName = cityNameEditText.getText().toString();
            if(!checkInternetAccess()){
                new AlertDialog.Builder(YAWA.this).setMessage("Please check your internet connection.").setPositiveButton("OK",null).show();
            }
            else if (cityName.equals("")){
                new AlertDialog.Builder(YAWA.this).setMessage("Please type in a city name.").setPositiveButton("OK",null).show();
            }
            else{
                Intent intent = new Intent();
                intent.setClass(YAWA.this, WeatherInfo.class);
                Bundle bundle = new Bundle();
                bundle.putString("KEY_CITYNAME", cityNameEditText.getText().toString());
                bundle.putInt("KEY_DAYS", daySpinner.getSelectedItemPosition() + 1);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_yawa, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

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
        if (wifi | internet) {
            return true;
        } else {
            return false;
        }
    }
}
