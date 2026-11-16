#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/.."
bash scripts/build.sh
java -cp 'build/cineman.jar:lib/*' vn.cineman.Main0
