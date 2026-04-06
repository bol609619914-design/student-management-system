$mysqlBase = "C:\Program Files\MySQL\MySQL Server 8.4\bin"
$configFile = "C:\MySQL84\my.ini"

$running = Get-Process mysqld -ErrorAction SilentlyContinue
if ($running) {
    Write-Output "MySQL is already running."
    exit 0
}

Start-Process -FilePath (Join-Path $mysqlBase "mysqld.exe") -ArgumentList "--defaults-file=$configFile" | Out-Null
Start-Sleep -Seconds 5

$check = Get-Process mysqld -ErrorAction SilentlyContinue
if ($check) {
    Write-Output "MySQL started successfully."
} else {
    Write-Output "MySQL failed to start."
    exit 1
}
