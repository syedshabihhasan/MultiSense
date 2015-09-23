package edu.uiowa.cs.multisense.fileio;


import java.util.ArrayList;
import edu.uiowa.cs.multisense.MultiSenseConstants;

/**
 * Created by Syed Shabih Hasan on 9/22/15.
 */
public class DataStore {

    private ArrayList<short[]> audioData;
    private ArrayList<int[]> surveyResp;
    private long timeStamp;
    private int DataType;

    public DataStore(long timeStamp, ArrayList<short[]> audioData){
        this.audioData = audioData;
        this.timeStamp = timeStamp;
        this.DataType = MultiSenseConstants.AUDIO_TYPE;
    }

    public DataStore(ArrayList<int[]> surveyResp, long timeStamp){
        this.surveyResp = surveyResp;
        this.timeStamp = timeStamp;
        this.DataType = MultiSenseConstants.SURVEY_TYPE;
    }

    private byte[] short2byte(short[] shortData) {
        int shortArrsize = shortData.length;
        byte[] byteData = new byte[shortArrsize * 2];
        for (int i = 0; i < shortArrsize; i++) {
            byteData[i * 2] = (byte) (shortData[i] & 0x00FF);
            byteData[(i * 2) + 1] = (byte) (shortData[i] >> 8);
            shortData[i] = 0;
        }
        return byteData;

    }

    public void writeToFile(){

    }
}
