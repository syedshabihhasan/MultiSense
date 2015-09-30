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
    public final static int BUFFER_SIZE_BYTES = 4096;

    public final static int AUDIO_TYPE = 1;
    public final static int SURVEY_TYPE = 2;

    public final static String FILE_LOCATION = "/sdcard/MultiSense/";

    public static final int TIMER_COUNT = 2*60*1000;
    public static final String[] MONTH_DICT = new String[]{
            "JAN",
            "FEB",
            "MAR",
            "APR",
            "MAY",
            "JUN",
            "JUL",
            "AUG",
            "SEP",
            "OCT",
            "NOV",
            "DEC"
    };

    public static final float EDITTEXT_FONTSIZE = 32;
    public static final float TEXTVIEW_FONTSIZE = 32;
    public static final float BUTTON_FONTSIZE = 32;

    public static final String CONFIG_FILENAME = "multisenseConfig.cfg";
}
