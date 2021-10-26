package com.rexijie.ioc.beans.store;

import com.rexijie.ioc.beans.BeanWrapper;
import com.rexijie.ioc.errors.BeanCreationException;

import java.util.Collection;
import java.util.HashSet;
import java.util.function.Predicate;

public class BeanDefinitionSet<T> extends HashSet<T> {
    private boolean primaryBeanSet = false;
    private Object primaryBean = null;

    public boolean add(T typeName, BeanWrapper<?> beanWrapper) {
        if (beanWrapper.isPrimary() && primaryBeanSet) {
            throw new BeanCreationException("Unable to add primary bean of type "+ beanWrapper.getTypename()+
                    " because bean "+ primaryBean+" is already marked as primary for this type");
        }

        this.primaryBeanSet = beanWrapper.isPrimary();
        primaryBean = beanWrapper.isPrimary() ? typeName : null;
        return super.add(typeName);
    }

    @Override
    public boolean add(T s) {
        throw new UnsupportedOperationException("Operation not supported in this type");
    }

    @Override
    public boolean remove(Object o) {
        if (o.equals(primaryBean)) unsetPrimary();
        return super.remove(o);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        unsetPrimary();
        return super.removeAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        unsetPrimary();
        return super.addAll(c);
    }

    @Override
    public boolean removeIf(Predicate<? super T> filter) {
        unsetPrimary();
        return super.removeIf(filter);
    }

    public boolean isPrimaryBeanSet() {
        return primaryBeanSet;
    }

    public Object getPrimaryBean() {
        return primaryBean;
    }

    private void unsetPrimary() {
        this.primaryBeanSet = false;
        this.primaryBean = new Object();
    }


}
