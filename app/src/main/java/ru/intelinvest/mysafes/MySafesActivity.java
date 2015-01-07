package ru.intelinvest.mysafes;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

import ru.intelinvest.mysafes.Additional.MyViewBinder;
import ru.intelinvest.mysafes.Additional.SimpleEncryptor;


public class MySafesActivity extends Activity {
    private static final String DB_TABLE = "crypto";
    final String ATTRIBUTE_NAME_TEXT = "str";
    final String COLUMN_STR = "str";
    final String ATTRIBUTE_NAME_CHECKED = "checked";
    public static final String COLUMN_ID = "_id";
    final int ADD_RQ = 1;
    final int EDIT_RQ = 2;
    final int PASSCHANGE_RQ = 3;
    final String myLogs = "myLogs";
    final int CM_DELETE_ID = 1;
    final int CM_EDIT_ID = 2;
    ListView lv;
    Button btnAdd;
    Context ctx;
    DbConnector dbconn;
    SQLiteDatabase db;
    int selectedID=-1;
    ArrayList<Map<String, Object>> data;
    SimpleAdapter sAdapter;
    SimpleCursorAdapter scAdapter;
    Cursor cursor;
    TextView tvInfo;
    String password = "";
    SimpleEncryptor se;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_safes);
        dbconn = new DbConnector(this);
        db = dbconn.getWritableDatabase();
        lv = (ListView) findViewById(R.id.lvItems);

        Intent intent = getIntent();
        password = intent.getStringExtra("password");
        se = new SimpleEncryptor();
        se.setPassword(password);
        cursor = db.query("crypto", null, null, null, null, null, "_id");
        startManagingCursor(cursor);
        // массив имен атрибутов, из которых будут читаться данные
        String[] from = { ATTRIBUTE_NAME_TEXT };
        // массив ID View-компонентов, в которые будут вставлять данные
        int[] to = { R.id.tvInfo};

        // создаем адаптер
        scAdapter = new SimpleCursorAdapter(this, R.layout.item, cursor, from, to);
        MyViewBinder myBinder = new MyViewBinder();
        scAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                String decoded = "";
                String value = "";
                value = cursor.getString(columnIndex);
                //Log.d(myLogs, value);


                switch (view.getId()) {
                    case R.id.tvInfo:
                        decoded = se.decryptIt(value);
                        ((TextView)view).setText(decoded);
                        return true;

                }
                return false;
            }
        });
        lv.setAdapter(scAdapter);
        registerForContextMenu(lv);

        ctx = this;
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, AddActivity.class);
                intent.putExtra("password", password);
                startActivityForResult(intent, ADD_RQ);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(0, CM_DELETE_ID, 0, "Delete");
        menu.add(0, CM_EDIT_ID, 0, "Edit");
        super.onCreateContextMenu(menu, v, menuInfo);
    }


    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if (item.getItemId() == CM_DELETE_ID) {
            // получаем инфу о пункте списка
            Log.d("myLogs", "id "+acmi.id);
            // уведомляем, что данные изменились
            delRec(acmi.id);
            cursor.requery();
            return true;
        } else if (item.getItemId() ==CM_EDIT_ID){
            Intent intent = new Intent(this, EditActivity.class);
            intent.putExtra("id", acmi.id);
            TextView tvForEdit = (TextView)acmi.targetView.findViewById(R.id.tvInfo);
            intent.putExtra("textForEdit", tvForEdit.getText().toString());
            intent.putExtra("password", password);
            startActivityForResult(intent, EDIT_RQ);
        }

        return super.onContextItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_safes, menu);
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
            //Intent intent = new Intent(this, SettingsActivity.class);
            //startActivityForResult(intent, PASSCHANGE_RQ);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {return;}
        if(requestCode == ADD_RQ) {
            String textData = data.getStringExtra("data");
            Log.d(myLogs, textData);
            ContentValues cv = new ContentValues();
            cv.put("str", textData);
            long IDs = db.insert("crypto", null, cv);
            Log.d(myLogs, "row inserted " + IDs);
            cursor.requery();
        } else if(requestCode == EDIT_RQ){
            String editedText = data.getStringExtra("editedText");
            long id = data.getLongExtra("id", -1);
            Log.d(myLogs, editedText);
            ContentValues cv = new ContentValues();
            cv.put("str", editedText);
            long IDs = db.update(DB_TABLE, cv, "_id = "+id, null);
            Log.d(myLogs, "row updated " + IDs);
            cursor.requery();
        } else if(requestCode == PASSCHANGE_RQ){
            Toast.makeText(ctx, "New password have been saved", Toast.LENGTH_SHORT).show();
            String newPassword = data.getStringExtra("password");
            Log.d(myLogs, "new pass "+newPassword);
            String oldPassword = data.getStringExtra("oldPassword");
            se.setPassword(oldPassword);
            Log.d(myLogs, "oldPas "+oldPassword);
            ArrayList<String> oldValues = new ArrayList<>();
            ArrayList<String> newValues = new ArrayList<>();
            Cursor c = db.query(DB_TABLE, null, null, null, null, null, null);
            if(c.moveToFirst()){
                do{
                    String oldValue = c.getString(1);
                    String encValue = se.decryptIt(oldValue);
                    oldValues.add(encValue);
                    Log.d(myLogs, "value "+oldValue);
                    Log.d(myLogs, "decrypting " + encValue);
                } while (c.moveToNext());
            }
            se.setPassword(newPassword);
            for(int i = 0; i < oldValues.size(); i++){
                String newValue = se.encryptIt(oldValues.get(i));
                Log.d(myLogs, "newValue "+newValue);
                newValues.add(newValue);
            }
            int res = 0;
            for(int i = 0; i< newValues.size(); i++){
                ContentValues cv = new ContentValues();
                cv.put("str", newValues.get(i));
                res += db.update(DB_TABLE, cv, "str = '"+oldValues.get(i)+"'", null);
            }
            Log.d(myLogs, "rows updated "+res);

        }
    }


    // получить все данные из таблицы DB_TABLE
    public Cursor getAllData() {
        return db.query(DB_TABLE, null, null, null, null, null, "_id");
    }

    // удалить запись из DB_TABLE
    public void delRec(long id) {
        db.delete(DB_TABLE, COLUMN_ID + " = " + id, null);
    }

    // добавить запись в DB_TABLE
    public void addRec(String value) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_STR, value);
        db.insert(DB_TABLE, null, cv);
    }


















}
