package kr.co.nexsys.mcp.mcm.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@ChannelHandler.Sharable
@Component
public class TestServiceHandler extends ChannelInboundHandlerAdapter {

  @Override
  public void channelActive(ChannelHandlerContext ctx) { // (1)
    ByteBuf time = ctx.alloc().buffer(4); // (2)
    time.writeInt((int) (System.currentTimeMillis() / 1000L + 2208988800L));

    ChannelFuture f = ctx.writeAndFlush(time); // (3)
    f.addListener((future)-> {
      assert f == future;
      ctx.close();
    }); // (4)
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    cause.printStackTrace();
    ctx.close();
  }

}


