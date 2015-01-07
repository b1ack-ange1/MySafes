package ru.intelinvest.mysafes;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import ru.intelinvest.mysafes.Additional.SimpleEncryptor;


public class MainActivity extends Activity implements View.OnClickListener {
    final String myLogs = "myLogs";
    final String PASSWORD = "PASSWORD";
    final String FILE = "mySafes";
    EditText etPassword, etConfPass;
    TextView tvMainInfo, tvConfInfo;
    Button btnAccess;
    DbConnector dbConnector;
    ListView lv;
    SharedPreferences sPref;
    String storedPass = "";
    String password = "";
    String decryptedPass = "";
    SimpleEncryptor se;
    private int FIRST_RUN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        se = new SimpleEncryptor();
        tvMainInfo = (TextView)findViewById(R.id.tvMainInfo);
        tvConfInfo = (TextView)findViewById(R.id.tvMainConfirm);
        etConfPass = (EditText)findViewById(R.id.etConfPass);
        btnAccess = (Button) findViewById(R.id.btnAccess);
        etPassword = (EditText) findViewById(R.id.etPassword);
        password = etPassword.getText().toString();
        sPref = getSharedPreferences(FILE, MODE_PRIVATE);
        storedPass = sPref.getString(PASSWORD, "");
        if(storedPass.equals("")){
            tvMainInfo.setText("The program is run for the first time, please setup a master password.");
        } else {
            etConfPass.setWidth(0);
            etConfPass.setHeight(0);
            tvConfInfo.setText("");
            FIRST_RUN = 0;
            Log.d("myLogs", "first run false "+FIRST_RUN);
        }
        dbConnector = new DbConnector(this);
        btnAccess.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(FIRST_RUN == 1){
            String encPassword = "";
            String confPassword = etConfPass.getText().toString();
            password = etPassword.getText().toString();
            if(password.equals(confPassword)){
                se = new SimpleEncryptor();
                se.setPassword(password);
                encPassword = se.encryptIt(password);
                sPref = getSharedPreferences(FILE, MODE_PRIVATE);
                SharedPreferences.Editor ed = sPref.edit();
                ed.putString(PASSWORD, encPassword);
                boolean done = ed.commit();
                if(done){
                    FIRST_RUN = 0;
                    Log.d("myLogs", "first run success ");
                    Intent intent = new Intent(this, MySafesActivity.class);
                    intent.putExtra("password", password);
                    startActivity(intent);
                }
            } else {
                Toast.makeText(this, "Passwords doesn't match"+ password +" <--> "+confPassword, Toast.LENGTH_SHORT).show();
            }
        } else if (FIRST_RUN ==0){
            password = etPassword.getText().toString();
            se.setPassword(etPassword.getText().toString());
            Log.d("myLogs", ""+se.decryptIt(storedPass)+ " <--> "+password + " -> "+storedPass);
            if (se.decryptIt(storedPass).equals(password)) {
                Intent intent = new Intent(this, MySafesActivity.class);
                intent.putExtra("password", password);
                startActivity(intent);
            }
        }
    }
}
