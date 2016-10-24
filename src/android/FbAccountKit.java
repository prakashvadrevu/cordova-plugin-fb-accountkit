package in.conect.fbaccountkit;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;

public class FbAccountKit extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {

        if (action.equals("PHONE_LOGIN")) {
            String name = data.getString(0);
            String message = "Plugin coming soon..., responseType : " + name;
            callbackContext.success(message);
            return true;

        } else {
            return false;

        }
    }
}
