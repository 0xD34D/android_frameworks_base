/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.text.method;

<<<<<<< HEAD
=======
import android.util.Log;
import android.view.KeyEvent;
>>>>>>> 54b6cfa... Initial Contribution
import android.view.MotionEvent;
import android.text.*;
import android.widget.TextView;
import android.view.View;

<<<<<<< HEAD
/**
 * A movement method that interprets movement keys by scrolling the text buffer.
 */
public class ScrollingMovementMethod extends BaseMovementMethod implements MovementMethod {
    @Override
    protected boolean left(TextView widget, Spannable buffer) {
        return scrollLeft(widget, buffer, 1);
    }

    @Override
    protected boolean right(TextView widget, Spannable buffer) {
        return scrollRight(widget, buffer, 1);
    }

    @Override
    protected boolean up(TextView widget, Spannable buffer) {
        return scrollUp(widget, buffer, 1);
    }

    @Override
    protected boolean down(TextView widget, Spannable buffer) {
        return scrollDown(widget, buffer, 1);
    }

    @Override
    protected boolean pageUp(TextView widget, Spannable buffer) {
        return scrollPageUp(widget, buffer);
    }

    @Override
    protected boolean pageDown(TextView widget, Spannable buffer) {
        return scrollPageDown(widget, buffer);
    }

    @Override
    protected boolean top(TextView widget, Spannable buffer) {
        return scrollTop(widget, buffer);
    }

    @Override
    protected boolean bottom(TextView widget, Spannable buffer) {
        return scrollBottom(widget, buffer);
    }

    @Override
    protected boolean lineStart(TextView widget, Spannable buffer) {
        return scrollLineStart(widget, buffer);
    }

    @Override
    protected boolean lineEnd(TextView widget, Spannable buffer) {
        return scrollLineEnd(widget, buffer);
    }

    @Override
    protected boolean home(TextView widget, Spannable buffer) {
        return top(widget, buffer);
    }

    @Override
    protected boolean end(TextView widget, Spannable buffer) {
        return bottom(widget, buffer);
    }

    @Override
    public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
        return Touch.onTouchEvent(widget, buffer, event);
    }

    @Override
