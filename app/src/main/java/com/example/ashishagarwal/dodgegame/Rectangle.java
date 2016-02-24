package com.example.ashishagarwal.dodgegame;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;

/**
 * Created by Ashish Agarwal on 02-11-2015.
 */
public class Rectangle {

    private static final String TAG = "Rectangle";
    private Point p;
    private int c;
    private int height, width;
    private int dx;
    private int dy;
    private Paint paint;
    int actionBarHeight;

    public Rectangle(int x, int y, int col, int l, int b,int kk) {

        p = new Point(x, y);
        c = col;

        height = l;
        width = b;

        paint = new Paint();
        paint.setColor(c);

        dx = 0;
        dy = 0;

        actionBarHeight = kk;
    }

    public int getX() {
        return p.x;
    }

    public int getY() {
        return p.y;
    }

    public int getheight() {
        return height;
    }

    public int getwidth() {
        return width;
    }

    public void setDX(int speed) {
        dx = speed;
    }

    public void setDY(int speed) {
        dy = speed;
    }

    public void setX(int speed) {
        p.x = speed;
    }

    public void setY(int speed) {
        p.y = speed;
    }

    public void moveTo(int px, int py) {
        p.x = px;
        p.y = py;
    }

    public void move() {
        p.x = p.x + dx;
        p.y = p.y + dy;


       // Log.v(TAG,p.x+" "+p.y);
    }

    public void bounce(Canvas canvas,int winP) {

        int win = canvas.getWidth()* winP/100;
        int winH = canvas.getHeight()* winP/100;


        move();
        if ((p.x ) > canvas.getWidth() || p.x < 0) {
            dx = dx * -1;
        }
        if ((p.y  ) > canvas.getHeight()|| p.y <(actionBarHeight)) {
            dy = dy * -1;
        }
    }
    public int getLeft(){
        return (p.x-width/2);
    }
    public int getRight(){
        return (p.x+width/2);
    }
    public int getTop(){
        return (p.y-height/2);
    }
    public int getBottom(){
        return (p.y+height/2);
    }

    public Paint getPaint() {
        return paint;
    }


    public boolean isColliding(Ball b) {

        if(Math.abs(b.getX() - p.x) < (b.getRadius()+width/2)  && Math.abs(b.getY()-p.y) < (b.getRadius()+height/2) ){

            return true;
        }
        return false;
    }

    public void bounceOff(Ball b){
        if(Math.abs(b.getX() - p.x) < (b.getRadius()+width/2)  && Math.abs(b.getY()-p.y) < (b.getRadius()+height/2) ){
            dx = dx * -1;
            dy = dy * -1;
        }
    }

    public void increase(int incSize) {

        this.setWidth(this.getwidth() * incSize);
        this.setHeight(this.getheight() * incSize);
    }

    private void setWidth(int i) {

        this.width = i;
    }

    private void setHeight(int i) {

        this.height = i;
    }
}
