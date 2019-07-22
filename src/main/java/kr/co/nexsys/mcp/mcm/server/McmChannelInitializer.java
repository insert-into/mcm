package kr.co.nexsys.mcp.mcm.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import kr.co.nexsys.mcp.mcm.server.handler.TestServiceHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class McmChannelInitializer extends ChannelInitializer<SocketChannel> {
  private final TestServiceHandler testServiceHandler;
  @Override
  protected void initChannel(SocketChannel socketChannel) {
    ChannelPipeline pipeline = socketChannel.pipeline();
    pipeline.addLast(testServiceHandler);
  }
}
