package dev.turingcomplete.quarkussimplifiedasync.vertx;

import dev.turingcomplete.quarkussimplifiedasync.core.Async;
import io.vertx.core.VertxOptions;

import javax.enterprise.util.Nonbinding;
import javax.interceptor.InterceptorBinding;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * An alternative to {@link Async} which allows the execution of asynchronous
 * methods on a custom shared worker executor.
 *
 * <p>Vert.x will only create one shared work executor for the same name. So,
 * in the following example:
 * <pre>{@code
 * void run() { run1(); run2(); }
 *
 * @VertxAsync("A") void run1() {}
 * @VertxAsync("A") void run2() {}
 * }</pre>
 * both methods will be executed on the {@code A} executor, but this executor
 * gets only initialized once during the call to {@code run1()} and will be
 * reused for {@code run2()}.
 */
@Inherited
@Async
@InterceptorBinding
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface VertxAsync {
  // -- Class Fields ------------------------------------------------------------------------------------------------ //

  String DEFAULT_EXECUTOR_NAME = "VertxAsync-DefaultExecutor";

  // -- Exposed Methods --------------------------------------------------------------------------------------------- //

  /**
   * The name of the shared worker executor to be used for the asynchronous
   * execution of the method.
   *
   * <p>Vert.x will only create one shared worker executor for each given name.
   *
   * @return the shared worker executor name.
   */
  @Nonbinding String value() default DEFAULT_EXECUTOR_NAME;

  /**
   * The pool size of the shared worker executor, which specifies how many tasks
   * can be executed in parallel.
   *
   * <p>The default values is {@link VertxOptions#DEFAULT_WORKER_POOL_SIZE}.
   *
   * <p>Note that the size is taken into account only during the initialization
   * of the shared worker executor. A subsequent call to another method with
   * the same executor name but a different pool size value, will not change the
   * initial pool size.
   *
   * @return the shared worker executor pool size.
   */
  @Nonbinding int executorPoolSize() default VertxOptions.DEFAULT_WORKER_POOL_SIZE;

  /**
   * Determines the maximum execution time of an asynchronous method before
   * Vert.x will log a warning.
   *
   * <p>The {@link TimeUnit} of the value gets determined by
   * {@link VertxAsync#maxExecutionTimeUnit()}.
   *
   * <p>The default value is 60,000,000,000 nanoseconds (= 60 seconds). This is
   * equal to {@link VertxOptions#DEFAULT_MAX_WORKER_EXECUTE_TIME}.
   *
   * @return the maximum execution time.
   */
  @Nonbinding long maxExecutionTime() default 60 * 1000_000_000L;

  /**
   * Determines the {@link TimeUnit} of {@link #maxExecutionTime()}.
   *
   * <p>The default values is equal to
   * {@link VertxOptions#DEFAULT_MAX_WORKER_EXECUTE_TIME_UNIT}.
   *
   * @return the {@link TimeUnit} of the maximum execution time.
   */
  @Nonbinding TimeUnit maxExecutionTimeUnit() default TimeUnit.NANOSECONDS;

  // -- Inner Type -------------------------------------------------------------------------------------------------- //
}
