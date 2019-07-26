package kr.co.nexsys.mcp.mcm.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import kr.co.nexsys.mcp.mcm.server.handler.McmServerHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Component
public class McmServer  {

  private final ServerProperties serverProperties;
  private final 
  ///EchoTestServerHandler
  McmServerHandler
  serviceHandler;

  public void start() {
		EventLoopGroup bossGroup = new NioEventLoopGroup(serverProperties.getBossThreadCount());
		EventLoopGroup workerGroup = new NioEventLoopGroup(serverProperties.getWorkerThreadCount());

		try {
			ServerBootstrap serverBootStrap = new ServerBootstrap();
			serverBootStrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
					.handler(new LoggingHandler(LogLevel.TRACE)).childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch)
						 throws Exception
						{
							ChannelPipeline pipeline = ch.pipeline();
							pipeline.addLast(serviceHandler);
						}
					});

			ChannelFuture channelFuture = serverBootStrap.bind(serverProperties.getPort()).sync();
			channelFuture.channel().closeFuture().sync();

		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
}