package com.example.aplicacion1.data;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "UserSession";
    private static final String KEY_USER_ROLE = "user_role"; // "ADMINISTRADOR", "CLIENTE", "AUDITOR"
    private final SharedPreferences pref;

    public SessionManager(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public String getUserRole() {
        return pref.getString(KEY_USER_ROLE, "CLIENTE"); // Rol predeterminado
    }

    public void saveUserRole(String role) {
        pref.edit().putString(KEY_USER_ROLE, role).apply();
    }
}