# Gramari Gerenciador de Pedidos
#Projeto Fork do projeto legado #GerenciadorDePedidos

Projeto de Gerenciador de Pedidos para Varejo

![1](https://user-images.githubusercontent.com/42498776/144947054-c786ab86-d47c-47da-8052-eae883cdcef1.PNG)

Modelado para solucionar processos de relatorios e controle de pedidos, envio e origens de pedidos
Contando com Banco de Dados proprio e trabalhando diretamente no mesmo

![Main Screen](https://user-images.githubusercontent.com/42498776/144947120-387ed769-7bff-4a36-b302-ae7051c554d3.PNG)
![2](https://user-images.githubusercontent.com/42498776/144947333-120583b3-609c-45d4-9868-26f04b511991.jpg)
![3](https://user-images.githubusercontent.com/42498776/144947306-334bc161-0763-46fe-a220-6086d5e49c78.jpg)


Geradores de Planilhas e Relatorios de:
(Cadastros)
- Usuarios
- Produtos
- ![4](https://user-images.githubusercontent.com/42498776/144947220-1583761e-fb74-4d26-8d48-b517827d91a9.jpg)
- Clientes
- ![6](https://user-images.githubusercontent.com/42498776/144947246-fef8ea75-4424-4857-afcc-70fa13e8e2a6.jpg)
- Pedidos
- ![5](https://user-images.githubusercontent.com/42498776/144947262-68f15af8-99ca-4469-87cc-8a8ebed6d41f.jpg)
- Lista Ruptura
- ![7](https://user-images.githubusercontent.com/42498776/144947279-9b7e2ad8-a1c7-4846-9394-39b8844215ef.jpg)
 
Geracao de impressao para impressoras termicas do pedido (Padrao de Cupom nao fiscal) 
DashBoard com graficos de estatisticas

Esse e um fork do projeto antigo em que tinha tudo em uma aplicacao unica, este por sua vez
trabalha mutuamente junto com uma aplicacao Rest Spring Boot, realizando consultas com maior velocidade,
requisitando de hardware menor para o client, e o tornando mais seguro atraves do Spring Security.

Foram Refatoradas todas as classes de comunicacao, melhoria do padrao de arquitetura de classes, e componentizacao.

