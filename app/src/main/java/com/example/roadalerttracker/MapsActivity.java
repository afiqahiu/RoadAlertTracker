package com.example.roadalerttracker;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.roadalerttracker.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private static final String API_URL = "http://10.0.2.2/roadhazardtracker/api.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng center = new LatLng(6.4, 100.29);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(center, 9));

        fetchHazardData();
    }

    private void fetchHazardData() {
        Request request = new Request.Builder()
                .url(API_URL)
                .build();

        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new okhttp3.Callback() {

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful() || response.body() == null) return;

                String jsonResult = response.body().string();
                Log.d("HazardMap", "Raw Json data " + jsonResult);

                Gson gson = new Gson();
                Type listType = new TypeToken<List<HazardsPoint>>() {}.getType();
                List<HazardsPoint> hazardsPoints = gson.fromJson(jsonResult, listType);

                runOnUiThread(() -> {
                    for (HazardsPoint hpoint : hazardsPoints) {

                        // LOG hazard type to check exact spelling
                        Log.d("HazardMap", "Type: " + hpoint.getHazardType());

                        LatLng hazardLocation = new LatLng(
                                Double.parseDouble(hpoint.getLatitude()),
                                Double.parseDouble(hpoint.getLongitude())
                        );

                        mMap.addMarker(new MarkerOptions()
                                .position(hazardLocation)
                                .icon(getResizedIcon(getMarkerIcon(hpoint.getHazardType()), 90, 90))
                                .title(hpoint.getLocationName())
                                .snippet(hpoint.getHazardType() + " - " + hpoint.getReporterName() + " _ " + hpoint.getReportDate()
                                ));
                    }
                });
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("HazardMap", "Network error", e);

                runOnUiThread(() ->
                        Toast.makeText(
                                MapsActivity.this, "Network error", Toast.LENGTH_SHORT).show()
                );
            }
        });
    }

    private BitmapDescriptor getResizedIcon(int drawableRes, int width, int height) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), drawableRes);
        Bitmap resized = Bitmap.createScaledBitmap(bitmap, width, height, false);
        return BitmapDescriptorFactory.fromBitmap(resized);
    }

    private int getMarkerIcon(String hazardType) {
        switch (hazardType.toLowerCase()) {
            case "pothole":
                return R.drawable.marker_pothole;
            case "flood":
                return R.drawable.marker_flood;
            case "accidents":
                return R.drawable.marker_accident;
            case "landslide":
                return R.drawable.marker_landslide;
            case "fallen tree":
                return R.drawable.marker_fallen_tree;
            case "road construction":
                return R.drawable.marker_road_construction;
            case "road closure":
                return R.drawable.marker_road_closed;
            case "uneven road surfaces":
                return R.drawable.marker_uneven_road_surfaces;
            default:
                return R.drawable.marker_other;
        }
    }

}
