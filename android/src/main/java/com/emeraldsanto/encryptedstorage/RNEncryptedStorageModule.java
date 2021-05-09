package com.emeraldsanto.encryptedstorage;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableNativeMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableNativeArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RNEncryptedStorageModule extends ReactContextBaseJavaModule {

    private static final String NATIVE_MODULE_NAME = "RNEncryptedStorage";
    private static final String SHARED_PREFERENCES_FILENAME = "RN_ENCRYPTED_STORAGE_SHARED_PREF";

    private final EncryptedStorage storage;

    public RNEncryptedStorageModule(ReactApplicationContext context) {
        super(context);

        this.storage = new EncryptedStorage(RNEncryptedStorageModule.SHARED_PREFERENCES_FILENAME, context);
    }

    @NonNull
    @Override
    public String getName() {
        return RNEncryptedStorageModule.NATIVE_MODULE_NAME;
    }

    @ReactMethod
    public void setItem(String key, String value, Promise promise) {
      try {
        boolean success = this.storage.setItem(key, value);

        if (success) {
          promise.resolve(value);
        }

        else {
          promise.reject(new Exception(String.format("An error occurred while saving %s", key)));
        }
      } catch (Exception ex) {
        promise.reject(ex);
      }
    }

    @ReactMethod
    public void multiSet(ReadableArray keyValuePairs, Promise promise) {
      List<List<String>> pairs = new ArrayList<>();

      for (Object pair : keyValuePairs.toArrayList()) {
        if (!(pair instanceof ReadableArray) || ((ReadableArray) pair).size() != 2) {
          // TODO IllegalArgumentException
        }

        else if (((ReadableArray) pair).size() != 2) {
          // TODO IllegalArgumentException
        }

        // TODO Check each element for type/length (IllegalArgumentException)

        // TODO Add pair to `pairs` list
      }

      try {
        boolean success = this.storage.multiSet(pairs);

        if (success) {
//          promise.resolve();
        }

        else {
//          promise.reject();
        }
      } catch (Exception ex) {
        promise.reject(ex);
      }
    }

    @ReactMethod
    public void multiGet(ReadableArray keys, Promise promise) {
//      if (this.sharedPreferences == null) {
//        promise.reject(new NullPointerException("Could not initialize SharedPreferences"));
//        return;
//      }
//
//      WritableArray values = new WritableNativeArray();
//
//      for (Object key : keys.toArrayList()) {
//        if (!(key instanceof String)) {
//          promise.reject(
//            new UnsupportedOperationException(
//              String.format(
//                "Unsupported key type, expected String but received {0}",
//                key == null ? "null" : key.getClass().getName()
//              )
//            )
//          );
//
//          return;
//        }
//
//        WritableArray pair = new WritableNativeArray();
//
//        pair.pushString((String) key);
//        pair.pushString(this.sharedPreferences.getString((String) key, null));
//
//        values.pushArray(pair);
//      }
//
//      promise.resolve(values);
    }

    @ReactMethod
    public void removeItem(String key, Promise promise) {
//        if (this.sharedPreferences == null) {
//            promise.reject(new NullPointerException("Could not initialize SharedPreferences"));
//            return;
//        }
//
//        SharedPreferences.Editor editor = this.sharedPreferences.edit();
//        editor.remove(key);
//        boolean saved = editor.commit();
//
//        if (saved) {
//            promise.resolve(key);
//        }
//
//        else {
//            promise.reject(new Exception(String.format("An error occured while removing %s", key)));
//        }
    }

    @ReactMethod
    public void clear(Promise promise) {
//        if (this.sharedPreferences == null) {
//            promise.reject(new NullPointerException("Could not initialize SharedPreferences"));
//            return;
//        }
//
//        SharedPreferences.Editor editor = this.sharedPreferences.edit();
//        editor.clear();
//        boolean saved = editor.commit();
//
//        if (saved) {
//            promise.resolve(null);
//        }
//
//        else {
//            promise.reject(new Exception("An error occured while clearing SharedPreferences"));
//        }
    }
}
