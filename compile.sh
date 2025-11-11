
#!/bin/bash
# Compilation script for Pacman Phase 1
# UNESP - Estruturas de Dados II

echo "=== Compiling Pacman Phase 1 ==="
echo

# Create bin directory
mkdir -p bin

# Compile all Java files
javac -cp "lib/*:src" -d bin \
    src/models/*.java \
    src/graph/*.java \
    src/graph/algorithms/*.java \
    src/managers/*.java \
    src/entities/*.java \
    src/*.java

if [ $? -eq 0 ]; then
    echo "✅ Compilation successful!"
    echo
    echo "To run the game:"
    echo "  ./run.sh"
    echo
    echo "Or manually:"
    echo "  java -cp \"lib/*:bin\" App"
else
    echo "❌ Compilation failed!"
    exit 1
fi
