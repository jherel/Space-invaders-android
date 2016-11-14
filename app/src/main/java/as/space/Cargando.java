package as.space;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioAttributes;
import android.os.Bundle;

import static as.space.R.layout.pantalla_carga;

/**
 * Created by Pedro on 19/10/2016.
 */

public class Cargando extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setVolumeControlStream(AudioAttributes.CONTENT_TYPE_MUSIC);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(pantalla_carga);

        Thread hilo = new Thread(){
            public void run(){
                try{
                    sleep(6000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    Intent intentCarga = new Intent (Cargando.this, Inicio.class);
                    startActivity(intentCarga);

                    finish();
                }
            }
        };
        hilo.start();
    }

}
