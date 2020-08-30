rm -f *.midi *.wav *.mp3 *.png
for i in *.ly; do
	name=$(basename -s '.ly' $i)
	echo $name
	lilypond --png -dresolution=200 "${name}.ly" \
		&& timidity "${name}.midi" -Ow \
		&& normalize-audio "${name}.wav" \
		&& lame "${name}.wav" "${name}.mp3" \
		&& rm "${name}.wav" "${name}.midi"
done
convert *.png +strip anki.pdf
