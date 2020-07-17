package com.sincosmos.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpFileServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    public HttpFileServerHandler(String defaultUrl) {

    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
        if(!msg.decoderResult().isSuccess()){
            log.error("Bad Request");
            return;
        }

        if(msg.method() != HttpMethod.GET){
            log.error("Bad Request");
            return;
        }

        final String uri = msg.uri();
        final String path = "";

        if(path == null){
            log.error("Bad Request");
            return;
        }
    }
}
