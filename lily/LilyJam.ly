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




c1:maj7
c1:maj7.11+
c1:m7
c1:m6
\break
c1:7
c1:m7.5-
c1:dim7
c1:m7+
\break
c1:maj7.5+
c1:7sus4
c1:7.9-
c1:7.9+
\break
c1:7.5-
c1:7.5+
c1:7.5-9-
c1:7.5+9-
\break
c1:7.5-9+
c1:7.5+9+
c1:
c1:m
\break
c1:dim
c1:aug
c1:sus4
df1:maj7
\break
df1:maj7.11+
cs1:m7
cs1:m6
df1:7
\break
cs1:7
cs1:m7.5-
df1:dim7
df1:m7+
\break
df1:maj7.5+
df1:7sus4
cs1:7sus4
cs1:7.9-
\break
df1:7.9+
df1:7.5-
cs1:7.5-
cs1:7.5+
\break
cs1:7.5-9-
cs1:7.5+9-
df1:7.5-9+
df1:7.5+9+
\break
df1:
cs1:m
cs1:dim
df1:aug
\break
df1:sus4
d1:maj7
d1:maj7.11+
d1:m7
\break
d1:m6
d1:7
d1:m7.5-
d1:dim7
\break
d1:m7+
d1:maj7.5+
d1:7sus4
d1:7.9-
\break
d1:7.9+
d1:7.5-
d1:7.5+
d1:7.5-9-
\break
d1:7.5+9-
d1:7.5-9+
d1:7.5+9+
d1:
\break
d1:m
d1:dim
d1:aug
d1:sus4
\break
ef1:maj7
ef1:maj7.11+
ef1:m7
ds1:m7
\break
ef1:m6
ef1:7
ds1:m7.5-
ef1:dim7
\break
ef1:m7+
ef1:maj7.5+
ef1:7sus4
ds1:7.9-
\break
ds1:7.9+
ef1:7.5-
ef1:7.5+
ds1:7.5+
\break
ds1:7.5-9-
ds1:7.5+9-
ds1:7.5-9+
ds1:7.5+9+
\break
ef1:
ef1:m
ds1:m
ds1:dim
\break
ef1:aug
ef1:sus4
e1:maj7
e1:maj7.11+
\break
e1:m7
e1:m6
e1:7
e1:m7.5-
\break
e1:dim7
e1:m7+
e1:maj7.5+
e1:7sus4
\break
e1:7.9-
e1:7.9+
e1:7.5-
e1:7.5+
\break
e1:7.5-9-
e1:7.5+9-
e1:7.5-9+
e1:7.5+9+
\break
e1:
e1:m
e1:dim
e1:aug
\break
e1:sus4
f1:maj7
f1:maj7.11+
f1:m7
\break
f1:m6
f1:7
f1:m7.5-
f1:m7.5-
\break
f1:dim7
f1:m7+
f1:maj7.5+
f1:7sus4
\break
f1:7.9-
f1:7.9+
f1:7.5-
f1:7.5+
\break
f1:7.5-9-
f1:7.5+9-
f1:7.5-9+
f1:7.5+9+
\break
f1:
f1:m
f1:dim
f1:dim
\break
f1:aug
f1:sus4
gf1:maj7
gf1:maj7.11+
\break
fs1:m7
fs1:m6
fs1:7
fs1:m7.5-
\break
gf1:dim7
gf1:m7+
gf1:maj7.5+
fs1:7sus4
\break
fs1:7.9-
gf1:7.9+
fs1:7.5-
fs1:7.5+
\break
fs1:7.5-9-
fs1:7.5+9-
gf1:7.5-9+
gf1:7.5+9+
\break
gf1:
fs1:
fs1:m
fs1:dim
\break
gf1:aug
gf1:sus4
fs1:sus4
g1:maj7
\break
g1:maj7.11+
g1:m7
g1:m6
g1:7
\break
g1:m7.5-
g1:dim7
g1:m7+
g1:maj7.5+
\break
g1:7sus4
g1:7.9-
g1:7.9+
g1:7.5-
\break
g1:7.5+
g1:7.5-9-
g1:7.5+9-
g1:7.5-9+
\break
g1:7.5+9+
g1:
g1:m
g1:dim
\break
g1:aug
g1:sus4
af1:maj7
af1:maj7.11+
\break
gs1:m7
af1:m6
gs1:m6
af1:7
\break
gs1:m7.5-
af1:dim7
af1:m7+
af1:maj7.5+
\break
af1:7sus4
gs1:7.9-
af1:7.9+
af1:7.5-
\break
gs1:7.5+
gs1:7.5-9-
gs1:7.5+9-
gs1:7.5-9+
\break
gs1:7.5+9+
af1:
gs1:m
gs1:dim
\break
af1:aug
af1:sus4
a1:maj7
a1:maj7.11+
\break
a1:m7
a1:m6
a1:7
a1:m7.5-
\break
a1:dim7
a1:m7+
a1:maj7.5+
a1:7sus4
\break
a1:7.9-
a1:7.9+
a1:7.5-
a1:7.5+
\break
a1:7.5-9-
a1:7.5+9-
a1:7.5-9+
a1:7.5+9+
\break
a1:
a1:m
a1:dim
a1:aug
\break
a1:sus4
bf1:maj7
bf1:maj7.11+
bf1:m7
\break
bf1:m6
bf1:7
as1:m7.5-
bf1:dim7
\break
bf1:m7+
bf1:maj7.5+
bf1:7sus4
as1:7.9-
\break
as1:7.9+
bf1:7.5-
bf1:7.5+
as1:7.5-9-
\break
as1:7.5+9-
as1:7.5-9+
as1:7.5+9+
bf1:
\break
bf1:m
as1:dim
bf1:aug
bf1:sus4
\break
b1:maj7
b1:maj7.11+
b1:m7
b1:m6
\break
b1:7
b1:m7.5-
b1:dim7
b1:dim7
\break
b1:m7+
b1:maj7.5+
b1:7sus4
b1:7.9-
\break
b1:7.9+
b1:7.5-
b1:7.5+
b1:7.5-9-
\break
b1:7.5+9-
b1:7.5-9+
b1:7.5+9+
b1:
\break
b1:m
b1:dim
b1:aug
b1:aug
\break
b1:sus4

      
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