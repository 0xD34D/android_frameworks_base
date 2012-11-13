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
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.text.style.LeadingMarginSpan;
import android.text.style.LeadingMarginSpan.LeadingMarginSpan2;
import android.text.style.LineHeightSpan;
import android.text.style.MetricAffectingSpan;
import android.text.style.TabStopSpan;
import android.util.Log;

import com.android.internal.util.ArrayUtils;
=======
import android.graphics.Paint;
import com.android.internal.util.ArrayUtils;
import android.util.Log;
import android.text.style.LeadingMarginSpan;
import android.text.style.LineHeightSpan;
import android.text.style.MetricAffectingSpan;
import android.text.style.ReplacementSpan;
>>>>>>> 54b6cfa... Initial Contribution

/**
 * StaticLayout is a Layout for text that will not be edited after it
 * is laid out.  Use {@link DynamicLayout} for text that may change.
 * <p>This is used by widgets to control text layout. You should not need
 * to use this class directly unless you are implementing your own widget
 * or custom display object, or would be tempted to call
<<<<<<< HEAD
 * {@link android.graphics.Canvas#drawText(java.lang.CharSequence, int, int,
 * float, float, android.graphics.Paint)
 * Canvas.drawText()} directly.</p>
 */
public class StaticLayout extends Layout {

