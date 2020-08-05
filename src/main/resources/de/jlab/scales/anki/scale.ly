\version "2.20.0"
\include "english.ly"

#(set! paper-alist (cons '("anki-png" . (cons (* 90 mm) (* 25 mm))) paper-alist))
#(set-default-paper-size "anki-png")

\header {
  tagline = "" % \markup { \tiny "mail@jlab.de" }
 % title = "C Lydian"
}

\score {
  <<
    \new Staff {
      \new Voice = "melody" {
        \clef treble
        \key ${key} \major
        
        % suppress 4/4 time signature
        \override Staff.TimeSignature.stencil = ##f
        
        \relative f' {
          ${notes}
        }
      }
    }
    \new Lyrics {
      \lyricsto "melody" {
        ${lyrics}
      }
    }
  >>
  \layout { }
  \midi {
    \tempo 4 = 120 
  }
}

