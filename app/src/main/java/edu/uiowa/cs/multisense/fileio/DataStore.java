package edu.uiowa.cs.multisense.fileio;

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
    private int DataType;


    public DataStore(ArrayList<int[]> surveyResp, String timeStamp){
        this.surveyResp = surveyResp;
        this.timeStamp = timeStamp;
        this.DataType = MultiSenseConstants.SURVEY_TYPE;
    }


}