    static final String TAG = "StaticLayout";

=======
 * {@link android.graphics.Canvas#drawText(java.lang.CharSequence, int, int, float, float, android.graphics.Paint)
 *  Canvas.drawText()} directly.</p>
 */
public class
StaticLayout
extends Layout
{
>>>>>>> 54b6cfa... Initial Contribution
    public StaticLayout(CharSequence source, TextPaint paint,
                        int width,
                        Alignment align, float spacingmult, float spacingadd,
                        boolean includepad) {
        this(source, 0, source.length(), paint, width, align,
             spacingmult, spacingadd, includepad);
    }

<<<<<<< HEAD
    /**
     * @hide
     */
    public StaticLayout(CharSequence source, TextPaint paint,
            int width, Alignment align, TextDirectionHeuristic textDir,
            float spacingmult, float spacingadd,
            boolean includepad) {
        this(source, 0, source.length(), paint, width, align, textDir,
                spacingmult, spacingadd, includepad);
    }

=======
>>>>>>> 54b6cfa... Initial Contribution
    public StaticLayout(CharSequence source, int bufstart, int bufend,
                        TextPaint paint, int outerwidth,
                        Alignment align,
                        float spacingmult, float spacingadd,
                        boolean includepad) {
        this(source, bufstart, bufend, paint, outerwidth, align,
             spacingmult, spacingadd, includepad, null, 0);
    }

<<<<<<< HEAD
    /**
     * @hide
     */
    public StaticLayout(CharSequence source, int bufstart, int bufend,
            TextPaint paint, int outerwidth,
            Alignment align, TextDirectionHeuristic textDir,
            float spacingmult, float spacingadd,
            boolean includepad) {
        this(source, bufstart, bufend, paint, outerwidth, align, textDir,
                spacingmult, spacingadd, includepad, null, 0, Integer.MAX_VALUE);
}

    public StaticLayout(CharSequence source, int bufstart, int bufend,
            TextPaint paint, int outerwidth,
            Alignment align,
            float spacingmult, float spacingadd,
            boolean includepad,
            TextUtils.TruncateAt ellipsize, int ellipsizedWidth) {
        this(source, bufstart, bufend, paint, outerwidth, align,
                TextDirectionHeuristics.FIRSTSTRONG_LTR,
                spacingmult, spacingadd, includepad, ellipsize, ellipsizedWidth, Integer.MAX_VALUE);
    }

    /**
     * @hide
     */
    public StaticLayout(CharSequence source, int bufstart, int bufend,
                        TextPaint paint, int outerwidth,
                        Alignment align, TextDirectionHeuristic textDir,
                        float spacingmult, float spacingadd,
                        boolean includepad,
                        TextUtils.TruncateAt ellipsize, int ellipsizedWidth, int maxLines) {
        super((ellipsize == null)
                ? source
                : (source instanceof Spanned)
                    ? new SpannedEllipsizer(source)
                    : new Ellipsizer(source),
              paint, outerwidth, align, textDir, spacingmult, spacingadd);
=======
    public StaticLayout(CharSequence source, int bufstart, int bufend,
                        TextPaint paint, int outerwidth,
                        Alignment align,
                        float spacingmult, float spacingadd,
                        boolean includepad,
                        TextUtils.TruncateAt ellipsize, int ellipsizedWidth) {
        super((ellipsize == null)
                ? source 
                : (source instanceof Spanned)
                    ? new SpannedEllipsizer(source)
                    : new Ellipsizer(source),
              paint, outerwidth, align, spacingmult, spacingadd);
>>>>>>> 54b6cfa... Initial Contribution

        /*
         * This is annoying, but we can't refer to the layout until
         * superclass construction is finished, and the superclass
         * constructor wants the reference to the display text.
<<<<<<< HEAD
         *
=======
         * 
>>>>>>> 54b6cfa... Initial Contribution
         * This will break if the superclass constructor ever actually
         * cares about the content instead of just holding the reference.
         */
        if (ellipsize != null) {
            Ellipsizer e = (Ellipsizer) getText();

            e.mLayout = this;
            e.mWidth = ellipsizedWidth;
            e.mMethod = ellipsize;
            mEllipsizedWidth = ellipsizedWidth;

            mColumns = COLUMNS_ELLIPSIZE;
        } else {
            mColumns = COLUMNS_NORMAL;
            mEllipsizedWidth = outerwidth;
        }

        mLines = new int[ArrayUtils.idealIntArraySize(2 * mColumns)];
        mLineDirections = new Directions[
                             ArrayUtils.idealIntArraySize(2 * mColumns)];
<<<<<<< HEAD
        mMaximumVisibleLineCount = maxLines;

        mMeasured = MeasuredText.obtain();

        generate(source, bufstart, bufend, paint, outerwidth, textDir, spacingmult,
                 spacingadd, includepad, includepad, ellipsizedWidth,
                 ellipsize);

        mMeasured = MeasuredText.recycle(mMeasured);
        mFontMetricsInt = null;
    }

    /* package */ StaticLayout(CharSequence text) {
        super(text, null, 0, null, 0, 0);
=======

        generate(source, bufstart, bufend, paint, outerwidth, align,
                 spacingmult, spacingadd, includepad, includepad,
                 ellipsize != null, ellipsizedWidth, ellipsize);

        mChdirs = null;
        mChs = null;
        mWidths = null;
        mFontMetricsInt = null;
    }

    /* package */ StaticLayout(boolean ellipsize) {
        super(null, null, 0, null, 0, 0);
>>>>>>> 54b6cfa... Initial Contribution

        mColumns = COLUMNS_ELLIPSIZE;
        mLines = new int[ArrayUtils.idealIntArraySize(2 * mColumns)];
        mLineDirections = new Directions[
                             ArrayUtils.idealIntArraySize(2 * mColumns)];
<<<<<<< HEAD
        mMeasured = MeasuredText.obtain();
    }

    /* package */ void generate(CharSequence source, int bufStart, int bufEnd,
                        TextPaint paint, int outerWidth,
                        TextDirectionHeuristic textDir, float spacingmult,
                        float spacingadd, boolean includepad,
                        boolean trackpad, float ellipsizedWidth,
                        TextUtils.TruncateAt ellipsize) {
=======
    }

    /* package */ void generate(CharSequence source, int bufstart, int bufend,
                        TextPaint paint, int outerwidth,
                        Alignment align,
                        float spacingmult, float spacingadd,
                        boolean includepad, boolean trackpad,
                        boolean breakOnlyAtSpaces,
                        float ellipsizedWidth, TextUtils.TruncateAt where) {
>>>>>>> 54b6cfa... Initial Contribution
        mLineCount = 0;

        int v = 0;
        boolean needMultiply = (spacingmult != 1 || spacingadd != 0);

        Paint.FontMetricsInt fm = mFontMetricsInt;
<<<<<<< HEAD
        int[] chooseHtv = null;

        MeasuredText measured = mMeasured;

        Spanned spanned = null;
=======
        int[] choosehtv = null;

        int end = TextUtils.indexOf(source, '\n', bufstart, bufend);
        int bufsiz = end >= 0 ? end - bufstart : bufend - bufstart;
        boolean first = true;

        if (mChdirs == null) {
            mChdirs = new byte[ArrayUtils.idealByteArraySize(bufsiz + 1)];
            mChs = new char[ArrayUtils.idealCharArraySize(bufsiz + 1)];
            mWidths = new float[ArrayUtils.idealIntArraySize((bufsiz + 1) * 2)];
        }

        byte[] chdirs = mChdirs;
        char[] chs = mChs;
        float[] widths = mWidths;

        AlteredCharSequence alter = null;
        Spanned spanned = null;

>>>>>>> 54b6cfa... Initial Contribution
        if (source instanceof Spanned)
            spanned = (Spanned) source;

        int DEFAULT_DIR = DIR_LEFT_TO_RIGHT; // XXX

<<<<<<< HEAD
        int paraEnd;
        for (int paraStart = bufStart; paraStart <= bufEnd; paraStart = paraEnd) {
            paraEnd = TextUtils.indexOf(source, CHAR_NEW_LINE, paraStart, bufEnd);
            if (paraEnd < 0)
                paraEnd = bufEnd;
            else
                paraEnd++;

            int firstWidthLineLimit = mLineCount + 1;
            int firstWidth = outerWidth;
            int restWidth = outerWidth;

            LineHeightSpan[] chooseHt = null;

            if (spanned != null) {
                LeadingMarginSpan[] sp = getParagraphSpans(spanned, paraStart, paraEnd,
                        LeadingMarginSpan.class);
                for (int i = 0; i < sp.length; i++) {
                    LeadingMarginSpan lms = sp[i];
                    firstWidth -= sp[i].getLeadingMargin(true);
                    restWidth -= sp[i].getLeadingMargin(false);

                    // LeadingMarginSpan2 is odd.  The count affects all
                    // leading margin spans, not just this particular one,
                    // and start from the top of the span, not the top of the
                    // paragraph.
                    if (lms instanceof LeadingMarginSpan2) {
                        LeadingMarginSpan2 lms2 = (LeadingMarginSpan2) lms;
                        int lmsFirstLine = getLineForOffset(spanned.getSpanStart(lms2));
                        firstWidthLineLimit = lmsFirstLine + lms2.getLeadingMarginLineCount();
                    }
                }

                chooseHt = getParagraphSpans(spanned, paraStart, paraEnd, LineHeightSpan.class);

                if (chooseHt.length != 0) {
                    if (chooseHtv == null ||
                        chooseHtv.length < chooseHt.length) {
                        chooseHtv = new int[ArrayUtils.idealIntArraySize(
                                            chooseHt.length)];
                    }

                    for (int i = 0; i < chooseHt.length; i++) {
                        int o = spanned.getSpanStart(chooseHt[i]);

                        if (o < paraStart) {
                            // starts in this layout, before the
                            // current paragraph

                            chooseHtv[i] = getLineTop(getLineForOffset(o));
                        } else {
                            // starts in this paragraph

                            chooseHtv[i] = v;
=======
        for (int start = bufstart; start <= bufend; start = end) {
            if (first)
                first = false;
            else
                end = TextUtils.indexOf(source, '\n', start, bufend);

            if (end < 0)
                end = bufend;
            else
                end++;

            int firstwidth = outerwidth;
            int restwidth = outerwidth;

            LineHeightSpan[] chooseht = null;

            if (spanned != null) {
                LeadingMarginSpan[] sp;

                sp = spanned.getSpans(start, end, LeadingMarginSpan.class);
                for (int i = 0; i < sp.length; i++) {
                    firstwidth -= sp[i].getLeadingMargin(true);
                    restwidth -= sp[i].getLeadingMargin(false);
                }

                chooseht = spanned.getSpans(start, end, LineHeightSpan.class);

                if (chooseht.length != 0) {
                    if (choosehtv == null ||
                        choosehtv.length < chooseht.length) {
                        choosehtv = new int[ArrayUtils.idealIntArraySize(
                                            chooseht.length)];
                    }

                    for (int i = 0; i < chooseht.length; i++) {
                        int o = spanned.getSpanStart(chooseht[i]);

                        if (o < start) {
                            // starts in this layout, before the
                            // current paragraph

                            choosehtv[i] = getLineTop(getLineForOffset(o)); 
                        } else {
                            // starts in this paragraph

                            choosehtv[i] = v;
>>>>>>> 54b6cfa... Initial Contribution
                        }
                    }
                }
            }

<<<<<<< HEAD
            measured.setPara(source, paraStart, paraEnd, textDir);
            char[] chs = measured.mChars;
            float[] widths = measured.mWidths;
            byte[] chdirs = measured.mLevels;
            int dir = measured.mDir;
            boolean easy = measured.mEasy;

            int width = firstWidth;

            float w = 0;
            // here is the offset of the starting character of the line we are currently measuring
            int here = paraStart;

            // ok is a character offset located after a word separator (space, tab, number...) where
            // we would prefer to cut the current line. Equals to here when no such break was found.
            int ok = paraStart;
            float okWidth = w;
            int okAscent = 0, okDescent = 0, okTop = 0, okBottom = 0;

            // fit is a character offset such that the [here, fit[ range fits in the allowed width.
            // We will cut the line there if no ok position is found.
            int fit = paraStart;
            float fitWidth = w;
            int fitAscent = 0, fitDescent = 0, fitTop = 0, fitBottom = 0;

            boolean hasTabOrEmoji = false;
            boolean hasTab = false;
            TabStops tabStops = null;

            for (int spanStart = paraStart, spanEnd; spanStart < paraEnd; spanStart = spanEnd) {

                if (spanned == null) {
                    spanEnd = paraEnd;
                    int spanLen = spanEnd - spanStart;
                    measured.addStyleRun(paint, spanLen, fm);
                } else {
                    spanEnd = spanned.nextSpanTransition(spanStart, paraEnd,
                            MetricAffectingSpan.class);
                    int spanLen = spanEnd - spanStart;
                    MetricAffectingSpan[] spans =
                            spanned.getSpans(spanStart, spanEnd, MetricAffectingSpan.class);
                    spans = TextUtils.removeEmptySpans(spans, spanned, MetricAffectingSpan.class);
                    measured.addStyleRun(paint, spans, spanLen, fm);
                }

                int fmTop = fm.top;
                int fmBottom = fm.bottom;
                int fmAscent = fm.ascent;
                int fmDescent = fm.descent;

                for (int j = spanStart; j < spanEnd; j++) {
                    char c = chs[j - paraStart];

                    if (c == CHAR_NEW_LINE) {
                        // intentionally left empty
                    } else if (c == CHAR_TAB) {
                        if (hasTab == false) {
                            hasTab = true;
                            hasTabOrEmoji = true;
                            if (spanned != null) {
                                // First tab this para, check for tabstops
                                TabStopSpan[] spans = getParagraphSpans(spanned, paraStart,
                                        paraEnd, TabStopSpan.class);
                                if (spans.length > 0) {
                                    tabStops = new TabStops(TAB_INCREMENT, spans);
                                }
                            }
                        }
                        if (tabStops != null) {
                            w = tabStops.nextTab(w);
                        } else {
                            w = TabStops.nextDefaultStop(w, TAB_INCREMENT);
                        }
                    } else if (c >= CHAR_FIRST_HIGH_SURROGATE && c <= CHAR_LAST_LOW_SURROGATE
                            && j + 1 < spanEnd) {
                        int emoji = Character.codePointAt(chs, j - paraStart);

                        if (emoji >= MIN_EMOJI && emoji <= MAX_EMOJI) {
                            Bitmap bm = EMOJI_FACTORY.getBitmapFromAndroidPua(emoji);

                            if (bm != null) {
                                Paint whichPaint;

                                if (spanned == null) {
                                    whichPaint = paint;
                                } else {
                                    whichPaint = mWorkPaint;
                                }

                                float wid = bm.getWidth() * -whichPaint.ascent() / bm.getHeight();

                                w += wid;
                                hasTabOrEmoji = true;
                                j++;
                            } else {
                                w += widths[j - paraStart];
                            }
                        } else {
                            w += widths[j - paraStart];
                        }
                    } else {
                        w += widths[j - paraStart];
                    }

                    if (w <= width) {
                        fitWidth = w;
                        fit = j + 1;

                        if (fmTop < fitTop)
                            fitTop = fmTop;
                        if (fmAscent < fitAscent)
                            fitAscent = fmAscent;
                        if (fmDescent > fitDescent)
                            fitDescent = fmDescent;
                        if (fmBottom > fitBottom)
                            fitBottom = fmBottom;

                        /*
                         * From the Unicode Line Breaking Algorithm:
                         * (at least approximately)
                         *
                         * .,:; are class IS: breakpoints
                         *      except when adjacent to digits
                         * /    is class SY: a breakpoint
                         *      except when followed by a digit.
                         * -    is class HY: a breakpoint
                         *      except when followed by a digit.
                         *
                         * Ideographs are class ID: breakpoints when adjacent,
                         * except for NS (non-starters), which can be broken
                         * after but not before.
                         */
                        if (c == CHAR_SPACE || c == CHAR_TAB ||
                            ((c == CHAR_DOT || c == CHAR_COMMA ||
                                    c == CHAR_COLON || c == CHAR_SEMICOLON) &&
                             (j - 1 < here || !Character.isDigit(chs[j - 1 - paraStart])) &&
                             (j + 1 >= spanEnd || !Character.isDigit(chs[j + 1 - paraStart]))) ||
                            ((c == CHAR_SLASH || c == CHAR_HYPHEN) &&
                             (j + 1 >= spanEnd || !Character.isDigit(chs[j + 1 - paraStart]))) ||
                            (c >= CHAR_FIRST_CJK && isIdeographic(c, true) &&
                             j + 1 < spanEnd && isIdeographic(chs[j + 1 - paraStart], false))) {
                            okWidth = w;
                            ok = j + 1;

                            if (fitTop < okTop)
                                okTop = fitTop;
                            if (fitAscent < okAscent)
                                okAscent = fitAscent;
                            if (fitDescent > okDescent)
                                okDescent = fitDescent;
                            if (fitBottom > okBottom)
                                okBottom = fitBottom;
                        }
                    } else {
                        final boolean moreChars = (j + 1 < spanEnd);
                        int endPos;
                        int above, below, top, bottom;
                        float currentTextWidth;

                        if (ok != here) {
                            // If it is a space that makes the length exceed width, cut here
                            if (c == CHAR_SPACE) ok = j + 1;

                            while (ok < spanEnd && chs[ok - paraStart] == CHAR_SPACE) {
                                ok++;
                            }

                            endPos = ok;
                            above = okAscent;
                            below = okDescent;
                            top = okTop;
                            bottom = okBottom;
                            currentTextWidth = okWidth;
                        } else if (fit != here) {
                            endPos = fit;
                            above = fitAscent;
                            below = fitDescent;
                            top = fitTop;
                            bottom = fitBottom;
                            currentTextWidth = fitWidth;
                        } else {
                            endPos = here + 1;
                            above = fm.ascent;
                            below = fm.descent;
                            top = fm.top;
                            bottom = fm.bottom;
                            currentTextWidth = widths[here - paraStart];
                        }

                        v = out(source, here, endPos,
                                above, below, top, bottom,
                                v, spacingmult, spacingadd, chooseHt,chooseHtv, fm, hasTabOrEmoji,
                                needMultiply, chdirs, dir, easy, bufEnd, includepad, trackpad,
                                chs, widths, paraStart, ellipsize, ellipsizedWidth,
                                currentTextWidth, paint, moreChars);

                        here = endPos;
                        j = here - 1; // restart j-span loop from here, compensating for the j++
                        ok = fit = here;
                        w = 0;
                        fitAscent = fitDescent = fitTop = fitBottom = 0;
                        okAscent = okDescent = okTop = okBottom = 0;

                        if (--firstWidthLineLimit <= 0) {
                            width = restWidth;
                        }

                        if (here < spanStart) {
                            // The text was cut before the beginning of the current span range.
                            // Exit the span loop, and get spanStart to start over from here.
                            measured.setPos(here);
                            spanEnd = here;
                            break;
                        }
                    }
                    // FIXME This should be moved in the above else block which changes mLineCount
                    if (mLineCount >= mMaximumVisibleLineCount) {
                        break;
=======
            if (end - start > chdirs.length) {
                chdirs = new byte[ArrayUtils.idealByteArraySize(end - start)];
                mChdirs = chdirs;
            }
            if (end - start > chs.length) {
                chs = new char[ArrayUtils.idealCharArraySize(end - start)];
                mChs = chs;
            }
            if ((end - start) * 2 > widths.length) {
                widths = new float[ArrayUtils.idealIntArraySize((end - start) * 2)];
                mWidths = widths;
            }

            TextUtils.getChars(source, start, end, chs, 0);
            final int n = end - start;

            boolean easy = true;
            boolean altered = false;
            int dir = DEFAULT_DIR; // XXX

            for (int i = 0; i < n; i++) {
                if (chs[i] >= FIRST_RIGHT_TO_LEFT) {
                    easy = false;
                    break;
                }
            }

            if (!easy) {
                AndroidCharacter.getDirectionalities(chs, chdirs, end - start);

                /*
                 * Determine primary paragraph direction
                 */

                for (int j = start; j < end; j++) {
                    int d = chdirs[j - start];

                    if (d == Character.DIRECTIONALITY_LEFT_TO_RIGHT) {
                        dir = DIR_LEFT_TO_RIGHT;
                        break;
                    }
                    if (d == Character.DIRECTIONALITY_RIGHT_TO_LEFT) {
                        dir = DIR_RIGHT_TO_LEFT;
                        break;
                    }
                }

                /*
                 * XXX Explicit overrides should go here
                 */

                /*
                 * Weak type resolution
                 */

                final byte SOR = dir == DIR_LEFT_TO_RIGHT ?
                                    Character.DIRECTIONALITY_LEFT_TO_RIGHT :
                                    Character.DIRECTIONALITY_RIGHT_TO_LEFT;

                // dump(chdirs, n, "initial");

                // W1 non spacing marks
                for (int j = 0; j < n; j++) {
                    if (chdirs[j] == Character.NON_SPACING_MARK) {
                        if (j == 0)
                            chdirs[j] = SOR;
                        else
                            chdirs[j] = chdirs[j - 1];
                    }
                }

                // dump(chdirs, n, "W1");

                // W2 european numbers
                byte cur = SOR;
                for (int j = 0; j < n; j++) {
                    byte d = chdirs[j];

                    if (d == Character.DIRECTIONALITY_LEFT_TO_RIGHT ||
                        d == Character.DIRECTIONALITY_RIGHT_TO_LEFT ||
                        d == Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC)
                        cur = d;
                    else if (d == Character.DIRECTIONALITY_EUROPEAN_NUMBER) {
                         if (cur ==
                            Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC)
                            chdirs[j] = Character.DIRECTIONALITY_ARABIC_NUMBER;
                    }
                }

                // dump(chdirs, n, "W2");

                // W3 arabic letters
                for (int j = 0; j < n; j++) {
                    if (chdirs[j] == Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC)
                        chdirs[j] = Character.DIRECTIONALITY_RIGHT_TO_LEFT;
                }

                // dump(chdirs, n, "W3");

                // W4 single separator between numbers
                for (int j = 1; j < n - 1; j++) {
                    byte d = chdirs[j];
                    byte prev = chdirs[j - 1];
                    byte next = chdirs[j + 1];

                    if (d == Character.DIRECTIONALITY_EUROPEAN_NUMBER_SEPARATOR) {
                        if (prev == Character.DIRECTIONALITY_EUROPEAN_NUMBER &&
                            next == Character.DIRECTIONALITY_EUROPEAN_NUMBER)
                            chdirs[j] = Character.DIRECTIONALITY_EUROPEAN_NUMBER;
                    } else if (d == Character.DIRECTIONALITY_COMMON_NUMBER_SEPARATOR) {
                        if (prev == Character.DIRECTIONALITY_EUROPEAN_NUMBER &&
                            next == Character.DIRECTIONALITY_EUROPEAN_NUMBER)
                            chdirs[j] = Character.DIRECTIONALITY_EUROPEAN_NUMBER;
                        if (prev == Character.DIRECTIONALITY_ARABIC_NUMBER &&
                            next == Character.DIRECTIONALITY_ARABIC_NUMBER)
                            chdirs[j] = Character.DIRECTIONALITY_ARABIC_NUMBER;
                    }
                }

                // dump(chdirs, n, "W4");

                // W5 european number terminators
                boolean adjacent = false;
                for (int j = 0; j < n; j++) {
                    byte d = chdirs[j];

                    if (d == Character.DIRECTIONALITY_EUROPEAN_NUMBER)
                        adjacent = true;
                    else if (d == Character.DIRECTIONALITY_EUROPEAN_NUMBER_TERMINATOR && adjacent)
                        chdirs[j] = Character.DIRECTIONALITY_EUROPEAN_NUMBER;
                    else
                        adjacent = false;
                }

                //dump(chdirs, n, "W5");

                // W5 european number terminators part 2,
                // W6 separators and terminators
                adjacent = false;
                for (int j = n - 1; j >= 0; j--) {
                    byte d = chdirs[j];

                    if (d == Character.DIRECTIONALITY_EUROPEAN_NUMBER)
                        adjacent = true;
                    else if (d == Character.DIRECTIONALITY_EUROPEAN_NUMBER_TERMINATOR) {
                        if (adjacent)
                            chdirs[j] = Character.DIRECTIONALITY_EUROPEAN_NUMBER;
                        else
                            chdirs[j] = Character.DIRECTIONALITY_OTHER_NEUTRALS;
                    }
                    else {
                        adjacent = false;

                        if (d == Character.DIRECTIONALITY_EUROPEAN_NUMBER_SEPARATOR ||
                            d == Character.DIRECTIONALITY_COMMON_NUMBER_SEPARATOR ||
                            d == Character.DIRECTIONALITY_PARAGRAPH_SEPARATOR ||
                            d == Character.DIRECTIONALITY_SEGMENT_SEPARATOR)
                            chdirs[j] = Character.DIRECTIONALITY_OTHER_NEUTRALS;
                    }
                }

                // dump(chdirs, n, "W6");

                // W7 strong direction of european numbers
                cur = SOR;
                for (int j = 0; j < n; j++) {
                    byte d = chdirs[j];

                    if (d == SOR ||
                        d == Character.DIRECTIONALITY_LEFT_TO_RIGHT ||
                        d == Character.DIRECTIONALITY_RIGHT_TO_LEFT)
                        cur = d;

                    if (d == Character.DIRECTIONALITY_EUROPEAN_NUMBER)
                        chdirs[j] = cur;
                }

                // dump(chdirs, n, "W7");

                // N1, N2 neutrals
                cur = SOR;
                for (int j = 0; j < n; j++) {
                    byte d = chdirs[j];

                    if (d == Character.DIRECTIONALITY_LEFT_TO_RIGHT ||
                        d == Character.DIRECTIONALITY_RIGHT_TO_LEFT) {
                        cur = d;
                    } else if (d == Character.DIRECTIONALITY_EUROPEAN_NUMBER ||
                               d == Character.DIRECTIONALITY_ARABIC_NUMBER) {
                        cur = Character.DIRECTIONALITY_RIGHT_TO_LEFT;
                    } else {
                        byte dd = SOR;
                        int k;

                        for (k = j + 1; k < n; k++) {
                            dd = chdirs[k];

                            if (dd == Character.DIRECTIONALITY_LEFT_TO_RIGHT ||
                                dd == Character.DIRECTIONALITY_RIGHT_TO_LEFT) {
                                break;
                            }
                            if (dd == Character.DIRECTIONALITY_EUROPEAN_NUMBER ||
                                dd == Character.DIRECTIONALITY_ARABIC_NUMBER) {
                                dd = Character.DIRECTIONALITY_RIGHT_TO_LEFT;
                                break;
                            }
                        }

                        for (int y = j; y < k; y++) {
                            if (dd == cur)
                                chdirs[y] = cur;
                            else
                                chdirs[y] = SOR;
                        }

                        j = k - 1;
                    }
                }

                // dump(chdirs, n, "final");

                // extra: enforce that all tabs go the primary direction

                for (int j = 0; j < n; j++) {
                    if (chs[j] == '\t')
                        chdirs[j] = SOR;
                }

                // extra: enforce that object replacements go to the
                // primary direction
                // and that none of the underlying characters are treated
                // as viable breakpoints

                if (source instanceof Spanned) {
                    Spanned sp = (Spanned) source;
                    ReplacementSpan[] spans = sp.getSpans(start, end, ReplacementSpan.class);

                    for (int y = 0; y < spans.length; y++) {
                        int a = sp.getSpanStart(spans[y]);
                        int b = sp.getSpanEnd(spans[y]);

                        for (int x = a; x < b; x++) {
                            chdirs[x - start] = SOR;
                            chs[x - start] = '\uFFFC';
                        }
                    }
                }

                // Do mirroring for right-to-left segments

                for (int i = 0; i < n; i++) {
                    if (chdirs[i] == Character.DIRECTIONALITY_RIGHT_TO_LEFT) {
                        int j;

                        for (j = i; j < n; j++) {
                            if (chdirs[j] !=
                                Character.DIRECTIONALITY_RIGHT_TO_LEFT)
                                break;
                        }

                        if (AndroidCharacter.mirror(chs, i, j - i))
                            altered = true;

                        i = j - 1;
                    }
                }
            }

            CharSequence sub;

            if (altered) {
                if (alter == null)
                    alter = AlteredCharSequence.make(source, chs, start, end);
                else
                    alter.update(chs, start, end);

                sub = alter;
            } else {
                sub = source;
            }

            int width = firstwidth;

            float w = 0;
            int here = start;

            int ok = start;
            float okwidth = w;
            int okascent = 0, okdescent = 0, oktop = 0, okbottom = 0;

            int fit = start;
            float fitwidth = w;
            int fitascent = 0, fitdescent = 0, fittop = 0, fitbottom = 0;

            boolean tab = false;

            int next;
            for (int i = start; i < end; i = next) {
                if (spanned == null)
                    next = end;
                else
                    next = spanned.nextSpanTransition(i, end,
                                                      MetricAffectingSpan.
                                                      class);

                if (spanned == null) {
                    paint.getTextWidths(sub, i, next, widths);
                    System.arraycopy(widths, 0, widths,
                                     end - start + (i - start), next - i);
                                     
                    paint.getFontMetricsInt(fm);
                } else {
                    mWorkPaint.baselineShift = 0;

                    Styled.getTextWidths(paint, mWorkPaint,
                                         spanned, i, next,
                                         widths, fm);
                    System.arraycopy(widths, 0, widths,
                                     end - start + (i - start), next - i);

                    if (mWorkPaint.baselineShift < 0) {
                        fm.ascent += mWorkPaint.baselineShift;
                        fm.top += mWorkPaint.baselineShift;
                    } else {
                        fm.descent += mWorkPaint.baselineShift;
                        fm.bottom += mWorkPaint.baselineShift;
                    }
                }

                int fmtop = fm.top;
                int fmbottom = fm.bottom;
                int fmascent = fm.ascent;
                int fmdescent = fm.descent;

                if (false) {
                    StringBuilder sb = new StringBuilder();
                    for (int j = i; j < next; j++) {
                        sb.append(widths[j - start + (end - start)]);
                        sb.append(' ');
                    }

                    Log.e("text", sb.toString());
                }

                for (int j = i; j < next; j++) {
                    char c = chs[j - start];
                    float before = w;

                    switch (c) {
                    case '\n':
                        break;

                    case '\t':
                        w = Layout.nextTab(sub, start, end, w, null);
                        tab = true;
                        break;

                    default:
                        w += widths[j - start + (end - start)];
                    }

                    // Log.e("text", "was " + before + " now " + w + " after " + c + " within " + width);

                    if (w <= width) {
                        fitwidth = w;
                        fit = j + 1;

                        if (fmtop < fittop)
                            fittop = fmtop;
                        if (fmascent < fitascent)
                            fitascent = fmascent;
                        if (fmdescent > fitdescent)
                            fitdescent = fmdescent;
                        if (fmbottom > fitbottom)
                            fitbottom = fmbottom;

                        if (c == ' ' || c == '\t') {
                            okwidth = w;
                            ok = j + 1;

                            if (fittop < oktop)
                                oktop = fittop;
                            if (fitascent < okascent)
                                okascent = fitascent;
                            if (fitdescent > okdescent)
                                okdescent = fitdescent;
                            if (fitbottom > okbottom)
                                okbottom = fitbottom;
                        }
                    } else if (breakOnlyAtSpaces) {
                        if (ok != here) {
                            // Log.e("text", "output ok " + here + " to " +ok);
                            v = out(source,
                                    here, ok,
                                    okascent, okdescent, oktop, okbottom,
                                    v,
                                    spacingmult, spacingadd, chooseht,
                                    choosehtv, fm, tab,
                                    needMultiply, start, chdirs, dir, easy,
                                    ok == bufend, includepad, trackpad,
                                    widths, start, end - start,
                                    where, ellipsizedWidth, okwidth,
                                    paint);

                            here = ok;
                        } else {
                            // Act like it fit even though it didn't.

                            fitwidth = w;
                            fit = j + 1;

                            if (fmtop < fittop)
                                fittop = fmtop;
                            if (fmascent < fitascent)
                                fitascent = fmascent;
                            if (fmdescent > fitdescent)
                                fitdescent = fmdescent;
                            if (fmbottom > fitbottom)
                                fitbottom = fmbottom;
                        }
                    } else {
                        if (ok != here) {
                            // Log.e("text", "output ok " + here + " to " +ok);
                            v = out(source,
                                    here, ok,
                                    okascent, okdescent, oktop, okbottom,
                                    v,
                                    spacingmult, spacingadd, chooseht,
                                    choosehtv, fm, tab,
                                    needMultiply, start, chdirs, dir, easy,
                                    ok == bufend, includepad, trackpad,
                                    widths, start, end - start,
                                    where, ellipsizedWidth, okwidth,
                                    paint);

                            here = ok;
                        } else if (fit != here) {
                            // Log.e("text", "output fit " + here + " to " +fit);
                            v = out(source,
                                    here, fit,
                                    fitascent, fitdescent,
                                    fittop, fitbottom,
                                    v,
                                    spacingmult, spacingadd, chooseht,
                                    choosehtv, fm, tab,
                                    needMultiply, start, chdirs, dir, easy,
                                    fit == bufend, includepad, trackpad,
                                    widths, start, end - start,
                                    where, ellipsizedWidth, fitwidth,
                                    paint);

                            here = fit;
                        } else {
                            // Log.e("text", "output one " + here + " to " +(here + 1));
                            measureText(paint, mWorkPaint,
                                        source, here, here + 1, fm, tab,
                                        null);

                            v = out(source,
                                    here, here+1,
                                    fm.ascent, fm.descent,
                                    fm.top, fm.bottom,
                                    v,
                                    spacingmult, spacingadd, chooseht,
                                    choosehtv, fm, tab,
                                    needMultiply, start, chdirs, dir, easy,
                                    here + 1 == bufend, includepad,
                                    trackpad,
                                    widths, start, end - start,
                                    where, ellipsizedWidth,
                                    widths[here - start], paint);

                            here = here + 1;
                        }

                        if (here < i) {
                            j = next = here; // must remeasure
                        } else {
                            j = here - 1;    // continue looping
                        }

                        ok = fit = here;
                        w = 0;
                        fitascent = fitdescent = fittop = fitbottom = 0;
                        okascent = okdescent = oktop = okbottom = 0;

                        width = restwidth;
>>>>>>> 54b6cfa... Initial Contribution
                    }
                }
            }

<<<<<<< HEAD
            if (paraEnd != here && mLineCount < mMaximumVisibleLineCount) {
                if ((fitTop | fitBottom | fitDescent | fitAscent) == 0) {
                    paint.getFontMetricsInt(fm);

                    fitTop = fm.top;
                    fitBottom = fm.bottom;
                    fitAscent = fm.ascent;
                    fitDescent = fm.descent;
=======
            if (end != here) {
                if ((fittop | fitbottom | fitdescent | fitascent) == 0) {
                    paint.getFontMetricsInt(fm);

                    fittop = fm.top;
                    fitbottom = fm.bottom;
                    fitascent = fm.ascent;
                    fitdescent = fm.descent;
>>>>>>> 54b6cfa... Initial Contribution
                }

                // Log.e("text", "output rest " + here + " to " + end);

                v = out(source,
<<<<<<< HEAD
                        here, paraEnd, fitAscent, fitDescent,
                        fitTop, fitBottom,
                        v,
                        spacingmult, spacingadd, chooseHt,
                        chooseHtv, fm, hasTabOrEmoji,
                        needMultiply, chdirs, dir, easy, bufEnd,
                        includepad, trackpad, chs,
                        widths, paraStart, ellipsize,
                        ellipsizedWidth, w, paint, paraEnd != bufEnd);
            }

            paraStart = paraEnd;

            if (paraEnd == bufEnd)
                break;
        }

        if ((bufEnd == bufStart || source.charAt(bufEnd - 1) == CHAR_NEW_LINE) &&
                mLineCount < mMaximumVisibleLineCount) {
            // Log.e("text", "output last " + bufEnd);
=======
                        here, end, fitascent, fitdescent,
                        fittop, fitbottom,
                        v,
                        spacingmult, spacingadd, chooseht,
                        choosehtv, fm, tab,
                        needMultiply, start, chdirs, dir, easy,
                        end == bufend, includepad, trackpad,
                        widths, start, end - start,
                        where, ellipsizedWidth, w, paint);
            }

            start = end;

            if (end == bufend)
                break;
        }

        if (bufend == bufstart || source.charAt(bufend - 1) == '\n') {
            // Log.e("text", "output last " + bufend);
>>>>>>> 54b6cfa... Initial Contribution

            paint.getFontMetricsInt(fm);

            v = out(source,
<<<<<<< HEAD
                    bufEnd, bufEnd, fm.ascent, fm.descent,
=======
                    bufend, bufend, fm.ascent, fm.descent,
>>>>>>> 54b6cfa... Initial Contribution
                    fm.top, fm.bottom,
                    v,
                    spacingmult, spacingadd, null,
                    null, fm, false,
<<<<<<< HEAD
                    needMultiply, null, DEFAULT_DIR, true, bufEnd,
                    includepad, trackpad, null,
                    null, bufStart, ellipsize,
                    ellipsizedWidth, 0, paint, false);
        }
    }

    /**
     * Returns true if the specified character is one of those specified
     * as being Ideographic (class ID) by the Unicode Line Breaking Algorithm
     * (http://www.unicode.org/unicode/reports/tr14/), and is therefore OK
     * to break between a pair of.
     *
     * @param includeNonStarters also return true for category NS
     *                           (non-starters), which can be broken
     *                           after but not before.
     */
    private static final boolean isIdeographic(char c, boolean includeNonStarters) {
        if (c >= '\u2E80' && c <= '\u2FFF') {
            return true; // CJK, KANGXI RADICALS, DESCRIPTION SYMBOLS
        }
        if (c == '\u3000') {
            return true; // IDEOGRAPHIC SPACE
        }
        if (c >= '\u3040' && c <= '\u309F') {
            if (!includeNonStarters) {
                switch (c) {
                case '\u3041': //  # HIRAGANA LETTER SMALL A
                case '\u3043': //  # HIRAGANA LETTER SMALL I
                case '\u3045': //  # HIRAGANA LETTER SMALL U
                case '\u3047': //  # HIRAGANA LETTER SMALL E
                case '\u3049': //  # HIRAGANA LETTER SMALL O
                case '\u3063': //  # HIRAGANA LETTER SMALL TU
                case '\u3083': //  # HIRAGANA LETTER SMALL YA
                case '\u3085': //  # HIRAGANA LETTER SMALL YU
                case '\u3087': //  # HIRAGANA LETTER SMALL YO
                case '\u308E': //  # HIRAGANA LETTER SMALL WA
                case '\u3095': //  # HIRAGANA LETTER SMALL KA
                case '\u3096': //  # HIRAGANA LETTER SMALL KE
                case '\u309B': //  # KATAKANA-HIRAGANA VOICED SOUND MARK
                case '\u309C': //  # KATAKANA-HIRAGANA SEMI-VOICED SOUND MARK
                case '\u309D': //  # HIRAGANA ITERATION MARK
                case '\u309E': //  # HIRAGANA VOICED ITERATION MARK
                    return false;
                }
            }
            return true; // Hiragana (except small characters)
        }
        if (c >= '\u30A0' && c <= '\u30FF') {
            if (!includeNonStarters) {
                switch (c) {
                case '\u30A0': //  # KATAKANA-HIRAGANA DOUBLE HYPHEN
                case '\u30A1': //  # KATAKANA LETTER SMALL A
                case '\u30A3': //  # KATAKANA LETTER SMALL I
                case '\u30A5': //  # KATAKANA LETTER SMALL U
                case '\u30A7': //  # KATAKANA LETTER SMALL E
                case '\u30A9': //  # KATAKANA LETTER SMALL O
                case '\u30C3': //  # KATAKANA LETTER SMALL TU
                case '\u30E3': //  # KATAKANA LETTER SMALL YA
                case '\u30E5': //  # KATAKANA LETTER SMALL YU
                case '\u30E7': //  # KATAKANA LETTER SMALL YO
                case '\u30EE': //  # KATAKANA LETTER SMALL WA
                case '\u30F5': //  # KATAKANA LETTER SMALL KA
                case '\u30F6': //  # KATAKANA LETTER SMALL KE
                case '\u30FB': //  # KATAKANA MIDDLE DOT
                case '\u30FC': //  # KATAKANA-HIRAGANA PROLONGED SOUND MARK
                case '\u30FD': //  # KATAKANA ITERATION MARK
                case '\u30FE': //  # KATAKANA VOICED ITERATION MARK
                    return false;
                }
            }
            return true; // Katakana (except small characters)
        }
        if (c >= '\u3400' && c <= '\u4DB5') {
            return true; // CJK UNIFIED IDEOGRAPHS EXTENSION A
        }
        if (c >= '\u4E00' && c <= '\u9FBB') {
            return true; // CJK UNIFIED IDEOGRAPHS
        }
        if (c >= '\uF900' && c <= '\uFAD9') {
            return true; // CJK COMPATIBILITY IDEOGRAPHS
        }
        if (c >= '\uA000' && c <= '\uA48F') {
            return true; // YI SYLLABLES
        }
        if (c >= '\uA490' && c <= '\uA4CF') {
            return true; // YI RADICALS
        }
        if (c >= '\uFE62' && c <= '\uFE66') {
            return true; // SMALL PLUS SIGN to SMALL EQUALS SIGN
        }
        if (c >= '\uFF10' && c <= '\uFF19') {
            return true; // WIDE DIGITS
        }

        return false;
=======
                    needMultiply, bufend, chdirs, DEFAULT_DIR, true,
                    true, includepad, trackpad,
                    widths, bufstart, 0,
                    where, ellipsizedWidth, 0, paint);
        }
    }

/*
    private static void dump(byte[] data, int count, String label) {
        if (false) {
            System.out.print(label);

            for (int i = 0; i < count; i++)
                System.out.print(" " + data[i]);

            System.out.println();
        }
    }
*/

    private static int getFit(TextPaint paint,
                              TextPaint workPaint,
                       CharSequence text, int start, int end,
                       float wid) {
        int high = end + 1, low = start - 1, guess;

        while (high - low > 1) {
            guess = (high + low) / 2;

            if (measureText(paint, workPaint,
                            text, start, guess, null, true, null) > wid)
                high = guess;
            else
                low = guess;
        }

        if (low < start)
            return start;
        else
            return low;
>>>>>>> 54b6cfa... Initial Contribution
    }

    private int out(CharSequence text, int start, int end,
                      int above, int below, int top, int bottom, int v,
                      float spacingmult, float spacingadd,
<<<<<<< HEAD
                      LineHeightSpan[] chooseHt, int[] chooseHtv,
                      Paint.FontMetricsInt fm, boolean hasTabOrEmoji,
                      boolean needMultiply, byte[] chdirs, int dir,
                      boolean easy, int bufEnd, boolean includePad,
                      boolean trackPad, char[] chs,
                      float[] widths, int widthStart, TextUtils.TruncateAt ellipsize,
                      float ellipsisWidth, float textWidth,
                      TextPaint paint, boolean moreChars) {
=======
                      LineHeightSpan[] chooseht, int[] choosehtv,
                      Paint.FontMetricsInt fm, boolean tab,
                      boolean needMultiply, int pstart, byte[] chdirs,
                      int dir, boolean easy, boolean last,
                      boolean includepad, boolean trackpad,
                      float[] widths, int widstart, int widoff,
                      TextUtils.TruncateAt ellipsize, float ellipsiswidth,
                      float textwidth, TextPaint paint) {
>>>>>>> 54b6cfa... Initial Contribution
        int j = mLineCount;
        int off = j * mColumns;
        int want = off + mColumns + TOP;
        int[] lines = mLines;

<<<<<<< HEAD
=======
        // Log.e("text", "line " + start + " to " + end + (last ? "===" : ""));

>>>>>>> 54b6cfa... Initial Contribution
        if (want >= lines.length) {
            int nlen = ArrayUtils.idealIntArraySize(want + 1);
            int[] grow = new int[nlen];
            System.arraycopy(lines, 0, grow, 0, lines.length);
            mLines = grow;
            lines = grow;

            Directions[] grow2 = new Directions[nlen];
            System.arraycopy(mLineDirections, 0, grow2, 0,
                             mLineDirections.length);
            mLineDirections = grow2;
        }

<<<<<<< HEAD
        if (chooseHt != null) {
=======
        if (chooseht != null) {
>>>>>>> 54b6cfa... Initial Contribution
            fm.ascent = above;
            fm.descent = below;
            fm.top = top;
            fm.bottom = bottom;

<<<<<<< HEAD
            for (int i = 0; i < chooseHt.length; i++) {
                if (chooseHt[i] instanceof LineHeightSpan.WithDensity) {
                    ((LineHeightSpan.WithDensity) chooseHt[i]).
                        chooseHeight(text, start, end, chooseHtv[i], v, fm, paint);

                } else {
                    chooseHt[i].chooseHeight(text, start, end, chooseHtv[i], v, fm);
                }
=======
            for (int i = 0; i < chooseht.length; i++) {
                chooseht[i].chooseHeight(text, start, end, choosehtv[i], v, fm);
>>>>>>> 54b6cfa... Initial Contribution
            }

            above = fm.ascent;
            below = fm.descent;
            top = fm.top;
            bottom = fm.bottom;
        }

        if (j == 0) {
<<<<<<< HEAD
            if (trackPad) {
                mTopPadding = top - above;
            }

            if (includePad) {
                above = top;
            }
        }
        if (end == bufEnd) {
            if (trackPad) {
                mBottomPadding = bottom - below;
            }

            if (includePad) {
=======
            if (trackpad) {
                mTopPadding = top - above;
            }

            if (includepad) {
                above = top;
            }
        }
        if (last) {
            if (trackpad) {
                mBottomPadding = bottom - below;
            }

            if (includepad) {
>>>>>>> 54b6cfa... Initial Contribution
                below = bottom;
            }
        }

        int extra;

        if (needMultiply) {
<<<<<<< HEAD
            double ex = (below - above) * (spacingmult - 1) + spacingadd;
            if (ex >= 0) {
                extra = (int)(ex + EXTRA_ROUNDING);
            } else {
                extra = -(int)(-ex + EXTRA_ROUNDING);
            }
=======
            extra = (int) ((below - above) * (spacingmult - 1)
                           + spacingadd + 0.5);
>>>>>>> 54b6cfa... Initial Contribution
        } else {
            extra = 0;
        }

        lines[off + START] = start;
        lines[off + TOP] = v;
        lines[off + DESCENT] = below + extra;

        v += (below - above) + extra;
        lines[off + mColumns + START] = end;
        lines[off + mColumns + TOP] = v;

<<<<<<< HEAD
        if (hasTabOrEmoji)
            lines[off + TAB] |= TAB_MASK;

        lines[off + DIR] |= dir << DIR_SHIFT;
        Directions linedirs = DIRS_ALL_LEFT_TO_RIGHT;
        // easy means all chars < the first RTL, so no emoji, no nothing
        // XXX a run with no text or all spaces is easy but might be an empty
        // RTL paragraph.  Make sure easy is false if this is the case.
        if (easy) {
            mLineDirections[j] = linedirs;
        } else {
            mLineDirections[j] = AndroidBidi.directions(dir, chdirs, start - widthStart, chs,
                    start - widthStart, end - start);
        }

        if (ellipsize != null) {
            // If there is only one line, then do any type of ellipsis except when it is MARQUEE
            // if there are multiple lines, just allow END ellipsis on the last line
            boolean firstLine = (j == 0);
            boolean currentLineIsTheLastVisibleOne = (j + 1 == mMaximumVisibleLineCount);
            boolean forceEllipsis = moreChars && (mLineCount + 1 == mMaximumVisibleLineCount);

            boolean doEllipsis =
                        (((mMaximumVisibleLineCount == 1 && moreChars) || (firstLine && !moreChars)) &&
                                ellipsize != TextUtils.TruncateAt.MARQUEE) ||
                        (!firstLine && (currentLineIsTheLastVisibleOne || !moreChars) &&
                                ellipsize == TextUtils.TruncateAt.END);
            if (doEllipsis) {
                calculateEllipsis(start, end, widths, widthStart,
                        ellipsisWidth, ellipsize, j,
                        textWidth, paint, forceEllipsis);
=======
        if (tab)
            lines[off + TAB] |= TAB_MASK;

        {
            lines[off + DIR] |= dir << DIR_SHIFT;

            int cur = Character.DIRECTIONALITY_LEFT_TO_RIGHT;
            int count = 0;

            if (!easy) {
                for (int k = start; k < end; k++) {
                    if (chdirs[k - pstart] != cur) {
                        count++;
                        cur = chdirs[k - pstart];
                    }
                }
            }

            Directions linedirs;

            if (count == 0) {
                linedirs = DIRS_ALL_LEFT_TO_RIGHT;
            } else {
                short[] ld = new short[count + 1];

                cur = Character.DIRECTIONALITY_LEFT_TO_RIGHT;
                count = 0;
                int here = start;

                for (int k = start; k < end; k++) {
                    if (chdirs[k - pstart] != cur) {
                        // XXX check to make sure we don't
                        //     overflow short
                        ld[count++] = (short) (k - here);
                        cur = chdirs[k - pstart];
                        here = k;
                    }
                }

                ld[count] = (short) (end - here);

                if (count == 1 && ld[0] == 0) {
                    linedirs = DIRS_ALL_RIGHT_TO_LEFT;
                } else {
                    linedirs = new Directions(ld);
                }
            }

            mLineDirections[j] = linedirs;

            if (ellipsize != null) {
                calculateEllipsis(start, end, widths, widstart, widoff,
                                  ellipsiswidth, ellipsize, j,
                                  textwidth, paint);
>>>>>>> 54b6cfa... Initial Contribution
            }
        }

        mLineCount++;
        return v;
    }

<<<<<<< HEAD
    private void calculateEllipsis(int lineStart, int lineEnd,
                                   float[] widths, int widthStart,
                                   float avail, TextUtils.TruncateAt where,
                                   int line, float textWidth, TextPaint paint,
                                   boolean forceEllipsis) {
        if (textWidth <= avail && !forceEllipsis) {
=======
    private void calculateEllipsis(int linestart, int lineend,
                                   float[] widths, int widstart, int widoff,
                                   float avail, TextUtils.TruncateAt where,
                                   int line, float textwidth, TextPaint paint) {
        int len = lineend - linestart;

        if (textwidth <= avail) {
>>>>>>> 54b6cfa... Initial Contribution
            // Everything fits!
            mLines[mColumns * line + ELLIPSIS_START] = 0;
            mLines[mColumns * line + ELLIPSIS_COUNT] = 0;
            return;
        }

<<<<<<< HEAD
        float ellipsisWidth = paint.measureText(
                (where == TextUtils.TruncateAt.END_SMALL) ?
                        ELLIPSIS_TWO_DOTS : ELLIPSIS_NORMAL, 0, 1);
        int ellipsisStart = 0;
        int ellipsisCount = 0;
        int len = lineEnd - lineStart;

        // We only support start ellipsis on a single line
        if (where == TextUtils.TruncateAt.START) {
            if (mMaximumVisibleLineCount == 1) {
                float sum = 0;
                int i;

                for (i = len; i >= 0; i--) {
                    float w = widths[i - 1 + lineStart - widthStart];

                    if (w + sum + ellipsisWidth > avail) {
                        break;
                    }

                    sum += w;
                }

                ellipsisStart = 0;
                ellipsisCount = i;
            } else {
                if (Log.isLoggable(TAG, Log.WARN)) {
                    Log.w(TAG, "Start Ellipsis only supported with one line");
                }
            }
        } else if (where == TextUtils.TruncateAt.END || where == TextUtils.TruncateAt.MARQUEE ||
                where == TextUtils.TruncateAt.END_SMALL) {
=======
        float ellipsiswid = paint.measureText("\u2026");
        int ellipsisStart, ellipsisCount;

        if (where == TextUtils.TruncateAt.START) {
            float sum = 0;
            int i;

            for (i = len; i >= 0; i--) {
                float w = widths[i - 1 + linestart - widstart + widoff];

                if (w + sum + ellipsiswid > avail) {
                    break;
                }

                sum += w;
            }

            ellipsisStart = 0;
            ellipsisCount = i;
        } else if (where == TextUtils.TruncateAt.END) {
>>>>>>> 54b6cfa... Initial Contribution
            float sum = 0;
            int i;

            for (i = 0; i < len; i++) {
<<<<<<< HEAD
                float w = widths[i + lineStart - widthStart];

                if (w + sum + ellipsisWidth > avail) {
=======
                float w = widths[i + linestart - widstart + widoff];

                if (w + sum + ellipsiswid > avail) {
>>>>>>> 54b6cfa... Initial Contribution
                    break;
                }

                sum += w;
            }

            ellipsisStart = i;
            ellipsisCount = len - i;
<<<<<<< HEAD
            if (forceEllipsis && ellipsisCount == 0 && len > 0) {
                ellipsisStart = len - 1;
                ellipsisCount = 1;
            }
        } else {
            // where = TextUtils.TruncateAt.MIDDLE We only support middle ellipsis on a single line
            if (mMaximumVisibleLineCount == 1) {
                float lsum = 0, rsum = 0;
                int left = 0, right = len;

                float ravail = (avail - ellipsisWidth) / 2;
                for (right = len; right >= 0; right--) {
                    float w = widths[right - 1 + lineStart - widthStart];

                    if (w + rsum > ravail) {
                        break;
                    }

                    rsum += w;
                }

                float lavail = avail - ellipsisWidth - rsum;
                for (left = 0; left < right; left++) {
                    float w = widths[left + lineStart - widthStart];

                    if (w + lsum > lavail) {
                        break;
                    }

                    lsum += w;
                }

                ellipsisStart = left;
                ellipsisCount = right - left;
            } else {
                if (Log.isLoggable(TAG, Log.WARN)) {
                    Log.w(TAG, "Middle Ellipsis only supported with one line");
                }
            }
=======
        } else /* where = TextUtils.TruncateAt.MIDDLE */ {
            float lsum = 0, rsum = 0;
            int left = 0, right = len;

            float ravail = (avail - ellipsiswid) / 2;
            for (right = len; right >= 0; right--) {
                float w = widths[right - 1 + linestart - widstart + widoff];

                if (w + rsum > ravail) {
                    break;
                }

                rsum += w;
            }

            float lavail = avail - ellipsiswid - rsum;
            for (left = 0; left < right; left++) {
                float w = widths[left + linestart - widstart + widoff];

                if (w + lsum > lavail) {
                    break;
                }

                lsum += w;
            }

            ellipsisStart = left;
            ellipsisCount = right - left;
>>>>>>> 54b6cfa... Initial Contribution
        }

        mLines[mColumns * line + ELLIPSIS_START] = ellipsisStart;
        mLines[mColumns * line + ELLIPSIS_COUNT] = ellipsisCount;
    }

<<<<<<< HEAD
    // Override the base class so we can directly access our members,
    // rather than relying on member functions.
    // The logic mirrors that of Layout.getLineForVertical
    // FIXME: It may be faster to do a linear search for layouts without many lines.
    @Override
=======
    // Override the baseclass so we can directly access our members,
    // rather than relying on member functions.
    // The logic mirrors that of Layout.getLineForVertical
    // FIXME: It may be faster to do a linear search for layouts without many lines.
>>>>>>> 54b6cfa... Initial Contribution
    public int getLineForVertical(int vertical) {
        int high = mLineCount;
        int low = -1;
        int guess;
        int[] lines = mLines;
        while (high - low > 1) {
            guess = (high + low) >> 1;
            if (lines[mColumns * guess + TOP] > vertical){
                high = guess;
            } else {
                low = guess;
            }
        }
        if (low < 0) {
            return 0;
        } else {
            return low;
        }
    }

<<<<<<< HEAD
    @Override
=======
>>>>>>> 54b6cfa... Initial Contribution
    public int getLineCount() {
        return mLineCount;
    }

<<<<<<< HEAD
    @Override
    public int getLineTop(int line) {
        int top = mLines[mColumns * line + TOP];
        if (mMaximumVisibleLineCount > 0 && line >= mMaximumVisibleLineCount &&
                line != mLineCount) {
            top += getBottomPadding();
        }
        return top;
    }

    @Override
    public int getLineDescent(int line) {
        int descent = mLines[mColumns * line + DESCENT];
        if (mMaximumVisibleLineCount > 0 && line >= mMaximumVisibleLineCount - 1 && // -1 intended
                line != mLineCount) {
            descent += getBottomPadding();
        }
        return descent;
    }

    @Override
=======
    public int getLineTop(int line) {
        return mLines[mColumns * line + TOP];    
    }

    public int getLineDescent(int line) {
        return mLines[mColumns * line + DESCENT];   
    }

>>>>>>> 54b6cfa... Initial Contribution
    public int getLineStart(int line) {
        return mLines[mColumns * line + START] & START_MASK;
    }

<<<<<<< HEAD
    @Override
=======
>>>>>>> 54b6cfa... Initial Contribution
    public int getParagraphDirection(int line) {
        return mLines[mColumns * line + DIR] >> DIR_SHIFT;
    }

<<<<<<< HEAD
    @Override
=======
>>>>>>> 54b6cfa... Initial Contribution
    public boolean getLineContainsTab(int line) {
        return (mLines[mColumns * line + TAB] & TAB_MASK) != 0;
    }

<<<<<<< HEAD
    @Override
=======
>>>>>>> 54b6cfa... Initial Contribution
    public final Directions getLineDirections(int line) {
        return mLineDirections[line];
    }

<<<<<<< HEAD
    @Override
=======
>>>>>>> 54b6cfa... Initial Contribution
    public int getTopPadding() {
        return mTopPadding;
    }

<<<<<<< HEAD
    @Override
=======
>>>>>>> 54b6cfa... Initial Contribution
    public int getBottomPadding() {
        return mBottomPadding;
    }

    @Override
    public int getEllipsisCount(int line) {
        if (mColumns < COLUMNS_ELLIPSIZE) {
            return 0;
        }

        return mLines[mColumns * line + ELLIPSIS_COUNT];
    }

    @Override
    public int getEllipsisStart(int line) {
        if (mColumns < COLUMNS_ELLIPSIZE) {
            return 0;
        }

        return mLines[mColumns * line + ELLIPSIS_START];
    }

    @Override
    public int getEllipsizedWidth() {
        return mEllipsizedWidth;
    }

<<<<<<< HEAD
    void prepare() {
        mMeasured = MeasuredText.obtain();
    }
    
    void finish() {
        mMeasured = MeasuredText.recycle(mMeasured);
    }

=======
>>>>>>> 54b6cfa... Initial Contribution
    private int mLineCount;
    private int mTopPadding, mBottomPadding;
    private int mColumns;
    private int mEllipsizedWidth;

    private static final int COLUMNS_NORMAL = 3;
    private static final int COLUMNS_ELLIPSIZE = 5;
    private static final int START = 0;
    private static final int DIR = START;
    private static final int TAB = START;
    private static final int TOP = 1;
    private static final int DESCENT = 2;
    private static final int ELLIPSIS_START = 3;
    private static final int ELLIPSIS_COUNT = 4;

    private int[] mLines;
    private Directions[] mLineDirections;
<<<<<<< HEAD
    private int mMaximumVisibleLineCount = Integer.MAX_VALUE;

    private static final int START_MASK = 0x1FFFFFFF;
    private static final int DIR_SHIFT  = 30;
    private static final int TAB_MASK   = 0x20000000;

    private static final int TAB_INCREMENT = 20; // same as Layout, but that's private

    private static final char CHAR_FIRST_CJK = '\u2E80';

    private static final char CHAR_NEW_LINE = '\n';
    private static final char CHAR_TAB = '\t';
    private static final char CHAR_SPACE = ' ';
    private static final char CHAR_DOT = '.';
    private static final char CHAR_COMMA = ',';
    private static final char CHAR_COLON = ':';
    private static final char CHAR_SEMICOLON = ';';
    private static final char CHAR_SLASH = '/';
    private static final char CHAR_HYPHEN = '-';

    private static final double EXTRA_ROUNDING = 0.5;

    private static final int CHAR_FIRST_HIGH_SURROGATE = 0xD800;
    private static final int CHAR_LAST_LOW_SURROGATE = 0xDFFF;

    /*
     * This is reused across calls to generate()
     */
    private MeasuredText mMeasured;
=======

    private static final int START_MASK = 0x1FFFFFFF;
    private static final int DIR_MASK   = 0xC0000000;
    private static final int DIR_SHIFT  = 30;
    private static final int TAB_MASK   = 0x20000000;

    private static final char FIRST_RIGHT_TO_LEFT = '\u0590';

    /*
     * These are reused across calls to generate()
     */
    private byte[] mChdirs;
    private char[] mChs;
    private float[] mWidths;
>>>>>>> 54b6cfa... Initial Contribution
    private Paint.FontMetricsInt mFontMetricsInt = new Paint.FontMetricsInt();
}
