package io.higgus.lab.module.data.gateway.controller;

import io.higgus.lab.module.data.common.constant.DataConstants;
import io.higgus.lab.module.data.gateway.ProtocolGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * HTTP 数据接入控制器
 *
 * <p>提供 HTTP 接口供设备上传数据
 */
@Slf4j
@RestController
@RequestMapping("/data")
@RequiredArgsConstructor
public class DataGatewayController {

    private final ProtocolGateway protocolGateway;

    /**
     * 接收设备数据（通用接口）
     */
    @PostMapping("/collect")
    public ResponseEntity<Map<String, Object>> collectData(@RequestBody byte[] data) {
        log.debug("收到采集数据: length={}", data.length);

        try {
            protocolGateway.handleAndPublish(DataConstants.ProtocolType.HTTP, data);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("处理采集数据失败: {}", e.getMessage(), e);

            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 接收设备数据（JSON 格式）
     */
    @PostMapping("/collect/json")
    public ResponseEntity<Map<String, Object>> collectDataJson(@RequestBody Map<String, Object> data) {
        log.debug("收到 JSON 采集数据: {}", data);

        try {
            String jsonStr = new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(data);
            protocolGateway.handleAndPublish(DataConstants.ProtocolType.HTTP, jsonStr);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("处理 JSON 采集数据失败: {}", e.getMessage(), e);

            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 接收状态变更数据
     */
    @PostMapping("/status")
    public ResponseEntity<Map<String, Object>> statusChange(@RequestBody Map<String, Object> data) {
        log.info("收到状态变更: {}", data);

        try {
            String jsonStr = new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(data);
            var message = protocolGateway.handle(DataConstants.ProtocolType.HTTP, jsonStr);
            protocolGateway.publishMessage(message);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("处理状态变更失败: {}", e.getMessage(), e);

            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 接收告警数据
     */
    @PostMapping("/alarm")
    public ResponseEntity<Map<String, Object>> alarm(@RequestBody Map<String, Object> data) {
        log.warn("收到告警: {}", data);

        try {
            String jsonStr = new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(data);
            var message = protocolGateway.handle(DataConstants.ProtocolType.HTTP, jsonStr);
            protocolGateway.publishMessage(message);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("处理告警失败: {}", e.getMessage(), e);

            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 心跳检测
     */
    @GetMapping("/heartbeat")
    public ResponseEntity<Map<String, Object>> heartbeat(@RequestParam(required = false) String deviceId) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("timestamp", System.currentTimeMillis());
        response.put("deviceId", deviceId);
        response.put("status", "online");

        return ResponseEntity.ok(response);
    }

    /**
     * 获取支持的协议列表
     */
    @GetMapping("/protocols")
    public ResponseEntity<Map<String, Object>> getSupportedProtocols() {
        Map<String, Object> response = new HashMap<>();
        response.put("protocols", protocolGateway.getSupportedProtocols());
        response.put("timestamp", System.currentTimeMillis());

        return ResponseEntity.ok(response);
    }
}
