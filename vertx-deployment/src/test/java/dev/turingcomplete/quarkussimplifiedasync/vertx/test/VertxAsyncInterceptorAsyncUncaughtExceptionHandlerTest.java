package dev.turingcomplete.quarkussimplifiedasync.vertx.test;

import dev.turingcomplete.quarkussimplifiedasync.core.Async;
import dev.turingcomplete.quarkussimplifiedasync.core.AsyncUncaughtExceptionHandler;
import dev.turingcomplete.quarkussimplifiedasync.testkit.CapturedThrowable;
import dev.turingcomplete.quarkussimplifiedasync.testkit.CapturingAsyncUncaughtExceptionHandler;
import dev.turingcomplete.quarkussimplifiedasync.vertx.VertxAsyncInterceptor;
import io.quarkus.test.QuarkusUnitTest;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.QuarkusTestProfile;
import io.quarkus.test.junit.TestProfile;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.RegisterExtension;

import javax.enterprise.inject.spi.CDI;
import java.util.Set;

/**
 * Tests that {@link VertxAsyncInterceptor} handles all throws exception via
 * the {@link AsyncUncaughtExceptionHandler}.
 */
@QuarkusTest
@TestProfile(VertxAsyncInterceptorAsyncUncaughtExceptionHandlerTest.MyTestProfile.class)
class VertxAsyncInterceptorAsyncUncaughtExceptionHandlerTest {
  // -- Class Fields ------------------------------------------------------------------------------------------------ //
  // -- Instance Fields --------------------------------------------------------------------------------------------- //
  // -- Initialization ---------------------------------------------------------------------------------------------- //
  // -- Exposed Methods --------------------------------------------------------------------------------------------- //

  @Test
  @Timeout(2)
  void testVertxAsyncInterceptorHandledExceptionViaAsyncUncaughtExceptionHandler() {
    CapturingAsyncUncaughtExceptionHandler asyncUncaughtExceptionHandler = CDI.current().select(CapturingAsyncUncaughtExceptionHandler.class).get();

    String expectedMessage = "FooBar";
    voidReturnMethodThrowingException(expectedMessage);

    CapturedThrowable capturedException = asyncUncaughtExceptionHandler.waitForCapturedThrowable();
    AssertionsForClassTypes.assertThat(capturedException.throwable)
                           .isInstanceOf(IllegalStateException.class)
                           .hasMessage(expectedMessage);
    AssertionsForClassTypes.assertThat(capturedException.asyncMethod.getName()).isEqualTo("voidReturnMethodThrowingException");
    AssertionsForClassTypes.assertThat(capturedException.asyncMethodParameters).isEqualTo(new Object[] {expectedMessage });
  }

  // --- Methods to be tested

  @Async
  void voidReturnMethodThrowingException(String message) {
    throw new IllegalStateException(message);
  }

  // -- Private Methods --------------------------------------------------------------------------------------------- //
  // -- Inner Type -------------------------------------------------------------------------------------------------- //

  public static class MyTestProfile implements QuarkusTestProfile {

    @Override
    public Set<Class<?>> getEnabledAlternatives() {
      return Set.of(CapturingAsyncUncaughtExceptionHandler.class);
    }
  }
}
