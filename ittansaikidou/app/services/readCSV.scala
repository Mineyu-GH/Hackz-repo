package services

import scala.io.Source

object CSVReader {
  private var 語録辞書: Set[String] = Set.empty
  private var シーエスブイファイルパス = "public/db/data.csv"

  def readCSV(): Unit = {
    val csvFile = new java.io.File(シーエスブイファイルパス)
    val csvContent = Source.fromFile(csvFile).getLines()
    語録辞書 = csvContent.flatMap(_.split(",").map(_.trim)).toSet
    println(語録辞書)
  }

  def isExistWord(word: String): Boolean = {

    語録辞書.contains(word)
  }
}
