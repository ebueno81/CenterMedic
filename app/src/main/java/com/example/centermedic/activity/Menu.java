package com.example.centermedic.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.centermedic.R;
import com.example.centermedic.adapter.TrendsAdapter;
import com.example.centermedic.api.MySingleton;
import com.example.centermedic.domain.TrendSDomain;
import com.example.centermedic.utils.AlertUtils;

import java.util.ArrayList;

public class Menu extends AppCompatActivity {
    TextView tvUser1;
    CardView cvPaciente;
    ImageView ivCloseOut;
    LinearLayout llCitas, llDoctor, llMnuHome, llMnuDoctor, llMnuCita, llMnuPaciente, llMnuUser;
    private RecyclerView.Adapter adapterTrendsList;
    private  RecyclerView recyclerViewTrends;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);

        llCitas = findViewById(R.id.llCitas);
        llDoctor = findViewById(R.id.llDoctor);

        llMnuHome = findViewById(R.id.llMnuHome);
        llMnuDoctor = findViewById(R.id.llMnuMedico);
        llMnuCita = findViewById(R.id.llMnuCita);
        llMnuPaciente = findViewById(R.id.llMnuPaciente);
        llMnuUser = findViewById(R.id.llMnuUser);

        ivCloseOut = findViewById(R.id.ivCloseOut);
        tvUser1 = findViewById(R.id.tvUser1);
        cvPaciente = findViewById(R.id.cvPaciente1);
        MySingleton singleton = MySingleton.getInstance();
        tvUser1.setText("Hola, " + singleton.getValor());
        initRecyclerView();

        ivCloseOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertUtils.showYesNoDialog(Menu.this, "Confirmación", "¿Estás seguro de que deseas continuar?", new AlertUtils.YesNoListener() {
                    @Override
                    public void onYes() {
                        Intent intent = new Intent(Menu.this, Login.class);
                        startActivity(intent);

                        finishAffinity();
                    }

                    @Override
                    public void onNo() {

                    }


                });

            }
        });
        cvPaciente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llamarPaciente();
            }
        });

        llMnuPaciente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llamarPaciente();
            }
        });

        llCitas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llamarCita();
            }
        });

        llMnuCita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llamarCita();
            }
        });

        llDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llamarDoctor();
            }
        });

        llMnuDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llamarDoctor();
            }
        });

        llMnuUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llamarUsuario();
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initRecyclerView() {
        ArrayList<TrendSDomain> items = new ArrayList<>();

        items.add(new TrendSDomain("Revision detallada por medicina general","Medicina General","medicina"));
        items.add(new TrendSDomain("Tomografias en HD para las embazaradas","Obstetricia","obstetricia"));
        items.add(new TrendSDomain("Atiendete por mejores ginecolos","Ginecología","ginecologia"));
        items.add(new TrendSDomain("Tus dientes necesitan de los mejores","Odontologia","odontologia"));

        recyclerViewTrends = findViewById(R.id.view1);
        recyclerViewTrends.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        adapterTrendsList=new TrendsAdapter((items));
        recyclerViewTrends.setAdapter(adapterTrendsList);
    }

    private void llamarDoctor(){
        Intent intent = new Intent(getApplicationContext(), MainPage.class);
        intent.putExtra("callDoctors", true); // Pasar datos adicionales al Intent
        startActivity(intent);
        finish();
    }

    private void llamarPaciente(){
        Intent i1 = new Intent(getApplicationContext(), MainPage.class);
        startActivity(i1);
        finish();
    }

    private void llamarCita(){
        Intent intent = new Intent(getApplicationContext(), MainPage.class);
        intent.putExtra("ejecutarFuncion", true); // Pasar datos adicionales al Intent
        startActivity(intent);
        finish();
    }

    private void llamarUsuario(){
        Intent intent = new Intent(getApplicationContext(), UserRegister.class);
        intent.putExtra("ejecutarFuncion", true); // Pasar datos adicionales al Intent
        startActivity(intent);
    }
}