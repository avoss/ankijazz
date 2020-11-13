\version "2.20.0"
\include "english.ly"

#(set! paper-alist (cons '("anki-png" . (cons (* 130 mm) (* 60 mm))) paper-alist))
#(set-default-paper-size "anki-png")

\header {
  title = "" % \markup{ \small { "C Major Scale" }}
  tagline = "" % \markup { \tiny "(C) mail@ankijazz.com" }
}


scaleNotes = \relative e' { r1
  a16 a a a  a a a a  a a a a  a a a a |
  a16 a a a  a a a a  a a a a  a a a a |
  a16 a a a  a a a a  a a a a  a a a a |
  a16 a a a  a a a a  a a a a  a a a a |

}

\score {
  <<
    \new Staff {
      \new Voice {
        %\clef treble
        %\key c \major
        % suppress 4/4 time signature
        \override Staff.TimeSignature.stencil = ##f
        \scaleNotes
      }
    }
  >>
  \layout { }
}

\score {
  <<
    \new Staff {
      \set Staff.midiMaximumVolume = #0.6
      \new Voice = "scale" {
         \scaleNotes
      }
    }
    \new DrumStaff = "click" {
      \drummode {
        cl4 wbh wbh wbh
		bd bd bd bd 
		bd bd bd bd 
		bd bd bd bd 
		bd bd bd bd 
      }
    }
  >>
  \midi {
    \tempo 4 = 75
  }
}
