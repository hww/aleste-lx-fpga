
package aleste.utils

import java.nio.file.{Files, Paths}
import spinal.core._

object BinaryTools {
  def readBinFile(path: String): Array[Byte] = {
    Files.readAllBytes(Paths.get(path))
  }

  def loadToMem(path: String): List[Bits] = {
    readBinFile(path).map(b => B(b & 0xFF)).toList
  }
}