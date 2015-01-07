package ru.intelinvest.mysafes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import ru.intelinvest.mysafes.Additional.SimpleEncryptor;


public class AddActivity extends Activity implements View.OnClickListener{
    EditText addText;
    Button btnOk;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_activity);
        Intent intent = getIntent();
        password = intent.getStringExtra("password");
        addText = (EditText) findViewById(R.id.addText);
        btnOk = (Button)findViewById(R.id.btnOk);
        btnOk.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        SimpleEncryptor se = new SimpleEncryptor();
        se.setPassword(password);
        String encString = "";
        String text = addText.getText().toString();
        encString = se.encryptIt(text);
        Log.d("myLogs", encString);

        Intent intent = new Intent();
        intent.putExtra("data", encString);
        setResult(RESULT_OK, intent);
        finish();
    }
}
