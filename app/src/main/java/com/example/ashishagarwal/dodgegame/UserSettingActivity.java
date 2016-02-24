package com.example.ashishagarwal.dodgegame;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ashish Agarwal on 10-11-2015.
 */

public class UserSettingActivity extends PreferenceActivity implements Preference.OnPreferenceClickListener {

    public static final String MyPREFERENCES = "MyPrefs";



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings);


        findPreference("pause_time").setOnPreferenceClickListener(this);
        findPreference("enemy_size").setOnPreferenceClickListener(this);
        findPreference("window_size").setOnPreferenceClickListener(this);
//        findPreference("challenge").setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        Log.v("FetchFrag", "prefrence " + preference.getKey());
        if (preference.getKey().equals("pause_time")) {

            showMyDialog("Pause");
        }
        if (preference.getKey().equals("enemy_size")) {

            showMyDialog("Enemy_size");

        } if (preference.getKey().equals("window_size")) {

            showMyDialog("Window_size");

        }

        return true;
    }


    private void showMyDialog(final String name) {
        Log.v("FetchFrag", "prefrence and button");
        LayoutInflater factory = LayoutInflater.from(this);
        final EditText et = new EditText(this);
        et.setInputType(2);
        final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,150); // Width , height
        et.setGravity(Gravity.CENTER);
        et.setLayoutParams(lparams);


        SharedPreferences prefs = getSharedPreferences(MyPREFERENCES,MODE_PRIVATE);
        int pauseTime = prefs.getInt(name, 5); //0 is the default value.
        et.setText(String.valueOf(pauseTime));

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle(name);
        //alert.setMessage("Enter your email and password");
        // Set an EditText view to get user input
        alert.setView(et);


        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                String value = et.getText().toString();
                Log.v("FetchFrag", "prefrence and button onclick" + value);


                SharedPreferences.Editor editor = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE).edit();
                editor.putInt(name, Integer.valueOf(value));
                editor.commit();

            }
        });
        alert.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });

        alert.show();
    }

}