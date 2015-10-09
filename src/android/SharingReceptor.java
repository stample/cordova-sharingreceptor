package org.apache.cordova.sharingreceptor;

import java.lang.RuntimeException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;

import org.apache.cordova.CordovaActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.net.Uri;
import android.text.Html;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaResourceApi;
import org.apache.cordova.PluginResult;

import android.util.Log;

/**
 *
 * @author lorber.sebastien@gmail.com
 */
public class SharingReceptor extends CordovaPlugin {

    // Constant that holds all the intent actions that we will handle.
    private static final Set<String> SEND_INTENTS = new HashSet<String>(Arrays.asList(
            Intent.ACTION_SEND,
            Intent.ACTION_SEND_MULTIPLE
    ));

    private static boolean isSendIntent(Intent intent) {
        return SEND_INTENTS.contains(intent.getAction());
    }

    private static JSONObject serializeIntent(Intent intent) throws JSONException {
        JSONObject extras = SharingReceptor.serializeBundle(intent.getExtras());
        JSONObject intentJson = new JSONObject();
        intentJson.put("action", intent.getAction());
        intentJson.put("extras", extras);
        JSONObject result = new JSONObject();
        result.put("platform", "android");
        result.put("intent", intentJson);
        return result;
    }

    // See http://stackoverflow.com/a/21859000/82609
    private static JSONObject serializeBundle(Bundle bundle) throws JSONException {
        JSONObject json = new JSONObject();
        if ( bundle != null ) {
            Set<String> keys = bundle.keySet();
            for (String key : keys) {
                try {
                    json.put(key, JSONObject.wrap(bundle.get(key)));
                } catch(JSONException e) {
                    throw new RuntimeException("Can't serialize bundle for key = " + key,e);
                }
            }
        }
        return json;
    }

    /////////////////////////////////////////////////////////////////////////////////////////:
    /////////////////////////////////////////////////////////////////////////////////////////:
    /////////////////////////////////////////////////////////////////////////////////////////:



    private CallbackContext listenerCallback = null;
    private String TAG = this.getClass().getSimpleName();


    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {
        try {
            if ( action.equals("listen") ) {
                installListener(callbackContext);
                return true;
            }
            else {
                callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.INVALID_ACTION,"action ["+action+"] does not exist"));
                return false;
            }
        }
        catch (Exception e) {
            Log.e(TAG,"Error while executing action ["+action+"]",e);
            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR,e.getMessage()));
            return false;
        }
    }



    private void installListener(CallbackContext callbackContext) {
        if ( this.listenerCallback != null ) {
            throw new RuntimeException("You already set a listener: it does not make sense to reset it.");
        }
        this.listenerCallback = callbackContext;

        // handle the case where the app is started by a new intent:
        // we publish the current intent (if needed) directly after the callback registration
        this.maybePublishIntent(this.cordova.getActivity().getIntent());
    }

    // handle the case when the app is already in background, and is "awakened" by a new intent sent from another app
    @Override
    public void onNewIntent(Intent intent) {
        Log.i(TAG, "onNewIntent -> " + intent);
        this.maybePublishIntent(intent);

        // TODO would it be useful to replace current activity intent by new intent? Can it messes things up?
    }




    // We try to publish in the JS callback the intent data, if the intent is a send intent, and if the callback was correctly setup
    private void maybePublishIntent(Intent intent) {
        if ( !SharingReceptor.isSendIntent(intent) ) {
            Log.i(TAG, "maybePublishIntent -> not publishing intent because the action name is not part of SEND_INTENTS=" + SEND_INTENTS);
        }
        else if ( this.listenerCallback == null ) {
            Log.w(TAG, "maybePublishIntent -> not publishing intent because listener callback not set");
        }
        else {

            JSONObject intentJson;
            try {
                intentJson = SharingReceptor.serializeIntent(intent);
            } catch (Exception e) {
                throw new RuntimeException("Can't serialize intent " + intent,e);
            }
            Log.i(TAG, "maybePublishIntent -> will publish intent -> " + intentJson.toString());
            PluginResult result = new PluginResult(PluginResult.Status.OK, intentJson);
            result.setKeepCallback(true);
            this.listenerCallback.sendPluginResult(result);
        }
    }


}
