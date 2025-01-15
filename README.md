bankTransfers é um projeto que permite incluir, alterar, deletar e consultar agendamento de transações bancárias e de Contas. Para criar uma transferência é obrigatorio que as contas já existam.

----------
URL para clonar o projeto

git clone https://github.com/SilvaPereira/bankTransfers.git

Depois compilar o projeto usando Maven

mvn clean install

Depois iniciar o projeto 

mvn spring-boot:run

----------
A aplicação usa um Banco de Dados H2 que é inicializado com alguns dados padrões para facilitar os testes ( arquivo data.sql no diretorio src/main/resources)

Para acessar o H2 Console e ver os dados:  
	Url: http://localhost:8080/h2  
	Username: sa  
	Password: sa  
 
----------
Endpoints criados

Account

GET /account/{id} - Retorna uma conta  
GET /account - Retorna todas as contas  
POST /account - Cria uma nova conta  
PUT /account/{id} - Editar uma conta já criada  
DELETE /account/{id} - Elimina uma conta  

Transfer

GET /transfers/{id} - Retorna uma transferência  
GET /transfers - Retorna todas as transferências  
POST /transfers - Cria uma nova transferência  
PUT /transfers/{id} - Editar uma transferência já criada  
DELETE /transfers/{id} - Elimina uma transferência  

----------	
Criei também uma Collection no Postman para testes das APIs ( Account e Transfer ) com o nome BankTransfers.postman_collection. Contém CREATE, UPDATE, GETbyId, GETall e DELETE para Transfers e Account.

----------
Finalmente para rodar a suite de testes basta usar o comando mvn test
