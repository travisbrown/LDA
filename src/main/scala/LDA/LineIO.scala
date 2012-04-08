package LDA

import java.io.{ BufferedWriter, File, FileWriter, IOException }
import scala.io.Source

abstract class LineIO(path: String) {
  protected val file = new File(path)
}

class LineReader(path: String) extends LineIO(path) {
  def readlines() = try {
    val in = Source.fromFile(this.file)
    val lines = in.getLines.toArray
    in.close()
    lines
  } catch { case e: IOException =>
    println("Exception: " + e)
    Array(null.asInstanceOf[String])
  }
}

class LineWriter(path: String, append: Boolean) extends LineIO(path) {
  def send(lines: Array[String]) {
    try {
      val out = new BufferedWriter(new FileWriter(this.file, append))
      lines.foreach(line => out.write(line + "\n"))
      out.close()
    } catch { case e: IOException =>
      println("Exception: " + e)
    }
  }
}

