package com.zhjs.scfcloud.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

/**
 * @author: dailongting
 * @date:2019/7/9 17:53
 */
public class MyMultiClassLoaderObjectInputStream extends ObjectInputStream {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public MyMultiClassLoaderObjectInputStream(InputStream in) throws IOException {
        super(in);
    }

    /**
     * Try :
     * 1. thread class loader
     * 2. application class loader
     * 3. system class loader
     * @param desc
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Override
    protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
        String name = desc.getName();
        if(name.startsWith("com.zhjs.scfcloud.ticket.config")){
            name = name.replace("com.zhjs.scfcloud.ticket.config","com.zhjs.scfcloud.config");
        }
        try {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            return Class.forName(name, false, cl);
        } catch (Throwable ex) {
            logger.debug("Cannot access thread context ClassLoader!", ex);
        }

        try {
            ClassLoader cl = MyMultiClassLoaderObjectInputStream.class.getClassLoader();
            return Class.forName(name, false, cl);
        } catch (Throwable ex) {
            logger.debug("Cannot access application ClassLoader", ex);
        }

        try {
            ClassLoader cl = ClassLoader.getSystemClassLoader();
            return Class.forName(name, false, cl);
        } catch (Throwable ex) {
            logger.debug("Cannot access system ClassLoader", ex);
        }

        return super.resolveClass(desc);
    }
}
