package ru.intelinvest.mysafes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ru.intelinvest.mysafes.Additional.SimpleEncryptor;


public class SettingsActivity extends Activity {
    final String PASSWORD = "PASSWORD";
    final String FILE = "mySafes";
    EditText etNewPass, etOldPass, etConfNewPass;
    Button btnConfirm;
    Context ctx;
    SharedPreferences sPref;
    String storedPass;
    SimpleEncryptor se;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        sPref = getSharedPreferences(FILE, MODE_PRIVATE);
        storedPass = sPref.getString(PASSWORD, "");
        Log.d("myLogs", "StoredPass "+storedPass);
        ctx = this;
        se = new SimpleEncryptor();
        etOldPass = (EditText)findViewById(R.id.etOldPass);
        etNewPass = (EditText)findViewById(R.id.etNewPass1);
        etConfNewPass = (EditText)findViewById(R.id.etNewPass2);
        btnConfirm = (Button)findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                se.setPassword(etOldPass.getText().toString());
                String newPass = etNewPass.getText().toString();
                String oldPass = etOldPass.getText().toString();
                String confNewPass = etConfNewPass.getText().toString();
                se.setPassword(etOldPass.getText().toString());
                String decryptedPass = se.decryptIt(storedPass);
                Log.d("myLogs", "DecryptedPass "+decryptedPass);
                if(decryptedPass.equals(oldPass)){
                    if(!newPass.equals(confNewPass)){
                        Toast.makeText(ctx, "Passwords doesn't match", Toast.LENGTH_SHORT).show();
                    } else if(newPass.equals(oldPass)){
                        Toast.makeText(ctx, "New password should be different from old password", Toast.LENGTH_SHORT).show();
                    } else {
                        String encPassword = "";
                        String password = etNewPass.getText().toString();
                        Log.d("myLogs", "newPassword "+password);
                        se.setPassword(password);
                        encPassword = se.encryptIt(password);
                        Log.d("myLogs", "newENCPassword "+encPassword);
                        sPref = getSharedPreferences(FILE, MODE_PRIVATE);
                        SharedPreferences.Editor ed = sPref.edit();
                        ed.putString(PASSWORD, encPassword);
                        ed.commit();
                        Intent intent = new Intent();
                        intent.putExtra("password", password);
                        intent.putExtra("oldPassword", oldPass);
                        setResult(RESULT_OK, intent);
                        Toast.makeText(ctx, "New password have been saved", Toast.LENGTH_SHORT).show();
                        finish();

                    }
                }

            }
        });

    }

}
