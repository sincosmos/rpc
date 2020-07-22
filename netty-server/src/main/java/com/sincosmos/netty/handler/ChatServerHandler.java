package com.sincosmos.netty.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChatServerHandler extends SimpleChannelInboundHandler<String> {
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * Eventually invoked if data is read from the Channel.
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel channel = ctx.channel();

        channelGroup.forEach(ch->{

            log.info("data in channel --> local: "
                    + ch.localAddress()
                    + " | remote: " + ch.remoteAddress());

            if(ch == channel){
                log.info("data from self peer: " + msg);
                ch.writeAndFlush("message from self: " + ch.remoteAddress() + " --> " + msg + "\n");
            }else{
                log.info("data from friend peer: " + msg);
                ch.writeAndFlush("message from friend: " + ch.remoteAddress() + " --> " + msg + "\n");
            }

        });
    }

    /**
     * Called when this ChannelHandler is added to a ChannelPipeline
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception{
        Channel channel = ctx.channel();
        log.info("new ServerSocketChannel created --> local: "
                + channel.localAddress()
                + " | remote: " + channel.remoteAddress());
        //notify all other online client
        channelGroup.writeAndFlush("new ServerSocketChannel created --> local: "
                + channel.localAddress()
                + " | remote: " + channel.remoteAddress() + "\n");

        //add this newly online client channel
        channelGroup.add(channel);
    }

    /**
     * Called when this ChannelHandler is removed from a ChannelPipeline
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception{
        Channel channel = ctx.channel();
        log.info("ServerSocketChannel closed --> local: "
                + channel.localAddress()
                + " | remote: " + channel.remoteAddress());
        //notify all other online client
        channelGroup.writeAndFlush("ServerSocketChannel closed --> local: "
                + channel.localAddress()
                + " | remote: " + channel.remoteAddress() + "\n");
    }

    /**
     * Called if an error occurs in the ChannelPipeline during processing
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception{
        log.info("Client " + ctx.channel().remoteAddress() + " connected to server");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception{
        log.info("Client " + ctx.channel().remoteAddress() + " disconnected from server");
    }
}
