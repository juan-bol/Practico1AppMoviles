package com.appmoviles.practico1;

import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.design.bottomappbar.BottomAppBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

public class Preguntas extends AppCompatActivity {

    private Vibrator vibe;

    public final static String SUMA ="+";
    public final static String RESTA ="-";
    public final static String MULTIPLICACION ="*";
    public final static String DIVISION ="/";

    private String operacion;
    private int operando1;
    private int operando2;

    private TextView tv_operacion;
    private Button bt_cambiar;
    private Button bt_enviar;
    private TextView tv_mensaje;
    private TextView tv_puntaje;
    private TextView tv_modo;

    private RadioGroup radioGroup;
    private RadioButton rb_option0;
    private RadioButton rb_option1;
    private RadioButton rb_option2;
    private RadioButton rb_option3;

    private int real;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preguntas);

        final Globals g = (Globals) getApplication();
        vibe = (Vibrator) Preguntas.this.getSystemService(Context.VIBRATOR_SERVICE);

        tv_operacion = (TextView) findViewById(R.id.tv_operacion);
        tv_mensaje = (TextView) findViewById(R.id.tv_mensaje);
        tv_puntaje = (TextView) findViewById(R.id.tv_puntaje);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        rb_option0 = (RadioButton) findViewById(R.id.rb_option0);
        rb_option1 = (RadioButton) findViewById(R.id.rb_option1);
        rb_option2 = (RadioButton) findViewById(R.id.rb_option2);
        rb_option3 = (RadioButton) findViewById(R.id.rb_option3);
        tv_modo =(TextView) findViewById(R.id.tv_modo);

        tv_puntaje.setText("Tu puntaje es "+g.getData());

        Intent datos = getIntent();
        final boolean facil = datos.getExtras().getBoolean("FACIL");
        if(facil){
            tv_modo.setText("Dificutad: Fácil");
        } else{
            tv_modo.setText("Dificutad: Difícil");
        }


        generarPregunta(facil);
        generarRespuestas();

        bt_cambiar = (Button) findViewById(R.id.bt_cambiar);
        bt_cambiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generarPregunta(facil);
                generarRespuestas();
            }
        });
        bt_enviar = (Button) findViewById(R.id.bt_enviar);
        bt_enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean correcto = revisarRespuesta();
                if(correcto){
                    tv_mensaje.setText("Has sumado 10 puntos");
                    g.setData(g.getData()+10);
                    tv_puntaje.setText("Tu puntaje es "+g.getData());
                    generarPregunta(facil);
                    generarRespuestas();
                }else{
                    vibe.vibrate(160);
                    tv_mensaje.setText("¡Intentalo de nuevo!");
                }
            }
        });
    }

    public void generarPregunta(boolean facil){
        int oper = new Random().nextInt(4);
        real = 0;
        switch (oper) {
            case 0:
                if(facil){
                    operando1 = new Random().nextInt(51)+1;
                    operando2 = new Random().nextInt(51)+1;
                } else {
                    operando1 = new Random().nextInt(201)+30;
                    operando2 = new Random().nextInt(201)+30;
                }
                operacion=SUMA;
                real = operando1+operando2;
                break;
            case 1:
                if(facil){
                    operando1 = new Random().nextInt(51)+1;
                    operando2 = new Random().nextInt(51)+1;
                } else {
                    operando1 = new Random().nextInt(201)+30;
                    operando2 = new Random().nextInt(201)+30;
                }
                operacion=RESTA;
                real = operando1-operando2;
                break;
            case 2:
                if(facil){
                    operando1 = new Random().nextInt(20)+1;
                    operando2 = new Random().nextInt(10)+1;
                } else {
                    operando1 = new Random().nextInt(40)+1;
                    operando2 = new Random().nextInt(20)+1;
                }
                operacion=MULTIPLICACION;
                real = operando1*operando2;
                break;
            case 3:
                if(facil){
                    operando1 = new Random().nextInt(61)+1;
                    operando2 = new Random().nextInt(3)+1;
                } else {
                    operando1 = new Random().nextInt(201)+1;
                    operando2 = new Random().nextInt(15)+1;
                }
                operacion=DIVISION;
                if(operando1>operando2){
                    real = (Integer) operando1/operando2;
                } else {
                    real = (Integer) operando2/operando1;
                }
                break;
        }

        tv_operacion.setText(operando1+" "+operacion+" "+operando2);

    }

    // Llena los radioButtons y retorna el id del radioButton correcto
    private void generarRespuestas(){
        int[] respuestas = new int[4];
        respuestas[0] = real;
        for(int i=1; i<4; i++){
            int varianza=new Random().nextInt(5)+1;
            if(new Random().nextInt(2)==1){
                respuestas[i] = real+varianza;
            } else {
                respuestas[i] = real-varianza;
            }
        }
        respuestas=shuffle(respuestas);
        rb_option0.setText(respuestas[0]+"");
        rb_option1.setText(respuestas[1]+"");
        rb_option2.setText(respuestas[2]+"");
        rb_option3.setText(respuestas[3]+"");
    }

    private int[] shuffle(int[] array){
        for(int i=array.length; i>1; i--){
            int r = new Random().nextInt(i);
            int temp = array[r];
            array[r] = array[i-1];
            array[i-1] = temp;
        }

        return array;
    }

    private boolean revisarRespuesta(){
        boolean correcto = false;
        int radioId =  radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = findViewById(radioId);
        int respuesta = Integer.parseInt(radioButton.getText().toString());
        if(real==respuesta){
            correcto=true;
        }
        return correcto;
    }
}
