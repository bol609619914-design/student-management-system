$ErrorActionPreference = "Stop"

$projectRoot = Split-Path -Parent (Split-Path -Parent $MyInvocation.MyCommand.Path)
$distRoot = Join-Path $projectRoot "dist"
$bundleRoot = Join-Path $distRoot "student-management-system-full-package"
$zipPath = Join-Path $distRoot "student-management-system-full-package.zip"
$projectCopyRoot = Join-Path $bundleRoot "project"
$runtimeRoot = Join-Path $bundleRoot "runtime"
$scriptsRoot = Join-Path $bundleRoot "scripts"
$docsRoot = Join-Path $bundleRoot "docs"
$sqlRoot = Join-Path $bundleRoot "sql"
$projectTarget = Join-Path $projectCopyRoot "student-management-system"

$javaHome = "C:\Program Files\Eclipse Adoptium\jdk-17.0.18.8-hotspot"
$mavenHome = "C:\Tools\apache-maven-3.9.14"
$mysqlHome = "C:\Program Files\MySQL\MySQL Server 8.4"
$mysqlData = "C:\MySQLData"
$mysqlDefaultsFile = "C:\MySQL84\my.ini"
$mysqlPort = 3306

function Assert-PathExists {
    param([string]$Path, [string]$Label)
    if (-not (Test-Path -LiteralPath $Path)) {
        throw "$Label not found: $Path"
    }
}

function Remove-IfExists {
    param([string]$Path)
    if (Test-Path -LiteralPath $Path) {
        Remove-Item -LiteralPath $Path -Recurse -Force
    }
}

Assert-PathExists -Path $javaHome -Label "JDK"
Assert-PathExists -Path $mavenHome -Label "Maven"
Assert-PathExists -Path $mysqlHome -Label "MySQL"
Assert-PathExists -Path $mysqlData -Label "MySQL data"
Assert-PathExists -Path $mysqlDefaultsFile -Label "MySQL config"

New-Item -ItemType Directory -Force -Path $distRoot | Out-Null
Remove-IfExists -Path $bundleRoot
Remove-IfExists -Path $zipPath

Write-Host "Building project jar..."
& "$mavenHome\bin\mvn.cmd" clean package -DskipTests

Write-Host "Preparing output folders..."
New-Item -ItemType Directory -Force -Path $bundleRoot, $projectCopyRoot, $runtimeRoot, $scriptsRoot, $docsRoot, $sqlRoot | Out-Null

Write-Host "Stopping local MySQL if it is running..."
$listening = Get-NetTCPConnection -LocalPort $mysqlPort -State Listen -ErrorAction SilentlyContinue |
    Where-Object { $_.OwningProcess -gt 0 }
if ($listening) {
    $ownedPids = $listening | Select-Object -ExpandProperty OwningProcess -Unique
    foreach ($processId in $ownedPids) {
        try {
            Stop-Process -Id $processId -Force -ErrorAction Stop
            Write-Host "Stopped process on port ${mysqlPort}: $processId"
        } catch {
            Write-Warning "Could not stop process $processId"
        }
    }
    Start-Sleep -Seconds 3
}

Write-Host "Copying project source..."
$copiedProject = Join-Path $projectCopyRoot "student-management-system"
New-Item -ItemType Directory -Force -Path $copiedProject | Out-Null
$roboLog = Join-Path $distRoot "robocopy-package.log"
$roboArgs = @(
    $projectRoot,
    $copiedProject,
    "/E",
    "/R:1",
    "/W:1",
    "/XD", ".git", "dist", "target",
    "/XF", "run-8081.out.log", "run-8081.err.log"
)
& robocopy @roboArgs | Tee-Object -FilePath $roboLog | Out-Null
$roboExit = $LASTEXITCODE
if ($roboExit -ge 8) {
    throw "Project copy failed. See $roboLog"
}

Write-Host "Copying runtimes..."
Copy-Item -Path $javaHome -Destination (Join-Path $runtimeRoot "jdk") -Recurse -Force
Copy-Item -Path $mavenHome -Destination (Join-Path $runtimeRoot "maven") -Recurse -Force
Copy-Item -Path $mysqlHome -Destination (Join-Path $runtimeRoot "mysql") -Recurse -Force
Copy-Item -Path $mysqlData -Destination (Join-Path $runtimeRoot "mysql-data") -Recurse -Force

Write-Host "Copying docs and SQL..."
Get-ChildItem -Path $projectRoot -Filter *.md -File | ForEach-Object {
    Copy-Item -Path $_.FullName -Destination $docsRoot -Force
}
Copy-Item -Path (Join-Path $projectRoot "sql\student_management_system.sql") -Destination $sqlRoot -Force

