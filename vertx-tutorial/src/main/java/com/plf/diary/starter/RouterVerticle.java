package com.plf.diary.starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;

/**
 * 路由
 * @author panlf
 * @date 2021/4/10
 */
public class RouterVerticle extends AbstractVerticle {

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(RouterVerticle.class.getName());
  }

  Router router;

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    //初始化Router
    router = Router.router(vertx);

    //配置Router解析URL
    //一般作为默认的地址
    router.route("/hello").handler(req -> {
        req.response()
          .putHeader("content-type", "text/plain")
          .end("Hello from Vert.x!");
    });

    router.get("/getData").handler(req -> {
        req.response().end("Get Data");
    });

    router.get("/hello/:name").handler(req -> {
      String name = req.pathParam("name");
      req.response().end(String.format("Hello %s",name));
    });


    // 将Router与vertx HttpServer绑定
    vertx.createHttpServer().requestHandler(router).listen(8888, http -> {
      if (http.succeeded()) {
        startPromise.complete();
        System.out.println("HTTP server started on port 8888");
      } else {
        startPromise.fail(http.cause());
      }
    });
  }
}
