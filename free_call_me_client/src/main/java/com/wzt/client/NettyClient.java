package com.wzt.client;

import com.wzt.handler.FirstClientHandler;
import com.wzt.utils.FreeByteBufUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 文泽天
 */
public class NettyClient {
    static int MAX_RETRY = 10;

    public static void main(String[] args) {
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new FirstClientHandler());
                    }
                });
        connect(bootstrap, "127.0.0.1", 8000, new AtomicInteger(MAX_RETRY));

    }

    private static void connect(Bootstrap bootstrap, String host, int port, AtomicInteger retry) {
        bootstrap.connect(host, port)
                .addListener(future -> {
                    int i = retry.get();
                    if (future.isSuccess()) {
                        System.out.println("链接成功");
                        startClientByAync(((ChannelFuture)future).channel());
                    } else if (i == 0) {
                        System.out.println("重试超过最大次数");
                    } else {
                        int order = (MAX_RETRY - i) + 1;
                        int delay = 1 << order;
                        System.out.println("连接失败，第" + retry + "次重连...");
                        AtomicInteger atomicInteger = new AtomicInteger(i--);
                        bootstrap.config().group().schedule(() -> connect(bootstrap, host, port + 1, atomicInteger), delay, TimeUnit.MINUTES);
                        System.out.println("连接失败重试");
                        System.out.println("连接失败重试成功");
                    }
                });
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
