

测试设备上下线时发布设备状态对应事件监听器监听结果：

发布者（事件发布）|观察者（事件监听）|是否监听到|结果|备注
---|---|---|---|---
事务方法+直接发布|异步监听(@Async + @EventListener)|是|√|发布者更新数据后的结果
事务方法+直接发布|事务监听(@TransactionalEventListener)|是|√|发布者更新数据后的结果
事务方法+调私有方法发布|异步监听(@Async + @EventListener)|是|√|发布者更新数据后的结果
事务方法+调私有方法发布|事务监听(@TransactionalEventListener)|是|√|发布者更新数据后的结果
无事务方法+直接发布|异步监听(@Async + @EventListener)|是|√|发布者更新数据后的结果
无事务方法+直接发布|事务监听(@TransactionalEventListener)|否|×|事务监听器只能监听事务方法发布
无事务方法+调私有方法发布|异步监听(@Async + @EventListener)|是|√|发布者更新数据后的结果
无事务方法+调私有方法发布|事务监听(@TransactionalEventListener)|否|×|事务监听器只能监听事务方法发布
