package dev.turingcomplete.quarkussimplifiedasync.vertx.test;

import dev.turingcomplete.quarkussimplifiedasync.testkit.AsyncAssertions;
import dev.turingcomplete.quarkussimplifiedasync.vertx.VertxAsync;
import dev.turingcomplete.quarkussimplifiedasync.vertx.VertxAsyncInterceptor;
import io.quarkus.test.junit.QuarkusTest;
import io.vertx.core.Future;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static dev.turingcomplete.quarkussimplifiedasync.testkit.AsyncAssertions.assertAsyncMethodExecutedInParallel;
import static io.vertx.core.Future.succeededFuture;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for custom executors via {@link VertxAsync}.
 */
@QuarkusTest
class VertxAsyncInterceptorCustomExecutorTest {
  // -- Class Fields ------------------------------------------------------------------------------------------------ //

  private static final String CUSTOM_EXECUTOR_NAME = "CustomExecutorTest";

  // -- Instance Fields --------------------------------------------------------------------------------------------- //
  // -- Initialization ---------------------------------------------------------------------------------------------- //
  // -- Exposed Methods --------------------------------------------------------------------------------------------- //

  /**
   * Tests whether {@link VertxAsyncInterceptor} executes a {@link VertxAsync}
   * annotated method asynchronously in parallel.
   */
  @Test
  @Timeout(value = AsyncAssertions.ASYNC_EXECUTION_TEST_TIMEOUT_MILLIS, unit = TimeUnit.MILLISECONDS)
  void testVertxAsyncParallelExecution() throws InterruptedException {
    assertAsyncMethodExecutedInParallel(this::voidReturnMethod);
  }

  @VertxAsync
  void voidReturnMethod(Runnable runnable) {
    runnable.run();
  }

  /**
   * Tests whether {@link VertxAsyncInterceptor} executes a with
   * {@link VertxAsync} annotated method on the
   * {@link VertxAsync#DEFAULT_EXECUTOR_NAME} executor if no name
   * was specified.
   */
  @Test
  void testAsyncOnDefaultExecutor() throws InterruptedException {
    var countDownLatch = new CountDownLatch(1);

    asyncOnDefaultExecutor().onSuccess(threadName -> {
      assertThat(threadName).startsWith(VertxAsync.DEFAULT_EXECUTOR_NAME);
      countDownLatch.countDown();
    });

    boolean onSuccessCalled = countDownLatch.await(2, TimeUnit.SECONDS);
    assertThat(onSuccessCalled).isTrue();
  }

  @VertxAsync
  Future<String> asyncOnDefaultExecutor() {
    return succeededFuture(Thread.currentThread().getName());
  }

  /**
   * Tests whether {@link VertxAsyncInterceptor} executes a with
   * {@link VertxAsync} annotated method on the
   * {@link #CUSTOM_EXECUTOR_NAME} executor if it was explicitly specified.
   */
  @Test
  void testAsyncOnCustomExecutor() throws InterruptedException {
    var countDownLatch = new CountDownLatch(1);

    asyncOnCustomExecutor().onSuccess(threadName -> {
      assertThat(threadName).startsWith(CUSTOM_EXECUTOR_NAME);
      countDownLatch.countDown();
    });

    boolean onSuccessCalled = countDownLatch.await(2, TimeUnit.SECONDS);
    assertThat(onSuccessCalled).isTrue();
  }

  @VertxAsync(CUSTOM_EXECUTOR_NAME)
  Future<String> asyncOnCustomExecutor() {
    return succeededFuture(Thread.currentThread().getName());
  }

  // -- Private Methods --------------------------------------------------------------------------------------------- //
  // -- Inner Type -------------------------------------------------------------------------------------------------- //
}
