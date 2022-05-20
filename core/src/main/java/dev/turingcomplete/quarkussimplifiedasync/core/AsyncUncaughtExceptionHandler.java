package dev.turingcomplete.quarkussimplifiedasync.core;

import java.lang.reflect.Method;

/**
 * An implementation of this handler gets called for any uncaught
 * {@link Throwable} in an @{@link Async} method.
 */
@FunctionalInterface
public interface AsyncUncaughtExceptionHandler {
  // -- Class Fields ------------------------------------------------------------------------------------------------ //
  // -- Initialization ---------------------------------------------------------------------------------------------- //
  // -- Exposed Methods --------------------------------------------------------------------------------------------- //

  void handleUncaughtException(Throwable throwable, Method method, Object... parameters);

  // -- Private Methods --------------------------------------------------------------------------------------------- //
  // -- Inner Type -------------------------------------------------------------------------------------------------- //
}
