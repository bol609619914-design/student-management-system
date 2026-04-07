param(
    [string]$OutputRoot = "dist"
)

$ErrorActionPreference = "Stop"

$projectRoot = Split-Path -Parent $PSScriptRoot
$outputRootPath = Join-Path $projectRoot $OutputRoot
$packageRoot = Join-Path $outputRootPath "student-management-system-demo"
$appRoot = Join-Path $packageRoot "app"
$runtimeRoot = Join-Path $packageRoot "runtime"
$dataRoot = Join-Path $packageRoot "data"
$jarSource = Join-Path $projectRoot "target\\student-management-system-1.0.0.jar"
$jarTarget = Join-Path $appRoot "student-management-system.jar"
$zipPath = Join-Path $outputRootPath "student-management-system-demo.zip"

if (-not $env:JAVA_HOME) {
    throw "JAVA_HOME is required for packaging."
}

Push-Location $projectRoot
try {
    mvn clean package -DskipTests
    if ($LASTEXITCODE -ne 0) {
        throw "Maven package failed."
    }
}
finally {
    Pop-Location
}

if (Test-Path $packageRoot) {
    Remove-Item $packageRoot -Recurse -Force
}
if (Test-Path $zipPath) {
    Remove-Item $zipPath -Force
}

New-Item -ItemType Directory -Path $appRoot -Force | Out-Null
New-Item -ItemType Directory -Path $runtimeRoot -Force | Out-Null
New-Item -ItemType Directory -Path $dataRoot -Force | Out-Null

Copy-Item $jarSource $jarTarget -Force
Copy-Item (Join-Path $env:JAVA_HOME "*") $runtimeRoot -Recurse -Force

$startBat = @"
@echo off
setlocal
cd /d %~dp0
set APP_PORT=%1
if "%APP_PORT%"=="" set APP_PORT=8080
set JAVA_CMD=%~dp0runtime\bin\java.exe
if not exist "%JAVA_CMD%" set JAVA_CMD=java
start "" http://localhost:%APP_PORT%/login
"%JAVA_CMD%" -jar app\student-management-system.jar --spring.profiles.active=demo --server.port=%APP_PORT%
endlocal
"@

$startPs1 = @'
param(
    [int]$Port = 8080
)

$ErrorActionPreference = "Stop"
$root = Split-Path -Parent $MyInvocation.MyCommand.Path
$javaCmd = Join-Path $root "runtime\bin\java.exe"
$jarPath = Join-Path $root "app\student-management-system.jar"
if (-not (Test-Path $javaCmd)) {
    $javaCmd = "java"
}

Start-Process "http://localhost:$Port/login"
& $javaCmd "-jar" $jarPath "--spring.profiles.active=demo" "--server.port=$Port"
'@

$readme = @"
Student Management System Demo Package
=====================================

This package is prepared for fast demo deployment.
It includes:
- bundled Java runtime
- Spring Boot executable jar
- embedded file database profile

How to start:
1. Double click start-demo.bat
2. Or run .\start-demo.ps1 in PowerShell

Default URL:
http://localhost:8080/login

Demo accounts:
- admin / admin123
- teacher / teacher123
- student / student123

Tips:
- The first startup will create local data files in the data folder.
- If port 8080 is busy, use start-demo.bat 8081 or .\start-demo.ps1 -Port 8081.
"@

Set-Content -Path (Join-Path $packageRoot "start-demo.bat") -Value $startBat -Encoding ASCII
Set-Content -Path (Join-Path $packageRoot "start-demo.ps1") -Value $startPs1 -Encoding UTF8
Set-Content -Path (Join-Path $packageRoot "README-quickstart.txt") -Value $readme -Encoding UTF8

Compress-Archive -Path $packageRoot -DestinationPath $zipPath -Force

Write-Output "Package created: $zipPath"
