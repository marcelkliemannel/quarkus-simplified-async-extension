package dev.turingcomplete.quarkussimplifiedasync.testkit;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

public final class AsyncAssertions {
  // -- Class Fields ------------------------------------------------------------------------------------------------ //

  public static final int SINGLE_ASYNC_EXECUTION_MILLIS        = 500;
  public static final int TOTAL_ASYNC_PARALLEL_OVERHEAD_MILLIS = 250;
  /**
   * Milliseconds a test case should use as a timeout if its executes
   * {@link AsyncAssertions#assertMethodExecutedInParallel(Consumer)}
   */
  public static final int ASYNC_EXECUTION_TEST_TIMEOUT_MILLIS  = SINGLE_ASYNC_EXECUTION_MILLIS + TOTAL_ASYNC_PARALLEL_OVERHEAD_MILLIS + 200;
  public static final int ASYNC_PARALLEL_TESTS                 = 6;

  // -- Instance Fields --------------------------------------------------------------------------------------------- //
  // -- Initialization ---------------------------------------------------------------------------------------------- //

  private AsyncAssertions() {
    throw new UnsupportedOperationException();
  }

  // -- Exposed Methods --------------------------------------------------------------------------------------------- //

  /**
   * Asserts that the given {@link Consumer} gets executed in parallel if
   * executed {@link #ASYNC_PARALLEL_TESTS}-times.
   *
   * <p>The caller must run the {@link Runnable} from the {@code Consumer}
   * inside the {@code Consumer} method. For example:
   * <pre>{@code
   *  @Test
   *  void testRunAsync() {
   *    assertMethodExecutedInParallel(this::runAsync)
   *  }
   *
   *  @Async
   *  void runAsync(Runnable runnable) {
   *    runnable.run();
   *  }
   * }</pre>
   *
   * <p>The execution of the {@link Runnable} will take
   * {@link #SINGLE_ASYNC_EXECUTION_MILLIS}.
   */
  public static void assertMethodExecutedInParallel(Consumer<Runnable> asyncMethod) throws InterruptedException {
    long[] asyncMethodExecutionStartTimes = new long[ASYNC_PARALLEL_TESTS];
    var parallelTestsCountDownLatch = new CountDownLatch(ASYNC_PARALLEL_TESTS);

    // Execute the given `asyncMethod` `PARALLEL_TESTS`-times in parallel.
    // The execution of one execution takes `SINGLE_ASYNC_METHOD_EXECUTION_MILLIS`.
    long baseStartTime = System.currentTimeMillis();
    for (int i = 0; i < ASYNC_PARALLEL_TESTS; i++) {
      final int finalI = i;
      asyncMethod.accept(() -> {
        try {
          asyncMethodExecutionStartTimes[finalI] = System.currentTimeMillis();
          Thread.sleep(SINGLE_ASYNC_EXECUTION_MILLIS);
          parallelTestsCountDownLatch.countDown();
        }
        catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
    }

    // Since all executions should have run in parallel, it should take in total
    // no more than `SINGLE_ASYNC_METHOD_EXECUTION_MILLIS` for all executions
    // to finish (plus a small overhead).
    boolean allAsyncMethodExecuted = parallelTestsCountDownLatch.await(SINGLE_ASYNC_EXECUTION_MILLIS + TOTAL_ASYNC_PARALLEL_OVERHEAD_MILLIS, TimeUnit.MILLISECONDS);
    assertThat(allAsyncMethodExecuted).isTrue();

    // Ensure that the start time is close to the overall start time.
    // If the task had not been executed in parallel, the start time of each
    // would be higher than `SINGLE_ASYNC_METHOD_EXECUTION_MILLIS` to the
    // previous one.
    for (int i = 0; i < ASYNC_PARALLEL_TESTS; i++) {
      long asyncMethodExecutionStartTime = asyncMethodExecutionStartTimes[i];
      assertThat(asyncMethodExecutionStartTime - baseStartTime).isLessThan(SINGLE_ASYNC_EXECUTION_MILLIS / 10);
    }
  }

  // -- Private Methods --------------------------------------------------------------------------------------------- //
  // -- Inner Type -------------------------------------------------------------------------------------------------- //
}
