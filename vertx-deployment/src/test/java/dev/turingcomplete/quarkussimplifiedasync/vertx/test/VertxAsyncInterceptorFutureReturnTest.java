package dev.turingcomplete.quarkussimplifiedasync.vertx.test;

import dev.turingcomplete.quarkussimplifiedasync.core.Async;
import dev.turingcomplete.quarkussimplifiedasync.testkit.AsyncAssertions;
import dev.turingcomplete.quarkussimplifiedasync.vertx.VertxAsyncInterceptor;
import io.quarkus.test.junit.QuarkusTest;
import io.vertx.core.Future;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static dev.turingcomplete.quarkussimplifiedasync.testkit.AsyncAssertions.assertAsyncMethodExecutedInParallel;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
class VertxAsyncInterceptorFutureReturnTest {
  // -- Class Fields ------------------------------------------------------------------------------------------------ //
  // -- Instance Fields --------------------------------------------------------------------------------------------- //
  // -- Initialization ---------------------------------------------------------------------------------------------- //
  // -- Exposed Methods --------------------------------------------------------------------------------------------- //

  /**
   * Tests whether {@link VertxAsyncInterceptor} executes a method asynchronously
   * that has {@link Future} as a return type but returns null.
   */
  @Test
  @Timeout(value = AsyncAssertions.ASYNC_EXECUTION_TEST_TIMEOUT_MILLIS, unit = TimeUnit.MILLISECONDS)
  void test_async_futureReturnType_nullReturn() throws InterruptedException {
    assertAsyncMethodExecutedInParallel(this::async_futureReturnType_nullReturn);
  }

  @Async
  @SuppressWarnings("UnusedReturnValue")
  Future<Void> async_futureReturnType_nullReturn(Runnable runnable) {
    runnable.run();
    return null;
  }

  // ---------------------------------------------------------------------------

  protected static final Future<String> SUCCESS_FUTURE = Future.succeededFuture("FooBar");

  /**
   * Tests whether {@link VertxAsyncInterceptor} passes the result of a returned
   * {@code SucceededFuture} from an asynchronously executed method to the
   * {@link Future} of the original call.
   */
  @Test
  void test_async_futureReturnType_succeededFutureReturn() throws InterruptedException {
    var countDownLatch = new CountDownLatch(1);

    Future<String> succeededFutureReturn = async_futureReturnType_succeededFutureReturn();

    // If not intercepted by the interceptor, both `Future`s would be the same.
    assertThat(succeededFutureReturn).doesNotHaveSameHashCodeAs(SUCCESS_FUTURE);

    succeededFutureReturn.onSuccess(result -> {
      assertThat(result).isEqualTo("FooBar");
      countDownLatch.countDown();
    });

    boolean onSuccessCalled = countDownLatch.await(2, TimeUnit.SECONDS);
    assertThat(onSuccessCalled).isTrue();
  }

  @Async
  Future<String> async_futureReturnType_succeededFutureReturn() {
    return SUCCESS_FUTURE;
  }

  // ---------------------------------------------------------------------------

  protected static final Future<String> FAILED_FUTURE = Future.failedFuture(new IllegalStateException("FooBar"));

  /**
   * Tests whether {@link VertxAsyncInterceptor} passes the failure of a
   * returned {@code FailedFuture} from an asynchronously executed method to the
   * {@link Future} of the original call.
   */
  @Test
  void testVertxFutureReturnMethod_realFailedFutureReturn() throws InterruptedException {
    var countDownLatch = new CountDownLatch(1);

    Future<String> failedFutureReturn = async_futureReturnType_failedFutureReturn();

    // If not intercepted by the interceptor, both `Future`s would be the same.
    assertThat(failedFutureReturn).doesNotHaveSameHashCodeAs(FAILED_FUTURE);

    failedFutureReturn.onFailure(throwable -> {
      assertThat(throwable).isInstanceOf(IllegalStateException.class).hasMessage("FooBar");
      countDownLatch.countDown();
    });

    boolean onFailureCalled = countDownLatch.await(2, TimeUnit.SECONDS);
    assertThat(onFailureCalled).isTrue();
  }

  @Async
  Future<String> async_futureReturnType_failedFutureReturn() {
    return FAILED_FUTURE;
  }

  // -- Private Methods --------------------------------------------------------------------------------------------- //
  // -- Inner Type -------------------------------------------------------------------------------------------------- //
}
