package com.example.myactivitytests.modules;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.FileUtils;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Manage device storage and paths.
 */
public class Storage {
    private static String TAG_3="STORAGE!";
    private static final String TAG = "Storage";

    private static Storage storageInstance = null;

    /**
     * File separator (sometime it's "/", sometimes "\" ...).
     */
    private static final String FS = File.separator;

    /**
     * Internal storage path.
     */
    private static final String INTERNAL_STORAGE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + FS;

    //========================//
    //       FOLDER PATH      //
    //========================//

    /**
     * Path that contains each folder of each program launch.
     */
    private static final String PIXEE_FOLDER_PATH = INTERNAL_STORAGE_PATH + "PixeeMedical" + FS;

    /**
     * Path to the config folder that contains PixeeMedical general configuration files
     */
    private static final String PIXEE_CONFIG_FOLDER_PATH = PIXEE_FOLDER_PATH + "Config" + FS;

    /**
     * Path that contains each folder and files used by KneePlus.
     */
    private static final String KNEEPLUS_FOLDER_PATH = PIXEE_FOLDER_PATH + "KneePlus" + FS;

    /**
     * Path to the Config folder that contains configuration files
     */
    private static final String CONFIG_FOLDER_PATH = KNEEPLUS_FOLDER_PATH + "Config" + FS;

    /**
     * Path to the Logs folder that contains every logs files
     */
    private static final String LOGS_FOLDER_PATH = KNEEPLUS_FOLDER_PATH + "Logs" + FS;

    /**
     * Path to surgery reports
     */
    private static final String REPORTS_FOLDER_PATH = INTERNAL_STORAGE_PATH + "SurgeryReports" + FS;

    //=======================//
    //       FILE PATH       //
    //=======================//

    /**
     * KneePlus general configuration file path
     */
    private static final String CONFIG_FILE_PATH = CONFIG_FOLDER_PATH + "config.json";

    /**
     * KneePlus instruments configuration file path
     */
    private static final String APP_CONFIG_PATH = CONFIG_FOLDER_PATH + "app_config.json";

    /**
     * Camera calibration path.
     */
    private static final String CALIB_PATH = PIXEE_CONFIG_FOLDER_PATH + "camera_calib.json";

    /**
     * OpenGL calibration file path
     */
    private static final String CALIB_OPENGL_PATH = PIXEE_CONFIG_FOLDER_PATH + "camera_calib_rotated.json";

    /**
     * SKey file path.
     */
    private static final String SECRET_KEY_FILE_PATH = CONFIG_FOLDER_PATH + "pixee_kneeplus.txt";

    /**
     * TK file path.
     */
    private static final String TK_FILE_PATH = PIXEE_CONFIG_FOLDER_PATH + "pixee.txt";

    //======================//
    //       FILE NAME      //
    //======================//

    public final String LOGCAT_FILE_NAME = "logcat.log";

    private final String SAVED_STATE_FILE_NAME = "SavedState.json";

    private final String SAVED_PROTOCOL_FILE_NAME = "SavedProtocol.json";

    private final String LOGS_FOLDER_NAME = "Logs";

    private final String FRAMES_FOLDER_NAME = "Images";

    private final String INFO_FILE_NAME = "info.json";

    private final String HTML_REPORT_FILE_NAME = "report.html";

    private final String REPORT_VALUES_FILE_NAME = "reportValues.json";

    private final String TOKEN_USED_FILE_NAME = "tokenUsed.json";

    private final String KNEE_DATA_FILE_NAME = "knee.json";

    private final String KNEE_DATA_FOLDER_NAME = "knee";

    private final String HC_DATA_FOLDER_NAME = "hipcenter";

    private final String TENSOR_DATA_FOLDER_NAME = "tensor";

    private final String PROTOCOL_FILE_NAME = "protocol.json";

    // ========================

    /**
     * Write dates with this format.
     */
    private SimpleDateFormat mStorageDateFormat = new SimpleDateFormat("yyyyMMdd--H-m-s", Locale.US);

    /**
     * Camera calibration path.
     */
    private String mReportPath;

    private String mSavedReportValuesPath;

    /**
     * Path that contains each folder of each program launch.
     */
    private String mSavedStatePath;

    private String mSavedProtocolPath;

    private String mProtocolPath;

    /**
     * Knee Data Folder
     */
    private String mKneeDataFolderPath;

    /**
     * HC Data Folder
     */
    private String mHCDataFolderPath;

    /**
     * TensorData Folder Path
     */
    private String mTensorDataFolderPath;

    /**
     * Token used path for restoration
     */
    private String mTokenUsedPath;

    /**
     * Path of the created folder each time app is running on device.
     */
    private String mCurrentPath;

