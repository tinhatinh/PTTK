$ErrorActionPreference = "Stop"
Set-Location (Join-Path $PSScriptRoot "..")
New-Item -ItemType Directory -Force build/classes | Out-Null
$sourceFiles = Get-ChildItem -Recurse src/main/java -Filter *.java | ForEach-Object { $_.FullName }
& javac --release 17 -encoding UTF-8 -cp "lib/*" -d build/classes $sourceFiles
if ($LASTEXITCODE -ne 0) { throw "Compilation failed" }
Copy-Item src/main/resources/* build/classes/
& jar --create --file build/cineman.jar --main-class vn.cineman.Main0 -C build/classes .
& java -cp "build/cineman.jar;lib/*" vn.cineman.Main0
