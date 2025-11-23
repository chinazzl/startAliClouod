@echo off
rem AliCloud Web 应用启动脚本 (Windows)
rem Usage: start.bat [dev|prod|test] [start|stop|restart|status]

setlocal enabledelayedexpansion

rem 设置变量
set SCRIPT_DIR=%~dp0
set APP_DIR=%SCRIPT_DIR%..
set APP_NAME=ali-cloud-web
set JAR_FILE=%APP_DIR%\lib\ali-cloud-web.jar

rem JVM参数
set JVM_OPTS=-Xms512m -Xmx2048m
set JVM_OPTS=%JVM_OPTS% -XX:+UseG1GC
set JVM_OPTS=%JVM_OPTS% -XX:MaxGCPauseMillis=200
set JVM_OPTS=%JVM_OPTS% -XX:+HeapDumpOnOutOfMemoryError
set JVM_OPTS=%JVM_OPTS% -XX:HeapDumpPath=%APP_DIR%\logs\heapdump.hprof

rem Spring Boot参数
set SPRING_OPTS=--spring.config.location=%APP_DIR%\config\
set SPRING_OPTS=%SPRING_OPTS% --spring.profiles.active=%~1
set SPRING_OPTS=%SPRING_OPTS% --logging.file.path=%APP_DIR%\logs

rem PID文件 (Windows下使用标题窗口进程名)
rem set PID_FILE=%APP_DIR%\app.pid

rem 获取参数
set ENV=%~1
if "%ENV%"=="" set ENV=dev

set COMMAND=%~2
if "%COMMAND%"=="" set COMMAND=start

rem 创建必要目录
if not exist "%APP_DIR%\logs" mkdir "%APP_DIR%\logs"
if not exist "%APP_DIR%\temp" mkdir "%APP_DIR%\temp"

rem 检查Java环境
where java >nul 2>nul
if %errorlevel% neq 0 (
    echo [ERROR] Java not found in PATH
    exit /b 1
)

rem 启动应用
if "%COMMAND%"=="start" goto :start
if "%COMMAND%"=="stop" goto :stop
if "%COMMAND%"=="restart" goto :restart
if "%COMMAND%"=="status" goto :status
goto :help

:start
echo [INFO] Starting %APP_NAME%...
echo [INFO] Environment: %ENV%
echo [INFO] Working directory: %APP_DIR%
echo [INFO] JAR file: %JAR_FILE%

if not exist "%JAR_FILE%" (
    echo [ERROR] JAR file not found: %JAR_FILE%
    exit /b 1
)

rem 启动应用
cd /d "%APP_DIR%"
start "AliCloud-Web" java %JVM_OPTS% -jar "%JAR_FILE%" %SPRING_OPTS%

echo [INFO] Application started in new window
echo [INFO] Log files will be in: %APP_DIR%\logs
goto :end

:stop
echo [INFO] Stopping application...
taskkill /f /im java.exe /fi "windowtitle eq AliCloud-Web*" 2>nul
if %errorlevel% equ 0 (
    echo [INFO] Application stopped successfully
) else (
    echo [WARN] Application may not be running or couldn't be stopped
)
goto :end

:restart
echo [INFO] Restarting application...
call :stop
timeout /t 2 /nobreak >nul
call :start %ENV%
goto :end

:status
tasklist /fi "windowtitle eq AliCloud-Web*" 2>nul | find "java.exe" >nul
if %errorlevel% equ 0 (
    echo [INFO] Application is running
) else (
    echo [INFO] Application is not running
)
goto :end

:help
echo Usage: %0 [environment] [command]
echo.
echo Environment:
echo   dev     Development environment (default)
echo   prod    Production environment
echo   test    Test environment
echo.
echo Commands:
echo   start   Start the application
echo   stop    Stop the application
echo   restart Restart the application
echo   status  Show application status
echo.
echo Examples:
echo   %0 dev start     # Start in development mode
echo   %0 prod restart  # Restart in production mode
echo   %0 status        # Show status

:end