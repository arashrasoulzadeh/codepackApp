package arashrasoulzadeh.codepack;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import thereisnospon.codeview.CodeView;
import thereisnospon.codeview.CodeViewTheme;

/**
 * Created by arashrasoulzadeh on 8/24/2016 AD.
 * www.mrarash.ir
 */
public class f2 extends AppCompatActivity {

    LayoutInflater inflater;
    String actionTitle = "";
    String actionCode = "";
    String transText = "";
    String transitionName = "";
    String id = "";
    TextView title;
    TextView date, seen;
    CodeView code;
    AlphaAnimation fadein, fadeout;

    SQLiteDatabase sql;
    MyDatabase myDb;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s2);
        myDb = new MyDatabase(this);
        sql = myDb.getWritableDatabase();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        title = (TextView) findViewById(R.id.info_text_large);
        date = (TextView) findViewById(R.id.info_date);
        seen = (TextView) findViewById(R.id.info_seen);


        code = (CodeView) findViewById(R.id.info_code);


        transitionName = getIntent().getStringExtra("TRANS_NAME");
        actionTitle = getIntent().getStringExtra("title");
        actionCode = getIntent().getStringExtra("code");
        id = getIntent().getStringExtra("id");
        transText = getIntent().getStringExtra("TRANS_TEXT");
        seen.setText(getIntent().getStringExtra("view") + " views so far");
        makeTransitions();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            title.setTransitionName(transText);
        }

        title.setText(actionTitle);
        code.setTheme(CodeViewTheme.GITHUB).fillColor();

        code.showCode(actionCode);


        //  code.setText(Html.fromHtml(actionCode));
    }

    private void makeTransitions() {

        fadeout = new AlphaAnimation(1, 0);
        fadeout.setDuration(0);
        fadeout.setFillAfter(true);
        fadein = new AlphaAnimation(0, 1);
        fadein.setDuration(500);
        fadein.setFillAfter(true);
        date.startAnimation(fadeout);
        seen.startAnimation(fadeout);
        code.startAnimation(fadeout);


        fadein.setStartOffset(500);
        date.startAnimation(fadein);
        seen.startAnimation(fadein);
        code.startAnimation(fadein);

    }


    @Override
    public void onBackPressed() {


        super.onBackPressed();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    saveNow();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(f2.this, "Permission denied to write your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void saveNow() {
        String url = "/sdcard/" + id + ".txt";
        try {
            File myFile = new File(url);
            myFile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(myFile);
            OutputStreamWriter myOutWriter =
                    new OutputStreamWriter(fOut);
            myOutWriter.append(actionCode);
            myOutWriter.close();
            fOut.close();
            Toast.makeText(getBaseContext(),
                    "Done writing to " + url,
                    Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void archivePost() {

        try {
            byte[] data = actionTitle.getBytes("UTF-8");
            String at = Base64.encodeToString(data, Base64.DEFAULT);


            data = actionCode.getBytes("UTF-8");
            String ac = Base64.encodeToString(data, Base64.DEFAULT);


            String q = "INSERT or IGNORE into archive values (null,'" + id + "','" + at + "','" + ac + "','')";
            sql.execSQL(q);
            Toast.makeText(f2.this, "ذخیره شد.", Toast.LENGTH_SHORT).show();
        } catch (Exception err) {

        }
    }

    public void saveToHard() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            ActivityCompat.requestPermissions(f2.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        } else {

            saveNow();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("save").setIcon(android.R.drawable.ic_menu_save).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                //saveToHard();
                archivePost();
                return false;
            }
        }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);


        menu.add("close").setIcon(android.R.drawable.ic_menu_close_clear_cancel).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                onBackPressed();
                return false;
            }
        }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);


        return super.onCreateOptionsMenu(menu);
    }
}
