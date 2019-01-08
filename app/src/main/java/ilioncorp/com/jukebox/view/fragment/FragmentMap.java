package ilioncorp.com.jukebox.view.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.Image;
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
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Calendar;

import ilioncorp.com.jukebox.model.dao.EstablishmentDAO;
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
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener,
        SeekBar.OnSeekBarChangeListener,
        GoogleMap.OnMarkerDragListener,
        Runnable,
        Handler.Callback,
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnCameraMoveStartedListener,
        GoogleMap.OnCameraMoveListener,
        GoogleMap.OnCameraMoveCanceledListener,
        GoogleMap.OnCameraIdleListener,
        GoogleMap.OnMarkerClickListener{

    private boolean oneTime =true;
    private android.widget.SeekBar sbRank;
    private android.widget.TextView tvRank;
    private android.widget.RelativeLayout layoutRango;
    private android.widget.ImageView myLocation;
    private CircleOptions circleOptions;
    private int progress;
    private boolean isThereBars;
    private LatLng positionAct;
    private GoogleMap mMap;
    private Localizacion Local;
    private Circle circulo;
    private boolean endHandler = false;
    private int contadorPrimeraVez=0;
    Handler mensaje;
    private Handler bridge;

    private String hilo;

    private ArrayList<EstablishmentVO> bar;
    double latitud;
    double longitud;
    LocationManager mlocManager;
    EstablishmentDAO establishmentDAO;

    Marker previousMarker;
    Marker lastMarker;

    private FusedLocationProviderClient mFusedLocationProviderClient;

    public final static int CODE_GPS = 101;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_maps, container, false);
        this.myLocation = view.findViewById(R.id.myLocation);
        this.layoutRango = view.findViewById(R.id.layoutRango);
        this.tvRank = view.findViewById(R.id.tvRank);
        this.sbRank = view.findViewById(R.id.sbRank);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this::onMapReady);
        this.sbRank.setOnSeekBarChangeListener(this);
        this.sbRank.setMax(3);
        progress = 1;
        this.sbRank.setProgress(progress);
        myLocation.setOnClickListener(this);
        circleOptions = new CircleOptions();
        mensaje = new Handler(this);
        bar = new ArrayList<>();
        bridge = new Handler((msg) -> {
            bar = (ArrayList<EstablishmentVO>) msg.obj;
            isThereBars = (bar.size() > 0) ? true : false;
            endHandler = true;
            return false;
        });
        hilo = "start";
        establishmentDAO = new EstablishmentDAO(bridge);
        establishmentDAO.getAllBars();
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
            startActivityForResult(settingsIntent, FragmentMap.CODE_GPS);
        } else {
            start();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (Constantes.openDialog)
            createDialog();
    }

    private void createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Te invitamos a que completes tu registro")
                .setPositiveButton("OK", (dialog, id) -> startActivity(new Intent(getContext(), ProfileActivity.class)))
                .setNegativeButton("Ahora no", (dialogInterface, i) -> {

                });
        builder.create().show();

    }


    private void start() {
        showCharging("Obtieniedo Ubicación", getContext(), true);
        locationStart();
        hilo = "start";
        new Thread(this::run).start();
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
        //mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, Local);
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2 * 20 * 1000, 10, Local);
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
            final Task location = mFusedLocationProviderClient.getLastLocation();
            location.addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    moveCamera(new LatLng(latitud, longitud),
                            14,
                            "My Location");

                } else {
                    Toast.makeText(getContext(), "unable to get current location", Toast.LENGTH_SHORT).show();
                }
            });
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
        //mMap.setMyLocationEnabled(true);
        mMap.setOnMarkerClickListener(this::onMarkerClick);
        mMap.setOnInfoWindowClickListener(this::onInfoWindowClick);
        mMap.setOnCameraIdleListener(this::onCameraIdle);
        mMap.setOnCameraMoveStartedListener(this::onCameraMoveStarted);
        mMap.setOnCameraMoveListener(this::onCameraMove);


        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                Log.e("here", marker.getId() + " " + marker.getTitle());
                View v;
                if (marker.isDraggable())
                    v = getLayoutInflater().inflate(R.layout.item_info_window, null);
                else {
                    v = getLayoutInflater().inflate(R.layout.preview_bar, null);
                    ImageView imageBar = v.findViewById(R.id.imagePreviewBar);
                    TextView nameBar = v.findViewById(R.id.tvNameBarPreview);
                    RatingBar bar = v.findViewById(R.id.ratingBarPreview);
                    TextView tvAddressBarPreview = v.findViewById(R.id.tvAddressBarPreview);
                    TextView tvGendersBarPreview = v.findViewById(R.id.tvGendersBarPreview);
                    EstablishmentVO establishment = (EstablishmentVO) marker.getTag();
                    Glide.with(getContext())
                            .load(establishment.getImagesBar()[0])
                            .placeholder(R.drawable.error)
                            .into(imageBar);
                    nameBar.setText(establishment.getName());
                    bar.setRating(establishment.getRaiting());
                    tvAddressBarPreview.setText(establishment.getAddress());
                    tvGendersBarPreview.setText(establishment.getGenders());

                }
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
        switch (view.getId()) {
            case R.id.myLocation:
                getDeviceLocation();
                break;
            /*case R.id.btnArrow:
                startActivity(new Intent(getContext(),FilterOptions.class));
                getActivity().overridePendingTransition(R.anim.hide, R.anim.show);
                break;
*/

        }
    }

    @Override
    public void run() {
        Calendar calendario = Calendar.getInstance();
        int segundos, segundos2;
        switch (hilo) {
            case "start":
                segundos = calendario.get(Calendar.SECOND);
                while (true) {
                    if (Local.getLatitud() != 0 & Local.getLongitud() != 0) {
                        latitud = Local.getLatitud();
                        longitud = Local.getLongitud();
                        break;
                    }
                    Calendar calen = Calendar.getInstance();
                    segundos2 = calen.get(Calendar.SECOND);
                    if (segundos2 < segundos)
                        segundos2 += segundos;
                    if (segundos2 - segundos > 5) {
                        lastKnowPosition();
                        getDeviceLocation();
                    }
                    if(latitud != 0 & longitud != 0)
                        break;
                }
            case "changeRank":
                /*for(int i = 0; i<1000000;i++){

                }*/
                break;

        }

        while (!endHandler) {

        }
        hideCharging();
            Message msg = new Message();
            mensaje.sendMessage(msg);


    }


    private void lastKnowPosition() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            messageToast("Active los permisos de localización");
            return;
        }
        mFusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(getActivity(), location -> {
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        latitud = location.getLatitude();
                        longitud = location.getLongitude();

                    }
                });
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

        lastMarker = mMap.addMarker(new MarkerOptions().position(
               positionAct).title("Buscar Aquí").draggable(true)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        lastMarker.setTag("search");
        for (EstablishmentVO establishmentVO:bar){
            if(masCercano(establishmentVO.latitude,establishmentVO.lenght,positionAct.latitude,positionAct.longitude))
            mMap.addMarker(new MarkerOptions().position(
                    new LatLng(establishmentVO.getLatitude(), establishmentVO.getLenght())).title(establishmentVO.getName())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.drink))).setTag(establishmentVO);
        }
    }
    public static double distFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double dist = (earthRadius * c);
        return dist;
    }
    public boolean masCercano(double lat1, double lng1, double lat2, double lng2){
        double distance = distFrom(lat1, lng1, lat2, lng2);
            if(distance <= (this.progress * 1000.0)){
                return true;
            }
            else
                return false;

    }
    private void startMarks() {
        mlocManager.removeUpdates(Local);
        Local = null;
        mlocManager = null;
        LatLng myPossition = new LatLng(latitud, longitud);
        positionAct = myPossition;
        drawPosition();
        addMarks();

    }

    private void drawPosition() {
//        mMap.addMarker(new MarkerOptions().position(positionAct)
//                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)).draggable(true));
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
            showCharging("Cargando", getContext(),false);
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

        //drawCircle(ll,progress);
        //addMarks();
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        if(!marker.isDraggable()) {
            EstablishmentVO vo = (EstablishmentVO) marker.getTag();
            Intent intent = new Intent(getContext(), BarActivity.class);
            intent.putExtra("establishment", vo);
            startActivity(intent);
        }


    }

    @Override
    public void onCameraIdle() {
        //messageToast("Camera Idle ");



    }

    @Override
    public void onCameraMoveCanceled() {
        //messageToast("Camera MoveCanceled ");
    }

    @Override
    public void onCameraMove() {

        LatLng latLng = mMap.getCameraPosition().target;
        if(lastMarker!=null)
            lastMarker.setPosition(latLng);



    }

    @Override
    public void onCameraMoveStarted(int i) {


    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        try {
            String tag = (String) marker.getTag();
            if(tag.equals("search")) {
                LatLng ll = marker.getPosition();
                mMap.clear();
                positionAct = ll;
                hilo = "changeRank";
                showCharging("Cargando", getContext(), false);
                new Thread(this).start();
                marker.hideInfoWindow();
            }
        }catch (Exception e){
            Log.e("ERROR","No STRING");
        }
        return false;
    }
}
