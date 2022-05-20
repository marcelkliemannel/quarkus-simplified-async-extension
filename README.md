# Quarkus Simplified Async Extensions

By annotating a non-private method with `@Async`, the _Simplified Async_ Quarkus extensions simplify the effort to asynchronous execution code. 

The general mechanism is shown in the following code:

```java
class MyClass {
  void run() {
    long start = System.currentTimeMillis();
    System.out.println(System.currentTimeMillis() - start + ": Before async call");
    runAsync(start);
    System.out.println(System.currentTimeMillis() - start + ": After async call");
  }
  
  @Async
  void runAsync(long start) {
    Thread.sleep(1000);
    System.out.println(System.currentTimeMillis() - start + ": Within async call");
  }
}
```

Whose execution would lead to the following output:

```
0 Before async call
1 After async call
1002 Within async call
```

Due to the modular design of Quarkus, there are many ways to execute code asynchronously. There is (or will be) an extension in this collection for many of these ways. Offering separate extensions for individual execution models is reducing third-party dependencies and utilize shared resources with other Quarkus components. 

Currently, there are extensions available for the following execution models:

- [Vert.x worker thread](#asynchronous-execution-via-vertx-extension)

## Asynchronous Execution via Vert.x Extension

Quarkus internal threading mechanic makes heavy usage of [Vert.x](https://vertx.io/). Therefore, it makes sense to directly utilize the powerful functionalities of Vert.x to execute our asynchronous application code. 

This _Quarkus Simplified Async via Vert.x Extension_ will intercept all calls to `@Async` annotated methods and execute them on a Vert.x worker thread.

The property `quarkus.vertx.worker-pool-size` can be used to control Vert.x worker threads pool size ([see documentation](https://quarkus.io/guides/all-config#quarkus-vertx_quarkus.vertx.worker-pool-size)).

### Dependency

This extension, which uses Vert.x for the asynchronous execution, is available at [Maven Central](https://mvnrepository.com/artifact/dev.turingcomplete/quarkus-simplified-async-vertx).

#### Gradle

```kotlin
dependencies {
  implementation("dev.turingcomplete:quarkus-simplified-async-vertx:1.0.0")
}
```

#### Maven

```xml
<dependency>
  <groupId>dev.turingcomplete</groupId>
  <artifactId>quarkus-simplified-async-vertx</artifactId>
  <version>1.0.0</version>
</dependency>
```

### Supported Return Types

This extension does not restrict the allowed return value of an `@Async` method. Note, however, that the return value of such a method call always returns `null`. Because the point of asynchronous execution is that we want to execute the code of the `@Async` method parallel to its calling method. Therefore, the method call will return immediately and does not wait for the result of the `@Async` method.

An exception to this restriction is Vert.x's `io.vertx.core.Future` type (not to be confused it with  Java's `java.util.concurrent.Future`). The intercepted method call will also return a `Future` object. This object will later get the results of the actual returned Future in the asynchronous method. With this functionality, we can add callbacks to the @`Asyc` method call, via which we get information about the execution of our asynchronous code. The following code example illustrates this principle with the `runAsync1()` method:

```java
class MyBean {
  void run() {
    runAsync1().onSuccess(result -> { assert result.equals("Foo"); }).onFailure(error -> { /* .. */ });
    runAsync2().onSuccess(result -> { assert result == null; }).onFailure(error -> { /* .. */ });
  }

  @Async
  io.vertx.core.Future<String> runAsync1() {
    return Future.succeededFuture("Foo");
  }

  @Async
  io.vertx.core.Future<Void> runAsync2() {
    return null;
  }
}
```

It's also possible to have `Future<Void>` as a return type and return `null` to use the callback functionalities, as seen for the `runAsync2()` in the code example above.

## Licensing

Copyright (c) 2022 Marcel Kliemannel

Licensed under the **Apache License, Version 2.0** (the "License"); you may not use this file except in compliance with the License.

You may obtain a copy of the License at <https://www.apache.org/licenses/LICENSE-2.0>.

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the [LICENSE](./LICENSE) for the specific language governing permissions and limitations under the License.
