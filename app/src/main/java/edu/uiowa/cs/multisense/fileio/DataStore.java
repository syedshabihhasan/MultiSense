package edu.uiowa.cs.multisense.fileio;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import edu.uiowa.cs.multisense.MultiSenseConstants;

/**
 * Created by Syed Shabih Hasan on 9/22/15.
 *
 * This class basically acts as a parking space for the data
 *
 */
public class DataStore {

    private ArrayList<int[]> surveyResp;
    private String timeStamp;
    private String allResponses;
    private int DataType;

    public DataStore(ArrayList<int[]> surveyResp, String timeStamp){
        this.surveyResp = surveyResp;
        this.timeStamp = timeStamp;
        this.DataType = MultiSenseConstants.SURVEY_TYPE;
        this.allResponses = "";
    }

    public void WriteSurveyToFile(boolean timeOut){
        Log.d("MS:", "Writing survey to file");
        int[] temp;
        for(int i=0; i< this.surveyResp.size(); i++){
            temp = this.surveyResp.get(i);
            this.allResponses += "" + temp[0] + ", " + temp[1] + "\n";
        }
        if(timeOut){
            this.allResponses += "TIMEOUT";
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(
                    MultiSenseConstants.FILE_LOCATION + this.timeStamp + ".survey", false);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            outputStreamWriter.write(this.allResponses);
            outputStreamWriter.close();
            fileOutputStream.close();
            Log.d("MS:", "Written survey to file");
        }catch(Exception e){
            Log.d("MS:", "An error occurred while writing survey to file");
            e.printStackTrace();
        }

    }


}
