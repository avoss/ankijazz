\version "2.20.0"
\include "english.ly"

#(set! paper-alist (cons '("anki-png" . (cons (* 90 mm) (* 35 mm))) paper-alist))
#(set-default-paper-size "anki-png")

\header {
  tagline = \markup { \tiny "mail@jlab.de" }
  title = "C Lydian"
}

\score {
  <<
    \new Staff {
      \new Voice = "melody" {
        \clef treble
        \key g \major
        \relative f' {
          c d e fs g a b c
        }
      }
    }
    \new Lyrics {
      \lyricsto "melody" {
        "C" "D" "E" "F#" "G" "A" "B" "C"
      }
    }
  >>
  \layout { }
  \midi { }
}

