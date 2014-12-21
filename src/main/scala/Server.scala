import java.util.concurrent.atomic.AtomicInteger

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel._
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.codec.LineBasedFrameDecoder
import io.netty.handler.codec.string.{StringEncoder, StringDecoder}
import java.nio.charset.Charset

object Server extends App {

  private val bossGroup = new NioEventLoopGroup()
  // Set the thread count to 1, so there is only one thread to send messages to all clients
  // which can keep the order (but with bad performance)
  private val workerGroup = new NioEventLoopGroup(1)
  private val bootstrap = new ServerBootstrap()
  bootstrap.group(bossGroup, workerGroup)
    .channel(classOf[NioServerSocketChannel])
    .childHandler(ChildHandler)
    .option(ChannelOption.SO_BACKLOG.asInstanceOf[ChannelOption[Any]], 128)
    .childOption(ChannelOption.SO_KEEPALIVE.asInstanceOf[ChannelOption[Any]], true)

  private val channel: ChannelFuture = bootstrap.bind(8888)
  channel.sync()

  object ChildHandler extends ChannelInitializer[SocketChannel] {
    override def initChannel(channel: SocketChannel) {
      channel.pipeline().addLast(
        new LineBasedFrameDecoder(Int.MaxValue),
        new StringDecoder(Charset.forName("UTF-8")),
        new StringEncoder(Charset.forName("UTF-8")),
        new ServerHandler())
    }
  }

}

object ServerHandler {
  val id = new AtomicInteger(0)
  var list: List[ChannelHandlerContext] = Nil
}

object Lock

class ServerHandler extends ChannelHandlerAdapter {

  import ServerHandler._

  override def channelActive(ctx: ChannelHandlerContext): Unit = {
    list = ctx :: list
    println("### new client active")
  }

  override def channelInactive(ctx: ChannelHandlerContext): Unit = {
    list = list.filterNot(_ == ctx)
  }

  override def channelRead(ctx: ChannelHandlerContext, msg: scala.Any): Unit = msg match {
    case line: String => Lock.synchronized {
      println("### server read line: " + line)
      val newMsg = Message(num = id.incrementAndGet())
//      Thread.sleep(500)
      list.foreach { ccc =>
        println(s"### going to send to client ${ccc.hashCode()} with message: " + newMsg.toMessage)
        ccc.writeAndFlush(newMsg.toMessage)
      }
    }
    case _ => println("Unknown message type: " + msg)
  }
}
