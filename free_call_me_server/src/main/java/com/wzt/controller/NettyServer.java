package com.wzt.controller;

import com.wzt.handler.FirstServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyServer {
    public static void main(String[] args) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap
                .group(bossGroup,workGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new FirstServerHandler());
                    }
                });
        bind(serverBootstrap,8000);
    }

    private static void bind(ServerBootstrap serverBootstrap,int port){
        serverBootstrap.bind(port).addListener(future -> {
            if(future.isSuccess()){
                System.out.println("连接成功");
//                startClientByAync(((ChannelFuture)future).channel());
            }else{
                System.out.println("连接失败");
            }

        });
    }


}
