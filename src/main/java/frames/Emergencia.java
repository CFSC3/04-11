package frames;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.a0411.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class Emergencia extends Fragment {

    private Button aleatorio, enviarLigar;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference vales;
    private EditText textVales;
    private Random random = new Random();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_emergencia, container, false);

        aleatorio = view.findViewById(R.id.buttonAleatorio);
        enviarLigar = view.findViewById(R.id.buttonAcao);
        textVales = view.findViewById(R.id.textVales);

        vales = databaseReference.child("vales");

        aleatorios();

        aleatorio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aleatorios();
            }
        });

        enviarLigar();

        return view;
    }

    public void aleatorios(){

        int i = random.nextInt(21);

            vales.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    textVales.setText(snapshot.child(""+i).getValue().toString());

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    public void enviarLigar(){

        enviarLigar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri whatssap = Uri.parse("https://api.whatsapp.com/send?text="+textVales.getText());
                Intent whatssapIntent = new Intent(Intent.ACTION_VIEW, whatssap);
                startActivity(whatssapIntent);

            }
        });

        enviarLigar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                Toast.makeText(getActivity(), "Esse programador te ama muito!!!", Toast.LENGTH_LONG)
                        .show();
                return true;
            }
        });
    }
}