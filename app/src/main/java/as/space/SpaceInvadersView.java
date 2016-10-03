package as.space;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.AudioManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.media.SoundPool;
import android.content.res.AssetManager;

import java.io.IOException;

public class SpaceInvadersView extends SurfaceView implements Runnable {

    Context context;

    // This is our thread
    private Thread gameThread = null;

    // Our SurfaceHolder to lock the surface before we draw our graphics
    private SurfaceHolder ourHolder;

    // A boolean which we will set and unset
    // when the game is running- or not.
    private volatile boolean playing;

    // Game is paused at the start
    private boolean paused = true;

    // A Canvas and a Paint object
    private Canvas canvas;
    private Paint paint;

    // This variable tracks the game frame rate
    private long fps;

    // This is used to help calculate the fps
    private long timeThisFrame;

    // The size of the screen in pixels
    private int screenX;
    private int screenY;

    // The players ship
    private PlayerShip playerShip;

    // The player's bullet
    private Bullet bullet;

    // How menacing should the sound be?
    private long menaceInterval = 1000;
    // Which menace sound should play next

    // When did we last play a menacing sound
    private long lastMenaceTime = System.currentTimeMillis();

    // When the we initialize (call new()) on gameView
    // This special constructor method runs
    public SpaceInvadersView(Context context, int x, int y) {

        // The next line of code asks the
        // SurfaceView class to set up our object.
        // How kind.
        super(context);

        // Make a globally available copy of the context so we can use it in another method
        this.context = context;

        // Initialize ourHolder and paint objects
        ourHolder = getHolder();
        paint = new Paint();

        screenX = x;
        screenY = y;

        prepareLevel();
    }

    private void prepareLevel() {
        // Reset the menace level
        menaceInterval = 1000;

        // Here we will initialize all the game objects

        // Make a new player space ship
        playerShip = new PlayerShip(context, screenX, screenY);

        // Prepare the players bullet
        bullet = new Bullet(screenY);

    }

    @Override
    public void run() {
        while (playing) {

            // Capture the current time in milliseconds in startFrameTime
            long startFrameTime = System.currentTimeMillis();

            // Update the frame
            if (!paused) {
                update();
            }

            // Draw the frame
            draw();

            // Calculate the fps this frame
            // We can then use the result to
            // time animations and more.
            timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if (timeThisFrame >= 1) {
                fps = 1000 / timeThisFrame;
            }
        }
    }

    private void update() {
        // Move the player's ship
        playerShip.update(fps);
        if (bullet.getStatus()) {
            bullet.update(fps);
        }
        // Has the player's bullet hit the top of the screen
        if (bullet.getImpactPointY() < 0) {
            bullet.setInactive();
        }
    }

    private void draw() {
        // Make sure our drawing surface is valid or we crash
        if (ourHolder.getSurface().isValid()) {
            // Lock the canvas ready to draw
            canvas = ourHolder.lockCanvas();

            // Draw the background color
            canvas.drawColor(Color.argb(255, 71, 71, 107));


            // Choose the brush color for drawing
            paint.setColor(Color.argb(255, 255, 255, 255));

            // Now draw the player spaceship
            canvas.drawBitmap(playerShip.getBitmap(), playerShip.getX(), screenY - playerShip.getHeight(), paint);
            // Draw the players bullet if active

            if (bullet.getStatus()) {
                canvas.drawRect(bullet.getRect(), paint);
            }

            ourHolder.unlockCanvasAndPost(canvas);
        }
    }

    // If as.space.SpaceInvadersActivity is paused/stopped
    // shutdown our thread.
    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            Log.e("Error:", "joining thread");
        }

    }

    // If as.space.SpaceInvadersActivity is started then
    // start our thread.
    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    // The SurfaceView class implements onTouchListener
    // So we can override this method and detect screen touches.
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {

        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {

            // Player has touched the screen
            case MotionEvent.ACTION_DOWN:
                paused = false;
                if (motionEvent.getY() > screenY - screenY / 8) {
                    if (motionEvent.getX() > screenX - screenX / 4) {
                            /*if(!(playerShip.getX() > screenX - playerShip.getLength())){*/
                        playerShip.setMovementState(playerShip.RIGHT);
                        //}
                    } else if (!(motionEvent.getX() > screenX / 4)) {
                        //if (!(playerShip.getX() < 0)) {
                        playerShip.setMovementState(playerShip.LEFT);
                        //}
                    }
                }
                if (motionEvent.getY() < screenY - screenY / 8) {
                    // Shots fired
                    if (bullet.
                            shoot(playerShip.getX() + playerShip.getLength() / 2, screenY - 130, bullet.UP)) {
                        //soundPool.play(shootID, 1, 1, 0, 0, 1);
                    }
                }
                break;
            // Player has removed finger from screen
            case MotionEvent.ACTION_UP:
                playerShip.setMovementState(playerShip.STOPPED);
                break;
        }
        return true;
    }
}