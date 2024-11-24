package controllers

import javax.inject._ 
import play.api.mvc._ 
import play.api.libs.json._
import models._
import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration._
import services.readCSV

@Singleton
class ShiritoriController @Inject()(val controllerComponents: ControllerComponents)
                                  (implicit ec: ExecutionContext) 
  extends BaseController {

  implicit val gameStateWrites: Writes[GameState] = new Writes[GameState] {
    def writes(state: GameState): JsValue = JsString(state match {
      case Active => "Active"
      case GameOver => "GameOver"
    })
  }
  
  implicit val gameWrites: Writes[ShiritoriGame] = Json.writes[ShiritoriGame]
  
  private var currentGame = ShiritoriGame(ShiritoriGame.getRandomChar)
  private var timeRemaining = 30 
  private var timerOpt: Option[java.util.Timer] = None

  private def startTimer(): Unit = {
    stopTimer() 
    timeRemaining = 30 
    
    val timer = new java.util.Timer()
    val task = new java.util.TimerTask {
      def run(): Unit = {
        timeRemaining -= 1
        if (timeRemaining <= 0) {
          currentGame = currentGame.copy(state = GameOver)
          stopTimer()
        }
      }
    }
    
    timer.scheduleAtFixedRate(task, 1000L, 1000L)
    timerOpt = Some(timer)
  }

  private def stopTimer(): Unit = {
    timerOpt.foreach(_.cancel())
    timerOpt = None
  }

  def index(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    readCSV.readCSV()
    Ok(views.html.shiritori(currentGame))
  }

  def submitWord: Action[JsValue] = Action(parse.json) { implicit request =>
    val wordResult = (request.body \ "word").asOpt[String]

    wordResult match {
      case Some(word) =>
        if (currentGame.state == Active) {
          ShiritoriGame.validateWord(word, currentGame) match {
            case Right(validWord) =>
              startTimer()

              if (validWord.startsWith("2")) {
                currentGame = currentGame.copy(
                  targetChar = ShiritoriGame.getRandomChar,
                  lastword = validWord.substring(1),
                  usedWords = validWord.substring(1) :: currentGame.usedWords,
                  score = currentGame.score + 2 * validWord.substring(1).length
                )
              } else {
                currentGame = currentGame.copy(
                  targetChar = ShiritoriGame.getRandomChar,
                  lastword = validWord,
                  usedWords = validWord :: currentGame.usedWords,
                  score = currentGame.score + word.length
                )
              }
              Ok(Json.obj(
                "game" -> Json.toJson(currentGame),
                "timeRemaining" -> timeRemaining
              ))

            case Left(error) =>
              if (error.startsWith("Game Over")) {
                currentGame = currentGame.copy(state = GameOver)
                stopTimer()
                Ok(Json.toJson(currentGame))
              } else {
                BadRequest(Json.obj("error" -> error))
              }
          }
        } else {
          
          BadRequest(Json.obj("error" -> "Game is already over"))
        }

      case None =>
        BadRequest(Json.obj("error" -> "No word provided"))
    }
  }

  def newGame: Action[AnyContent] = Action { implicit request =>
    stopTimer()
    currentGame = ShiritoriGame(ShiritoriGame.getRandomChar)
    timeRemaining = 30
    Ok(Json.toJson(currentGame))
  }

  def getTimeRemaining: Action[AnyContent] = Action {
    Ok(Json.obj("timeRemaining" -> timeRemaining))
  }
}
