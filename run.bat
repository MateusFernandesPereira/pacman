
@echo off
echo ====================================
echo   EXECUTANDO PACMAN - FASE 1
echo ====================================
echo.

REM Verificar se o projeto foi compilado
if not exist "bin\MainMenu.class" (
    echo ERRO: Projeto nao compilado!
    echo Execute compile.bat primeiro.
    echo.
    pause
    exit /b 1
)

REM Executar o jogo
java -cp "lib/*;bin" MainMenu