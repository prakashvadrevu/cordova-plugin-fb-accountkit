/*
 * MIT License
 *
 * Copyright (c) 2016 Prakash Vadrevu
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */
package in.confest.fbaccountkit;

import android.app.Activity;
import android.content.Intent;

import com.facebook.accountkit.AccessToken;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.AccountKitConfiguration.AccountKitConfigurationBuilder;
import com.facebook.accountkit.ui.LoginType;
import com.google.gson.Gson;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;

import org.json.JSONObject;

import java.io.Serializable;

public class FbAccountKit extends CordovaPlugin {

    private static final int LOGIN_REQUEST_CODE = 197345;

    private enum Action {
        PHONE_LOGIN
        // TODO : to be enabled
        /*LOGOUT
        EMAIL_LOGIN,
        IS_LOGGED_IN,
        GET_ACCESS_TOKEN*/
    }

    public enum Status {
        CANCELLED,
        ERROR,
        SUCCESS
    }

    public class FailureResult implements Serializable {

        public Status status;

        public String error;

        public FailureResult(Status status, String error) {
            this.status = status;
            this.error = error;
        }

    }

    public class SuccessResult implements Serializable {

        public Status status = Status.SUCCESS;

        public AccessToken accessToken;

        public String authorizationCode;

        public String finalAuthorizationState;

        public long tokenRefreshIntervalInSeconds;

    }

    private CallbackContext callbackContext;

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);

        AccountKit.initialize(cordova.getActivity().getApplicationContext());
    }

    public boolean execute(String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {
        this.callbackContext = callbackContext;

        if (Action.PHONE_LOGIN.name().equals(action)) {
            context().runOnUiThread(new Runnable() {
                @Override public void run() {
                    login(LoginType.PHONE, args);
                }
            });
        }

        return true;
    }


    private void login(final LoginType loginType, JSONArray args) {
        final Intent intent = new Intent(context(), AccountKitActivity.class);

        // TODO : extend the support to 'TOKEN'
        String responseType = "CODE";

        PluginResult r = new PluginResult(PluginResult.Status.NO_RESULT);
        r.setKeepCallback(true);
        callbackContext.sendPluginResult(r);

        AccountKitConfigurationBuilder configurationBuilder = new AccountKitConfigurationBuilder(
            loginType,
            AccountKitActivity.ResponseType.valueOf(responseType)
        );
        final AccountKitConfiguration configuration = configurationBuilder.build();
        intent.putExtra(
            AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
            configuration);

        this.cordova.setActivityResultCallback(this);
        this.cordova.startActivityForResult(this, intent, LOGIN_REQUEST_CODE);
    }

    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (LOGIN_REQUEST_CODE != requestCode) {
            return;
        }

        final AccountKitLoginResult loginResult = AccountKit.loginResultWithIntent(data);

        if (loginResult == null || loginResult.wasCancelled()) {
            this.callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, new Gson().toJson(new FailureResult(Status.CANCELLED, null))));

        } else if (loginResult.getError() != null) {
            String error = "" + loginResult.getError();
            this.callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, new Gson().toJson(new FailureResult(Status.ERROR, error))));

        } else {
            final AccessToken accessToken = loginResult.getAccessToken();

            final String authorizationCode = loginResult.getAuthorizationCode();
            final long tokenRefreshIntervalInSeconds = loginResult.getTokenRefreshIntervalInSeconds();

            SuccessResult successResult = new SuccessResult();
            successResult.accessToken = accessToken;
            successResult.authorizationCode = authorizationCode;
            successResult.finalAuthorizationState = loginResult.getFinalAuthorizationState();
            successResult.tokenRefreshIntervalInSeconds = loginResult.getTokenRefreshIntervalInSeconds();

            this.callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, new Gson().toJson(successResult)));

        }

    }

    private Activity context() {
        return this.cordova.getActivity();
    }

}
