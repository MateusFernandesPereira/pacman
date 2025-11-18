
@echo off
echo ====================================
echo   COMPILANDO PACMAN - FASE 1
echo ====================================
echo.

REM Criar diretorio bin se nao existir
if not exist "bin" mkdir bin

REM Gerar sources.txt dinamicamente
echo Gerando lista de arquivos fonte...
dir /s /b src\*.java > sources.txt

REM Compilar todos os arquivos Java
echo Compilando...
javac -d bin -cp "lib/*" @sources.txt

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ====================================
    echo   COMPILACAO CONCLUIDA COM SUCESSO
    echo ====================================
    echo.
    echo Para executar o jogo, use:
    echo   run.bat
    echo.
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

pause
