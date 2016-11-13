package as.space;
//Comentario de prueba - branch desarrollo

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.media.AudioAttributes;
import android.os.Bundle;
import android.view.Display;

public class MainActivity extends Activity {

    SpaceInvadersView spaceInvadersView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setVolumeControlStream(AudioAttributes.CONTENT_TYPE_MUSIC);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        spaceInvadersView = new SpaceInvadersView(this, size.x, size.y,this);
        setContentView(spaceInvadersView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        spaceInvadersView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        spaceInvadersView.pause();
    }
}