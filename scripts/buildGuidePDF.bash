#!/bin/bash

mkdir -p /tmp/manual
cd /tmp/manual || exit

# curl downloads the index page of the website
# grep extracts the <nav> ... </nav> section
# sed(1) injects a line break in front of every URL and adds the full domain
# sed(2) deletes from each line the " character and everything that follows, leaving the clean URL
# tail deletes the first line, which contains a lonely <nav> tag

urlstr=$(curl -s "https://guide.openrndr.org" | grep -o -E '<nav .*</nav>' | sed "s/href=\"\//href=\"\nhttps:\/\/guide.openrndr.org\//g" | sed "s/\".*//g" | tail +2)

# convert a long string into an array
urls=($urlstr)

# count how many items in the array
length=${#urls[@]}

echo "Found $length URLs"

# one by one create NNNN.pdf files from each URL
for (( i=0; i<${length}; i++ ));
do
  padded=$(printf "%04d" $i)
  wkhtmltopdf ${urls[$i]} $padded.pdf
done

date=$(date +"%F")

# finally join all the PDF files into one
pdfunite *.pdf /tmp/openrndr-guide-$date.pdf
