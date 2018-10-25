package com.sherlock.imapp.utils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by Administrator on 2018/5/10 0010.
 */

public class ListUtil {
    public static <K, V> Map<K, V> list2Map(List<V> list, String keyMethodName,Class<V> c) {
        Map<K, V> map = new HashMap<K, V>();
        if (list != null) {
            try {
                Method methodGetKey = c.getMethod(keyMethodName);
                for (int i = 0; i < list.size(); i++) {
                    V value = list.get(i);
                    @SuppressWarnings("unchecked")
                    K key = (K) methodGetKey.invoke(list.get(i));
                    map.put(key, value);
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("field can't match the key!");
            }
        }

        return map;
    }

//    public static <K, V> Map<K, V> list2Map2(List<V> list, String fieldName4Key, Class<V> c) {
//        Map<K, V> map = new HashMap<K, V>();
//        if (list != null) {
//            try {
//                PropertyDescriptor propDesc = new PropertyDescriptor(fieldName4Key, c);
//                Method methodGetKey = propDesc.getReadMethod();
//                for (int i = 0; i < list.size(); i++) {
//                    V value = list.get(i);
//                    @SuppressWarnings("unchecked")
//                    K key = (K) methodGetKey.invoke(list.get(i));
//                    map.put(key, value);
//                }
//            } catch (Exception e) {
//                throw new IllegalArgumentException("field can't match the key!");
//            }
//        }
//
//        return map;
//    }
}
