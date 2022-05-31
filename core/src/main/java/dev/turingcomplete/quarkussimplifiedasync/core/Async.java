package dev.turingcomplete.quarkussimplifiedasync.core;

import javax.interceptor.InterceptorBinding;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The <em>Simplified Async</em> Quarkus extensions will intercept calls to
 * non-private methods that are annotated with @{@link Async} and will execute
 * them asynchronously.
 *
 * <p>The supported return types are dependent on the used extension. See their
 * documentation for a more detailed description.
 */
@Inherited
@InterceptorBinding
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Async {
  // -- Class Fields ------------------------------------------------------------------------------------------------ //
  // -- Exposed Methods --------------------------------------------------------------------------------------------- //
  // -- Inner Type -------------------------------------------------------------------------------------------------- //
}
