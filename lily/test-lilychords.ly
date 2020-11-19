\version "2.20.0"
\include "english.ly"

#(set! paper-alist (cons '("anki-png" . (cons (* 120 mm) (* 125 mm))) paper-alist))
#(set-default-paper-size "anki-png")

\header {
  tagline = "" % \markup { \tiny "mail@jlab.de" }
}

\header {
  title = \markup{
    \small {
      "Test Lilypond Chord notation"
    }
  }
  tagline = \markup {
    \teeny "(C) 2020 AnkiJazz.com - do not distribute"
  }
}

\score {
  <<
    \new ChordNames \with {
      \override BarLine.bar-extent = #'(-2 . 4)
      \consists "Bar_engraver"
    }
    \chordmode {
       f1:maj7 f:7 bf:7 a:m7+ bf:aug7 c:7.9- gs:9+
       f1:maj7 f:7 bf:7 a:m7+ bf:aug7 c:7.9- gs:9+
       f1:maj7 f:7 bf:7 a:m7+ bf:aug7 c:7.9- gs:9+
       f1:maj7 f:7 bf:7 a:m7+ bf:aug7 c:7.9- gs:9+
       f1:maj7 f:7 bf:7 a:m7+ bf:aug7 c:7.9- gs:9+
    }
  >>
  \layout {
    indent = 0.0
  }
}