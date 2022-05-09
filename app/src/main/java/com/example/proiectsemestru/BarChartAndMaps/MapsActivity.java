package com.example.proiectsemestru.BarChartAndMaps;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.example.proiectsemestru.MyDatabase;
import com.example.proiectsemestru.R;
import com.example.proiectsemestru.Entities.Task;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.proiectsemestru.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private Date dataSelectata;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /***/
        Intent intent = getIntent();
        String data = intent.getStringExtra("currentDate");
        this.dataSelectata = new Date(data);

        /***/
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

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

        mMap.setMinZoomPreference(14);
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        MyDatabase db = MyDatabase.getInstance(getApplicationContext());

        List<Task> listaTaskuri = db.getTaskDAO().getTasksByDate(db.getUserCurrent().getId(), dataSelectata.getTime());


        ArrayList<LatLng> listaPuncte = new ArrayList<>();


        MarkerOptions markerOptions = null;

        for (Task task : listaTaskuri) {
            String lat = task.getLatitute();
            String longit = task.getLongitude();
            LatLng ticker = new LatLng(Double.parseDouble(lat), Double.parseDouble(longit));
            listaPuncte.add(ticker);
            mMap.addMarker(new MarkerOptions().position(ticker).title(task.getName()));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(ticker));

            markerOptions = new MarkerOptions();

            if (task.getPrioritate().equals("High"))
                markerOptions.position(ticker).title(task.getName()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            else if (task.getPrioritate().equals("Medium"))
                markerOptions.position(ticker).title(task.getName()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
            else
                markerOptions.position(ticker).title(task.getName()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));


            Marker m = mMap.addMarker(markerOptions);
            m.showInfoWindow();


        }


        PolylineOptions plo = new PolylineOptions();
        for (LatLng ticker : listaPuncte) {
            plo.add(ticker);
        }
        plo.color(Color.RED);
        plo.width(20);
        mMap.addPolyline(plo);


    }
}