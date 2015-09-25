package edu.uiowa.cs.multisense.ui;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Arrays;


/**
 *
 */
public class CreateLayout {
    private static Context context;
    private boolean[] pressedResponse;

    public CreateLayout(Context context){
        this.context = context;
    }

    /**
     * Creates a linear layout based on the string input
     * @param qAString : a string array, first string is the question text, the remaining are the
     *                 answer texts
     * @return LinearLayout with the appropriate questions and answers
     * */
    public LinearLayout createLayoutOnInput(String[] qAString){
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
            optionButton[i] = setButtonLogic(optionButton[i], optionButton, i);
            linearLayout.addView(optionButton[i]);
        }

        return linearLayout;
    }

    public LinearLayout createFinalLayout(){
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

    private Button setButtonLogic(final Button buttonToUse, final Button[] allButtons,
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

    public int getFinalResponse(){
        int toReturn = -1;
        for(int i=0;i < pressedResponse.length; i++){
            if(pressedResponse[i]){
                return i;
            }
        }
        return toReturn;
    }
}
