package com.plf.diary.advance.eventbus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

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
    public void start() throws Exception {
        DeploymentOptions opts = new DeploymentOptions()
                .setWorker(true)
                .setInstances(8);

        //vertx.deployVerticle(new HelloVerticle());
        vertx.deployVerticle("com.plf.diary.advance.eventbus.HelloVerticle",opts);
        router = Router.router(vertx);

        router.route("/hello").handler(this::helloVertx);

        router.get("/hello/:name").handler(this::helloName);

        vertx.createHttpServer().requestHandler(router).listen(8888);
    }

    void helloVertx(RoutingContext ctx){
        vertx.eventBus().request("hello.vertx.addr","",reply->{
            ctx.request().response().end((String) reply.result().body());
        });
    }

    void helloName(RoutingContext ctx){
       String name =  ctx.pathParam("name");
        vertx.eventBus().request("hello.named.addr",name,reply->{
            ctx.request().response().end((String) reply.result().body());
        });
    }
}
