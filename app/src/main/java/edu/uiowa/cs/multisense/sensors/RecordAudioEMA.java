package edu.uiowa.cs.multisense.sensors;

import android.app.Service;
import android.content.Intent;
import android.media.AudioRecord;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;

import edu.uiowa.cs.multisense.MultiSenseConstants;
import edu.uiowa.cs.multisense.fileio.DataStore;

public class RecordAudioEMA extends Service {

    private boolean isRecording = false;
    private AudioRecord audioRecord;
    private Thread extractFromBuffer;
    private long timeStamp;
    private ArrayList<short[]> recordedAudio;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Log.d("MS:RecordService", "Entering onStartCommand()");
        if(!isRecording){
            Log.d("MS:RecordService", "Not recording, going to record");
            initVals();
            audioRecord.startRecording();
            isRecording = true;
            extractFromBuffer.start();
        }
        Log.d("MS:RecordService", "Exiting onStartCommand()");
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy(){
        Log.d("MS:RecordService", "Entering onDestroy()");
        isRecording = false;
        audioRecord.stop();
        audioRecord.release();
        audioRecord = null;
        extractFromBuffer = null;
        Log.d("MS:RecordService", "Total recorded shorts:"+recordedAudio.size());
        DataStore audioData = new DataStore(timeStamp, recordedAudio);
        audioData.writeToFile();
        Log.d("MS:RecordService", "Exiting onDestroy()");
    }

    private void initVals(){
        Log.d("MS:RecordService", "Entering initVals()");
        timeStamp = System.currentTimeMillis();
        audioRecord = new AudioRecord(MultiSenseConstants.RECORDER_SOURCE,
                MultiSenseConstants.SAMPLING_RATE,
                MultiSenseConstants.RECORDER_CHANNEL,
                MultiSenseConstants.RECORDER_ENCODING,
                MultiSenseConstants.BUFFER_SIZE_BYTES);
        recordedAudio = new ArrayList<>();
        extractFromBuffer = new Thread(new Runnable() {
            @Override
            public void run() {
                extractShortsFromBuffer();
            }
        }, "AudioRecordingThread");
        Log.d("MS:RecordService", "Exiting initVals()");
    }

    private ArrayList<short[]> extractShortsFromBuffer() {
        Log.d("MS:RecordService", "Entered extractShortsFromBuffer");
        short[] audioData = new short[MultiSenseConstants.BUFFER_SIZE_BYTES/2];
        int status;
        while (isRecording){
            status = audioRecord.read(audioData, 0, MultiSenseConstants.BUFFER_SIZE_BYTES/2);
            if(-1 < status){
                recordedAudio.add(audioData);
            }
        }
        // in case some data is left in the buffer
        status = audioRecord.read(audioData, 0, MultiSenseConstants.BUFFER_SIZE_BYTES/2);
        if(-1 < status){
            recordedAudio.add(audioData);
        }
        Log.d("MS:RecordService", "Exiting extractShortsFromBuffer");
        return recordedAudio;
    }

}
