package services

import scala.io.{Codec, Source}
import scala.util.Using

object readCSV {
  private var lib: Set[String] = Set.empty
  private var csv1 = "public/db/goihyo02.csv"

  def readCSV(): Unit = {
    val csvFile = new java.io.File(csv1)
    // val csvContent = Source.fromFile(csvFile).getLines()
    // Usingを使ってファイルを安全に読み込む
    val csvContent: Set[String] = Using(Source.fromFile(csvFile)(Codec.UTF8)) { source =>
      source.getLines()
        .flatMap(_.split(",").map(_.trim)) // カンマ区切りで分割してトリム
        .toSet // Setに変換
    } match {
      case scala.util.Success(content) => content // 成功時
      case scala.util.Failure(exception) =>
        println(s"Error reading CSV file: ${exception.getMessage}") // エラー時のログ出力
        Set.empty[String] // エラー時は空のセットを返す
    }
    lib = csvContent.flatMap(_.split(",").map(_.trim).toSet)

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
