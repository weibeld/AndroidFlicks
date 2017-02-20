#!/bin/bash
#
# Resize an image to a batch of output sizes.
#
# Requires ImageMagick (convert)
#-----------------------------------------------------------------------------#

set -e

# 1st arg: image to resize (must be in current directory)
in_file=$1
# 2nd arg: output sizes
sizes=$2
# 3rd arg: output directory
out_dir=${3%/}

# Body and extension of the input file name    
base=${in_file%.*}
ext=${in_file##*.}

# Resize
for s in $sizes; do
  out_file="$out_dir/${base}_${s}.${ext}"
  convert "$in_file" -resize "$s" "$out_file"
  echo "$out_file"
done

