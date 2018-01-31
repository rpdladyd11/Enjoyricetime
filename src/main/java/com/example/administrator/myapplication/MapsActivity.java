package com.example.administrator.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Timer;
import java.util.TimerTask;

public class MapsActivity extends FragmentActivity implements LocationListener,OnMapReadyCallback,GoogleMap.OnInfoWindowClickListener,GoogleMap.OnMarkerClickListener,GoogleApiClient.OnConnectionFailedListener {
    private GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap, mMap2;
    private SensorManager mSensorManger;
    double mLatitude;  //위도
    double mLongitude; //경도



    private String provider;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private TextView time;
    private final long FINISH_INTERVAL_TIME = 2000;
    private long   backPressedTime = 0;
    Button student,professor,fast,delivery;
    private  boolean mCompassEnabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        PermissionRequester.Builder request = new PermissionRequester.Builder(this);
        request.create().request(Manifest.permission.INTERNET, 10000, new PermissionRequester.OnClickDenyButtonListener() {
            @Override
            public void onClick(Activity activity) {
                Toast.makeText(activity, "인터넷 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
                activity.finish();

            }
        });


        if(ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION ,
                    Manifest.permission.ACCESS_FINE_LOCATION} , 1);
        }
        //권한이 있는 경우
        else{

        }


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        student=(Button)findViewById(R.id.student);
        professor=(Button)findViewById(R.id.professor);
        fast=(Button)findViewById(R.id.fast);
        delivery=(Button)findViewById(R.id.delivery);
        time=(TextView)findViewById(R.id.time);
        Criteria criteria = new Criteria();
        mSensorManger =(SensorManager)getSystemService(Context.SENSOR_SERVICE);

    }

    private void requestMyLocation() {
        if(ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return;
        }
        //요청
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 10, locationListener);


    }

    private final LocationListener mLocationListener=new LocationListener() {
    @Override
    public void onLocationChanged(Location location) {
        Log.d("test", "onLocationChanged, location:" + location);
        double longitude = location.getLongitude(); //경도
        double latitude = location.getLatitude();   //위도
        double altitude = location.getAltitude();   //고도
        float accuracy = location.getAccuracy();    //정확도
        String provider = location.getProvider();   //위치제공자



    }



    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {


        Log.d("test", "onStatusChanged, provider:" + provider + ", status:" + status + " ,Bundle:" + extras);
    }




    @Override
    public void onProviderEnabled(String provider) {
        Log.d("test", "onProviderEnabled, provider:" + provider);


    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("test", "onProviderDisabled, provider:" + provider);
    }
};





    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {


        mMap = googleMap;

        GpsInfo gps = new GpsInfo(MapsActivity.this);
        if (gps.isGetLocation()){
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude),15));
            mMap.addMarker(new MarkerOptions().position(new LatLng(latitude,longitude)).title("현재위치"));

            //double loation=getDistance(new LatLng(latitude,longitude),new LatLng(35.901377, 128.846948));
            // String loat=Double.toString(loation);
            // time.setText(loat);
        }
        else {
            gps.showSettingsAlert();
        }

        final MarkerOptions unji=new MarkerOptions();
        unji.position(new LatLng(35.901377, 128.846948));  //웅지관식당
        unji.title("웅지관(학생식당)").snippet("053-851-5807");

        final MarkerOptions miz=new MarkerOptions();
        miz.position(new LatLng(35.901239, 128.846589)); //미즈 식당
        miz.title("미즈컨테이너").snippet("010-2570-0219");

        final MarkerOptions unjipro=new MarkerOptions();
        unjipro.position(new LatLng(35.901494, 128.846931)); //교직원 식당
        unjipro.title("교직원식당(웅지관)").snippet("053-850-5979");

        final MarkerOptions convenience=new MarkerOptions();
        convenience.position(new LatLng(35.901807, 128.848861)); //편의점
        convenience.title("GS25");

        final MarkerOptions green=new MarkerOptions();
        green.position(new LatLng(35.903969, 128.845116)); //그린테리아
        green.title("그린테리아").snippet("053-851-9672");


        final MarkerOptions eight=new MarkerOptions();
        eight.position(new LatLng(35.903381, 128.843036)); //8호관 식당
        eight.title("8호관 식당").snippet("053-851-8988");

        final MarkerOptions bukji=new MarkerOptions();
        bukji.position(new LatLng(35.899528, 128.852703)); //복지관 학생식당
        bukji.title("복지관(학생식당)").snippet("053-851-8002");

        final MarkerOptions burgerking=new MarkerOptions();
        burgerking.position(new LatLng(35.899467, 128.852917)); //버거킹
        burgerking.title("버거킹").snippet("053-852-2254");

        final MarkerOptions bukjipro=new MarkerOptions();
        bukjipro.position(new LatLng(35.899363, 128.852694)); //복지관 교직원식당
        bukjipro.title("교직원식당(복지관)").snippet("053-850-5989");

        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
