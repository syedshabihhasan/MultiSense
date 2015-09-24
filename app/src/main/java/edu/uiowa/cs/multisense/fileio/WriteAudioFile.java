package edu.uiowa.cs.multisense.fileio;

import android.util.Log;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import edu.uiowa.cs.multisense.MultiSenseConstants;

/**
 * Created by Syed Shabih Hasan on 9/23/15.
 */
public class WriteAudioFile {
    private String filetoWrite;
    private FileOutputStream fileOutputStream;
    private byte[] byteToWrite;

    public WriteAudioFile(String currentDateTime){
        this.filetoWrite = MultiSenseConstants.FILE_LOCATION+currentDateTime+".audio";
        try{
            this.fileOutputStream = new FileOutputStream(this.filetoWrite);
        }catch (FileNotFoundException e){
            Log.e("MS:", "WriteAudioFile constructor, file not found");
            e.printStackTrace();
        }
    }

    public void writeToFile(short[] shortArray){
        this.byteToWrite = short2ByteBuffer(shortArray);
        try{
            this.fileOutputStream.write(this.byteToWrite, 0, this.byteToWrite.length);
        }catch (IOException e){
            Log.e("MS:", "Error writing to file");
            e.printStackTrace();
        }
    }

    public void doneWriting(){
        try{
            this.fileOutputStream.close();
        }catch(IOException e){
            Log.e("MS:", "Error closing file");
            e.printStackTrace();
        }
    }

    private byte[] short2ByteBuffer(short[] shortData){
        ByteBuffer buffer = ByteBuffer.allocate(MultiSenseConstants.BUFFER_SIZE_BYTES);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        for(int i=0; i < shortData.length; i++){
            buffer.putShort(shortData[i]);
        }
        buffer.flip();
        return buffer.array();
    }
}
