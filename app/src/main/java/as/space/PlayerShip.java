package as.space;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

/**
 * Created by josegarben on 26/9/16.
 */

public class PlayerShip {
    // Objetos muy parecidos a los del enemigo (clase Invader)
    RectF rect;

    private Bitmap bitmap;

    private float length;
    private float height;

    private float x;
    private float y;

    private float shipSpeed;

    public final int STOPPED = 0;
    public final int LEFT = 1;
    public final int RIGHT = 2;


    private int shipMoving = STOPPED;


    public PlayerShip(Context context, int screenX, int screenY) {


        rect = new RectF();

        length = screenX / 10;
        height = screenY / 10;

        // Centramos los enemigos al inicio de la partida
        x = screenX / 2;
        y = screenY - 20;

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ship);

        bitmap = Bitmap.createScaledBitmap(bitmap,
                (int) (length),
                (int) (height),
                false);

        shipSpeed = 600;
    }

    public RectF getRect() {
        return rect;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public float getX() {
        return x;
    }

    public float getHeight() {
        return height;
    }

    public float getLength() {
        return length;
    }


    public void setMovementState(int state) {
        shipMoving = state;
    }

    // A este método se le llama desde SpaceInvadersView
    public void update(long fps) {
        if (shipMoving == LEFT && x >= 1) {
            x = x - shipSpeed / fps;
        } else if (shipMoving == RIGHT && x <= length * 9) {
            x = x + shipSpeed / fps;
        }

        rect.top = y;
        rect.bottom = y + height;
        rect.left = x;
        rect.right = x + length;

    }
}
