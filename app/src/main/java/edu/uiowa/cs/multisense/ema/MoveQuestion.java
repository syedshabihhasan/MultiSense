package edu.uiowa.cs.multisense.ema;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by hasanshabih on 9/18/15.
 */
public class MoveQuestion {
    //TODO link this class with MultiSense

    private HashMap<Integer, Integer[]> logicMap;

    public MoveQuestion(HashMap<Integer, Integer[]> logicMap){
        this.logicMap = logicMap;
    }

    /**
     * Based on the logic, select the next question
     * */
    public int getNextQuestion(int qNo, ArrayList<String[]> qDB,
                               ResponseStore responses){

        Log.d("NQ:gNQ", "Entered getNextQuestion");
        // we have moved out of the range
        if(qNo >= qDB.size()){
            Log.d("NQ:gNQ", "qNo >= qDB.size(), returning -1");
            return -1;
        }

        // if no conditions exist for the next question, return the next question index, otherwise
        // check and see if the conditions are met, if they are, return next question, otherwise
        // check to see if qNo+1 is a viable option

        if(!this.logicMap.containsKey(qNo)){
            Log.d("NQ:gNQ", "No conditions exist for the qNo:"+qNo+", good to go");
            return qNo;
        }
        else{
            Log.d("NQ:gNQ", "Conditions exist for qNo:"+qNo+", checking");
            Integer[] conditions = logicMap.get(qNo);
            boolean conditionsMet = true;
            for(int i=0; i< conditions.length; i+=3){
                int qToCheck = conditions[i];
                int lowerLim = conditions[i+1];
                int upperLim = conditions[i+2];
                // check if the question has even been answered
                if(!responses.entryExists(qToCheck)){
                    conditionsMet &= false;
                    break;
                }
                else{
                    int actualResponse = responses.getResponse(qToCheck);

                    if(lowerLim < actualResponse && actualResponse < upperLim){
                        conditionsMet &= true;
                    }
                    else{
                        conditionsMet &= false;
                        break;
                    }
                }
            }
            if(conditionsMet){
                Log.d("NQ:gNQ", "all conditions met, qNo:"+qNo+", good to go");
                return qNo;
            }
            else{
                Log.d("NQ:gNQ", "some condition(s) not met, qNo:"+qNo+", going for qNo+1");
                return getNextQuestion(qNo+1, qDB, responses);
            }
        }
    }

    /**
     * Given the current question (qNo), find the previous question.
     * */
    public int getPreviousQuestion(int qNo, ResponseStore responses){
        return responses.getPreviousQuestion(qNo);
    }
}
