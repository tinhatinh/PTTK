#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/.."
bash scripts/build.sh
find src/test/java -name '*.java' > build/tests.txt
javac --release 17 -encoding UTF-8 -cp 'build/classes:lib/*' -d build/classes @build/tests.txt
java -ea -cp 'build/classes:lib/*' vn.cineman.ProjectTest0
