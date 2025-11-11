
#!/bin/bash
# Run script with menu for Pacman Phase 1
# UNESP - Estruturas de Dados II

echo "=== Starting Pacman Phase 1 (with Menu) ==="
echo

# Check if compiled
if [ ! -d "bin" ] || [ -z "$(ls -A bin)" ]; then
    echo "⚠️  Project not compiled. Running compilation..."
    ./compile.sh
    echo
fi

# Run with menu
java -cp "lib/*:bin" MainMenu
