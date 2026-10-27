$ErrorActionPreference = 'Stop'
$pawthRoot = Split-Path $PSScriptRoot -Parent
$pawthRuntime = Join-Path $pawthRoot 'runtime\mysql'
& "$pawthRuntime\mysql-8.4.13-winx64\bin\mysqladmin.exe" "--defaults-extra-file=$pawthRuntime\admin.ini" shutdown
if ($LASTEXITCODE -ne 0) { throw 'MySQL shutdown failed.' }
Write-Host 'PAWTH MySQL stopped.'
