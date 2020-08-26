\version "2.20.0"
\include "english.ly"

#(set! paper-alist (cons '("anki-png" . (cons (* 120 mm) (* 90 mm))) paper-alist))
#(set-default-paper-size "anki-png")

\header {
  tagline = "" % \markup { \tiny "mail@jlab.de" }
}


scaleNotes = { g4 a b c d e fs g ~ g1}


\score {
  <<
    \new StaffGroup <<
    \new Staff {
      \new Voice = "scale" {
        \clef treble
        \key g \major
        % suppress 4/4 time signature
        \override Staff.TimeSignature.stencil = ##f
        \relative e' { \scaleNotes }
      }
    }
    \new TabStaff {
      \clef moderntab
      \set TabStaff.minimumFret = #4
      \set TabStaff.restrainOpenStrings = ##t      
      \relative e { \scaleNotes }
    }
    >>
    \new ChordNames {
      \scaleNotes
    }
 
  >>
  \layout { }
}

