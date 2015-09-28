package edu.uiowa.cs.multisense.ui;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import edu.uiowa.cs.multisense.MultiSense;
import edu.uiowa.cs.multisense.ema.MultiSenseAlarmManager;

/**
 * Created by Syed Shabih Hasan on 9/25/15.
 */
public class CreateMainScreenLayout {

    private Context context;
    private Activity multiSenseActivity;
    private CreateSurveyLayout createSurveyLayout;
    private final CreateSettingsScreenLayout createSettingsScreenLayout;

    public CreateMainScreenLayout(Context ipContext){
        this.context = ipContext;
        this.multiSenseActivity = (Activity) this.context;
        this.createSurveyLayout = new CreateSurveyLayout(this.context);
        this.createSettingsScreenLayout = new CreateSettingsScreenLayout(this.context, this);
    }

    public void constructMainScreen(){
        LinearLayout settingsLayout = new LinearLayout(this.context);
        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        settingsLayout.setOrientation(LinearLayout.VERTICAL);
        settingsLayout.setLayoutParams(linearLayoutParams);

        Button startSurvey = createButton("Start Survey", 24, this.context);
        startSurvey = startSurveyButtonLogic(this.createSurveyLayout, startSurvey);
        /*
        * TODO: when start survey is clicked, a method in createSurveyLayout should be called that
        * initializes the layout process, the control essentially goes to the CreateSurveyLayout
        * class
        * */

        Button snoozeSurvey = createButton("Snooze", 24, this.context);
        snoozeSurvey = setGenericLogic(snoozeSurvey, this.context);

        Button vibrationMode = createButton("Ring/Vibrate", 24, this.context);
        vibrationMode = setGenericLogic(vibrationMode, this.context);

        Button internalSettings = createButton("Settings", 24, this.context);
        internalSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createSettingsScreenLayout.constructSettingsLayout();
            }
        });
//        internalSettings.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setAlarm();
//            }
//        });
//        internalSettings = setGenericLogic(internalSettings, this.context);

        Button exitButton = createFinish(this.context, this.multiSenseActivity);

        settingsLayout.addView(startSurvey);
        settingsLayout.addView(snoozeSurvey);
        settingsLayout.addView(vibrationMode);
        settingsLayout.addView(internalSettings);
        settingsLayout.addView(exitButton);

        this.multiSenseActivity.setContentView(settingsLayout);
    }


    private Button createButton(String toSet, float fontSize, Context context) {
        Button button = new Button(context);

        button.setText(toSet);
        button.setTextSize(fontSize);
        button.setBackgroundColor(Color.rgb(102, 178, 255));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(10, 5, 10, 5);
        button.setLayoutParams(params);

        return button;
    }

    private Button setGenericLogic(final Button ipButton,final Context context){
        ipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, ipButton.getText().toString(), Toast.LENGTH_LONG).show();
            }
        });
        return ipButton;
    }

    private Button createFinish(Context context, final Activity multiSenseActivity){
        Button finishButton = new Button(context);
        finishButton.setText("Finish");
        finishButton.setTextSize(32);
        finishButton.setBackgroundColor(Color.RED);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                multiSenseActivity.finish();
            }
        });
        return finishButton;
    }

    private Button startSurveyButtonLogic(final CreateSurveyLayout createSurveyLayout, Button ipButton){
        ipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createSurveyLayout.createNextLayout(0);
            }
        });
        return ipButton;
    }

//    private void setAlarm(){
//        Intent intent = new Intent(this.context, MultiSenseAlarmManager.class);
//        intent.putExtra("Activity", true);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(
//                this.multiSenseActivity.getApplicationContext(), 0, intent, 0);
//        AlarmManager alarmManager = (AlarmManager) this.context.getSystemService(
//                                        Context.ALARM_SERVICE);
//        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (60 * 1000),
//                pendingIntent);
//        Toast.makeText(this.context, "Alarm set for 1 minute", Toast.LENGTH_SHORT).show();
//    }

}