//35.899467, 128.852917 버거킹
        student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mMap.clear();
                GpsInfo gps = new GpsInfo(MapsActivity.this);
                if (gps.isGetLocation()){
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude),15));
                    mMap.addMarker(new MarkerOptions().position(new LatLng(latitude,longitude)).title("현재위치"));
                    //double loation=getDistance(new LatLng(latitude,longitude),new LatLng(35.901377, 128.846948));
                    // String loat=Double.toString(loation);
                    // time.setText(loat);
                }
                else {
                    gps.showSettingsAlert();
                }
                mMap.addMarker(unji).showInfoWindow();     //웅지관 학생식당
                mMap.addMarker(bukji).showInfoWindow();   //학생식당(복지관)
                mMap.addMarker(eight).showInfoWindow(); //8호관식당
                mMap.addMarker(green).showInfoWindow();    //그린테리아 기숙사식당
            }
        });
        professor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             


                mMap.clear();
                GpsInfo gps = new GpsInfo(MapsActivity.this);
                if (gps.isGetLocation()){
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude),15));
                    mMap.addMarker(new MarkerOptions().position(new LatLng(latitude,longitude)).title("현재위치"));
                    //double loation=getDistance(new LatLng(latitude,longitude),new LatLng(35.901377, 128.846948));
                    // String loat=Double.toString(loation);
                    // time.setText(loat);
                }
                else {
                    gps.showSettingsAlert();
                }
                mMap.addMarker(unjipro).showInfoWindow();  //웅지관교직원
                mMap.addMarker(bukjipro).showInfoWindow();// 복지관교직원식당
            }
        });

        fast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mMap.clear();
                GpsInfo gps = new GpsInfo(MapsActivity.this);
                if (gps.isGetLocation()){
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude),15));
                    mMap.addMarker(new MarkerOptions().position(new LatLng(latitude,longitude)).title("현재위치"));
                    //double loation=getDistance(new LatLng(latitude,longitude),new LatLng(35.901377, 128.846948));
                    // String loat=Double.toString(loation);
                    // time.setText(loat);
                }
                else {
                    gps.showSettingsAlert();
                }
                mMap.addMarker(miz).showInfoWindow();     //미즈
                mMap.addMarker(convenience).showInfoWindow();  //편의점 GS25
                mMap.addMarker(burgerking).showInfoWindow(); //버거킹
            }
        });




    }

    private void changeSelectedMarker(Marker marker) {

    }

    @Override
    public void onInfoWindowClick(Marker marker) {

        if (marker.getTitle().equals("웅지관(학생식당)")) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/dg_welstory/"));
            startActivity(intent);
            //인스타그램 홈페이지 실행  https://www.instagram.com/dg_welstory/
        } else if (marker.getTitle().equals("교직원식당(웅지관)")) {
            Intent intent = new Intent(getApplicationContext(), unji.class);
            startActivity(intent);
            finish();
        } else if (marker.getTitle().equals("미즈컨테이너")) {
            Intent intent = new Intent(getApplicationContext(), miz.class);
            startActivity(intent);
            finish();
        } else if (marker.getTitle().equals("GS25")) {
            Toast.makeText(getApplication(), "메뉴는 직접가서 골라드세요", Toast.LENGTH_SHORT).show();
        } else if (marker.getTitle().equals("8호관 식당")) {
            Intent intent = new Intent(getApplication(), eight.class);
            startActivity(intent);
            finish();
        } else if (marker.getTitle().equals("그린테리아")) {
            Intent intent = new Intent(getApplication(), green.class);
            startActivity(intent);
            finish();
        } else if (marker.getTitle().equals("복지관(학생식당)")) {
            Intent intent = new Intent(getApplication(), bukjist.class);
            startActivity(intent);
            finish();
        } else if (marker.getTitle().equals("교직원식당(복지관)")) {
            Intent intent = new Intent(getApplication(), bukjipro.class);
            startActivity(intent);
            finish();
        }
        else if (marker.getTitle().equals("버거킹")){
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.burgerking.co.kr/#"));
            startActivity(intent);
        }

    }
    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.getTitle().equals("웅지관(학생식당)")){
            time.setText(" 위치 \n 제1학생회관 1층 학생식당(사범대 맞은편)\n 영업시간 \n 07:30~19:00");
        }
        else if (marker.getTitle().equals("교직원식당(웅지관)")){
            time.setText(" 위치 \n 제1학생회관 2층 웅지관 교직원식당 \n 영업시간 \n 11:30~18:30");
        }
        else if (marker.getTitle().equals("미즈컨테이너")){
            time.setText(" 영업시간 \n 평일 10:30 - 19:30 마지막주문시간: 19:30 \n 주말 11:00 - 20:00   마지막주문시간: 20:00 ");
        }
        else if (marker.getTitle().equals("GS25")){
            time.setText(" 영업시간 \n 24시 ");
        }
        else if (marker.getTitle().equals("8호관 식당")){
            time.setText(" 위치 \n 법.행정대 뒤편 인재양성관 지하 영업시간 \n 07:40~19:00");
        }
        else if (marker.getTitle().equals("그린테리아")){
            time.setText(" 위치 \n 비호생활관 식당(비호 그린테리아) 2층 \n 영업시간 \n 07:30~19:00");
        }
        else if (marker.getTitle().equals("복지관(학생식당)")){
            time.setText(" 위치 \n 대구대학교내 재활과학대학 맞은편 복지관 1층 \n 영업시간 \n 08:00~19:30");
        }
        else if (marker.getTitle().equals("버거킹")){
            time.setText(" 위치 \n 대구대학교내 재활과학대학 맞은편 복지관 1층 \n 영업시간 \n 09:00~21:00");
        }
        else if (marker.getTitle().equals("교직원식당(복지관)")){
            time.setText(" 위치 \n 대구대학교내 재활과학대학 맞은편 복지관 2층 \n 영업시간 \n 11:30~18:30");
        }
        return false;
    }
    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime)
        {
            super.onBackPressed();
        }
        else
        {
            backPressedTime = tempTime;
            Toast.makeText(getApplicationContext(), "한번 더 뒤로가기 누르면 꺼집니다.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if(ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return;
        }
        //나의 위치를 한번만 가져오기 위해
        locationManager.removeUpdates(locationListener);

        //위도 경도
        mLatitude = location.getLatitude();   //위도
        mLongitude = location.getLongitude(); //경도

        //맵생성
        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        //콜백클래스 설정
        mapFragment.getMapAsync(MapsActivity.this);



    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }
    public double getDistance(LatLng LatLng1, LatLng LatLng2) {
        double distance = 0;
        Location locationA = new Location("A");
        locationA.setLatitude(LatLng1.latitude);
        locationA.setLongitude(LatLng1.longitude);
        Location locationB = new Location("B");
        locationB.setLatitude(LatLng2.latitude);
        locationB.setLongitude(LatLng2.longitude);
        distance = locationA.distanceTo(locationB);

        return distance;

    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}