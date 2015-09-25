package edu.uiowa.cs.multisense;

import android.media.AudioFormat;
import android.media.MediaRecorder;

/**
 * Created by Syed Shabih Hasan on 9/22/15.
 */
public class MultiSenseConstants {
    public final static int SAMPLING_RATE = 44100;
    public final static int RECORDER_CHANNEL = AudioFormat.CHANNEL_IN_MONO;
    public final static int RECORDER_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    public final static int RECORDER_SOURCE = MediaRecorder.AudioSource.MIC;
    public final static int BUFFER_SIZE_BYTES = 8192;

    public final static int AUDIO_TYPE = 1;
    public final static int SURVEY_TYPE = 2;

    public final static String FILE_LOCATION = "/sdcard/MultiSense/";

    public static final int TIMER_COUNT = 1*60*1000;
}
