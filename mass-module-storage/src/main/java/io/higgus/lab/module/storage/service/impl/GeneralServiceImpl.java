package io.higgus.lab.module.storage.service.impl;

import io.higgus.lab.module.storage.controller.vo.CellUpdateReqVO;
import io.higgus.lab.module.storage.service.CollabMessagingService;
import io.higgus.lab.module.storage.service.GeneralService;
import org.springframework.stereotype.Service;


@Service
public class GeneralServiceImpl implements GeneralService {

    CollabMessagingService mqService;

    public void editCell(CellUpdateReqVO reqVO) {

        // 先不考虑幂等性的逻辑。先写完这个分发
        // 先更新 Redis 内的内容
        mqService.sendRedisUpdateMessage();





    }


}
