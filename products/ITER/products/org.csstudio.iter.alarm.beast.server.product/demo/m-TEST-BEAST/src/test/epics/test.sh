#!/bin/bash
#+======================================================================
# $HeadURL: https://svnpub.iter.org/codac/iter/codac/dev/units/m-iter-units-api/branches/codac-core-4.0/src/main/resources/test.sh $
# $Id: test.sh 33491 2013-01-20 18:21:08Z zagara $
#
# Project       : CODAC Core System
#
# Description   : Test script
#
# Author        : Cosylab
#
# Copyright (c) : 2010-2013 ITER Organization,
#                 CS 90 046
#                 13067 St. Paul-lez-Durance Cedex
#                 France
#
# This file is part of ITER CODAC software.
# For the terms and conditions of redistribution or use of this software
# refer to the file ITER-LICENSE.TXT located in the top level directory
# of the distribution package.
#
#-======================================================================

######################################################################
# Test scripts
######################################################################

# Add your test scripts in TEST_SCRIPTS list. They
# will be executed in order.
# 
# The test script must return non-zero value if it
# meets any error. Please use test_template.[sh|pl]
# to write a new test script.
#
# Each test script must have executable permission.
# Don't forget to add "./" before the script file name.
TEST_SCRIPTS=("mvn iter:run -Ddaemon=true -v")

# Add your test scripts that should be executed
# after the whole test script procedure. The scripts
# listed in ENSURE_SCRIPTS will be executed even if
# test procedure forcibly quits by user interruption.
# 
# The test script must return non-zero value if it
# meets any error. Please use test_template.[sh|pl]
# to write a new test script.
ENSURE_SCRIPTS=("mvn iter:stop -v")

######################################################################
# Test execution
######################################################################

source sunit
run_tests

exit $?
