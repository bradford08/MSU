package com.springlabs.msulife;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;



public class AboutActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("О приложении");

        TextView msuLink= (TextView) findViewById(R.id.msuLink);
        TextView slLink= (TextView) findViewById(R.id.slLink);

        String msu = "<a href=\"" + "http://msulife.com/" + "\">"
                + "http://msulife.com/" + "</a> ";
        msuLink.setText(Html.fromHtml(msu));
        msuLink.setMovementMethod(LinkMovementMethod.getInstance());

        String sl = "<a href=\"" + "http://spring-labs.ru/" + "\">"
                + "http://spring-labs.ru/" + "</a> ";
        slLink.setText(Html.fromHtml(sl));
        slLink.setMovementMethod(LinkMovementMethod.getInstance());

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.about, menu);
        menu.findItem(R.id.action_settings).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                /*Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);*/
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
