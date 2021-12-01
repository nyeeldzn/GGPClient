# Gramari Gerenciador de Pedidos
#Projeto Fork do projeto legado #GerenciadorDePedidos

Projeto de Gerenciador de Pedidos para Varejo
Modelado para solucionar processos de relatorios e controle de pedidos, envio e origens de pedidos
Contando com Banco de Dados proprio e trabalhando diretamente no mesmo
Geradores de Planilhas e Relatorios de:
(Cadastros)
- Usuarios
- Produtos
- Clientes
- Pedidos
 
Geracao de impressao para impressoras termicas do pedido (Padrao de Cupom nao fiscal) 
DashBoard com graficos de estatisticas

Esse e um fork do projeto antigo em que tinha tudo em uma aplicacao unica, este por sua vez
trabalha mutuamente junto com uma aplicacao Rest Spring Boot, realizando consultas com maior velocidade,
requisitando de hardware menor para o client, e o tornando mais seguro atraves do Spring Security.

Foram Refatoradas todas as classes de comunicacao, melhoria do padrao de arquitetura de classes, e componentizacao.

