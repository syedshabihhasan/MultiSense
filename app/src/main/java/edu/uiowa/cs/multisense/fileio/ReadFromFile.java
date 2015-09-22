package edu.uiowa.cs.multisense.fileio;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Pattern;

import edu.uiowa.cs.multisense.R;

/**
 * Created by hasanshabih on 9/18/15.
 */
public class ReadFromFile {
    //TODO create method to read logic file
    Context context;

    public ReadFromFile(Context context){
        this.context = context;
    }

    /**
     * This method extracts the questions in the survey and returns them as an arraylist of string[]
     * */
    public ArrayList<String[]> getQuestions(){

        InputStream is = this.context.getResources().openRawResource(R.raw.surveyquestions);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        ArrayList<String> allInput = new ArrayList<>();
        String line;

        try {
            while (null != (line = br.readLine())) {
                allInput.add(line);
            }
        }catch(IOException e){
            Log.e("getQuestions", "IO Exception: " + e.toString());
        }
        ArrayList<String[]> qDB = seperateOutValues(allInput);
        return qDB;
    }

    /**
     * Read from the input arraylist and extract the questions, and options in an ArrayList of
     * String[]
     * */
    private ArrayList<String[]> seperateOutValues(ArrayList<String> allInput){
        ArrayList<String[]> qDB = new ArrayList<String[]>();
        for(int i=0; i<allInput.size(); i++){
            String temp = allInput.get(i);
            qDB.add(temp.split(Pattern.quote("//")));
        }
        return qDB;
    }
}
