package dev.turingcomplete.quarkussimplifiedasync.testkit;

public final class AsyncTestUtils {
  // -- Class Fields ------------------------------------------------------------------------------------------------ //
  // -- Instance Fields --------------------------------------------------------------------------------------------- //
  // -- Initialization ---------------------------------------------------------------------------------------------- //

  private AsyncTestUtils() {
    throw new UnsupportedOperationException();
  }

  // -- Exposed Methods --------------------------------------------------------------------------------------------- //

  /**
   * Creates a {@link Runnable} which sleeps for the given milliseconds.
   */
  public static Runnable sleep(long millis) {
    return () -> {
      try {
        Thread.sleep(millis);
      }
      catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    };
  }

  // -- Private Methods --------------------------------------------------------------------------------------------- //
  // -- Inner Type -------------------------------------------------------------------------------------------------- //
}
