echo "Running AnkiJazz in " $1

runLily() {
  name=$(basename -s '.ly' $1) &&
  lilypond --png -dresolution=200 "${name}.ly"
}
export -f runLily

runSynth() {
  name=$(basename -s ".midi" $1) &&
  timidity "${name}.midi" -Ow &&
  normalize-audio --quiet "${name}.wav" &&
  lame --quiet "${name}.wav" "${name}.mp3" &&
  rm "${name}.wav"
}
export -f runSynth

(cd $1 && find . -name '*.ly' | parallel runLily)
(cd $1 && find . -name '*.midi' | parallel runSynth)


