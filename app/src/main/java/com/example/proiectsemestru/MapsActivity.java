package com.example.proiectsemestru;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.Toast;

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

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

        for (Task task : listaTaskuri) {
            String lat = task.getLatitute();
            String longit = task.getLongitude();
            LatLng ticker = new LatLng(Double.parseDouble(lat), Double.parseDouble(longit));
            listaPuncte.add(ticker);
            mMap.addMarker(new MarkerOptions().position(ticker).title(task.getName()));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(ticker));
        }


//        MarkerOptions markerOptions=new MarkerOptions();
//        markerOptions.position(atm).title("ATM").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

        PolylineOptions plo = new PolylineOptions();
        for(LatLng ticker:listaPuncte){
            plo.add(ticker);
        }
        plo.color(Color.RED);
        plo.width(20);
        mMap.addPolyline(plo);

      //  Marker m = mMap.addMarker(markerOptions);
      //  m.showInfoWindow();


//        //marker la ase
//        LatLng ase = new LatLng( 44.41825564675483, 26.086762849191615);
//        mMap.addMarker(new MarkerOptions().position(ase).title("Marker in ASE"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(ase));
//
//
//
//        // Add a marker in Sydney and move the camera
//        LatLng atm = new LatLng(44.41825088339814, 26.086731821140315);
//        mMap.addMarker(new MarkerOptions().position(atm).title("Marker in ATM"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(atm));


    }
}