package com.gson;

import java.io.IOException;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.tools.FileObject;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;

/**
 * @author: hezhiqiang
 * @date: 17/4/24
 * @version:
 * @description:
 */

public class SynchronizedFiler implements Filer {
    private final Filer mFiler;
    public SynchronizedFiler(Filer filer){
        this.mFiler = filer;
    }

    @Override
    public JavaFileObject createSourceFile(CharSequence name, Element... originatingElements) throws IOException {
        synchronized(mFiler){
            return mFiler.createSourceFile(name,originatingElements);
        }
    }

    @Override
    public JavaFileObject createClassFile(CharSequence name, Element... originatingElements) throws IOException {
        synchronized(mFiler){
            return mFiler.createClassFile(name,originatingElements);
        }
    }

    @Override
    public FileObject createResource(JavaFileManager.Location location, CharSequence pkg, CharSequence relativeName, Element... originatingElements) throws IOException {
        synchronized(mFiler){
            return mFiler.createResource(location,pkg,relativeName,originatingElements);
        }
    }

    @Override
    public FileObject getResource(JavaFileManager.Location location, CharSequence pkg, CharSequence relativeName) throws IOException {
        synchronized(mFiler){
            return mFiler.getResource(location, pkg, relativeName);
        }
    }
}
