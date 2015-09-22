package edu.uiowa.cs.multisense;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 *
 */
public class CreateLayout {
    private Context context;

    protected CreateLayout(Context context){
        this.context = context;
        //TODO: create recording class and initalize
    }

    /**
     * Creates a linear layout based on the string input
     * @param qAString : a string array, first string is the question text, the remaining are the
     *                 answer texts
     * @param qNo : integer representing the question number
     * @return LinearLayout with the appropriate questions and answers
     * */
    protected LinearLayout createLayoutOnInput(String[] qAString, int qNo){
        LinearLayout linearLayout = new LinearLayout(this.context);

        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(linearLayoutParams);

        final int n = qAString.length;

        // set the question text
        TextView questionText = createTextView(qAString[0], (float) 32, Color.BLACK, this.context);
        linearLayout.addView(questionText);

        // create the option button and set the logic
        Button[] optionButton = new Button[n-1];
        for(int i=0; i<n-1; i++){
            optionButton[i] = createButton(qAString[i + 1], 32, this.context);
            optionButton[i] = setButtonLogic(optionButton[i], optionButton, i);
            linearLayout.addView(optionButton[i]);
        }

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
        button.setBackgroundColor(Color.DKGRAY);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(2, 5, 2, 5);
        button.setLayoutParams(params);

        return button;
    }

    private Button setButtonLogic(final Button buttonToUse, final Button[] allButtons,
                                  final int idx){
        buttonToUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonToUse.setBackgroundColor(Color.LTGRAY);
                for(int i=0; i<allButtons.length; i++){
                    if(idx != i){
                        allButtons[i].setBackgroundColor(Color.DKGRAY);
                    }
                }
            }
        });
        return buttonToUse;
    }
}
