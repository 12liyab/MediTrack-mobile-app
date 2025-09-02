package com.meditrack.auth;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.meditrack.data.AppDatabase;
import com.meditrack.data.model.User;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LocalAuthManager {
    private static final String PREF_NAME = "auth_prefs";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_EMAIL = "user_email";

    private static LocalAuthManager instance;
    private final SharedPreferences prefs;
    private final AppDatabase database;
    private final ExecutorService executor;
    private final MutableLiveData<Boolean> isAuthenticated;

    private LocalAuthManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        database = AppDatabase.getInstance(context);
        executor = Executors.newSingleThreadExecutor();
        isAuthenticated = new MutableLiveData<>(getCurrentUserId() != null);
    }

    public static synchronized LocalAuthManager getInstance(Context context) {
        if (instance == null) {
            instance = new LocalAuthManager(context.getApplicationContext());
        }
        return instance;
    }

    public void signIn(String email, String password, AuthCallback callback) {
        executor.execute(() -> {
            try {
                User user = database.userDao().authenticate(email, password);
                if (user != null) {
                    saveUserSession(user);
                    isAuthenticated.postValue(true);
                    callback.onSuccess();
                } else {
                    callback.onError(new Exception("Invalid credentials"));
                }
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    public void signUp(String email, String password, AuthCallback callback) {
        executor.execute(() -> {
            try {
                // Check if user already exists
                if (database.userDao().getUserByEmail(email) != null) {
                    callback.onError(new Exception("Email already registered"));
                    return;
                }

                // Create new user
                User newUser = new User(email, password);
                long userId = database.userDao().insert(newUser);
                newUser.setId(userId);

                // Save session
                saveUserSession(newUser);
                isAuthenticated.postValue(true);
                callback.onSuccess();
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    public void signOut() {
        prefs.edit()
            .remove(KEY_USER_ID)
            .remove(KEY_USER_EMAIL)
            .apply();
        isAuthenticated.postValue(false);
    }

    public String getCurrentUserId() {
        return prefs.getString(KEY_USER_ID, null);
    }

    public String getCurrentUserEmail() {
        return prefs.getString(KEY_USER_EMAIL, null);
    }

    public LiveData<Boolean> getAuthState() {
        return isAuthenticated;
    }

    private void saveUserSession(User user) {
        prefs.edit()
            .putString(KEY_USER_ID, String.valueOf(user.getId()))
            .putString(KEY_USER_EMAIL, user.getEmail())
            .apply();
    }

    public interface AuthCallback {
        void onSuccess();
        void onError(Exception e);
    }
}
