package edu.uiowa.cs.multisense.ema;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import edu.uiowa.cs.multisense.MultiSense;

/**
 * Created by Syed Shabih Hasan on 9/25/15.
 *
 * This class checks the type of alarm received. If the alarm is for starting the recording, then
 * the recording starts and an alarm for starting the survey is set.
 * If the alarm is for starting the survey, the survey is started and the next alarm for the
 * recording is set.
 */
public class MultiSenseAlarmManager extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getBooleanExtra("Activity", false)){
            intent = new Intent(context, MultiSense.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    public void setAudioAlarm(){

    }

    public void setSurveyAlarm(){

    }
}
