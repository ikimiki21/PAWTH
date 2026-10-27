$ErrorActionPreference = 'Stop'
$pawthRoot = Split-Path $PSScriptRoot -Parent
$pawthRuntime = Join-Path $pawthRoot 'runtime\mysql'
$pawthBin = Join-Path $pawthRuntime 'mysql-8.4.13-winx64\bin'
$pawthDefaults = Join-Path $pawthRuntime 'my.ini'
$pawthClientDefaults = Join-Path $pawthRuntime 'admin.ini'
$pawthSocket = [System.Net.Sockets.TcpClient]::new()
try { $pawthConnected = $pawthSocket.ConnectAsync('127.0.0.1', 3306).Wait(1000) -and $pawthSocket.Connected }
catch { $pawthConnected = $false }
finally { $pawthSocket.Dispose() }
if ($pawthConnected) {
    $pawthDataDir = & "$pawthBin\mysql.exe" "--defaults-extra-file=$pawthClientDefaults" -N -B -e 'SELECT @@datadir;' 2>$null
    if ($LASTEXITCODE -ne 0) { throw 'Port 3306 is occupied by another server, or local credentials are invalid.' }
    $pawthExpected = [System.IO.Path]::GetFullPath((Join-Path $pawthRuntime 'data')).TrimEnd('\','/')
    if ([System.IO.Path]::GetFullPath($pawthDataDir.Trim()).TrimEnd('\','/') -ne $pawthExpected) { throw 'Port 3306 is occupied by another MySQL installation.' }
    Write-Host 'PAWTH MySQL is already running.'
    exit 0
}
Start-Process -FilePath "$pawthBin\mysqld.exe" -ArgumentList ('--defaults-file="' + $pawthDefaults + '"') -WindowStyle Hidden
for ($pawthAttempt = 0; $pawthAttempt -lt 60; $pawthAttempt++) {
    Start-Sleep -Seconds 1
    & "$pawthBin\mysql.exe" "--defaults-extra-file=$pawthClientDefaults" -N -B -e 'SELECT 1;' 2>$null | Out-Null
    if ($LASTEXITCODE -eq 0) { Write-Host 'PAWTH MySQL is running at 127.0.0.1:3306.'; exit 0 }
}
throw "MySQL did not start. Check $pawthRuntime\mysql-error.log"
