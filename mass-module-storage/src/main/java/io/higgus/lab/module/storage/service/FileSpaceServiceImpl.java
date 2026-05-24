package io.higgus.lab.module.storage.service;


import cn.hutool.crypto.SecureUtil;
import io.higgus.lab.module.storage.dal.dataobject.DirectoryDetailDO;
import io.higgus.lab.module.storage.dal.dataobject.FileDetailDO;
import io.higgus.lab.module.storage.dal.mysql.DirectoryDetailMapper;
import io.higgus.lab.module.storage.dal.mysql.FileDetailMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class FileSpaceServiceImpl implements FileSpaceService {

    @Resource
    FileDetailMapper<FileDetailDO> fileMapper;

    @Resource
    DirectoryDetailMapper<DirectoryDetailDO> directoryDetailMapper;

    @Resource
    TransactionTemplate transactionTemplate;

    @Resource
    RedissonClient redissonClient;
    /**
     * 上传一个文件。具体流程：
     * 1. 计算 MD5, 将 MD5 和 parentID 联合索引 查询该文件是否存在，若已存在，直接返回；
     * 2. 若不存在，开锁，写盘。如果中断或异常，返回并清除；如果成功落盘，进行下一步。
     * 3. 开事务，将文件明细写入 文件明细表， 维护 文件夹明细表的实际路径；
     * 4. 关闭事务。
     * 5. 释放锁。
      * @return
     */
    public boolean uploadSingleFile(MultipartFile file, Long userId, Long parentId) throws IOException {
        String fileMd5 = calculateFileMd5(file);
        FileDetailDO fileDO = fileMapper.selectOne(FileDetailDO::getFileMd5, fileMd5,
                                    FileDetailDO::getParentId, parentId);
        if (fileDO != null ) {
            throw new RuntimeException("该文件已存在在该目录下！");
        }

        String lockKey = "lock:file:upload" + fileMd5;
        RLock lock = redissonClient.getLock(lockKey);

        try {
            boolean isLocked = lock.tryLock(5, 30, TimeUnit.SECONDS);
            if (!isLocked) {

                // 说明这个文件正在上传中……
                // 返回说明：请稍后再试。
                throw new RuntimeException("文件正在处理中，请稍后再试");

            }
            // 先写盘。
            doWriteInDisk(file, fileMd5, parentId, userId);

            transactionTemplate



        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {

        }
    }

    /**
     * 写盘操作中，首先查找对应的 parentId 的实际物理映射路径。
     * 根据其实际物理映射路径进行写盘。
     * 在写盘前，先向数据库写入该操作 WAF log。
     * 出现任意异常，擦除并回滚清理僵尸文件。
     * 成功写入时， WAF log 修改 flag = true 两两对应。
     * 如果断电了，通过查询 WAF log 中的 flag 没有被修改成 true，得知有未写入僵尸文件
     * 继续清除
     * @param file
     * @param fileMd5
     * @param parentId
     * @param userId
     */
    public void doWriteInDisk(MultipartFile file, String fileMd5, Long parentId, Long userId)  throws IOException{

        DirectoryDetailDO directoryDO = directoryDetailMapper.selectOne(DirectoryDetailDO::getId, parentId);
        String path = directoryDO.getRealPath();
        // 写 WAF 先


        // 写好 WAF 回来写盘。
        File dest = new File(path);
        file.transferTo(dest);
        // 写盘成功，修改 WAF 内容


        // 任何异常直接回滚


    }


    public String createRealPath() {

    }

    public String calculateFileMd5(MultipartFile file) throws IOException {
        return SecureUtil.md5(file.getInputStream());
    }
}
