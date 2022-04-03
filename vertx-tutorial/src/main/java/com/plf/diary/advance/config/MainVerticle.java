package com.plf.diary.advance.config;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.*;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.CSRFHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.LoggerHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.sstore.LocalSessionStore;
import io.vertx.ext.web.sstore.SessionStore;

/**
 * @author panlf
 * @date 2022/3/26
 */
public class MainVerticle extends AbstractVerticle {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(MainVerticle.class.getName());
    }

    Router router;

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        router = Router.router(vertx);

        SessionStore sessionStore = LocalSessionStore.create(vertx);
        router.route().handler(LoggerHandler.create());
        router.route().handler(SessionHandler.create(sessionStore));
        router.route().handler(CorsHandler.create("localhost"));
        router.route().handler(CSRFHandler.create(vertx,"secret"));


        router.route("/hello").handler(this::helloVertx);
        ConfigStoreOptions defaultConfig = new ConfigStoreOptions()
                .setType("file")
                .setFormat("json")
                .setConfig(new JsonObject().put("path","conf/config.json"));

        ConfigRetrieverOptions options = new ConfigRetrieverOptions()
                .addStore(defaultConfig);

        ConfigRetriever configRetriever = ConfigRetriever.create(vertx,options);

        Handler<AsyncResult<JsonObject>> handler =
                asyncResult -> this.handleConfigResults(startPromise,router,asyncResult);
        configRetriever.getConfig(handler);
    }

    void helloVertx(RoutingContext ctx){
        ctx.request().response().end("Config - Hello Vertx World");
    }

    void handleConfigResults(Promise<Void> startPromise,
                             Router router,
                             AsyncResult<JsonObject> asyncResult){
        if(asyncResult.succeeded()){
            JsonObject config = asyncResult.result();
            JsonObject http = config.getJsonObject("http");
            int httpPort = http.getInteger("port");
            System.out.println(String.format("HTTP server started on port %d",httpPort));
            vertx.createHttpServer().requestHandler(router).listen(httpPort);
            startPromise.complete();
        }else {
            startPromise.fail("unable to load configuration.");
        }
    }
}
