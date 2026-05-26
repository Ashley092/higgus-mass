package io.higgus.lab.module.storage.controller;

import io.higgus.lab.mass.framework.common.pojo.CommonResult;
import io.higgus.lab.module.storage.service.impl.CollaborationSpaceServiceImpl;
import io.higgus.lab.module.storage.controller.vo.CollaborationSpaceCreateReqVO;
import io.higgus.lab.module.storage.controller.vo.CollaborationSpaceRespVO;
import io.higgus.lab.module.storage.controller.vo.CollaborationSpaceUpdateReqVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "协作空间管理")
@Slf4j
@RestController
@RequestMapping("/collaboration/space")
public class CollaborationSpaceController {

    @Resource
    private CollaborationSpaceServiceImpl collaborationSpaceService;

    @Operation(summary = "创建协作空间")
    @PostMapping("/create")
    public CommonResult<Long> createSpace(@Valid @RequestBody CollaborationSpaceCreateReqVO createReqVO,
                                          @Parameter(description = "创建者") @RequestParam(value = "creator", defaultValue = "") String creator) {
        log.info("创建协作空间, name={}, creator={}", createReqVO.getName(), creator);
        return CommonResult.success(collaborationSpaceService.createSpace(createReqVO, creator));
    }

    @Operation(summary = "更新协作空间")
    @PutMapping("/update")
    public CommonResult<Boolean> updateSpace(@Valid @RequestBody CollaborationSpaceUpdateReqVO updateReqVO,
                                             @Parameter(description = "更新者") @RequestParam(value = "updater", defaultValue = "") String updater) {
        log.info("更新协作空间, id={}, updater={}", updateReqVO.getId(), updater);
        collaborationSpaceService.updateSpace(updateReqVO, updater);
        return CommonResult.success(true);
    }

    @Operation(summary = "删除协作空间")
    @DeleteMapping("/delete")
    public CommonResult<Boolean> deleteSpace(@Parameter(description = "空间ID") @RequestParam("id") Long id) {
        log.info("删除协作空间, id={}", id);
        collaborationSpaceService.deleteSpace(id);
        return CommonResult.success(true);
    }

    @Operation(summary = "获取协作空间")
    @GetMapping("/get")
    public CommonResult<CollaborationSpaceRespVO> getSpace(@Parameter(description = "空间ID") @RequestParam("id") Long id) {
        return CommonResult.success(collaborationSpaceService.getSpace(id));
    }

    @Operation(summary = "获取协作空间列表")
    @GetMapping("/list")
    public CommonResult<List<CollaborationSpaceRespVO>> getSpaceList() {
        return CommonResult.success(collaborationSpaceService.getSpaceList());
    }
}
