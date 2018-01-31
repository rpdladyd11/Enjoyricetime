package com.example.administrator.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Administrator on 2017-06-06.
 */

public class bukjipro extends AppCompatActivity {
    String weekday;
    SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
    Calendar calendar = Calendar.getInstance();
    TextView text1,text2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.unji);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        PermissionRequester.Builder request = new PermissionRequester.Builder(this);
        request.create().request(Manifest.permission.INTERNET, 10000, new PermissionRequester.OnClickDenyButtonListener() {
            @Override
            public void onClick(Activity activity) {
                Toast.makeText(activity, "인터넷 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
                activity.finish();
            }
        });
        weekday = dayFormat.format(calendar.getTime());
        text1=(TextView)findViewById(R.id.text1);
        text2=(TextView)findViewById(R.id.text2);
    }

    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
    @Override
    protected void onResume() {
        super.onResume();

        GetLottoNumberTask task = new GetLottoNumberTask();
        task.execute();
    }

    private class GetLottoNumberTask extends AsyncTask<Void, Void, Map<String, String>> {


        @Override
        protected Map<String, String> doInBackground(Void... params) {
            Map<String, String> result = new HashMap<String, String>();
            try {
                Document document = Jsoup.connect("http://www.daegu.ac.kr/web/sub_title/student/sub4_1.asp?mm_num=4").get();


                if (weekday.equals("일요일")) {
                    Elements elements = document.select("table.table4 thead tr th:eq(0)");
                    result.put("text1", elements.text());
                    Elements elements2 = document.select("table.table4 tbody tr td:eq(0)");
                    result.put("text2", elements2.text());
                } else if (weekday.equals("월요일")) {
                    Elements elements = document.select("table.table4 thead tr th:eq(1)");
                    result.put("text1", elements.text());
                    Elements elements2 = document.select("table.table4 tbody tr td:eq(1)");
                    result.put("text2", elements2.text());
                } else if (weekday.contains("화요일")) {
                    Elements elements = document.select("table.table4 thead tr th:eq(2)");
                    result.put("text1", elements.text());
                    Elements elements2 = document.select("table.table4 tbody tr td:eq(2)");
                    result.put("text2", elements2.text());
                } else if (weekday.equals("수요일")) {
                    Elements elements = document.select("table.table4 thead tr th:eq(3)");
                    result.put("text1", elements.text());
                    Elements elements2 = document.select("table.table4 tbody tr td:eq(3)");
                    result.put("text2", elements2.text());
                } else if (weekday.equals("목요일")) {
                    Elements elements = document.select("table.table4 thead tr th:eq(4)");
                    result.put("text1", elements.text());
                    Elements elements2 = document.select("table.table4 tbody tr td:eq(4)");
                    result.put("text2", elements2.text());
                } else if (weekday.equals("금요일")) {
                    Elements elements = document.select("table.table4 thead tr th:eq(5)");
                    result.put("text1", elements.text());
                    Elements elements2 = document.select("table.table4 tbody tr td:eq(5)");
                    result.put("text2", elements2.text());
                } else if (weekday.equals("토요일")) {
                    Elements elements = document.select("table.table4 thead tr th:eq(6)");
                    result.put("text1", elements.text());
                    Elements elements2 = document.select("table.table4 tbody tr td:eq(6)");
                    result.put("text2", elements2.text());
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(Map<String, String> map) {

            text1.setText(map.get("text1"));
            if (map.get("text2").equals("")) {
                text2.setText("식단이 없거나 아직 식단이 인터넷에 업로드 되지 않았습니다");
            } else {
                text2.setText(map.get("text2"));
            }

        }

    }
}

