package com.example.anushan.tourguide;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class OneHistoricalSiteOnMapView extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private View view;
    private MapView mapView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_historical_sites_on_map_view, container, false);
        mapView = (MapView) view.findViewById(R.id.mapviewWhole);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
        return view;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Bundle bundle = this.getArguments();
        double v1= bundle.getDouble("Log");
        double v2=bundle.getDouble("Lat");
        Geocoder geocoder = new Geocoder(view.getContext(), Locale.getDefault());
        String result = null;
        try {
            List<Address> addressList = geocoder.getFromLocation(
                    v2, v1, 1);
            if (addressList != null && addressList.size() > 0) {
                Address address = addressList.get(0);
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                    sb.append(address.getAddressLine(i)).append("\n");//adress
                }
                sb.append(address.getLocality()).append("\n");//village

                sb.append(address.getPostalCode()).append("\n");
                sb.append(address.getCountryName());
                sb.append(address.getAdminArea()).append("\n"); //state

                sb.append(address.getSubAdminArea()).append("\n");//district

                sb.append(address.getSubLocality()).append("\n");

                result = sb.toString();
            }
        } catch (IOException e) {
            // Log.e(TAG, "Unable connect to Geocoder", e);
        }
        final LatLng latlng = new LatLng(v2, v1);
        float zoomLevel = (float) 10.0;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, zoomLevel));
        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(latlng)
                .title(result)
                .snippet(result)
                .icon(bitmapDescriptorFromVector(view.getContext(),R.drawable.ic_location_on_black_24dp)));



    }



    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}
