package com.example.mifone_lib.core;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.mifone_lib.api.Common;
import com.example.mifone_lib.api.IResponseAPIs;
import com.example.mifone_lib.listener.MifoneCoreListener;
import com.example.mifone_lib.model.other.Privileges;
import com.example.mifone_lib.model.other.ProfileUser;
import com.example.mifone_lib.model.other.UpdateTokenFirebase;
import com.example.mifone_lib.model.other.User;
import com.example.mifone_lib.model.response.APIsResponse;
import com.example.mifone_lib.util.DecodeAssistant;
import com.example.mifone_lib.util.MifoneManager;
import com.example.mifone_lib.util.MifonePreferences;
import com.example.mifone_lib.util.SharePrefUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.linphone.core.AccountCreator;
import org.linphone.core.Core;
import org.linphone.core.CoreListenerStub;
import org.linphone.core.ProxyConfig;
import org.linphone.core.RegistrationState;
import org.linphone.core.TransportType;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MifoneCore {
    private static MifoneCore mInstance;
    private User mUser;
    private CoreListenerStub mListener;
    private MifoneCoreListener mifoneCoreListener;
    private IResponseAPIs iResponseAPIs;
    private static final String defaultDomain = "mifone.vn/mitek";

    private MifoneCore(User mUser) {
        this.mUser = mUser;
    }

    public static void initMifoneCore(User mUser){
        mInstance = new MifoneCore(mUser);
    }

    public static MifoneCore getInstance(){
        if(mInstance==null){
            Log.e("[Mifone Core]", "Mifone core not initialized");
            return null;
        }

        return mInstance;
    }
    public void registerListener(MifoneCoreListener mifoneCoreListener){
        this.mifoneCoreListener = mifoneCoreListener;
        mListener =
                new CoreListenerStub() {
                    @Override
                    public void onCallStateChanged(Core core, org.linphone.core.Call call, org.linphone.core.Call.State state, String message) {
                        if (state == org.linphone.core.Call.State.End || state == org.linphone.core.Call.State.Released) {
                            mifoneCoreListener.onIncomingCall(state,message);
                        }
                    }

                    @Override
                    public void onRegistrationStateChanged(
                            Core core,
                            ProxyConfig proxyConfig,
                            RegistrationState state,
                            String message) {
                        mifoneCoreListener.onRegistrationStateChanged(proxyConfig,state,message);
//                        mSideMenuFragment.displayMainAccount();
//                        if (state == RegistrationState.Ok) {
//                            DeviceUtils
//                                    .displayDialogIfDeviceHasPowerManagerThatCouldPreventPushNotifications(
//                                            MainActivity.this);
//
//                            if (getResources().getBoolean(vn.mitek.mifone.R.bool.use_phone_number_validation)) {
//                                AuthInfo authInfo =
//                                        core.findAuthInfo(
//                                                proxyConfig.getRealm(),
//                                                proxyConfig.getIdentityAddress().getUsername(),
//                                                proxyConfig.getDomain());
//                                if (authInfo != null
//                                        && authInfo.getDomain()
//                                        .equals(getString(vn.mitek.mifone.R.string.default_domain))) {
//                                    vn.mitek.mifone.MifoneManager.getInstance().isAccountWithAlias();
//                                }
//                            }
//                            core.setNetworkReachable(true);
//                        }
                    }

                    @Override
                    public void onLogCollectionUploadStateChanged(
                            Core core, Core.LogCollectionUploadState state, String info) {
                        mifoneCoreListener.onLogCollectionUploadStateChanged(state,info);
//                        org.linphone.core.tools.Log.d(
//                                "[Main Activity] Log upload state: "
//                                        + state.toString()
//                                        + ", info = "
//                                        + info);
//                        if (state == Core.LogCollectionUploadState.Delivered) {
//                            ClipboardManager clipboard =
//                                    (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
//                            ClipData clip = ClipData.newPlainText("Logs url", info);
//                            clipboard.setPrimaryClip(clip);
//                            Toast.makeText(
//                                    MainActivity.this,
//                                    getString(vn.mitek.mifone.R.string.logs_url_copied_to_clipboard),
//                                    Toast.LENGTH_SHORT)
//                                    .show();
//                        }
                    }
                };
    }
    private void configAccount(){
        if (mUser==null){
            mifoneCoreListener.onResultConfigAccount(false,"Email and password of user not be configured");
            return;
        }
        if(mInstance==null){
            mifoneCoreListener.onResultConfigAccount(false,"MifoneCore not be initialized");
            return;
        }
        iResponseAPIs.isLoginData(mUser.getUsername(), mUser.getPassword(), mUser.getType())
                .enqueue(new Callback<APIsResponse>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(Call<APIsResponse> call, Response<APIsResponse> response) {
                        APIsResponse result = response.body();
                        if (response.isSuccessful()) {
                            assert result != null;
                            if (result.getCode() == 200) {
                                String secret = result.getSecret();
                                signIn(result, secret);
                                Common.groupId = result.getGroupId();
                                String user_log_id = result.getUser_log_id();
                                List<Privileges> arrayPrivileges = result.getPrivileges();
                                mifoneCoreListener.onResultConfigAccount(true,"Config Successful!");
                            } else {
                                mifoneCoreListener.onResultConfigAccount(false,"Username and Password are wrong! Please check again");
                            }
                        } else {
                            mifoneCoreListener.onResultConfigAccount(false,"Email or password not precision");
                        }
                    }

                    @Override
                    public void onFailure(Call<APIsResponse> call, Throwable t) {
                        Log.e("Error ", t.getMessage());
                        mifoneCoreListener.onResultConfigAccount(false,"Login fail. Please try again");
                    }
                });
    }

    private void reloadDefaultAccountCreatorConfig() {
        org.linphone.core.tools.Log.i("[Assistant] Reloading configuration with default");
        reloadAccountCreatorConfig(MifonePreferences.instance().getDefaultDynamicConfigFile());
    }
    private AccountCreator getAccountCreator() {
        return MifoneManager.getInstance().getAccountCreator();
    }
    private void reloadAccountCreatorConfig(String path) {
        Core core = MifoneManager.getCore();
        if (core != null) {
            core.loadConfigFromXml(path);
            AccountCreator accountCreator = getAccountCreator();
            accountCreator.reset();
            accountCreator.setLanguage(Locale.getDefault().getLanguage());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void signIn(APIsResponse result, String secret) {
        Core core = MifoneManager.getCore();
        if (core != null) {
            reloadDefaultAccountCreatorConfig();
        }

        if (result.getData() == "" || result.getData() == null) {
            //"Information does not exist, \nPlease check again"
        } else {
            DecodeAssistant decodeAssistant = new DecodeAssistant(result.getData());
            JSONObject json = decodeAssistant.decodeDataAssistant();
            if(json == null) {
                //"Unable to access information, \n Please check again!"
                return;
            }
            configMifoneProd(json, secret);
        }
    }


    private void createProxyConfigAndLeaveAssistant(boolean isGenericAccount) {
        Core core = MifoneManager.getCore();
        boolean useMiphoneDefaultValues = defaultDomain.equals(getAccountCreator().getDomain());

        if (isGenericAccount) {
            if (useMiphoneDefaultValues) {
                org.linphone.core.tools.Log.i(
                        "[Assistant] Default domain found for generic connection, reloading configuration");
                core.loadConfigFromXml(MifonePreferences.instance().getMifoneDynamicConfigFile());
            } else {
                org.linphone.core.tools.Log.i("[Assistant] Third party domain found, keeping default values");
            }
        }

        String server = Common.curentUser.getProxy() + ":" + Common.curentUser.getPort();
        ProxyConfig proxyConfig = getAccountCreator().createProxyConfig();
        proxyConfig.setServerAddr(server);

        if (isGenericAccount) {
            if (useMiphoneDefaultValues) {
                // Restore default values
                org.linphone.core.tools.Log.i("[Assistant] Restoring default assistant configuration");
                core.loadConfigFromXml(MifonePreferences.instance().getDefaultDynamicConfigFile());
            } else {

                if (proxyConfig != null) {
                    proxyConfig.setPushNotificationAllowed(false);
                }
                org.linphone.core.tools.Log.w(
                        "[Assistant] Unknown domain used, push probably won't work, enable service mode");
                MifonePreferences.instance().setServiceNotificationVisibility(true);
//                MifoneContext.instance().getNotificationManager().startForeground();
                String mProxy = proxyConfig.getServerAddr();
                proxyConfig.setRoute(mProxy);
            }
        }

        MifonePreferences.instance().firstLaunchSuccessful();
        MifonePreferences.instance()
                .setPushNotificationEnabled(
                        MifonePreferences.instance().isPushNotificationEnabled());
//        goToMifoneActivity();
    }

    private void configureAccountMifone(ProfileUser data, String secret) {
        Common.curentUser = data;
        AccountCreator accountCreator = getAccountCreator();
        accountCreator.setUsername(data.getExtension());
        accountCreator.setPassword(data.getPassword());
        accountCreator.setDomain(data.getDomain());
        if (data.getTransport().equals("TLS")) {
            accountCreator.setTransport(TransportType.Tls);
        } else if (data.getTransport().equals("UDP")) {
            accountCreator.setTransport(TransportType.Udp);
        } else {
            accountCreator.setTransport(TransportType.Tcp);
        }
        createProxyConfigAndLeaveAssistant(true);
        String token = MifonePreferences.instance().getPushNotificationRegistrationID();
        String provider = "android";
        iResponseAPIs.isUpdateTokenFirebase(token, secret, data.getExtension(), provider)
                .enqueue(new Callback<UpdateTokenFirebase>() {
                    @Override
                    public void onResponse(Call<UpdateTokenFirebase> call, Response<UpdateTokenFirebase> response) {
                        org.linphone.core.tools.Log.d("TAG  ", response.body());
                    }

                    @Override
                    public void onFailure(Call<UpdateTokenFirebase> call, Throwable t) {
                        org.linphone.core.tools.Log.d("TAG  ", t.getMessage());
                    }
                });
    }

    private void configMifoneProd(JSONObject json, String secret) {
        try {
            JSONObject jsonObject = json.getJSONObject("data");
            ProfileUser dataRes =
                    new ProfileUser(
                            jsonObject.getString("extension"),
                            jsonObject.getString("password"),
                            jsonObject.getString("domain"),
                            jsonObject.getString("proxy"),
                            jsonObject.getString("port"),
                            jsonObject.getString("transport"));
            SharePrefUtils.getInstance().put("ProfileUser", dataRes);
            configureAccountMifone(dataRes, secret);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
