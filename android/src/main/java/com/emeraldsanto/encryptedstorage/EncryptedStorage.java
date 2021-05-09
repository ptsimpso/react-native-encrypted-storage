package com.emeraldsanto.encryptedstorage;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EncryptedStorage {

  private final SharedPreferences sharedPreferences;

  public EncryptedStorage(String fileName, Context context) {
    this.sharedPreferences = this.getSharedPreferencesInstance(fileName, context);
  }

  private SharedPreferences getSharedPreferencesInstance(String fileName, Context context) {
    try {
      MasterKey key = new MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build();

      return EncryptedSharedPreferences.create(
        context,
        fileName,
        key,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
      );
    } catch (Exception ex) {
      Log.e("EncryptedStorage", "Failed to create encrypted shared preferences! Failing back to standard SharedPreferences", ex);
      return context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
    }
  }

  private void guardAgainstUnsetSharedPreferences() throws IllegalStateException {
    if (this.sharedPreferences == null) {
      throw new IllegalStateException("The SharedPreferences instance is null");
    }
  }

  private void guardAgainstMalformedKeyValuePair(List<?> pair) throws IllegalArgumentException {
    if (pair.size() != 2) {
      throw new IllegalArgumentException(
        String.format("Expected key/value pair to have length of 2 but was %d", pair.size())
      );
    }
  }

  public boolean setItem(String key, String value) {
    List<List<String>> pair = Collections.singletonList(Arrays.asList(key, value));
    return this.multiSet(pair);
  }

  public boolean multiSet(List<List<String>> keyValuePairs) {
    this.guardAgainstUnsetSharedPreferences();

    SharedPreferences.Editor editor = this.sharedPreferences.edit();

    for (List<String> pair : keyValuePairs) {
      guardAgainstMalformedKeyValuePair(pair);

      editor.putString(pair.get(0), pair.get(1));
    }

    return editor.commit();
  }

  public String getItem(String key) {
    return this.multiGet(Collections.singletonList(key)).get(key);
  }

  public Map<String, String> multiGet(List<String> keys) {
    this.guardAgainstUnsetSharedPreferences();

    Map<String, String> results = new HashMap<>();

    for (String key : keys) {
      results.put(
        key,
        this.sharedPreferences.getString(key, null)
      );
    }

    return results;
  }

  public boolean removeItem(String key) {
    return this.multiRemove(Collections.singletonList(key));
  }

  public boolean multiRemove(List<String> keys) {
    this.guardAgainstUnsetSharedPreferences();

    SharedPreferences.Editor editor = this.sharedPreferences.edit();

    for (String key : keys) {
      editor.remove(key);
    }

    return editor.commit();
  }

  public boolean clear() {
    this.guardAgainstUnsetSharedPreferences();

    return this.sharedPreferences
      .edit()
      .clear()
      .commit();
  }
}
