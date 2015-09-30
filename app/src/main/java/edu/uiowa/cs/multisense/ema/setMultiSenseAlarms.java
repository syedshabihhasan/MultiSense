package edu.uiowa.cs.multisense.ema;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Syed Shabih Hasan on 9/28/15.
 */
public class setMultiSenseAlarms {

    private Context context;
    private Activity multisense;

    public setMultiSenseAlarms(Context ipContext){
        this.context = ipContext;
        this.multisense = (Activity) this.context;
    }


    public void setAlarm(long toStart, boolean isAudio){
        Intent intent;
        PendingIntent pendingIntent;
        AlarmManager alarmManager;

        intent = new Intent(this.context, MultiSenseAlarmManager.class);
        if(isAudio){
            intent.putExtra("AudioAlarm", true);
        }else{
            intent.putExtra("SurveyAlarm", true);
        }
        pendingIntent = PendingIntent.getBroadcast(this.multisense.getApplicationContext(),
                0, intent, 0);
        alarmManager = (AlarmManager) this.context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + toStart,
                pendingIntent);

        if(isAudio){
            Log.d("MS:", "Audio alarm set");
        }else{
            Log.d("MS:", "Survey alarm set");
        }
    }
}
