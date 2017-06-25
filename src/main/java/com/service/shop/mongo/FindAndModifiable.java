package com.service.shop.mongo;

import java.util.Optional;

public interface FindAndModifiable<T,I> {
    Optional<T> findAndModify(T t, I i);
}
