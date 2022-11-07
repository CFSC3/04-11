package telas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a0411.R;

public class Apresentacao extends AppCompatActivity {

    private ImageView apresentacao;
    private int pass = 0;
    private Button next;
    private TextView apre1, apre2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apresentacao);

        apresentacao = findViewById(R.id.imageViewApresentacao);
        next = findViewById(R.id.buttonNext);
        apre1 = findViewById(R.id.textViewA1);
        apre2 = findViewById(R.id.textViewA2);

        apresentacao.setImageResource(R.drawable.gradiente);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apre1.setVisibility(View.INVISIBLE);
                apre2.setVisibility(View.INVISIBLE);
                pass++;
                if(pass==1){
                    apresentacao.setImageResource(R.drawable.apresentacao1);
                }else if(pass==2){
                    apresentacao.setImageResource(R.drawable.apresentacao2);
                }else if(pass==3){
                    apresentacao.setImageResource(R.drawable.apresentacao3);
                }else{
                    Intent intent = new Intent(Apresentacao.this, TelaInicio.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}