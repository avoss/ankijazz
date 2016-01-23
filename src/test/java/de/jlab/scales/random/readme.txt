use-case:

scott: phrases are connected by ending with the same rhythmic shape - optionally shifted in time
phrase1 = random()
phrase2 = random();
ending = random();
result = phrase1 + ending + phrase2 + ending;

rhythmic phrases should have a length.

Applies to both: note-start and note-length! 
This would remove the lengthGenerator from Instrument 
... or rhythmGenerator and lengthGenerator should be somehow interconnected
... similar to chordGenerator and melodyGenerator ...
need a recorder to record chord choices and playback them to many instruments