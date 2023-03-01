package com.wzt.utils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.nio.charset.StandardCharsets;

/**
 * @author 文泽天
 */
public class FreeByteBufUtil {
    public static ByteBuf getByteBuf(ChannelHandlerContext ctx, String msg){
        //get byte abstract byteBuf
        ByteBuf buffer = ctx.alloc().buffer();

        //ready data,specify String code to UTF-8
        byte[] bytes = msg.getBytes(StandardCharsets.UTF_8);

        //fill data to byteBuf
        buffer.writeBytes(bytes);
        return buffer;
    }
    public static ByteBuf getByteBuf(Channel ctx, String msg){
        //get byte abstract byteBuf
        ByteBuf buffer = ctx.alloc().buffer();

        //ready data,specify String code to UTF-8
        byte[] bytes = msg.getBytes(StandardCharsets.UTF_8);

        //fill data to byteBuf
        buffer.writeBytes(bytes);
        return buffer;
    }
}
