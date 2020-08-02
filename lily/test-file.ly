\version "2.20.0"
\include "english.ly"

#(set! paper-alist (cons '("anki-png" . (cons (* 80 mm) (* 20 mm))) paper-alist))
#(set-default-paper-size "anki-png")

\header {
  tagline = "www.jlab.de"
}

melody = \relative c' {
  \clef treble
  \key c \major
  \time 4/4

  c4 d e | f g a b c 
}

\score {
  \new Staff \melody
  \layout { }
  \midi { }
}

