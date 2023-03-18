package com.lp.demo.component.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.google.common.base.Preconditions;
import com.lp.demo.component.common.constants.CodeEnum;
import com.lp.demo.component.common.exception.BusinessException;
import com.lp.demo.component.common.support.BaseRepository;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.data.domain.AbstractAggregateRoot;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 这个类利用 Java8 函数式编程将对象和行为进行分离。
 * Service 继承这个类后可以使用链式编程并且将原来 Service 层业务逻辑代码抽离到实体类中进行编写。
 */
@Slf4j
public abstract class AbstractService implements IService {

    protected <ID, T extends AbstractAggregateRoot<T>> Creator<ID, T> creatorFor(BaseRepository<T, ID> repository) {
        return new Creator<ID, T>(repository);
    }

    protected <ID, T extends AbstractAggregateRoot<T>> Updater<ID, T> updaterFor(BaseRepository<T, ID> repository) {
        return new Updater<ID, T>(repository);
    }

    protected class Creator<ID, T extends AbstractAggregateRoot<T>> {

        private final BaseRepository<T, ID> repository;

        private Supplier<T> instanceFun;

        private Consumer<T> updater = a -> {
        };

        private Consumer<T> successFun = a -> log.info("success to save ");

        private BiConsumer<T, Exception> errorFun = (a, e) -> {

            log.error("failed to save {}.", a, e);

            if (BusinessException.class.isAssignableFrom(e.getClass())) {
                throw (BusinessException) e;
            }

            throw new BusinessException(CodeEnum.SAVE_ERROR);
        };

        Creator(BaseRepository<T, ID> repository) {

            Preconditions.checkArgument(repository != null);
            this.repository = repository;
        }

        public Creator<ID, T> instance(Supplier<T> instanceFun) {

            Preconditions.checkArgument(instanceFun != null);
            this.instanceFun = instanceFun;
            return this;
        }

        public Creator<ID, T> update(Consumer<T> updater) {

            Preconditions.checkArgument(updater != null);
            this.updater = this.updater.andThen(updater);
            return this;
        }

        public Creator<ID, T> onSuccess(Consumer<T> onSuccessFun) {

            Preconditions.checkArgument(onSuccessFun != null);
            this.successFun = onSuccessFun.andThen(this.successFun);
            return this;
        }

        public Creator<ID, T> onError(BiConsumer<T, Exception> errorFun) {

            Preconditions.checkArgument(errorFun != null);
            this.errorFun = errorFun.andThen(this.errorFun);
            return this;
        }

        public T call() {

            Preconditions.checkArgument(this.instanceFun != null, "instance fun can not be null");
            Preconditions.checkArgument(this.repository != null, "repository can not be null");

            T a = null;

            try {

                a = this.instanceFun.get();
                this.updater.accept(a);
                this.repository.save(a);
                this.successFun.accept(a);

            } catch (Exception e) {
                this.errorFun.accept(a, e);
            }

            return a;
        }
    }

    protected class Updater<ID, T extends AbstractAggregateRoot<T>> {

        private final BaseRepository<T, ID> repository;

        private ID id;

        private Supplier<Optional<T>> loader;

        private Consumer<ID> onNotExistFun = id -> {
            throw new BusinessException(CodeEnum.NOT_FIND_ERROR);
        };

        private Consumer<T> updater = a -> {
        };

        private Consumer<Data> successFun = a -> log.info("success to update {}", a.getId());

        private BiConsumer<Data, Exception> errorFun = (a, e) -> {

            log.error("failed to update {}.{}", a, e);

            if (BusinessException.class.isAssignableFrom(e.getClass())) {
                throw (BusinessException) e;
            }

            throw new BusinessException(CodeEnum.UPDATE_ERROR);
        };

        Updater(BaseRepository<T, ID> repository) {
            this.repository = repository;
        }

        public Updater<ID, T> id(ID id) {
            Preconditions.checkArgument(id != null);
            this.id = id;
            return this;
        }

        public Updater<ID, T> loader(Supplier<Optional<T>> loader) {
            Preconditions.checkArgument(loader != null);
            this.loader = loader;
            return this;
        }

        public Updater<ID, T> update(Consumer<T> updater) {
            Preconditions.checkArgument(updater != null);
            this.updater = updater.andThen(this.updater);
            return this;
        }

        public Updater<ID, T> onSuccess(Consumer<Data> onSuccessFun) {
            Preconditions.checkArgument(onSuccessFun != null);
            this.successFun = onSuccessFun.andThen(this.successFun);
            return this;
        }

        public Updater<ID, T> onError(BiConsumer<Data, Exception> errorFun) {
            Preconditions.checkArgument(errorFun != null);
            this.errorFun = errorFun.andThen(this.errorFun);
            return this;
        }

        public Updater<ID, T> onNotExist(Consumer<ID> onNotExistFun) {
            Preconditions.checkArgument(onNotExistFun != null);
            this.onNotExistFun = onNotExistFun.andThen(this.onNotExistFun);
            return this;
        }

        public T call() {
            Preconditions.checkArgument(this.repository != null, "repository can not be null");
            Preconditions.checkArgument((this.loader != null || this.id != null), "id and loader can not both be null");
            T a = null;
            try {
                if (id != null && loader != null) {
                    throw new RuntimeException("id and loader can both set");
                }

                if (id != null) {
                    this.loader = () -> this.repository.findById(id);
                }

                Optional<T> aOptional = this.loader.get();

                if (!aOptional.isPresent()) {
                    this.onNotExistFun.accept(id);
                }

                a = aOptional.get();
                updater.accept(a);

                this.repository.save(a);
                this.successFun.accept(new Data(id, a));

            } catch (Exception e) {
                this.errorFun.accept(new Data(id, a), e);
            }

            return a;
        }

        @Value
        public class Data {
            private final ID id;
            private final T entity;
        }
    }
}