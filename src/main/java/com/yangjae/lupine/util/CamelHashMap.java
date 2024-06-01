package com.yangjae.lupine.util;

import org.springframework.lang.Nullable;

import java.util.LinkedHashMap;

public class CamelHashMap extends LinkedHashMap {
    @Override
    public Object put(Object key, Object value) {
        return super.put(convertUnderscoreToCamelcase((String) key), value);
    }

    public String convertUnderscoreToCamelcase(@Nullable String name) {
        StringBuilder result = new StringBuilder();
        boolean nextIsUpper = false;
        if (name != null && !name.isEmpty()) {
            if (name.length() > 1 && name.charAt(1) == '_') {
                result.append(Character.toUpperCase(name.charAt(0)));
            } else {
                result.append(name.charAt(0));
            }

            for (int i = 1; i < name.length(); i++) {
                char c = name.charAt(i);
                if (c == '_') {
                    nextIsUpper = true;
                } else {
                    if (nextIsUpper) {
                        result.append(Character.toUpperCase(c));
                        nextIsUpper = false;
                    } else {
                        result.append(c);
                    }
                }
            }
        }

        return result.toString();
    }
}
