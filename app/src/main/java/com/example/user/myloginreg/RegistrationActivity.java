package com.example.user.myloginreg;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;



import android.net.Uri;
import android.os.AsyncTask;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegistrationActivity extends AppCompatActivity {
    private Button btnReg;
    private TextView tvLogin;

    private EditText etName, etHobby, etUsername, etPassword, etRePassword;

    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        btnReg = (Button) findViewById(R.id.btnReg);
        tvLogin = (TextView) findViewById(R.id.tvlogin);

        etName = (EditText) findViewById(R.id.etname);
        etHobby = (EditText) findViewById(R.id.ethobby);
        etUsername = (EditText) findViewById(R.id.etusername);
        etPassword = (EditText) findViewById(R.id.etpassword);
        etRePassword = (EditText) findViewById(R.id.etrepassword);

        tvLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
        btnReg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }
    private void register(){
        String name = etName.getText().toString();
        String hobby = etHobby.getText().toString();
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        String repassword = etRePassword.getText().toString();

        if(!(password.equals(repassword))){
            displayToast("The passwords do not match");
            etPassword.setText("");
            etRePassword.setText("");}
        if(name.equals("") || hobby.equals("") || username.equals("") || password.equals("")){
            displayToast("Please fill out all the fields");
        }
        else{
            String type="register";
            BackgroundWorker backgroundworker=new BackgroundWorker(this);
            backgroundworker.execute(type, name, hobby, username, password);

        }


    }

    public class BackgroundWorker extends AsyncTask<String, Void, String>{
        Context context;
        AlertDialog alertDialog;
        ProgressDialog pdLoading = new ProgressDialog(RegistrationActivity.this);
        BackgroundWorker (Context ctx) { context = ctx; }
        @Override
        protected String doInBackground(String... params) {
            String type = params[0];
            String register_url = "http://mkadamba.hostingerapp.com/register.php";
            if(type.equals("register")){
                try {
                    String db_name= params[1];
                    String db_hobby = params[2];
                    String db_username = params[3];
                    String db_password = params[4];
                    URL url = new URL(register_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("dbname", "UTF-8")+"="+URLEncoder.encode(db_name, "UTF-8")+"&"+
                            URLEncoder.encode("dbhobby", "UTF-8")+"="+URLEncoder.encode(db_hobby, "UTF-8")+"&"+
                            URLEncoder.encode("dbusername", "UTF-8")+"="+URLEncoder.encode(db_username, "UTF-8")+"&"+
                            URLEncoder.encode("dbpassword", "UTF-8")+"="+URLEncoder.encode(db_password, "UTF-8");
                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    InputStream inputStream=httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                    String result="";
                    String line="";
                    while((line = bufferedReader.readLine())!= null){
                        result += line;
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return result;
                } catch (MalformedURLException e) {

                    e.printStackTrace();
                } catch (IOException e) {

                    e.printStackTrace();
                }

            }
            return null;
        }
        @Override
        protected void onPreExecute() {
            pdLoading.setMessage("Please wait...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }
        @Override
        protected void onPostExecute(String result) {
            pdLoading.dismiss();
            Toast.makeText(RegistrationActivity.this, result, Toast.LENGTH_LONG).show();

            if(result.equals("Register successful")){
                Intent intent = new Intent(RegistrationActivity.this,LoginActivity.class);
                startActivity(intent);
                RegistrationActivity.this.finish();
            }
        }
        @Override
        protected void onProgressUpdate(Void... values){
            super.onProgressUpdate(values);
        }


    }

    private void displayToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}
