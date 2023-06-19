package com.ecnu.rai.counsel.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class LowerCaseUtil {
    public static void changePropertyNamesToLowerCase(Object object) throws Exception{
        Class<?> clazz = object.getClass();
        List<Field> fields = Arrays.asList(clazz.getDeclaredFields());

        for (Field field : fields) {
            String fieldName = field.getName();
            String newFieldName = Character.toLowerCase(fieldName.charAt(0)) + fieldName.substring(1);

            PropertyDescriptor pd = new PropertyDescriptor(fieldName, clazz);
            Method getter = pd.getReadMethod();
            Object value = getter.invoke(object);

            if (value != null) {
                Method setter = pd.getWriteMethod();
                setter.invoke(object, newFieldName);
            }

            if (value instanceof List) {
                List<?> list = (List<?>) value;
                for (Object item : list) {
                    changePropertyNamesToLowerCase(item);
                }
            } else if (value instanceof Object) {
                changePropertyNamesToLowerCase(value);
            }
        }
    }
}
