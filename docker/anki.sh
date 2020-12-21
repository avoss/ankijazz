for i in *.ly; do
	name=$(basename -s '.ly' $i) &&
	echo $name &&
	lilypond --png -dresolution=200 "${name}.ly"
done

for i in *.midi; do
  name=$(basename -s '.midi' $i) &&
  timidity "${name}.midi" -Ow &&
  normalize-audio "${name}.wav" &&
  lame "${name}.wav" "${name}.mp3" &&
  m "${name}.wav"
done
