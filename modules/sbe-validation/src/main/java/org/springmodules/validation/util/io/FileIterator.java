/*
 * Copyright 2004-2009 the original author or authors.
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

package org.springmodules.validation.util.io;

import java.io.File;
import java.io.FileFilter;
import java.util.Iterator;

import org.apache.commons.collections.iterators.ArrayIterator;
import org.springframework.util.Assert;
import org.springmodules.validation.util.collection.ReadOnlyIterator;

/**
 * Iterates over the files within a specific directory.
 *
 * @author Uri Boness
 */
public class FileIterator extends ReadOnlyIterator {

    private Iterator fileIterator;

    public FileIterator(String dirName) {
        this(new File(dirName));
    }

    public FileIterator(File dir) {
        Assert.isTrue(dir.isDirectory(), "Given file must be a directory");
        fileIterator = new ArrayIterator(dir.listFiles());
    }

    public FileIterator(String dirName, FileFilter filter) {
        this(new File(dirName), filter);
    }

    public FileIterator(File dir, FileFilter filter) {
        Assert.isTrue(dir.isDirectory(), "Given file must be a directory");
        fileIterator = new ArrayIterator(dir.listFiles(filter));
    }

    public boolean hasNext() {
        return fileIterator.hasNext();
    }

    public Object next() {
        return fileIterator.next();
    }

}
