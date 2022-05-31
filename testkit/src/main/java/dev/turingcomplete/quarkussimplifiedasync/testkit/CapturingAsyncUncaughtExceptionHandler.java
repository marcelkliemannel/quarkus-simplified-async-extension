package dev.turingcomplete.quarkussimplifiedasync.testkit;

import dev.turingcomplete.quarkussimplifiedasync.core.AsyncUncaughtExceptionHandler;
import io.quarkus.arc.Unremovable;

import javax.enterprise.inject.Alternative;
import javax.inject.Singleton;
import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * An {@link AsyncUncaughtExceptionHandler} that remembers a thrown
 * {@link Throwable} as a {@link CapturedThrowable}.
 */
@Alternative
@Singleton
@Unremovable
public class CapturingAsyncUncaughtExceptionHandler implements AsyncUncaughtExceptionHandler {
  // -- Class Fields ------------------------------------------------------------------------------------------------ //
  // -- Instance Fields --------------------------------------------------------------------------------------------- //

  private volatile CapturedThrowable capturedThrowable;

  // -- Initialization ---------------------------------------------------------------------------------------------- //
  // -- Exposed Methods --------------------------------------------------------------------------------------------- //

  @Override
  public void handleUncaughtException(Throwable throwable, Method method, Object... parameters) {
    assertThat(capturedThrowable).overridingErrorMessage("Already captured a Throwable.").isNull();
    capturedThrowable = new CapturedThrowable(throwable, method, parameters);
  }

  /**
   * Waits till this {@link AsyncUncaughtExceptionHandler} captured an
   * {@link CapturedThrowable}.
   *
   * <p>Caller tests should be annotated with a timeout condition.
   */
  public CapturedThrowable waitForCapturedThrowable() {
    while (capturedThrowable == null) {
      Thread.onSpinWait();
    }
    return capturedThrowable;
  }

  // -- Private Methods --------------------------------------------------------------------------------------------- //
  // -- Inner Type -------------------------------------------------------------------------------------------------- //
  // -- Inner Type -------------------------------------------------------------------------------------------------- //
}
