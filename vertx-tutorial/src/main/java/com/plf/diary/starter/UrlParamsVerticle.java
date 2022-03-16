package com.plf.diary.starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

/**
 * 获取URL中的参数
 * @author panlf
 * @date 2021/4/10
 */
public class UrlParamsVerticle extends AbstractVerticle {
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(UrlParamsVerticle.class.getName());
  }

  Router router;

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    //初始化Router
    router = Router.router(vertx);

    //获取body参数，得先添加这句
    router.route().handler(BodyHandler.create());

    //配置Router解析URL
    ///test/page?pageSize=1&pageNum=10
    router.route("/test/page").handler(req -> {
      var pageSize = req.request().getParam("pageSize");
      var pageNum = req.request().getParam("pageNum");
      System.out.println("pageSize:["+pageSize+"],pageNum:["+pageNum+"]");
      req.response()
        .putHeader("content-type", "text/plain")
        .end("Hello from Vert.x!");
    });

    //test/page/1/10
    router.route("/test/page/:pageSize/:pageNum").handler(req -> {
      var pageSize = req.request().getParam("pageSize");
      var pageNum = req.request().getParam("pageNum");
      System.out.println("pageSize:["+pageSize+"],pageNum:["+pageNum+"]");
      req.response()
        .putHeader("content-type", "text/plain")
        .end("Hello from Vert.x!");
    });


    //form-data格式
    //请求头中的content-type:application/json
    router.route("/test/form").handler(req -> {
      var formData = req.request().getFormAttribute("formData");
      req.response()
        .putHeader("content-type", "text/plain")
        .end(formData);
    });

    //json格式数据
    //请求头中的content-type:application/json
    router.route("/test/json").handler(req -> {
      var paramJson = req.getBodyAsJson();
      req.response()
        .putHeader("content-type", "text/plain")
        .end(paramJson.toString());
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
