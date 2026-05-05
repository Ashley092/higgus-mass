package io.higgus.lab.module.erp.admin.service;

import io.higgus.lab.module.erp.admin.dal.dataobject.product.ProductDO;
import io.higgus.lab.module.erp.admin.dal.mysql.product.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    ProductMapper productMapper;

    @Override
    public ProductDO getProductById(Long id) {
        return productMapper.selectById(id);
    }
}
