package com.appmoviles.practico1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Canje extends AppCompatActivity {

    private ListView lista;
    private TextView tv_puntaje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canje);

        final Globals g = (Globals) getApplication();

        tv_puntaje = (TextView) findViewById(R.id.tv_puntaje);

        tv_puntaje.setText("Tu puntaje es "+g.getData());

        lista = (ListView) findViewById(R.id.lista);

        String[] values = new String[] {"Lapicero Icesi -> 20 pts", "Cuaderno -> 30 pts", "Libreta Icesi -> 40 pts", "Camiseta Icesi -> 80 pts", "Buso Icesi -> 100 pts"};
        final int[] prices = new int[] {20,30,40,80,100};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);

        lista.setAdapter(adapter);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(g.getData()>=prices[position]){
                    String item = (String) lista.getItemAtPosition(position);
                    int precio = prices[position];
                    Toast.makeText(Canje.this, "Compraste "+item+" por $"+precio, Toast.LENGTH_SHORT).show();
                    g.setData(g.getData()-prices[position]);
                    tv_puntaje.setText("Tu puntaje es "+g.getData());
                } else {
                    Toast.makeText(Canje.this, "Puntos insuficientes, ve a una Zona de Preguntas", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
