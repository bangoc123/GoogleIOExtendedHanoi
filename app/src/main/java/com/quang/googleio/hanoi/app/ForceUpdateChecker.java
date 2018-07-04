package com.quang.googleio.hanoi.app;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.quang.googleio.hanoi.BuildConfig;

public class ForceUpdateChecker {
    private static final String TAG = ForceUpdateChecker.class.getSimpleName();

    public static final String KEY_UPDATE_REQUIRED = "force_update_required";
    public static final String KEY_CURRENT_VERSION = "force_update_current_version";
    public static final String KEY_UPDATE_URL = "force_update_store_url";

    private OnUpdateNeededListener onUpdateNeededListener;
    private Context context;

    public interface OnUpdateNeededListener {
        void onUpdateNeeded(String updateUrl);
    }

    public static Builder with(@NonNull Context context) {
        return new Builder(context);
    }

    public ForceUpdateChecker(@NonNull Context context,
                              OnUpdateNeededListener onUpdateNeededListener) {
        this.context = context;
        this.onUpdateNeededListener = onUpdateNeededListener;
    }

    public void check() {
        final FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();

        if (remoteConfig.getBoolean(KEY_UPDATE_REQUIRED)) {
            long currentVersion = remoteConfig.getLong(KEY_CURRENT_VERSION);
            Log.d("ServerVersion", remoteConfig.getBoolean(KEY_UPDATE_REQUIRED) + " - " + currentVersion + " - " + remoteConfig.getString(KEY_UPDATE_URL));
            String updateUrl = remoteConfig.getString(KEY_UPDATE_URL);

            if (currentVersion > BuildConfig.VERSION_CODE
                    && onUpdateNeededListener != null) {
                onUpdateNeededListener.onUpdateNeeded(updateUrl);
            }
        }
    }

    public static class Builder {

        private Context context;
        private OnUpdateNeededListener onUpdateNeededListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder onUpdateNeeded(OnUpdateNeededListener onUpdateNeededListener) {
            this.onUpdateNeededListener = onUpdateNeededListener;
            return this;
        }

        public ForceUpdateChecker build() {
            return new ForceUpdateChecker(context, onUpdateNeededListener);
        }

        public ForceUpdateChecker check() {
            ForceUpdateChecker forceUpdateChecker = build();
            forceUpdateChecker.check();

            return forceUpdateChecker;
        }
    }
}
