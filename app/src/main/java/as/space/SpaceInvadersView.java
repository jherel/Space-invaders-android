package as.space;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.media.SoundPool;
import android.content.res.AssetManager;

import java.io.IOException;

import static java.lang.Thread.sleep;

public class SpaceInvadersView extends SurfaceView implements Runnable {

    Context context;

    // Nuestro hilo
    private Thread gameThread = null;

    // Nuestro SurfaceHolder para bloquear el surface antes de dibujar nuestros gráficos
    private SurfaceHolder ourHolder;

    // Un boolean para hacer set y unset cuando el juego está corriendo o no
    private volatile boolean playing;

    // El juego está parado al principio
    private boolean paused = true;

    // Un Canvas y un objeto Paint
    private Canvas canvas;

    private Paint paint;

    // Esta variable llevala velocidad de las imágenes del juego
    private long fps;

    // Para calcular los fps
    private long timeThisFrame;

    // Tamaño de la pantalla en píxeles
    private int screenX;
    private int screenY;

    // El objeto jugador
    private PlayerShip playerShip;

    // La bala del jugador
    private Bullet bullet;
    // Las balas de los enemigos
    private Bullet[] invadersBullets = new Bullet[200];
    private int nextBullet;
    private int maxInvaderBullets = 10;

    // Hasta 60 invaders
    Invader[] invaders = new Invader[60];
    int numInvaders = 0;

    // Los bloques para defender a los invasores
    private DefenceBrick[] bricks = new DefenceBrick[400];
    private int numBricks;

    // Para el sonido FX
    private SoundPool soundPool;
    private int playerExplodeID = -1;
    private int invaderExplodeID = -1;
    private int shootID;
    private int gameoverID;
    private int damageShelterID = -1;
    private int uhID = -1;
    private int ohID = -1;

    //Puntuación
    int score = 0;
    //Vidas

    // Cómo debe ser el sonido
    private long menaceInterval = 1000;

    private boolean uhOrOh;

    private long lastMenaceTime = System.currentTimeMillis();


    Bitmap background = BitmapFactory.decodeResource(getResources(), R.drawable.backgroun_d);


