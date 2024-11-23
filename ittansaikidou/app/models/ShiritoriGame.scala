package models

case class ShiritoriGame(
    targetChar: String,
    usedWords: List[String] = List.empty,
    score: Int = 0,
    state: GameState = Active
)

object ShiritoriGame {
    def validateWord(word: String, game: ShiritoriGame): Either[String, String] = {
        if (!word.matches("^[ぁ-んー]*$")) {
            Left("ひらがな")
        } else if (word.endsWith("ん")) {
            Left("Game Over! 「ん」で終わりました")
        } else if (game.usedWords.contains(word)) {
            Left("その言葉はもう使っています")
        } else if (word.endsWith(game.targetChar)) {
            Right("2" + word)
        }
        else {
            Right(word)
        }
    }

    val targetChars = List("あ", "い", "う", "え", "お", "か", "き", "く", "け", "こ", "さ", "し", "す", "せ", "そ", "た", "ち", "つ",
        "て", "と", "な", "に", "ぬ", "ね", "の", "は", "ひ", "ふ", "へ", "ほ", "ま", "み", "む", "め", "も", "や", "ゆ", "よ", "ら", "り",
        "る", "れ", "ろ", "わ")
    
    def getRandomChar: String = {
        val r = new scala.util.Random
        targetChars(r.nextInt(targetChars.length))
    }
}
