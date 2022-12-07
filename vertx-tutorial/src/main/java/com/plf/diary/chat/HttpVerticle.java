package com.plf.diary.chat;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.sockjs.SockJSBridgeOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;

public class HttpVerticle extends AbstractVerticle {

  public static void main(String[] args) {
    Vertx.vertx().deployVerticle(new HttpVerticle());
  }

  @Override
  public void start() throws Exception {
    EventBus bus = vertx.eventBus();

    HttpServer server = vertx.createHttpServer();

    Router router = Router.router(vertx);



    SockJSHandler sockJSHandler = SockJSHandler.create(vertx);

    PermittedOptions permittedOptions = new PermittedOptions()
      .setAddress("chatroom");

    SockJSBridgeOptions options = new SockJSBridgeOptions()
      .addInboundPermitted(permittedOptions)
      .addOutboundPermitted(permittedOptions);

    router.route("/eventbus/*")
      .subRouter(sockJSHandler.bridge(options));

    router.route().handler(StaticHandler.create());
    server.requestHandler(router).listen(8888,serverAsyncResult->{
      if(serverAsyncResult.succeeded()){
        System.out.println("启动在8888端口");
      }
    });
  }
}
