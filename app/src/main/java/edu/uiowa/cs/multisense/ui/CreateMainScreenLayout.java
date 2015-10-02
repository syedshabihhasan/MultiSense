package edu.uiowa.cs.multisense.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import edu.uiowa.cs.multisense.MultiSenseConstants;
import edu.uiowa.cs.multisense.fileio.ReadConfigFile;
import edu.uiowa.cs.multisense.sensors.RecordAudioEMA;

/**
 * Created by Syed Shabih Hasan on 9/25/15.
 */
public class CreateMainScreenLayout {

    private Context context;
    private Activity multiSenseActivity;
    private CreateSurveyLayout createSurveyLayout;
    private final CreateSettingsScreenLayout createSettingsScreenLayout;
    private String currentDateTime;

    private AudioManager audioManager;

    private Vibrator vibrator;

    private Timer mainScreenTimeoutTimer;
    private TimerTask mainScreenTimeoutTimerTask;

    public CreateMainScreenLayout(Context ipContext, String currentDateTime){
        this.context = ipContext;
        this.multiSenseActivity = (Activity) this.context;
        this.currentDateTime = currentDateTime;
        this.createSurveyLayout = new CreateSurveyLayout(this.context, this.currentDateTime);
        this.createSettingsScreenLayout = new CreateSettingsScreenLayout(this.context, this);
        this.audioManager = (AudioManager) this.context.getSystemService(
                Context.AUDIO_SERVICE);
        this.vibrator = (Vibrator) this.context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    public void constructMainScreen(boolean isAlarm){
        Log.d("MS:", "Main screen construction called, isAlarm:"+ (isAlarm ? "true": "false"));

        cancelTimeOuts();
        LinearLayout settingsLayout = new LinearLayout(this.context);
        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        settingsLayout.setOrientation(LinearLayout.VERTICAL);
        settingsLayout.setLayoutParams(linearLayoutParams);

        Button startSurvey = createButton("Start Survey", 24, this.context);
        if(doesConfigExist() && RecordAudioEMA.serviceRunning){
            startSurvey = startSurveyButtonLogic(this.createSurveyLayout, startSurvey);
        }else{
            startSurvey.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vibrator.vibrate(MultiSenseConstants.BUTTON_VIBRATE_DURATION);
                    Toast.makeText(context, "Cannot start survey, no configuration, or " +
                                    "not the right time",
                            Toast.LENGTH_LONG).show();
                }
            });
        }
        /*
        * TODO: when start survey is clicked, a method in createSurveyLayout should be called that
        * initializes the layout process, the control essentially goes to the CreateSurveyLayout
        * class
        * */

        Button snoozeSurvey = createButton("Snooze", 24, this.context);
        //TODO: implement snooze capability
        snoozeSurvey = setGenericLogic(snoozeSurvey, this.context);

//        Button vibrationMode = createButton("Ring/Vibrate", 24, this.context);
        //TODO implement vibration facility
