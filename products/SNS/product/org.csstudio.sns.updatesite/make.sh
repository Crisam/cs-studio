#!/bin/sh
#
# Script that triggers a build of "everything"
#
# In principle, this should be an ANT build.xml,
# but the ant copy tasks were really slow compared to rsync.
# Maybe use a 'system' ant task to call rsync?
# In any case, this one is good enough for now.
#
# Kay Kasemir

source settings.sh

# Fetch new copy of sources
ant clean

echo Fetching sources
ant get_sources

PRODS="config_build_Basic_CSS config_build_SNS_CSS config_build_optional"

# Build products and optional feature
for prod in $PRODS
do
    export PROD=$prod
    echo $prod
    (cd $prod; sh build.sh)
    echo Done with $prod
done

OK=1
# Each build log contains 2(!) "BUILD SUCCESSFUL" lines
for prod in $PRODS
do
    if [ `cat $BUILDDIR/$prod.log | grep -c "BUILD SUCCESSFUL"` -eq 2 ]
    then
        echo OK: $prod
    else
        echo Build failed: $prod
        OK=0
    fi
done

if [ $OK = 1 ]
then
    echo Collecting ZIP files
    mkdir -p $BUILDDIR/apps
    
    ## Basic EPICS
    sh patch_product.sh I.epics_css_$VERSION/epics_css_$VERSION-macosx.$OSWIN.x86.zip  CSS_EPICS_$VERSION apps/epics_css_$VERSION-macosx.$OSWIN.x86.zip
    sh patch_product.sh I.epics_css_$VERSION/epics_css_$VERSION-linux.gtk.x86.zip      CSS_EPICS_$VERSION apps/epics_css_$VERSION-linux.gtk.x86.zip
    sh patch_product.sh I.epics_css_$VERSION/epics_css_$VERSION-linux.gtk.x86_64.zip   CSS_EPICS_$VERSION apps/epics_css_$VERSION-linux.gtk.x86_64.zip
    sh patch_product.sh I.epics_css_$VERSION/epics_css_$VERSION-win32.win32.x86.zip    CSS_EPICS_$VERSION apps/epics_css_$VERSION-win32.win32.x86.zip
    sh patch_product.sh I.epics_css_$VERSION/epics_css_$VERSION-win32.win32.x86_64.zip CSS_EPICS_$VERSION apps/epics_css_$VERSION-win32.win32.x86_64.zip

    ## SNS CSS
    # OS X
    sh patch_product.sh I.sns_css_$VERSION/sns_css_$VERSION-macosx.$OSWIN.x86.zip      CSS_$VERSION      apps/sns_css_$VERSION-macosx.$OSWIN.x86.zip
	sh patch_product.sh I.sns_css_$VERSION/sns_css_$VERSION-linux.gtk.x86.zip          CSS_$VERSION      apps/sns_css_$VERSION-linux.gtk.x86.zip
	sh patch_product.sh I.sns_css_$VERSION/sns_css_$VERSION-linux.gtk.x86_64.zip       CSS_$VERSION      apps/sns_css_$VERSION-linux.gtk.x86_64.zip
	sh patch_product.sh I.sns_css_$VERSION/sns_css_$VERSION-win32.win32.x86.zip        CSS_$VERSION      apps/sns_css_$VERSION-win32.win32.x86.zip
	sh patch_product.sh I.sns_css_$VERSION/sns_css_$VERSION-win32.win32.x86_64.zip     CSS_$VERSION      apps/sns_css_$VERSION-win32.win32.x86_64.zip

    ## Optional feature is already in buildRepo

    ## Source code
    ant zip_sources
fi

