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

package android.text;

<<<<<<< HEAD
import android.emoji.EmojiFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.text.method.TextKeyListener;
import android.text.style.AlignmentSpan;
import android.text.style.LeadingMarginSpan;
import android.text.style.LeadingMarginSpan.LeadingMarginSpan2;
import android.text.style.LineBackgroundSpan;
import android.text.style.ParagraphStyle;
import android.text.style.ReplacementSpan;
import android.text.style.TabStopSpan;

import com.android.internal.util.ArrayUtils;

import java.util.Arrays;

/**
 * A base class that manages text layout in visual elements on
 * the screen.
 * <p>For text that will be edited, use a {@link DynamicLayout},
 * which will be updated as the text changes.
 * For text that will not change, use a {@link StaticLayout}.
 */
public abstract class Layout {
    private static final ParagraphStyle[] NO_PARA_SPANS =
        ArrayUtils.emptyArray(ParagraphStyle.class);

    /* package */ static final EmojiFactory EMOJI_FACTORY = EmojiFactory.newAvailableInstance();
    /* package */ static final int MIN_EMOJI, MAX_EMOJI;

    static {
        if (EMOJI_FACTORY != null) {
            MIN_EMOJI = EMOJI_FACTORY.getMinimumAndroidPua();
            MAX_EMOJI = EMOJI_FACTORY.getMaximumAndroidPua();
        } else {
            MIN_EMOJI = -1;
            MAX_EMOJI = -1;
        }
    }

    /**
     * Return how wide a layout must be in order to display the
=======
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Path;
import com.android.internal.util.ArrayUtils;
import android.util.Config;

import junit.framework.Assert;
import android.text.style.*;
import android.text.method.TextKeyListener;
import android.view.KeyEvent;

/**
 * A base class that manages text layout in visual elements on 
 * the screen. 
 * <p>For text that will be edited, use a {@link DynamicLayout}, 
 * which will be updated as the text changes.  
 * For text that will not change, use a {@link StaticLayout}.
 */
public abstract class Layout {
    /**
     * Return how wide a layout would be necessary to display the
>>>>>>> 54b6cfa... Initial Contribution
     * specified text with one line per paragraph.
     */
    public static float getDesiredWidth(CharSequence source,
                                        TextPaint paint) {
        return getDesiredWidth(source, 0, source.length(), paint);
    }
<<<<<<< HEAD

    /**
     * Return how wide a layout must be in order to display the
=======
    
    /**
     * Return how wide a layout would be necessary to display the
>>>>>>> 54b6cfa... Initial Contribution
     * specified text slice with one line per paragraph.
     */
    public static float getDesiredWidth(CharSequence source,
                                        int start, int end,
                                        TextPaint paint) {
        float need = 0;
<<<<<<< HEAD
=======
        TextPaint workPaint = new TextPaint();
>>>>>>> 54b6cfa... Initial Contribution

        int next;
        for (int i = start; i <= end; i = next) {
            next = TextUtils.indexOf(source, '\n', i, end);

            if (next < 0)
                next = end;

<<<<<<< HEAD
            // note, omits trailing paragraph char
            float w = measurePara(paint, source, i, next);
=======
            float w = measureText(paint, workPaint,
                                  source, i, next, null, true, null);
>>>>>>> 54b6cfa... Initial Contribution

            if (w > need)
                need = w;

            next++;
        }

        return need;
    }

    /**
     * Subclasses of Layout use this constructor to set the display text,
     * width, and other standard properties.
<<<<<<< HEAD
     * @param text the text to render
     * @param paint the default paint for the layout.  Styles can override
     * various attributes of the paint.
     * @param width the wrapping width for the text.
     * @param align whether to left, right, or center the text.  Styles can
     * override the alignment.
     * @param spacingMult factor by which to scale the font size to get the
     * default line spacing
     * @param spacingAdd amount to add to the default line spacing
     */
    protected Layout(CharSequence text, TextPaint paint,
                     int width, Alignment align,
                     float spacingMult, float spacingAdd) {
        this(text, paint, width, align, TextDirectionHeuristics.FIRSTSTRONG_LTR,
                spacingMult, spacingAdd);
    }