=======
public class
ScrollingMovementMethod
implements MovementMethod
{
    /**
     * Scrolls the text to the left if possible.
     */
    protected boolean left(TextView widget, Spannable buffer) {
        Layout layout = widget.getLayout();
        
        int scrolly = widget.getScrollY();
        int scr = widget.getScrollX();
        int em = Math.round(layout.getPaint().getFontSpacing());

        int padding = widget.getTotalPaddingTop() +
                      widget.getTotalPaddingBottom();
        int top = layout.getLineForVertical(scrolly);
        int bottom = layout.getLineForVertical(scrolly + widget.getHeight() -
                                               padding);
        int left = Integer.MAX_VALUE;

        for (int i = top; i <= bottom; i++) {
            left = (int) Math.min(left, layout.getLineLeft(i));
        }

        if (scr > left) {
            int s = Math.max(scr - em, left);
            widget.scrollTo(s, widget.getScrollY());
            return true;
        }

        return false;
    }

    /**
     * Scrolls the text to the right if possible.
     */
    protected boolean right(TextView widget, Spannable buffer) {
        Layout layout = widget.getLayout();

        int scrolly = widget.getScrollY();
        int scr = widget.getScrollX();
        int em = Math.round(layout.getPaint().getFontSpacing());

        int padding = widget.getTotalPaddingTop() +
                      widget.getTotalPaddingBottom();
        int top = layout.getLineForVertical(scrolly);
        int bottom = layout.getLineForVertical(scrolly + widget.getHeight() -
                                               padding);
        int right = 0;

        for (int i = top; i <= bottom; i++) {
            right = (int) Math.max(right, layout.getLineRight(i));
        }

        padding = widget.getTotalPaddingLeft() + widget.getTotalPaddingRight();
        if (scr < right - (widget.getWidth() - padding)) {
            int s = Math.min(scr + em, right - (widget.getWidth() - padding));
            widget.scrollTo(s, widget.getScrollY());
            return true;
        }

        return false;
    }

    /**
     * Scrolls the text up if possible.
     */
    protected boolean up(TextView widget, Spannable buffer) {
        Layout layout = widget.getLayout();

        int areatop = widget.getScrollY();
        int line = layout.getLineForVertical(areatop);
        int linetop = layout.getLineTop(line);

        // If the top line is partially visible, bring it all the way
        // into view; otherwise, bring the previous line into view.
        if (areatop == linetop)
            line--;

        if (line >= 0) {
            Touch.scrollTo(widget, layout,
                           widget.getScrollX(), layout.getLineTop(line));
            return true;
        }

        return false;
    }

    /**
     * Scrolls the text down if possible.
     */
    protected boolean down(TextView widget, Spannable buffer) {
        Layout layout = widget.getLayout();

        int padding = widget.getTotalPaddingTop() +
                      widget.getTotalPaddingBottom();

        int areabot = widget.getScrollY() + widget.getHeight() - padding;
        int line = layout.getLineForVertical(areabot);

        if (layout.getLineTop(line+1) < areabot + 1) {
            // Less than a pixel of this line is out of view,
            // so we must have tried to make it entirely in view
            // and now want the next line to be in view instead.

            line++;
        }

        if (line <= layout.getLineCount() - 1) {
            widget.scrollTo(widget.getScrollX(), layout.getLineTop(line+1) -
                            (widget.getHeight() - padding));
            Touch.scrollTo(widget, layout,
                                widget.getScrollX(), widget.getScrollY());
            return true;
        }

        return false;
    }

    public boolean onKeyDown(TextView widget, Spannable buffer, int keyCode, KeyEvent event) {
        boolean handled = false;

        switch (keyCode) {
        case KeyEvent.KEYCODE_DPAD_LEFT:
            handled |= left(widget, buffer);
            break;

        case KeyEvent.KEYCODE_DPAD_RIGHT:
            handled |= right(widget, buffer);
            break;

        case KeyEvent.KEYCODE_DPAD_UP:
            handled |= up(widget, buffer);
            break;

        case KeyEvent.KEYCODE_DPAD_DOWN:
            handled |= down(widget, buffer);
            break;
        }

        return handled;
    }

    public boolean onKeyUp(TextView widget, Spannable buffer, int keyCode, KeyEvent event) {
        return false;
    }

    public boolean onTouchEvent(TextView widget, Spannable buffer,
                                 MotionEvent event) {
        return Touch.onTouchEvent(widget, buffer, event);
    }

    public boolean onTrackballEvent(TextView widget, Spannable buffer,
                                    MotionEvent event) {
        boolean handled = false;
        int x = (int) event.getX();
        int y = (int) event.getY();

        for (; y < 0; y++) {
            handled |= up(widget, buffer);
        }
        for (; y > 0; y--) {
            handled |= down(widget, buffer);
        }

        for (; x < 0; x++) {
            handled |= left(widget, buffer);
        }
        for (; x > 0; x--) {
            handled |= right(widget, buffer);
        }

        return handled;
    }

    public void initialize(TextView widget, Spannable text) { }

    public boolean canSelectArbitrarily() {
        return false;
    }

>>>>>>> 54b6cfa... Initial Contribution
    public void onTakeFocus(TextView widget, Spannable text, int dir) {
        Layout layout = widget.getLayout();

        if (layout != null && (dir & View.FOCUS_FORWARD) != 0) {
            widget.scrollTo(widget.getScrollX(),
                            layout.getLineTop(0));
        }
        if (layout != null && (dir & View.FOCUS_BACKWARD) != 0) {
            int padding = widget.getTotalPaddingTop() +
                          widget.getTotalPaddingBottom();
            int line = layout.getLineCount() - 1;

            widget.scrollTo(widget.getScrollX(),
                            layout.getLineTop(line+1) -
                            (widget.getHeight() - padding));
        }
    }

    public static MovementMethod getInstance() {
        if (sInstance == null)
            sInstance = new ScrollingMovementMethod();

        return sInstance;
    }

    private static ScrollingMovementMethod sInstance;
}