    // Cuando inicializamos en gameView comienzan a correr los métodos del constructor
    public SpaceInvadersView(Context context, int x, int y, Activity activity) {
        super(context);
        activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        // Hacemos una copia del context global para poder usarlo en otros métodos
        this.context = context;

        ourHolder = getHolder();
        paint = new Paint();

        screenX = x;
        screenY = y;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes aAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setMaxStreams(20)
                    .setAudioAttributes(aAttributes)
                    .build();
        } else {
            soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 1);
        }

        shootID = soundPool.load(context, R.raw.shoot, 1);
        gameoverID = soundPool.load(context, R.raw.gameover, 1);

        background = resizeImage(background, screenX, screenY);

        prepareLevel();
    }

    private void prepareLevel() {

        menaceInterval = 1000;

        // Aquí iniciaremos todos los objetos del juego

        playerShip = new PlayerShip(context, screenX, screenY);

        bullet = new Bullet(screenY);


        for (int i = 0; i < invadersBullets.length; i++) {
            invadersBullets[i] = new Bullet(screenY);
        }


        numInvaders = 0;
        for (int column = 0; column < 2; column++) {
            for (int row = 0; row < 2; row++) {
                invaders[numInvaders] = new Invader(context, row, column, screenX, screenY);
                numInvaders++;
            }
        }

        numBricks = 0;
        for (int shelterNumber = 0; shelterNumber < 4; shelterNumber++) {
            for (int column = 0; column < 10; column++) {
                for (int row = 0; row < 5; row++) {
                    bricks[numBricks] = new DefenceBrick(row, column, shelterNumber, screenX, screenY);
                    numBricks++;
                }
            }
        }
    }

    @Override
    public void run() {
        while (playing) {

            // Guardamos el tiempo actual en milisegundos
            long startFrameTime = System.currentTimeMillis();

            // Actualizamos la pantalla si el juego no está en pausa
            if (!paused) {
                update();
            }

            draw();

            // Calculamos los fps en esta pantalla para poder usarlos luego
            timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if (timeThisFrame >= 1) {
                fps = 1000 / timeThisFrame;
            }

            if (!paused) {
                if ((startFrameTime - lastMenaceTime) > menaceInterval) {
                    // ¿PUEDO BORRAR ESTO?
                    if (uhOrOh) {
                        // Play Uh
                        //soundPool.play(uhID, 1, 1, 0, 0, 1);

                    } else {
                        // Play Oh
                        //soundPool.play(ohID, 1, 1, 0, 0, 1);
                    }

                    // Reiniciamos el tiempo anterior en milisegundos
                    lastMenaceTime = System.currentTimeMillis();
                    // ¿BORRAR?
                    // Alter value of uhOrOh
                    uhOrOh = !uhOrOh;
                }
            }// if !paused
        }// while
    }// run

    private void update() {
        // Si un enemigo ha chocado cona un lado de la pantalla
        boolean bumped = false;
        // Si el jugador a perdido
        boolean lost = false;
        // Actualizar el movimiento del jugador
        playerShip.update(fps);

        // Actualizar todos los enemigos que haya aún vivos
        for (int i = 0; i < numInvaders; i++) {
            if (invaders[i].getVisibility()) {
                // Mover el siguiente enemigo
                invaders[i].update(fps);
                // Si quiere disparar
                if (invaders[i].takeAim(playerShip.getX(), playerShip.getLength())) {
                    // Hacer una bala para el enemigo
                    if (invadersBullets[nextBullet].shoot(invaders[i].getX() + invaders[i].getLength() / 2, invaders[i].getY(), bullet.DOWN)) {
                        // Disparar y preparar para el próximo disparo
                        nextBullet++;
                        // No sacar otra bala mientras la anterior no haya completado su recorrido
                        if (nextBullet == maxInvaderBullets) {
                            nextBullet = 0;
                        }
                    }
                }
                // Si el enemigo choca contra un lado, cambiar el boolean a true
                if (invaders[i].getX() > screenX - invaders[i].getLength()
                        || invaders[i].getX() < 0) {
                    bumped = true;
                }
            }
        }

        // Actualizar todas las balas de los enemigos que estén viéndose
        for (int i = 0; i < invadersBullets.length; i++) {
            if (invadersBullets[i].getStatus()) {
                invadersBullets[i].update(fps);
            }

        }
        // Si se chocan
        if (bumped) {
            // Mover los enemigos hacia abajo y cambiar de dirección
            for (int i = 0; i < numInvaders; i++) {
                invaders[i].dropDownAndReverse();
                // Si los enemigos llegan abajo
                if (invaders[i].getY()+100 > screenY - screenY / 10) {
                    lost = true;
                }
            }
            // Incrementar el nivel haciendo los sonidos más frecuentes
            menaceInterval = menaceInterval - 80;
        }

        //Actualizar la bala del jugador
        if (bullet.getStatus()) {
            bullet.update(fps);
        }

        // Comprobar que la bala del jugador ha llegado arriba
        if (bullet.getImpactPointY() < 0) {
            bullet.setInactive();
        }

        // Comprobar que la bala de los enemigos ha llegado abajo
        for (int i = 0; i < invadersBullets.length; i++) {
            if (invadersBullets[i].getImpactPointY() > screenY) {
                invadersBullets[i].setInactive();
            }
        }
        // Comprobar si la bala del jugador ha dado a un enemigo
        if (bullet.getStatus()) {
            for (int i = 0; i < numInvaders; i++) {
                if (invaders[i].getVisibility()) {
                    if ((RectF.intersects(bullet.getRect(), invaders[i].getRect())) && (numInvaders < 1)) {
                        invaders[i].setInvisible();
                        //soundPool.play(invaderExplodeID, 1, 1, 0, 0, 1);
                        bullet.setInactive();
                        //prepareLevel();
                        score = score + 10;

                        // Cuando el jugador gana
                        if(score == numInvaders * 10){
                            paused = true;
                            score = 0;
                            //lives = 3;
                            prepareLevel();

                        }

                    } else if (RectF.intersects(bullet.getRect(), invaders[i].getRect())) {
                        invaders[i].setInvisible();
                        bullet.setInactive();
                    }
                }
            }
        }
        // Comprobar si una bala enemiga ha chocado contra un bloque
        for (int i = 0; i < invadersBullets.length; i++) {
            if (invadersBullets[i].getStatus()) {
                for (int j = 0; j < numBricks; j++) {
                    if (bricks[j].getVisibility()) {
                        if (RectF.intersects(invadersBullets[i].getRect(), bricks[j].getRect())) {
                            // A collision has occurred
                            invadersBullets[i].setInactive();
                            bricks[j].setInvisible();
                            soundPool.play(damageShelterID, 1, 1, 0, 0, 1);
                        }
                    }
                }
            }

        }
        // Comprobar si una bala del jugador ha chocado contra un bloque
        if (bullet.getStatus()) {
            for (int i = 0; i < numBricks; i++) {
                if (bricks[i].getVisibility()) {
                    if (RectF.intersects(bullet.getRect(), bricks[i].getRect())) {
                        // A collision has occurred
                        bullet.setInactive();
                        bricks[i].setInvisible();
                        //soundPool.play(damageShelterID, 1, 1, 0, 0, 1);
                    }
                }
            }
        }
        // Comprobar si la bala enemiga ha impactado en el jugador
        for (int i = 0; i < invadersBullets.length; i++) {
            if (invadersBullets[i].getStatus()) {
                if (RectF.intersects(playerShip.getRect(), invadersBullets[i].getRect())) {
                    invadersBullets[i].setInactive();
                    gameover();
                    System.exit(0);
                    /*lives --;
                    soundPool.play(playerExplodeID, 1, 1, 0, 0, 1);

                    // Is it game over?
                    if(lives == 0){
                        paused = true;
                        lives = 3;
                        score = 0;
                        prepareLevel();

                    }
                    */
                }
            }
        }

        if (lost) {
            soundPool.play(gameoverID, 1, 1, 1, 0, 1); // codigo anterior
            try {
                sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            gameover();
            System.exit(0);
            //prepareLevel();
        }


    }//fin update

    private void gameover() {
        context.startActivity(new Intent(context, GameOver.class));
    }

    private void draw() {
        if (ourHolder.getSurface().isValid()) {
            // Comprobar si ya podemos dibujar en el canvas
            canvas = ourHolder.lockCanvas();
            //canvas.drawColor(Color.argb(255, 0, 0, 0));
            synchronized (ourHolder) {
                canvas.drawColor(Color.BLACK);
                canvas.drawBitmap(background, 0, 0, null);
            }
            paint.setColor(Color.argb(255, 255, 255, 255));

            // Dibujar el jugador
            canvas.drawBitmap(playerShip.getBitmap(), playerShip.getX(), screenY - playerShip.getHeight(), paint);

            // Dibujar los enemigos
            for (int i = 0; i < numInvaders; i++) {
                if (invaders[i].getVisibility()) {
                    if (uhOrOh) {
                        canvas.drawBitmap(invaders[i].getBitmap(), invaders[i].getX(), invaders[i].getY(), paint);
                    } else {
                        canvas.drawBitmap(invaders[i].getBitmap2(), invaders[i].getX(), invaders[i].getY(), paint);
                    }
                }
            }

            // Draibujar las balas del jugador
            if (bullet.getStatus()) {
                canvas.drawRect(bullet.getRect(), paint);
            }

            // Dibujar las balas enemigas
            for (int i = 0; i < invadersBullets.length; i++) {
                if (invadersBullets[i].getStatus()) {
                    canvas.drawRect(invadersBullets[i].getRect(), paint);
                }
            }

            // Dibujar los bloques
            for (int i = 0; i < numBricks; i++) {
                if (bricks[i].getVisibility()) {
                    paint.setColor(Color.argb(255, 89, 89, 89));
                    canvas.drawRect(bricks[i].getRect(), paint);
                }
            }

            //Puntos
            paint.setColor(Color.argb(255,  249, 129, 0));
            paint.setTextSize(40);
            canvas.drawText("Puntos: " + score, 10,50, paint);

            ourHolder.unlockCanvasAndPost(canvas);
        }
    }


    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            Log.e("Error:", "joining thread");
        }

    }

    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }


    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {

        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {

            // Cuando se toca la pantalla
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
                    // Disparo
                    //soundPool.play(shootID,1,1,1,0,1);
                    if (bullet.
                            shoot(playerShip.getX() + playerShip.getLength() / 2, screenY - 130, bullet.UP)) {
                        soundPool.play(shootID, 1, 1, 1, 0, 1); // codigo anterior

                    }
                }
                break;
            // Se deja de tocar la pantalla
            case MotionEvent.ACTION_UP:
                playerShip.setMovementState(playerShip.STOPPED);
                break;
        }
        return true;
    }

    public Bitmap resizeImage(Bitmap image, int maxWidth, int maxHeight) {
        Bitmap resizedImage = null;
        try {
            int imageHeight = image.getHeight();


            if (imageHeight > maxHeight)
                imageHeight = maxHeight;
            int imageWidth = (imageHeight * image.getWidth())
                    / image.getHeight();

            if (imageWidth > maxWidth) {
                imageWidth = maxWidth;
                imageHeight = (imageWidth * image.getHeight())
                        / image.getWidth();
            }

            if (imageHeight > maxHeight)
                imageHeight = maxHeight;
            if (imageWidth > maxWidth)
                imageWidth = maxWidth;


            resizedImage = Bitmap.createScaledBitmap(image, imageWidth,
                    imageHeight, true);
        } catch (OutOfMemoryError e) {

            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resizedImage;
    }
}