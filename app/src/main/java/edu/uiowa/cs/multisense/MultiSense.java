package edu.uiowa.cs.multisense;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import edu.uiowa.cs.multisense.ema.MoveQuestion;
import edu.uiowa.cs.multisense.ema.ResponseStore;
import edu.uiowa.cs.multisense.fileio.ReadFromFile;

public class MultiSense extends AppCompatActivity {

    private Context context;
    private CreateLayout createLayout;
    private ReadFromFile readFromFile;
    private ResponseStore responseStore;
    private HashMap<Integer, Integer[]> logicMap;
    private MoveQuestion moveQuestion;
    private ArrayList<String[]> qDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_sense);
        initVals();
        createNextLayout(0);
    }

    private void initVals(){
        context = getApplicationContext();
        logicMap = new HashMap<>();
        createLayout = new CreateLayout(context);
        readFromFile = new ReadFromFile(context);
        qDB = readFromFile.getQuestions();
        responseStore = new ResponseStore();
        moveQuestion = new MoveQuestion(logicMap);
    }

    private void createNextLayout(int qNo){
        if(-1 == qNo){
            return;
        }
        LinearLayout ll = createLayout.createLayoutOnInput(qDB.get(qNo));
        Button nextButton = createActionButton(true, qNo);
        ll.addView(nextButton);
        setContentView(ll);

    }

    private Button createActionButton(boolean isNext, final int qNo){
        Button actionButton = new Button(context);
        actionButton.setText(isNext? "Next": "Back");
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
                        Toast.makeText(context, (CharSequence) "Please select an option",
                                Toast.LENGTH_SHORT);
                    }
                }
            });
        }
        return actionButton;
    }

}
