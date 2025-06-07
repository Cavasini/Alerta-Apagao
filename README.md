🚨 **Ordem de Inicialização Essencial:** 🚨

Para que o sistema funcione corretamente, os serviços devem ser iniciados na seguinte sequência:

1.  **Banco de Dados (Docker Compose)**
2.  **AlertService (Web Service)**
3.  **Outros Serviços Spring Boot (`AuthService`, `LocationService`, `MonitoringService`)**

---

🔗 **Frontend do Projeto:** [https://v0-user-login-and-registration-pi.vercel.app/](https://v0-user-login-and-registration-pi.vercel.app/) (Basta rodar os serviços que tudo funcionará corretamente) 🔗

---

# Apagão Alerta 🚨

## Visão Geral 🌍💡

Em um cenário global de instabilidade climática e crescente sobrecarga nas redes elétricas, quedas de energia prolongadas tornaram-se mais frequentes. Além do inconveniente, essas falhas representam 🔒 brechas significativas para ataques cibernéticos, comprometendo a segurança de hospitais, redes de comunicação e serviços essenciais. A segurança da informação é intrinsecamente ligada à continuidade dos sistemas, e é nesse contexto que o projeto **Apagão Alerta** se posiciona, buscando mitigar os impactos e riscos associados a essas interrupções.

O Apagão Alerta é um sistema distribuído projetado para monitorar condições climáticas ⛈️, prever riscos de apagão e alertar os usuários em tempo real sobre interrupções e restaurações de energia em suas regiões de interesse.

## Funcionalidades ✨

* **Monitoramento Climático Inteligente:** Verifica dados climáticos para identificar condições de risco de apagão. ☁️
* **Previsão e Alerta de Apagão:** Calcula o risco de apagão com base em dados climáticos e geográficos, enviando alertas preventivos. ⚡
* **Notificações em Tempo Real:** Envia SMS 📱 para usuários em regiões afetadas sobre riscos iminentes, início e fim de apagões.
* **Gestão de Localizações de Interesse:** Permite aos usuários salvar e monitorar locais específicos para receber alertas personalizados. 📍
* **Autenticação e Registro de Usuários:** Sistema seguro de login e gerenciamento de contas. 🔑
* **Simulação de Cenários de Apagão:** Ferramenta para simular condições climáticas extremas, apagões e normalização para testes e demonstrações. 🧪

## Arquitetura do Sistema 🏗️

O projeto Apagão Alerta é composto por um sistema distribuído modular, com quatro microsserviços interconectados, cada um com uma responsabilidade específica, além de um sistema de simulação e um banco de dados central PostgreSQL.

## Serviços 🧩

### 1. `AlertService` (Web Service) 📧

* **Função:** Serviço dedicado ao envio de mensagens SMS para os usuários. 💬
* **Integração:** Utiliza a API de terceiros `https://app.notificationapi.com/` para a entrega eficiente de SMS.
* **Consumo:** Principalmente consumido pelo `MonitoringService` para disparar notificações.

### 2. `AuthService` (Spring Boot) 🔐

* **Função:** Gerencia todo o ciclo de vida do usuário, incluindo registro de novas contas e autenticação (login), além de validação de tokens.
* **Credenciais:** Para o registro e login, são necessários: número de celular, um nome de usuário (username) e uma senha.
* **Porta:** Este serviço está configurado para rodar na porta `8082`.

#### Endpoints do `AuthService`

Base URL: `/auth`

* `POST /auth/login`
* `POST /auth/register`
* `POST /auth/validar`
* `GET /auth/users/{userId}`

### 3. `LocationService` (Spring Boot) 🗺️

* **Função:** Permite que os usuários salvem e gerenciem seus locais de interesse (por exemplo, residência, trabalho). Cada local possui uma `zone`, que é uma string composta por `estado+cidade+bairro`, permitindo um agrupamento geográfico. 🏠
* **Monitoramento:** A cada 1 hora, este serviço realiza uma verificação das condições climáticas para os locais cadastrados. ⏱️
* **Integração:** Utiliza a API `https://cep.awesomeapi.com.br/` para buscar dados detalhados de localização a partir de um CEP. 📮
* **Porta:** Este serviço está configurado para rodar na porta `8081`.

#### Endpoints do `LocationService`

Base URL: `/locations`

* `GET /locations/users/{userId}`
* `GET /locations/zones/{zone}`
* `GET /locations/cep/{cep}`
* `POST /locations`
* `DELETE /locations/{locationId}`
* `GET /locations/zones`

    * **Detalhe:** Este endpoint é utilizado para buscar *um* local por `zone` (estado+cidade+bairro), otimizando o processamento ao evitar verificações redundantes de clima para áreas já representadas.
* `PUT /locations/zones/status`
* `GET /locations/{locationId}`

### 4. `MonitoringService` (Spring Boot) - O Núcleo do Sistema ❤️‍🔥

* **Função:** É o serviço principal, responsável pela lógica de monitoramento climático, cálculo de risco e orquestração dos alertas. 🧠
* **Coleta de Dados:** Obtém dados de latitude e longitude de cada região de interesse dos usuários, provenientes do `LocationService`. 🌐
* **Verificação Climática:** Consulta dados climáticos em tempo real utilizando a API `https://api.weatherapi.com/`. ☀️
* **Cálculo de Risco de Apagão:** Com base nos dados climáticos coletados e algoritmos internos, calcula a probabilidade e o risco de um apagão. 📈
* **Disparo de Alertas:**

    * Se um risco de apagão for detectado, envia um alerta preventivo para todas as pessoas na região afetada. ⚠️
    * Quando um apagão efetivamente ocorre, envia um SMS informando os usuários. 🌑
    * Ao identificar a normalização da energia, envia um SMS notificando que a luz voltou. 💡
* **Interdependência:** Consome o `AlertService` para o envio das mensagens SMS.
* **Porta:** Este serviço está configurado para rodar na porta `8080` (porta padrão).

#### Endpoints do `MonitoringService`

Base URL: `/monitoring`

* `POST /monitoring`
* `POST /monitoring/{locationId}`
* `POST /monitoring/blackout/{zone}`

## Sistema de Simulação 🧪

Para fins de teste e demonstração, foi disponibilizado um sistema para simular diferentes cenários:

* **Simulação de Risco de Apagão:** Para testar os alertas preventivos. ⚠️
* **Simulação de Apagão:** Para verificar o envio de alertas de interrupção. 🚫
* **Simulação de Normalização:** Para testar o envio de alertas de retorno da energia. ✅

#### Endpoints do Sistema de Simulação

Base URL: `/simulation`

* `POST /simulation/climaExtremo`
* `POST /simulation/apagao`
* `POST /simulation/normalizacao`

## Banco de Dados 🗄️

* **Tecnologia:** PostgreSQL. 🐘
* **Configuração:** Uma imagem do banco de dados PostgreSQL é facilmente provisionada e gerenciada utilizando o arquivo `docker-compose.yml` localizado na raiz do projeto. Isso garante um ambiente de desenvolvimento e teste consistente. 🐳

## Como Executar o Projeto 🚀

### Pré-requisitos ✅

Certifique-se de ter as seguintes ferramentas instaladas em seu ambiente:

* **Docker:** Para gerenciar os contêineres do banco de dados e possivelmente os serviços. [Baixar Docker](https://www.docker.com/get-started) 🐳
* **Java Development Kit (JDK) 17 ou superior:** Para compilar e executar os serviços Spring Boot. [Baixar JDK](https://www.oracle.com/java/technologies/downloads/) ☕
* **Apache Maven:** Para gerenciar as dependências e o build dos projetos Spring Boot. [Baixar Maven](https://maven.apache.org/download.cgi) ⚙️

### 1. Configuração do Banco de Dados (PostgreSQL com Docker) 📀

1.  Navegue até o diretório raiz do projeto onde o `docker-compose.yml` está localizado.
2.  Execute o seguinte comando para iniciar o contêiner do PostgreSQL:

    ```bash
    docker-compose up -d
    ```

    Isso criará e iniciará o serviço do banco de dados, que estará disponível para os microsserviços.

### 2. Execução do `AlertService` (Web Service) 🌐

As instruções para o `AlertService` dependerão da sua tecnologia específica, já que é um "Web Service" e não um Spring Boot.

* **Se for um arquivo WAR:** Você pode precisar de um servidor de aplicação como Tomcat.

    ```bash
    # Exemplo: Copie o WAR para o diretório webapps do Tomcat
    cp AlertService.war /path/to/tomcat/webapps/
    # Inicie o Tomcat
    /path/to/tomcat/bin/startup.sh
    ```
* **Se for um JAR executável não Spring Boot:**

    ```bash
    java -jar AlertService.jar
    ```
* **Outro:** Siga as instruções específicas para o tipo de deployment do seu `AlertService`.

### 3. Build e Execução dos Outros Serviços Spring Boot 🛠️

Certifique-se de configurar as chaves de API necessárias para que os serviços possam se comunicar com as plataformas externas. Geralmente, isso é feito através de variáveis de ambiente ou arquivos de configuração (`application.properties`/`application.yml`) em cada serviço.

* **NotificationAPI:** Chave da API para o `AlertService` (`https://app.notificationapi.com/`).
* **WeatherAPI:** Chave da API para o `MonitoringService` (`https://api.weatherapi.com/`).
* **AwesomeAPI (CEP):** Para o `LocationService` (`https://cep.awesomeapi.com.br/`). Verifique a documentação da API, pois algumas APIs públicas não exigem chave.

Para cada um dos serviços Spring Boot (`AuthService`, `LocationService`, `MonitoringService`):

1.  Navegue até o diretório do serviço:

    ```bash
    cd <nome-do-servico> # Ex: cd AuthService
    ```
2.  Compile o projeto usando Maven:

    ```bash
    mvn clean install
    ```
3.  Após a compilação bem-sucedida, execute o serviço:

    ```bash
    java -jar target/<nome-do-servico-jar>.jar
    ```

    Substitua `<nome-do-servico-jar>` pelo nome real do arquivo `.jar` gerado (ex: `authservice-0.0.1-SNAPSHOT.jar`).

---

## Desenvolvedores 🧑‍💻

Este projeto foi desenvolvido por:

* **Lana Leite** - RM551143
* **Matheus Cavasini** - RM97722

---

Sinta-se à vontade para explorar e contribuir com o projeto Apagão Alerta! ⭐
