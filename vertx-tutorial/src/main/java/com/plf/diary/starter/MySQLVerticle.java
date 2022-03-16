package com.plf.diary.starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.mysqlclient.MySQLClient;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.SqlClient;
import io.vertx.sqlclient.SqlConnection;
import io.vertx.sqlclient.Tuple;

import java.util.ArrayList;

/**
 * @author panlf
 * @date 2021/4/10
 */
public class MySQLVerticle extends AbstractVerticle {
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(MySQLVerticle.class.getName());
  }

  Router router;

  MySQLConnectOptions connectOptions = new MySQLConnectOptions()
    .setPort(3306)
    .setHost("127.0.0.1")
    .setDatabase("test")
    .setUser("root")
    .setPassword("root");

  PoolOptions poolOptions = new PoolOptions()
    .setMaxSize(5);

  MySQLPool client;

  @Override
  public void start(Promise<Void> startPromise) throws Exception  {

    router = Router.router(vertx);

    client = MySQLPool.pool(vertx, connectOptions, poolOptions);

    router.route("/test/list").handler(req -> {
      var id = req.request().getParam("id");
      client.getConnection(ar1 -> {
        if (ar1.succeeded()) {
          System.out.println("Connected");
          SqlConnection connection = ar1.result();

          connection.preparedQuery("select id,sfzhm from user where id=?")
            .execute(Tuple.of(id),ar2 -> {
              connection.close();
              if (ar2.succeeded()) {
                var list = new ArrayList<JsonObject>();

                ar2.result().forEach(a->{
                  var json = new JsonObject();
                  json.put("id",a.getValue("id"));
                  json.put("sfzhm",a.getValue("sfzhm"));
                  list.add(json);
                });

                req.response()
                  .putHeader("content-type", "text/plain")
                  .end(list.toString());
              }else{
                req.response()
                  .putHeader("content-type", "text/plain")
                  .end(ar2.cause().toString());
              }
            });
          }
        });
      });

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
