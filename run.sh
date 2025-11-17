
#!/bin/bash
# Run script for Pacman Phase 1
# UNESP - Estruturas de Dados II

echo "=== Starting Pacman Phase 1 ==="
echo "Graph Algorithms & Intelligent Ghost AI"
echo

# Check if compiled
if [ ! -d "bin" ] || [ -z "$(ls -A bin)" ]; then
    echo "⚠️  Project not compiled. Running compilation..."
    ./compile.sh
    echo
fi

# Run the game
java -cp "lib/*:bin" App
