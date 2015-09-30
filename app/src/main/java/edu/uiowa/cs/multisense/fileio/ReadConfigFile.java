package edu.uiowa.cs.multisense.fileio;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;

import edu.uiowa.cs.multisense.MultiSenseConstants;

/**
 * Created by Syed Shabih Hasan on 9/27/15.
 */
public class ReadConfigFile {

    private Context context;
    private String configData;

    public ReadConfigFile(Context ipContext){
        this.context = ipContext;
        this.configData = "";
    }

    public String readConfigData(){
        try{
            FileInputStream fileInputStream = this.context.openFileInput(
                    MultiSenseConstants.CONFIG_FILENAME);
            int c;
            while(-1 != (c = fileInputStream.read())){
                this.configData += Character.toString((char) c);
            }
            fileInputStream.close();
        }catch (Exception e){
            Log.e("MS:", "Something happened");
            e.printStackTrace();
        }
        return this.configData;
    }
}
