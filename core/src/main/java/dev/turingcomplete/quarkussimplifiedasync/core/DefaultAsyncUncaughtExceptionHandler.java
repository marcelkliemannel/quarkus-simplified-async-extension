package dev.turingcomplete.quarkussimplifiedasync.core;

import java.lang.reflect.Method;

/**
 * This is a default {@link AsyncUncaughtExceptionHandler} implementation that
 * will rethrow any uncaught {@link Throwable} in an @{@link Async} method as a
 * {@link RuntimeException}.
 */
public class DefaultAsyncUncaughtExceptionHandler implements AsyncUncaughtExceptionHandler {
  // -- Class Fields ------------------------------------------------------------------------------------------------ //
  // -- Instance Fields --------------------------------------------------------------------------------------------- //
  // -- Initialization ---------------------------------------------------------------------------------------------- //
  // -- Exposed Methods --------------------------------------------------------------------------------------------- //

  @Override
  public void handleUncaughtException(Throwable throwable, Method method, Object... parameters) {
    throw new RuntimeException(String.format("An exception was thrown during the asynchronous execution of the method: %s.%s",
                                             method.getDeclaringClass().getSimpleName(),
                                             method.getName()),
                               throwable);
  }

  // -- Private Methods --------------------------------------------------------------------------------------------- //
  // -- Inner Type -------------------------------------------------------------------------------------------------- //
}
