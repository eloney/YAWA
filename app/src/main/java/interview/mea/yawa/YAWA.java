package interview.mea.yawa;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class YAWA extends Activity implements View.OnClickListener {

    private List<String> dayList = new ArrayList<String>();
    private int dayCount = 16;
    private int defaultDays = 7;
    private EditText cityNameText;
    private Spinner daySpinner;
    private ArrayAdapter<String> dayAdapter;
    private Button checkWeatherButton;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yawa);


        //initialize dayList
        for (int i = 1;i <= dayCount; i++){
            if (i == 1){
                dayList.add(i + "day");
            } else {
                dayList.add(i + "days");
            }
        }

        cityNameText = (EditText)findViewById(R.id.EditText_cityName);
        daySpinner = (Spinner)findViewById(R.id.Spinner_Days);
        checkWeatherButton = (Button)findViewById(R.id.Button_CheckWeather);

        dayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,dayList);
        daySpinner.setAdapter(dayAdapter);
        daySpinner.setSelection(defaultDays-1);



        findViewById(R.id.Activity_YAWA_main).setOnClickListener(this);

    }


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
}
