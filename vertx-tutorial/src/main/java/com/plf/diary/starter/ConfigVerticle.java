package com.plf.diary.starter;

import io.vertx.config.ConfigRetriever;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author panlf
 * @date 2021/5/2
 */
public class ConfigVerticle extends AbstractVerticle {
  private static Logger log = LoggerFactory.getLogger(ConfigVerticle.class);

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(ConfigVerticle.class.getName());
  }

  ConfigRetriever retriever;


  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    retriever = ConfigRetriever.create(vertx);

    JsonObject json = new JsonObject();
    retriever.getConfig(ar -> {
      if (ar.failed()) {
      } else {
        JsonObject object = ar.result();
        log.info("Config JSON:{}", object);
        json.put("hello", object.getString("name"));
      }
    });

    vertx.createHttpServer().requestHandler(req -> {
      req.response()
        .putHeader("content-type", "application/json")
        .end(json.toString())
        .onFailure(e -> {
          req.response().putHeader("content-type", "application/json")
            .end(e.toString());
        });
    }).listen(8888, http -> {
      if (http.succeeded()) {
        startPromise.complete();
        System.out.println("HTTP server started on port 8888");
      } else {
        //这里相当于继续向上抛出异常，用Promise来向上抛出异常
        startPromise.fail(http.cause());
      }
    });
  }
}
