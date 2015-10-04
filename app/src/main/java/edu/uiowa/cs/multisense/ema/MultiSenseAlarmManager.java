package edu.uiowa.cs.multisense.ema;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

import edu.uiowa.cs.multisense.MultiSense;
import edu.uiowa.cs.multisense.power.CPULock;
import edu.uiowa.cs.multisense.sensors.RecordAudioEMA;

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
        Log.d("MS:", "Broadcast received");
        CPULock.context  = context;
        SetMultiSenseAlarms setMultiSenseAlarms;
        CheckAlarmConditions checkAlarmConditions = new CheckAlarmConditions(context);
        int res = checkAlarmConditions.IsAlarmLegal(System.currentTimeMillis());
        Log.d("MS:", "Alarm conditions check: "+res);
        setMultiSenseAlarms = new SetMultiSenseAlarms(context);
        if(1 == res){
            if(intent.getAction().equals("StartActivity")){
                Log.d("MS:", "Has StartActivity extra");
                String currentDT = intent.getStringExtra("CurrentDT");

                intent = new Intent(context, MultiSense.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setAction("SurveyAlarm");
                intent.putExtra("CurrentDT", currentDT);

                //before starting survey, set the alarm for the next audio
                int surveyRand = setMultiSenseAlarms.getNextSurveyTime(true);
                setMultiSenseAlarms.setAlarm(surveyRand*60*1000, true, "");

                context.startActivity(intent);
            }
            else if(intent.getAction().equals("StartAudio")){
                Log.d("MS:", "Has StartAudio extra");
                //before starting the audio recording, set the alarm for the survey

                Log.d("MS:", "Get CPU lock for recording");
                CPULock.acquireCPULock("AudioAlarm");
                Log.d("MS:", "Acquired CPU lock");

                String currentDT = getCurrentDateTime();
                intent = new Intent(context, RecordAudioEMA.class);
                intent.putExtra("CurrentDT", currentDT);

                //TODO: do something here about the 1
                setMultiSenseAlarms.setAlarm(1 * 60 * 1000, false, currentDT);

                context.startService(intent);
            }
        }else if(0 == res){
            int surveyRand = setMultiSenseAlarms.getNextSurveyTime(true);
            setMultiSenseAlarms.setAlarm(surveyRand*60*1000, true, "");

        }else{
            Log.d("MS:", "No more alarms, bye!");
            // the end date and time have passed, do nothing
        }
    }

    public String getCurrentDateTime(){
        Calendar calendar = Calendar.getInstance();
        String currentTimeDate = ""+
                calendar.get(Calendar.YEAR)+"_"+
                (calendar.get(Calendar.MONTH)+1)+"_"+
                calendar.get(Calendar.DAY_OF_MONTH)+"_"+
                calendar.get(Calendar.HOUR_OF_DAY)+"_"+
                calendar.get(Calendar.MINUTE)+"_"+
                calendar.get(Calendar.SECOND)+"_"+
                calendar.get(Calendar.MILLISECOND);
        return currentTimeDate;
    }
}
