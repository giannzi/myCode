package edu.byuh.cis.cs300.ns;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.List;

public class GameView extends View implements TickListener{

    private boolean init;
    private float w,h;
    private List<NumberedSquare> squares;
    private Timer tim;
    private int expectedSquareNumber;
    private int level;
    private Bitmap backgroundImage;
    private RectF rec;



    /**
     * Handler is an Android class that allows a method (handleMessage)
     * to be called at regular intervals. We extend the class so we can
     * override handleMessage to do what we want. For this project, we
     * don't use the Message object for anything.
     */
//    private class Timer extends Handler {
//
//        Timer() {
//            sendMessageDelayed(obtainMessage(), 50);
//        }
//
//        @Override
//        public void handleMessage(Message m) {
//            for (NumberedSquare ns : squares) {
//                ns.move();
//                ns.checkForCollisions(squares);
//            }
//            invalidate();
//            sendMessageDelayed(obtainMessage(), 50);
//        }
//    }


    /**
     * GameView constructor. Just initializes a few variables
     * @param context - required by API
     */
    public GameView(Context context) {
        super(context);
        init = false;
        squares = new ArrayList<>();
        tim = new Timer();
        tim.registerListener(this);
        level = 1;  // Start with Level 1
        expectedSquareNumber = 1;
        createSquares();
        // Show "Level 1" Toast when game starts
        Toast.makeText(context, "Level 1", Toast.LENGTH_SHORT).show();
        backgroundImage = BitmapFactory.decodeResource(getResources(), R.drawable.temple);
        rec = new RectF(0, 0, w, h);
    }


    /**
     * This method is called by Android when the view size is known.
     * We trigger square creation here so the squares know the screen size.
     */
    @Override
    protected void onSizeChanged(int width, int height, int oldw, int oldh) {
        super.onSizeChanged(width, height, oldw, oldh);
        w = width;
        h = height;
        createSquares();
    }

    /**
     * All the rendering happens here. The first time onDraw is called, we also do some
     * one-time initialization of the graphics objects (since the width and height of
     * the screen are not known until onDraw is called).
     * @param c
     */
    @Override
    public void onDraw(Canvas c) {
        c.drawBitmap(backgroundImage, null, new RectF(0, 0, w, h), null);
        for (NumberedSquare ns : squares) {
            ns.draw(c);
        }
    }


    /**
     * Helper method for creating numbered squares for current level
     */
    private void createSquares() {
        Resources res = getResources();
        float w = getWidth();
        float h = getHeight();
        int danceSpeed = Preferences.getSpeedPref(getContext());
        squares.clear();
        NumberedSquare.resetIDs();
        for (int i=0; i<level; i++) {
            NumberedSquare lauren = new NumberedSquare(res, w, h);
            lauren.setDanceSpeed(danceSpeed);
            while (true) {
                var candidate = new NumberedSquare(w,h);
                var legal = true;
                for (var other : squares) {
                    if (other.overlaps(candidate)) {
                        legal = false;
                        candidate.reject();
                        break;  //break out of the for loop... no need to keep checking once an overlap is found
                    }
                }
                //at this point, legal is only true if there were no overlaps
                //so we're safe to add the new NS to the family.
                if (legal) {
                    squares.add(candidate);
                    tim.registerListener(candidate);
                    break; //break out of the infinite while loop
                }
            }
        }
    }

    /**
     * Handle touch events
     * Checks if touching is in correct order
     * @param me - the MotionEvent object that encapsulates all the data about this
     *           particular touch event. Supplied by OS.
     * @return true, always.
     */
    @Override
    public boolean onTouchEvent(MotionEvent me) {
        if (me.getAction() == MotionEvent.ACTION_DOWN) {
            float touchX = me.getX();
            float touchY = me.getY();

            for (NumberedSquare ns : squares) {
                if (ns.contains(touchX, touchY)) {
                    if (ns.getNumber() == expectedSquareNumber) {
                        ns.freeze();
                        ns.changeColor(Color.RED);
                        expectedSquareNumber++;
                    } else {
                        ((MainActivity)getContext()).runOnUiThread(() ->
                                Toast.makeText(getContext(), "Try Again! Touch Square #" + expectedSquareNumber, Toast.LENGTH_SHORT).show()
                        );
                    }
                    break;
                }
            }
            invalidate();
        }
        return true;
    }


    private void resetGame() {
        for (NumberedSquare ns : squares) {
            ns.unfreeze();  // You might need to create this method in NumberedSquare
            ns.changeColor(Color.BLUE);  // Back to original color
        }
        expectedSquareNumber = 1;  // Restart order
    }

    private void levelUp() {
        level++;
        ((MainActivity)getContext()).runOnUiThread(() ->
                Toast.makeText(getContext(), "Level " + level, Toast.LENGTH_SHORT).show()
        );
        resetGameForNewLevel();
    }


    private void resetGameForNewLevel() {
        expectedSquareNumber = 1;
        createSquares();
    }

    /**
     * Called by Timer at regular intervals
     */
    @Override
    public void tick() {
        boolean allFrozen = true;
        for (var sq : squares) {
            sq.checkForCollisions(squares);
            if (!sq.isFrozen()) {
                allFrozen = false;
            }
        }
        if (allFrozen) {
            levelUp();
        }
        invalidate();
    }

    /**
     * Registers a new tick listener
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
     * Removes all registered tick listeners
     */
    @Override
    public void removeAllListener() {

    }
}
