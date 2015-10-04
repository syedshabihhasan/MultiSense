package edu.uiowa.cs.multisense;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.Calendar;

import edu.uiowa.cs.multisense.power.CPULock;
import edu.uiowa.cs.multisense.sensors.RecordAudioEMA;
import edu.uiowa.cs.multisense.ui.CreateMainScreenLayout;

public class MultiSense extends AppCompatActivity {

    private Context context;
    private Intent intent;
    private CreateMainScreenLayout createMainScreenLayout;
    private String currentTimeDate = null;

    //TODO: power locks

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_sense);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        boolean isAlarm;
        if(getIntent().getAction().equals("SurveyAlarm")){
            Log.d("MS:", "Activity started by alarm");
            currentTimeDate = getIntent().getStringExtra("CurrentDT");
            isAlarm = true;
            Log.d("MS:", "Current date and time from intent: "+currentTimeDate);
        }else{
            Log.d("MS:", "Activity started by normal invocation");
            isAlarm = false;
            if(RecordAudioEMA.serviceRunning){
                Toast.makeText(this, "Cannot open app", Toast.LENGTH_SHORT).show();
                finish();
            }else{
                Log.d("MS:", "No ongoing recording");
                if(CPULock.lockAcquisitionStatus()){
                    Log.d("MS:", "CPU lock is still acquired, releasing lock");
                    int lockReleased = CPULock.releaseLock(CPULock.getLockAcquirer());
                    if(MultiSenseConstants.LOCK_NULL == lockReleased
                            || MultiSenseConstants.LOCK_RELEASED == lockReleased){
                        Log.d("MS:", "CPU lock released");
                    }else{
                        Log.d("MS:", "CPU lock acquired by someone else, not released");
                    }
                }
            }
        }
        initVals();
        createMainScreenLayout.constructMainScreen(isAlarm);
    }

    private void initVals(){
        context = this;
        if(null == currentTimeDate){
            Calendar calendar = Calendar.getInstance();
            currentTimeDate = ""+
                    calendar.get(Calendar.YEAR)+"_"+
                    (calendar.get(Calendar.MONTH)+1)+"_"+
                    calendar.get(Calendar.DAY_OF_MONTH)+"_"+
                    calendar.get(Calendar.HOUR_OF_DAY)+"_"+
                    calendar.get(Calendar.MINUTE)+"_"+
                    calendar.get(Calendar.SECOND)+"_"+
                    calendar.get(Calendar.MILLISECOND);
        }
        createMainScreenLayout = new CreateMainScreenLayout(context, currentTimeDate);
        intent = new Intent(this, RecordAudioEMA.class);
    }

    @Override
    public void onBackPressed(){
        Toast.makeText(context, "Button Disabled", Toast.LENGTH_SHORT).show();
    }
}
