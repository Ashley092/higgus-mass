package io.higgus.lab.module.erp.admin.service;


import io.higgus.lab.module.erp.admin.dal.dataobject.product.ProductDO;

public interface ProductService {

    ProductDO getProductById(Long id);
}
