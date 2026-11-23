package com.sqx.modules.utils;

import org.bouncycastle.util.Arrays;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @description：TODO 处理 任意数量 AudioInputStream 的混合类
 * @author： liuduo
 * @create： 2025/9/8 17:30
 */
public class NWayMixedAudioInputStream extends AudioInputStream {
    private final List<AudioInputStream> streams;
    private final AudioFormat format;
    private final byte[] readBuffer;
//    private final byte[] mixBuffer;

    public NWayMixedAudioInputStream(List<AudioInputStream> streams, AudioFormat format) {
        super(new ByteArrayInputStream(new byte[0]), format, AudioSystem.NOT_SPECIFIED);
        this.streams = new ArrayList<>(streams);
        this.format = format;
        int bufferSize = 1024;
        this.readBuffer = new byte[bufferSize];
//        this.mixBuffer = new byte[bufferSize];
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        // 重置混合缓冲区(清空输出缓冲区)
        Arrays.fill(b, off, off + len, (byte) 0);

        int bytesRead = -1;
        boolean anyStreamActive = false;

        // 循环读取所有音频输入流(从每个流中读取并混合)
        for (AudioInputStream stream : streams) {
            int read = stream.read(readBuffer, 0, Math.min(len, readBuffer.length));
            if (read > 0) {
                anyStreamActive = true;
                if (bytesRead == -1) bytesRead = read;
                mixByteArrays(b, off, readBuffer, read);
            }
        }
        // 如果没有流处于活动状态，则返回-1表示没有更多数据可读（说明都结束了）
//        if (!anyStreamActive) return -1;
        // 混合后的数据写入输出缓冲区并返回实际读取的字节数(将混合后的数据复制到输出缓冲区)
//        System.arraycopy(b, 0, b, off, bytesRead);
        return anyStreamActive ? bytesRead : -1;
    }

    // 混合两个字节数组（16位PCM示例)混合两个 PCM 16位小端音频片段（平均法防爆音）
    private void mixByteArrays(byte[] target, int targetOffset, byte[] source, int len) {
        for (int i = 0; i < len; i += 2) {
            short t = (short) ((target[targetOffset + i] & 0xFF) | (target[targetOffset + i + 1] << 8));
            short s = (short) ((source[i] & 0xFF) | (source[i + 1] << 8));
            // 方法1：简单平均（适合多路混合）
            float mixed = (t + s) / 2.0f;
            // 方法2：带衰减的叠加（保留原始音量感）
            // float mixed = (t + s) * 0.7f;
//            mixed = Math.max(-32768,Math.min(32767,mixed));
            short result = (short) Math.max(-32768, Math.min(32767, mixed));
            target[targetOffset + i]     = (byte) (result & 0xFF);
            target[targetOffset + i + 1] = (byte) ((result >> 8) & 0xFF);
        }
    }


}
