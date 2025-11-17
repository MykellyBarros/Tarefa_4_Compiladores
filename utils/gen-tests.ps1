# Generate .s assembly outputs for all .cmm tests using the built parser
# Usage: run from anywhere: powershell -ExecutionPolicy Bypass -File utils\gen-tests.ps1

Set-StrictMode -Version Latest
$ErrorActionPreference = 'Stop'

$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$repoRoot  = Split-Path -Parent $scriptDir
$parserDir = Join-Path $repoRoot 'parser'
$testsDir  = Join-Path $repoRoot 'testes'

if (-not (Test-Path (Join-Path $parserDir 'Parser.class'))) {
    Write-Warning 'No compiled classes found. Running build first...'
    & (Join-Path $scriptDir 'build-windows.ps1')
}

$tests = Get-ChildItem -LiteralPath $testsDir -Filter *.cmm | Sort-Object Name
if (-not $tests) {
    Write-Host 'No .cmm tests found in testes/.'
    exit 0
}

foreach ($t in $tests) {
    $in  = $t.FullName
    $out = Join-Path $parserDir ($t.BaseName + '.s')
    Write-Host ("Generating {0}" -f [System.IO.Path]::GetFileName($out))
    # Pipe stdout to .s (assembly); errors will still show in console
    & java -cp $repoRoot parser.Parser $in | Out-File -Encoding ASCII -LiteralPath $out
}

Write-Host 'All tests processed.' -ForegroundColor Green
