file:///C:/Users/miney/OneDrive/デスクトップ/Dev/Hackz/ittansaikidou/app/controllers/HomeController.scala
### dotty.tools.dotc.ast.Trees$UnAssignedTypeException: type of Ident(Some) is not assigned

occurred in the presentation compiler.

presentation compiler configuration:


action parameters:
offset: 479
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

  def SubmitWord = {
    val wordresult = 
    wordresult match {
      case Some[@@]
    }
  }
}

```



#### Error stacktrace:

```
dotty.tools.dotc.ast.Trees$Tree.tpe(Trees.scala:74)
	dotty.tools.dotc.util.Signatures$.applyCallInfo(Signatures.scala:208)
	dotty.tools.dotc.util.Signatures$.computeSignatureHelp(Signatures.scala:104)
	dotty.tools.dotc.util.Signatures$.signatureHelp(Signatures.scala:88)
	dotty.tools.pc.SignatureHelpProvider$.signatureHelp(SignatureHelpProvider.scala:47)
	dotty.tools.pc.ScalaPresentationCompiler.signatureHelp$$anonfun$1(ScalaPresentationCompiler.scala:422)
```
#### Short summary: 

dotty.tools.dotc.ast.Trees$UnAssignedTypeException: type of Ident(Some) is not assigned