package edu.uiowa.cs.multisense.ema;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.Random;

import edu.uiowa.cs.multisense.MultiSenseConstants;
import edu.uiowa.cs.multisense.fileio.ReadConfigFile;

/**
 * Created by Syed Shabih Hasan on 9/28/15.
 */
public class SetMultiSenseAlarms {

    private Context context;

    public SetMultiSenseAlarms(Context ipContext){
        this.context = ipContext;
    }


    public void setAlarm(long toStart, boolean isAudio, String currentDateTime){
        Intent intent;
        PendingIntent pendingIntent;
        AlarmManager alarmManager;

        intent = new Intent(this.context, MultiSenseAlarmManager.class);
        if(isAudio){
            intent.setAction("StartAudio");
        }else{
            intent.setAction("StartActivity");
            intent.putExtra("CurrentDT", currentDateTime);
        }

        pendingIntent = PendingIntent.getBroadcast(this.context, 0, intent, 0);
        alarmManager = (AlarmManager) this.context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + toStart,
                pendingIntent);

        if(isAudio){
            Log.d("MS:", "Audio alarm set to start in " + toStart/60000 + "mins");
        }else{
            Log.d("MS:", "Survey alarm set to start in " + toStart/60000 + "mins");
        }
    }

    public int getNextSurveyTime(boolean forAudio){
        ReadConfigFile readConfigFile = new ReadConfigFile(this.context);
        String[] allConfig = readConfigFile.readConfigData().split("\\n");

        int duration = Integer.parseInt(allConfig[MultiSenseConstants.SURVEY_INTERVAL]);
        int randInterval = Integer.parseInt(allConfig[MultiSenseConstants.RANDOM_INTERVAL]);
        Random randomIntGenerator = new Random((System.currentTimeMillis()%10)+1);
        if(0 != randInterval){
            randInterval = randomIntGenerator.nextInt(randInterval);
        }
        int surveyStartPoint = duration + randInterval;

        if(forAudio){
            return (surveyStartPoint - 1);
        }
        else{
            return surveyStartPoint;
        }
    }
}
