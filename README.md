Sistema de Hotelaria Distribuído – API RESTful

🚀 Visão Geral do Projeto

Este projeto implementa um sistema de gerenciamento de hotelaria com foco na comunicação distribuída. Originalmente concebido com Java RMI (Remote Method Invocation), o sistema foi refatorado para operar como uma API RESTful utilizando o framework Spring Boot. Esta transição visa aprimorar a interoperabilidade, permitindo que clientes desenvolvidos em qualquer linguagem (como o cliente JavaScript mencionado) possam se comunicar com o servidor de forma padronizada via HTTP e JSON.

O sistema permite gerenciar quartos e reservas, oferecendo um conjunto de operações essenciais para a administração hoteleira.

✨ Funcionalidades Principais

A API oferece os seguintes serviços:

Gestão de Quartos

    Listar quartos disponíveis.
    Buscar um quarto específico por número.
    Adicionar novos quartos.
    Remover quartos existentes.
    Atualizar o status de disponibilidade de um quarto.

Gestão de Reservas

    Realizar novas reservas, verificando a disponibilidade dos quartos.
    Cancelar reservas existentes.
    Buscar uma reserva específica por ID.
    Listar reservas por hóspede (CPF).
    Listar todas as reservas.

🛠️ Tecnologias Utilizadas

    Linguagem: Java 
    Framework Web: Spring Boot (para o servidor API)
    Comunicação: HTTP/HTTPS
    Formato de Dados: JSON (com a biblioteca Gson)
    Gerenciador de Dependências: Maven (ou Gradle, dependendo da configuração inicial)
    Cliente: Aplicação Java Console (para demonstração) e Cliente JavaScript (para demonstração de interoperabilidade).

## LINK DA APRESENTAÇÃO 
  https://drive.google.com/file/d/10v8BJ0bAn-uO3PShUCHrhwjUzBWLT7qf/view?usp=drives
  
## LINK DO RELATÓRIO
 https://docs.google.com/document/d/1fYZHsAK4qUB5ADnnsx63zBhKw5BUSdVnJV0VKSYcPRQ/edit?usp=sharing

