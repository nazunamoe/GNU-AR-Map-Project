package com.gnuarmap;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MarkerActivity extends AppCompatActivity {

    String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = this.getIntent();
        url = intent.getExtras().getString("URL");
        String title = intent.getExtras().getString("Title");
        String Menu1 = getResources().getString(R.string.NaverMarker);
        String Menu2 = getResources().getString(R.string.WebSite);
        final String[] LIST_MENU = {Menu1, Menu2} ;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, LIST_MENU) ;
        ListView listview = (ListView) findViewById(R.id.listview1) ;
        listview.setAdapter(adapter) ;
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                Intent intent;
                switch(position){
                    case 0:{
                        intent = new Intent(MarkerActivity.this, NaverMap.class);
                        intent.putExtra("Return","True");
                        intent.putExtra("set","True");
                        startActivity(intent);
                        break;
                    }
                    case 1:{
                        try {
                            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
            }
        }) ;
        setTitle(title);
    }


    @Override
    public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        if (keyCode == android.view.KeyEvent.KEYCODE_BACK){
            Context ctx;
            ctx = this;
            startActivity(new Intent(ctx, MixView.class));
            finish();
        }
        return false;
    }


}
