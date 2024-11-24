package controllers

import com.redis._
import com.typesafe.config.{Config, ConfigFactory}
import play.api.libs.json._
import play.api.mvc._

import javax.inject.Inject

class DBInterfaceController @Inject()(cc: ControllerComponents)
  extends AbstractController(cc) {

  private val config: Config = ConfigFactory.load()

  private val authKey = config.getString("redis.authKey")

  private val redisHost = "ikkai.redis.cache.windows.net"
  private val redisPort = 6379 // Redisの標準ポートに修正
  private val redisClient = new RedisClient(redisHost, redisPort)

  // コンストラクタ内で認証を設定
  redisClient.auth(authKey)

  def fetchDB(): Action[AnyContent] = Action {
    // Redisからデータを取得
    val storedDataOption = redisClient.get[String]("wordsList")  // "wordsList"というキーでデータを取得

    // データが存在するか確認し、存在する場合はJSON形式で返す
    storedDataOption match {
      case Some(data) =>
        // Redisから取得したデータ（文字列）をJSON形式に変換
        val jsonResponse: JsValue = Json.obj(
          "status" -> "success",
          "data" -> data
        )
        Ok(jsonResponse)

      case None =>
        // データが存在しない場合
        val errorResponse: JsValue = Json.obj(
          "status" -> "error",
          "message" -> "No data found in Redis."
        )
        NotFound(errorResponse)
    }
  }

  def postDB(): Action[JsValue] = Action(parse.json) { request =>
    val wordsArray = (request.body \ "name").as[Seq[String]]

    // 配列の最初の値をキーとして保存→IDやら識別やら
    redisClient.set(wordsArray.head, wordsArray.mkString(", ")) // 配列をカンマ区切りの文字列として保存

    // レスポンスを返す
    val json: JsValue = Json.obj(
      "status" -> "success",
      "message" -> "Words have been saved to Redis.",
      "words" -> wordsArray
    )
    Ok(json)
  }
}
