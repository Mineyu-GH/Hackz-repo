package controllers

import javax.inject._ 
import play.api.mvc._ 
import play.api.libs.json._
import models._

//ありがてぇ！

@Singleton
class ShiritoriController @Inject()(val controllerComponents: ControllerComponents) 
  extends BaseController {

  implicit val gameStateWrites: Writes[GameState] = new Writes[GameState] {
    
    def writes(state: GameState): JsValue = JsString(state match {
      case Active => "Active"
      case GameOver => "GameOver"
    })
  }
  
  implicit val gameWrites: Writes[ShiritoriGame] = Json.writes[ShiritoriGame]
  
  private var currentGame = ShiritoriGame(ShiritoriGame.getRandomChar)

  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.shiritori(currentGame))
  }

  def submitWord = Action(parse.json) { implicit request =>
    val wordResult = (request.body \ "word").asOpt[String]
    
    wordResult match {
      case Some(word) =>
        ShiritoriGame.validateWord(word, currentGame) match {
          case Right(validWord) =>
            if (validWord.startsWith("2")) {
              currentGame = currentGame.copy(
                targetChar = ShiritoriGame.getRandomChar,
                usedWords = validWord.substring(1) :: currentGame.usedWords,
                score = currentGame.score + 2 * (validWord.substring(1).length)
            )
            }
            else {
              currentGame = currentGame.copy(
                targetChar = ShiritoriGame.getRandomChar,
                usedWords = validWord :: currentGame.usedWords,
                score = currentGame.score + word.length
            )
            }
            Ok(Json.toJson(currentGame))
          
          case Left(error) =>
            if (error.startsWith("Game Over")) {
              currentGame = currentGame.copy(state = GameOver)
              Ok(Json.toJson(currentGame))
            } else {
              BadRequest(Json.obj("error" -> error))
            }
        }
      
      case None =>
        BadRequest(Json.obj("error" -> "No word provided"))
    }
  }

  def newGame = Action { implicit request =>
    currentGame = ShiritoriGame(ShiritoriGame.getRandomChar)
    Ok(Json.toJson(currentGame))
  }
}
