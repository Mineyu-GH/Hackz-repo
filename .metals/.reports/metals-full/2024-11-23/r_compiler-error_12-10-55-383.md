file:///C:/Users/miney/OneDrive/デスクトップ/Dev/Hackz/ittansaikidou/app/controllers/HomeController.scala
### java.lang.IndexOutOfBoundsException: -1

occurred in the presentation compiler.

presentation compiler configuration:


action parameters:
offset: 488
uri: file:///C:/Users/miney/OneDrive/デスクトップ/Dev/Hackz/ittansaikidou/app/controllers/HomeController.scala
text:
```scala
package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import models._

@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  var currentgame = ShiritoriGame(ShiritoriGame.getTargetChar)

  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def SubmitWord = Action() {implicit request: Request[AnyContent] =>
    val wordResult = ()@@
    wordResult match {
      case Some(word) =>
        ShiritoriGame.CheckWord(word, currentgame) match {
          case Right(OkWord) =>
            currentgame = hoge

          case Left(errorM) =>
            if (errorM.startsWith("Game Over!")) {
              currentgame.state = ShiritoriGame.GameOver
            }
            else {
              BadRequest(errorM)
            }
        }
      case None => BadRequest("Invalid word")
    }
  }
}

```



#### Error stacktrace:

```
scala.collection.LinearSeqOps.apply(LinearSeq.scala:129)
	scala.collection.LinearSeqOps.apply$(LinearSeq.scala:128)
	scala.collection.immutable.List.apply(List.scala:79)
	dotty.tools.dotc.util.Signatures$.applyCallInfo(Signatures.scala:244)
	dotty.tools.dotc.util.Signatures$.computeSignatureHelp(Signatures.scala:101)
	dotty.tools.dotc.util.Signatures$.signatureHelp(Signatures.scala:88)
	dotty.tools.pc.SignatureHelpProvider$.signatureHelp(SignatureHelpProvider.scala:47)
	dotty.tools.pc.ScalaPresentationCompiler.signatureHelp$$anonfun$1(ScalaPresentationCompiler.scala:422)
```
#### Short summary: 

java.lang.IndexOutOfBoundsException: -1