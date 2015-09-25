package edu.uiowa.cs.multisense.sensors;

import android.app.Service;
import android.content.Intent;
import android.media.AudioRecord;
import android.os.IBinder;
import android.util.Log;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import edu.uiowa.cs.multisense.MultiSenseConstants;
import edu.uiowa.cs.multisense.fileio.WriteAudioFile;

/**
 * @author syed shabih hasan
 * This is a service class that records audio and stores it to disk.
 * */
public class RecordAudioEMA extends Service {

    private static boolean isRecording = false;
    private AudioRecord audioRecord;
    private Thread extractFromBuffer;
    private WriteAudioFile writeAudioFile;
    private static TimerTask stopServiceTimerTask;
    private static Timer stopServiceTimer;

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
            isRecording = true;
            audioRecord.startRecording();
            extractFromBuffer.start();
            stopServiceTimer.schedule(stopServiceTimerTask, MultiSenseConstants.TIMER_COUNT);
        }else{
            // if there is a user initiated survey, or if there is an intent,
            // cancel the current timer and reset to record for another pre-decided duration
            stopServiceTimer.cancel();
            stopServiceTimer.purge();
            stopServiceTimer.schedule(stopServiceTimerTask, MultiSenseConstants.TIMER_COUNT);
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
        writeAudioFile.doneWriting();
        Log.d("MS:RecordService", "Exiting onDestroy()");
    }

    /**
     * Initializes the values of the all the variables needed, also defines the variable
     * */
    private void initVals(){
        Log.d("MS:RecordService", "Entering initVals()");
        Calendar calendar = Calendar.getInstance();

        String currentDateTime = ""+
                calendar.get(Calendar.YEAR)+"_"+
                (calendar.get(Calendar.MONTH)+1)+"_"+
                calendar.get(Calendar.DAY_OF_MONTH)+"_"+
                calendar.get(Calendar.HOUR_OF_DAY)+"_"+
                calendar.get(Calendar.MINUTE)+"_"+
                calendar.get(Calendar.SECOND)+"_"+
                calendar.get(Calendar.MILLISECOND);

        writeAudioFile = new WriteAudioFile(currentDateTime);

        audioRecord = new AudioRecord(MultiSenseConstants.RECORDER_SOURCE,
                MultiSenseConstants.SAMPLING_RATE,
                MultiSenseConstants.RECORDER_CHANNEL,
                MultiSenseConstants.RECORDER_ENCODING,
                MultiSenseConstants.BUFFER_SIZE_BYTES);

        // the thread that extracts the recorded audio
        extractFromBuffer = new Thread(new Runnable() {
            @Override
            public void run() {
                extractShortsFromBuffer();
            }
        }, "AudioRecordingThread");

        stopServiceTimerTask = new TimerTask() {
            @Override
            public void run() {
                onDestroy();
            }
        };

        stopServiceTimer = new Timer();

        Log.d("MS:RecordService", "Exiting initVals()");
    }

    /**
     * extract the recorded audio from the AudioRecorder object and ship it to be written to disk
     * */
    private void extractShortsFromBuffer() {
        Log.d("MS:RecordService", "Entered extractShortsFromBuffer");
        short[] audioData = new short[MultiSenseConstants.BUFFER_SIZE_BYTES/2];
        int status;
        while (isRecording){
            audioRecord.read(audioData, 0, MultiSenseConstants.BUFFER_SIZE_BYTES / 2);
            writeAudioFile.writeToFile(audioData);
        }
        // in case some data is left in the buffer
        status = audioRecord.read(audioData, 0, MultiSenseConstants.BUFFER_SIZE_BYTES/2);
        if(-1 < status){
            writeAudioFile.writeToFile(audioData);
        }
        Log.d("MS:RecordService", "Exiting extractShortsFromBuffer");
    }

}
