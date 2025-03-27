package com.github.indeednb.taskstatusquery.infrastructure.convertor;

import java.util.function.BiConsumer;

public class SetterConvertor {
    private SetterConvertor() {
        throw new IllegalStateException("Utility class");
    }
    /**
     * 将setter(T value)方法引用转换为setter(Object vlue)方法
     */
    @SuppressWarnings("unchecked")
    public static <T, U> BiConsumer<T, Object> conver(BiConsumer<T, U> setter) {
        return (t, u) -> setter.accept(t, (U) u);
    }
}
