#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd $DIR

SELF=`basename $0`
SOURCE_BRANCH="master"
DEST_BRANCH="gh-pages"
TMP_DIR="tmp"

#git checkout $SOURCE_BRANCH
jekyll build -d $DIR/jekyll-site/ -s $DIR/jekyll-bootstrap/
#git checkout $DEST_BRANCH
# This will remove previous files, which we may not want (e.g. CNAME)
# git rm -qr .
#cp -r $DIR/jekyll-site/. .
# Delete this script from the output
#rm ./$SELF
#rm -r $TMP_DIR
#git add -A
#git commit -m "Published updates"
# May not want to push straight away
#git push origin gh-pages
#git checkout $SOURCE_BRANCH
