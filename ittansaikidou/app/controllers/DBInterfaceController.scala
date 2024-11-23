package controllers

import javax.inject.Inject
import play.api.libs.json._
import play.api.mvc._
import com.redis._
import com.typesafe.config.{Config, ConfigFactory}

import javax.net.ssl.SSLContext

class DBInterfaceController @Inject()(cc: ControllerComponents)
  extends AbstractController(cc) {

  private val config: Config = ConfigFactory.load()

  private val authKey = config.getString("redis.authKey")

  private val redisHost = "ikkai.redis.cache.windows.net"
  private val redisPort = 6379 // Redisの標準ポートに修正
  private val redisClient = new RedisClient(redisHost, redisPort)

  // コンストラクタ内で認証を設定
  redisClient.auth(authKey)

  def fetch(): Action[AnyContent] = Action {
    Ok("This is Interface")
  }

  def postDB(): Action[JsValue] = Action(parse.json) { request =>
    val wordsArray = (request.body \ "words").as[Seq[String]]

    // 配列の内容をRedisに保存
    redisClient.set("wordsList", wordsArray.mkString(", ")) // 配列をカンマ区切りの文字列として保存

    // レスポンスを返す
    val json: JsValue = Json.obj(
      "status" -> "success",
      "message" -> "Words have been saved to Redis.",
      "words" -> wordsArray
    )

    Ok(json)
  }
}
