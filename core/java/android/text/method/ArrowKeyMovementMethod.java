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
import android.graphics.Rect;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * A movement method that provides cursor movement and selection.
 * Supports displaying the context menu on DPad Center.
 */
public class ArrowKeyMovementMethod extends BaseMovementMethod implements MovementMethod {
    private static boolean isSelecting(Spannable buffer) {
        return ((MetaKeyKeyListener.getMetaState(buffer, MetaKeyKeyListener.META_SHIFT_ON) == 1) ||
                (MetaKeyKeyListener.getMetaState(buffer, MetaKeyKeyListener.META_SELECTING) != 0));
    }

    private static int getCurrentLineTop(Spannable buffer, Layout layout) {
        return layout.getLineTop(layout.getLineForOffset(Selection.getSelectionEnd(buffer)));
    }

    private static int getPageHeight(TextView widget) {
        // This calculation does not take into account the view transformations that
        // may have been applied to the child or its containers.  In case of scaling or
        // rotation, the calculated page height may be incorrect.
        final Rect rect = new Rect();
        return widget.getGlobalVisibleRect(rect) ? rect.height() : 0;
    }

    @Override
    protected boolean handleMovementKey(TextView widget, Spannable buffer, int keyCode,
            int movementMetaState, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_CENTER:
                if (KeyEvent.metaStateHasNoModifiers(movementMetaState)) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN
                            && event.getRepeatCount() == 0
                            && MetaKeyKeyListener.getMetaState(buffer,
                                        MetaKeyKeyListener.META_SELECTING) != 0) {
                        return widget.showContextMenu();
                    }
                }
                break;
        }
        return super.handleMovementKey(widget, buffer, keyCode, movementMetaState, event);
    }

    @Override
    protected boolean left(TextView widget, Spannable buffer) {
        final Layout layout = widget.getLayout();
        if (isSelecting(buffer)) {
            return Selection.extendLeft(buffer, layout);
        } else {
            return Selection.moveLeft(buffer, layout);
        }
    }

    @Override
    protected boolean right(TextView widget, Spannable buffer) {
        final Layout layout = widget.getLayout();
        if (isSelecting(buffer)) {
            return Selection.extendRight(buffer, layout);
        } else {
            return Selection.moveRight(buffer, layout);
        }
    }

    @Override
    protected boolean up(TextView widget, Spannable buffer) {
        final Layout layout = widget.getLayout();
        if (isSelecting(buffer)) {
            return Selection.extendUp(buffer, layout);
        } else {
            return Selection.moveUp(buffer, layout);
        }
    }

    @Override
    protected boolean down(TextView widget, Spannable buffer) {
        final Layout layout = widget.getLayout();
        if (isSelecting(buffer)) {
            return Selection.extendDown(buffer, layout);
        } else {
            return Selection.moveDown(buffer, layout);
        }
    }

    @Override
    protected boolean pageUp(TextView widget, Spannable buffer) {
        final Layout layout = widget.getLayout();
        final boolean selecting = isSelecting(buffer);
        final int targetY = getCurrentLineTop(buffer, layout) - getPageHeight(widget);
        boolean handled = false;
        for (;;) {
            final int previousSelectionEnd = Selection.getSelectionEnd(buffer);
            if (selecting) {
                Selection.extendUp(buffer, layout);
            } else {
                Selection.moveUp(buffer, layout);
            }
            if (Selection.getSelectionEnd(buffer) == previousSelectionEnd) {
                break;
            }
            handled = true;
            if (getCurrentLineTop(buffer, layout) <= targetY) {
                break;
            }
        }
        return handled;
    }

    @Override
    protected boolean pageDown(TextView widget, Spannable buffer) {
        final Layout layout = widget.getLayout();
        final boolean selecting = isSelecting(buffer);
        final int targetY = getCurrentLineTop(buffer, layout) + getPageHeight(widget);
        boolean handled = false;
        for (;;) {
            final int previousSelectionEnd = Selection.getSelectionEnd(buffer);
            if (selecting) {
                Selection.extendDown(buffer, layout);
            } else {
                Selection.moveDown(buffer, layout);
            }
            if (Selection.getSelectionEnd(buffer) == previousSelectionEnd) {
                break;
            }
            handled = true;
            if (getCurrentLineTop(buffer, layout) >= targetY) {
                break;
            }
        }
        return handled;
    }

    @Override
    protected boolean top(TextView widget, Spannable buffer) {
        if (isSelecting(buffer)) {
            Selection.extendSelection(buffer, 0);
        } else {
            Selection.setSelection(buffer, 0);
        }
        return true;
    }

    @Override
    protected boolean bottom(TextView widget, Spannable buffer) {
        if (isSelecting(buffer)) {
            Selection.extendSelection(buffer, buffer.length());
        } else {
            Selection.setSelection(buffer, buffer.length());
        }
        return true;
    }

    @Override
    protected boolean lineStart(TextView widget, Spannable buffer) {
        final Layout layout = widget.getLayout();
        if (isSelecting(buffer)) {
            return Selection.extendToLeftEdge(buffer, layout);
        } else {
            return Selection.moveToLeftEdge(buffer, layout);
        }
    }

    @Override
    protected boolean lineEnd(TextView widget, Spannable buffer) {
        final Layout layout = widget.getLayout();
        if (isSelecting(buffer)) {
            return Selection.extendToRightEdge(buffer, layout);
        } else {
            return Selection.moveToRightEdge(buffer, layout);
        }
    }

    /** {@hide} */
    @Override
    protected boolean leftWord(TextView widget, Spannable buffer) {
        final int selectionEnd = widget.getSelectionEnd();
        final WordIterator wordIterator = widget.getWordIterator();
        wordIterator.setCharSequence(buffer, selectionEnd, selectionEnd);
        return Selection.moveToPreceding(buffer, wordIterator, isSelecting(buffer));
    }

    /** {@hide} */
    @Override
    protected boolean rightWord(TextView widget, Spannable buffer) {
        final int selectionEnd = widget.getSelectionEnd();
        final WordIterator wordIterator = widget.getWordIterator();
        wordIterator.setCharSequence(buffer, selectionEnd, selectionEnd);
        return Selection.moveToFollowing(buffer, wordIterator, isSelecting(buffer));
    }

    @Override
    protected boolean home(TextView widget, Spannable buffer) {
        return lineStart(widget, buffer);
    }

    @Override
    protected boolean end(TextView widget, Spannable buffer) {
        return lineEnd(widget, buffer);
    }

    @Override
    public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
        int initialScrollX = -1;
        int initialScrollY = -1;
        final int action = event.getAction();

        if (action == MotionEvent.ACTION_UP) {
            initialScrollX = Touch.getInitialScrollX(widget, buffer);
            initialScrollY = Touch.getInitialScrollY(widget, buffer);
        }

        boolean handled = Touch.onTouchEvent(widget, buffer, event);

        if (widget.isFocused() && !widget.didTouchFocusSelect()) {
            if (action == MotionEvent.ACTION_DOWN) {
              if (isSelecting(buffer)) {
                  int offset = widget.getOffsetForPosition(event.getX(), event.getY());

                  buffer.setSpan(LAST_TAP_DOWN, offset, offset, Spannable.SPAN_POINT_POINT);

                  // Disallow intercepting of the touch events, so that
                  // users can scroll and select at the same time.
                  // without this, users would get booted out of select
                  // mode once the view detected it needed to scroll.
                  widget.getParent().requestDisallowInterceptTouchEvent(true);
              }
            } else if (action == MotionEvent.ACTION_MOVE) {
                if (isSelecting(buffer) && handled) {
                    // Before selecting, make sure we've moved out of the "slop".
                    // handled will be true, if we're in select mode AND we're
                    // OUT of the slop

                    // Turn long press off while we're selecting. User needs to
                    // re-tap on the selection to enable long press
                    widget.cancelLongPress();

                    // Update selection as we're moving the selection area.

                    // Get the current touch position
                    int offset = widget.getOffsetForPosition(event.getX(), event.getY());

                    Selection.extendSelection(buffer, offset);
                    return true;
                }
            } else if (action == MotionEvent.ACTION_UP) {
                // If we have scrolled, then the up shouldn't move the cursor,
                // but we do need to make sure the cursor is still visible at
                // the current scroll offset to avoid the scroll jumping later
                // to show it.
                if ((initialScrollY >= 0 && initialScrollY != widget.getScrollY()) ||
                    (initialScrollX >= 0 && initialScrollX != widget.getScrollX())) {
                    widget.moveCursorToVisibleOffset();
                    return true;
                }

                int offset = widget.getOffsetForPosition(event.getX(), event.getY());
                if (isSelecting(buffer)) {
                    buffer.removeSpan(LAST_TAP_DOWN);
                    Selection.extendSelection(buffer, offset);
=======
import android.view.KeyEvent;
import android.text.*;
import android.widget.TextView;
import android.view.View;
import android.view.MotionEvent;

// XXX this doesn't extend MetaKeyKeyListener because the signatures
// don't match.  Need to figure that out.  Meanwhile the meta keys
// won't work in fields that don't take input.

public class
ArrowKeyMovementMethod
implements MovementMethod
{
    private boolean up(TextView widget, Spannable buffer) {
        boolean cap = MetaKeyKeyListener.getMetaState(buffer,
                        KeyEvent.META_SHIFT_ON) == 1;
        boolean alt = MetaKeyKeyListener.getMetaState(buffer,
                        KeyEvent.META_ALT_ON) == 1;
        Layout layout = widget.getLayout();

        if (cap) {
            if (alt) {
                Selection.extendSelection(buffer, 0);
                return true;
            } else {
                return Selection.extendUp(buffer, layout);
            }
        } else {
            if (alt) {
                Selection.setSelection(buffer, 0);
                return true;
            } else {
                return Selection.moveUp(buffer, layout); 
            }
        }
    }

    private boolean down(TextView widget, Spannable buffer) {
        boolean cap = MetaKeyKeyListener.getMetaState(buffer,
                        KeyEvent.META_SHIFT_ON) == 1;
        boolean alt = MetaKeyKeyListener.getMetaState(buffer,
                        KeyEvent.META_ALT_ON) == 1;
        Layout layout = widget.getLayout();

        if (cap) {
            if (alt) {
                Selection.extendSelection(buffer, buffer.length());
                return true;
            } else {
                return Selection.extendDown(buffer, layout);
            }
        } else {
            if (alt) {
                Selection.setSelection(buffer, buffer.length());
                return true;
            } else {
                return Selection.moveDown(buffer, layout); 
            }
        }
    }

    private boolean left(TextView widget, Spannable buffer) {
        boolean cap = MetaKeyKeyListener.getMetaState(buffer,
                        KeyEvent.META_SHIFT_ON) == 1;
        boolean alt = MetaKeyKeyListener.getMetaState(buffer,
                        KeyEvent.META_ALT_ON) == 1;
        Layout layout = widget.getLayout();

        if (cap) {
            if (alt) {
                return Selection.extendToLeftEdge(buffer, layout);
            } else {
                return Selection.extendLeft(buffer, layout);
            }
        } else {
            if (alt) {
                return Selection.moveToLeftEdge(buffer, layout);
            } else {
                return Selection.moveLeft(buffer, layout); 
            }
        }
    }

    private boolean right(TextView widget, Spannable buffer) {
        boolean cap = MetaKeyKeyListener.getMetaState(buffer,
                        KeyEvent.META_SHIFT_ON) == 1;
        boolean alt = MetaKeyKeyListener.getMetaState(buffer,
                        KeyEvent.META_ALT_ON) == 1;
        Layout layout = widget.getLayout();

        if (cap) {
            if (alt) {
                return Selection.extendToRightEdge(buffer, layout);
            } else {
                return Selection.extendRight(buffer, layout);
            }
        } else {
            if (alt) {
                return Selection.moveToRightEdge(buffer, layout);
            } else {
                return Selection.moveRight(buffer, layout); 
            }
        }
    }

    public boolean onKeyDown(TextView widget, Spannable buffer, int keyCode, KeyEvent event) {
        boolean handled = false;

        switch (keyCode) {
        case KeyEvent.KEYCODE_DPAD_UP:
            handled |= up(widget, buffer);
            break;

        case KeyEvent.KEYCODE_DPAD_DOWN:
            handled |= down(widget, buffer);
            break;

        case KeyEvent.KEYCODE_DPAD_LEFT:
            handled |= left(widget, buffer);
            break;

        case KeyEvent.KEYCODE_DPAD_RIGHT:
            handled |= right(widget, buffer);
            break;
        }

        if (handled) {
            MetaKeyKeyListener.adjustMetaAfterKeypress(buffer);
            MetaKeyKeyListener.resetLockedMeta(buffer);
        }

        return handled;
    }

    public boolean onKeyUp(TextView widget, Spannable buffer, int keyCode, KeyEvent event) {
        return false;
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

        if (handled) {
            MetaKeyKeyListener.adjustMetaAfterKeypress(buffer);
            MetaKeyKeyListener.resetLockedMeta(buffer);
        }

        return handled;
    }

    public boolean onTouchEvent(TextView widget, Spannable buffer,
                                MotionEvent event) {
        boolean handled = Touch.onTouchEvent(widget, buffer, event);

        if (widget.isFocused()) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                int x = (int) event.getX();
                int y = (int) event.getY();

                x -= widget.getTotalPaddingLeft();
                y -= widget.getTotalPaddingTop();

                x += widget.getScrollX();
                y += widget.getScrollY();

                Layout layout = widget.getLayout();
                int line = layout.getLineForVertical(y);
                int off = layout.getOffsetForHorizontal(line, x);

                boolean cap = (event.getMetaState() & KeyEvent.META_SHIFT_ON) != 0;

                if (cap) {
                    Selection.extendSelection(buffer, off);
                } else {
                    Selection.setSelection(buffer, off);
>>>>>>> 54b6cfa... Initial Contribution
                }

                MetaKeyKeyListener.adjustMetaAfterKeypress(buffer);
                MetaKeyKeyListener.resetLockedMeta(buffer);

                return true;
            }
        }

        return handled;
    }

<<<<<<< HEAD
    @Override
=======
>>>>>>> 54b6cfa... Initial Contribution
    public boolean canSelectArbitrarily() {
        return true;
    }

<<<<<<< HEAD
    @Override
=======
>>>>>>> 54b6cfa... Initial Contribution
    public void initialize(TextView widget, Spannable text) {
        Selection.setSelection(text, 0);
    }

<<<<<<< HEAD
    @Override
    public void onTakeFocus(TextView view, Spannable text, int dir) {
        if ((dir & (View.FOCUS_FORWARD | View.FOCUS_DOWN)) != 0) {
            if (view.getLayout() == null) {
                // This shouldn't be null, but do something sensible if it is.
                Selection.setSelection(text, text.length());
=======
    public void onTakeFocus(TextView view, Spannable text, int dir) {
        if ((dir & (View.FOCUS_FORWARD | View.FOCUS_DOWN)) != 0) {
            Layout layout = view.getLayout();

            if (layout == null) {
                /*
                 * This shouldn't be null, but do something sensible if it is.
                 */
                Selection.setSelection(text, text.length());
            } else {
                /*
                 * Put the cursor at the end of the first line, which is
                 * either the last offset if there is only one line, or the
                 * offset before the first character of the second line
                 * if there is more than one line.
                 */
                if (layout.getLineCount() == 1) {
                    Selection.setSelection(text, text.length());
                } else {
                    Selection.setSelection(text, layout.getLineStart(1) - 1);
                }
>>>>>>> 54b6cfa... Initial Contribution
            }
        } else {
            Selection.setSelection(text, text.length());
        }
    }

    public static MovementMethod getInstance() {
<<<<<<< HEAD
        if (sInstance == null) {
            sInstance = new ArrowKeyMovementMethod();
        }
=======
        if (sInstance == null)
            sInstance = new ArrowKeyMovementMethod();
>>>>>>> 54b6cfa... Initial Contribution

        return sInstance;
    }

<<<<<<< HEAD
    private static final Object LAST_TAP_DOWN = new Object();
=======
>>>>>>> 54b6cfa... Initial Contribution
    private static ArrowKeyMovementMethod sInstance;
}
