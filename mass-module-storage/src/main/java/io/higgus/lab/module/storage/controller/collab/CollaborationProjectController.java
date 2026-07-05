package io.higgus.lab.module.storage.controller.collab;

import io.higgus.lab.mass.framework.common.pojo.CommonResult;
import io.higgus.lab.module.storage.service.collab.impl.CollaborationProjectServiceImpl;
import io.higgus.lab.module.storage.controller.collab.vo.collab.project.CollaborationProjectCreateReqVO;
import io.higgus.lab.module.storage.controller.collab.vo.collab.project.CollaborationProjectRespVO;
import io.higgus.lab.module.storage.controller.collab.vo.collab.project.CollaborationProjectUpdateReqVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "协作项目/渠道管理")
@Slf4j
@RestController
@RequestMapping("/collaboration/item")
public class CollaborationProjectController {

    @Resource
    private CollaborationProjectServiceImpl collaborationItemService;

    @Operation(summary = "创建项目/渠道")
    @PostMapping("/create")
    public CommonResult<Long> createItem(@Valid @RequestBody CollaborationProjectCreateReqVO createReqVO,
                                         @Parameter(description = "创建者") @RequestParam(value = "creator", defaultValue = "") String creator) {
        log.info("创建项目/渠道, name={}, type={}, creator={}", createReqVO.getName(), createReqVO.getType(), creator);
        return CommonResult.success(collaborationItemService.createItem(createReqVO, creator));
    }

    @Operation(summary = "更新项目/渠道")
    @PutMapping("/update")
    public CommonResult<Boolean> updateItem(@Valid @RequestBody CollaborationProjectUpdateReqVO updateReqVO,
                                            @Parameter(description = "更新者") @RequestParam(value = "updater", defaultValue = "") String updater) {
        log.info("更新项目/渠道, id={}, updater={}", updateReqVO.getId(), updater);
        collaborationItemService.updateItem(updateReqVO, updater);
        return CommonResult.success(true);
    }

    @Operation(summary = "删除项目/渠道")
    @DeleteMapping("/delete")
    public CommonResult<Boolean> deleteItem(@Parameter(description = "项目ID") @RequestParam("id") Long id) {
        log.info("删除项目/渠道, id={}", id);
        collaborationItemService.deleteItem(id);
        return CommonResult.success(true);
    }

    @Operation(summary = "获取项目/渠道")
    @GetMapping("/get")
    public CommonResult<CollaborationProjectRespVO> getItem(@Parameter(description = "项目ID") @RequestParam("id") Long id) {
        return CommonResult.success(collaborationItemService.getItem(id));
    }

    @Operation(summary = "获取项目/渠道列表")
    @GetMapping("/list")
    public CommonResult<List<CollaborationProjectRespVO>> getItemList() {
        return CommonResult.success(collaborationItemService.getItemList());
    }

    @Operation(summary = "获取空间下的项目/渠道列表")
    @GetMapping("/list-by-space")
    public CommonResult<List<CollaborationProjectRespVO>> getItemListBySpaceId(@Parameter(description = "空间ID") @RequestParam("spaceId") Long spaceId) {
        return CommonResult.success(collaborationItemService.getItemListBySpaceId(spaceId));
    }
}
