package com.lp.demo.common.valid;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * 作用：用于校验List泛型对象中字段合法性
 *
 * 解决@Validated用在Controller接口列表参数上不生效问题
 *
 * 如下，在入参为[{"id":1, "message":[]}]时，@Validated注解不会校验住空的message
 *     @PostMapping("/test/validListParam")
 *     public JsonResult<Void> test(@RequestBody @Validated(AddGroup.class) List<xxxDTO> list) {
 *         return JsonResult.success();
 *     }
 *
 *     @Data
 *     public class xxxDTO {
 *
 *          @NotNull(groups = {AddGroup.class}, message = "id不能为空!")
 *          private Long id;
 *
 *          @Valid
 *          @NotEmpty(groups = {AddGroup.class}, message = "评分信息不能为空!")
 *          private List<yyyDTO> message;
 *     }
 *
 * 修改后即可：
 *     @PostMapping("/test/validListParam")
 *     public JsonResult<Void> test(@RequestBody @Validated(AddGroup.class) ValidList<xxxDTO> list) { // 修改List -> ValidList
 *         return JsonResult.success();
 *     }
 *
 * 可能原因：List在java.util包下，不能自己对参数进行校验
 */
@Data
@NoArgsConstructor
public class ValidList<E> implements List<E> {

    @Valid
    private List<E> list = new LinkedList<>();

    public ValidList(List<E> paramList) {
        this.list = paramList;
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return list.contains(0);
    }

    @Override
    public Iterator<E> iterator() {
        return list.iterator();
    }

    @Override
    public Object[] toArray() {
        return list.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return list.toArray(a);
    }

    @Override
    public boolean add(E e) {
        return list.add(e);
    }

    @Override
    public boolean remove(Object o) {
        return list.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return list.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return list.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        return list.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return list.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return list.retainAll(c);
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public E get(int index) {
        return list.get(index);
    }

    @Override
    public E set(int index, E element) {
        return list.set(index, element);
    }

    @Override
    public void add(int index, E element) {
        list.add(index, element);
    }

    @Override
    public E remove(int index) {
        return list.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return list.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return list.lastIndexOf(o);
    }

    @Override
    public ListIterator<E> listIterator() {
        return list.listIterator();
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return list.listIterator(index);
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return list.subList(fromIndex, toIndex);
    }
}
