package com.danielme.android.recyclerview.endless;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * @author danielme.com
 */
public class MainActivity extends AppCompatActivity {

  private List<Item> colors;
  private boolean hasMore;
  private AsyncTask asyncTask;
  private RecyclerView recyclerView;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    colors = buildColors();

    hasMore = true;

    recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
    recyclerView.setAdapter(new MaterialPaletteAdapter(colors, new
        RecyclerViewOnItemClickListener() {
      @Override
      public void onClick(View v, int position) {
        if (colors.get(position) instanceof Color) {
          Toast toast = Toast.makeText(MainActivity.this, String.valueOf(position), Toast
              .LENGTH_SHORT);
          int color = android.graphics.Color.parseColor(((Color) colors.get(position)).getHex());
          toast.getView().setBackgroundColor(color);
          toast.show();
        }
      }
    }));
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.addItemDecoration(new DividerItemDecoration(this));
    recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
      @Override
      public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (hasMore && !(hasFooter())) {
          LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
          //position starts at 0
          if (layoutManager.findLastCompletelyVisibleItemPosition()
              == layoutManager.getItemCount() - 2) {

            //displays the footer after the onscroll listener
            colors.add(new Footer());
            Handler handler = new Handler();

            final Runnable r = new Runnable() {
              public void run() {
                MainActivity.this.recyclerView.getAdapter().notifyItemInserted(colors.size() - 1);
              }
            };
            handler.post(r);
            asyncTask = new BackgroundTask();
            asyncTask.execute((Object[]) null);
          }
        }
      }
    });

  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (asyncTask != null) {
      asyncTask.cancel(false);
    }
  }

  private class BackgroundTask extends AsyncTask<Void, Void, Void> {

    @Override
    protected Void doInBackground(Void... params) {
      try {
        Thread.sleep(3000);
      } catch (InterruptedException e) {
        Log.e(this.getClass().toString(), e.getMessage());
      }
      return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
      int size = colors.size();
      colors.remove(size - 1);//removes footer
      colors.addAll(buildColors());
      recyclerView.getAdapter().notifyItemRangeChanged(size - 1, colors.size() - size);
    }

  }

  private boolean hasFooter() {
    return colors.get(colors.size() - 1) instanceof Footer;
  }

  private ArrayList<Item> buildColors() {
    ArrayList<Item> colors = new ArrayList<>(13);

    colors.add(new Color(getString(R.string.blue), getColorString(R.color.blue)));
    colors.add(new Color(getString(R.string.indigo), getColorString(R.color.indigo)));
    colors.add(new Color(getString(R.string.red), getColorString(R.color.red)));
    colors.add(new Color(getString(R.string.green), getColorString(R.color.green)));
    colors.add(new Color(getString(R.string.orange), getColorString(R.color.orange)));
    colors.add(new Color(getString(R.string.grey), getColorString(R.color.bluegrey)));
    colors.add(new Color(getString(R.string.amber), getColorString(R.color.teal)));
    colors.add(new Color(getString(R.string.deeppurple), getColorString(R.color.deeppurple)));
    colors.add(new Color(getString(R.string.bluegrey), getColorString(R.color.bluegrey)));
    colors.add(new Color(getString(R.string.yellow), getColorString(R.color.yellow)));
    colors.add(new Color(getString(R.string.cyan), getColorString(R.color.cyan)));
    colors.add(new Color(getString(R.string.brown), getColorString(R.color.brown)));
    colors.add(new Color(getString(R.string.teal), getColorString(R.color.teal)));

    return colors;
  }

  //returns the string hex value of a color in colors.xml
  private String getColorString(int colorId) {
    return "#" + Integer.toHexString(ContextCompat.getColor(this, colorId)).toUpperCase()
        .substring(2);
  }

}