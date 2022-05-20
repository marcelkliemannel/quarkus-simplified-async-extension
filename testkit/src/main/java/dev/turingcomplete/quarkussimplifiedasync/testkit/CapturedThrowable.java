package dev.turingcomplete.quarkussimplifiedasync.testkit;

import java.lang.reflect.Method;

public final class CapturedThrowable {
  // -- Class Fields ------------------------------------------------------------------------------------------------ //
  // -- Instance Fields --------------------------------------------------------------------------------------------- //

  public final Throwable throwable;
  public final Method   asyncMethod;
  public final Object[] asyncMethodParameters;

  // -- Initialization ---------------------------------------------------------------------------------------------- //

  public CapturedThrowable(Throwable throwable, Method asyncMethod, Object[] asyncMethodParameters) {
    this.throwable = throwable;
    this.asyncMethod = asyncMethod;
    this.asyncMethodParameters = asyncMethodParameters;
  }

  // -- Exposed Methods --------------------------------------------------------------------------------------------- //
  // -- Private Methods --------------------------------------------------------------------------------------------- //
  // -- Inner Type -------------------------------------------------------------------------------------------------- //
}
