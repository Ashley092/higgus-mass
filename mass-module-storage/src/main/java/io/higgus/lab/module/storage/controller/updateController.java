package io.higgus.lab.module.storage.controller;


import io.higgus.lab.module.storage.controller.vo.CellUpdateReqVO;
import io.higgus.lab.module.storage.service.GeneralService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Tag(name = "测试链路 Controller")
@Slf4j
@RestController
@RequestMapping("/content/test")
public class updateController {

    @Resource
    GeneralService generalService;


    @PostMapping("/update")
    public void update(
            @RequestParam("editDetail") CellUpdateReqVO reqVO
    ) {

        // 对于更新请求，转发至一般 Service 进行处理
        generalService.editCell(reqVO);


    }


}
