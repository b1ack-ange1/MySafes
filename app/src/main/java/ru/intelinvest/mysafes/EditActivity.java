package ru.intelinvest.mysafes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ru.intelinvest.mysafes.Additional.SimpleEncryptor;


public class EditActivity extends Activity {
    TextView tvEdit;
    Button editOkBtn;
    long id;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit);
        tvEdit = (TextView)findViewById(R.id.EditText);
        editOkBtn = (Button)findViewById(R.id.btnEditOk);
        Intent intent = getIntent();
        String editText = intent.getStringExtra("textForEdit");
        password = intent.getStringExtra("password");
        id = intent.getLongExtra("id", -1);
        tvEdit.setText(editText);
        Context ctx = this;

        editOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Пишем отредактированные данные в интент и отправляем обратно с результатом ОК
                SimpleEncryptor se = new SimpleEncryptor();
                se.setPassword(password);
                String encString = "";
                String text = tvEdit.getText().toString();
                encString = se.encryptIt(text);
                Intent intent = new Intent();
                intent.putExtra("editedText", encString);
                intent.putExtra("id", id);
                setResult(RESULT_OK, intent);
                finish();
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit, menu);
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
}
