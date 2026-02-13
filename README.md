# Sistema de Gestão de Documentos

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0.2-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![H2 Database](https://img.shields.io/badge/Database-H2-blue?style=for-the-badge&logo=h2&logoColor=white)
![HTML5](https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white)
![CSS3](https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=css3&logoColor=white)
![JavaScript](https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black)

> **Esta aplicação é uma solução Full Stack desenvolvida para o gerenciamento centralizado de documentos digitais. O sistema permite que usuários realizem o **upload seguro**
> de arquivos (PDF, PNG, JPG), visualizem metadados, realizem o **download** dos itens armazenados e mantenham um **histórico de comentários** vinculado a cada documento.
> O projeto foca em alta coesão e baixo acoplamento, utilizando arquitetura em camadas e DTOs para proteção da camada de domínio.**

---

## Stack Tecnológica

### `Back-end (Core)`
* **Java 21**
* **Spring Boot**
* **Spring Data JPA**
* **PostgreSQL** (Banco de Dados Principal)
* **H2 Database** (Exclusivo para Testes Unitários e de Integração)
* **Java NIO (New I/O)**
* **Bean Validation**
* **Java Records**

### `Front-end`
* **HTML5**
* **CSS3**
* **JavaScript**

### `DevOps e Infraestrutura`
* **Maven**
* **Git**

---

## Engenharia e Documentação

* **Arquitetura REST:** Design de API utilizando os verbos HTTP corretos e endpoints semânticos.
* **Separação de Conceitos:** Isolamento claro entre a camada de apresentação (Controller), regras de negócio (Service) e acesso a dados (Repository).
* **Segurança de Dados:** Utilização de DTOs para prevenir a exposição direta de entidades do banco de dados.

---

## Funcionalidades Principais

* **Gestão de Arquivos (Upload/Download):**
    * Upload via `MultipartFile`.
    * Armazenamento físico na pasta local `/uploads` com renomeação via UUID para evitar conflitos.
    * Download de arquivos preservando o tipo de conteúdo (MIME Type).


* **Metadados e Organização:**
    * Persistência de título, descrição, nome original e data de upload.
    * Listagem otimizada de documentos cadastrados.


* **Histórico e Colaboração:**
    * **Feed de Comentários:** Cada documento possui seu próprio histórico de interações.
    * **Integridade:** Comentários estritamente vinculados ao documento pai.
    * **Timeline:** Registro automático de data e hora da criação.


* **Visualização Inteligente (Preview):**
  * Detecção automática do tipo de arquivo.
  * **Imagens:** Visualização direta na tela de detalhes.
  * **PDFs:** Leitura incorporada via iframe.
  * **Outros:** Fallback elegante com botão de download.

* **Colaboração:**
  * **Feed de Comentários:** Histórico de interações por documento.
  * **Interatividade:** Adição de comentários em tempo real via API.
---

## Arquitetura e Estrutura

O projeto segue uma estrutura baseada em camadas dentro do pacote `br.com.testrmh`, facilitando a navegação:

```text
teste-mrh/
├── backend/                          #Módulo Servidor (Spring Boot)
│   ├── src/main/java/br/com/testrmh
│   │   ├── controller/               #Endpoints REST
│   │   ├── service/                  #Regras de Negócio e I/O
│   │   ├── repository/               #Interfaces JPA
│   │   ├── model/                    #Entidades (Documento, Comentario)
│   │   └── dto/                      #Records (Transferência de dados)
│   └── src/main/resources/           #Configurações (application.properties)
│
├── frontend/                         #Módulo Cliente (Estático)
│   ├── css/                          #Estilos (styles.css, upload.css, etc.)
│   ├── scripts/                      #Lógica Unificada (main.js)
│   ├── index.html                    #Menu Principal
│   ├── upload.html                   #Tela de Envio
│   ├── documentos.html               #Listagem (Tabela)
│   └── detalhes.html                 #Visualização e Chat
│
├── uploads/                          #Armazenamento físico (Gerado automaticamente)
└── banco_teste.mv.db                 #Arquivo de Banco de Dados (H2)
```

## Instruções Claras para Execução Local

### **Pré-requisitos**
* Java JDK 21 instalado.
* Maven 3.8+ instalado.
* PostgreSQL instalado e com um banco de dados criado. (Caso queira utilizar já possui a Lib de conexão)
* Navegador Web

### **Passo 1: Clonar o Repositório**
````
git clone https://github.com/Guismx/teste-mrh.git
```` 

### **Passo 2: Configurar o Banco de Dados**
**application.properties**
```
spring.datasource.url=jdbc:postgresql://localhost:5432/nome_do_seu_banco
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update
server.port=9090
```

### **Passo 3: Executar o back-end**
Na raiz do projeto, execute:
````
1. Navegue até a pasta do back-end:
cd backend
````
````
Bash
2. Execute a aplicação:
mvn spring-boot:run
````
O servidor estará ativo em: http://localhost:080 (conferir a porta utilizada)


### **Passo 4: Executar o front-end**
Como o Frontend é construído com tecnologias nativas (HTML/JS), não é necessário build.
````
Navegue até a pasta frontend.

Abra o arquivo index.html diretamente no seu navegador (dois cliques) 
ou use uma extensão como "Live Server".
````

### **Observações Relevantes e Limitações Conhecidas**
*Armazenamento: Os arquivos físicos são salvos na pasta /uploads na raiz do projeto. Esta pasta é criada automaticamente no primeiro upload.*

*CORS: O Backend está configurado (@CrossOrigin("*"*)) para aceitar requisições do Frontend local.*