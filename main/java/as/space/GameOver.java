package as.space;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import static as.space.R.layout.game_over;
import static java.lang.Thread.sleep;

/**
 * Created by Pedro on 13/11/2016.
 */

public class GameOver extends Activity {
    TextView volverJugar, puntuacion, salir;
    int contador = 0;

    private Inicio inicio = new Inicio();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(game_over);

        volverJugar = (TextView) findViewById(R.id.txtJugar);
        puntuacion = (TextView) findViewById(R.id.txtPuntuacion);
        salir = (TextView) findViewById(R.id.txtSalir);

        final AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setDuration(2000);
        fadeIn.setStartOffset(2500);
        fadeIn.setFillAfter(true);

        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                contador++;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(contador < 1) {
                    volverJugar.startAnimation(fadeIn);
                    puntuacion.startAnimation(fadeIn);
                    salir.startAnimation(fadeIn);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        try{
            volverJugar.startAnimation(fadeIn);
            sleep(1000);
            puntuacion.startAnimation(fadeIn);
            sleep(1000);
        }catch(InterruptedException e){
            e.printStackTrace();
        }finally{
            salir.startAnimation(fadeIn);
        }

        volverJugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent volverIntent = new Intent(GameOver.this, MainActivity.class);
                startActivity(volverIntent);
            }
        });

        puntuacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(GameOver.this, "Aún no se pueden ver las puntuaciones", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });

        salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(
                        GameOver.this);

                dialog.setMessage("¿Realmente desea salir de la aplicación?");
                dialog.setCancelable(false);
                dialog.setPositiveButton("Sí",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                try{
                                    sleep(0);
                                    inicio.cerrar.finish();
                                }catch(InterruptedException e){
                                    e.printStackTrace();
                                }finally{
                                    finish();
                                }
                                System.exit(0);
                            }
                        });
                dialog.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.cancel();
                            }
                        });
                dialog.show();
            }
        });

    }

}
