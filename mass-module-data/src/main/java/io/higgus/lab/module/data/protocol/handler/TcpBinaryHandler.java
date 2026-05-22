package io.higgus.lab.module.data.protocol.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.higgus.lab.module.data.common.constant.DataConstants;
import io.higgus.lab.module.data.common.message.UnifiedMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.ByteOrder;
import java.util.List;

/**
 * TCP 自定义二进制协议处理器
 *
 * <p>帧格式：
 * <pre>
 * ┌─────────┬─────────┬─────────┬─────────┬─────────┬─────────┬─────────┐
 * │ Header  │ Length  │DeviceID │Timestamp│  Type   │  Data   │Checksum │
 * │ 0xAA55  │  2字节  │  4字节  │  8字节  │  1字节  │ N字节   │  1字节  │
 * └─────────┴─────────┴─────────┴─────────┴─────────┴─────────┴─────────┘
 * </pre>
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TcpBinaryHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private final ObjectMapper objectMapper;

    // 协议常量
    private static final short FRAME_HEADER = (short) 0xAA55;
    private static final int FIXED_HEADER_LENGTH = 2 + 2 + 4 + 8 + 1; // 固定部分长度

    @Override
    public DataConstants.ProtocolType getProtocol() {
        return DataConstants.ProtocolType.TCP;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        // 将 ByteBuf 转换为 byte[]
        byte[] data = new byte[msg.readableBytes()];
        msg.readBytes(data);

        try {
            UnifiedMessage message = handle(data);
            // 发送响应
            if (supportResponse()) {
                byte[] response = send(message);
                ctx.writeAndFlush(ctx.alloc().buffer().writeBytes(response));
            }
        } catch (Exception e) {
            log.error("处理 TCP 数据失败: {}", e.getMessage(), e);
        }
    }

    @Override
    public UnifiedMessage handle(byte[] rawData) {
        try {
            BinaryProtocol protocol = BinaryProtocol.parse(rawData);

            return UnifiedMessage.builder()
                    .messageId(generateMessageId())
                    .type(protocol.getType())
                    .deviceId(protocol.getDeviceId())
                    .timestamp(protocol.getTimestamp())
                    .payload(protocol.getPayload())
                    .sourceProtocol(DataConstants.ProtocolType.TCP)
                    .encoding(DataConstants.EncodingType.CUSTOM_BINARY)
                    .processed(false)
                    .build();

        } catch (Exception e) {
            log.error("解析二进制协议失败: {}", e.getMessage());
            return UnifiedMessage.builder()
                    .messageId(generateMessageId())
                    .type(DataConstants.MessageType.COLLECTION_DATA)
                    .deviceId("unknown")
                    .timestamp(System.currentTimeMillis())
                    .payload(rawData)
                    .sourceProtocol(DataConstants.ProtocolType.TCP)
                    .encoding(DataConstants.EncodingType.CUSTOM_BINARY)
                    .processed(false)
                    .processResult("parse error: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public boolean supportResponse() {
        return true;
    }

    @Override
    public byte[] send(UnifiedMessage message) {
        // 构建简单的响应帧
        byte[] data = "OK".getBytes();
        return BinaryProtocol.encode("ACK", System.currentTimeMillis(), (byte) 0, data);
    }

    private String generateMessageId() {
        return String.format("%d-%s",
                System.currentTimeMillis(),
                java.util.UUID.randomUUID().toString().substring(0, 8));
    }

    /**
     * 二进制协议解析器
     */
    @lombok.Data
    @lombok.AllArgsConstructor
    private static class BinaryProtocol {
        private String deviceId;
        private long timestamp;
        private DataConstants.MessageType type;
        private byte[] payload;

        /**
         * 解析二进制数据
         */
        public static BinaryProtocol parse(byte[] data) throws Exception {
            if (data.length < FIXED_HEADER_LENGTH + 1) {
                throw new IllegalArgumentException("数据长度不足");
            }

            int offset = 0;

            // 解析帧头
            short header = bytesToShort(data, offset);
            offset += 2;
            if (header != FRAME_HEADER) {
                throw new IllegalArgumentException("帧头错误: 0x" + Integer.toHexString(header));
            }

            // 解析长度
            short length = bytesToShort(data, offset);
            offset += 2;

            // 解析设备ID
            byte[] deviceIdBytes = new byte[4];
            System.arraycopy(data, offset, deviceIdBytes, 0, 4);
            String deviceId = new String(deviceIdBytes).trim();
            offset += 4;

            // 解析时间戳
            long timestamp = bytesToLong(data, offset);
            offset += 8;

            // 解析类型
            byte typeByte = data[offset];
            offset += 1;

            // 解析数据
            int dataLength = length - FIXED_HEADER_LENGTH;
            byte[] payload = new byte[dataLength];
            if (dataLength > 0) {
                System.arraycopy(data, offset, payload, 0, dataLength);
            }
            offset += dataLength;

            // 校验校验和
            byte checksum = data[offset];
            byte calculatedChecksum = calculateChecksum(data, offset);
            if (checksum != calculatedChecksum) {
                throw new IllegalArgumentException("校验和不匹配");
            }

            // 解析消息类型
            DataConstants.MessageType[] types = DataConstants.MessageType.values();
            DataConstants.MessageType type = typeByte < types.length ? types[typeByte] : DataConstants.MessageType.COLLECTION_DATA;

            return new BinaryProtocol(deviceId, timestamp, type, payload);
        }

        /**
         * 编码为二进制
         */
        public static byte[] encode(String deviceId, long timestamp, byte type, byte[] payload) {
            int dataLength = payload != null ? payload.length : 0;
            int totalLength = FIXED_HEADER_LENGTH + dataLength + 1;

            byte[] result = new byte[totalLength];
            int offset = 0;

            // 写入帧头
            shortToBytes(FRAME_HEADER, result, offset);
            offset += 2;

            // 写入长度
            shortToBytes((short) totalLength, result, offset);
            offset += 2;

            // 写入设备ID
            byte[] deviceIdBytes = new byte[4];
            byte[] idBytes = deviceId.getBytes();
            System.arraycopy(idBytes, 0, deviceIdBytes, 0, Math.min(idBytes.length, 4));
            System.arraycopy(deviceIdBytes, 0, result, offset, 4);
            offset += 4;

            // 写入时间戳
            longToBytes(timestamp, result, offset);
            offset += 8;

            // 写入类型
            result[offset] = type;
            offset += 1;

            // 写入数据
            if (dataLength > 0) {
                System.arraycopy(payload, 0, result, offset, dataLength);
                offset += dataLength;
            }

            // 写入校验和
            result[offset] = calculateChecksum(result, offset);

            return result;
        }

        private static short bytesToShort(byte[] data, int offset) {
            return (short) ((data[offset] & 0xFF) << 8 | (data[offset + 1] & 0xFF));
        }

        private static void shortToBytes(short value, byte[] data, int offset) {
            data[offset] = (byte) (value >> 8);
            data[offset + 1] = (byte) value;
        }

        private static long bytesToLong(byte[] data, int offset) {
            return ((long) (data[offset] & 0xFF) << 56) |
                    ((long) (data[offset + 1] & 0xFF) << 48) |
                    ((long) (data[offset + 2] & 0xFF) << 40) |
                    ((long) (data[offset + 3] & 0xFF) << 32) |
                    ((long) (data[offset + 4] & 0xFF) << 24) |
                    ((long) (data[offset + 5] & 0xFF) << 16) |
                    ((long) (data[offset + 6] & 0xFF) << 8) |
                    ((long) (data[offset + 7] & 0xFF));
        }

        private static void longToBytes(long value, byte[] data, int offset) {
            data[offset] = (byte) (value >> 56);
            data[offset + 1] = (byte) (value >> 48);
            data[offset + 2] = (byte) (value >> 40);
            data[offset + 3] = (byte) (value >> 32);
            data[offset + 4] = (byte) (value >> 24);
            data[offset + 5] = (byte) (value >> 16);
            data[offset + 6] = (byte) (value >> 8);
            data[offset + 7] = (byte) value;
        }

        private static byte calculateChecksum(byte[] data, int length) {
            byte sum = 0;
            for (int i = 0; i < length; i++) {
                sum += data[i];
            }
            return sum;
        }
    }
}
