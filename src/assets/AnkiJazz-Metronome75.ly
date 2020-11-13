\version "2.20.0"
\include "english.ly"

\score {
  <<
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
