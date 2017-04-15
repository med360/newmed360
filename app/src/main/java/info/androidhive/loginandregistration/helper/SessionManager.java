package info.androidhive.loginandregistration.helper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import java.util.HashMap;

import info.androidhive.loginandregistration.activity.LoginActivity;

public class SessionManager {
	// LogCat tag
	private static String TAG = SessionManager.class.getSimpleName();

	// Shared Preferences
	SharedPreferences pref;

	Editor editor;
	Context _context;

	// Shared pref mode
	int PRIVATE_MODE = 0;

	// Shared preferences file name
	private static final String PREF_NAME = "MED360";
	
	private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

	public static final String KEY_NAME = "name";
	public static final String KEY_EMAIL = "email";

	public SessionManager(Context context) {
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}

	public void setLogin(boolean isLoggedIn) {

		editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);

		// commit changes
		editor.commit();

		Log.d(TAG, "User login session modified!");
	}
	public void createLoginSession(String name, String email, String dob, String nationality, String blood){
		Log.e("login", "beginning of createloginsession method");
		// Storing login value as TRUE
		// Storing name in pref
		editor.putString("name", name);
		editor.putString("email", email);
		editor.putString("dob", dob);
        editor.putString("nationality", nationality);
        editor.putString("blood", blood);
		editor.commit();
		Log.e("login", "all user details added to pref - login session");
	}

	public HashMap<String, String> getUserDetails(){
		Log.e("login", "beginning of getuserdetails method");
		HashMap<String, String> user = new HashMap<String, String>();
		// user name
		user.put("name", pref.getString(KEY_NAME, null));

		// user email id
		user.put("email", pref.getString(KEY_EMAIL, null));

		user.put("dob", pref.getString("dob", null));

        user.put("nationality", pref.getString("nationality", null));
        user.put("blood", pref.getString("blood", null));

		Log.e("login", "added all user details in session to hashmap when getuserdetails method called");

		// return user
		return user;

	}

	public void logoutUser(){
		// Clearing all data from Shared Preferences
		editor.clear();
		editor.commit();

		// After logout redirect user to Loing Activity
		Intent i = new Intent(_context, LoginActivity.class);
		// Closing all the Activities
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		// Add new Flag to start new Activity
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		// Staring Login Activity
		_context.startActivity(i);
	}


	public boolean isLoggedIn(){
		return pref.getBoolean(KEY_IS_LOGGED_IN, false);
	}
}
