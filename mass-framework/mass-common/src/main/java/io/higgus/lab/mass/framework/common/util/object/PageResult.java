package io.higgus.lab.mass.framework.common.util.object;


import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public final class PageResult<T> implements Serializable {

    private Long total;

    private List<T> list;





}