//        vibrationMode = setGenericLogic(vibrationMode, this.context);
        LinearLayout vibrationMode = toggleButtonLayout("Vibration:", 24, this.context);

        Button internalSettings = createButton("Settings", 24, this.context);
        internalSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelTimeOuts();
                vibrator.vibrate(MultiSenseConstants.BUTTON_VIBRATE_DURATION);
                createSettingsScreenLayout.constructSettingsLayout();
            }
        });

        Button exitButton = createFinish(this.context, this.multiSenseActivity);

        settingsLayout.addView(startSurvey);
        settingsLayout.addView(snoozeSurvey);
        settingsLayout.addView(vibrationMode);
        settingsLayout.addView(internalSettings);
        settingsLayout.addView(exitButton);

        if(isAlarm){
            setTimeOuts();
        }

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

    private LinearLayout toggleButtonLayout(String toSet, float fontSize, Context context){
        LinearLayout toggleLayout = new LinearLayout(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(10, 5, 10, 5);
        toggleLayout.setOrientation(LinearLayout.HORIZONTAL);
        toggleLayout.setBackgroundColor(Color.rgb(102, 178, 255));
        toggleLayout.setLayoutParams(params);

        ToggleButton toggleButton = createToggleButton(24, context);
        toggleButton = setVibrationLogic(toggleButton);

        params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.weight = 1;
        TextView toggleText = new TextView(context);
        toggleText.setLayoutParams(params);
        toggleText.setText(toSet);
        toggleText.setTextSize(fontSize);
        toggleText.setBackgroundColor(Color.rgb(102, 178, 255));

        toggleLayout.addView(toggleText);
        toggleLayout.addView(toggleButton);
        return toggleLayout;
    }

    private ToggleButton createToggleButton(float fontSize, Context context){
        ToggleButton toggleButton = new ToggleButton(context);

        toggleButton.setTextSize(fontSize);
        toggleButton.setBackgroundColor(Color.rgb(102, 178, 255));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        params.setMargins(10, 5, 10, 5);
        params.weight = 1;
        toggleButton.setLayoutParams(params);
        if(AudioManager.RINGER_MODE_VIBRATE == this.audioManager.getRingerMode()){
            toggleButton.setChecked(true);
            toggleButton.setBackgroundColor(Color.GREEN);
        }else{
            toggleButton.setChecked(false);
            toggleButton.setBackgroundColor(Color.RED);
        }
        return toggleButton;
    }

    private Button setGenericLogic(final Button ipButton,final Context context){
        ipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(MultiSenseConstants.BUTTON_VIBRATE_DURATION);
                Toast.makeText(context, ipButton.getText().toString(), Toast.LENGTH_LONG).show();
            }
        });
        return ipButton;
    }

    private ToggleButton setVibrationLogic(final ToggleButton toggleButton){
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                    toggleButton.setBackgroundColor(Color.GREEN);
                    vibrator.vibrate(MultiSenseConstants.BUTTON_VIBRATE_DURATION);
                    Log.d("MS:", "Vibration: ON");
                } else {
                    audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                    toggleButton.setBackgroundColor(Color.RED);
                    Log.d("MS:", "Vibration: OFF");
                }
            }
        });
        return toggleButton;
    }

    private Button createFinish(Context context, final Activity multiSenseActivity){
        Button finishButton = new Button(context);
        finishButton.setText("Finish");
        finishButton.setTextSize(32);
        finishButton.setBackgroundColor(Color.RED);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelTimeOuts();
                vibrator.vibrate(MultiSenseConstants.BUTTON_VIBRATE_DURATION);
                multiSenseActivity.finish();
            }
        });
        return finishButton;
    }

    private Button startSurveyButtonLogic(final CreateSurveyLayout createSurveyLayout, Button ipButton) {
        ipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelTimeOuts();
                vibrator.vibrate(MultiSenseConstants.BUTTON_VIBRATE_DURATION);
                createSurveyLayout.createNextLayout(0, -1);
            }
        });
        return ipButton;
    }

    private boolean doesConfigExist(){
        File file = this.context.getFileStreamPath(MultiSenseConstants.CONFIG_FILENAME);
        return file.exists();
    }

    private void cancelTimeOuts(){
        Log.d("MS:", "Cancelled main screen timeout");
        if(null != this.mainScreenTimeoutTimer){
            this.mainScreenTimeoutTimer.purge();
            this.mainScreenTimeoutTimer.cancel();
            this.mainScreenTimeoutTimer = null;
            if(null != this.mainScreenTimeoutTimerTask){
                this.mainScreenTimeoutTimerTask = null;
            }
        }
    }

    private void setTimeOuts(){
        this.mainScreenTimeoutTimer = new Timer();
        this.mainScreenTimeoutTimerTask = new TimerTask() {
            @Override
            public void run() {
                Log.d("MS:", "Main screen timed out!");
                multiSenseActivity.finish();
            }
        };
        ReadConfigFile readConfigFile = new ReadConfigFile(context);
        String[] allResponses = readConfigFile.readConfigData().split("\\n");

        mainScreenTimeoutTimer.schedule(mainScreenTimeoutTimerTask,
                Integer.parseInt(allResponses[MultiSenseConstants.MAINSCREEN_TIMEOUT])*60*1000);

        Log.d("MS:", "timeout set for "+
                Integer.parseInt(allResponses[MultiSenseConstants.MAINSCREEN_TIMEOUT]) + "mins");
    }

}
