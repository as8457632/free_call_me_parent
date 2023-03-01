package com.wzt.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class FirstClientHandler extends ChannelInboundHandlerAdapter {
    /**
     * 覆盖 channelActive方法,该方法会在客户端连接建立成功之后被调用。
     * @param ctx
     * @throws Exception
     */
//    @Override
//    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        System.out.println(new Date()+"客户端写出数据:");
////        String endFlag="";
////        do {
////            Scanner scanner = new Scanner(System.in);
////            String msg = scanner.nextLine();
////            ByteBuf byteBuf = getByteBuf(ctx,msg);
////            ctx.channel().writeAndFlush(byteBuf);
////            endFlag = msg;
////        }while (!"end".equals(endFlag));
//    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println("客户端接收到返回值:"+byteBuf.toString(StandardCharsets.UTF_8));
    }

    private ByteBuf getByteBuf(ChannelHandlerContext ctx,String msg){
        //get byte abstract byteBuf
        ByteBuf buffer = ctx.alloc().buffer();

        //ready data,specify String code to UTF-8
        byte[] bytes = msg.getBytes(Charset.forName("utf-8"));

        //fill data to byteBuf
        buffer.writeBytes(bytes);
        return buffer;
    }
}
