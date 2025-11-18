@echo off
echo ====================================
echo   COMPILANDO PACMAN
echo ====================================
echo.

if not exist "bin" mkdir bin

REM Copiar imagens para bin
xcopy /E /I /Y src\Images bin\Images

echo Gerando lista de arquivos fonte...
dir /s /b src\*.java > sources.txt

echo Compilando...
javac -d bin -cp "lib/*" @sources.txt

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ====================================
    echo   COMPILACAO CONCLUIDA COM SUCESSO
    echo ====================================
    echo.

    :ask
    set /p "choice=Deseja rodar? (Y/N): "
    if /i "%choice%"=="Y" goto run
    if /i "%choice%"=="N" goto end
    echo Digite apenas Y ou N
    goto ask

    :run
    call run.bat
    goto end

) else (
    echo.
    echo ====================================
    echo   ERRO NA COMPILACAO
    echo ====================================
    echo.
    echo Verifique:
    echo   1. Java JDK 11+ esta instalado
    echo   2. A biblioteca gson esta em lib/
    echo   3. Todos os arquivos .java estao corretos
    echo.
)

:end
pause