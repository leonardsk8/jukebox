package ilioncorp.com.jukebox.view.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

import ilioncorp.com.jukebox.utils.constantes.Constantes;
import ilioncorp.com.jukebox.view.activity.BarActivity;
import ilioncorp.com.jukebox.R;
import ilioncorp.com.jukebox.model.dto.EstablishmentVO;
import ilioncorp.com.jukebox.view.activity.FilterOptions;
import ilioncorp.com.jukebox.view.activity.ProfileActivity;
import ilioncorp.com.jukebox.view.generic.GenericFragment;
import ilioncorp.com.jukebox.view.Localizacion;

@SuppressLint("ValidFragment")
public class FragmentMap extends GenericFragment implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener, View.OnClickListener, SeekBar.OnSeekBarChangeListener,
        GoogleMap.OnMarkerDragListener, Runnable, Handler.Callback, GoogleMap.OnInfoWindowClickListener {

    private android.widget.SeekBar sbRank;
    private android.widget.TextView tvRank;
    private android.widget.RelativeLayout layoutRango;
    private android.widget.ImageView myLocation;
    private CircleOptions circleOptions;
    private int progress;
    private LatLng positionAct;
    private GoogleMap mMap;
    private Localizacion Local;
    private Circle circulo;
    private ImageView btnArrow;
    Handler mensaje;
    private Marker mainMarker;
    private String hilo;
    private boolean firts = true;
    private ArrayList<EstablishmentVO> bar;
    double latitud;
    double longitud;
    LocationManager mlocManager;


    private FusedLocationProviderClient mFusedLocationProviderClient;

    public final static int CODE_GPS=101;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_maps, container, false);
        this.myLocation = view.findViewById(R.id.myLocation);
        this.layoutRango = view.findViewById(R.id.layoutRango);
        this.tvRank = view.findViewById(R.id.tvRank);
        this.sbRank = view.findViewById(R.id.sbRank);
        this.btnArrow = view.findViewById(R.id.btnArrow);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        this.sbRank.setOnSeekBarChangeListener(this);
        this.sbRank.setMax(3);
        progress = 1;
        this.sbRank.setProgress(progress);
        myLocation.setOnClickListener(this);
        circleOptions = new CircleOptions();
        mensaje = new Handler(this);
        btnArrow.setOnClickListener(this);
        bar = new ArrayList<>();

        mlocManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        } else {
            permission();
        }

        return view;
    }



    private void permission() {
        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(settingsIntent,FragmentMap.CODE_GPS);
        }else{
            start();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(Constantes.openDialog)
            createDialog();
    }

    private void createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Te invitamos a que completes tu registro")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(getContext(),ProfileActivity.class));
                    }
                }).setNegativeButton("Ahora no", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();

    }


    private void start() {
        showCharging("Obtieniedo Ubicación", getContext());
        locationStart();
        hilo = "start";
        new Thread(this).start();
    }




    private void locationStart() {
        mlocManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Local = new Localizacion();
        Local.setMainActivity(this);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            return;
        }
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, Local);
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, Local);
    }

    private void drawCircle(LatLng point, int radius) {

        if (point == null) {
            return;
        }
        if (circulo != null) {
            circulo.remove();
        }
        circleOptions = new CircleOptions();
        circleOptions.center(point);

        // Radius of the circle
        circleOptions.radius(radius * 1000);

        // Border color of the circle
        circleOptions.strokeColor(Color.WHITE);

        // Fill color of the circle
        circleOptions.fillColor(0x908181F7);

        // Border width of the circle
        circleOptions.strokeWidth(2);

        // Adding the circle to the GoogleMap
        circulo = mMap.addCircle(circleOptions);

    }

    public void moveCamera(LatLng latLng, float zoom, String title) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        /*if (!title.equals("Name Bar")) {
            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .title(title);
            mMap.addMarker(options);
        }*/
    }

    private void getDeviceLocation() {

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        try {
            if (true) {

                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {

                            moveCamera(new LatLng(latitud, longitud),
                                    14,
                                    "My Location");

                        } else {
                            Toast.makeText(getContext(), "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            mFusedLocationProviderClient = null;
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getView().getContext(), R.raw.mapstyle));
        mMap.setOnMarkerDragListener(this);
        mMap.setOnInfoWindowClickListener(this);
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter(){
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }
            @Override
            public View getInfoContents(Marker marker) {
                Log.e("here",marker.getId()+" "+marker.getTitle());
                View v;
                if(marker.isDraggable())
                    v = getLayoutInflater().inflate(R.layout.item_info_window, null);
                else
                    v= getLayoutInflater().inflate(R.layout.preview_bar, null);
                return v;
            }
        });
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        UiSettings setting = mMap.getUiSettings();
        setting.setMyLocationButtonEnabled(false);
        setting.setZoomControlsEnabled(true);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.myLocation:
                getDeviceLocation();
                break;
            case R.id.btnArrow:
                startActivity(new Intent(getContext(),FilterOptions.class));
                getActivity().overridePendingTransition(R.anim.hide, R.anim.show);
                break;


        }
    }

    @Override
    public void run() {
        switch (hilo) {
            case "start":
                while (true) {
                    if (Local.getLatitud() > 0 | Local.getLongitud() > 0) {
                        latitud = Local.getLatitud();
                        longitud = Local.getLongitud();
                        mlocManager.removeUpdates(Local);
                        Local = null;
                        mlocManager = null;
                        break;
                    }
                }
            case "changeRank":
                /*for(int i = 0; i<1000000;i++){

                }*/
                break;

        }
        hideCharging();
        Message msg = new Message();
        mensaje.sendMessage(msg);
    }

    @Override
    public boolean handleMessage(Message message) {
        switch (hilo) {
            case "start":
                startMarks();
                break;
            case "changeRank":
                addMarks();
                drawCircle(positionAct,progress);
                break;
            case "dragMark":

                break;


        }
        return false;
    }

    private void addMarks() {
        /*mMap.addMarker(new MarkerOptions().position(
                new LatLng(positionAct.latitude + progress*0.001 + 0.0003, positionAct.longitude +progress*0.0041 )).title("0 Position")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.drink)));
        mMap.addMarker(new MarkerOptions().position(
                new LatLng(positionAct.latitude + progress*0.002 + 0.0007, positionAct.longitude +progress*0.0031)).title("1 Position")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.drink)));
        mMap.addMarker(new MarkerOptions().position(
                new LatLng(positionAct.latitude + progress*0.003 + 0.0008, positionAct.longitude +progress*0.0021 )).title("2 Position")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.drink)));
        mMap.addMarker(new MarkerOptions().position(
                new LatLng(positionAct.latitude + progress*0.004 - 0.0001 , positionAct.longitude +progress*0.0011 )).title("3 Position")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.drink)));
        mMap.addMarker(new MarkerOptions().position(
                new LatLng(positionAct.latitude + progress*0.005 + 0.0003, positionAct.longitude +progress*0.0001)).title("4  Position")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.drink)));
        if(progress>=2) {
            mMap.addMarker(new MarkerOptions().position(
                    new LatLng(positionAct.latitude - 0.01, positionAct.longitude + 0.01)).title("5  Position")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.drink)));
        }*/
        mMap.addMarker(new MarkerOptions().position(positionAct)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)).draggable(true));
    }

    private void startMarks() {
        LatLng myPossition = new LatLng(latitud, longitud);
        positionAct = myPossition;
        drawPosition();
        addMarks();

    }

    private void drawPosition() {
        mMap.addMarker(new MarkerOptions().position(positionAct)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)).draggable(true));
        float zoomLevel = 14;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(positionAct, zoomLevel));
        drawCircle(positionAct, progress);
    }

    //StartProgressBar
    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        this.progress = i;
        tvRank.setText("RANGO: "+this.progress+" KM");
        hilo="changeRank";
        if(mensaje != null) {
            showCharging("Cargando", getContext());
            new Thread(this).start();
            mMap.clear();

        }

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
    //EndProgressBar
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    //StartMarketOptions
    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        Geocoder gc = new Geocoder(getContext());
        LatLng ll = marker.getPosition();
        /*double lat = ll.latitude;
        double lng = ll.longitude;
       /* List<Address> list = null;
        try {
            list = gc.getFromLocation(lat, lng, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        //Address add = list.get(0);
        //marker.setTitle(add.getLocality());
        //marker.showInfoWindow();
        mMap.clear();
        positionAct = ll;
        hilo="changeRank";
        showCharging("Cargando", getContext());
        new Thread(this).start();
        marker.hideInfoWindow();
        //drawCircle(ll,progress);
        //addMarks();
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        startActivity(new Intent(getContext(), BarActivity.class));

    }
    //EndMarkertOptions


}
