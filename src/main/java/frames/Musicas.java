package frames;

import static java.lang.System.err;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a0411.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.logging.LogRecord;

public class Musicas extends Fragment implements Runnable {

    private Button play;
    private Button pause;
    private Button anterior;
    private Button proximo;
    private SeekBar progressoMusica;
    private MediaPlayer music = new MediaPlayer();
    private ImageView foto;
    private TextView tituloMusica;
    private TextView frases;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private StorageReference imagens;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference frase;

    private int aleatorioMusica = 0;
    private int atual = 0;
    private Random aleatorioMusic = new Random();
    private int back = -1;
    private ArrayList<Integer> antes = new ArrayList<>();
    private int aux = -1;
    private int aux2;
    private int aux3 = 0;
    private int auxAtual = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_musicas, container, false);

        play = view.findViewById(R.id.play);
        pause = view.findViewById(R.id.pause);
        anterior = view.findViewById(R.id.anterior);
        proximo = view.findViewById(R.id.proximo);
        tituloMusica = view.findViewById(R.id.tituloMusica);
        foto = view.findViewById(R.id.imageViewFoto);
        frases = view.findViewById(R.id.frases);

        imagens = storageReference.child("fotos");
        frase = databaseReference.child("frases");

        progressoMusica = view.findViewById(R.id.progressoMusica);

        play.setVisibility(View.INVISIBLE);


        //recuperarMusicas();
        tocarMusicaAleatorio();
        progressoMusica.setEnabled(false);
        progressoMusica.setMax(music.getDuration());
        new Thread(this).start();
        seek();

        controleMusica();
        imagemBaixar();
        frases();

        return view;
    }

    private void seek() {
        progressoMusica.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                try {
                    if (music.isPlaying() || music != null) {
                        if (fromUser)
                            music.seekTo(progress);
                    } else if (music == null) {
                        Toast.makeText(getActivity(), "Media is not running",
                                Toast.LENGTH_SHORT).show();
                        seekBar.setProgress(0);
                    }
                } catch (Exception e) {
                    Log.e("seek bar", "" + e);
                    seekBar.setEnabled(false);

                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void run() {
        int currentPosition = music.getCurrentPosition();
        int total = music.getDuration();

        while (music != null && currentPosition <= total) {
            try {
                Thread.sleep(100);
                currentPosition = music.getCurrentPosition();
            } catch (InterruptedException e) {
                return;
            } catch (Exception e) {
                return;
            }
            progressoMusica.setProgress(currentPosition);

            if (currentPosition==total){
                imagemBaixar();
                frases();
                tocarMusicaAleatorio();
            }
        }
    }

    public void controleMusica(){

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play.setVisibility(View.INVISIBLE);
                pause.setVisibility(View.VISIBLE);

                music.start();
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pause.setVisibility(View.INVISIBLE);
                play.setVisibility(View.VISIBLE);

                music.pause();
            }
        });

        anterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (aux>=0){
                    aux2 = aux;
                    back = aux2;
                    aux--;
                    music.stop();
                    music.reset();
                    pause.setVisibility(View.VISIBLE);
                    play.setVisibility(View.INVISIBLE);
                    tocarMusicaAleatorio();
                    imagemBaixar();
                    frases();

                }

            }
        });

        proximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                music.stop();
                music.reset();
                pause.setVisibility(View.VISIBLE);
                play.setVisibility(View.INVISIBLE);
                tocarMusicaAleatorio();
                imagemBaixar();
                frases();
                //recuperarMusicas();

            }
        });

    }

    public void recuperarMusicasFirebs() {

        aleatorioMusica = aleatorioMusic.nextInt(13);

        if (aleatorioMusica != 0 && aleatorioMusica != atual) {

            atual = aleatorioMusica;

            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            StorageReference storageReference = firebaseStorage.getReferenceFromUrl("gs://project-7910597881712257165.appspot.com/musicas/"+atual+".mp3");

            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {

                    String musicaUri = uri.toString();

                    tocarMusica(musicaUri);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.i("Música Error", e.getMessage());
                }
            });
        }
    }

    public void tocarMusica(String uri){

        try {
            music.setDataSource( getContext().getApplicationContext(), Uri.parse(uri));
            music.setAudioStreamType(AudioManager.STREAM_MUSIC);

            music.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    music.start();

                }
            });

            music.prepareAsync();

        } catch (IOException e) {

            Log.e("Música Error", err.toString());
        }

    }

    public void tocarMusicaAleatorio(){

        if(back>=0){

            aleatorioMusica = antes.get(back);
            back = -1;

        }else {
            aleatorioMusica = aleatorioMusic.nextInt(12);
        }

        if (aleatorioMusica > 0 && aleatorioMusica!=atual) {

            atual = aleatorioMusica;

            if (aux<13) {
                aux++;
                antes.add(atual);
                auxAtual = atual;
                aux3 = aux;

                if (aux == 12){
                    music.pause();
                }
            }

            switch (atual){

                case 1: music = MediaPlayer.create(this.getContext(), R.raw.m1); tituloMusica.setText("Dandelions - Ruth B."); break;
                case 2: music = MediaPlayer.create(this.getContext(), R.raw.m3); tituloMusica.setText("Rosa Linn - SNAP");break;
                case 3: music = MediaPlayer.create(this.getContext(), R.raw.m4); tituloMusica.setText("Wake Up - Julie and the Phantoms");break;
                case 4: music = MediaPlayer.create(this.getContext(), R.raw.m5); tituloMusica.setText("Scar To Your Beautiful - Alessia Cara");break;
                case 5: music = MediaPlayer.create(this.getContext(), R.raw.m6); tituloMusica.setText("Unsaid Emily - Julie and the Phantoms");break;
                case 6: music = MediaPlayer.create(this.getContext(), R.raw.m7); tituloMusica.setText("Maps - Marron 5");break;
                case 7: music = MediaPlayer.create(this.getContext(), R.raw.m8); tituloMusica.setText("Sober Up - AJR");break;
                case 8: music = MediaPlayer.create(this.getContext(), R.raw.m9); tituloMusica.setText("Happier than ever/mashup - Han");break;
                case 9: music = MediaPlayer.create(this.getContext(), R.raw.m10); tituloMusica.setText("Flying Solo - Julie and the Phantoms");break;
                case 10: music = MediaPlayer.create(this.getContext(), R.raw.m11); tituloMusica.setText("Edge of Great - Julie and the Phantoms");break;
                case 11: music = MediaPlayer.create(this.getContext(), R.raw.m12); tituloMusica.setText("Stand Tall - Julie and the Phantoms");break;

                default:
                    Toast.makeText(getContext(), "Erro!!!!", Toast.LENGTH_SHORT).show();

            }

    }else{
            tocarMusicaAleatorio();
        }

        music.start();

    }

    public void imagemBaixar(){

        int imagemA = aleatorioMusic.nextInt(16);

        final StorageReference imagemRef = imagens.child(imagemA+".jpeg");

        final long MEGABYTE = 1024 * 1024;

        imagemRef.getBytes(MEGABYTE).addOnSuccessListener(getActivity(), new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                foto.setImageBitmap(bitmap);
            }
        });
    }

    public void frases(){

        int i = aleatorioMusic.nextInt(21);

        if (aleatorioMusica>0) {

            frase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    frases.setText(snapshot.child("frase" + i).getValue().toString());

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        music.release();
        onDestroyView();
    }

}