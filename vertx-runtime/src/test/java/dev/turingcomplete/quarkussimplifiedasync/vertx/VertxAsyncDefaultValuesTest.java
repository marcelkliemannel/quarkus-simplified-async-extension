package dev.turingcomplete.quarkussimplifiedasync.vertx;

import io.vertx.core.VertxOptions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The {@link VertxAsync} annotation should use the default values from
 * {@link VertxOptions}. But since annotation default values must be a constant,
 * it's not always possible to directly reference to {@link VertxOptions}'s
 * fields. This test should ensure, that the copied default values stay aligned
 * with the official ones.
 */
class VertxAsyncDefaultValuesTest {
  // -- Class Fields ------------------------------------------------------------------------------------------------ //
  // -- Instance Fields --------------------------------------------------------------------------------------------- //
  // -- Initialization ---------------------------------------------------------------------------------------------- //
  // -- Exposed Methods --------------------------------------------------------------------------------------------- //

  /**
   * Tests whether {@link VertxAsync#maxExecutionTime()} is
   * {@link VertxOptions#DEFAULT_MAX_WORKER_EXECUTE_TIME}.
   */
  @Test
  void testDefaultMaxExecutionTime() throws NoSuchMethodException {
    VertxAsync vertxAsyncAnnotation = VertxAsyncDefaultValuesTest.class.getDeclaredMethod("dummyMethodWithDefaultVertxAsyncProperties").getAnnotation(VertxAsync.class);
    assertThat(vertxAsyncAnnotation.maxExecutionTime())
              .isEqualTo(VertxOptions.DEFAULT_MAX_WORKER_EXECUTE_TIME);
  }

  /**
   * Tests whether {@link VertxAsync#maxExecutionTimeUnit()} is
   * {@link VertxOptions#DEFAULT_MAX_WORKER_EXECUTE_TIME_UNIT}.
   */
  @Test
  void testDefaultMaxExecutionTimeUnit() throws NoSuchMethodException {
    VertxAsync vertxAsyncAnnotation = VertxAsyncDefaultValuesTest.class.getDeclaredMethod("dummyMethodWithDefaultVertxAsyncProperties").getAnnotation(VertxAsync.class);
    assertThat(vertxAsyncAnnotation.maxExecutionTimeUnit())
            .isEqualTo(VertxOptions.DEFAULT_MAX_WORKER_EXECUTE_TIME_UNIT);
  }

  // -- Private Methods --------------------------------------------------------------------------------------------- //

  @VertxAsync
  private void dummyMethodWithDefaultVertxAsyncProperties() {
  }

  // -- Inner Type -------------------------------------------------------------------------------------------------- //
}
