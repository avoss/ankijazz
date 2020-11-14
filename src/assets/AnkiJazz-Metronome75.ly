\version "2.20.0"
\include "english.ly"

\score {
  <<
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
