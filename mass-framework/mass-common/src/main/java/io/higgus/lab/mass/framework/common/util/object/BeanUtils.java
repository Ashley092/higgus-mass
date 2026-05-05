package io.higgus.lab.mass.framework.common.util.object;


import cn.hutool.core.bean.BeanUtil;

public class BeanUtils {

    public static <T> T toBean(Object source, Class<T> targetClass) {
        return BeanUtil.toBean(source, targetClass);
    }


}
