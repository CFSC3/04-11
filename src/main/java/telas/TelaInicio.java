package telas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.a0411.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import frames.Emergencia;
import frames.Mapa;
import frames.MapsFragment;
import frames.Video;

public class TelaInicio extends AppCompatActivity {

    private Video video;
    private Emergencia emergencia;
    private Mapa mapa;
    private BottomNavigationView navegacao;
    private Button filtroGeral, filtroPasseio, filtroRestaurante;
    private Bundle dados = new Bundle();
    private MapsFragment mapsFragment;

    private TextView musica, vales, map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_inicio);

        navegacao = findViewById(R.id.bottomNavigationView);
        filtroGeral = findViewById(R.id.filtroGeral);
        filtroPasseio = findViewById(R.id.filtroPasseio);
        filtroRestaurante = findViewById(R.id.filtroRestaurante);

        filtroGeral.setVisibility(View.INVISIBLE);
        filtroPasseio.setVisibility(View.INVISIBLE);
        filtroRestaurante.setVisibility(View.INVISIBLE);

        musica = findViewById(R.id.textViewMusica);
        vales = findViewById(R.id.textViewEmergencia);
        map = findViewById(R.id.textViewMapa);

        musica.setVisibility(View.VISIBLE);
        vales.setVisibility(View.INVISIBLE);
        map.setVisibility(View.INVISIBLE);

        video = new Video();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.passeDeFrames, video );
        fragmentTransaction.commit();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.getWindow().setStatusBarColor(getColor(R.color.notificacao));
        }

        fragmen();
        botaoMapa();
    }

    public void fragmen(){

        navegacao.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){

                    case R.id.musica:

                        video = new Video();
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.passeDeFrames, video );
                        fragmentTransaction.commit();

                        filtroGeral.setVisibility(View.INVISIBLE);
                        filtroPasseio.setVisibility(View.INVISIBLE);
                        filtroRestaurante.setVisibility(View.INVISIBLE);

                        musica.setVisibility(View.VISIBLE);
                        vales.setVisibility(View.INVISIBLE);
                        map.setVisibility(View.INVISIBLE);

                        break;

                    case R.id.emergencia:

                        emergencia = new Emergencia();
                        FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction2.replace(R.id.passeDeFrames, emergencia );
                        fragmentTransaction2.commit();

                        filtroGeral.setVisibility(View.INVISIBLE);
                        filtroPasseio.setVisibility(View.INVISIBLE);
                        filtroRestaurante.setVisibility(View.INVISIBLE);

                        musica.setVisibility(View.INVISIBLE);
                        vales.setVisibility(View.VISIBLE);
                        map.setVisibility(View.INVISIBLE);

                        break;

                    case R.id.mapa:

                        MapsFragment mapa = new MapsFragment();
                        FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction3.replace(R.id.passeDeFrames, mapa );
                        fragmentTransaction3.commit();

                        filtroGeral.setVisibility(View.VISIBLE);
                        filtroPasseio.setVisibility(View.INVISIBLE);
                        filtroRestaurante.setVisibility(View.INVISIBLE);

                        musica.setVisibility(View.INVISIBLE);
                        vales.setVisibility(View.INVISIBLE);
                        map.setVisibility(View.VISIBLE);

                        break;

                    default:
                        Toast.makeText(TelaInicio.this, "Algo deu errado!", Toast.LENGTH_SHORT).show();
                }

                return true;
            }
        });
    }

    public void botaoMapa(){

        filtroGeral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filtroGeral.setVisibility(View.INVISIBLE);
                filtroPasseio.setVisibility(View.VISIBLE);
                filtroRestaurante.setVisibility(View.INVISIBLE);

                mapsFragment = new MapsFragment();

                dados.putInt("dados", 1);
                mapsFragment.setArguments(dados);

                FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
                fragmentTransaction3.replace(R.id.passeDeFrames, mapsFragment );
                fragmentTransaction3.commit();
            }
        });

        filtroPasseio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filtroGeral.setVisibility(View.INVISIBLE);
                filtroPasseio.setVisibility(View.INVISIBLE);
                filtroRestaurante.setVisibility(View.VISIBLE);

                mapsFragment = new MapsFragment();

                dados.putInt("dados", 2);
                mapsFragment.setArguments(dados);

                FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
                fragmentTransaction3.replace(R.id.passeDeFrames, mapsFragment );
                fragmentTransaction3.commit();
            }
        });

        filtroRestaurante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filtroGeral.setVisibility(View.VISIBLE);
                filtroPasseio.setVisibility(View.INVISIBLE);
                filtroRestaurante.setVisibility(View.INVISIBLE);

                mapsFragment = new MapsFragment();

                dados.putInt("dados", 3);
                mapsFragment.setArguments(dados);

                FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
                fragmentTransaction3.replace(R.id.passeDeFrames, mapsFragment );
                fragmentTransaction3.commit();
            }
        });
    }
}