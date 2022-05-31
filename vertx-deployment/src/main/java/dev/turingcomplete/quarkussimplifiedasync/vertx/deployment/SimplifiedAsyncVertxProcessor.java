package dev.turingcomplete.quarkussimplifiedasync.vertx.deployment;

import dev.turingcomplete.quarkussimplifiedasync.vertx.VertxAsyncInterceptor;
import dev.turingcomplete.quarkussimplifiedasync.vertx.VertxAsync;
import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;

class SimplifiedAsyncVertxProcessor {

    private static final String FEATURE = "simplified-async-vertx";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    AdditionalBeanBuildItem additionalBean() {
        return new AdditionalBeanBuildItem(VertxAsyncInterceptor.class,
                                           VertxAsync.class);
    }
}