    /**
     * Subclasses of Layout use this constructor to set the display text,
     * width, and other standard properties.
     * @param text the text to render
     * @param paint the default paint for the layout.  Styles can override
     * various attributes of the paint.
     * @param width the wrapping width for the text.
     * @param align whether to left, right, or center the text.  Styles can
     * override the alignment.
     * @param spacingMult factor by which to scale the font size to get the
     * default line spacing
     * @param spacingAdd amount to add to the default line spacing
     *
     * @hide
     */
    protected Layout(CharSequence text, TextPaint paint,
                     int width, Alignment align, TextDirectionHeuristic textDir,
                     float spacingMult, float spacingAdd) {

        if (width < 0)
            throw new IllegalArgumentException("Layout: " + width + " < 0");

        // Ensure paint doesn't have baselineShift set.
        // While normally we don't modify the paint the user passed in,
        // we were already doing this in Styled.drawUniformRun with both
        // baselineShift and bgColor.  We probably should reevaluate bgColor.
        if (paint != null) {
            paint.bgColor = 0;
            paint.baselineShift = 0;
        }

=======
     */
    protected Layout(CharSequence text, TextPaint paint,
                     int width, Alignment align,
                     float spacingmult, float spacingadd) {
        if (width < 0)
            throw new IllegalArgumentException("Layout: " + width + " < 0");

>>>>>>> 54b6cfa... Initial Contribution
        mText = text;
        mPaint = paint;
        mWorkPaint = new TextPaint();
        mWidth = width;
        mAlignment = align;
<<<<<<< HEAD
        mSpacingMult = spacingMult;
        mSpacingAdd = spacingAdd;
        mSpannedText = text instanceof Spanned;
        mTextDir = textDir;
=======
        mSpacingMult = spacingmult;
        mSpacingAdd = spacingadd;
        mSpannedText = text instanceof Spanned;
>>>>>>> 54b6cfa... Initial Contribution
    }

    /**
     * Replace constructor properties of this Layout with new ones.  Be careful.
     */
    /* package */ void replaceWith(CharSequence text, TextPaint paint,
                              int width, Alignment align,
                              float spacingmult, float spacingadd) {
        if (width < 0) {
            throw new IllegalArgumentException("Layout: " + width + " < 0");
        }

        mText = text;
        mPaint = paint;
        mWidth = width;
        mAlignment = align;
        mSpacingMult = spacingmult;
        mSpacingAdd = spacingadd;
        mSpannedText = text instanceof Spanned;
    }

    /**
     * Draw this Layout on the specified Canvas.
     */
    public void draw(Canvas c) {
        draw(c, null, null, 0);
    }

    /**
<<<<<<< HEAD
     * Draw this Layout on the specified canvas, with the highlight path drawn
     * between the background and the text.
     *
     * @param canvas the canvas
     * @param highlight the path of the highlight or cursor; can be null
     * @param highlightPaint the paint for the highlight
     * @param cursorOffsetVertical the amount to temporarily translate the
     *        canvas while rendering the highlight
     */
    public void draw(Canvas canvas, Path highlight, Paint highlightPaint,
            int cursorOffsetVertical) {
        final long lineRange = getLineRangeForDraw(canvas);
        int firstLine = TextUtils.unpackRangeStartFromLong(lineRange);
        int lastLine = TextUtils.unpackRangeEndFromLong(lineRange);
        if (lastLine < 0) return;

        drawBackground(canvas, highlight, highlightPaint, cursorOffsetVertical,
                firstLine, lastLine);
        drawText(canvas, firstLine, lastLine);
    }

    /**
     * @hide
     */
    public void drawText(Canvas canvas, int firstLine, int lastLine) {
        int previousLineBottom = getLineTop(firstLine);
        int previousLineEnd = getLineStart(firstLine);
        ParagraphStyle[] spans = NO_PARA_SPANS;
        int spanEnd = 0;
        TextPaint paint = mPaint;
        CharSequence buf = mText;

        Alignment paraAlign = mAlignment;
        TabStops tabStops = null;
        boolean tabStopsIsInitialized = false;

        TextLine tl = TextLine.obtain();

        // Draw the lines, one at a time.
        // The baseline is the top of the following line minus the current line's descent.
        for (int i = firstLine; i <= lastLine; i++) {
            int start = previousLineEnd;
            previousLineEnd = getLineStart(i + 1);
=======
     * Draw the specified rectangle from this Layout on the specified Canvas,
     * with the specified path drawn between the background and the text.
     */
    public void draw(Canvas c, Path highlight, Paint highlightpaint,
                     int cursorOffsetVertical) {
        int dtop, dbottom;

        synchronized (sTempRect) {
            if (!c.getClipBounds(sTempRect)) {
                return;
            }

            dtop = sTempRect.top;
            dbottom = sTempRect.bottom;
        }

        TextPaint paint = mPaint;

        int top = 0;
        // getLineBottom(getLineCount() -1) just calls getLineTop(getLineCount) 
        int bottom = getLineTop(getLineCount());


        if (dtop > top) {
            top = dtop;
        }
        if (dbottom < bottom) {
            bottom = dbottom;
        }
        
        int first = getLineForVertical(top); 
        int last = getLineForVertical(bottom);
        
        int previousLineBottom = getLineTop(first);
        int previousLineEnd = getLineStart(first);
        
        CharSequence buf = mText;

        ParagraphStyle[] nospans = ArrayUtils.emptyArray(ParagraphStyle.class);
        ParagraphStyle[] spans = nospans;
        int spanend = 0;
        int textLength = 0;
        boolean spannedText = mSpannedText;

        if (spannedText) {
            spanend = 0;
            textLength = buf.length();
            for (int i = first; i <= last; i++) {
                int start = previousLineEnd;
                int end = getLineStart(i+1);
                previousLineEnd = end;

                int ltop = previousLineBottom;
                int lbottom = getLineTop(i+1);
                previousLineBottom = lbottom;
                int lbaseline = lbottom - getLineDescent(i);

                if (start >= spanend) {
                   Spanned sp = (Spanned) buf;
                   spanend = sp.nextSpanTransition(start, textLength,
                                                   LineBackgroundSpan.class);
                   spans = sp.getSpans(start, spanend,
                                       LineBackgroundSpan.class);
                }

                for (int n = 0; n < spans.length; n++) {
                    LineBackgroundSpan back = (LineBackgroundSpan) spans[n];

                    back.drawBackground(c, paint, 0, mWidth,
                                       ltop, lbaseline, lbottom,
                                       buf, start, end,
                                       i);
                }
            }
            // reset to their original values
            spanend = 0;
            previousLineBottom = getLineTop(first);
            previousLineEnd = getLineStart(first);
            spans = nospans;
        } 

        // There can be a highlight even without spans if we are drawing
        // a non-spanned transformation of a spanned editing buffer.
        if (highlight != null) {
            if (cursorOffsetVertical != 0) {
                c.translate(0, cursorOffsetVertical);
            }

            c.drawPath(highlight, highlightpaint);

            if (cursorOffsetVertical != 0) {
                c.translate(0, -cursorOffsetVertical);
            }
        }

        Alignment align = mAlignment;
        
        for (int i = first; i <= last; i++) {
            int start = previousLineEnd;

            previousLineEnd = getLineStart(i+1);
>>>>>>> 54b6cfa... Initial Contribution
            int end = getLineVisibleEnd(i, start, previousLineEnd);

            int ltop = previousLineBottom;
            int lbottom = getLineTop(i+1);
            previousLineBottom = lbottom;
            int lbaseline = lbottom - getLineDescent(i);

<<<<<<< HEAD
            int dir = getParagraphDirection(i);
            int left = 0;
            int right = mWidth;

            if (mSpannedText) {
                Spanned sp = (Spanned) buf;
                int textLength = buf.length();
                boolean isFirstParaLine = (start == 0 || buf.charAt(start - 1) == '\n');

                // New batch of paragraph styles, collect into spans array.
                // Compute the alignment, last alignment style wins.
                // Reset tabStops, we'll rebuild if we encounter a line with
                // tabs.
                // We expect paragraph spans to be relatively infrequent, use
                // spanEnd so that we can check less frequently.  Since
                // paragraph styles ought to apply to entire paragraphs, we can
                // just collect the ones present at the start of the paragraph.
                // If spanEnd is before the end of the paragraph, that's not
                // our problem.
                if (start >= spanEnd && (i == firstLine || isFirstParaLine)) {
                    spanEnd = sp.nextSpanTransition(start, textLength,
                                                    ParagraphStyle.class);
                    spans = getParagraphSpans(sp, start, spanEnd, ParagraphStyle.class);

                    paraAlign = mAlignment;
                    for (int n = spans.length - 1; n >= 0; n--) {
                        if (spans[n] instanceof AlignmentSpan) {
                            paraAlign = ((AlignmentSpan) spans[n]).getAlignment();
                            break;
                        }
                    }

                    tabStopsIsInitialized = false;
                }

                // Draw all leading margin spans.  Adjust left or right according
                // to the paragraph direction of the line.
=======
            boolean par = false;
            if (spannedText) { 
                if (start == 0 || buf.charAt(start - 1) == '\n') {
                    par = true;
                }
                if (start >= spanend) {

                    Spanned sp = (Spanned) buf;

                    spanend = sp.nextSpanTransition(start, textLength,
                                                    ParagraphStyle.class);
                    spans = sp.getSpans(start, spanend, ParagraphStyle.class);
                    
                    align = mAlignment;
                    
                    for (int n = spans.length-1; n >= 0; n--) {
                        if (spans[n] instanceof AlignmentSpan) {
                            align = ((AlignmentSpan) spans[n]).getAlignment();
                            break;
                        }
                    }
                }
            }
            
            int dir = getParagraphDirection(i);
            int left = 0;
            int right = mWidth;

            if (spannedText) {
>>>>>>> 54b6cfa... Initial Contribution
                final int length = spans.length;
                for (int n = 0; n < length; n++) {
                    if (spans[n] instanceof LeadingMarginSpan) {
                        LeadingMarginSpan margin = (LeadingMarginSpan) spans[n];
<<<<<<< HEAD
                        boolean useFirstLineMargin = isFirstParaLine;
                        if (margin instanceof LeadingMarginSpan2) {
                            int count = ((LeadingMarginSpan2) margin).getLeadingMarginLineCount();
                            int startLine = getLineForOffset(sp.getSpanStart(margin));
                            useFirstLineMargin = i < startLine + count;
                        }

                        if (dir == DIR_RIGHT_TO_LEFT) {
                            margin.drawLeadingMargin(canvas, paint, right, dir, ltop,
                                                     lbaseline, lbottom, buf,
                                                     start, end, isFirstParaLine, this);
                            right -= margin.getLeadingMargin(useFirstLineMargin);
                        } else {
                            margin.drawLeadingMargin(canvas, paint, left, dir, ltop,
                                                     lbaseline, lbottom, buf,
                                                     start, end, isFirstParaLine, this);
                            left += margin.getLeadingMargin(useFirstLineMargin);
=======

                        if (dir == DIR_RIGHT_TO_LEFT) {
                            margin.drawLeadingMargin(c, paint, right, dir, ltop,
                                                     lbaseline, lbottom, buf,
                                                     start, end, par, this);
                                
                            right -= margin.getLeadingMargin(par);
                        } else {
                            margin.drawLeadingMargin(c, paint, left, dir, ltop,
                                                     lbaseline, lbottom, buf,
                                                     start, end, par, this);

                            left += margin.getLeadingMargin(par);
>>>>>>> 54b6cfa... Initial Contribution
                        }
                    }
                }
            }

<<<<<<< HEAD
            boolean hasTabOrEmoji = getLineContainsTab(i);
            // Can't tell if we have tabs for sure, currently
            if (hasTabOrEmoji && !tabStopsIsInitialized) {
                if (tabStops == null) {
                    tabStops = new TabStops(TAB_INCREMENT, spans);
                } else {
                    tabStops.reset(TAB_INCREMENT, spans);
                }
                tabStopsIsInitialized = true;
            }

            // Determine whether the line aligns to normal, opposite, or center.
            Alignment align = paraAlign;
            if (align == Alignment.ALIGN_LEFT) {
                align = (dir == DIR_LEFT_TO_RIGHT) ?
                    Alignment.ALIGN_NORMAL : Alignment.ALIGN_OPPOSITE;
            } else if (align == Alignment.ALIGN_RIGHT) {
                align = (dir == DIR_LEFT_TO_RIGHT) ?
                    Alignment.ALIGN_OPPOSITE : Alignment.ALIGN_NORMAL;
            }

=======
>>>>>>> 54b6cfa... Initial Contribution
            int x;
            if (align == Alignment.ALIGN_NORMAL) {
                if (dir == DIR_LEFT_TO_RIGHT) {
                    x = left;
                } else {
                    x = right;
                }
            } else {
<<<<<<< HEAD
                int max = (int)getLineExtent(i, tabStops, false);
                if (align == Alignment.ALIGN_OPPOSITE) {
                    if (dir == DIR_LEFT_TO_RIGHT) {
                        x = right - max;
                    } else {
                        x = left - max;
                    }
                } else { // Alignment.ALIGN_CENTER
                    max = max & ~1;
                    x = (right + left - max) >> 1;
=======
                int max = (int)getLineMax(i, spans, false);
                if (align == Alignment.ALIGN_OPPOSITE) {
                    if (dir == DIR_RIGHT_TO_LEFT) {
                        x = left + max;
                    } else {
                        x = right - max;
                    }
                } else {
                    // Alignment.ALIGN_CENTER
                    max = max & ~1;
                    int half = (right - left - max) >> 1;
                    if (dir == DIR_RIGHT_TO_LEFT) {
                        x = right - half;
                    } else {
                        x = left + half;
                    }
>>>>>>> 54b6cfa... Initial Contribution
                }
            }

            Directions directions = getLineDirections(i);
<<<<<<< HEAD
            if (directions == DIRS_ALL_LEFT_TO_RIGHT && !mSpannedText && !hasTabOrEmoji) {
                // XXX: assumes there's nothing additional to be done
                canvas.drawText(buf, start, end, x, lbaseline, paint);
            } else {
                tl.set(paint, buf, start, end, dir, directions, hasTabOrEmoji, tabStops);
                tl.draw(canvas, x, ltop, lbaseline, lbottom);
            }
        }

        TextLine.recycle(tl);
    }

    /**
     * @hide
     */
    public void drawBackground(Canvas canvas, Path highlight, Paint highlightPaint,
            int cursorOffsetVertical, int firstLine, int lastLine) {
        // First, draw LineBackgroundSpans.
        // LineBackgroundSpans know nothing about the alignment, margins, or
        // direction of the layout or line.  XXX: Should they?
        // They are evaluated at each line.
        if (mSpannedText) {
            if (mLineBackgroundSpans == null) {
                mLineBackgroundSpans = new SpanSet<LineBackgroundSpan>(LineBackgroundSpan.class);
            }

            Spanned buffer = (Spanned) mText;
            int textLength = buffer.length();
            mLineBackgroundSpans.init(buffer, 0, textLength);

            if (mLineBackgroundSpans.numberOfSpans > 0) {
                int previousLineBottom = getLineTop(firstLine);
                int previousLineEnd = getLineStart(firstLine);
                ParagraphStyle[] spans = NO_PARA_SPANS;
                int spansLength = 0;
                TextPaint paint = mPaint;
                int spanEnd = 0;
                final int width = mWidth;
                for (int i = firstLine; i <= lastLine; i++) {
                    int start = previousLineEnd;
                    int end = getLineStart(i + 1);
                    previousLineEnd = end;

                    int ltop = previousLineBottom;
                    int lbottom = getLineTop(i + 1);
                    previousLineBottom = lbottom;
                    int lbaseline = lbottom - getLineDescent(i);

                    if (start >= spanEnd) {
                        // These should be infrequent, so we'll use this so that
                        // we don't have to check as often.
                        spanEnd = mLineBackgroundSpans.getNextTransition(start, textLength);
                        // All LineBackgroundSpans on a line contribute to its background.
                        spansLength = 0;
                        // Duplication of the logic of getParagraphSpans
                        if (start != end || start == 0) {
                            // Equivalent to a getSpans(start, end), but filling the 'spans' local
                            // array instead to reduce memory allocation
                            for (int j = 0; j < mLineBackgroundSpans.numberOfSpans; j++) {
                                // equal test is valid since both intervals are not empty by
                                // construction
                                if (mLineBackgroundSpans.spanStarts[j] >= end ||
                                        mLineBackgroundSpans.spanEnds[j] <= start) continue;
                                if (spansLength == spans.length) {
                                    // The spans array needs to be expanded
                                    int newSize = ArrayUtils.idealObjectArraySize(2 * spansLength);
                                    ParagraphStyle[] newSpans = new ParagraphStyle[newSize];
                                    System.arraycopy(spans, 0, newSpans, 0, spansLength);
                                    spans = newSpans;
                                }
                                spans[spansLength++] = mLineBackgroundSpans.spans[j];
                            }
                        }
                    }

                    for (int n = 0; n < spansLength; n++) {
                        LineBackgroundSpan lineBackgroundSpan = (LineBackgroundSpan) spans[n];
                        lineBackgroundSpan.drawBackground(canvas, paint, 0, width,
                                ltop, lbaseline, lbottom,
                                buffer, start, end, i);
                    }
                }
            }
            mLineBackgroundSpans.recycle();
        }

        // There can be a highlight even without spans if we are drawing
        // a non-spanned transformation of a spanned editing buffer.
        if (highlight != null) {
            if (cursorOffsetVertical != 0) canvas.translate(0, cursorOffsetVertical);
            canvas.drawPath(highlight, highlightPaint);
            if (cursorOffsetVertical != 0) canvas.translate(0, -cursorOffsetVertical);
        }
    }

    /**
     * @param canvas
     * @return The range of lines that need to be drawn, possibly empty.
     * @hide
     */
    public long getLineRangeForDraw(Canvas canvas) {
        int dtop, dbottom;

        synchronized (sTempRect) {
            if (!canvas.getClipBounds(sTempRect)) {
                // Negative range end used as a special flag
                return TextUtils.packRangeInLong(0, -1);
            }

            dtop = sTempRect.top;
            dbottom = sTempRect.bottom;
        }

        final int top = Math.max(dtop, 0);
        final int bottom = Math.min(getLineTop(getLineCount()), dbottom);

        if (top >= bottom) return TextUtils.packRangeInLong(0, -1);
        return TextUtils.packRangeInLong(getLineForVertical(top), getLineForVertical(bottom));
    }

    /**
     * Return the start position of the line, given the left and right bounds
     * of the margins.
     *
     * @param line the line index
     * @param left the left bounds (0, or leading margin if ltr para)
     * @param right the right bounds (width, minus leading margin if rtl para)
     * @return the start position of the line (to right of line if rtl para)
     */
    private int getLineStartPos(int line, int left, int right) {
        // Adjust the point at which to start rendering depending on the
        // alignment of the paragraph.
        Alignment align = getParagraphAlignment(line);
        int dir = getParagraphDirection(line);

        int x;
        if (align == Alignment.ALIGN_LEFT) {
            x = left;
        } else if (align == Alignment.ALIGN_NORMAL) {
            if (dir == DIR_LEFT_TO_RIGHT) {
                x = left;
            } else {
                x = right;
            }
        } else {
            TabStops tabStops = null;
            if (mSpannedText && getLineContainsTab(line)) {
                Spanned spanned = (Spanned) mText;
                int start = getLineStart(line);
                int spanEnd = spanned.nextSpanTransition(start, spanned.length(),
                        TabStopSpan.class);
                TabStopSpan[] tabSpans = getParagraphSpans(spanned, start, spanEnd,
                        TabStopSpan.class);
                if (tabSpans.length > 0) {
                    tabStops = new TabStops(TAB_INCREMENT, tabSpans);
                }
            }
            int max = (int)getLineExtent(line, tabStops, false);
            if (align == Alignment.ALIGN_RIGHT) {
                x = right - max;
            } else if (align == Alignment.ALIGN_OPPOSITE) {
                if (dir == DIR_LEFT_TO_RIGHT) {
                    x = right - max;
                } else {
                    x = left - max;
                }
            } else { // Alignment.ALIGN_CENTER
                max = max & ~1;
                x = (left + right - max) >> 1;
            }
        }
        return x;
=======
            boolean hasTab = getLineContainsTab(i);
            if (directions == DIRS_ALL_LEFT_TO_RIGHT &&
                    !spannedText && !hasTab) {
                if (Config.DEBUG) {
                    Assert.assertTrue(dir == DIR_LEFT_TO_RIGHT);
                    Assert.assertNotNull(c);
                }
                c.drawText(buf, start, end, x, lbaseline, paint);
            } else {
                drawText(c, buf, start, end, dir, directions,
                    x, ltop, lbaseline, lbottom, paint, mWorkPaint,
                    hasTab, spans);
            }
        }
>>>>>>> 54b6cfa... Initial Contribution
    }

    /**
     * Return the text that is displayed by this Layout.
     */
    public final CharSequence getText() {
        return mText;
    }

    /**
     * Return the base Paint properties for this layout.
     * Do NOT change the paint, which may result in funny
     * drawing for this layout.
     */
    public final TextPaint getPaint() {
        return mPaint;
    }

    /**
     * Return the width of this layout.
     */
    public final int getWidth() {
        return mWidth;
    }

    /**
     * Return the width to which this Layout is ellipsizing, or
     * {@link #getWidth} if it is not doing anything special.
     */
    public int getEllipsizedWidth() {
        return mWidth;
    }

    /**
     * Increase the width of this layout to the specified width.
<<<<<<< HEAD
     * Be careful to use this only when you know it is appropriate&mdash;
=======
     * Be careful to use this only when you know it is appropriate --
>>>>>>> 54b6cfa... Initial Contribution
     * it does not cause the text to reflow to use the full new width.
     */
    public final void increaseWidthTo(int wid) {
        if (wid < mWidth) {
            throw new RuntimeException("attempted to reduce Layout width");
        }

        mWidth = wid;
    }
<<<<<<< HEAD

=======
    
>>>>>>> 54b6cfa... Initial Contribution
    /**
     * Return the total height of this layout.
     */
    public int getHeight() {
<<<<<<< HEAD
        return getLineTop(getLineCount());
=======
        return getLineTop(getLineCount());  // same as getLineBottom(getLineCount() - 1);
>>>>>>> 54b6cfa... Initial Contribution
    }

    /**
     * Return the base alignment of this layout.
     */
    public final Alignment getAlignment() {
        return mAlignment;
    }

    /**
     * Return what the text height is multiplied by to get the line height.
     */
    public final float getSpacingMultiplier() {
        return mSpacingMult;
    }

    /**
     * Return the number of units of leading that are added to each line.
     */
    public final float getSpacingAdd() {
        return mSpacingAdd;
    }

    /**
<<<<<<< HEAD
     * Return the heuristic used to determine paragraph text direction.
     * @hide
     */
    public final TextDirectionHeuristic getTextDirectionHeuristic() {
        return mTextDir;
    }

    /**
     * Return the number of lines of text in this layout.
     */
    public abstract int getLineCount();

=======
     * Return the number of lines of text in this layout.
     */
    public abstract int getLineCount();
    
>>>>>>> 54b6cfa... Initial Contribution
    /**
     * Return the baseline for the specified line (0&hellip;getLineCount() - 1)
     * If bounds is not null, return the top, left, right, bottom extents
     * of the specified line in it.
     * @param line which line to examine (0..getLineCount() - 1)
     * @param bounds Optional. If not null, it returns the extent of the line
     * @return the Y-coordinate of the baseline
     */
    public int getLineBounds(int line, Rect bounds) {
        if (bounds != null) {
            bounds.left = 0;     // ???
            bounds.top = getLineTop(line);
            bounds.right = mWidth;   // ???
<<<<<<< HEAD
            bounds.bottom = getLineTop(line + 1);
=======
            bounds.bottom = getLineBottom(line);
>>>>>>> 54b6cfa... Initial Contribution
        }
        return getLineBaseline(line);
    }

    /**
<<<<<<< HEAD
     * Return the vertical position of the top of the specified line
     * (0&hellip;getLineCount()).
     * If the specified line is equal to the line count, returns the
=======
     * Return the vertical position of the top of the specified line.
     * If the specified line is one beyond the last line, returns the
>>>>>>> 54b6cfa... Initial Contribution
     * bottom of the last line.
     */
    public abstract int getLineTop(int line);

    /**
<<<<<<< HEAD
     * Return the descent of the specified line(0&hellip;getLineCount() - 1).
=======
     * Return the descent of the specified line.
>>>>>>> 54b6cfa... Initial Contribution
     */
    public abstract int getLineDescent(int line);

    /**
<<<<<<< HEAD
     * Return the text offset of the beginning of the specified line (
     * 0&hellip;getLineCount()). If the specified line is equal to the line
     * count, returns the length of the text.
=======
     * Return the text offset of the beginning of the specified line.
     * If the specified line is one beyond the last line, returns the
     * end of the last line.
>>>>>>> 54b6cfa... Initial Contribution
     */
    public abstract int getLineStart(int line);

    /**
<<<<<<< HEAD
     * Returns the primary directionality of the paragraph containing the
     * specified line, either 1 for left-to-right lines, or -1 for right-to-left
     * lines (see {@link #DIR_LEFT_TO_RIGHT}, {@link #DIR_RIGHT_TO_LEFT}).
=======
     * Returns the primary directionality of the paragraph containing
     * the specified line.
>>>>>>> 54b6cfa... Initial Contribution
     */
    public abstract int getParagraphDirection(int line);

    /**
<<<<<<< HEAD
     * Returns whether the specified line contains one or more
     * characters that need to be handled specially, like tabs
     * or emoji.
=======
     * Returns whether the specified line contains one or more tabs.
>>>>>>> 54b6cfa... Initial Contribution
     */
    public abstract boolean getLineContainsTab(int line);

    /**
<<<<<<< HEAD
     * Returns the directional run information for the specified line.
     * The array alternates counts of characters in left-to-right
     * and right-to-left segments of the line.
     *
     * <p>NOTE: this is inadequate to support bidirectional text, and will change.
=======
     * Returns an array of directionalities for the specified line.
     * The array alternates counts of characters in left-to-right
     * and right-to-left segments of the line.
>>>>>>> 54b6cfa... Initial Contribution
     */
    public abstract Directions getLineDirections(int line);

    /**
     * Returns the (negative) number of extra pixels of ascent padding in the
     * top line of the Layout.
     */
    public abstract int getTopPadding();

    /**
     * Returns the number of extra pixels of descent padding in the
     * bottom line of the Layout.
     */
    public abstract int getBottomPadding();

<<<<<<< HEAD

    /**
     * Returns true if the character at offset and the preceding character
     * are at different run levels (and thus there's a split caret).
     * @param offset the offset
     * @return true if at a level boundary
     * @hide
     */
    public boolean isLevelBoundary(int offset) {
        int line = getLineForOffset(offset);
        Directions dirs = getLineDirections(line);
        if (dirs == DIRS_ALL_LEFT_TO_RIGHT || dirs == DIRS_ALL_RIGHT_TO_LEFT) {
            return false;
        }

        int[] runs = dirs.mDirections;
        int lineStart = getLineStart(line);
        int lineEnd = getLineEnd(line);
        if (offset == lineStart || offset == lineEnd) {
            int paraLevel = getParagraphDirection(line) == 1 ? 0 : 1;
            int runIndex = offset == lineStart ? 0 : runs.length - 2;
            return ((runs[runIndex + 1] >>> RUN_LEVEL_SHIFT) & RUN_LEVEL_MASK) != paraLevel;
        }

        offset -= lineStart;
        for (int i = 0; i < runs.length; i += 2) {
            if (offset == runs[i]) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if the character at offset is right to left (RTL).
     * @param offset the offset
     * @return true if the character is RTL, false if it is LTR
     */
    public boolean isRtlCharAt(int offset) {
        int line = getLineForOffset(offset);
        Directions dirs = getLineDirections(line);
        if (dirs == DIRS_ALL_LEFT_TO_RIGHT) {
            return false;
        }
        if (dirs == DIRS_ALL_RIGHT_TO_LEFT) {
            return  true;
        }
        int[] runs = dirs.mDirections;
        int lineStart = getLineStart(line);
        for (int i = 0; i < runs.length; i += 2) {
            int start = lineStart + (runs[i] & RUN_LENGTH_MASK);
            // No need to test the end as an offset after the last run should return the value
            // corresponding of the last run
            if (offset >= start) {
                int level = (runs[i+1] >>> RUN_LEVEL_SHIFT) & RUN_LEVEL_MASK;
                return ((level & 1) != 0);
            }
        }
        // Should happen only if the offset is "out of bounds"
        return false;
    }

    private boolean primaryIsTrailingPrevious(int offset) {
        int line = getLineForOffset(offset);
        int lineStart = getLineStart(line);
        int lineEnd = getLineEnd(line);
        int[] runs = getLineDirections(line).mDirections;

        int levelAt = -1;
        for (int i = 0; i < runs.length; i += 2) {
            int start = lineStart + runs[i];
            int limit = start + (runs[i+1] & RUN_LENGTH_MASK);
            if (limit > lineEnd) {
                limit = lineEnd;
            }
            if (offset >= start && offset < limit) {
                if (offset > start) {
                    // Previous character is at same level, so don't use trailing.
                    return false;
                }
                levelAt = (runs[i+1] >>> RUN_LEVEL_SHIFT) & RUN_LEVEL_MASK;
                break;
            }
        }
        if (levelAt == -1) {
            // Offset was limit of line.
            levelAt = getParagraphDirection(line) == 1 ? 0 : 1;
        }

        // At level boundary, check previous level.
        int levelBefore = -1;
        if (offset == lineStart) {
            levelBefore = getParagraphDirection(line) == 1 ? 0 : 1;
        } else {
            offset -= 1;
            for (int i = 0; i < runs.length; i += 2) {
                int start = lineStart + runs[i];
                int limit = start + (runs[i+1] & RUN_LENGTH_MASK);
                if (limit > lineEnd) {
                    limit = lineEnd;
                }
                if (offset >= start && offset < limit) {
                    levelBefore = (runs[i+1] >>> RUN_LEVEL_SHIFT) & RUN_LEVEL_MASK;
                    break;
                }
            }
        }

        return levelBefore < levelAt;
    }

=======
>>>>>>> 54b6cfa... Initial Contribution
    /**
     * Get the primary horizontal position for the specified text offset.
     * This is the location where a new character would be inserted in
     * the paragraph's primary direction.
     */
    public float getPrimaryHorizontal(int offset) {
<<<<<<< HEAD
        boolean trailing = primaryIsTrailingPrevious(offset);
        return getHorizontal(offset, trailing);
=======
        return getHorizontal(offset, false, true);
>>>>>>> 54b6cfa... Initial Contribution
    }

    /**
     * Get the secondary horizontal position for the specified text offset.
     * This is the location where a new character would be inserted in
     * the direction other than the paragraph's primary direction.
     */
    public float getSecondaryHorizontal(int offset) {
<<<<<<< HEAD
        boolean trailing = primaryIsTrailingPrevious(offset);
        return getHorizontal(offset, !trailing);
    }

    private float getHorizontal(int offset, boolean trailing) {
        int line = getLineForOffset(offset);

        return getHorizontal(offset, trailing, line);
    }

    private float getHorizontal(int offset, boolean trailing, int line) {
        int start = getLineStart(line);
        int end = getLineEnd(line);
        int dir = getParagraphDirection(line);
        boolean hasTabOrEmoji = getLineContainsTab(line);
        Directions directions = getLineDirections(line);

        TabStops tabStops = null;
        if (hasTabOrEmoji && mText instanceof Spanned) {
            // Just checking this line should be good enough, tabs should be
            // consistent across all lines in a paragraph.
            TabStopSpan[] tabs = getParagraphSpans((Spanned) mText, start, end, TabStopSpan.class);
            if (tabs.length > 0) {
                tabStops = new TabStops(TAB_INCREMENT, tabs); // XXX should reuse
            }
        }

        TextLine tl = TextLine.obtain();
        tl.set(mPaint, mText, start, end, dir, directions, hasTabOrEmoji, tabStops);
        float wid = tl.measure(offset - start, trailing, null);
        TextLine.recycle(tl);

        int left = getParagraphLeft(line);
        int right = getParagraphRight(line);

        return getLineStartPos(line, left, right) + wid;
=======
        return getHorizontal(offset, true, true);
    }

    private float getHorizontal(int offset, boolean trailing, boolean alt) {
        int line = getLineForOffset(offset);

        return getHorizontal(offset, trailing, alt, line);
    }

    private float getHorizontal(int offset, boolean trailing, boolean alt,
                                int line) {
        int start = getLineStart(line);
        int end = getLineVisibleEnd(line);
        int dir = getParagraphDirection(line);
        boolean tab = getLineContainsTab(line);
        Directions directions = getLineDirections(line);

        TabStopSpan[] tabs = null;
        if (tab && mText instanceof Spanned) {
            tabs = ((Spanned) mText).getSpans(start, end, TabStopSpan.class);
        }

        float wid = measureText(mPaint, mWorkPaint, mText, start, offset, end,
                                dir, directions, trailing, alt, tab, tabs);

        if (offset > end) {
            if (dir == DIR_RIGHT_TO_LEFT)
                wid -= measureText(mPaint, mWorkPaint,
                                   mText, end, offset, null, tab, tabs);
            else
                wid += measureText(mPaint, mWorkPaint,
                                   mText, end, offset, null, tab, tabs);
        }

        Alignment align = getParagraphAlignment(line);
        int left = getParagraphLeft(line);
        int right = getParagraphRight(line);

        if (align == Alignment.ALIGN_NORMAL) {
            if (dir == DIR_RIGHT_TO_LEFT)
                return right + wid;
            else
                return left + wid;
        }

        float max = getLineMax(line);

        if (align == Alignment.ALIGN_OPPOSITE) {
            if (dir == DIR_RIGHT_TO_LEFT)
                return left + max + wid;
            else
                return right - max + wid;
        } else { /* align == Alignment.ALIGN_CENTER */
            int imax = ((int) max) & ~1;

            if (dir == DIR_RIGHT_TO_LEFT)
                return right - (((right - left) - imax) / 2) + wid;
            else
                return left + ((right - left) - imax) / 2 + wid;
        }
>>>>>>> 54b6cfa... Initial Contribution
    }

    /**
     * Get the leftmost position that should be exposed for horizontal
     * scrolling on the specified line.
     */
    public float getLineLeft(int line) {
        int dir = getParagraphDirection(line);
        Alignment align = getParagraphAlignment(line);

<<<<<<< HEAD
        if (align == Alignment.ALIGN_LEFT) {
            return 0;
        } else if (align == Alignment.ALIGN_NORMAL) {
=======
        if (align == Alignment.ALIGN_NORMAL) {
>>>>>>> 54b6cfa... Initial Contribution
            if (dir == DIR_RIGHT_TO_LEFT)
                return getParagraphRight(line) - getLineMax(line);
            else
                return 0;
<<<<<<< HEAD
        } else if (align == Alignment.ALIGN_RIGHT) {
            return mWidth - getLineMax(line);
=======
>>>>>>> 54b6cfa... Initial Contribution
        } else if (align == Alignment.ALIGN_OPPOSITE) {
            if (dir == DIR_RIGHT_TO_LEFT)
                return 0;
            else
                return mWidth - getLineMax(line);
        } else { /* align == Alignment.ALIGN_CENTER */
            int left = getParagraphLeft(line);
            int right = getParagraphRight(line);
            int max = ((int) getLineMax(line)) & ~1;

            return left + ((right - left) - max) / 2;
        }
    }

    /**
     * Get the rightmost position that should be exposed for horizontal
     * scrolling on the specified line.
     */
    public float getLineRight(int line) {
        int dir = getParagraphDirection(line);
        Alignment align = getParagraphAlignment(line);

<<<<<<< HEAD
        if (align == Alignment.ALIGN_LEFT) {
            return getParagraphLeft(line) + getLineMax(line);
        } else if (align == Alignment.ALIGN_NORMAL) {
=======
        if (align == Alignment.ALIGN_NORMAL) {
>>>>>>> 54b6cfa... Initial Contribution
            if (dir == DIR_RIGHT_TO_LEFT)
                return mWidth;
            else
                return getParagraphLeft(line) + getLineMax(line);
<<<<<<< HEAD
        } else if (align == Alignment.ALIGN_RIGHT) {
            return mWidth;
=======
>>>>>>> 54b6cfa... Initial Contribution
        } else if (align == Alignment.ALIGN_OPPOSITE) {
            if (dir == DIR_RIGHT_TO_LEFT)
                return getLineMax(line);
            else
                return mWidth;
        } else { /* align == Alignment.ALIGN_CENTER */
            int left = getParagraphLeft(line);
            int right = getParagraphRight(line);
            int max = ((int) getLineMax(line)) & ~1;

            return right - ((right - left) - max) / 2;
        }
    }

    /**
<<<<<<< HEAD
     * Gets the unsigned horizontal extent of the specified line, including
     * leading margin indent, but excluding trailing whitespace.
     */
    public float getLineMax(int line) {
        float margin = getParagraphLeadingMargin(line);
        float signedExtent = getLineExtent(line, false);
        return margin + signedExtent >= 0 ? signedExtent : -signedExtent;
    }

    /**
     * Gets the unsigned horizontal extent of the specified line, including
     * leading margin indent and trailing whitespace.
     */
    public float getLineWidth(int line) {
        float margin = getParagraphLeadingMargin(line);
        float signedExtent = getLineExtent(line, true);
        return margin + signedExtent >= 0 ? signedExtent : -signedExtent;
    }

    /**
     * Like {@link #getLineExtent(int,TabStops,boolean)} but determines the
     * tab stops instead of using the ones passed in.
     * @param line the index of the line
     * @param full whether to include trailing whitespace
     * @return the extent of the line
     */
    private float getLineExtent(int line, boolean full) {
        int start = getLineStart(line);
        int end = full ? getLineEnd(line) : getLineVisibleEnd(line);

        boolean hasTabsOrEmoji = getLineContainsTab(line);
        TabStops tabStops = null;
        if (hasTabsOrEmoji && mText instanceof Spanned) {
            // Just checking this line should be good enough, tabs should be
            // consistent across all lines in a paragraph.
            TabStopSpan[] tabs = getParagraphSpans((Spanned) mText, start, end, TabStopSpan.class);
            if (tabs.length > 0) {
                tabStops = new TabStops(TAB_INCREMENT, tabs); // XXX should reuse
            }
        }
        Directions directions = getLineDirections(line);
        // Returned directions can actually be null
        if (directions == null) {
            return 0f;
        }
        int dir = getParagraphDirection(line);

        TextLine tl = TextLine.obtain();
        tl.set(mPaint, mText, start, end, dir, directions, hasTabsOrEmoji, tabStops);
        float width = tl.metrics(null);
        TextLine.recycle(tl);
        return width;
    }

    /**
     * Returns the signed horizontal extent of the specified line, excluding
     * leading margin.  If full is false, excludes trailing whitespace.
     * @param line the index of the line
     * @param tabStops the tab stops, can be null if we know they're not used.
     * @param full whether to include trailing whitespace
     * @return the extent of the text on this line
     */
    private float getLineExtent(int line, TabStops tabStops, boolean full) {
        int start = getLineStart(line);
        int end = full ? getLineEnd(line) : getLineVisibleEnd(line);
        boolean hasTabsOrEmoji = getLineContainsTab(line);
        Directions directions = getLineDirections(line);
        int dir = getParagraphDirection(line);

        TextLine tl = TextLine.obtain();
        tl.set(mPaint, mText, start, end, dir, directions, hasTabsOrEmoji, tabStops);
        float width = tl.metrics(null);
        TextLine.recycle(tl);
        return width;
=======
     * Gets the horizontal extent of the specified line, excluding
     * trailing whitespace.
     */
    public float getLineMax(int line) {
        return getLineMax(line, null, false);
    }

    /**
     * Gets the horizontal extent of the specified line, including
     * trailing whitespace.
     */
    public float getLineWidth(int line) {
        return getLineMax(line, null, true);
    }

    private float getLineMax(int line, Object[] tabs, boolean full) {
        int start = getLineStart(line);
        int end;

        if (full) {
            end = getLineEnd(line);
        } else {
            end = getLineVisibleEnd(line);
        } 
        boolean tab = getLineContainsTab(line);

        if (tabs == null && tab && mText instanceof Spanned) {
            tabs = ((Spanned) mText).getSpans(start, end, TabStopSpan.class);
        }

        return measureText(mPaint, mWorkPaint,
                           mText, start, end, null, tab, tabs);
>>>>>>> 54b6cfa... Initial Contribution
    }

    /**
     * Get the line number corresponding to the specified vertical position.
     * If you ask for a position above 0, you get 0; if you ask for a position
     * below the bottom of the text, you get the last line.
     */
    // FIXME: It may be faster to do a linear search for layouts without many lines.
    public int getLineForVertical(int vertical) {
        int high = getLineCount(), low = -1, guess;

        while (high - low > 1) {
            guess = (high + low) / 2;

            if (getLineTop(guess) > vertical)
                high = guess;
            else
                low = guess;
        }

        if (low < 0)
            return 0;
        else
            return low;
    }

    /**
     * Get the line number on which the specified text offset appears.
     * If you ask for a position before 0, you get 0; if you ask for a position
     * beyond the end of the text, you get the last line.
     */
    public int getLineForOffset(int offset) {
        int high = getLineCount(), low = -1, guess;

        while (high - low > 1) {
            guess = (high + low) / 2;

            if (getLineStart(guess) > offset)
                high = guess;
            else
                low = guess;
        }

        if (low < 0)
            return 0;
        else
            return low;
    }

    /**
<<<<<<< HEAD
     * Get the character offset on the specified line whose position is
=======
     * Get the character offset on the specfied line whose position is
>>>>>>> 54b6cfa... Initial Contribution
     * closest to the specified horizontal position.
     */
    public int getOffsetForHorizontal(int line, float horiz) {
        int max = getLineEnd(line) - 1;
        int min = getLineStart(line);
        Directions dirs = getLineDirections(line);

        if (line == getLineCount() - 1)
            max++;

        int best = min;
        float bestdist = Math.abs(getPrimaryHorizontal(best) - horiz);

<<<<<<< HEAD
        for (int i = 0; i < dirs.mDirections.length; i += 2) {
            int here = min + dirs.mDirections[i];
            int there = here + (dirs.mDirections[i+1] & RUN_LENGTH_MASK);
            int swap = (dirs.mDirections[i+1] & RUN_RTL_FLAG) != 0 ? -1 : 1;

            if (there > max)
                there = max;
=======
        int here = min;
        for (int i = 0; i < dirs.mDirections.length; i++) {
            int there = here + dirs.mDirections[i];
            int swap = ((i & 1) == 0) ? 1 : -1;

            if (there > max)
                there = max;

>>>>>>> 54b6cfa... Initial Contribution
            int high = there - 1 + 1, low = here + 1 - 1, guess;

            while (high - low > 1) {
                guess = (high + low) / 2;
                int adguess = getOffsetAtStartOf(guess);

                if (getPrimaryHorizontal(adguess) * swap >= horiz * swap)
                    high = guess;
                else
                    low = guess;
            }

            if (low < here + 1)
                low = here + 1;

            if (low < there) {
                low = getOffsetAtStartOf(low);

                float dist = Math.abs(getPrimaryHorizontal(low) - horiz);

                int aft = TextUtils.getOffsetAfter(mText, low);
                if (aft < there) {
                    float other = Math.abs(getPrimaryHorizontal(aft) - horiz);

                    if (other < dist) {
                        dist = other;
                        low = aft;
                    }
                }

                if (dist < bestdist) {
                    bestdist = dist;
<<<<<<< HEAD
                    best = low;
=======
                    best = low;   
>>>>>>> 54b6cfa... Initial Contribution
                }
            }

            float dist = Math.abs(getPrimaryHorizontal(here) - horiz);

            if (dist < bestdist) {
                bestdist = dist;
                best = here;
            }
<<<<<<< HEAD
=======

            here = there;
>>>>>>> 54b6cfa... Initial Contribution
        }

        float dist = Math.abs(getPrimaryHorizontal(max) - horiz);

        if (dist < bestdist) {
            bestdist = dist;
            best = max;
        }

        return best;
    }

    /**
     * Return the text offset after the last character on the specified line.
     */
    public final int getLineEnd(int line) {
        return getLineStart(line + 1);
    }

<<<<<<< HEAD
    /**
=======
    /** 
>>>>>>> 54b6cfa... Initial Contribution
     * Return the text offset after the last visible character (so whitespace
     * is not counted) on the specified line.
     */
    public int getLineVisibleEnd(int line) {
        return getLineVisibleEnd(line, getLineStart(line), getLineStart(line+1));
    }
<<<<<<< HEAD

    private int getLineVisibleEnd(int line, int start, int end) {
=======
    
    private int getLineVisibleEnd(int line, int start, int end) {
        if (Config.DEBUG) {
            Assert.assertTrue(getLineStart(line) == start && getLineStart(line+1) == end);
        }

>>>>>>> 54b6cfa... Initial Contribution
        CharSequence text = mText;
        char ch;
        if (line == getLineCount() - 1) {
            return end;
        }

        for (; end > start; end--) {
            ch = text.charAt(end - 1);

            if (ch == '\n') {
                return end - 1;
            }

            if (ch != ' ' && ch != '\t') {
                break;
            }

        }

        return end;
    }

    /**
     * Return the vertical position of the bottom of the specified line.
     */
    public final int getLineBottom(int line) {
        return getLineTop(line + 1);
    }

    /**
     * Return the vertical position of the baseline of the specified line.
     */
    public final int getLineBaseline(int line) {
        // getLineTop(line+1) == getLineTop(line)
        return getLineTop(line+1) - getLineDescent(line);
    }

    /**
     * Get the ascent of the text on the specified line.
     * The return value is negative to match the Paint.ascent() convention.
     */
    public final int getLineAscent(int line) {
        // getLineTop(line+1) - getLineDescent(line) == getLineBaseLine(line)
        return getLineTop(line) - (getLineTop(line+1) - getLineDescent(line));
    }

<<<<<<< HEAD
    public int getOffsetToLeftOf(int offset) {
        return getOffsetToLeftRightOf(offset, true);
    }

    public int getOffsetToRightOf(int offset) {
        return getOffsetToLeftRightOf(offset, false);
    }

    private int getOffsetToLeftRightOf(int caret, boolean toLeft) {
        int line = getLineForOffset(caret);
        int lineStart = getLineStart(line);
        int lineEnd = getLineEnd(line);
        int lineDir = getParagraphDirection(line);

        boolean lineChanged = false;
        boolean advance = toLeft == (lineDir == DIR_RIGHT_TO_LEFT);
        // if walking off line, look at the line we're headed to
        if (advance) {
            if (caret == lineEnd) {
                if (line < getLineCount() - 1) {
                    lineChanged = true;
                    ++line;
                } else {
                    return caret; // at very end, don't move
                }
            }
        } else {
            if (caret == lineStart) {
                if (line > 0) {
                    lineChanged = true;
                    --line;
                } else {
                    return caret; // at very start, don't move
                }
            }
        }

        if (lineChanged) {
            lineStart = getLineStart(line);
            lineEnd = getLineEnd(line);
            int newDir = getParagraphDirection(line);
            if (newDir != lineDir) {
                // unusual case.  we want to walk onto the line, but it runs
                // in a different direction than this one, so we fake movement
                // in the opposite direction.
                toLeft = !toLeft;
                lineDir = newDir;
            }
        }

        Directions directions = getLineDirections(line);

        TextLine tl = TextLine.obtain();
        // XXX: we don't care about tabs
        tl.set(mPaint, mText, lineStart, lineEnd, lineDir, directions, false, null);
        caret = lineStart + tl.getOffsetToLeftRightOf(caret - lineStart, toLeft);
        tl = TextLine.recycle(tl);
        return caret;
    }

    private int getOffsetAtStartOf(int offset) {
        // XXX this probably should skip local reorderings and
        // zero-width characters, look at callers
=======
    /**
     * Return the text offset that would be reached by moving left
     * (possibly onto another line) from the specified offset.
     */
    public int getOffsetToLeftOf(int offset) {
        int line = getLineForOffset(offset);
        int start = getLineStart(line);
        int end = getLineEnd(line);
        Directions dirs = getLineDirections(line);

        if (line != getLineCount() - 1)
            end--;

        float horiz = getPrimaryHorizontal(offset);

        int best = offset;
        float besth = Integer.MIN_VALUE;
        int candidate;

        candidate = TextUtils.getOffsetBefore(mText, offset);
        if (candidate >= start && candidate <= end) {
            float h = getPrimaryHorizontal(candidate);

            if (h < horiz && h > besth) {
                best = candidate;
                besth = h;
            }
        }

        candidate = TextUtils.getOffsetAfter(mText, offset);
        if (candidate >= start && candidate <= end) {
            float h = getPrimaryHorizontal(candidate);

            if (h < horiz && h > besth) {
                best = candidate;
                besth = h;
            }
        }

        int here = start;
        for (int i = 0; i < dirs.mDirections.length; i++) {
            int there = here + dirs.mDirections[i];
            if (there > end)
                there = end;

            float h = getPrimaryHorizontal(here);

            if (h < horiz && h > besth) {
                best = here;
                besth = h;
            }

            candidate = TextUtils.getOffsetAfter(mText, here);
            if (candidate >= start && candidate <= end) {
                h = getPrimaryHorizontal(candidate);

                if (h < horiz && h > besth) {
                    best = candidate;
                    besth = h;
                }
            }

            candidate = TextUtils.getOffsetBefore(mText, there);
            if (candidate >= start && candidate <= end) {
                h = getPrimaryHorizontal(candidate);

                if (h < horiz && h > besth) {
                    best = candidate;
                    besth = h;
                }
            }

            here = there;
        }

        float h = getPrimaryHorizontal(end);

        if (h < horiz && h > besth) {
            best = end;
            besth = h;
        }

        if (best != offset)
            return best;

        int dir = getParagraphDirection(line);

        if (dir > 0) {
            if (line == 0)
                return best;
            else
                return getOffsetForHorizontal(line - 1, 10000);
        } else {
            if (line == getLineCount() - 1)
                return best;
            else
                return getOffsetForHorizontal(line + 1, 10000);
        }
    }

    /**
     * Return the text offset that would be reached by moving right
     * (possibly onto another line) from the specified offset.
     */
    public int getOffsetToRightOf(int offset) {
        int line = getLineForOffset(offset);
        int start = getLineStart(line);
        int end = getLineEnd(line);
        Directions dirs = getLineDirections(line);

        if (line != getLineCount() - 1)
            end--;

        float horiz = getPrimaryHorizontal(offset);

        int best = offset;
        float besth = Integer.MAX_VALUE;
        int candidate;

        candidate = TextUtils.getOffsetBefore(mText, offset);
        if (candidate >= start && candidate <= end) {
            float h = getPrimaryHorizontal(candidate);

            if (h > horiz && h < besth) {
                best = candidate;
                besth = h;
            }
        }

        candidate = TextUtils.getOffsetAfter(mText, offset);
        if (candidate >= start && candidate <= end) {
            float h = getPrimaryHorizontal(candidate);

            if (h > horiz && h < besth) {
                best = candidate;
                besth = h;
            }
        }

        int here = start;
        for (int i = 0; i < dirs.mDirections.length; i++) {
            int there = here + dirs.mDirections[i];
            if (there > end)
                there = end;

            float h = getPrimaryHorizontal(here);

            if (h > horiz && h < besth) {
                best = here;
                besth = h;
            }

            candidate = TextUtils.getOffsetAfter(mText, here);
            if (candidate >= start && candidate <= end) {
                h = getPrimaryHorizontal(candidate);

                if (h > horiz && h < besth) {
                    best = candidate;
                    besth = h;
                }
            }

            candidate = TextUtils.getOffsetBefore(mText, there);
            if (candidate >= start && candidate <= end) {
                h = getPrimaryHorizontal(candidate);

                if (h > horiz && h < besth) {
                    best = candidate;
                    besth = h;
                }
            }

            here = there;
        }

        float h = getPrimaryHorizontal(end);

        if (h > horiz && h < besth) {
            best = end;
            besth = h;
        }

        if (best != offset)
            return best;

        int dir = getParagraphDirection(line);

        if (dir > 0) {
            if (line == getLineCount() - 1)
                return best;
            else
                return getOffsetForHorizontal(line + 1, -10000);
        } else {
            if (line == 0)
                return best;
            else
                return getOffsetForHorizontal(line - 1, -10000);
        }
    }

    private int getOffsetAtStartOf(int offset) {
>>>>>>> 54b6cfa... Initial Contribution
        if (offset == 0)
            return 0;

        CharSequence text = mText;
        char c = text.charAt(offset);

        if (c >= '\uDC00' && c <= '\uDFFF') {
            char c1 = text.charAt(offset - 1);

            if (c1 >= '\uD800' && c1 <= '\uDBFF')
                offset -= 1;
        }

        if (mSpannedText) {
            ReplacementSpan[] spans = ((Spanned) text).getSpans(offset, offset,
                                                       ReplacementSpan.class);

            for (int i = 0; i < spans.length; i++) {
                int start = ((Spanned) text).getSpanStart(spans[i]);
                int end = ((Spanned) text).getSpanEnd(spans[i]);

                if (start < offset && end > offset)
                    offset = start;
            }
        }

        return offset;
    }

    /**
     * Fills in the specified Path with a representation of a cursor
     * at the specified offset.  This will often be a vertical line
<<<<<<< HEAD
     * but can be multiple discontinuous lines in text with multiple
=======
     * but can be multiple discontinous lines in text with multiple
>>>>>>> 54b6cfa... Initial Contribution
     * directionalities.
     */
    public void getCursorPath(int point, Path dest,
                              CharSequence editingBuffer) {
        dest.reset();

        int line = getLineForOffset(point);
        int top = getLineTop(line);
        int bottom = getLineTop(line+1);

        float h1 = getPrimaryHorizontal(point) - 0.5f;
<<<<<<< HEAD
        float h2 = isLevelBoundary(point) ? getSecondaryHorizontal(point) - 0.5f : h1;

        int caps = TextKeyListener.getMetaState(editingBuffer, TextKeyListener.META_SHIFT_ON) |
                   TextKeyListener.getMetaState(editingBuffer, TextKeyListener.META_SELECTING);
        int fn = TextKeyListener.getMetaState(editingBuffer, TextKeyListener.META_ALT_ON);
=======
        float h2 = getSecondaryHorizontal(point) - 0.5f;

        int caps = TextKeyListener.getMetaState(editingBuffer,
                                                KeyEvent.META_SHIFT_ON);
        int fn = TextKeyListener.getMetaState(editingBuffer,
                                              KeyEvent.META_ALT_ON);
>>>>>>> 54b6cfa... Initial Contribution
        int dist = 0;

        if (caps != 0 || fn != 0) {
            dist = (bottom - top) >> 2;

            if (fn != 0)
                top += dist;
            if (caps != 0)
                bottom -= dist;
        }

        if (h1 < 0.5f)
            h1 = 0.5f;
        if (h2 < 0.5f)
            h2 = 0.5f;

<<<<<<< HEAD
        if (Float.compare(h1, h2) == 0) {
=======
        if (h1 == h2) {
>>>>>>> 54b6cfa... Initial Contribution
            dest.moveTo(h1, top);
            dest.lineTo(h1, bottom);
        } else {
            dest.moveTo(h1, top);
            dest.lineTo(h1, (top + bottom) >> 1);

            dest.moveTo(h2, (top + bottom) >> 1);
            dest.lineTo(h2, bottom);
        }

        if (caps == 2) {
            dest.moveTo(h2, bottom);
            dest.lineTo(h2 - dist, bottom + dist);
            dest.lineTo(h2, bottom);
            dest.lineTo(h2 + dist, bottom + dist);
        } else if (caps == 1) {
            dest.moveTo(h2, bottom);
            dest.lineTo(h2 - dist, bottom + dist);

            dest.moveTo(h2 - dist, bottom + dist - 0.5f);
            dest.lineTo(h2 + dist, bottom + dist - 0.5f);

            dest.moveTo(h2 + dist, bottom + dist);
            dest.lineTo(h2, bottom);
        }

        if (fn == 2) {
            dest.moveTo(h1, top);
            dest.lineTo(h1 - dist, top - dist);
            dest.lineTo(h1, top);
            dest.lineTo(h1 + dist, top - dist);
        } else if (fn == 1) {
            dest.moveTo(h1, top);
            dest.lineTo(h1 - dist, top - dist);

            dest.moveTo(h1 - dist, top - dist + 0.5f);
            dest.lineTo(h1 + dist, top - dist + 0.5f);

            dest.moveTo(h1 + dist, top - dist);
            dest.lineTo(h1, top);
        }
    }

    private void addSelection(int line, int start, int end,
                              int top, int bottom, Path dest) {
        int linestart = getLineStart(line);
        int lineend = getLineEnd(line);
        Directions dirs = getLineDirections(line);

        if (lineend > linestart && mText.charAt(lineend - 1) == '\n')
            lineend--;

<<<<<<< HEAD
        for (int i = 0; i < dirs.mDirections.length; i += 2) {
            int here = linestart + dirs.mDirections[i];
            int there = here + (dirs.mDirections[i+1] & RUN_LENGTH_MASK);

=======
        int here = linestart;
        for (int i = 0; i < dirs.mDirections.length; i++) {
            int there = here + dirs.mDirections[i];
>>>>>>> 54b6cfa... Initial Contribution
            if (there > lineend)
                there = lineend;

            if (start <= there && end >= here) {
                int st = Math.max(start, here);
                int en = Math.min(end, there);

                if (st != en) {
<<<<<<< HEAD
                    float h1 = getHorizontal(st, false, line);
                    float h2 = getHorizontal(en, true, line);

                    float left = Math.min(h1, h2);
                    float right = Math.max(h1, h2);

                    dest.addRect(left, top, right, bottom, Path.Direction.CW);
                }
            }
=======
                    float h1 = getHorizontal(st, false, false, line);
                    float h2 = getHorizontal(en, true, false, line);

                    dest.addRect(h1, top, h2, bottom, Path.Direction.CW);
                }
            }

            here = there;
>>>>>>> 54b6cfa... Initial Contribution
        }
    }

    /**
     * Fills in the specified Path with a representation of a highlight
     * between the specified offsets.  This will often be a rectangle
     * or a potentially discontinuous set of rectangles.  If the start
     * and end are the same, the returned path is empty.
     */
    public void getSelectionPath(int start, int end, Path dest) {
        dest.reset();

        if (start == end)
            return;

        if (end < start) {
            int temp = end;
            end = start;
            start = temp;
        }

        int startline = getLineForOffset(start);
        int endline = getLineForOffset(end);

        int top = getLineTop(startline);
        int bottom = getLineBottom(endline);

        if (startline == endline) {
            addSelection(startline, start, end, top, bottom, dest);
        } else {
            final float width = mWidth;

            addSelection(startline, start, getLineEnd(startline),
                         top, getLineBottom(startline), dest);
<<<<<<< HEAD

=======
            
>>>>>>> 54b6cfa... Initial Contribution
            if (getParagraphDirection(startline) == DIR_RIGHT_TO_LEFT)
                dest.addRect(getLineLeft(startline), top,
                              0, getLineBottom(startline), Path.Direction.CW);
            else
                dest.addRect(getLineRight(startline), top,
                              width, getLineBottom(startline), Path.Direction.CW);

            for (int i = startline + 1; i < endline; i++) {
                top = getLineTop(i);
                bottom = getLineBottom(i);
                dest.addRect(0, top, width, bottom, Path.Direction.CW);
            }

            top = getLineTop(endline);
            bottom = getLineBottom(endline);

            addSelection(endline, getLineStart(endline), end,
                         top, bottom, dest);

            if (getParagraphDirection(endline) == DIR_RIGHT_TO_LEFT)
                dest.addRect(width, top, getLineRight(endline), bottom, Path.Direction.CW);
            else
                dest.addRect(0, top, getLineLeft(endline), bottom, Path.Direction.CW);
        }
    }

    /**
     * Get the alignment of the specified paragraph, taking into account
     * markup attached to it.
     */
    public final Alignment getParagraphAlignment(int line) {
        Alignment align = mAlignment;

        if (mSpannedText) {
            Spanned sp = (Spanned) mText;
<<<<<<< HEAD
            AlignmentSpan[] spans = getParagraphSpans(sp, getLineStart(line),
=======
            AlignmentSpan[] spans = sp.getSpans(getLineStart(line),
>>>>>>> 54b6cfa... Initial Contribution
                                                getLineEnd(line),
                                                AlignmentSpan.class);

            int spanLength = spans.length;
            if (spanLength > 0) {
                align = spans[spanLength-1].getAlignment();
            }
        }

        return align;
    }

    /**
     * Get the left edge of the specified paragraph, inset by left margins.
     */
    public final int getParagraphLeft(int line) {
<<<<<<< HEAD
        int left = 0;
        int dir = getParagraphDirection(line);
        if (dir == DIR_RIGHT_TO_LEFT || !mSpannedText) {
            return left; // leading margin has no impact, or no styles
        }
        return getParagraphLeadingMargin(line);
=======
        int dir = getParagraphDirection(line);

        int left = 0;

        boolean par = false;
        int off = getLineStart(line);
        if (off == 0 || mText.charAt(off - 1) == '\n')
            par = true;

        if (dir == DIR_LEFT_TO_RIGHT) {
            if (mSpannedText) {
                Spanned sp = (Spanned) mText;
                LeadingMarginSpan[] spans = sp.getSpans(getLineStart(line),
                                                        getLineEnd(line),
                                                        LeadingMarginSpan.class);

                for (int i = 0; i < spans.length; i++) {
                    left += spans[i].getLeadingMargin(par);
                }
            }
        }

        return left;
>>>>>>> 54b6cfa... Initial Contribution
    }

    /**
     * Get the right edge of the specified paragraph, inset by right margins.
     */
    public final int getParagraphRight(int line) {
<<<<<<< HEAD
        int right = mWidth;
        int dir = getParagraphDirection(line);
        if (dir == DIR_LEFT_TO_RIGHT || !mSpannedText) {
            return right; // leading margin has no impact, or no styles
        }
        return right - getParagraphLeadingMargin(line);
    }

    /**
     * Returns the effective leading margin (unsigned) for this line,
     * taking into account LeadingMarginSpan and LeadingMarginSpan2.
     * @param line the line index
     * @return the leading margin of this line
     */
    private int getParagraphLeadingMargin(int line) {
        if (!mSpannedText) {
            return 0;
        }
        Spanned spanned = (Spanned) mText;

        int lineStart = getLineStart(line);
        int lineEnd = getLineEnd(line);
        int spanEnd = spanned.nextSpanTransition(lineStart, lineEnd,
                LeadingMarginSpan.class);
        LeadingMarginSpan[] spans = getParagraphSpans(spanned, lineStart, spanEnd,
                                                LeadingMarginSpan.class);
        if (spans.length == 0) {
            return 0; // no leading margin span;
        }

        int margin = 0;

        boolean isFirstParaLine = lineStart == 0 ||
            spanned.charAt(lineStart - 1) == '\n';

        for (int i = 0; i < spans.length; i++) {
            LeadingMarginSpan span = spans[i];
            boolean useFirstLineMargin = isFirstParaLine;
            if (span instanceof LeadingMarginSpan2) {
                int spStart = spanned.getSpanStart(span);
                int spanLine = getLineForOffset(spStart);
                int count = ((LeadingMarginSpan2)span).getLeadingMarginLineCount();
                useFirstLineMargin = line < spanLine + count;
            }
            margin += span.getLeadingMargin(useFirstLineMargin);
        }

        return margin;
    }

    /* package */
    static float measurePara(TextPaint paint, CharSequence text, int start, int end) {

        MeasuredText mt = MeasuredText.obtain();
        TextLine tl = TextLine.obtain();
        try {
            mt.setPara(text, start, end, TextDirectionHeuristics.LTR);
            Directions directions;
            int dir;
            if (mt.mEasy) {
                directions = DIRS_ALL_LEFT_TO_RIGHT;
                dir = Layout.DIR_LEFT_TO_RIGHT;
            } else {
                directions = AndroidBidi.directions(mt.mDir, mt.mLevels,
                    0, mt.mChars, 0, mt.mLen);
                dir = mt.mDir;
            }
            char[] chars = mt.mChars;
            int len = mt.mLen;
            boolean hasTabs = false;
            TabStops tabStops = null;
            for (int i = 0; i < len; ++i) {
                if (chars[i] == '\t') {
                    hasTabs = true;
                    if (text instanceof Spanned) {
                        Spanned spanned = (Spanned) text;
                        int spanEnd = spanned.nextSpanTransition(start, end,
                                TabStopSpan.class);
                        TabStopSpan[] spans = getParagraphSpans(spanned, start, spanEnd,
                                TabStopSpan.class);
                        if (spans.length > 0) {
                            tabStops = new TabStops(TAB_INCREMENT, spans);
                        }
                    }
                    break;
                }
            }
            tl.set(paint, text, start, end, dir, directions, hasTabs, tabStops);
            return tl.metrics(null);
        } finally {
            TextLine.recycle(tl);
            MeasuredText.recycle(mt);
        }
    }

    /**
     * @hide
     */
    /* package */ static class TabStops {
        private int[] mStops;
        private int mNumStops;
        private int mIncrement;

        TabStops(int increment, Object[] spans) {
            reset(increment, spans);
        }

        void reset(int increment, Object[] spans) {
            this.mIncrement = increment;

            int ns = 0;
            if (spans != null) {
                int[] stops = this.mStops;
                for (Object o : spans) {
                    if (o instanceof TabStopSpan) {
                        if (stops == null) {
                            stops = new int[10];
                        } else if (ns == stops.length) {
                            int[] nstops = new int[ns * 2];
                            for (int i = 0; i < ns; ++i) {
                                nstops[i] = stops[i];
                            }
                            stops = nstops;
                        }
                        stops[ns++] = ((TabStopSpan) o).getTabStop();
                    }
                }
                if (ns > 1) {
                    Arrays.sort(stops, 0, ns);
                }
                if (stops != this.mStops) {
                    this.mStops = stops;
                }
            }
            this.mNumStops = ns;
        }

        float nextTab(float h) {
            int ns = this.mNumStops;
            if (ns > 0) {
                int[] stops = this.mStops;
                for (int i = 0; i < ns; ++i) {
                    int stop = stops[i];
                    if (stop > h) {
                        return stop;
                    }
                }
            }
            return nextDefaultStop(h, mIncrement);
        }

        public static float nextDefaultStop(float h, int inc) {
            return ((int) ((h + inc) / inc)) * inc;
        }
    }

    /**
     * Returns the position of the next tab stop after h on the line.
     *
     * @param text the text
     * @param start start of the line
     * @param end limit of the line
     * @param h the current horizontal offset
     * @param tabs the tabs, can be null.  If it is null, any tabs in effect
     * on the line will be used.  If there are no tabs, a default offset
     * will be used to compute the tab stop.
     * @return the offset of the next tab stop.
     */
=======
        int dir = getParagraphDirection(line);

        int right = mWidth;

        boolean par = false;
        int off = getLineStart(line);
        if (off == 0 || mText.charAt(off - 1) == '\n')
            par = true;


        if (dir == DIR_RIGHT_TO_LEFT) {
            if (mSpannedText) {
                Spanned sp = (Spanned) mText;
                LeadingMarginSpan[] spans = sp.getSpans(getLineStart(line),
                                                        getLineEnd(line),
                                                        LeadingMarginSpan.class);

                for (int i = 0; i < spans.length; i++) {
                    right -= spans[i].getLeadingMargin(par);
                }
            }
        }

        return right;
    }

    private static void drawText(Canvas canvas,
                                 CharSequence text, int start, int end,
                                 int dir, Directions directions,
                                 float x, int top, int y, int bottom,
                                 TextPaint paint,
                                 TextPaint workPaint,
                                 boolean hasTabs, Object[] parspans) {
        char[] buf;
        if (!hasTabs) {
            if (directions == DIRS_ALL_LEFT_TO_RIGHT) {
                if (Config.DEBUG) {
                    Assert.assertTrue(DIR_LEFT_TO_RIGHT == dir);
                }
                Styled.drawText(canvas, text, start, end, dir, false, x, top, y, bottom, paint, workPaint, false);
                return;
            }
            buf = null;
        } else {
            buf = TextUtils.obtain(end - start);
            TextUtils.getChars(text, start, end, buf, 0);
        }

        float h = 0;

        int here = 0;
        for (int i = 0; i < directions.mDirections.length; i++) {
            int there = here + directions.mDirections[i];
            if (there > end - start)
                there = end - start;

            int segstart = here;
            for (int j = hasTabs ? here : there; j <= there; j++) {
                if (j == there || buf[j] == '\t') {
                    h += Styled.drawText(canvas, text,
                                         start + segstart, start + j,
                                         dir, (i & 1) != 0, x + h,
                                         top, y, bottom, paint, workPaint,
                                         start + j != end);

                    if (j != there && buf[j] == '\t')
                        h = dir * nextTab(text, start, end, h * dir, parspans);

                    segstart = j + 1;
                }
            }

            here = there;
        }

        if (hasTabs)
            TextUtils.recycle(buf);
    }

    private static float measureText(TextPaint paint,
                                     TextPaint workPaint,
                                     CharSequence text,
                                     int start, int offset, int end,
                                     int dir, Directions directions,
                                     boolean trailing, boolean alt,
                                     boolean hasTabs, Object[] tabs) {
        char[] buf = null;

        if (hasTabs) {
            buf = TextUtils.obtain(end - start);
            TextUtils.getChars(text, start, end, buf, 0);
        }

        float h = 0;

        if (alt) {
            if (dir == DIR_RIGHT_TO_LEFT)
                trailing = !trailing;
        }

        int here = 0;
        for (int i = 0; i < directions.mDirections.length; i++) {
            if (alt)
                trailing = !trailing;

            int there = here + directions.mDirections[i];
            if (there > end - start)
                there = end - start;

            int segstart = here;
            for (int j = hasTabs ? here : there; j <= there; j++) {
                if (j == there || buf[j] == '\t') {
                    float segw;

                    if (offset < start + j ||
                       (trailing && offset <= start + j)) {
                        if (dir == DIR_LEFT_TO_RIGHT && (i & 1) == 0) {
                            h += Styled.measureText(paint, workPaint, text,
                                                    start + segstart, offset,
                                                    null);
                            return h;
                        }

                        if (dir == DIR_RIGHT_TO_LEFT && (i & 1) != 0) {
                            h -= Styled.measureText(paint, workPaint, text,
                                                    start + segstart, offset,
                                                    null);
                            return h;
                        }
                    }

                    segw = Styled.measureText(paint, workPaint, text,
                                              start + segstart, start + j,
                                              null);

                    if (offset < start + j ||
                        (trailing && offset <= start + j)) {
                        if (dir == DIR_LEFT_TO_RIGHT) {
                            h += segw - Styled.measureText(paint, workPaint,
                                                           text,
                                                           start + segstart,
                                                           offset, null);
                            return h;
                        }

                        if (dir == DIR_RIGHT_TO_LEFT) {
                            h -= segw - Styled.measureText(paint, workPaint,
                                                           text,
                                                           start + segstart,
                                                           offset, null);
                            return h;
                        }
                    }

                    if (dir == DIR_RIGHT_TO_LEFT)
                        h -= segw;
                    else
                        h += segw;

                    if (j != there && buf[j] == '\t') {
                        if (offset == start + j)
                            return h;

                        h = dir * nextTab(text, start, end, h * dir, tabs);
                    }

                    segstart = j + 1;
                }
            }

            here = there;
        }

        if (hasTabs)
            TextUtils.recycle(buf);

        return h;
    }

    /* package */ static float measureText(TextPaint paint,
                                           TextPaint workPaint,
                                           CharSequence text,
                                           int start, int end,
                                           Paint.FontMetricsInt fm,
                                           boolean hasTabs, Object[] tabs) {
        char[] buf = null;
  
        if (hasTabs) {
            buf = TextUtils.obtain(end - start);
            TextUtils.getChars(text, start, end, buf, 0);
        }

        int len = end - start;

        int here = 0;
        float h = 0;
        int ab = 0, be = 0;
        int top = 0, bot = 0;

        if (fm != null) {
            fm.ascent = 0;
            fm.descent = 0;
        }

        for (int i = hasTabs ? 0 : len; i <= len; i++) {
            if (i == len || buf[i] == '\t') {
                workPaint.baselineShift = 0;

                h += Styled.measureText(paint, workPaint, text,
                                        start + here, start + i,
                                        fm);

                if (fm != null) {
                    if (workPaint.baselineShift < 0) {
                        fm.ascent += workPaint.baselineShift;
                        fm.top += workPaint.baselineShift;
                    } else {
                        fm.descent += workPaint.baselineShift;
                        fm.bottom += workPaint.baselineShift;
                    }
                }

                if (i != len)
                    h = nextTab(text, start, end, h, tabs);

                if (fm != null) {
                    if (fm.ascent < ab) {
                        ab = fm.ascent;
                    }
                    if (fm.descent > be) {
                        be = fm.descent;
                    }

                    if (fm.top < top) {
                        top = fm.top;
                    }
                    if (fm.bottom > bot) {
                        bot = fm.bottom;
                    }
                }

                here = i + 1;
            }
        }

        if (fm != null) {
            fm.ascent = ab;
            fm.descent = be;
            fm.top = top;
            fm.bottom = bot;
        }

        if (hasTabs)
            TextUtils.recycle(buf);

        return h;
    }

>>>>>>> 54b6cfa... Initial Contribution
    /* package */ static float nextTab(CharSequence text, int start, int end,
                                       float h, Object[] tabs) {
        float nh = Float.MAX_VALUE;
        boolean alltabs = false;

        if (text instanceof Spanned) {
            if (tabs == null) {
<<<<<<< HEAD
                tabs = getParagraphSpans((Spanned) text, start, end, TabStopSpan.class);
=======
                tabs = ((Spanned) text).getSpans(start, end, TabStopSpan.class);
>>>>>>> 54b6cfa... Initial Contribution
                alltabs = true;
            }

            for (int i = 0; i < tabs.length; i++) {
                if (!alltabs) {
                    if (!(tabs[i] instanceof TabStopSpan))
                        continue;
                }

                int where = ((TabStopSpan) tabs[i]).getTabStop();

                if (where < nh && where > h)
                    nh = where;
            }

            if (nh != Float.MAX_VALUE)
                return nh;
        }

        return ((int) ((h + TAB_INCREMENT) / TAB_INCREMENT)) * TAB_INCREMENT;
    }

    protected final boolean isSpanned() {
        return mSpannedText;
    }

<<<<<<< HEAD
    /**
     * Returns the same as <code>text.getSpans()</code>, except where
     * <code>start</code> and <code>end</code> are the same and are not
     * at the very beginning of the text, in which case an empty array
     * is returned instead.
     * <p>
     * This is needed because of the special case that <code>getSpans()</code>
     * on an empty range returns the spans adjacent to that range, which is
     * primarily for the sake of <code>TextWatchers</code> so they will get
     * notifications when text goes from empty to non-empty.  But it also
     * has the unfortunate side effect that if the text ends with an empty
     * paragraph, that paragraph accidentally picks up the styles of the
     * preceding paragraph (even though those styles will not be picked up
     * by new text that is inserted into the empty paragraph).
     * <p>
     * The reason it just checks whether <code>start</code> and <code>end</code>
     * is the same is that the only time a line can contain 0 characters
     * is if it is the final paragraph of the Layout; otherwise any line will
     * contain at least one printing or newline character.  The reason for the
     * additional check if <code>start</code> is greater than 0 is that
     * if the empty paragraph is the entire content of the buffer, paragraph
     * styles that are already applied to the buffer will apply to text that
     * is inserted into it.
     */
    /* package */static <T> T[] getParagraphSpans(Spanned text, int start, int end, Class<T> type) {
        if (start == end && start > 0) {
            return ArrayUtils.emptyArray(type);
        }

        return text.getSpans(start, end, type);
    }

    private char getEllipsisChar(TextUtils.TruncateAt method) {
        return (method == TextUtils.TruncateAt.END_SMALL) ?
                ELLIPSIS_TWO_DOTS[0] :
                ELLIPSIS_NORMAL[0];
    }

    private void ellipsize(int start, int end, int line,
                           char[] dest, int destoff, TextUtils.TruncateAt method) {
=======
    private void ellipsize(int start, int end, int line,
                           char[] dest, int destoff) {
>>>>>>> 54b6cfa... Initial Contribution
        int ellipsisCount = getEllipsisCount(line);

        if (ellipsisCount == 0) {
            return;
        }

        int ellipsisStart = getEllipsisStart(line);
        int linestart = getLineStart(line);

        for (int i = ellipsisStart; i < ellipsisStart + ellipsisCount; i++) {
            char c;

            if (i == ellipsisStart) {
<<<<<<< HEAD
                c = getEllipsisChar(method); // ellipsis
=======
                c = '\u2026'; // ellipsis
>>>>>>> 54b6cfa... Initial Contribution
            } else {
                c = '\uFEFF'; // 0-width space
            }

            int a = i + linestart;

            if (a >= start && a < end) {
                dest[destoff + a - start] = c;
            }
        }
    }

    /**
     * Stores information about bidirectional (left-to-right or right-to-left)
<<<<<<< HEAD
     * text within the layout of a line.
     */
    public static class Directions {
        // Directions represents directional runs within a line of text.
        // Runs are pairs of ints listed in visual order, starting from the
        // leading margin.  The first int of each pair is the offset from
        // the first character of the line to the start of the run.  The
        // second int represents both the length and level of the run.
        // The length is in the lower bits, accessed by masking with
        // DIR_LENGTH_MASK.  The level is in the higher bits, accessed
        // by shifting by DIR_LEVEL_SHIFT and masking by DIR_LEVEL_MASK.
        // To simply test for an RTL direction, test the bit using
        // DIR_RTL_FLAG, if set then the direction is rtl.

        /* package */ int[] mDirections;
        /* package */ Directions(int[] dirs) {
=======
     * text within the layout of a line.  TODO: This work is not complete
     * or correct and will be fleshed out in a later revision.
     */
    public static class Directions {
        private short[] mDirections;

        /* package */ Directions(short[] dirs) {
>>>>>>> 54b6cfa... Initial Contribution
            mDirections = dirs;
        }
    }

    /**
     * Return the offset of the first character to be ellipsized away,
     * relative to the start of the line.  (So 0 if the beginning of the
     * line is ellipsized, not getLineStart().)
     */
    public abstract int getEllipsisStart(int line);
<<<<<<< HEAD

=======
>>>>>>> 54b6cfa... Initial Contribution
    /**
     * Returns the number of characters to be ellipsized away, or 0 if
     * no ellipsis is to take place.
     */
    public abstract int getEllipsisCount(int line);

    /* package */ static class Ellipsizer implements CharSequence, GetChars {
        /* package */ CharSequence mText;
        /* package */ Layout mLayout;
        /* package */ int mWidth;
        /* package */ TextUtils.TruncateAt mMethod;

        public Ellipsizer(CharSequence s) {
            mText = s;
        }

        public char charAt(int off) {
            char[] buf = TextUtils.obtain(1);
            getChars(off, off + 1, buf, 0);
            char ret = buf[0];

            TextUtils.recycle(buf);
            return ret;
        }

        public void getChars(int start, int end, char[] dest, int destoff) {
            int line1 = mLayout.getLineForOffset(start);
            int line2 = mLayout.getLineForOffset(end);

            TextUtils.getChars(mText, start, end, dest, destoff);

            for (int i = line1; i <= line2; i++) {
<<<<<<< HEAD
                mLayout.ellipsize(start, end, i, dest, destoff, mMethod);
=======
                mLayout.ellipsize(start, end, i, dest, destoff);
>>>>>>> 54b6cfa... Initial Contribution
            }
        }

        public int length() {
            return mText.length();
        }
<<<<<<< HEAD

=======
    
>>>>>>> 54b6cfa... Initial Contribution
        public CharSequence subSequence(int start, int end) {
            char[] s = new char[end - start];
            getChars(start, end, s, 0);
            return new String(s);
        }

<<<<<<< HEAD
        @Override
=======
>>>>>>> 54b6cfa... Initial Contribution
        public String toString() {
            char[] s = new char[length()];
            getChars(0, length(), s, 0);
            return new String(s);
        }

    }

<<<<<<< HEAD
    /* package */ static class SpannedEllipsizer extends Ellipsizer implements Spanned {
=======
    /* package */ static class SpannedEllipsizer
                    extends Ellipsizer implements Spanned {
>>>>>>> 54b6cfa... Initial Contribution
        private Spanned mSpanned;

        public SpannedEllipsizer(CharSequence display) {
            super(display);
            mSpanned = (Spanned) display;
        }

        public <T> T[] getSpans(int start, int end, Class<T> type) {
            return mSpanned.getSpans(start, end, type);
        }

        public int getSpanStart(Object tag) {
            return mSpanned.getSpanStart(tag);
        }

        public int getSpanEnd(Object tag) {
            return mSpanned.getSpanEnd(tag);
        }

        public int getSpanFlags(Object tag) {
            return mSpanned.getSpanFlags(tag);
        }

<<<<<<< HEAD
        @SuppressWarnings("rawtypes")
=======
>>>>>>> 54b6cfa... Initial Contribution
        public int nextSpanTransition(int start, int limit, Class type) {
            return mSpanned.nextSpanTransition(start, limit, type);
        }

<<<<<<< HEAD
        @Override
=======
>>>>>>> 54b6cfa... Initial Contribution
        public CharSequence subSequence(int start, int end) {
            char[] s = new char[end - start];
            getChars(start, end, s, 0);

            SpannableString ss = new SpannableString(new String(s));
            TextUtils.copySpansFrom(mSpanned, start, end, Object.class, ss, 0);
            return ss;
        }
    }

    private CharSequence mText;
    private TextPaint mPaint;
    /* package */ TextPaint mWorkPaint;
    private int mWidth;
    private Alignment mAlignment = Alignment.ALIGN_NORMAL;
    private float mSpacingMult;
    private float mSpacingAdd;
<<<<<<< HEAD
    private static final Rect sTempRect = new Rect();
    private boolean mSpannedText;
    private TextDirectionHeuristic mTextDir;
    private SpanSet<LineBackgroundSpan> mLineBackgroundSpans;
=======
    private static Rect sTempRect = new Rect();
    private boolean mSpannedText;
>>>>>>> 54b6cfa... Initial Contribution

    public static final int DIR_LEFT_TO_RIGHT = 1;
    public static final int DIR_RIGHT_TO_LEFT = -1;

<<<<<<< HEAD
    /* package */ static final int DIR_REQUEST_LTR = 1;
    /* package */ static final int DIR_REQUEST_RTL = -1;
    /* package */ static final int DIR_REQUEST_DEFAULT_LTR = 2;
    /* package */ static final int DIR_REQUEST_DEFAULT_RTL = -2;

    /* package */ static final int RUN_LENGTH_MASK = 0x03ffffff;
    /* package */ static final int RUN_LEVEL_SHIFT = 26;
    /* package */ static final int RUN_LEVEL_MASK = 0x3f;
    /* package */ static final int RUN_RTL_FLAG = 1 << RUN_LEVEL_SHIFT;

=======
>>>>>>> 54b6cfa... Initial Contribution
    public enum Alignment {
        ALIGN_NORMAL,
        ALIGN_OPPOSITE,
        ALIGN_CENTER,
<<<<<<< HEAD
        /** @hide */
        ALIGN_LEFT,
        /** @hide */
        ALIGN_RIGHT,
=======
        // XXX ALIGN_LEFT,
        // XXX ALIGN_RIGHT,
>>>>>>> 54b6cfa... Initial Contribution
    }

    private static final int TAB_INCREMENT = 20;

    /* package */ static final Directions DIRS_ALL_LEFT_TO_RIGHT =
<<<<<<< HEAD
        new Directions(new int[] { 0, RUN_LENGTH_MASK });
    /* package */ static final Directions DIRS_ALL_RIGHT_TO_LEFT =
        new Directions(new int[] { 0, RUN_LENGTH_MASK | RUN_RTL_FLAG });

    /* package */ static final char[] ELLIPSIS_NORMAL = { '\u2026' }; // this is "..."
    /* package */ static final char[] ELLIPSIS_TWO_DOTS = { '\u2025' }; // this is ".."
}
=======
                                       new Directions(new short[] { 32767 });
    /* package */ static final Directions DIRS_ALL_RIGHT_TO_LEFT =
                                       new Directions(new short[] { 0, 32767 });

}

>>>>>>> 54b6cfa... Initial Contribution
