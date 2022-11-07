package frames;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.a0411.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Mapa extends Fragment implements OnMapReadyCallback {

    private GoogleMap googleMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mapa, container, false);

        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);


        meuLugar();
        lugares();

        return view;
    }

    public void lugares(){

    }

    public void meuLugar(){

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMp) {

        this.googleMap = googleMp;

        LatLng lugares = new LatLng(-3.0672120039760125, -60.043744436277706);
        googleMap.addMarker(new MarkerOptions().position(lugares).title("Balneário do SESC"));
        googleMp.moveCamera(CameraUpdateFactory.newLatLng(lugares));

    }
}