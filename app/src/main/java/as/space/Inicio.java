package as.space;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import static as.space.R.layout.pantalla_inicio;

/**
 * Created by Pedro on 18/10/2016.
 */

public class Inicio extends Activity {
    Button btnStart;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(pantalla_inicio);

        btnStart = (Button) findViewById(R.id.boton_start);

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
