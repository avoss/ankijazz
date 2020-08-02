\version "2.20.0"
\include "english.ly"

#(set! paper-alist (cons '("anki-png" . (cons (* 90 mm) (* 23 mm))) paper-alist))
#(set-default-paper-size "anki-png")

\header {
  tagline = "C Lydian"
}

melody = \relative c' {
  \clef treble
  \key g \major
  \time 4/4

  c4 d e fs g a b c 
}

\score {
  \new Staff \melody
  \layout { }
  \midi { }
}

