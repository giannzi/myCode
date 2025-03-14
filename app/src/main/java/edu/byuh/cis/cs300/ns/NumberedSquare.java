package edu.byuh.cis.cs300.ns;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

import java.util.List;

/**
 * Represents a numbered square that moves around the screen
 */
public class NumberedSquare implements TickListener{
    private float size;
    private int id;
    private RectF bounds;
    private float screenWidth, screenHeight;
    private Paint textPaint;
    private Paint backgroundPaint;
    private PointF velocity;
    private static int counter = 0;
    private PointF point;
    private int velocityX;
    private int velocityY;
    private static Paint red;
    private boolean freeze = false;
    private static Bitmap normalImage;
    private static Bitmap frozenImage;
    private boolean frozen;
    private int danceSpeed;

    public NumberedSquare(Resources res, float w, float h) {
    }


    /**
     * Called once every timer tick
     */
    @Override
    public void tick() {
        move();
    }

    /**
     * Registers a tick listener
     * @param o
     */
    @Override
    public void registerListener(TickListener o) {

    }

    /**
     * Removes a tick listener
     * @param o
     */
    @Override
    public void removeListener(TickListener o) {

    }

    /**
     * Removes all tick listener
     */
    @Override
    public void removeAllListener() {

    }


    private enum HitSide {
        TOP,
        BOTTOM,
        LEFT,
        RIGHT,
        NONE
    }

    /**
     * Constructor for NumberedSquare. This constructor accepts two parameters,
     * representing the width and height of the display. The constructor finds the
     * smaller of these two values (typically, width is smaller for portrait orientation;
     * height is smaller for landscape) and bases the size of the square on that.
     *
     * @param w - the width of the screen
     * @param h - the height of the screen
     */
    public NumberedSquare(float w, float h) {
        screenWidth = w;
        screenHeight = h;
        float lesser = Math.min(w, h);
        size = lesser / 8f;
        float x = (float) (Math.random() * (screenWidth - size));
        float y = (float) (Math.random() * (screenHeight - size));
        bounds = new RectF(x, y, x + size, y + size);
        backgroundPaint = new Paint();
        textPaint = new Paint();
        backgroundPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint.setStrokeWidth(lesser / 100f);
        backgroundPaint.setColor(Color.BLUE);
        textPaint.setTextSize(MainActivity.findThePerfectFontSize(lesser / 20f));
        textPaint.setColor(Color.BLACK);
        textPaint.setTextAlign(Paint.Align.CENTER);
        float randomX = (float) (size * 0.08 - Math.random() * size * 0.16);
        float randomY = (float) (size * 0.08 - Math.random() * size * 0.16);
        velocity = new PointF(randomX, randomY);
        counter++;
        id = counter;
        red=new Paint();
        red.setColor(Color.RED);
        if (normalImage == null || frozenImage == null) {
            normalImage = BitmapFactory.decodeResource(MainActivity.getAppContext().getResources(), R.drawable.cube1000);
            frozenImage = BitmapFactory.decodeResource(MainActivity.getAppContext().getResources(), R.drawable.frozen1000);
        }
        danceSpeed = 10;
    }
    public void setDanceSpeed(int d) {
        danceSpeed = d;
    }

    public void dance() {
        float dx = (float)(Math.random()*danceSpeed)-danceSpeed/2;
        float dy = (float)(Math.random()*danceSpeed)-danceSpeed/2;
        bounds.offset(dx,dy);
    }

    /**
     * Draws the square on the canvas
     *Image is drawn whether frozen or not
     * @param c the Canvas object, representing the display area
     */
    public void draw(Canvas c) {
        Bitmap toDraw;

        if (frozen) {
            toDraw = frozenImage;
        } else {
            toDraw = normalImage;
        }

        c.drawBitmap(toDraw, null, bounds, null);  // Draw the image (background)

        // Draw the square number (centered)
        float textX = bounds.centerX();
        float textY = bounds.centerY() + size / 7;

        Paint textPaint = new Paint();
        textPaint.setTextSize(MainActivity.findThePerfectFontSize(size / 3f));

        if (frozen) {
            textPaint.setColor(0xFF000000);
        } else {
            textPaint.setColor(0xFF000000);
        }

        textPaint.setTextAlign(Paint.Align.CENTER);

        c.drawText(String.valueOf(id), textX, textY, textPaint);
    }



    /**
     * Determines whether this NumberedSquare overlaps another
     *
     * @param other the other NumberedSquare object to compare against
     * @return true if they overlap; false otherwise
     */
    public boolean overlaps(NumberedSquare other) {
        return RectF.intersects(this.bounds, other.bounds);
    }

