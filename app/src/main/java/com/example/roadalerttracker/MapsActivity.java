package com.example.roadalerttracker;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.PixelCopy;
import android.view.textclassifier.TextSelection;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.roadalerttracker.databinding.ActivityMapsBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;
import okhttp3.OkHttp;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private String API_URL = "http://10.0.2.2/roadhazardtracker/api.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        // Add a marker in Sydney and move the camera
        LatLng center = new LatLng(6.4, 100.29);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(center,9));
        fetchHazardData();
    }

    private void fetchHazardData() {
        Request request = new Request.Builder()
                .url(API_URL)
                .build();

        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new okhttp3.Callback(){

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                if(response.isSuccessful() && response.body() != null){
                    String jsonResult = response.body().string();
                    Log.d("HazardMap", "Raw Json data " + jsonResult);

                    //pase json using Gson library
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<HazardsPoint>>(){}.getType();

                    List<HazardsPoint> hazardsPoints = gson.fromJson(jsonResult, listType);
                    runOnUiThread(() -> {
                        for (HazardsPoint hpoint : hazardsPoints) {
                            LatLng hazardLocation = new LatLng(Double.parseDouble(hpoint.getLatitude()), Double.parseDouble(hpoint.getLongitude()));
                            mMap.addMarker(new MarkerOptions().position(hazardLocation)
                                    .title(hpoint.getLocationName())
                                    .snippet(hpoint.getHazardType() + " - " + hpoint.getReporterName() + " _ " + hpoint.getReportDate()
                                    ));
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.e("HazardMap", "Network error", e);

                //display a Toast
                runOnUiThread(() -> {
                    Toast.makeText(MapsActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}