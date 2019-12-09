package com.zhjs.scfcloud.ticket.config;

import org.crazycake.shiro.exception.SerializationException;
import org.crazycake.shiro.serializer.ObjectSerializer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * @author: dailongting
 * @date:2019/7/9 17:52
 */
public class MyObjectSerializer extends ObjectSerializer {

    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        Object result = null;

        if (bytes == null || bytes.length == 0) {
            return result;
        }

        try {
            ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
            ObjectInputStream objectInputStream = new MyMultiClassLoaderObjectInputStream(byteStream);
            result = objectInputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
            throw new SerializationException("deserialize error", e);
        } catch (ClassNotFoundException e) {
            throw new SerializationException("deserialize error", e);
        }

        return result;
    }
}
