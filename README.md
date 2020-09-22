# Scales Core

## BUGS
- KeySignature.notationMap contains flats and sharps, should use accidental of keysignature to notate notes not in scale.
- limit number of b and # in key signature to 6 (Marcus), put additional b/# into staff
- C altered is notated as B# altered because C altered is 7th mode of 

## Accidentals
- scales und chords sind doch verschieden, chords beziehen sich immer auf eine Scale, zumindest erbt er die Vorzeichen
- Gb und F# haben ces und eis. Bei 7-Ton scales kann jeder Buchstabe nur einmal vergeben werden. Wie funktioniert das bei diminished scale?
- Major Modes erben vorzeichen von parent-scale. HM/MM erben Vorzeichen von relative major. Bei Minor Pentatonic nicht klar, vermutlich dorisch zu sehen

## Special cases
- B Lydian wäre korrekt Cb Lydian weil Gb major scale Cb statt B enthält
- 
- C Altered wäre korrekt B# Altered weil Calt = 7th mode of C# MM and relative minor of C# is E (MM with aeolean notation)

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
