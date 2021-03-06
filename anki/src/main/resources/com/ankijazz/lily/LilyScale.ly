\version "2.20.0"
\include "english.ly"

#(set! paper-alist (cons '("anki-png" . (cons (* 100 mm) (* 32 mm))) paper-alist))
#(set-default-paper-size "anki-png")

% http://lilypond.org/doc/v2.18/Documentation/notation/other
% http://lilypond.org/doc/v2.18/Documentation/notation/list-of-colors

\header {
  title = \markup{ \small { "${title}" }}
  tagline = \markup { \teeny { \with-color #grey { "(C) 2021 AnkiJazz.com - Anki Shared Deck License" } } }
}


scaleNotes = \relative ${relativeTo} { ${scaleNotes} }
noteNames = \relative e' { ${noteNames} }
midiChord = \relative e' { ${midiChord} }

\score {
  <<
    \new Staff {
      \new Voice {
        \clef ${clef}
        \key ${key} \major
        \scaleNotes
      }
    }
%	  \new ChordNames {
%	    \noteNames
%	  }
  >>
  \layout {
    indent = 0.0
    \context { \Score \omit BarNumber }
  }
}

\score {
  <<
    \new Staff {
      \set Staff.midiInstrument = #"electric grand"
      \new Voice = "scale" {
        ${scaleTranspose} { \scaleNotes }
      }
    }
    \new Staff {
      \set Staff.midiInstrument = #"pad 2 (warm)"
      \set Staff.midiMinimumVolume = #0.2
      \set Staff.midiMaximumVolume = #0.2
      \new Voice = "chord" {
        ${chordsTranspose} { \midiChord ~ \midiChord }
      }
    }
  >>
  \midi {
    \tempo 4 = 100 
  }
}
