package as.space;

import android.content.res.AssetFileDescriptor;
import android.graphics.RectF;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.ParcelFileDescriptor;
import android.view.MotionEvent;

/**
 * Created by josegarben on 26/9/16.
 */

public class Bullet {
    private float x;
    private float y;
    private RectF rect;


    // Hacia dónde se dispara
    public final int UP = 0;
    public final int DOWN = 1;


    int heading = -1;
    float speed = 950;

    private int width = 10;
    private int height;

    private boolean isActive;

    public Bullet(int screenY) {

        height = screenY / 20;
        isActive = false;

        rect = new RectF();
    }

    public RectF getRect() {
        return rect;
    }

    public boolean getStatus() {
        return isActive;
    }

    public void setInactive() {
        isActive = false;
    }


    public float getImpactPointY() {
        if (heading == DOWN) {
            return y + height;
        } else {
            return y;
        }

    }

    public boolean shoot(float startX, float startY, int direction) {


        if (!isActive) {
            x = startX;
            y = startY;
            heading = direction;
            isActive = true;
            return true;
        }

        // Bala activa
        return false;
    }

    public void update(long fps) {

        // Mover arriba o abajo
        if (heading == UP) {
            y = y - speed / fps;
        } else {
            y = y + speed / fps;
        }

        rect.left = x;
        rect.right = x + width;
        rect.top = y;
        rect.bottom = y + height;

    }

}

