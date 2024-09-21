工作中事务使用

# TransactionSynchronizationAdapter

## 概述

TransactionSynchronizationAdapter 是 Spring 框架中的一个类，它实现了 TransactionSynchronization 接口，提供了一种方便的方式来使用事务同步回调。
通过实现 TransactionSynchronization 接口，开发者可以在事务的不同阶段（如提交前、提交后、回滚后等）执行自定义的逻辑。

TransactionSynchronizationAdapter 5.3版本之后已移除，后使用`TransactionSynchronization`接口上的默认方法

## 原理

TransactionSynchronizationManager 是 Spring 中管理事务同步的中心类。它维护了当前线程的事务同步回调，这些回调与当前线程绑定的资源（如数据库连接）相关联。
当一个事务被管理时，你可以通过 TransactionSynchronizationManager.registerSynchronization 方法注册一个 TransactionSynchronization 实例。Spring 会在事务的相应阶段调用该实例的回调方法。
TransactionSynchronizationAdapter 是 TransactionSynchronization 的一个空实现，它为所有的方法提供了空操作。你可以选择性地覆盖你感兴趣的方法，而不是实现接口中的所有方法。

## 优点

- 灵活性：允许在事务的关键阶段插入自定义逻辑，如资源清理、统计信息更新、后续操作触发等。
- 便利性：通过提供默认实现，TransactionSynchronizationAdapter 使得你只需覆盖你关心的回调方法。
- 一致性：确保在事务的正确阶段执行操作，有助于保持代码的一致性和可维护性。
- 集成：与 Spring 的事务管理无缝集成，不需要额外的事务控制代码。

## 缺点

- 依赖于Spring框架：使用 TransactionSynchronizationAdapter 需要依赖于Spring框架，这对于非Spring项目来说可能是一个限制。
- 线程局限性：TransactionSynchronization 是与线程绑定的，这意味着它只能在事务控制的同一线程中工作。
- 性能考虑：如果在事务同步回调中执行耗时操作，可能会影响事务的性能。
- 复杂性：在复杂的事务场景中，管理多个同步回调可能会增加代码的复杂性。

## 使用场景

- 在事务提交后发送消息或通知。
- 在事务完成后执行某些统计或日志记录。
- 在事务回滚后进行资源清理或状态重置。
- 在事务提交前进行最后的校验或准备工作。

## 默认方法

- `beforeCommit(boolean readOnly)`: 事务提交之前调用，readOnly 标志指示事务是否是只读的。
- `beforeCompletion()`: 事务完成之前（即提交或回滚之前）调用。
- `afterCommit()`: 事务提交之后调用。
- `afterCompletion(int status)`: 事务完成之后调用，status 表示完成状态（提交或回滚）。
- `suspend()`: 事务挂起时调用。
- `resume()`: 事务恢复时调用。
- `flush()`: 清理挂起资源时调用。

## 执行流程

1. 客户端代码请求事务管理器开始一个事务。
2. 事务管理器在 `TransactionSynchronizationManager` 中注册 `TransactionSynchronizationAdapter`。
3. 客户端执行事务性工作。
4. 事务完成后，客户端请求事务管理器提交或回滚事务。
5. 如果是提交，`TransactionSynchronizationAdapter` 的 `beforeCommit` 方法将被触发。如果事务回滚，事务管理器将调用 `TransactionSynchronizationAdapter` 的 `beforeCompletion` 方法。
6. 客户端的自定义逻辑在提交前执行。
7. 事务提交后，`TransactionSynchronizationAdapter` 的 `afterCommit` 方法被触发。
8. 客户端的自定义逻辑在提交后执行。
9. 不论是提交还是回滚，`TransactionSynchronizationAdapter` 的 `afterCompletion` 方法都会被触发。
10. 客户端的自定义逻辑在事务完成后执行。


## 简单示例
```java
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class MyService {

    public void performTransactionalOperation() {
        // 执行一些事务操作...

        // 注册事务同步适配器
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                @Override
                public void afterCommit() {
                    // 事务提交后执行
                    doAfterTransactionCommit();
                }
        });
    }

    private void doAfterTransactionCommit() {
        // 自定义逻辑...
    }
}
```




