package ru.intelinvest.mysafes.Additional;

import android.view.View;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import ru.intelinvest.mysafes.R;

/**
 * Created by Stanislav on 07.01.2015.
 */
public class MyViewBinder implements SimpleAdapter.ViewBinder {

    @Override
    public boolean setViewValue(View view, Object data,
                                String textRepresentation) {
        String decoded = "";
        switch (view.getId()) {
            // LinearLayout
            case R.id.tvInfo:
                SimpleEncryptor se = new SimpleEncryptor();
                se.setPassword("12345678");
                decoded = se.decryptIt((String)data);
                ((TextView)view).setText(decoded);
                return true;

        }
        return false;
    }
}
