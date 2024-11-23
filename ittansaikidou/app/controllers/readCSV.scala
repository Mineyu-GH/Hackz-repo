package controllers

import javax.inject._
import play.api.mvc._
import play.api.Environment
import scala.io.Source
import scala.collection.mutable.ArrayBuffer




@Singleton
class readCSV @Inject()(
                         val controllerComponents: ControllerComponents,
                         env: Environment
                       ) extends BaseController {

  def printCSV = Action {
    val csvFile = env.getFile("public/db/data.csv")

    try {
      // CSVファイルの内容を取得
      val csvContent = Source.fromFile(csvFile).getLines()

      // カンマで分割してSetに格納
      val wordSet: Set[String] = csvContent.flatMap(_.split(",").map(_.trim)).toSet

      // デバッグ用の出力
      println(wordSet)

      // レスポンスとして返す（任意）
      Ok(wordSet.mkString("\n"))
    } catch {
      case e: Exception => InternalServerError(s"Error reading CSV file: ${e.getMessage}")
    }
  }
}