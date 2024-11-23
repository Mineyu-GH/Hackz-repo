package services

import scala.io.Source

object CSVReader {
  private var lib: Set[String] = Set.empty
  private var csv1 = "public/db/goihyo02.csv"

  def readCSV(): Unit = {
    val csvFile = new java.io.File(csv1)
    val csvContent = Source.fromFile(csvFile).getLines()
    lib = csvContent.flatMap(_.split(",").map(_.trim)).toSet

  }

  //ひらがな→カタカナ
  def convertHtoT(hiragana: String): String = {
    if (hiragana == null || hiragana.isEmpty) {
      return ""
    }
    hiragana.map { c =>
      // ひらがな範囲内の文字かをチェック
      if (c >= '\u3041' && c <= '\u3096') {
        // ひらがなをカタカナに変換
        (c + 0x60).toChar
      } else {
        // ひらがなでない文字はそのまま追加
        c
      }
    }.mkString
  }

  def isExistWord(word: String): Boolean = {
    //wordがひらがなの場合はカタカナに変換
    val conv_word = convertHtoT(word)
    lib.contains(conv_word)
  }
}
