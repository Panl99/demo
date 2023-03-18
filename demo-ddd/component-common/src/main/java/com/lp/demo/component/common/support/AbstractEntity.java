package com.lp.demo.component.common.support;

import com.lp.demo.component.common.converter.InstantLongConverter;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.domain.AbstractAggregateRoot;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.Instant;

/**
 * 这个类为每个实体的父类，这样子类就不用单独写 Id、CreateTime 、UpdateTime、Version 等属性了。
 * 另外 JPA 还支持自动审计的功能 @EnableJpaAuditing，这个在某些场景下不是很通用，如果有需要可以封装在自己编写的父类上。
 *
 */

/**
 * 事件解耦：
 * 继承了AbstractAggregateRoot这个类后，在实体中可以注册事件，当调用 JPA 的 save 方法后，会将事件发出。
 * 并且还可以通过 @TransactionalEventListener 进行事务的集成。再通过
 * TransactionPhase.BEFORE_COMMIT
 * TransactionPhase.AFTER_COMMIT
 * TransactionPhase.AFTER_ROLLBACK
 * TransactionPhase.AFTER_COMPLETION
 * 这四种配置监听器执行的时机。
 */
@MappedSuperclass
@Data
public abstract class AbstractEntity extends AbstractAggregateRoot<AbstractEntity> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.PROTECTED)
    @Column(name = "id")
    private Long id;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Convert(converter = InstantLongConverter.class)
    @Setter(AccessLevel.PRIVATE)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    @Convert(converter = InstantLongConverter.class)
    @Setter(AccessLevel.PRIVATE)
    private Instant updatedAt;

    @Version
    @Column(name = "version")
    @Setter(AccessLevel.PRIVATE)
    private Integer version;

    @PrePersist
    public void prePersist() {
        this.setCreatedAt(Instant.now());
        this.setUpdatedAt(Instant.now());
    }

    @PreUpdate
    public void preUpdate() {
        this.setUpdatedAt(Instant.now());
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "-" + getId();
    }

}