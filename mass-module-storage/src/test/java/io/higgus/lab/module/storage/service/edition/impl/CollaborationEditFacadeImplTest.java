package io.higgus.lab.module.storage.service.edition.impl;

import io.higgus.lab.module.storage.service.edition.dto.EditionExcelSaveLogDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

class CollaborationEditFacadeImplTest {

    private final CollaborationEditFacadeImpl facade = new CollaborationEditFacadeImpl();

    @Test
    @DisplayName("generateIdempotentKey: 相同内容+同分钟内 应产生相同 key")
    void generateIdempotentKey_sameMinute_shouldBeStable() throws Exception {
        EditionExcelSaveLogDto dto = new EditionExcelSaveLogDto();
        dto.setContentId("42");
        dto.setRowIndex(1);
        dto.setColIndex(2);
        dto.setNewValue("hello");
        dto.setCreateTime(LocalDateTime.of(2026, 7, 8, 18, 30));

        Method m = CollaborationEditFacadeImpl.class
                .getDeclaredMethod("generateIdempotentKey",
                        io.higgus.lab.module.storage.service.edition.dto.EditionExcelSaveLogDto.class);
        m.setAccessible(true);

        String k1 = (String) m.invoke(facade, dto);
        String k2 = (String) m.invoke(facade, dto);

        assertThat(k1).isEqualTo(k2).startsWith("cell:");
    }

    @Test
    @DisplayName("generateIdempotentKey: 跨分钟 应产生不同 key")
    void generateIdempotentKey_differentMinute_shouldDiffer() throws Exception {
        EditionExcelSaveLogDto a = baseDto(LocalDateTime.of(2026, 7, 8, 18, 30));
        EditionExcelSaveLogDto b = baseDto(LocalDateTime.of(2026, 7, 8, 18, 31));

        Method m = CollaborationEditFacadeImpl.class
                .getDeclaredMethod("generateIdempotentKey",
                        io.higgus.lab.module.storage.service.edition.dto.EditionExcelSaveLogDto.class);
        m.setAccessible(true);

        assertThat((String) m.invoke(facade, a))
                .isNotEqualTo((String) m.invoke(facade, b));
    }

    private EditionExcelSaveLogDto baseDto(LocalDateTime t) {
        EditionExcelSaveLogDto dto = new EditionExcelSaveLogDto();
        dto.setContentId("42");
        dto.setRowIndex(0);
        dto.setColIndex(0);
        dto.setNewValue("v");
        dto.setCreateTime(t);
        return dto;
    }
}