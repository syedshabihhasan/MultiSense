package edu.uiowa.cs.multisense.ema;

import java.util.ArrayList;

/**
 * @author Syed Shabih Hasan
 */
public class ResponseStore {
    private ArrayList<int[]> responses;

    public ResponseStore(){
        this.responses = new ArrayList();
    }


    /**
     * Add a record to the existing response store
     * */
    private void pushResponse(int qNo, int response){
        this.responses.add(new int[]{qNo, response});
    }


    /**
     * Checks if an entry exists in the records for the input question number
     * @return <b>true</b> if question exists<br> <b>false</b> if question does not exist
     * */
    public boolean entryExists(int qNo){
        return (-1 != searchQuestionNo(qNo));
    }


    /**
     * Removes the input question, and all the questions that were answered after it
     * */
    public void removeResponse(int qNo){
        int idx;
        if( -1 != (idx = searchQuestionNo(qNo))){
            for(int i = idx; i < this.responses.size(); i++){
                this.responses.remove(i);
            }
        }
    }


    /**
     * Removes the input question number and all the questions after it, then updates the records
     * */
    public void removeAndPush(int qNo, int response){
        removeResponse(qNo);
        pushResponse(qNo, response);
    }


    /**
     * Returns all the responses
     * */
    public ArrayList<int[]> getAllResponses(){
        return this.responses;
    }

    /**
     * Return the response of a particular question, if entry does not exist, return -1
     * */
    public int getResponse(int qNo){
        int idx;
        if(-1 != (idx = searchQuestionNo(qNo))){
            int[] resp = this.responses.get(idx);
            return resp[1];
        }else {
            return -1;
        }
    }

    /**
     * Returns the index of the entry in the response store, otherwise returns -1
     * */
    private int searchQuestionNo(int qNo){
        int qPresentAt = -1;
        int[] temp = new int[2];
        for(int i = 0; i < this.responses.size(); i++){
            temp = this.responses.get(i);
            if(qNo == temp[0]){
                qPresentAt = i;
                break;
            }
        }
        return qPresentAt;
    }

    /**
     * Return the question number of the of question asked before the input qNo, return -1 if the
     * current question (qNo) does not exist
     * */
    public int getPreviousQuestion(int qNo){
        if(0 == qNo){
            return 0;
        }else{
            int temp[] = responses.get(responses.size()-1);
            return temp[0];
        }
    }
}
