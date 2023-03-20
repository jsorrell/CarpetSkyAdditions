package com.jsorrell.carpetskyadditions.helpers;

import com.google.common.collect.ImmutableMap;
import java.util.Map;

public class DuplicateMapKey {
    public static <T, U> Map<T, U> duplicateMapKey(T originalKey, T copyKey, Map<T, U> originalMap) {
        return ImmutableMap.<T, U>builder()
                .putAll(originalMap)
                .put(copyKey, originalMap.get(originalKey))
                .build();
    }
}
