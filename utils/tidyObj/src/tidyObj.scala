#!/bin/sh
exec scala "$0" "$@"
!#

import io.Source._

object App {
  def main(args: Array[String]) {
    //println(s"args: ${args.mkString( """,""")}")
    val lines = if (args.length > 0) fromFile(args(0)).getLines() else stdin.getLines()
    for (ln <- lines)
      println(
        if (ln.startsWith("o ") || ln.startsWith("g "))
          ln.substring(0, 2) + ln.substring(2).replaceAll("[^\\w]", "_")
        else ln
      )
  }
}

App.main(args)