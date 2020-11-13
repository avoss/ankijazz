# Scales Core

# TODO
- agpl license
- migrate to junit 5
- rename methods to getX, setX
- scales free, modes commercial?

--------------------------------------------------------------------------------------------

# Anki

Title:
practice *efficiently* scales and more

## Rhythm

Rhyhtm: ties always end on a beat 1,2,3,4 (reason for ties)

http://lilypond.org/doc/v2.18/Documentation/notation/displaying-rhythms
Completion_heads_engraver = automatic ties across bars
Pitch_squash_engraver = Guitar strumming notation

9 Patterns pro 1/4 Note von https://www.youtube.com/watch?v=Y5_27Gc28ls
- fraction: 4, 8+8, 16+16+16+16, 16+16+8, 8+16+16, 16+8+16, 8.+16, 16+8., 8-triplets
- duration: 4, 2+2, 1+1+1+1, 1+1+2, 2+1+1, 1+2+1, 1+3, 3+1, 4/3+4/3+4/3

- in jedem Pattern kann eine oder mehrere Noten fehlen (Pause), wobei dann das selbe rauskommen kann, z.B. r1+r1+r1+b1 == r1+r2+b1 == r3+b1
- mehrere Pattern können über TIE verbunden werden, aber nur wenn die Pattern mit beat enden bzw anfangen und mehr als 2 events beteiligt sind.
- maybe same for triplet (swing) based rhyhtms

b1 ... b4 = beats der länge 1..4
r1 ... r4 = rest der länge 1..4
bt / rt = 8-th triplet

difficulty depends on length and where the beat occurs (on beat, on 8th, on 16th), number of different lenghts, number of different types (beat vs rest) and beams.

Play single note melody with rhythm over different chords. Z.B. Note = A over Am7, FMaj7, Bm7b5, Esus4 - oder einfach Am7/D79

## Decks

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
- notation of rhythms
- scales
- bass clef?

Sheets
- know your fretboard: single note, natural, flat, sharps, all keys. 
- triads in 251 progressions

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
