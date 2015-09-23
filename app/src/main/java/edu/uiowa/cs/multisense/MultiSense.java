package edu.uiowa.cs.multisense;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import edu.uiowa.cs.multisense.ema.MoveQuestion;
import edu.uiowa.cs.multisense.ema.ResponseStore;
import edu.uiowa.cs.multisense.fileio.ReadFromFile;
import edu.uiowa.cs.multisense.sensors.RecordAudioEMA;

public class MultiSense extends AppCompatActivity {

    private Context context;
    private CreateLayout createLayout;
    private ReadFromFile readFromFile;
    private ResponseStore responseStore;
    private HashMap<Integer, int[]> logicMap;
    private MoveQuestion moveQuestion;
    private ArrayList<String[]> qDB;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_sense);
        initVals();
        startService(intent);
        Toast.makeText(context, "Starting service", Toast.LENGTH_SHORT).show();
        createNextLayout(0);
    }

    private void initVals(){
        context = getApplicationContext();
        createLayout = new CreateLayout(context);
        readFromFile = new ReadFromFile(context);
        qDB = readFromFile.getQuestions();
        logicMap = readFromFile.getLogicMap();
        responseStore = new ResponseStore();
        moveQuestion = new MoveQuestion(logicMap);
        intent = new Intent(this, RecordAudioEMA.class);
    }

    private void createNextLayout(int qNo){
        LinearLayout ll;
        if(-1 == qNo){
            ArrayList<int[]> allResponses = responseStore.getAllResponses();
            int[] temp;
            for(int i=0; i<allResponses.size(); i++){
                temp = allResponses.get(i);
                Log.d("CNL", "Q: "+temp[0]+", A: "+temp[1]);
            }
            ll = createLayout.createFinalLayout();
            ll.addView(createFinish());
        }else{
            ll = createLayout.createLayoutOnInput(qDB.get(qNo));
            Button nextButton = createActionButton(true, qNo);
            ll.addView(nextButton);
        }
        ll.setBackgroundColor(Color.rgb(153, 204, 255));
        setContentView(ll);
    }

    private Button createActionButton(boolean isNext, final int qNo){
        Button actionButton = new Button(context);
        actionButton.setBackgroundColor(Color.rgb(102, 178, 255));
        actionButton.setTextSize(32);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(20,20,20,20);
        actionButton.setLayoutParams(params);
                actionButton.setText(isNext ? "Next" : "Back");
        if(isNext){
            actionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int resp = createLayout.getFinalResponse();
                    if(-1 != resp){
                        responseStore.removeAndPush(qNo, resp);
                        int nextQ = moveQuestion.getNextQuestion(qNo+1, qDB, responseStore);
                        createNextLayout(nextQ);
                    }else{
                        Toast.makeText(context, "Please select an option",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        return actionButton;
    }

    private Button createFinish(){
        Button finishButton = new Button(context);
        finishButton.setText("Finish");
        finishButton.setTextSize(32);
        finishButton.setBackgroundColor(Color.RED);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(intent);
                Toast.makeText(context, "Stopping service", Toast.LENGTH_LONG).show();
                finish();
            }
        });

        return finishButton;
    }

}
