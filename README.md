# criando projeto
mvn archetype:generate -DgroupId=br.com.sqs_consomer -DartifactId=sqs_consomer -DarchetypeArtifactId=maven-archetype-quickstart -DarchetypeVersion=1.4 -DinteractiveMode=false

# doc mvn
https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html
### gerar com manifest
https://www.sohamkamani.com/java/cli-app-with-maven/

# exemplo code base
https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/home.html
https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2

### mvn options
- mvn validate: validate the project is correct and all necessary information is available
- mvn compile: compile the source code of the project
- mvn test: test the compiled source code using a suitable unit testing framework. These tests should not require the code be - packaged or deployed
- mvn package: take the compiled code and package it in its distributable format, such as a JAR.
- mvn integration-test: process and deploy the package if necessary into an environment where integration tests can be run
- mvn verify: run any checks to verify the package is valid and meets quality criteria
- mvn install: install the package into the local repository, for use as a dependency in other projects locally
- mvn deploy: done in an integration or release environment, copies the final package to the remote repository for sharing with other developers and projects.

# como rodar
- configurar o seu .bash_profile ou .bashrc
```shell
code ~/.bash_profile

export AWS_ACCESS_KEY="SUA_KEY"
export AWS_SECRET_KEY="SEU_SECRET"

source ~/.bash_profile
```

# rodar o comando
```shell
./buld.sh
./start.sh
```

# custo
https://aws.amazon.com/pt/sqs/pricing/

# rodando via aws cli
```shell
AWS configure
AWS sqs create-queue --queue-name="fila-danilo" --region=us-east-1
AWS sqs list-queues # retorna filas existentes
AWS sqs send-message --message-body="Teste" --queue-url=https://sqs.us-east-1.amazonaws.com/473247640396/fila-danilo 
aws sqs receive-message --queue-url=https://sqs.us-east-1.amazonaws.com/473247640396/fila-danilo 
aws sqs receive-message --queue-url=https://sqs.us-east-1.amazonaws.com/473247640396/fila-danilo --wait-time-seconds=20 # Long Polling economiza request
AWS sqs delete-message --queue-url=https://sqs.us-east-1.amazonaws.com/473247640396/fila-danilo  --receipt-handle=AQEBn65lYxXGVM5uopn7HIZ4fPpsWx9/NiFinPJK8etxuneMVBD+r3j7MegQ0LlRdgOWne281xrYx2rXoeRo0GDyOH7/W795n2rQjSifxPPaDJNw8rfeu1rS/GqGEjLfiLTfOeabEREWVV2L938VZc7Zpsitcu8GpO57mYEG2nKGXZwB0H43cqicFEqYZaRmZIbV+RWtFC7mnE3vZb3ollVGjRyVMlt6pPmEA9UAQrrFL2JrMEW/I8Apq3Ei6HQpapmK8BL5YmAcpulkiIjnyJAiztxhgz4TaDAgJEVp+8Ra3n1y5Q9tGWoplohK4Kw28tM/Ak1ySRPzqNZwmL6wPGVvNbLzVgWADGWAI8sA07gOHjFdY7uF666nQelohIVv6kEYtHBc+S0YTzxODbULex4hzQ==
AWS sqs delete-queue --queue-url=https://sqs.us-east-1.amazonaws.com/473247640396/fila-danilo 
AWS sqs create-queue --queue-name="fila-danilo-dlq" # Dead-letter queue - file para itens errados não processados/indesejados. configurar a fila fila-danilo vinculando com a DLQ fila-danilo-dlq
```

# FIFO Queue
- A file do tipo FIFO, garante a entrada e saida de mensagem ordenada
- Todos as mensagens precisam ser enviados com um grupo obrigatório, a ordem irá retornar categorizada por grupo
- a criação da fila precisa ter em seu nome o ".fifo"
- Ao criar a fila com o corpo "Content-based deduplication" não deixa duplicar mensagens enviadas em um periodo de 5 minnutos


# rodando via aws cli FIFO
```shell
AWS configure
AWS sqs create-queue --queue-name="fila-danilo.fifo" --attributes="{\"FifoQueue\":\"true\", \"ContentBasedDeduplication\":\"true\"}" --region=us-east-1
AWS sqs list-queues # retorna filas existentes
AWS sqs send-message --message-body="Teste2" --queue-url=https://sqs.us-east-1.amazonaws.com/473247640396/fila-danilo.fifo --message-group-id="grupo"
aws sqs receive-message --queue-url=https://sqs.us-east-1.amazonaws.com/473247640396/fila-danilo.fifo 
aws sqs receive-message --queue-url=https://sqs.us-east-1.amazonaws.com/473247640396/fila-danilo.fifo --wait-time-seconds=20 # Long Polling economiza request
AWS sqs delete-message --queue-url=https://sqs.us-east-1.amazonaws.com/473247640396/fila-danilo.fifo  --receipt-handle=AQEBirF79Oq5fKxhUR0XNAUlokwpdc/iXAYMW0BY+7ylafmPqAZWlOAeHZkPQ4R77P6I/OFsLZdJzQHUXQoDwxwdxAthhjrWASx6JYta9xr4s+3TJwbMWXeGbR4jFv3ubJFW+az4hr5GL7dTXPea0xzAwSeDtvtgjEPpiDZfbDN5WC0XyAkKZbvkf+h6dYvzqTAMn1HSuXgOJzW6Ay/ldkxbkJA+k++ulyhPHshrW0FzRYVugzUR8b2xYZ6umtzQjuSP6iN9POV49zSTa5ZhQfHj1A==
AWS sqs delete-queue --queue-url=https://sqs.us-east-1.amazonaws.com/473247640396/fila-danilo.fifo 
```