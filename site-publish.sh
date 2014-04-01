#!/bin/bash

DIR="/Users/dzsessona/Projects/github/sbt-neo-dependencies"
cd $DIR

SOURCE_BRANCH="docs"
DEST_BRANCH="gh-pages"

git checkout $SOURCE_BRANCH
git add -A
git commit -m "documentation"
jekyll build -d $DIR/jekyll-site/ -s $DIR/jekyll/ --config $DIR/_config.yml
git checkout $DEST_BRANCH
# This will remove previous files, which we may not want (e.g. CNAME)
cp -r $DIR/jekyll-site/. .
git add -A
git commit -m "Published updated site"
# May not want to push straight away
git push origin $DEST_BRANCH
git checkout $SOURCE_BRANCH
git push origin $SOURCE_BRANCH
