# Build parser and lexer on Windows and compile Java classes
# Usage: run from anywhere: powershell -ExecutionPolicy Bypass -File utils\build-windows.ps1

Set-StrictMode -Version Latest
$ErrorActionPreference = 'Stop'

$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$repoRoot  = Split-Path -Parent $scriptDir
$parserDir = Join-Path $repoRoot 'parser'

function Ensure-PackageHeader {
    param(
        [Parameter(Mandatory=$true)][string]$Path
    )
    if (-not (Test-Path $Path)) { return }
    $raw = Get-Content -LiteralPath $Path -Raw -ErrorAction Stop
    # Strip UTF-8 BOM if present
    if ($raw.Length -gt 0 -and [int]$raw[0] -eq 0xFEFF) {
        $raw = $raw.Substring(1)
    }
    if (-not $raw.StartsWith('package parser;')) {
        $raw = "package parser;`r`n" + $raw
    }
    # Write as UTF-8 (no BOM) to support acentuação
    $utf8NoBom = New-Object System.Text.UTF8Encoding($false)
    [System.IO.File]::WriteAllText($Path, $raw, $utf8NoBom)
}

function Convert-ToUtf8NoBom {
    param(
        [Parameter(Mandatory=$true)][string]$Path
    )
    if (-not (Test-Path $Path)) { return }
    # Read as bytes then decode using the system default encoding if needed
    $bytes = [System.IO.File]::ReadAllBytes($Path)
    $defaultEnc = [System.Text.Encoding]::Default
    $text = $defaultEnc.GetString($bytes)
    $utf8NoBom = New-Object System.Text.UTF8Encoding($false)
    [System.IO.File]::WriteAllText($Path, $text, $utf8NoBom)
}

Write-Host 'Generating lexer with JFlex...'
Push-Location $parserDir
& java -jar (Join-Path $scriptDir 'JFlex.jar') 'exemploGC.flex'

Write-Host 'Generating parser with byacc/j (yacc.exe)...'
& (Join-Path $scriptDir 'yacc.exe') -J -tv 'exemploGC.y'

# JFlex re-generates Yylex.java without package; ensure it
Ensure-PackageHeader -Path (Join-Path $parserDir 'Yylex.java')
# ParserVal.java is part of repo but ensure consistency too
Ensure-PackageHeader -Path (Join-Path $parserDir 'ParserVal.java')
# Normalize encoding of all Java sources to UTF-8 (no BOM)
Get-ChildItem -LiteralPath $parserDir -Filter *.java | ForEach-Object {
    Convert-ToUtf8NoBom -Path $_.FullName
}
Pop-Location

Write-Host 'Compiling Java sources...'
# Build argument array for javac
$javaFiles = Get-ChildItem -LiteralPath $parserDir -Filter *.java | Select-Object -ExpandProperty FullName
if (-not $javaFiles) { throw 'No Java sources found under parser/' }
& javac -encoding UTF-8 -d $repoRoot @javaFiles

Write-Host 'Build completed successfully.' -ForegroundColor Green
