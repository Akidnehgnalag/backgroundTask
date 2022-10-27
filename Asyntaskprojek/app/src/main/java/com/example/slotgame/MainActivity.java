package com.example.slotgame;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    ImageView _slot1,_slot2,_slot3;
    Button _btStart;
    boolean isPlay=false;
    private static int[] _imgs = {R.drawable.slot1, R.drawable.slot2, R.drawable.slot3, R.drawable.slot4,
            R.drawable.slot5, R.drawable.slotbar};

    SlotAsyncTask _slotAsyn1,_slotAsyn2,_slotAsyn3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _slot1 = findViewById(R.id.id_Slot1);
        _slot2 = findViewById(R.id.id_Slot2);
        _slot3 = findViewById(R.id.id_Slot3);

        _slot1.setImageResource(R.drawable.slotbar);
        _slot2.setImageResource(R.drawable.slotbar);
        _slot3.setImageResource(R.drawable.slotbar);

        _btStart = findViewById(R.id.id_BtPlay);
        _btStart.setOnClickListener(this);

        _btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                execGetImage.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final String txt = loadStringFromNetwork("https://mocki.io/v1/821f1b13-fa9a-43aa-ba9a-9e328df8270e");
                            try {
                                JSONArray jsonArray = new JSONArray(txt);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    arrayUrl.add(jsonObject.getString("url"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Glide.with(MainActivity.this)
                                            .load(arrayUrl.get(0))
                                            .into(imgSlot1);
                                    Glide.with(MainActivity.this)
                                            .load(arrayUrl.get(1))
                                            .into(imgSlot2);
                                    Glide.with(MainActivity.this)
                                            .load(arrayUrl.get(2))
                                            .into(imgSlot3);
                                    tvHasil.setText(txt);
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        private String loadStringFromNetwork(String s) throws IOException {
            final URL myUrl = new URL(s);
            final InputStream in = myUrl.openStream();
            final StringBuilder out = new StringBuilder();
            final byte[] buffer = new byte[1024];
            try {
                for (int ctr; (ctr = in.read(buffer)) != -1; ) {
                    out.append(new String(buffer, 0, ctr));
                }
            } catch (IOException e) {
                throw new RuntimeException("Gagal mendapatkan text", e);
            }
            final String yourFileAsAString = out.toString();
            return yourFileAsAString;
        }

    }

    @Override
    public void onClick(View v) {


        if(v.getId()==_btStart.getId())
        {
            if(!isPlay){


                _slotAsyn1 = new SlotAsyncTask();
                _slotAsyn2 = new SlotAsyncTask();
                _slotAsyn3 = new SlotAsyncTask();


                _slotAsyn1.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,_slot1);
                _slotAsyn2.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,_slot2);
                _slotAsyn3.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,_slot3);

                _btStart.setText("Stop");
                isPlay=!isPlay;
            }
            else {

              //  _slotAsyn1.cancel(true);
              //  _slotAsyn2.cancel(true);
              //  _slotAsyn3.cancel(true);
                _slotAsyn1._play = false;
                _slotAsyn2._play = false;
                _slotAsyn3._play = false;
                _btStart.setText("Play");
                isPlay=!isPlay;
            }

        }

    }


    private class SlotAsyncTask extends AsyncTask<ImageView, Integer, Boolean> {

        ImageView _slotImg;
        Random _random = new Random();
        public  boolean _play=true;





        public SlotAsyncTask() {
               _play=true;
        }

        @Override
        protected Boolean doInBackground(ImageView... imgs) {
            _slotImg = imgs[0];
            int a=0;
            while (_play) {
                int i = _random.nextInt(6);

                publishProgress(i);

              try {
                    Thread.sleep(_random.nextInt(500));}
                 catch (InterruptedException e) {
                 e.printStackTrace(); }
                }
            return !_play;

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            _slotImg.setImageResource(_imgs[values[0]]);
        }
    }


}