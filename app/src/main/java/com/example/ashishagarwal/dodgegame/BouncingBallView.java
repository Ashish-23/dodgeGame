package com.example.ashishagarwal.dodgegame;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Ashish Agarwal on 30-10-2015.
 */
public class BouncingBallView extends View {

    private static final String TAG = " BouncingBallView";
    public static final String MyPREFERENCES = "MyPrefs";
    Bitmap b;

    int sec = 0;
    int min = 0, pauseTime;
    private Handler h;
    Paint p;
    private final int FRAME_RATE = 30;
    String time1;
    int height = 0, width = 0, cm = 0;
    int life = -10, lx, ly, f = 0;
    Ball blueBall, redball, greenball, mainBall;
    boolean gameover = false, isLife = false,imageStop = false;
    Context context;
    Rectangle myRectangle;

    int prevX = 0, prevY = 0;
    boolean canImageMove = true;

    //   Button pause;
    boolean isPause = false;
    int x1, y1, dx, dy;

    int actionBarHeight = 0;
    int incSize = 1, winSize = 1;

    public BouncingBallView(final Context context) {

        super(context);

        this.context = context;
        h = new Handler();

        TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        width = metrics.widthPixels;
        height = metrics.heightPixels;

        mainBall = new Ball(width / 2, height / 2, Color.MAGENTA, 50, actionBarHeight);
        prevX = 0;
        prevY = 0;

        blueBall = new Ball(300, 110, Color.BLUE, 50);
        //greenball = new Ball(555, 115, Color.GREEN, 50);
        redball = new Ball(50, 400, Color.RED, 50);

        myRectangle = new Rectangle(150, 150, Color.WHITE, 80, 80, actionBarHeight);

        dx = 10;
        dy = 10;
        x1 = 0;
        y1 = 0;


        b = BitmapFactory.decodeResource(getResources(), R.drawable.tri);
        b = getResizedBitmap(b, 100, 100);

        startAgain();

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {

                sec++;
                if (!isPause) {
                    time1 = "Time = ";
                    f++;
                    cm++;


                    SharedPreferences prefs = context.getSharedPreferences(MyPREFERENCES, context.MODE_PRIVATE);
                    incSize = prefs.getInt("Enemy_size", 1); //0 is the default value.
                    winSize = prefs.getInt("Window_size", 1);

                    width = width + (width * (winSize / 100));
                    height = height + (height * (winSize / 100));

                    if (cm == 5) {
          //              greenball.incrase(incSize);
                        blueBall.incrase(incSize);
                        redball.incrase(incSize);
                        myRectangle.increase(incSize);
                        int tw = b.getWidth() * incSize;
                        int th = b.getHeight() * incSize;
                        b = getResizedBitmap(b, tw, th);

                        winSize = winSize * 2;
                        cm = 0;
                    }
                    if (f == life + pauseTime) {
                        isLife = false;
                        imageStop = false;
                        h.postDelayed(r, FRAME_RATE);
                        life = -10;
                        f = 0;
                    }

                    if (sec > 60) {
                        min++;
                        sec = 0;
                    }
                    if (min < 10) {
                        time1 = time1 + " 0" + min + ":";
                    } else {
                        time1 = time1 + min + ":";
                    }
                    if (sec < 10) {
                        time1 = time1 + "0" + sec;
                    } else {
                        time1 = time1 + sec;
                    }

                }
            }
        }, 1000, 1000);

    }

    protected void onDraw(Canvas c) {

        moveTriangle();

        // ball bouncing on walls
        blueBall.bounce(c, winSize);
        //greenball.bounce(c, winSize);
        redball.bounce(c, winSize);

        myRectangle.bounce(c, winSize);

        // balls bouncing on each other
        blueBall.bounceOff(redball);
        //blueBall.bounceOff(greenball);
        //redball.bounceOff(greenball);

        // myRectangle.bounceOff(redball);

        //draw balls
       // c.drawCircle(greenball.getX(), greenball.getY(), greenball.getRadius(), greenball.getPaint());
        c.drawCircle(redball.getX(), redball.getY(), redball.getRadius(), redball.getPaint());
        c.drawCircle(blueBall.getX(), blueBall.getY(), blueBall.getRadius(), blueBall.getPaint());
//
        c.drawCircle(mainBall.getX(), mainBall.getY(), mainBall.getRadius(), mainBall.getPaint());
//
        c.drawRect(myRectangle.getLeft(), myRectangle.getTop(), myRectangle.getRight(), myRectangle.getBottom(), myRectangle.getPaint());

        //drawing a triangle
        c.drawBitmap(b, x1, y1, p);

        if (!gameover && !isPause && !isLife)
            h.postDelayed(r, FRAME_RATE);
    }

    private void moveTriangle() {
        if (x1 + dx + 100 > width || x1 < 0) {
            dx = dx * -1;
        }

        if (y1 + dy + 100 > height - actionBarHeight || y1 < 0) {
            dy = dy * -1;
        }
        x1 = x1 + dx;
        y1 = y1 + dy;

    }


    public boolean onTouchEvent(MotionEvent event) {

        int dX = 0, dY = 0;


        switch (event.getActionMasked()) {

            case MotionEvent.ACTION_DOWN:

                int rawX1 = (int) event.getRawX();
                int rawY1 = (int) event.getRawY();

                if (prevX == 0 && prevY == 0) {
                    prevX = rawX1;
                    prevY = rawY1;
                }

                dX = (int) (mainBall.getX() - event.getRawX());
                dY = (int) (mainBall.getY() - event.getRawY());
                if (rawX1 - prevX < 30 && rawY1 - prevY - actionBarHeight < 80) {
                    canImageMove = true;
                } else {
                    canImageMove = false;
                }
                Log.v(TAG, "Raw  Event = " + rawX1 + "  " + rawY1 + "\n Ball is at " + prevX + " " + prevY);
                break;

            case MotionEvent.ACTION_MOVE:

                if (canImageMove) {
                    float rawX = event.getRawX();
                    float rawY = event.getRawY();
                    if (rawX < 630 && rawX > 50 && rawY > (100 + actionBarHeight) && rawY < 1050) {
                        mainBall.moveTo(Math.round(rawX) + dX, Math.round(rawY) + dY - actionBarHeight);
                        prevX = Math.round(rawX) + dX;
                        prevY = Math.round(rawY) + dY - actionBarHeight;
                       // Log.v(TAG," in onTouch"+(Math.round(rawY) + dy  - ly));
                        if ((Math.round(rawX) + dX - lx) < 80 && (Math.round(rawY) + dy - ly-actionBarHeight) < 80) {

                            mainBall.moveTo(Math.round(rawX) + dX, Math.round(rawY) + dY - actionBarHeight);
                            isLife = true;
                            life = f;
                            imageStop = true;
                            Log.v(TAG," inside ontouch"+life+ " "+imageStop);

                            SharedPreferences prefs = context.getSharedPreferences(MyPREFERENCES, context.MODE_PRIVATE);
                            pauseTime = prefs.getInt("Pause", 5); //0 is the default value.

                        }
                    }
                }
                break;
            default:
                return false;
        }
        return true;
//        return gd.onTouchEvent(event);
    }

    private Runnable r = new Runnable() {
        @Override
        public void run() {
            if (mainBall.isColliding(redball)  || mainBall.isColliding(blueBall) || myRectangle.isColliding(mainBall) || isTriangleColliding()) {
                gameover = true;
                showConfirmationBox();

            } else {

                invalidate();
            }
       }

    };


    private boolean isTriangleColliding() {

        int midptx, midpty;
        midptx = x1 + 50;
        midpty = y1 + 50;

        if ((Math.abs(midptx - mainBall.getX()) < 40 + mainBall.getRadius()) && (Math.abs(midpty - mainBall.getY()) < mainBall.getRadius() + 40)) {

            return true;
        }
        return false;
    }

    private void showConfirmationBox() {

        final String items = "Wanna play again ?" + "\n" + time1;

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("GameOver");

        builder.setMessage(items);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                startAgain();
            }

        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();

            }
        });
        builder.create().show();
    }

    public void startAgain() {

        isLife = false;
        f = 0;
        life = -10;
        sec = 0;
        min = 0;
        prevX = 0;
        prevY = 0;
        time1 = " ";

        cm = 0;

        mainBall.goTo(width / 2, height / 2);
       // greenball.goTo(200, 200);
        blueBall.goTo(30, 50);
        redball.goTo(50, 400);

        mainBall.setDX(10);
        mainBall.setDY(-10);

    //    greenball.setDX(-20);
     //   greenball.setDY(-15);
        redball.setDX(5);
        redball.setDY(-5);
        blueBall.setDX(15);
        blueBall.setDY(-15);

        myRectangle.setDY(10);
        myRectangle.setDX(10);

        myRectangle.setX(10);
        myRectangle.setDY(10);

        x1 = 0;
        y1 = 0;
        gameover = false;
        h.postDelayed(r, FRAME_RATE);
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    public String pause() {

        Log.v(TAG, "Pause");
        if (isPause) {
            isPause = false;
            h.postDelayed(r, FRAME_RATE);
            return "Pause";
        }
        isPause = true;
        return "Resume";

    }

    public boolean getGameover(){
        return gameover;
    }

    public void setLifexy(int x, int y) {

        lx = x;
        ly = y;


    }

    public boolean getImageShow() {

        return imageStop;
    }
}
