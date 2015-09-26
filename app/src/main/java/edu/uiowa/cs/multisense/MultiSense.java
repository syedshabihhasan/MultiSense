package edu.uiowa.cs.multisense;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import edu.uiowa.cs.multisense.sensors.RecordAudioEMA;
import edu.uiowa.cs.multisense.ui.CreateSettingsLayout;

public class MultiSense extends AppCompatActivity {

    private Context context;
    private Intent intent;
    private CreateSettingsLayout createSettingsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_sense);
        initVals();
        startService(intent);
        Toast.makeText(context, "Starting service", Toast.LENGTH_SHORT).show();
        createSettingsLayout.constructSettingsScreen();
    }

    private void initVals(){
        context = this;
        createSettingsLayout = new CreateSettingsLayout(context);
        intent = new Intent(this, RecordAudioEMA.class);
    }


}