    /**
     * Move the square by its velocity. If we're near the edges, move
     * in the opposite direction.
     */
    public void move() {
        if (bounds.left < 0 || bounds.right > screenWidth) {
            backupOneStep();
            velocity.x *= -1;
        }
        if (bounds.top < 0 || bounds.bottom > screenHeight) {
            backupOneStep();
            velocity.y *= -1;
        }
        bounds.offset(velocity.x, velocity.y);
    }

    /**
     * Resets the static ID counter
     */
    public static void resetIDs() {
        counter = 0;
    }

    /**
     * Rejects the current square if overlaps another during creation
     */
    public void reject() {
        counter--;
    }

    /**
     * Checks whether this square has collided with any others. If so, both
     * squares velocities are adjusted.
     *
     * @param others list of other squares to compare against this one.
     */
    public void checkForCollisions(List<NumberedSquare> others) {
        for (NumberedSquare other : others) {
            if (other.id > this.id) {
                if (this.overlaps(other)) {
                    HitSide hitSide = HitSide.NONE;
                    float dtop = Math.abs(other.bounds.bottom - this.bounds.top);
                    float dbot = Math.abs(other.bounds.top - this.bounds.bottom);
                    float dleft = Math.abs(other.bounds.right - this.bounds.left);
                    float drt = Math.abs(other.bounds.left - this.bounds.right);
                    float min = Math.min(Math.min(dtop, dbot), Math.min(drt, dleft));
                    if (min == dtop) {
                        hitSide = HitSide.TOP;
                    }
                    if (min == dbot) {
                        hitSide = HitSide.BOTTOM;
                    }
                    if (min == dleft) {
                        hitSide = HitSide.LEFT;
                    }
                    if (min == drt) {
                        hitSide = HitSide.RIGHT;
                    }
                    exchangeMomentum(other, hitSide);
                }
            }
        }
    }

    private void backupOneStep() {
        bounds.offset(-velocity.x, -velocity.y);
    }

    private void exchangeMomentum(NumberedSquare other, HitSide hitside) {
        forceApart(other, hitside);
        if (hitside == HitSide.TOP || hitside == HitSide.BOTTOM) {
            float tmp = this.velocity.y;
            this.velocity.y = other.velocity.y;
            other.velocity.y = tmp;
        } else {
            float tmp = this.velocity.x;
            this.velocity.x = other.velocity.x;
            other.velocity.x = tmp;
        }
    }

    private void forceApart(NumberedSquare other, HitSide hitside) {
        RectF myBounds = new RectF(this.bounds);
        RectF otherBounds = new RectF(other.bounds);
        switch (hitside) {
            case LEFT:
                this.setLeft(otherBounds.right + 1);
                other.setRight(myBounds.left - 1);
                break;
            case RIGHT:
                this.setRight(otherBounds.left - 1);
                other.setLeft(myBounds.right + 1);
                break;
            case TOP:
                this.setTop(otherBounds.bottom + 1);
                other.setBottom(myBounds.top - 1);
                break;
            case BOTTOM:
                this.setBottom(otherBounds.top - 1);
                other.setTop(myBounds.bottom + 1);
        }
    }

    private void setBottom(float b) {
        float dy = b - bounds.bottom;
        bounds.offset(0, dy);
    }

    private void setRight(float r) {
        float dx = r - bounds.right;
        bounds.offset(dx, 0);
    }

    private void setLeft(float lf) {
        bounds.offsetTo(lf, bounds.top);
    }

    private void setTop(float t) {
        bounds.offsetTo(bounds.left, t);
    }

    /**
     * Determine if a touch is inside square
     * @param x
     * @param y
     * @return
     */
    public boolean contains(float x, float y) {
        return bounds.contains(x,y);
    }

    /**
     * Freezes the square
     */
    public void freeze() {
        velocity.set(0, 0);
        frozen = true;  // This makes it switch to frozenImage in draw()
    }

    /**
     * Changes the bck paint of of the square
     * @param c
     */
    public void changeColor(int c) {
        backgroundPaint.setColor(c);
    }

    /**
     * Gets the assigned number to the square
     * @return
     */
    public int getNumber() {
        return id;
    }

    /**
     * Unfreezes the square, restoring movement
     */
    public void unfreeze() {
        float randomX = (float) (size * 0.08 - Math.random() * size * 0.16);
        float randomY = (float) (size * 0.08 - Math.random() * size * 0.16);
        velocity.set(randomX, randomY);
    }

    /**
     * Checks if the square is frozen
     * @return
     */
    public boolean isFrozen() {
        return velocity.x == 0 && velocity.y == 0;
    }

}


