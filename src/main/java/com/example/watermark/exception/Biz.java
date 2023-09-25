package com.example.watermark.exception;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

/**
 * @author laihongfeng
 * @ClassName: Biz
 * @Description:
 * @date 2023年07月06日 11:03:51
 */
public class Biz {
    public Biz() {
    }

    public static void check(boolean flag, String msg) {
        if (flag) {
            throw new CustomerException(msg);
        }
    }

    public static void check(boolean flag, String msg, Object... args) {
        if (flag) {
            throw new CustomerException(StrUtil.format(msg, args));
        }
    }

    public static void notNull(Object obj) {
        check(obj == null, "参数为空！");
    }

    public static void notNull(Object obj, String msg) {
        check(obj == null, msg);
    }

    public static void notNull(Object obj, String msg, Object... args) {
        check(obj == null, msg, args);
    }

    public static void notEmpty(Object obj) {
        check(ObjectUtil.isEmpty(obj), "参数为空！");
    }

    public static void notEmpty(Object obj, String msg) {
        check(ObjectUtil.isEmpty(obj), msg);
    }

    public static void notEmpty(Object obj, String msg, Object... args) {
        check(ObjectUtil.isEmpty(obj), msg, args);
    }
}
