#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/.."
mkdir -p build/classes
find src/main/java -name '*.java' > build/sources.txt
javac --release 17 -encoding UTF-8 -cp 'lib/*' -d build/classes @build/sources.txt
cp src/main/resources/* build/classes/
jar --create --file build/cineman.jar --main-class vn.cineman.Main0 -C build/classes .
