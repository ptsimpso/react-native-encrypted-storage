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
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableNativeArray;

public class RNEncryptedStorageModule extends ReactContextBaseJavaModule {

    private static final String NATIVE_MODULE_NAME = "RNEncryptedStorage";
    private static final String SHARED_PREFERENCES_FILENAME = "RN_ENCRYPTED_STORAGE_SHARED_PREF";

    private SharedPreferences sharedPreferences;

    public RNEncryptedStorageModule(ReactApplicationContext context) {
        super(context);

        try {
            MasterKey key = new MasterKey.Builder(context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();

            this.sharedPreferences = EncryptedSharedPreferences.create(
                context,
                RNEncryptedStorageModule.SHARED_PREFERENCES_FILENAME,
                key,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        }

        catch (Exception ex) {
            Log.e(NATIVE_MODULE_NAME, "Failed to create encrypted shared preferences! Failing back to standard SharedPreferences", ex);
            this.sharedPreferences = context.getSharedPreferences(RNEncryptedStorageModule.SHARED_PREFERENCES_FILENAME, Context.MODE_PRIVATE);
        }
    }

    @NonNull
    @Override
    public String getName() {
        return RNEncryptedStorageModule.NATIVE_MODULE_NAME;
    }

    @ReactMethod
    public void setItem(String key, String value, Promise promise) {
        if (this.sharedPreferences == null) {
            promise.reject(new NullPointerException("Could not initialize SharedPreferences"));
            return;
        }

        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putString(key, value);
        boolean saved = editor.commit();

        if (saved) {
            promise.resolve(value);
        }

        else {
            promise.reject(new Exception(String.format("An error occurred while saving %s", key)));
        }
    }

    @ReactMethod
    public void multiGet(ReadableArray keys, Promise promise) {
      if (this.sharedPreferences == null) {
        promise.reject(new NullPointerException("Could not initialize SharedPreferences"));
        return;
      }

      WritableArray values = new WritableNativeArray();

      for (Object key : keys.toArrayList()) {
        if (!(key instanceof String)) {
          promise.reject(
            new UnsupportedOperationException(
              String.format(
                "Unsupported key type, expected String but received {0}",
                key == null ? "null" : key.getClass().getName()
              )
            )
          );

          return;
        }

        WritableArray pair = new WritableNativeArray();

        pair.pushString((String) key);
        pair.pushString(this.sharedPreferences.getString((String) key, null));

        values.pushArray(pair);
      }

      promise.resolve(values);
    }

    @ReactMethod
    public void removeItem(String key, Promise promise) {
        if (this.sharedPreferences == null) {
            promise.reject(new NullPointerException("Could not initialize SharedPreferences"));
            return;
        }

        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.remove(key);
        boolean saved = editor.commit();

        if (saved) {
            promise.resolve(key);
        }

        else {
            promise.reject(new Exception(String.format("An error occured while removing %s", key)));
        }
    }

    @ReactMethod
    public void clear(Promise promise) {
        if (this.sharedPreferences == null) {
            promise.reject(new NullPointerException("Could not initialize SharedPreferences"));
            return;
        }

        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.clear();
        boolean saved = editor.commit();

        if (saved) {
            promise.resolve(null);
        }

        else {
            promise.reject(new Exception("An error occured while clearing SharedPreferences"));
        }
    }
}
