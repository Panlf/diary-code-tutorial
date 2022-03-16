package com.plf.diary.starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.templ.thymeleaf.ThymeleafTemplateEngine;

/**
 * @author panlf
 * @date 2021/5/2
 */
public class TemplateVerticle extends AbstractVerticle {

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(TemplateVerticle.class.getName());
  }

  Router router;

  ThymeleafTemplateEngine thymeleafTemplateEngine;

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    //初始化Router
    router = Router.router(vertx);

    thymeleafTemplateEngine = ThymeleafTemplateEngine.create(vertx);

    router.route("/static/*").handler(StaticHandler.create());

    //配置Router解析URL
    router.route("/").handler(req -> {
      JsonObject obj = new JsonObject();
      obj.put("name","Hello World From Verticle");
      thymeleafTemplateEngine.render(obj,
        "templates/index.html",
        bufferAsyncResult ->{
          if(bufferAsyncResult.succeeded()){
            req.response()
              .putHeader("content-type", "text/html")
              .end(bufferAsyncResult.result());
          } else {
            req.fail(bufferAsyncResult.cause());
          }
      });
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
