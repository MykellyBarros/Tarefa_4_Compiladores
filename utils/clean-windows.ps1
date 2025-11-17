Set-StrictMode -Version Latest
$ErrorActionPreference = 'Stop'

# Resolve repo root (parent of this script's directory)
$repoRoot = Split-Path -Parent $PSScriptRoot
$parserDir = Join-Path $repoRoot 'parser'

Write-Host "Cleaning generated artifacts under: $parserDir"

$targets = @(
    (Join-Path $parserDir '*.class'),
    (Join-Path $parserDir '*.s'),
    (Join-Path $parserDir 'y.output'),
    (Join-Path $parserDir 'Yylex.java~')
)

foreach ($pattern in $targets) {
    $items = Get-ChildItem -Path $pattern -ErrorAction SilentlyContinue
    foreach ($item in $items) {
        try {
            Remove-Item -LiteralPath $item.FullName -Force -ErrorAction Stop
            Write-Host "Removed" $item.FullName
        }
        catch {
            Write-Warning "Could not remove $($item.FullName): $($_.Exception.Message)"
        }
    }
}

Write-Host 'Done.'
