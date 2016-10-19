package as.space;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import pl.droidsonroids.gif.GifImageButton;
import pl.droidsonroids.gif.GifImageView;

import static as.space.R.layout.pantalla_inicio;

/**
 * Created by Pedro on 18/10/2016.
 */

public class Inicio extends Activity {
    GifImageView btnStart;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(pantalla_inicio);

        btnStart = (GifImageView) findViewById(R.id.gifStart);

        btnStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub
                Intent iniciointent = new Intent(Inicio.this, MainActivity.class);
                startActivity(iniciointent);
            }
        });
    }
}