    /**
     * Path where storing each frame.
     */
    private String mFramesPath;

    /**
     * Path where storing every data each frame.
     */
    private String mLogsPath;

    /**
     * Shared preferences used to save a KneePlus launch counter
     */
    private SharedPreferences mKneePlusSharedPrefs;

    /**
     * Name used to define the shared preferences file
     */
    public final static String KNEEPLUS_SHARED_PREFS = "kneeplus_shared_prefs";

    /**
     * Name of the key containing the surgery index value
     */
    public final String SURGERY_INDEX = "surgeryIndex";

    public final String SET_ID = "sid";

    private final String DEMO_MODE = "demoMode";

    /**
     * Accelerometer offset
     */
    private final String ACCELEROMETER_OFFEST = "accelerometer_offset";

    /**
     * Initialize all storage params and create folders.
     * @param context context of the app.
     */
    private Storage(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("init storage : context is null");
        }
        // init paths
        mSavedStatePath = context.getFilesDir().getPath() + FS + SAVED_STATE_FILE_NAME;
        mSavedProtocolPath = context.getFilesDir().getPath() + FS + SAVED_PROTOCOL_FILE_NAME;
        mSavedReportValuesPath = context.getFilesDir().getPath() + FS + REPORT_VALUES_FILE_NAME;
        mTokenUsedPath = context.getFilesDir().getPath() + FS + TOKEN_USED_FILE_NAME;
        mCurrentPath = LOGS_FOLDER_PATH + mStorageDateFormat.format(new Date()) + FS;
        mLogsPath = mCurrentPath + LOGS_FOLDER_NAME + FS;
        mFramesPath = mCurrentPath + FRAMES_FOLDER_NAME + FS;

        mKneeDataFolderPath = mCurrentPath + KNEE_DATA_FOLDER_NAME + FS;
        mHCDataFolderPath = mCurrentPath + HC_DATA_FOLDER_NAME + FS;
        mTensorDataFolderPath = mCurrentPath + TENSOR_DATA_FOLDER_NAME + FS;

        mReportPath = REPORTS_FOLDER_PATH + mStorageDateFormat.format(new Date()) + "-" + HTML_REPORT_FILE_NAME;

        //why duplicated line
        mProtocolPath = mCurrentPath + PROTOCOL_FILE_NAME;
        mProtocolPath = mCurrentPath + PROTOCOL_FILE_NAME;


