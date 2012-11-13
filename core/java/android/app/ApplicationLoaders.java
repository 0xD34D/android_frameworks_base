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

package android.app;

import dalvik.system.PathClassLoader;

import java.util.HashMap;
<<<<<<< HEAD
import java.util.Map;
=======
>>>>>>> 54b6cfa... Initial Contribution

class ApplicationLoaders
{
    public static ApplicationLoaders getDefault()
    {
        return gApplicationLoaders;
    }

<<<<<<< HEAD
    public ClassLoader getClassLoader(String zip, String libPath, ClassLoader parent)
=======
    public ClassLoader getClassLoader(String zip, String appDataDir,
            ClassLoader parent)
>>>>>>> 54b6cfa... Initial Contribution
    {
        /*
         * This is the parent we use if they pass "null" in.  In theory
         * this should be the "system" class loader; in practice we
         * don't use that and can happily (and more efficiently) use the
         * bootstrap class loader.
         */
        ClassLoader baseParent = ClassLoader.getSystemClassLoader().getParent();

        synchronized (mLoaders) {
            if (parent == null) {
                parent = baseParent;
            }

            /*
             * If we're one step up from the base class loader, find
             * something in our cache.  Otherwise, we create a whole
             * new ClassLoader for the zip archive.
             */
            if (parent == baseParent) {
<<<<<<< HEAD
                ClassLoader loader = mLoaders.get(zip);
=======
                ClassLoader loader = (ClassLoader)mLoaders.get(zip);
>>>>>>> 54b6cfa... Initial Contribution
                if (loader != null) {
                    return loader;
                }
    
                PathClassLoader pathClassloader =
<<<<<<< HEAD
                    new PathClassLoader(zip, libPath, parent);
=======
                    new PathClassLoader(zip, appDataDir + "/lib", parent);
>>>>>>> 54b6cfa... Initial Contribution
                
                mLoaders.put(zip, pathClassloader);
                return pathClassloader;
            }

            return new PathClassLoader(zip, parent);
        }
    }

<<<<<<< HEAD
    private final Map<String, ClassLoader> mLoaders = new HashMap<String, ClassLoader>();
=======
    private final HashMap mLoaders = new HashMap();
>>>>>>> 54b6cfa... Initial Contribution

    private static final ApplicationLoaders gApplicationLoaders
        = new ApplicationLoaders();
}
