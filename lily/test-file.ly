\version "2.20.0"
\include "english.ly"

#(set! paper-alist (cons '("anki-png" . (cons (* 120 mm) (* 25 mm))) paper-alist))
#(set-default-paper-size "anki-png")

\header {
  tagline = "" % \markup { \tiny "mail@jlab.de" }
}


scaleNotes = \relative e' {
%  ${scaleNotes}
  c4 d e fs g af b c ~ c1
}

chordNotes = \relative e' {
  c4 d e fs g af b c
}


\score {
  <<
    \new Staff {
      \new Voice {
        \clef treble
        \key c \major
        % suppress 4/4 time signature
        \override Staff.TimeSignature.stencil = ##f
        \scaleNotes
      }
    }
	\new ChordNames {
%	  \set minorChordModifier = \markup { "-" }
	  \chordNotes
	}
  >>
  \layout { }
}

\score {
  <<
    \new Staff {
      \new Voice = "notes" {
         r1 \unfoldRepeats{ \repeat volta 4 { \scaleNotes } }
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