        // INIT SHARED PREFS
        mKneePlusSharedPrefs = context.getSharedPreferences(KNEEPLUS_SHARED_PREFS, Context.MODE_PRIVATE);
    }

    public static Storage getStorage(Context context){
        if(storageInstance == null){
            storageInstance = new Storage(context);
        }
        return storageInstance;
    }

    public String getSavedProtocolPath() {
        return mSavedProtocolPath;
    }

    public String getReportPath() {
        return mReportPath;
    }

    public String getReportValuesPath(){
        return mSavedReportValuesPath;
    }

    public String getSavedStatePath() {
        return mSavedStatePath;
    }

    public void setSavedStatePath(String pSavedStatePath) {
        mSavedStatePath = pSavedStatePath;
    }

    public String getKneeDataFolderPath(){ return mKneeDataFolderPath; }

    public String getHCDataFolderPath(){ return mHCDataFolderPath; }

    public String getTensorDataFolderPath() {
        return mTensorDataFolderPath;
    }

    public String getLogsPath() {
        return mLogsPath;
    }



    public void startSavingLogcat() {
        File filename = new File(mCurrentPath + LOGCAT_FILE_NAME);
        try {
            if (filename.createNewFile()) {
                Log.d(TAG_3,"about to get logs now logcat  in "+filename.getAbsolutePath());
                Runtime.getRuntime().exec("logcat -c");
                // save everything
                String cmd = "logcat -f " + filename.getAbsolutePath();
                // save only logs with tag MainActivity
                //String cmd = "logcat -s MainActivity -f" + filename.getAbsolutePath();
                Runtime.getRuntime().exec(cmd);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Save various information about the device and the app (free memory, app version...).
     * Also save app configuration json file.
     * @param context Context of the device.
     * @return JSON Object containing all the information.
     */
    public JSONObject saveInfo(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Context can not be null");
        }

        // save information about the device
        JSONObject jsonInfo = new JSONObject();
        try {
               jsonInfo.put("glasses_brand", Build.MANUFACTURER + " " + Build.MODEL);
            jsonInfo.put("android_sdk", Build.VERSION.SDK_INT);
            jsonInfo.put("android_version", Build.VERSION.RELEASE);
            jsonInfo.put("soft_version", context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName);
       } catch (JSONException | PackageManager.NameNotFoundException e) {
            Log.e(TAG, e.getMessage());
        }

        return jsonInfo;
    }

    /**
     * Return the calibration path and copy file to current path.
     * @return calibration path.
     */
    public String getCalibrationPathAndCopy() {
     return CALIB_PATH;
    }

    /**
     * Return the OpenGL calibration path and copy file to current path.
     * @return OpenGL calibration path.
     */
    public String getOpenGLCalibPathAndCopy() {
     return CALIB_OPENGL_PATH;
    }

    /**
     * Save FunctioningMode to a json in the current path and in the hidden app storage.
     */
    public void saveProtocolWithCopy() {

    }

    /**
     * Save actual date in the sharedPreferences.
     */
    public void saveDateInCache() {
        saveDateInCache(new Date().getTime());
    }

    /**
     * Save given date in the sharedPreferences.
     * @param date date to save.
     */
    public void saveDateInCache(long date) {
        if (date < 0) {
            throw new IllegalArgumentException("saveDateInCache : negative date");
        }
        mKneePlusSharedPrefs.edit().putLong("time", date).commit();
    }

    /**
     * Get the last date when app has been closed from the sharedPreferences.
     * @return date of the last close.
     */
    public Date getLastCloseDate() {
        return new Date(mKneePlusSharedPrefs.getLong("time", 0));
    }

    /**
     * Save the given surgery index in the SharedPreferences.
     * @param index String surgeryIndex
     * @return true if written successfully, false otherwise
     */
    private boolean saveSurgeryIndex(String index){
       return mKneePlusSharedPrefs.edit().putString(SURGERY_INDEX, index).commit();
    }

    public boolean saveDemoMode(boolean demo) {
        return mKneePlusSharedPrefs.edit().putBoolean(DEMO_MODE, demo).commit();
    }

    public boolean saveAccelerometerOffset(float offest[]) {
        boolean saved = mKneePlusSharedPrefs.edit().putFloat(ACCELEROMETER_OFFEST + "_x", offest[0]).commit();
        saved &= mKneePlusSharedPrefs.edit().putFloat(ACCELEROMETER_OFFEST + "_y", offest[1]).commit();
        saved &= mKneePlusSharedPrefs.edit().putFloat(ACCELEROMETER_OFFEST + "_z", offest[2]).commit();

        return saved;
    }

    public float[] getAccelerometerOffset() {
        float[] offset = new float[3];
        offset[0] = mKneePlusSharedPrefs.getFloat(ACCELEROMETER_OFFEST + "_x", 0);
        offset[1] = mKneePlusSharedPrefs.getFloat(ACCELEROMETER_OFFEST + "_y", 0);
        offset[2] = mKneePlusSharedPrefs.getFloat(ACCELEROMETER_OFFEST + "_z", 0);

        return offset;
    }

    public boolean saveSid(String sid) {
        return mKneePlusSharedPrefs.edit().putString(SET_ID, sid).commit();
    }

    /**
     * Increment the current surgery index by 1
     */
    public void incrementSurgeryIndex() {
        int current = Integer.parseInt(getSurgeryIndex());
        boolean ok = saveSurgeryIndex(String.valueOf(current+1));
        if(!ok){
            Log.e(TAG, "Surgery index could not be incremented");
        }
    }

    /**
     * Get the current surgeryIndex saved in the shared preferences.
     * @return String representing the surgeryIndex
     */
    public String getSurgeryIndex(){
        return mKneePlusSharedPrefs.getString(SURGERY_INDEX, "0");
    }

    private boolean getDemoMode() {
        return mKneePlusSharedPrefs.getBoolean(DEMO_MODE, false);
    }

    public String getSid() {
        return mKneePlusSharedPrefs.getString(SET_ID, "...");
    }

    public boolean isDemoMode() {
        return getDemoMode();
    }

    /**
     * Method that return the launchId composed of device
     * serial number and the current surgeryIndex
     * @return String launchId
     */

    public String getConfigPath() {
        return CONFIG_FILE_PATH;
    }

    public String getAppConfigPath() {
        return APP_CONFIG_PATH;
    }

    public String getReportsFolderPath() {
        return REPORTS_FOLDER_PATH;
    }

    public String getReportFileName() {
        return HTML_REPORT_FILE_NAME;
    }

    public String getCurrentPath() {
        return mCurrentPath;
    }

    public String getFramesPath() {
        return mFramesPath;
    }

    public String getSecretKeyPath() {
        return SECRET_KEY_FILE_PATH;
    }

    public String getTokenUsedPath(){
        return mTokenUsedPath;
    }

    public String getPixeeTKPath() {
        return TK_FILE_PATH;
    }
}
