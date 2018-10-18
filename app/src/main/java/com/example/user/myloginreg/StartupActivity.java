package com.example.user.myloginreg;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class StartupActivity extends AppCompatActivity {

    String usernameStored = "", passwordStored = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                SharedPreferences pref = getSharedPreferences("loginData", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                //editor.clear();  //for bebugging
                //editor.commit(); //for bebugging
                usernameStored = pref.getString("username", null);
                passwordStored = pref.getString("password", null);

                if(usernameStored == null){
                    Intent in = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(in);
                }
                else{
                    Intent in = new Intent(getApplicationContext(), WelcomeActivity.class);
                    String get_user=usernameStored.toString();
                    Bundle bundle=new Bundle();
                    bundle.putString("username", get_user);
                    in.putExtras(bundle);
                    startActivity(in);
                }
                StartupActivity.this.finish();
            }
        }, 3000);
    }
}
