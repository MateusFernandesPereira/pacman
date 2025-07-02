# 🕹️ Pacman

Este repositório apresenta o desenvolvimento de um jogo no estilo clássico Pac-Man, utilizando a linguagem Java e recursos gráficos para construção da interface e movimentação dos personagens.

O projeto foi desenvolvido como trabalho final da disciplina Técnicas de Programação, do curso de Bacharelado em Sistemas de Informação da UNESP – Universidade Estadual Paulista “Júlio de Mesquita Filho”. O objetivo principal é aplicar conceitos de programação orientada a objetos, manipulação gráfica e lógica de jogos, proporcionando uma experiência prática e divertida no desenvolvimento de software interativo.


---

## 🧠 Conceitos Aplicados

- Programação Orientada a Objetos (POO)
- Estruturação modular do projeto
- Manipulação de sprites e recursos gráficos
- Lógica de colisão e movimentação
- Organização de arquivos e boas práticas com Java

## 📁 Estrutura do Projeto

```bash
pacman/
├── lib/                         # Bibliotecas externas
├── src/
│   ├── entities/                # Personagens e objetos do jogo (Pac-Man, Fantasmas, etc.)
│   │   ├── PacMan.java          # Lógica principal do jogo e renderização gráfica
│   │   ├── ScoreEntry.java      # Modelo de entrada do ranking (nome + score)
│   │   ├── RankingManager.java  # Gerenciamento de leitura/escrita do ranking em JSON
│   ├── images/                  # Recursos visuais (sprites do jogo)
│   ├── App.java                 # Inicialização do jogo diretamente (sem menu)
│   ├── MainMenu.java           # Tela inicial com opções: jogar, ver ranking, sair
├── ranking.json                # Arquivo de armazenamento das pontuações
├── README.md                   # Documentação do projeto


```

---
