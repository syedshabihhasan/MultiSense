package edu.uiowa.cs.multisense.fileio;

import android.content.Context;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import edu.uiowa.cs.multisense.MultiSenseConstants;

/**
 * Created by Syed Shabih Hasan on 9/27/15.
 */
public class WriteConfigFile {

    private Context context;
    private String configData;
    private ArrayList<String[]> configEntries;

    public WriteConfigFile(Context ipContext){
        this.context = ipContext;
        this.configData = "";
        this.configEntries = new ArrayList<>();
    }

    public void pushConfig(String field, String configString){
        if(field.equals("Start Time") || field.equals("End Time")){
            String[] temp = configString.split("\\s");
            if(1 == temp[1].length()){
                temp[1] = "0"+temp[1];
            }
            if(1 == temp[0].length()){
                temp[0] = "0"+temp[0];
            }
            configString = temp[0] + " " + temp[1];
        }else if(field.equals("Start Date") || field.equals("End Date")){
            String[] temp = configString.split("\\s");
            if(1 == temp[0].length()){
                temp[0] = "0"+temp[0];
            }
            configString = temp[0] + " " + temp[1] + " " + temp[2];
        }
        this.configData += configString + "\n";
        this.configEntries.add(new String[]{field, configString});
        Log.d("MS:", "Pushed data, "+ field + ": "+ configString);
    }

    public ArrayList<String[]> getConfigData(){
        return this.configEntries;
    }

    public boolean saveConfig(){
        FileOutputStream fileOutputStream;
        try{
            fileOutputStream = this.context.openFileOutput(MultiSenseConstants.CONFIG_FILENAME,
                    Context.MODE_PRIVATE);
        }catch(FileNotFoundException e){
            Log.e("MS:", "Could not open multisenseConfig.cfg");
            e.printStackTrace();
            return false;
        }
        try {
            fileOutputStream.write(this.configData.getBytes());
        } catch (IOException e) {
            Log.e("MS:", "Could not write data to multisenseConfig.cfg");
            e.printStackTrace();
            return false;
        }
        try {
            fileOutputStream.close();
        } catch (IOException e) {
            Log.e("MS:", "Error closing multisenseConfig.cfg file");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void clearCurrentConfig(){
        this.configData = "";
        this.configEntries.clear();
    }

}
