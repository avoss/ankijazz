
rm -f *.midi *.mp3 *.png

for i in *.ly; do
	name=$(basename -s '.ly' $i)
	echo $name
	lilypond --png -dresolution=200 "${name}.ly" \
		&& timidity --adjust-tempo=200 "${name}.midi" -Ow \
		&& normalize-audio "${name}.wav" \
		&& lame "${name}.wav" "${name}.mp3"
done
