package dev.turingcomplete.quarkussimplifiedasync.vertx;

import dev.turingcomplete.quarkussimplifiedasync.core.Async;
import dev.turingcomplete.quarkussimplifiedasync.core.AsyncUncaughtExceptionHandler;
import io.quarkus.arc.Priority;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * An interceptor which executes @{@link Async} methods asynchronously
 * on a Vert.x worker thread.
 *
 * <p>This interceptor does not restrict the allowed return value of
 * an @{@code Async} method. Note, however, that the return value of such a
 * method call always returns null. Because the point of asynchronous
 * execution is that we want to execute the code of the @{@code Async} method
 * parallel to its calling method. Therefore, the method call will return
 * immediately and does not wait for the result of the @{@code Async} method.
 *
 * <p>An exception to this restriction is Vert.x's {@link Future} type (not to
 * be confused it with  Java's {@link java.util.concurrent.Future}). The
 * intercepted method call will also return a `Future` object. This object will
 * later get the results of the actual returned Future in the asynchronous
 * method. With this functionality, we can add callbacks to the @{@code Async}
 * method call, via which we get information about the execution of our
 * asynchronous code. The following code example illustrates this principle with
 * the {@code runAsync()} method:
 * <pre>{@code
 * class MyBean {
 *   void run() {
 *     runAsync().onSuccess(result -> { assert result.equals("Foo"); })
 *               .onFailure(error -> { });
 *   }
 *
 *   @Async
 *   io.vertx.core.Future<String> runAsync(){
 *      return Future.succeededFuture("Foo");
 *   }
 * }
 * }</pre>
 * It's also possible to have {@code Future<Void>} as a return type and return
 * null to use the callback functionalities.
 *
 * <p>By using {@link VertxAsync}, as an alternative to {@link Async}, it's
 * possible to execute an asynchronous method on a custom shared worker
 * executor.
 */
@Interceptor
@Async
@Priority(VertxAsyncInterceptor.PRIORITY)
public class VertxAsyncInterceptor {
  // -- Class Fields ------------------------------------------------------------------------------------------------ //

  public static final int PRIORITY = Interceptor.Priority.LIBRARY_AFTER + 100;

  // -- Instance Fields --------------------------------------------------------------------------------------------- //

  @Inject
  Vertx vertx;

  @Inject
  AsyncUncaughtExceptionHandler asyncUncaughtExceptionHandler;

  // -- Initialization ---------------------------------------------------------------------------------------------- //
  // -- Exposed Methods --------------------------------------------------------------------------------------------- //

  @AroundInvoke
  public Object intercept(InvocationContext context) throws Exception {
    Method method = context.getMethod();
    if (method == null) {
      return context.proceed();
    }

    Handler<Promise<Object>> taskHandler = createTaskHandler(context);
    Future<?> future = executeTaskHandler(method, taskHandler);
    if (method.getReturnType().equals(Future.class)) {
      return future;
    }
    else {
      future.onFailure(e -> asyncUncaughtExceptionHandler.handleUncaughtException(e, context.getMethod(), context.getParameters()));
      return null;
    }
  }

  // -- Private Methods --------------------------------------------------------------------------------------------- //

  private Future<?> executeTaskHandler(Method method, Handler<Promise<Object>> taskHandler) {
    VertxAsync vertxAsyncAnnotation = method.getAnnotation(VertxAsync.class);
    if (vertxAsyncAnnotation != null) {
      return executeOnCustomExecutor(vertxAsyncAnnotation, taskHandler);
    }
    else {
      return executorOnDefaultExecutor(taskHandler);
    }
  }

  private Future<?> executeOnCustomExecutor(VertxAsync vertxAsyncAnnotation, Handler<Promise<Object>> taskHandler) {
    String executorName = vertxAsyncAnnotation.value();
    int executorPoolSize = vertxAsyncAnnotation.executorPoolSize();
    long maxExecutionTime = vertxAsyncAnnotation.maxExecutionTime();
    TimeUnit maxExecutionTimeUnit = vertxAsyncAnnotation.maxExecutionTimeUnit();
    return vertx.createSharedWorkerExecutor(executorName, executorPoolSize, maxExecutionTime, maxExecutionTimeUnit)
                .executeBlocking(taskHandler, false);
  }

  private Future<?> executorOnDefaultExecutor(Handler<Promise<Object>> taskHandler) {
    return vertx.executeBlocking(taskHandler, false);
  }

  private Handler<Promise<Object>> createTaskHandler(InvocationContext context) {
    return promise -> {
      try {
        Object result = context.proceed();

        if (result instanceof Future) {
          // Map the results from the actual returned `Future` of the async
          // method to the `Future` that was returned from the method call.
          ((Future<?>) result).onComplete(asyncResult -> {
            if (asyncResult.succeeded()) {
              promise.complete(asyncResult.result());
            }
            else {
              promise.fail(asyncResult.cause());
            }
          });
        }
        else {
          // Handle any other return type
          promise.complete(result);
        }
      }
      catch (Exception e) {
        // Will be handled by the `asyncUncaughtExceptionHandler`
        promise.fail(e);
      }
    };
  }

  // -- Inner Type -------------------------------------------------------------------------------------------------- //
}
