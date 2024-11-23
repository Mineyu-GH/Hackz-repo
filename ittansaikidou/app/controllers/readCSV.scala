package controllers

import javax.inject._
import play.api.mvc._
import play.api.Environment
import scala.io.Source

@Singleton
class readCSV @Inject()(val controllerComponents: ControllerComponents, env: Environment) extends BaseController {

  def printCSV = Action {
    val csvFile = env.getFile("public/data/data.csv")

    try {
      val csvContent = Source.fromFile(csvFile).getLines().mkString("\n")
      Ok(csvContent)
    } catch {
      case e: Exception => InternalServerError(s"Error reading CSV file: ${e.getMessage}")
    }
  }
}