#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd $DIR

SELF=`basename $0`
SOURCE_BRANCH="master"
DEST_BRANCH="gh-pages"
TMP_DIR="tmp"

lessc ~/Installed/bootstrap-3.1.1/less/bootstrap.less > ~/Projects/github/sbt-neo-dependencies/docs/jekyll/assets/css/bootstrap.css
jekyll serve -s $DIR/jekyll-bootstrap/ --config $DIR/jekyll/_config.yml
