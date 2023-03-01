package com.wzt.handler;

import com.wzt.utils.FreeByteBufUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class FirstServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("服务端接收到消息:"+buf.toString(StandardCharsets.UTF_8));
        String endFlag ="";
//        ctx.channel().writeAndFlush(getByteBuf(ctx,"服务端收到你的消息"));
        Scanner scanner = new Scanner(System.in);
        String strMsg = scanner.nextLine();
        ByteBuf byteBuf = FreeByteBufUtil.getByteBuf(ctx, strMsg);
        ctx.channel().writeAndFlush(byteBuf);
    }
    private static ByteBuf getByteBuf(ChannelHandlerContext ctx,String msg){
        ByteBuf buffer = ctx.alloc().buffer();
        byte[] bytes = msg.getBytes(StandardCharsets.UTF_8);
        buffer.writeBytes(bytes);
        return buffer;
    }

    private static void startClientByAync(Channel channel) {
        new Thread(() -> {
            String endFlag = "";
            do {
                Scanner scanner = new Scanner(System.in);
                String msg = scanner.nextLine();
                ByteBuf byteBuf = FreeByteBufUtil.getByteBuf(channel, msg);
                channel.writeAndFlush(byteBuf);
                endFlag = msg;
            } while (!"end".equals(endFlag));
        }).start();
    }

}
