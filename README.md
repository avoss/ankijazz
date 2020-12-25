# Scales Core
Anki media directory is C:\Users\andreas\AppData\Roaming\Anki2

# TODO
- agpl license
- migrate to junit 5
- rename methods to getX, setX
- scales free, modes commercial?
- guitar tabulator

## pentatonics
- m7 and m6 pentatonics
- string skipping 
- chords maj7, min7, min6, dominant7, altered 2x

--------------------------------------------------------------------------------------------

## Chords
- add to scales deck (already in theory deck)
- add pentatonics to scales deck?
- add which pentatonic over which chord?

## JTG in Lily?
[proportional notation](https://lilypond.org/doc/v2.16/Documentation/notation/proportional-notation.en.html)
bars with equal space length?


## Rhythm
12/8 Bembe Bell Pattern, 6/8 Clave, 

alle Pattern mit max 1 Pause (statt pause nur am anfang = syncopation)?

http://lilypond.org/doc/v2.18/Documentation/notation/displaying-rhythms
Completion_heads_engraver = automatic ties across bars
Pitch_squash_engraver = Guitar strumming notation

9 Patterns pro 1/4 Note von https://www.youtube.com/watch?v=Y5_27Gc28ls
- fraction: 4, 8+8, 16+16+16+16, 16+16+8, 8+16+16, 16+8+16, 8.+16, 16+8., 8-triplets
- duration: 4, 2+2, 1+1+1+1, 1+1+2, 2+1+1, 1+2+1, 1+3, 3+1, 4/3+4/3+4/3

**Schnipsel anzeigen** also aus welchen Bausteinen besteht ein Rhythmus.

## Business - buymeacoffee.com


Create a robot liking on ankiweb ...?
youtube video?
mturk?

## Decks

Money
- "Advanced Pentatonics"


Text
- learn enharmonics Ab -> G# etc
- learn intervals e.g. what is b13 of F?
- learn scale notes w/o notation
- learn parent scales
- spell mode intervals
- learn relative minor/major (included in parent scales)
- für Bb und Eb Instrumente (midi transponiert)
- Rhythm kostet

Lily
- intervals
- chords

Sheets
- know your fretboard: single note, natural, flat, sharps, all keys. 
- triads in 251 progressions
- 7th chords
- melody snippets for site reading training

## Anki Scheduler:
- preferences -> scheduling: use new 2.1 scheduler and select "show new cards after review" to make sure repetition is completed even if you don't have time for new cards on a certain day
- in deck options change learning steps from "1 10" to "20 1440" and increase graduating interval to 3 days
- similar in lapses section change re-learning steps from "1 10" to "20 1440"
- in "general" tab change "ignore answer time" to at least 1 minute (or how long it takes to play a scale or some chords)

--------------------------------------------------------------------------------------------

# Stuff

## Dr. B Music
- https://youtu.be/ICDPWP6HUbk?list=PLw9t0oA3fHkxx1PgYpiXrMUPXaOiwh6KU
- there are 15 major scales, need to know the enharmonics 
- parallel = same root, relative = same signature

Intervals
- always starting from bottom counting to top
- "number" = number of letters including start pitch
- "category: 1,4,5,8 APd (Augmented, Perfect,diminished), 2,3,6,7 AMmd (Major, minor)
- Inversion P->P, A->d, M->m and sum of numbers = 9

## Frank Sikora "Jazz Harmony"
- German edition is very popular here.
- https://en.schott-music.com/shop/jazz-harmony-no326750.html
- all minor modes relative to aeolean, all major modes relative to ionean

## Special cases
- MM notated as Dorian #7 -> mode Lydian Dominant is notated as Mixolyidian #11 which makes sense!
- C altered is notated as B# altered because C altered is 7th mode of C# MM and C# Aeolean is the relative minor of E-Major
- B Lydian wäre korrekt Cb Lydian weil Gb major scale Cb statt B enthält
- C Altered wäre korrekt B# Altered weil Calt = 7th mode of C# MM and relative minor of C# is E (MM with aeolean notation)

- G#  Harmonic Minor, Signature:  B (5#), Notation: G# A# B C# D# E Fx // bb or ##
- Db  Harmonic Major, Signature: Db (5b), Notation: Db Eb F Gb Ab Bbb C // bb or ##
- Gb  Harmonic Major, Signature: Gb (6b), Notation: Gb Ab Bb Cb Db Ebb F // bb or ##

Major Scales and Modes:
- F#/Gb: no one better than the other, need both - why is it so complicated? Modes always have enharmonic roots Gb has Cb and F# has E#
- Cb Lydian, Signature: Gb (6b), Notation: Cb Db Eb F Gb Ab Bb // enharmonic root

KeySignature strict:
- Modes use signature of parent scale
- HM and MM use signature of parallel Aeolean
- result contains double bb and ##, enharmonic roots

KeySignature simplified:
- MM uses signature of parallel Dorian


## noteworthy
- Ab/G# harmonic minor requires double-sharp
- Db and Gb harmonic major require double-flat
- D harmonic minor requires both, flats and sharps
- CMMin, GHMin, DHMin, GHMaj and DHMaj contain both, # and b ??? double check!
