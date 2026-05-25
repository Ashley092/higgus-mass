package io.higgus.lab.module.storage.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "文件上传结果")
public class UploadResultVO {

    @Schema(description = "内容ID")
    private Long contentId;

    @Schema(description = "是否秒传成功（文件已存在）")
    private Boolean skipUpload;

    @Schema(description = "存储键")
    private String storageKey;

    @Schema(description = "文件MD5")
    private String fileMd5;

    public static UploadResultVO skip(Long contentId, String storageKey, String fileMd5) {
        return UploadResultVO.builder()
                .contentId(contentId)
                .skipUpload(true)
                .storageKey(storageKey)
                .fileMd5(fileMd5)
                .build();
    }

    public static UploadResultVO newUpload(Long contentId, String storageKey, String fileMd5) {
        return UploadResultVO.builder()
                .contentId(contentId)
                .skipUpload(false)
                .storageKey(storageKey)
                .fileMd5(fileMd5)
                .build();
    }
}
