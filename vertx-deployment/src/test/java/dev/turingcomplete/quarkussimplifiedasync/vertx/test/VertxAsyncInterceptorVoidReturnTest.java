package dev.turingcomplete.quarkussimplifiedasync.vertx.test;

import dev.turingcomplete.quarkussimplifiedasync.core.Async;
import dev.turingcomplete.quarkussimplifiedasync.testkit.AsyncAssertions;
import dev.turingcomplete.quarkussimplifiedasync.vertx.VertxAsyncInterceptor;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.TimeUnit;

@QuarkusTest
class VertxAsyncInterceptorVoidReturnTest {
  // -- Class Fields ------------------------------------------------------------------------------------------------ //
  // -- Instance Fields --------------------------------------------------------------------------------------------- //
  // -- Initialization ---------------------------------------------------------------------------------------------- //
  // -- Exposed Methods --------------------------------------------------------------------------------------------- //

  /**
   * Tests whether {@link VertxAsyncInterceptor} executes a void returning method
   * asynchronously.
   */
  @Test
  @Timeout(value = AsyncAssertions.ASYNC_EXECUTION_TEST_TIMEOUT_MILLIS, unit = TimeUnit.MILLISECONDS)
  void test_voidReturnMethod() throws InterruptedException {
    AsyncAssertions.assertAsyncMethodExecutedInParallel(this::voidReturnMethod);
  }

  @Async
  void voidReturnMethod(Runnable runnable) {
    runnable.run();
  }

  // -- Private Methods --------------------------------------------------------------------------------------------- //
  // -- Inner Type -------------------------------------------------------------------------------------------------- //
}
