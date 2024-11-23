package models

sealed trait GameState
case object Active extends GameState
case object GameOver extends GameState