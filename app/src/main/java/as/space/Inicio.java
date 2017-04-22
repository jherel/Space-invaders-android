package as.space;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

//import pl.droidsonroids.gif.GifImageView;

import static as.space.R.layout.pantalla_inicio;
import static java.lang.Thread.sleep;

/**
 * Created by Pedro on 18/10/2016.
 */

public class Inicio extends Activity {
    //GifImageView btnStart;
    SoundPool spInicio;
    MediaPlayer mediaPlayer;

    int startGameID;

    public static Activity cerrar;

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer.start();
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(pantalla_inicio);

        mediaPlayer = MediaPlayer.create(this, R.raw.intromusic);
       // btnStart = (GifImageView) findViewById(R.id.gifStart);

        cerrar = this;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes aAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build();

            spInicio = new SoundPool.Builder()
                    .setMaxStreams(3)
                    .setAudioAttributes(aAttributes)
                    .build();
        } else {
            spInicio = new SoundPool(10, AudioManager.STREAM_MUSIC, 1);
        }


        mediaPlayer.start();
        startGameID = spInicio.load(this, R.raw.playerexplode, 1);


       /* btnStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                try {
                    spInicio.play(startGameID, 1, 1, 1, 0, 1);
                    Intent iniciointent = new Intent(Inicio.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(iniciointent);

                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    finish();
                }
            }


        });*/
    }
}
