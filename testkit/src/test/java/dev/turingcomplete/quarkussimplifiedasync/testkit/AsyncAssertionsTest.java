package dev.turingcomplete.quarkussimplifiedasync.testkit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static dev.turingcomplete.quarkussimplifiedasync.testkit.AsyncAssertions.ASYNC_PARALLEL_TESTS;
import static dev.turingcomplete.quarkussimplifiedasync.testkit.AsyncAssertions.SINGLE_ASYNC_EXECUTION_MILLIS;
import static dev.turingcomplete.quarkussimplifiedasync.testkit.AsyncAssertions.TOTAL_ASYNC_PARALLEL_OVERHEAD_MILLIS;
import static dev.turingcomplete.quarkussimplifiedasync.testkit.AsyncAssertions.assertAsyncMethodExecutedInParallel;

class AsyncAssertionsTest {
  // -- Class Fields ------------------------------------------------------------------------------------------------ //
  // -- Instance Fields --------------------------------------------------------------------------------------------- //
  // -- Initialization ---------------------------------------------------------------------------------------------- //
  // -- Exposed Methods --------------------------------------------------------------------------------------------- //

  /**
   * Tests whether {@link AsyncAssertions#assertAsyncMethodExecutedInParallel(Consumer)}
   * fails with non-asynchronous methods.
   */
  @Test
  @Tag("slow")
  @Timeout(value = (SINGLE_ASYNC_EXECUTION_MILLIS * ASYNC_PARALLEL_TESTS) + TOTAL_ASYNC_PARALLEL_OVERHEAD_MILLIS, unit = TimeUnit.MILLISECONDS)
  void testAssertMethodExecutedInParallel_failsWithNonAsyncMethod() {
    Assertions.assertThatThrownBy(() -> assertAsyncMethodExecutedInParallel(this::nonAsyncMethod))
              .isInstanceOf(AssertionError.class)
              .hasMessageContaining("to be less than:");
  }

  void nonAsyncMethod(Runnable runnable) {
    runnable.run();
  }

  // -- Private Methods --------------------------------------------------------------------------------------------- //
  // -- Inner Type -------------------------------------------------------------------------------------------------- //
}
