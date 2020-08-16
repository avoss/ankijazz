\version "2.20.0"
\include "english.ly"

#(set! paper-alist (cons '("anki-png" . (cons (* 120 mm) (* 25 mm))) paper-alist))
#(set-default-paper-size "anki-png")

\header {
  tagline = "" % \markup { \tiny "mail@jlab.de" }
}


scaleNotes = \relative e' { e4 fs4 g4 a4 b4 cs4 d4 e~ e1 }
noteNames = \relative e' { e4 fs4 g4 a4 b4 cs4 d4 e }
midiChord = \relative e' { <e, b' d g>1 }
lilyChord = \relative e' { <e g b d>1 }

\score {
  <<
    \new Staff {
      \new Voice {
        \clef treble
        \key d \major
        % suppress 4/4 time signature
        \override Staff.TimeSignature.stencil = ##f
        \scaleNotes
      }
    }
	\new ChordNames {
%	  \set minorChordModifier = \markup { "-" }
	  \noteNames \lilyChord
	}
  >>
  \layout { }
}

\score {
  <<
    \new Staff {
      \new Voice = "scale" {
         r1 \unfoldRepeats{ \repeat volta 4 { \scaleNotes } }
      }
    }
    \new Staff {
      \set Staff.midiInstrument = #"pad 2 (warm)"
      \set Staff.midiMinimumVolume = #0.1
      \set Staff.midiMaximumVolume = #0.1
      \new Voice = "chord" {
         \unfoldRepeats{ \repeat volta 12 { \midiChord } }
      }
    }
    \new DrumStaff = "click" {
      \drummode {
        \unfoldRepeats \repeat volta 4 { cl4 wbh wbh wbh r1 r1 }
      }
    }
  >>
  \midi {
    \tempo 4 = 80 
  }
}
