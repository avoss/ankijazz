\version "2.20.0"

\include "english.ly"

#(set! paper-alist (cons '("anki-png" . (cons (* 120 mm) (* 110 mm))) paper-alist))
#(set-default-paper-size "anki-png")

\header {
%  title = \markup{ \small { "${title}" }}
  tagline = \markup { \teeny { \with-color #grey { "(C) 2020 AnkiJazz.com - Do not distribute!" } } }
}

chordmusic = {
  \chordmode {
    \repeat volta 2 { 
      ${chords} 
%      a1:m7 d:m7 g:7 c:maj7 \break
%      a1:m7 d:m7 g:7 c:maj7 \break
%      a1:m7 d:m7 g:7 c:maj7 \break
%      a1:m7 d:m7 g:7 c:maj7 \break
    }
  } 
}

\score {
  <<
%    \new ChordNames \with {
%      \override BarLine.bar-extent = #'(-2 . 4)
%      \consists "Bar_engraver"
%    }
%    \chordmusic
    \new ChordNames {
      \chordmusic
    }
    {
      \chordmusic
    }
    
  >>
  \layout {
%    \context { 
%      \Staff \omit TimeSignature
%    }
    \context { 
     \Score \omit BarNumber
    }
%    \context {
%      \Score
%       proportionalNotationDuration = #(ly:make-moment 1 8)
%    }
    indent = 0.0
  }
}