# 🕹️ Pacman

Este repositório apresenta o desenvolvimento de um jogo no estilo clássico Pac-Man, utilizando a linguagem Java e recursos gráficos para construção da interface e movimentação dos personagens.

O projeto foi desenvolvido como trabalho final da disciplina Técnicas de Programação, do curso de Bacharelado em Sistemas de Informação da UNESP – Universidade Estadual Paulista “Júlio de Mesquita Filho”. O objetivo principal é aplicar conceitos de programação orientada a objetos, manipulação gráfica e lógica de jogos, proporcionando uma experiência prática e divertida no desenvolvimento de software interativo.


---

## 🧠 Conceitos Aplicados

- Programação Orientada a Objetos (POO)  
- Modularização do projeto  
- Manipulação de sprites e recursos gráficos  
- Lógica de movimentação e colisão  
- Organização de arquivos e boas práticas em Java  

## 📁 Estrutura do Projeto

```bash
pacman/
├── lib/
│   └── gson-2.10.1.jar          # Biblioteca externa para manipulação JSON
├── src/
│   ├── entities/                # Classes dos personagens e lógica do jogo
│   │   ├── Pacman.java
│   │   ├── RankingManager.java
│   │   ├── ScoreEntry.java
│   │   └── (outros .java e .class relacionados)
│   ├── images/                  # Imagens e sprites usados no jogo
│   ├── App.java                 # Classe principal para iniciar o jogo direto (sem menu)
│   ├── MainMenu.java            # Tela inicial com menu para jogar, ranking, sair
├── ranking.json                # Armazena as pontuações dos jogadores
├── README.md                   # Documentação (este arquivo)


```

---

## 🚀 Como Compilar e Rodar

Abra o terminal (cmd) na pasta src do projeto e rode:

```bash
javac App.java
```

Para executar o jogo:

```bash
javac App.java
```

---

---

## ⚙️ Observações

- A biblioteca `gson-2.10.1.jar` é usada para ler e escrever o arquivo `ranking.json`.  
- A pasta `images` deve conter os sprites usados pelo jogo para exibir personagens e cenários.
- O arquivo `ranking.json` guarda as pontuações dos jogadores e é atualizado durante o jogo.

---