$setEnvPs1 = @'
$bundleRoot = Split-Path -Parent (Split-Path -Parent $MyInvocation.MyCommand.Path)
$env:JAVA_HOME = Join-Path $bundleRoot "runtime\jdk"
$env:MAVEN_HOME = Join-Path $bundleRoot "runtime\maven"
$env:Path = "$env:JAVA_HOME\bin;$env:MAVEN_HOME\bin;$env:Path"
Write-Host "JAVA_HOME=$env:JAVA_HOME"
Write-Host "MAVEN_HOME=$env:MAVEN_HOME"
Write-Host "Environment variables are ready for this terminal."
'@
Set-Content -Path (Join-Path $scriptsRoot "set-env.ps1") -Value $setEnvPs1 -Encoding UTF8

$setEnvBat = @'
@echo off
set "BUNDLE_ROOT=%~dp0.."
set "JAVA_HOME=%BUNDLE_ROOT%\runtime\jdk"
set "MAVEN_HOME=%BUNDLE_ROOT%\runtime\maven"
set "PATH=%JAVA_HOME%\bin;%MAVEN_HOME%\bin;%PATH%"
echo JAVA_HOME=%JAVA_HOME%
echo MAVEN_HOME=%MAVEN_HOME%
echo Environment variables are ready for this window.
'@
Set-Content -Path (Join-Path $scriptsRoot "set-env.bat") -Value $setEnvBat -Encoding ASCII

$startMysqlPs1 = @'
$bundleRoot = Split-Path -Parent (Split-Path -Parent $MyInvocation.MyCommand.Path)
$mysqlRoot = Join-Path $bundleRoot "runtime\mysql"
$dataRoot = Join-Path $bundleRoot "runtime\mysql-data"
$myIni = Join-Path $dataRoot "my.ini"
if (-not (Test-Path -LiteralPath $myIni)) {
    $myIni = Join-Path $bundleRoot "runtime\mysql\my.ini"
}

$listening = Get-NetTCPConnection -LocalPort 3306 -State Listen -ErrorAction SilentlyContinue
if ($listening) {
    Write-Host "MySQL is already running on port 3306."
    exit 0
}

$process = Start-Process -FilePath (Join-Path $mysqlRoot "bin\mysqld.exe") -ArgumentList "--defaults-file=$myIni" -PassThru
Start-Sleep -Seconds 8
$ok = Get-NetTCPConnection -LocalPort 3306 -State Listen -ErrorAction SilentlyContinue
if (-not $ok) {
    throw "Bundled MySQL failed to start."
}

Write-Host "Bundled MySQL started successfully."
Write-Host "ProcessId=$($process.Id)"
'@
Set-Content -Path (Join-Path $scriptsRoot "start-bundled-mysql.ps1") -Value $startMysqlPs1 -Encoding UTF8

$startMysqlBat = @'
@echo off
powershell -ExecutionPolicy Bypass -File "%~dp0start-bundled-mysql.ps1"
'@
Set-Content -Path (Join-Path $scriptsRoot "start-bundled-mysql.bat") -Value $startMysqlBat -Encoding ASCII

$stopMysqlPs1 = @'
$listening = Get-NetTCPConnection -LocalPort 3306 -State Listen -ErrorAction SilentlyContinue |
    Where-Object { $_.OwningProcess -gt 0 }
if (-not $listening) {
    Write-Host "MySQL is not running on port 3306."
    exit 0
}

$ownedPids = $listening | Select-Object -ExpandProperty OwningProcess -Unique
foreach ($processId in $ownedPids) {
    Stop-Process -Id $processId -Force
    Write-Host "Stopped MySQL process: $processId"
}
'@
Set-Content -Path (Join-Path $scriptsRoot "stop-bundled-mysql.ps1") -Value $stopMysqlPs1 -Encoding UTF8

$stopMysqlBat = @'
@echo off
powershell -ExecutionPolicy Bypass -File "%~dp0stop-bundled-mysql.ps1"
'@
Set-Content -Path (Join-Path $scriptsRoot "stop-bundled-mysql.bat") -Value $stopMysqlBat -Encoding ASCII

$quickReadme = @'
Full delivery package contents:
1. project\student-management-system        Source code for IDEA
2. runtime\jdk                             JDK 17
3. runtime\maven                           Maven 3.9.14
4. runtime\mysql                           MySQL 8.4 binaries
5. runtime\mysql-data                      Existing MySQL data directory
6. sql\student_management_system.sql       Database export
7. docs\                                   Chinese deployment docs

Recommended usage on another computer:
1. Install IntelliJ IDEA.
2. Open scripts\start-bundled-mysql.bat to start bundled MySQL.
3. Open project\student-management-system in IDEA.
4. Run StudentManagementSystemApplication.
5. Read docs\部署说明-IDEA+MySQL.md for detailed steps.
'@
Set-Content -Path (Join-Path $bundleRoot "START-HERE.txt") -Value $quickReadme -Encoding UTF8

Write-Host "Compressing final package..."
Compress-Archive -Path (Join-Path $bundleRoot "*") -DestinationPath $zipPath -Force

Write-Host ""
Write-Host "Package created:"
Write-Host $zipPath
