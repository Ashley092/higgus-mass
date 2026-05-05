package io.higgus.lab.module.erp.admin.controller;


import io.higgus.lab.mass.framework.common.pojo.CommonResult;
import io.higgus.lab.module.erp.admin.controller.vo.ProductRespVO;
import io.higgus.lab.module.erp.admin.convert.product.ProductConvert;
import io.higgus.lab.module.erp.admin.dal.dataobject.product.ProductDO;
import io.higgus.lab.module.erp.admin.service.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static io.higgus.lab.mass.framework.common.pojo.CommonResult.success;


@Tag( name = " erp系统 - 商品接口 ")
@RestController
public class ProductController {

    @Autowired
    ProductService productService;


    @GetMapping("/product/query/")
    public CommonResult<ProductRespVO> getProductSimple(@RequestParam("id") long id) {
        ProductDO productDO = productService.getProductById(id);

        /**
         * 在业务逻辑上，一个商品信息可能有多层嵌套，甚至可能需要引用对象，这就意味着它不是扁平的，而是可能纵深的。
         * 因此，我们仿照 DDD（领域驱动设计） 领域的 聚合根转换模式的思想，尽量使用组合和面向对象结构。
         * 也就是说，面对复杂的业务逻辑情况，
         * VO 对象最终会以一种“聚合”的数据载体表征，
         * 而 convert 会负责 assemble（装配）多个来源的数据，以 Bean 形式存储在内存中
         * 由此形成一个完整、而不破坏纵深，也不扁平的数据
         */

        return success(ProductConvert.INSTANCE.convert(productDO));
    }



}
