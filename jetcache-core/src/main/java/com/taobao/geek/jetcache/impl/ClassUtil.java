/**
 * Created on  13-09-09 17:20
 */
package com.taobao.geek.jetcache.impl;

import com.alibaba.fastjson.util.IdentityHashMap;
import com.taobao.geek.jetcache.support.CacheConfig;
import com.taobao.geek.jetcache.objectweb.asm.Type;

import java.lang.reflect.Method;
import java.util.HashSet;

/**
 * @author <a href="mailto:yeli.hl@taobao.com">huangli</a>
 */
class ClassUtil {

    private static IdentityHashMap<Method, String> subAreaMap = new IdentityHashMap<Method, String>();
    private static IdentityHashMap<Method, String> methodSigMap = new IdentityHashMap<Method, String>();

    public static String getSubArea(CacheConfig cacheConfig, Method method) {
        // TODO invalid cache when param type changed

        String prefix = subAreaMap.get(method);

        if (prefix == null) {
            StringBuilder sb = new StringBuilder();
            sb.append(cacheConfig.getVersion()).append('_');
            sb.append(method.getDeclaringClass().getName());
            sb.append('.');
            getMethodSig(sb, method);
            subAreaMap.put(method, sb.toString());
            return sb.toString();
        } else {
            return prefix;
        }
    }

    public static Class<?>[] getAllInterfaces(Object obj) {
        Class<?> c = obj.getClass();
        HashSet<Class<?>> s = new HashSet<Class<?>>();
        do {
            Class<?>[] its = c.getInterfaces();
            for (Class<?> it : its) {
                s.add(it);
            }
            c = c.getSuperclass();
        } while (c != null);
        return s.toArray(new Class<?>[s.size()]);
    }

    private static void getMethodSig(StringBuilder sb, Method m) {
        sb.append(m.getName());
        sb.append(Type.getType(m).getDescriptor());
    }

    public static String getMethodSig(Method m) {
        String sig = methodSigMap.get(m);
        if (sig != null) {
            return sig;
        } else {
            StringBuilder sb = new StringBuilder();
            getMethodSig(sb, m);
            sig = sb.toString();
            methodSigMap.put(m, sig);
            return sig;
        }
    }
}
