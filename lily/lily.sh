
rm -f *.midi *.mp3 *.png

for i in *.ly; do
	name=$(basename -s '.ly' $i)
	echo $name
	lilypond --png "${name}.ly" && timidity "${name}.midi" -Ow && lame "${name}.wav" "${name}.mp3"
done
