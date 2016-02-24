package com.example.ashishagarwal.dodgegame;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
import android.util.TypedValue;

/**
 * Created by Ashish Agarwal on 30-10-2015.
 */
public class Ball {

    private static final String TAG = "Ball";
    private Point p;
    private int c;
    private int r;
    private int dx;
    private int dy;
    private Paint paint;
    int actionBarHeight;

    public Ball(int x,int y,int col,int radius){

        p = new Point(x,y);
        c = col;
        r = radius;
        paint = new Paint();
        paint.setColor(c);

        dx = 0;
        dy =0;


    }
    public Ball(int x,int y,int col,int radius , int kk){

        p = new Point(x,y);
        c = col;
        r = radius;
        paint = new Paint();
        paint.setColor(c);

        dx = 0;
        dy =0;

        actionBarHeight = kk;
    }

    public int getX(){
        return p.x;
    }
    public int getY(){
        return p.y;
    }

    public int getRadius(){
        return r;
    }

    public Paint getPaint(){
        return paint;
    }

    public void setColor(int col){
        c = col;
    }
    public void goTo(int x,int y){
        p.x =x;
        p.y=y;
    }
    public void setDX(int speed){
        dx=speed;
    }
    public void setDY(int speed){
        dy = speed;
    }

    public void move(){
        p.x = p.x + dx;
        p.y = p.y + dy;
    }
    public void moveTo(int px ,int py){
        p.x = px;
        p.y = py;
    }

    public void bounce(Canvas canvas,int winP){
        int win = canvas.getWidth()* winP/100;
        int winH = canvas.getHeight()* winP/100;
        move();
        if((p.x) > canvas.getWidth() || (p.x) <0){
            dx = dx * -1;
        }
        if(p.y > (canvas.getHeight()-actionBarHeight) ||( p.y) <actionBarHeight){
            dy = dy * -1;
        }
    }
    public void bounceOff(Ball b){
        if((Math.abs(b.getX()-p.x)<b.getRadius()+r) && (Math.abs(b.getY()-p.y)<b.getRadius()+r)){
            dx = dx * -1;
            dy = dy * -1;
        }
    }

    public boolean isColliding(Ball b){
        if((Math.abs(b.getX()-p.x)+20<b.getRadius()+r) && (Math.abs(b.getY()-p.y)+20<b.getRadius()+r)){
           // Log.v(TAG,"Colliding" +b.getX()+" "+p.x+" "+b.getRadius());
            return true;
        }
        return false;
    }


    public void incrase(int incSize) {

        this.setRadius(this.getRadius() * incSize);
    }

    private void setRadius(int rad) {

        this.r = rad;
    }
}
