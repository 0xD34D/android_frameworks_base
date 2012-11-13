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

package android.text.style;

<<<<<<< HEAD
/**
 * Represents a single tab stop on a line.
 */
public interface TabStopSpan
extends ParagraphStyle
{
    /**
     * Returns the offset of the tab stop from the leading margin of the
     * line.
     * @return the offset
     */
    public int getTabStop();

    /**
     * The default implementation of TabStopSpan.
     */
    public static class Standard
    implements TabStopSpan
    {
        /**
         * Constructor.
         *
         * @param where the offset of the tab stop from the leading margin of
         *        the line
         */
=======
public interface TabStopSpan
extends ParagraphStyle
{
    public int getTabStop();

    public static class Standard
    implements TabStopSpan
    {
>>>>>>> 54b6cfa... Initial Contribution
        public Standard(int where) {
            mTab = where;
        }

        public int getTabStop() {
            return mTab;
        }

        private int mTab;
    }
}
