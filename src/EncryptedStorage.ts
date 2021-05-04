/* eslint-disable no-dupe-class-members */

import { NativeModules } from 'react-native';
const { RNEncryptedStorage } = NativeModules;

if (!RNEncryptedStorage) {
  throw new Error('RNEncryptedStorage is undefined');
}

export type StorageErrorCallback = (error?: Error) => void;
export type StorageValueCallback = (error?: Error, value?: string) => void;

export default class EncryptedStorage {
  /**
   * Writes data to the disk, using SharedPreferences or KeyChain, depending on the platform.
   * @param {string} key - A string that will be associated to the value for later retrieval.
   * @param {string} value - The data to store.
   */
  static setItem(key: string, value: string): Promise<void>;

  /**
   * Writes data to the disk, using SharedPreferences or KeyChain, depending on the platform.
   * @param {string} key - A string that will be associated to the value for later retrieval.
   * @param {string} value - The data to store.
   * @param {Function} cb - The function to call when the operation completes.
   */
  static setItem(key: string, value: string, cb: StorageErrorCallback): void;
  static setItem(
    key: string,
    value: string,
    cb?: StorageErrorCallback
  ): void | Promise<void> {
    if (cb) {
      RNEncryptedStorage.setItem(key, value).then(cb).catch(cb);
      return;
    }

    return RNEncryptedStorage.setItem(key, value);
  }

  /**
   * Retrieves data from the disk, using SharedPreferences or KeyChain, depending on the platform and returns it as the specified type.
   * @param {string} key - A string that is associated to a value.
   */
  static getItem(key: string): Promise<string | null>;

  /**
   * Retrieves data from the disk, using SharedPreferences or KeyChain, depending on the platform and returns it as the specified type.
   * @param {string} key - A string that is associated to a value.
   * @param {Function} cb - The function to call when the operation completes.
   */
  static getItem(key: string, cb: StorageValueCallback): void;
  static getItem(
    key: string,
    cb?: StorageValueCallback
  ): void | Promise<string | null> {
    if (cb) {
      EncryptedStorage.multiGet([key])
        .then((pairs) => cb(undefined, pairs[0]?.[1] ?? undefined))
        .catch(cb);

      return;
    }

    return EncryptedStorage.multiGet([key]).then((pairs) => pairs[0]?.[1]);
  }

  static multiGet(keys: Array<string>): Promise<Array<[string, string | null]>>;
  static multiGet(
    keys: Array<string>,
    cb: (error?: Error, value?: Array<[string | null]>) => void
  ): void;
  static multiGet(
    keys: Array<string>,
    cb?: (error?: Error, value?: Array<[string | null]>) => void
  ) {
    if (cb) {
      RNEncryptedStorage.multiGet(keys).then(cb).catch(cb);
      return;
    }

    return RNEncryptedStorage.multiGet(keys);
  }

  /**
   * Deletes data from the disk, using SharedPreferences or KeyChain, depending on the platform.
   * @param {string} key - A string that is associated to a value.
   */
  static removeItem(key: string): Promise<void>;

  /**
   * Deletes data from the disk, using SharedPreferences or KeyChain, depending on the platform.
   * @param {string} key - A string that is associated to a value.
   * @param {Function} cb - The function to call when the operation completes.
   */
  static removeItem(key: string, cb: StorageErrorCallback): void;
  static removeItem(
    key: string,
    cb?: StorageErrorCallback
  ): void | Promise<void> {
    if (cb) {
      RNEncryptedStorage.removeItem(key).then(cb).catch(cb);
      return;
    }

    return RNEncryptedStorage.removeItem(key);
  }

  /**
   * Clears all data from disk, using SharedPreferences or KeyChain, depending on the platform.
   */
  static clear(): Promise<void>;

  /**
   * Clears all data from disk, using SharedPreferences or KeyChain, depending on the platform.
   * @param {Function} cb - The function to call when the operation completes.
   */
  static clear(cb: StorageErrorCallback): void;
  static clear(cb?: StorageErrorCallback): void | Promise<void> {
    if (cb) {
      RNEncryptedStorage.clear().then(cb).catch(cb);
      return;
    }

    return RNEncryptedStorage.clear();
  }
}
