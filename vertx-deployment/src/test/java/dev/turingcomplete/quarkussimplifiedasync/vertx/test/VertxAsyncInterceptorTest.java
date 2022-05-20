package dev.turingcomplete.quarkussimplifiedasync.vertx.test;

import dev.turingcomplete.quarkussimplifiedasync.core.Async;
import dev.turingcomplete.quarkussimplifiedasync.testkit.AsyncAssertions;
import dev.turingcomplete.quarkussimplifiedasync.vertx.VertxAsyncInterceptor;
import io.quarkus.test.QuarkusUnitTest;
import io.quarkus.test.junit.QuarkusTest;
import io.vertx.core.Future;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@QuarkusTest
class VertxAsyncInterceptorTest {
  // -- Class Fields ------------------------------------------------------------------------------------------------ //
  // -- Instance Fields --------------------------------------------------------------------------------------------- //
  // -- Initialization ---------------------------------------------------------------------------------------------- //
  // -- Exposed Methods --------------------------------------------------------------------------------------------- //

  /**
   * Tests that {@link VertxAsyncInterceptor} executed a void returning and
   * with {@link Async} annotated method asynchronously.
   */
  @Test
  @Timeout(value = AsyncAssertions.ASYNC_EXECUTION_TEST_TIMEOUT_MILLIS, unit = TimeUnit.MILLISECONDS)
  void testVoidReturnMethod_executedInParallel() throws InterruptedException {
    AsyncAssertions.assertMethodExecutedInParallel(this::voidReturnMethod);
  }

  /**
   * Tests that {@link VertxAsyncInterceptor} executed a {@link Future} returning
   * and with {@link Async} annotated method asynchronously.
   */
  @Test
  @Timeout(value = AsyncAssertions.ASYNC_EXECUTION_TEST_TIMEOUT_MILLIS, unit = TimeUnit.MILLISECONDS)
  void testVertxFutureReturnMethod_executedInParallel() throws InterruptedException {
    AsyncAssertions.assertMethodExecutedInParallel(this::vertxFutureReturnMethod_nullReturn);
  }

  /**
   * Tests that {@link VertxAsyncInterceptor} passes the result of a returned
   * {@code SucceededFuture} from an asynchronously executed method to the
   * {@code Future} of the original call.
   */
  @Test
  void testVertxFutureReturnMethod_realSucceededFutureReturn() throws InterruptedException {
    var countDownLatch = new CountDownLatch(1);

    String expectedValue = "FooBar";
    vertxFutureReturnMethod_realSucceededFutureReturn(expectedValue)
            .onSuccess(result -> {
              Assertions.assertThat(result).isEqualTo(expectedValue);
              countDownLatch.countDown();
            });

    boolean onSuccessCalled = countDownLatch.await(2, TimeUnit.SECONDS);
    Assertions.assertThat(onSuccessCalled).isTrue();
  }

  /**
   * Tests that {@link VertxAsyncInterceptor} passes the failure of a returned
   * {@code FailedFuture} from an asynchronously executed method to the
   * {@code Future} of the original call.
   */
  @Test
  void testVertxFutureReturnMethod_realFailedFutureReturn() throws InterruptedException {
    var countDownLatch = new CountDownLatch(1);

    String errorMessage = "FooBar";
    vertxFutureReturnMethod_realFailedFutureReturn(errorMessage)
            .onFailure(throwable -> {
              Assertions.assertThat(throwable).isInstanceOf(IllegalStateException.class).hasMessage(errorMessage);
              countDownLatch.countDown();
            });

    boolean onFailureCalled = countDownLatch.await(2, TimeUnit.SECONDS);
    Assertions.assertThat(onFailureCalled).isTrue();
  }

  @Async
  void voidReturnMethod(Runnable runnable) {
    runnable.run();
  }

  @Async
  @SuppressWarnings("UnusedReturnValue")
  Future<Void> vertxFutureReturnMethod_nullReturn(Runnable runnable) {
    runnable.run();
    return null;
  }

  @Async
  Future<String> vertxFutureReturnMethod_realSucceededFutureReturn(String value) {
    return Future.succeededFuture(value);
  }

  @Async
  Future<String> vertxFutureReturnMethod_realFailedFutureReturn(String errorMessage) {
    return Future.failedFuture(new IllegalStateException(errorMessage));
  }

  // -- Private Methods --------------------------------------------------------------------------------------------- //
  // -- Inner Type -------------------------------------------------------------------------------------------------- //
}
