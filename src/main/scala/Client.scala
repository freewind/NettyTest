import java.nio.charset.Charset

import io.netty.bootstrap.Bootstrap
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.channel.{ChannelOption, ChannelInitializer, ChannelHandlerContext, ChannelHandlerAdapter}
import io.netty.handler.codec.LineBasedFrameDecoder
import io.netty.handler.codec.string.{StringEncoder, StringDecoder}

object Client extends App {

  val workerGroup = new NioEventLoopGroup(100)
  val bootstrap = new Bootstrap()
  bootstrap.group(workerGroup)
  bootstrap.channel(classOf[NioSocketChannel])
  bootstrap.option(ChannelOption.SO_KEEPALIVE.asInstanceOf[ChannelOption[Any]], true)
  bootstrap.handler(MyChannelInitializer)
  bootstrap.connect("127.0.0.1", 8888).sync()
}

class MyChannelHandler extends ChannelHandlerAdapter {

  private val clientId = System.currentTimeMillis()

  override def channelRead(ctx: ChannelHandlerContext, msg: Any): Unit = msg match {
    case line: String => handleMessage(ctx, Message(line))
    case _ => println("### unknown message type: " + msg)
  }

  var lastNum: Option[Int] = None

  private def handleMessage(ctx: ChannelHandlerContext, msg: Message): Unit = {
    lastNum.foreach { n =>
      if (msg.num != n + 1) {
        println("!!! This msg num: " + msg.num + ", pre num: " + n)
      }
    }
    println(s"<client $clientId>: " + msg.num)
    lastNum = Some(msg.num)
    ctx.writeAndFlush(Request(clientId, msg.num).toMessage)
  }

  override def exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
    cause.printStackTrace()
  }
}

object MyChannelInitializer extends ChannelInitializer[SocketChannel] {
  override def initChannel(ch: SocketChannel) {
    ch.pipeline().addLast(
      new LineBasedFrameDecoder(Int.MaxValue),
      new StringDecoder(Charset.forName("UTF-8")),
      new StringEncoder(Charset.forName("UTF-8")),
      new MyChannelHandler())
  }
}



