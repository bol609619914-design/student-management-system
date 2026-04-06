$running = Get-Process mysqld -ErrorAction SilentlyContinue
if (-not $running) {
    Write-Output "MySQL is not running."
    exit 0
}

$running | Stop-Process -Force
Write-Output "MySQL stopped successfully."
