package com.tms.tms_backend.util;

import com.tms.tms_backend.model.User;
import java.lang.reflect.Field;

public class UserUtil {

    public static String validateFields(User user, String... fields) {
        for (String field : fields) {
            try {
                Field f = User.class.getDeclaredField(field);
                f.setAccessible(true);
                Object value = f.get(user);
                if (value == null || (value instanceof String && ((String) value).trim().isEmpty())) {
                    return field;
                }
            } catch (NoSuchFieldException | IllegalAccessException ignored) {}
        }
        return null;
    }
}
