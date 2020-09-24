# Scales Core


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

## BUGS
- KeySignature.notationMap contains flats and sharps, should use accidental of keysignature to notate notes not in scale.
- limit number of b and # in key signature to 6 (Marcus), put additional b/# into staff
- C altered is notated as B# altered because C altered is 7th mode of C# MM and C# Aeolean is parallel major of E-Major

## Accidentals
- scales und chords sind doch verschieden, chords beziehen sich immer auf eine Scale, zumindest erbt er die Vorzeichen
- Gb und F# haben ces und eis. Bei 7-Ton scales kann jeder Buchstabe nur einmal vergeben werden. Wie funktioniert das bei diminished scale?
- Major Modes erben vorzeichen von parent-scale. HM/MM erben Vorzeichen von relative major. Bei Minor Pentatonic nicht klar, vermutlich dorisch zu sehen

## Special cases
- B Lydian wäre korrekt Cb Lydian weil Gb major scale Cb statt B enthält
- C Altered wäre korrekt B# Altered weil Calt = 7th mode of C# MM and relative minor of C# is E (MM with aeolean notation)

special cases:

- G#  Harmonic Minor, Signature:  B (5#), Notation: G# A# B C# D# E Fx // bb or ##
- Db  Harmonic Major, Signature: Db (5b), Notation: Db Eb F Gb Ab Bbb C // bb or ##
- Gb  Harmonic Major, Signature: Gb (6b), Notation: Gb Ab Bb Cb Db Ebb F // bb or ##

Major Scales and Modes:
- F#/Gb: no one better than the other, need both - why is it so complicated? Modes always have enharmonic roots Gb has Cb and F# has E#
- Cb Lydian, Signature: Gb (6b), Notation: Cb Db Eb F Gb Ab Bb // enharmonic root

KeySignature strict:
- Modes use signature of parent scale
- HM and MM use signature of Aeolean
- result contains double bb and ##, enharmonic roots

KeySignature simplified:
- same as above
- in case of bb/## use inverse accidental and limit signature to 6 accidentals
- enharmonic root is replaced with accidental applied (e.g. E# is replaced with F) TODO: all or just root?

## TODO
- migrate to junit 5

# Anki


## TODO
Decks:
- learn enharmonics Ab -> G# etc
- learn scale notes (text only)
- parent scales (done)
- spell types (done)

Bugs:
- double ## and bb are completely eliminated by choosing the inverse accidental! No need to take care in badness() or so.
- have fallback() with notationKey and badness()
- enharmonic roots (e.g. B# Altered instead of C Altered)!
- scales, modes, w/ or w/o guitar should share media
- add scaleNotes back to deck?


### Midi
- know your fretboard: single note, natural, flat, sharps, all keys. 
  - in different fretboard positions.
  - single note on every string strings

## features:
- the parallel Minor of B Major is G# Minor. Notation of G# Melodic Minor would require double sharps. To avoid this, it is notated as Ab. All scales, which require double sharps or double flats, are notated with the enharmonic "replacement" if that avoids the double sharps / double flats.
- enharmonic roots (e.g. B# Altered instead of C Altered) removed?

- sorted by number of accidentals with some randomness
- since not answering a single word which is fast, playing a scale or chord progression takes time. Need to adjust settings. Here are mine:
  - preferences -> scheduling: use new 2.1 scheduler and select "show new cards after review" to make sure repetition is completed even if you don't have time for new cards on a certain day
  - in deck options change learning steps from "1 10" to "20 1440" and increase graduating interval to 3 days
  - similar in lapses section change re-learning steps from "1 10" to "20 1440"
  - in "general" tab change "ignore answer time" to at least 1 minute (or how long it takes to play a scale or some chords)
- harmonic minor notated with key signature of aeolean 
- meldodic minor is "jazz melodic minor", ascending and descending the same notes
- melodic minor notated with key signature of dorian
- //diminished and whole tone are notated with flats / naturals only
- minor 251 key = relative major key

## noteworthy
- Ab/G# harmonic minor requires double-sharp
- Db and Gb harmonic major require double-flat
- CMMin, GHMin, DHMin, GHMaj and DHMaj contain both, # and b ??? double check!

## done
- symbol b and # replace with unicode -> fonts don't support this
