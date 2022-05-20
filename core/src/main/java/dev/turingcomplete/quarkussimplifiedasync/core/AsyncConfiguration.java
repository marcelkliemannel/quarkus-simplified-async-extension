package dev.turingcomplete.quarkussimplifiedasync.core;

import io.quarkus.arc.DefaultBean;

import javax.enterprise.inject.Produces;

/**
 * This bean provides the default beans for functionality related to the
 * executions of @{@link Async} methods.
 */
public class AsyncConfiguration {
  // -- Class Fields ------------------------------------------------------------------------------------------------ //
  // -- Instance Fields --------------------------------------------------------------------------------------------- //
  // -- Initialization ---------------------------------------------------------------------------------------------- //
  // -- Exposed Methods --------------------------------------------------------------------------------------------- //

  @Produces
  @DefaultBean
  public AsyncUncaughtExceptionHandler asyncUncaughtExceptionHandler() {
    return new DefaultAsyncUncaughtExceptionHandler();
  }

  // -- Private Methods --------------------------------------------------------------------------------------------- //
  // -- Inner Type -------------------------------------------------------------------------------------------------- //
}
