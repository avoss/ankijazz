runLily() {
  name=$(basename -s '.ly' $1) &&
  lilypond --png -dresolution=200 "${name}.ly"
}
export -f runLily

find . -name '*.ly' | parallel runLily

runSynth() {
  name=$(basename -s ".midi" $1) &&
  timidity "${name}.midi" -Ow &&
  normalize-audio --quiet "${name}.wav" &&
  lame --quiet "${name}.wav" "${name}.mp3" &&
  rm "${name}.wav"
}
export -f runSynth

find . -name '*.midi' | parallel runSynth


#for i in *.ly; do
#  name=$(basename -s '.ly' $i) &&
#  lilypond --png -dresolution=200 "${name}.ly"
#done
#
#for i in *.midi; do
#  name=$(basename -s '.midi' $i) &&
#  timidity "${name}.midi" -Ow &&
#  normalize-audio "${name}.wav" &&
#  lame "${name}.wav" "${name}.mp3" &&
#  rm "${name}.wav"
#done
