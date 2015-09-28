package edu.uiowa.cs.multisense.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;

import edu.uiowa.cs.multisense.MultiSense;
import edu.uiowa.cs.multisense.MultiSenseConstants;
import edu.uiowa.cs.multisense.fileio.WriteConfigFile;

/**
 * Created by Syed Shabih Hasan on 9/26/15.
 */
public class CreateSettingsScreenLayout {
    private final Context context;
    private final Activity multisense;
    private final WriteConfigFile writeConfigFile;
    private final CreateMainScreenLayout createMainScreenLayout;

    public CreateSettingsScreenLayout(Context ipContext,
                                      CreateMainScreenLayout cMSL){
        this.context = ipContext;
        multisense = (Activity) this.context;
        writeConfigFile = new WriteConfigFile(this.context);
        createMainScreenLayout = cMSL;
    }

    protected void constructSettingsLayout(){
        LinearLayout settingsLayout = getSubjectInfo();
        multisense.setContentView(settingsLayout);
    }

    private LinearLayout getSubjectInfo(){
        LinearLayout patientInfo = new LinearLayout(context);
        patientInfo = setCorrectParams(patientInfo);

        final EditText patientID = new EditText(context);
        patientID.setTextSize(MultiSenseConstants.EDITTEXT_FONTSIZE);
        patientID.setInputType(InputType.TYPE_CLASS_NUMBER);
        patientID.setHint("Enter Subject ID");

        final EditText deviceID = new EditText(context);
        deviceID.setTextSize(MultiSenseConstants.EDITTEXT_FONTSIZE);
        deviceID.setInputType(InputType.TYPE_CLASS_NUMBER);
        deviceID.setHint("Enter E4 device ID");

        final EditText surveyGap = new EditText(context);
        surveyGap.setTextSize(MultiSenseConstants.EDITTEXT_FONTSIZE);
        surveyGap.setInputType(InputType.TYPE_CLASS_NUMBER);
        surveyGap.setHint("Gap between surveys (minutes)");

        final EditText randomPeriod = new EditText(context);
        randomPeriod.setTextSize(MultiSenseConstants.EDITTEXT_FONTSIZE);
        randomPeriod.setInputType(InputType.TYPE_CLASS_NUMBER);
        randomPeriod.setHint("Enter random period (minutes)");

        Button nextButton = new Button(context);
        nextButton.setTextSize(MultiSenseConstants.BUTTON_FONTSIZE);
        nextButton.setText("Next");
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeConfigFile.pushConfig("Patient ID", patientID.getText().toString());
                writeConfigFile.pushConfig("Device ID", deviceID.getText().toString());
                writeConfigFile.pushConfig("Time Gap", surveyGap.getText().toString());
                writeConfigFile.pushConfig("Random Gap", randomPeriod.getText().toString());

                LinearLayout nextLayout = getStartEndDate(true);
                multisense.setContentView(nextLayout);
            }
        });
        patientInfo.addView(patientID);
        patientInfo.addView(deviceID);
        patientInfo.addView(surveyGap);
        patientInfo.addView(randomPeriod);
        patientInfo.addView(nextButton);
        return patientInfo;
    }

    private LinearLayout getStartEndDate(final boolean isStart){
        LinearLayout dateLayout = new LinearLayout(context);
        dateLayout = setCorrectParams(dateLayout);

        TextView instructionDate = new TextView(context);
        instructionDate.setTextSize(MultiSenseConstants.TEXTVIEW_FONTSIZE);
        if(isStart){
            instructionDate.setText("Select start date");
        }else{
            instructionDate.setText("Select end date");
        }

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        final DatePicker datePicker = new DatePicker(context);
        datePicker.init(year, month, day, null);
        datePicker.setSpinnersShown(true);

        final Button nextButton = new Button(context);
        nextButton.setTextSize(MultiSenseConstants.BUTTON_FONTSIZE);
        nextButton.setText("Next");
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dateSelected = "" + datePicker.getDayOfMonth() + " " +
                        MultiSenseConstants.MONTH_DICT[datePicker.getMonth()] + " " +
                        datePicker.getYear();
                if (isStart) {
                    writeConfigFile.pushConfig("Start Date", dateSelected);
                } else {
                    writeConfigFile.pushConfig("End Date", dateSelected);
                }
                LinearLayout nextLayout = isStart ? getStartEndDate(false) : getStartEndTime(true);
                multisense.setContentView(nextLayout);
            }
        });
        dateLayout.addView(instructionDate);
        dateLayout.addView(datePicker);
        dateLayout.addView(nextButton);
        return dateLayout;
    }

    private LinearLayout getStartEndTime(final boolean isStart){
        LinearLayout startEndTimeLayout = new LinearLayout(context);
        startEndTimeLayout = setCorrectParams(startEndTimeLayout);

        TextView instructionTime = new TextView(context);
        instructionTime.setTextSize(MultiSenseConstants.TEXTVIEW_FONTSIZE);
        if(isStart){
            instructionTime.setText("Select start time");
        }else{
            instructionTime.setText("Select end time");
        }

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        final TimePicker timePicker = new TimePicker(context);
        timePicker.setIs24HourView(true);
        if(Build.VERSION_CODES.M > Build.VERSION.SDK_INT){
            timePicker.setCurrentHour(hour);
            timePicker.setCurrentMinute(minute);
        }else{
            timePicker.setHour(hour);
            timePicker.setMinute(minute);
        }

        Button nextButton = new Button(context);
        nextButton.setTextSize(MultiSenseConstants.BUTTON_FONTSIZE);
        nextButton.setText("Next");
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String timeSelected;
                if (Build.VERSION_CODES.M > Build.VERSION.SDK_INT) {
                    timeSelected = "" + timePicker.getCurrentHour() +
                            " " + timePicker.getCurrentMinute();
                } else {
                    timeSelected = "" + timePicker.getHour() + " " + timePicker.getMinute();
                }
                if (isStart) {
                    writeConfigFile.pushConfig("Start Time", timeSelected);
                } else {
                    writeConfigFile.pushConfig("End Time", timeSelected);
                }
                LinearLayout nextLayout = isStart ? getStartEndTime(false) : finalScreen();
                multisense.setContentView(nextLayout);
            }
        });

        startEndTimeLayout.addView(instructionTime);
        startEndTimeLayout.addView(timePicker);
        startEndTimeLayout.addView(nextButton);
        return startEndTimeLayout;
    }

    private LinearLayout finalScreen(){
        LinearLayout finalScreenLayout = new LinearLayout(context);
        finalScreenLayout = setCorrectParams(finalScreenLayout);

        TextView configSettings = new TextView(context);
        configSettings.setTextSize(MultiSenseConstants.TEXTVIEW_FONTSIZE);
        ArrayList<String[]> allEntries = writeConfigFile.getConfigData();
        String toDisplay = "";
        String[] temp;
        for(int i = 0; i < allEntries.size(); i++){
            temp = allEntries.get(i);
            toDisplay += temp[0] + ":" + temp[1] + "\n";
        }
        configSettings.setText(toDisplay);

        Button saveSettings = new Button(context);
        saveSettings.setTextSize(MultiSenseConstants.BUTTON_FONTSIZE);
        saveSettings.setText("Save Settings");
        saveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean success = writeConfigFile.saveConfig();
                if(success) {
                    Log.d("MS:", "saved config");
                } else{
                    Log.d("MS:", "not saved config");
                }
                createMainScreenLayout.constructMainScreen();
            }
        });

        Button redoSettings = new Button(context);
        redoSettings.setTextSize(MultiSenseConstants.BUTTON_FONTSIZE);
        redoSettings.setText("Redo Settings");
        redoSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeConfigFile.clearCurrentConfig();
                constructSettingsLayout();
            }
        });

        finalScreenLayout.addView(configSettings);
        finalScreenLayout.addView(saveSettings);
        finalScreenLayout.addView(redoSettings);
        return finalScreenLayout;
    }

    private LinearLayout setCorrectParams(LinearLayout ipLayout){
        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        ipLayout.setOrientation(LinearLayout.VERTICAL);
        ipLayout.setLayoutParams(linearLayoutParams);
        return ipLayout;
    }
}
