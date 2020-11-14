\version "2.20.0"
\include "english.ly"

#(set! paper-alist (cons '("anki-png" . (cons (* 130 mm) (* 60 mm))) paper-alist))
#(set-default-paper-size "anki-png")

\header {
  title = "" % \markup{ \small { "C Major Scale" }}
  tagline = \markup { \tiny "Licensed to my friend Shahram ;-) (C) 2020 Andreas Voss" }
}


scaleNotes = \relative e' { ${scaleNotes} }

midiChord = \relative e' { <g c e a>1 <fs' c e a>1 }
bassNotes = \relative e, { a2. a4 d2. d4 }

\score {
  <<
    \new Staff {
      \new Voice {
        %\clef treble
        %\key c \major
        % suppress 4/4 time signature
        \override Staff.TimeSignature.stencil = ##f
        \repeat volta 2 { \scaleNotes }
      }
    }
  >>
  \layout { }
}

\score {
  <<
    \new Staff {
      \new Voice = "scale" {
        r1  \unfoldRepeats{ \repeat volta 2 { \scaleNotes } }
      }
    }
    \new Staff {
      \set Staff.midiInstrument = #"pad 2 (warm)"
      \set Staff.midiMinimumVolume = #0.2
      \set Staff.midiMaximumVolume = #0.2
      \new Voice = "chord" {
        r1 \unfoldRepeats{ \repeat volta 4 { \midiChord } } 
      }
    }
    \new Staff {
      \set Staff.midiInstrument = #"electric bass (finger)"
      \set Staff.midiMinimumVolume = #0.4
      \set Staff.midiMaximumVolume = #0.4
      \new Voice = "chord" {
        r1 \unfoldRepeats{ \repeat volta 4 { \bassNotes } } 
      }
    }
    \new DrumStaff = "click" {
      \drummode {
        cl4 wbh wbh wbh
        \unfoldRepeats{ \repeat volta 4 {bd4 <bd sn> bd <bd sn>    bd <bd sn> bd <bd sn> } }
      }
    }
  >>
  \midi {
    \tempo 4 = 75
  }
}
