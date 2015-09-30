package edu.uiowa.cs.multisense.ema;

import android.content.Context;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import edu.uiowa.cs.multisense.MultiSenseConstants;
import edu.uiowa.cs.multisense.fileio.ReadConfigFile;

/**
 * Created by Syed Shabih Hasan on 9/29/15.
 */
public class CheckAlarmConditions {
    private Context context;
    private ReadConfigFile readConfigFile;

    private final int DATE_TIME_CORRECT = 1;
    private final int DATE_OR_TIME_INCORRECT = 0;
    private final int DATE_TIME_INCORRECT = -1;

    public CheckAlarmConditions(Context ipContext){
        this.context = ipContext;
        readConfigFile = new ReadConfigFile(this.context);
    }

    public int IsAlarmLegal(long currentTime){
        String[] indvInfo = getIndivInfo(readConfigFile.readConfigData());
        String[] startDate = indvInfo[4].split("\\s");
        String[] endDate = indvInfo[5].split("\\s");
        String[] startTime = indvInfo[6].split("\\s");
        String[] endTime = indvInfo[7].split("\\s");
        // check if currentTime is before the end date and end time
        if(beforeDateEndTime(currentTime, endDate, endTime)){
            Log.d("MS:", "currentTime  < End D & T");
            // check if currentTime is after start date and start time
            if(!beforeDateEndTime(currentTime, startDate, startTime)){
                Log.d("MS:", "currentTime  > Start D & T");
                // check if current Time is before today's end time
                String[] currentDate = getCurrentDate();
                if(beforeDateEndTime(currentTime, currentDate, endTime)){
                    Log.d("MS:", "currentTime  < End T");
                    // check if current Time is after today's start time
                    if(!beforeDateEndTime(currentTime, currentDate, startTime)){
                        Log.d("MS:", "currentTime  > Start T");
                        return DATE_TIME_CORRECT;
                    }else{
                        Log.d("MS:", "currentTime  <= Start T");
                        return DATE_OR_TIME_INCORRECT;
                    }
                }else{
                    Log.d("MS:", "currentTime  > End T");
                    return DATE_OR_TIME_INCORRECT;
                }
            }else{
                Log.d("MS:", "currentTime  <= Start D & T");
                return DATE_OR_TIME_INCORRECT;
            }
        }else{
            Log.d("MS:", "currentTime  >= End D & T");
            return DATE_TIME_INCORRECT;
        }
    }

    private String[] getIndivInfo(String allConfigInfo){
        return allConfigInfo.split("\\n");
    }

    private boolean beforeDateEndTime(long currentTime, String[] dateToCheck, String[] timeToCheck){
        Date currentDateTime = new Date(currentTime);
        String endDateTimeString = dateToCheck[0] + " " + monthToInt(dateToCheck[1]) + " " +
                dateToCheck[2] + " " + timeToCheck[0] + " " + timeToCheck[1];
        Log.d("MS:", "toCheck String: "+ endDateTimeString);
        DateFormat dateFormat = new SimpleDateFormat("dd MM yyyy kk mm", Locale.US);
        Date endDateTime;
        try {
            endDateTime = dateFormat.parse(endDateTimeString);
            Log.d("MS:", "currentDate: "+ currentDateTime.toString() + ", toCheck: "+ endDateTime.toString());
            return currentDateTime.before(endDateTime);
        } catch (ParseException e) {
            Log.e("MS:", "Could not parse date time for checking");
            e.printStackTrace();
        }
        return false;
    }

    private String monthToInt(String month){
        for(int i=0; i < MultiSenseConstants.MONTH_DICT.length; i++){
            if(month.equals(MultiSenseConstants.MONTH_DICT[i])){
                if(i+1 < 10) {
                    return "0" + (i+1);
                }else{
                    return ""+ (i+1);
                }
            }
        }
        return "-1";
    }

    private String[] getCurrentDate(){
        String[] currentDateTime;
        long currentTime = System.currentTimeMillis();
        Date cDT = new Date(currentTime);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MM yyyy", Locale.US);
        String currentDT = simpleDateFormat.format(cDT);
        currentDateTime = currentDT.split("\\s");
        currentDateTime[1] = MultiSenseConstants.MONTH_DICT[(Integer.parseInt(currentDateTime[1]) - 1)];
        Log.d("MS:", "Current Date from getCurrentDate: "+currentDateTime[0] +
                " " + currentDateTime[1] + " " + currentDateTime[2]);
        return currentDateTime;
    }
}
