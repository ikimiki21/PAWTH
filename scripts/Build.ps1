$ErrorActionPreference='Stop'
$pawthRoot=Split-Path $PSScriptRoot -Parent
$pawthSources=@("$pawthRoot\Main.java")
foreach($pawthFolder in @('config','dao','model','service','util','view')){
    $pawthSources+=@(Get-ChildItem -LiteralPath "$pawthRoot\$pawthFolder" -Filter *.java -Recurse | ForEach-Object {$_.FullName})
}
New-Item -ItemType Directory -Path "$pawthRoot\out\production\PAWTH" -Force | Out-Null
javac -encoding UTF-8 --release 17 -cp "$pawthRoot\lib\*" -d "$pawthRoot\out\production\PAWTH" @pawthSources
if($LASTEXITCODE -ne 0){throw 'Java compilation failed'}
Write-Host 'PAWTH compiled successfully.'
