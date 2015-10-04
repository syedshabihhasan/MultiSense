package edu.uiowa.cs.multisense.power;

import android.content.Context;
import android.os.PowerManager;
import android.util.Log;

import edu.uiowa.cs.multisense.MultiSenseConstants;

/**
 * Created by Syed Shabih Hasan on 10/2/15.
 */
public class CPULock {

    private static String lockAcquirer;
    private static boolean isLockAcquired;

    private static PowerManager powerManager;
    private static PowerManager.WakeLock wakeLock;
    public static Context context;

    public static void acquireCPULock(String acquirerName){
        Log.d("MS:", "Call for CPU lock by "+acquirerName);
        if(isLockAcquired){
            lockAcquirer = acquirerName;
            isLockAcquired = true;
        }else{
            powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK |
                    PowerManager.ACQUIRE_CAUSES_WAKEUP, "CPULock");
            wakeLock.acquire();
            isLockAcquired = true;
            lockAcquirer = acquirerName;
        }
        Log.d("MS:", "CPU Lock acquired by: "+ lockAcquirer);
    }

    public static int releaseLock(String releasorName){
        Log.d("MS:", "Lock release request by "+ releasorName);
        if(null == wakeLock){
            lockAcquirer = null;
            return MultiSenseConstants.LOCK_NULL;
        } else {
            if(releasorName.equals(lockAcquirer)){
                wakeLock.release();
                wakeLock = null;
                lockAcquirer = null;
                return MultiSenseConstants.LOCK_RELEASED;
            } else{
                return MultiSenseConstants.LOCK_ACQUIRED_BY_SOMEONE_ELSE;
            }
        }
    }

    public static String getLockAcquirer(){
        return lockAcquirer;
    }

    public static boolean lockAcquisitionStatus(){
        return isLockAcquired;
    }

}
