#!/usr/bin/env bash
SECONDS=0
./gradlew :about:test
./gradlew :base:test
./gradlew :categories:test
./gradlew :search:test
./gradlew :common:test
./gradlew :core:test
./gradlew :network:test

./gradlew :about:createDebugUnitTestCoverageReport
./gradlew :network:createDebugUnitTestCoverageReport
./gradlew :categories:createDebugUnitTestCoverageReport
./gradlew :common:createDebugUnitTestCoverageReport
./gradlew :core:createDebugUnitTestCoverageReport
./gradlew :network:createDebugUnitTestCoverageReport
duration=$SECONDS
echo "$(($duration / 60)) minutes and $(($duration % 60)) seconds elapsed."
exit 1