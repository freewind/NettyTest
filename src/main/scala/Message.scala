import org.json4s.native.Serialization

object Formats {
  implicit val formats = org.json4s.DefaultFormats
}

import Formats.formats

case class Message(num: Int) {
  def toMessage = Serialization.write(this) + "\n"
}

object Message {
  def apply(line: String) = Serialization.read[Message](line)
}

object X extends App {
  val msg = Message(11)
  println(msg.toMessage)
}
