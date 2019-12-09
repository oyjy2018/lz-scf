package com.zhjs.scfcloud.ticket.config;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.crazycake.shiro.IRedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.crazycake.shiro.SessionInMemory;
import org.crazycake.shiro.exception.SerializationException;
import org.crazycake.shiro.serializer.ObjectSerializer;
import org.crazycake.shiro.serializer.RedisSerializer;
import org.crazycake.shiro.serializer.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;
import java.util.*;

/**
 * @author: dailongting
 * @date:2019/5/21 10:32
 */
public class MyRedisSessionDAO extends RedisSessionDAO {
    private Logger logger = LoggerFactory.getLogger(MyRedisSessionDAO.class);

    private static final String DEFAULT_SESSION_KEY_PREFIX = "shiro:session:";
    @Value("${session.redis.keyPrefix}")
    private String keyPrefix;
    private static final long DEFAULT_SESSION_IN_MEMORY_TIMEOUT = 1000L;
    private long sessionInMemoryTimeout = 1000L;
    private static final int DEFAULT_EXPIRE = -2;
    private static final int NO_EXPIRE = -1;
    private int expire = -2;
    private static final int MILLISECONDS_IN_A_SECOND = 1000;
    private IRedisManager redisManager;
    private RedisSerializer keySerializer = new StringSerializer();
    private RedisSerializer valueSerializer = new MyObjectSerializer();
    private static ThreadLocal sessionsInThread = new ThreadLocal();

    public void update(Session session) throws UnknownSessionException {
        this.saveSession(session);
    }

    private void saveSession(Session session) throws UnknownSessionException {
        if (session != null && session.getId() != null) {
            byte[] key;
            byte[] value;
            try {
                key = this.keySerializer.serialize(this.getRedisSessionKey(session.getId()));
                value = this.valueSerializer.serialize(session);
            } catch (SerializationException var5) {
                logger.error("serialize session error. session id=" + session.getId());
                throw new UnknownSessionException(var5);
            }

            if (this.expire == -2) {
                this.redisManager.set(key, value, (int)(session.getTimeout() / 1000L));
            } else {
                if (this.expire != -1 && (long)(this.expire * 1000) < session.getTimeout()) {
                    logger.warn("Redis session expire time: " + this.expire * 1000 + " is less than Session timeout: " + session.getTimeout() + " . It may cause some problems.");
                }

                this.redisManager.set(key, value, this.expire);
            }
        } else {
            logger.error("session or session id is null");
            throw new UnknownSessionException("session or session id is null");
        }
    }

    public void delete(Session session) {
        if (session != null && session.getId() != null) {
            try {
                this.redisManager.del(this.keySerializer.serialize(this.getRedisSessionKey(session.getId())));
            } catch (SerializationException var3) {
                logger.error("delete session error. session id=" + session.getId());
            }

        } else {
            logger.error("session or session id is null");
        }
    }

    public Collection<Session> getActiveSessions() {
        HashSet sessions = new HashSet();

        try {
            Set<byte[]> keys = this.redisManager.keys(this.keySerializer.serialize(DEFAULT_SESSION_KEY_PREFIX + this.keyPrefix + ":" + "*"));
            if (keys != null && keys.size() > 0) {
                Iterator i$ = keys.iterator();

                while(i$.hasNext()) {
                    byte[] key = (byte[])i$.next();
                    Session s = (Session)this.valueSerializer.deserialize(this.redisManager.get(key));
                    sessions.add(s);
                }
            }
        } catch (SerializationException var6) {
            logger.error("get active sessions error.");
        }

        return sessions;
    }

    protected Serializable doCreate(Session session) {
        if (session == null) {
            logger.error("session is null");
            throw new UnknownSessionException("session is null");
        } else {
            Serializable sessionId = this.generateSessionId(session);
            this.assignSessionId(session, sessionId);
            this.saveSession(session);
            return sessionId;
        }
    }

    protected Session doReadSession(Serializable sessionId) {
        if (sessionId == null) {
            logger.warn("session id is null");
            return null;
        } else {
            Session s = this.getSessionFromThreadLocal(sessionId);
            if (s != null) {
                return s;
            } else {
                logger.debug("read session from redis");

                try {
                    s = (Session)this.valueSerializer.deserialize(this.redisManager.get(this.keySerializer.serialize(this.getRedisSessionKey(sessionId))));
                    this.setSessionToThreadLocal(sessionId, s);
                } catch (SerializationException var4) {
                    logger.error("read session error. settionId=" + sessionId);
                }

                return s;
            }
        }
    }

    private void setSessionToThreadLocal(Serializable sessionId, Session s) {
        Map<Serializable, SessionInMemory> sessionMap = (Map)sessionsInThread.get();
        if (sessionMap == null) {
            sessionMap = new HashMap();
            sessionsInThread.set(sessionMap);
        }

        SessionInMemory sessionInMemory = new SessionInMemory();
        sessionInMemory.setCreateTime(new Date());
        sessionInMemory.setSession(s);
        ((Map)sessionMap).put(sessionId, sessionInMemory);
    }

    private Session getSessionFromThreadLocal(Serializable sessionId) {
        Session s = null;
        if (sessionsInThread.get() == null) {
            return null;
        } else {
            Map<Serializable, SessionInMemory> sessionMap = (Map)sessionsInThread.get();
            SessionInMemory sessionInMemory = (SessionInMemory)sessionMap.get(sessionId);
            if (sessionInMemory == null) {
                return null;
            } else {
                Date now = new Date();
                long duration = now.getTime() - sessionInMemory.getCreateTime().getTime();
                if (duration < this.sessionInMemoryTimeout) {
                    s = sessionInMemory.getSession();
                    logger.debug("read session from memory");
                } else {
                    sessionMap.remove(sessionId);
                }

                return s;
            }
        }
    }

    private String getRedisSessionKey(Serializable sessionId) {
        return DEFAULT_SESSION_KEY_PREFIX + this.keyPrefix + ":" + sessionId;
    }

    public IRedisManager getRedisManager() {
        return this.redisManager;
    }

    public void setRedisManager(IRedisManager redisManager) {
        this.redisManager = redisManager;
    }

    public String getKeyPrefix() {
        return this.keyPrefix;
    }

    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }

    public RedisSerializer getKeySerializer() {
        return this.keySerializer;
    }

    public void setKeySerializer(RedisSerializer keySerializer) {
        this.keySerializer = keySerializer;
    }

    public RedisSerializer getValueSerializer() {
        return this.valueSerializer;
    }

    public void setValueSerializer(RedisSerializer valueSerializer) {
        this.valueSerializer = valueSerializer;
    }

    public long getSessionInMemoryTimeout() {
        return this.sessionInMemoryTimeout;
    }

    public void setSessionInMemoryTimeout(long sessionInMemoryTimeout) {
        this.sessionInMemoryTimeout = sessionInMemoryTimeout;
    }

    public int getExpire() {
        return this.expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }

}
