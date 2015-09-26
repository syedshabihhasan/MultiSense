package edu.uiowa.cs.multisense.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import edu.uiowa.cs.multisense.ema.MoveQuestion;
import edu.uiowa.cs.multisense.ema.ResponseStore;
import edu.uiowa.cs.multisense.fileio.ReadFromFile;


/**
 *
 */
public class CreateSurveyLayout {

    /*
    * TODO: the response store, logicMap, questions DB etc all have to initialized here
    * */
    private static Context context;
    private boolean[] pressedResponse;
    private static Activity multiSense;
    private ResponseStore responseStore;
    private HashMap<Integer, int[]> logicMap;
    private ReadFromFile readFromFile;
    private ArrayList<String[]> qDB;
    private MoveQuestion moveQuestion;

    public CreateSurveyLayout(Context ipContext){
        context = ipContext;
        multiSense = (Activity) context;

        // get the survey and the conditional structure
        this.readFromFile = new ReadFromFile(context);
        this.qDB = readFromFile.getQuestions();
        this.logicMap = readFromFile.getLogicMap();

        // initialize the question movement object
        moveQuestion = new MoveQuestion(this.logicMap);

        // initialize the response store
        responseStore = new ResponseStore();
    }

    /**
     * Creates a linear layout based on the string input
     * @param qAString : a string array, first string is the question text, the remaining are the
     *                 answer texts
     * @return LinearLayout with the appropriate questions and answers
     * */
    private LinearLayout createLayoutOnInput(String[] qAString){
        LinearLayout linearLayout = new LinearLayout(context);
        pressedResponse = new boolean[qAString.length - 1];
        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(linearLayoutParams);

        final int n = qAString.length;

        // set the question text
        TextView questionText = createTextView(qAString[0], (float) 24, Color.BLACK, context);
        linearLayout.addView(questionText);

        // create the option button and set the logic
        Button[] optionButton = new Button[n-1];
        for(int i=0; i<n-1; i++){
            optionButton[i] = createButton(qAString[i + 1], 24, context);
            optionButton[i] = setSurveyButtonLogic(optionButton[i], optionButton, i);
            linearLayout.addView(optionButton[i]);
        }

        return linearLayout;
    }

    private LinearLayout createFinalLayout(){
        LinearLayout linearLayout = new LinearLayout(context);
        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(linearLayoutParams);
        TextView textView = createTextView("Survey finished, press Finish to end", (float) 24,
                Color.GREEN, context);
        linearLayout.addView(textView);
        return linearLayout;
    }

    private TextView createTextView(String toSet, float fontSize, int textColor, Context context) {
        TextView textView = new TextView(context);

        textView.setText(toSet);
        textView.setTextSize(fontSize);
        textView.setTextColor(textColor);

        return textView;
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

    private Button setSurveyButtonLogic(final Button buttonToUse, final Button[] allButtons,
                                        final int idx){
        buttonToUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Arrays.fill(pressedResponse, false);
                pressedResponse[idx] = true;
                buttonToUse.setBackgroundColor(Color.rgb(0, 128, 255));
                for (int i = 0; i < allButtons.length; i++) {
                    if (idx != i) {
                        allButtons[i].setBackgroundColor(Color.rgb(102, 178, 255));
                    }
                }
            }
        });
        return buttonToUse;
    }

    private int getFinalResponse(){
        int toReturn = -1;
        for(int i=0;i < pressedResponse.length; i++){
            if(pressedResponse[i]){
                return i;
            }
        }
        return toReturn;
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
                    int resp = getFinalResponse();
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

    protected void createNextLayout(int qNo){
        LinearLayout ll;
        if(-1 == qNo){
            ArrayList<int[]> allResponses = this.responseStore.getAllResponses();
            int[] temp;
            for(int i=0; i<allResponses.size(); i++){
                temp = allResponses.get(i);
                Log.d("CNL", "Q: "+temp[0]+", A: "+temp[1]);
            }
            ll = createFinalLayout();
            ll.addView(createFinish());
        }else{
            ll = createLayoutOnInput(this.qDB.get(qNo));
            Button nextButton = createActionButton(true, qNo);
            ll.addView(nextButton);
        }
        ll.setBackgroundColor(Color.rgb(153, 204, 255));
        multiSense.setContentView(ll);
    }

    private Button createFinish(){
        Button finishButton = new Button(context);
        finishButton.setText("Finish");
        finishButton.setTextSize(32);
        finishButton.setBackgroundColor(Color.RED);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                multiSense.finish();
            }
        });
        return finishButton;
    }
}
