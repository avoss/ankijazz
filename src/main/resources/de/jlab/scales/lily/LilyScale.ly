\version "2.20.0"
\include "english.ly"

#(set! paper-alist (cons '("anki-png" . (cons (* 100 mm) (* 28 mm))) paper-alist))
#(set-default-paper-size "anki-png")

\header {
  title = \markup{ \small { "${title}" }}
  tagline = \markup { \tiny "(C) 2020 www.AnkiJazz.com - Anki Shared Deck License" }
}


scaleNotes = \relative ${relativeTo} { ${scaleNotes} }
noteNames = \relative e' { ${noteNames} }
midiChord = \relative e' { ${midiChord} }
lilyChord = \relative e' { ${lilyChord} }

\score {
  <<
    \new Staff {
      \new Voice {
        \clef ${clef}
        \key ${key} \major
        % suppress 4/4 time signature
        % \override Staff.TimeSignature.stencil = ##f
        \scaleNotes
      }
    }
	\new ChordNames {
%	  \set minorChordModifier = \markup { "-" }
	  \noteNames % \lilyChord
	}
  >>
  \layout {
    indent = 0.0 
  }
}

\score {
  <<
    \new Staff {
      \set Staff.midiInstrument = #"electric grand"
      \new Voice = "scale" {
         \scaleNotes
      }
    }
    \new Staff {
      \set Staff.midiInstrument = #"pad 2 (warm)"
      \set Staff.midiMinimumVolume = #0.2
      \set Staff.midiMaximumVolume = #0.2
      \new Voice = "chord" {
        \midiChord ~ \midiChord
      }
    }
  >>
  \midi {
    \tempo 4 = 100 
  }
}
