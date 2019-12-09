package com.zhjs.scfcloud.ticket.controller.jd.handler;


public interface NotifyHandler<T> {
    void handle(T body);
}
