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

  def postDB(): Action[AnyContent] = Action {
    redisClient.set("test2", "ora") // Redisにデータを設定

    val json: JsValue = Json.obj(
      "hello" -> "world",
      "language" -> "scala"
    )

    Ok(json)
  }
}
