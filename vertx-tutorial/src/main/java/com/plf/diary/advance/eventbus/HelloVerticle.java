package com.plf.diary.advance.eventbus;

import io.vertx.core.AbstractVerticle;

/**
 * @author panlf
 * @date 2022/3/26
 */
public class HelloVerticle extends AbstractVerticle {
    @Override
    public void start() throws Exception {
        vertx.eventBus().consumer("hello.vertx.addr",msg->{
           msg.reply("EventBus -> Hello Vert.x World!");
        });

        vertx.eventBus().consumer("hello.named.addr",msg->{
            String name = (String) msg.body();
            msg.reply(String.format("EventBus -> Hello %s!",name));
        });
    }
}
