package com.example.mifone_lib.util;

import android.content.Context;
import android.os.Build;

//import androidx.annotation.RequiresApi;

import com.example.mifone_lib.R;

import org.linphone.core.Call;
import org.linphone.core.ConfiguringState;
import org.linphone.core.Core;
import org.linphone.core.CoreListenerStub;
import org.linphone.core.Factory;
import org.linphone.core.GlobalState;
import org.linphone.core.LoggingServiceListener;
import org.linphone.core.tools.Log;
import org.linphone.mediastream.Version;

import java.util.ArrayList;


public class MifoneContext {
    private static MifoneContext sInstance = null;

    private Context mContext;

    private final LoggingServiceListener mJavaLoggingService =
            (logService, domain, lev, message) -> {
                switch (lev) {
                    case Debug:
                        android.util.Log.d(domain, message);
                        break;
                    case Message:
                        android.util.Log.i(domain, message);
                        break;
                    case Warning:
                        android.util.Log.w(domain, message);
                        break;
                    case Error:
                        android.util.Log.e(domain, message);
                        break;
                    case Fatal:
                    default:
                        android.util.Log.wtf(domain, message);
                        break;
                }
            };
    private CoreListenerStub mListener;
//    private NotificationsManager mNotificationManager;
    private MifoneManager mMifoneManager;
//    private ContactsManager mContactsManager;
    private final ArrayList<CoreStartedListener> mCoreStartedListeners;

    public static boolean isReady() {
        return sInstance != null;
    }

    public static MifoneContext instance() {
        if (sInstance == null) {
            throw new RuntimeException("[Context] Mifone Context not available!");
        }
        return sInstance;
    }

    public MifoneContext(Context context) {
        mContext = context;
        mCoreStartedListeners = new ArrayList<>();

        MifonePreferences.instance().setContext(context);
        Factory.instance().setLogCollectionPath(context.getFilesDir().getAbsolutePath());
        boolean isDebugEnabled = MifonePreferences.instance().isDebugEnabled();
        MifoneUtils.configureLoggingService(isDebugEnabled, context.getString(R.string.app_name));

        dumpDeviceInformation();
        dumpLinphoneInformation();

        sInstance = this;
        mMifoneManager = new MifoneManager(context);
    }

    public void start(boolean isPush) {
        Log.i("[Context] Starting, push status is ", isPush);
        mMifoneManager.startLibLinphone(isPush, mListener);

//        mNotificationManager.onCoreReady();
//
//        mContactsManager = new ContactsManager(mContext);
//        if (!Version.sdkAboveOrEqual(Version.API26_O_80)
//                || (mContactsManager.hasReadContactsAccess())) {
//            mContext.getContentResolver()
//                    .registerContentObserver(
//                            ContactsContract.Contacts.CONTENT_URI, true, mContactsManager);
//        }
//        if (mContactsManager.hasReadContactsAccess()) {
//            mContactsManager.enableContactsAccess();
//        }
//        mContactsManager.initializeContactManager();
    }

    public void destroy() {
        Log.i("[Context] Destroying");
        Core core = MifoneManager.getCore();
        if (core != null) {
            core.removeListener(mListener);
            core = null; // To allow the gc calls below to free the Core
        }

//        if (mNotificationManager != null) {
//            mNotificationManager.destroy();
//        }
//
//        if (mContactsManager != null) {
//            mContactsManager.destroy();
//        }

        sInstance = null;

        if (MifonePreferences.instance().useJavaLogger()) {
            Factory.instance().getLoggingService().removeListener(mJavaLoggingService);
        }
    }

    public void updateContext(Context context) {
        mContext = context;
    }

    public Context getApplicationContext() {
        return mContext;
    }

    /* Managers accessors */

    public LoggingServiceListener getJavaLoggingService() {
        return mJavaLoggingService;
    }

//    public NotificationsManager getNotificationManager() {
//        return mNotificationManager;
//    }

    public MifoneManager getMifoneManager() {
        return mMifoneManager;
    }

//    public ContactsManager getContactsManager() {
//        return mContactsManager;
//    }

    public void addCoreStartedListener(CoreStartedListener listener) {
        mCoreStartedListeners.add(listener);
    }

    public void removeCoreStartedListener(CoreStartedListener listener) {
        mCoreStartedListeners.remove(listener);
    }

    /* Log device related information */

    private void dumpDeviceInformation() {
        Log.i("==== Phone information dump ====");
//        Log.i("DISPLAY NAME=" + Compatibility.getDeviceName(mContext));
        Log.i("DEVICE=" + Build.DEVICE);
        Log.i("MODEL=" + Build.MODEL);
        Log.i("MANUFACTURER=" + Build.MANUFACTURER);
        Log.i("ANDROID SDK=" + Build.VERSION.SDK_INT);
        StringBuilder sb = new StringBuilder();
        sb.append("ABIs=");
        for (String abi : Version.getCpuAbis()) {
            sb.append(abi).append(", ");
        }
        Log.i(sb.substring(0, sb.length() - 2));
    }

    private void dumpLinphoneInformation() {
//        Log.i("==== Mifone information dump ====");
//        Log.i("VERSION NAME=" + BuildConfig.VERSION_NAME);
//        Log.i("VERSION CODE=" + BuildConfig.VERSION_CODE);
//        Log.i("PACKAGE=" + BuildConfig.APPLICATION_ID);
//        Log.i("BUILD TYPE=" + BuildConfig.BUILD_TYPE);
//        Log.i("SDK VERSION=" + mContext.getString(vn.mitek.mifone.R.string.linphone_sdk_version));
//        Log.i("SDK BRANCH=" + mContext.getString(vn.mitek.mifone.R.string.linphone_sdk_branch));
    }

    /* Call activities */

//    private void onIncomingReceived() {
//        Intent intent = new Intent(mContext, CallIncomingActivity.class);
//        // This flag is required to start an Activity from a Service context
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        mContext.startActivity(intent);
//    }
//
//    private void onOutgoingStarted() {
//        Intent intent = new Intent(mContext, CallOutgoingActivity.class);
//        // This flag is required to start an Activity from a Service context
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        mContext.startActivity(intent);
//    }
//
//    private void onCallStarted() {
//        Intent intent = new Intent(mContext, CallActivity.class);
//        // This flag is required to start an Activity from a Service context
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        mContext.startActivity(intent);
//    }

    public interface CoreStartedListener {
        void onCoreStarted();
    }
}
