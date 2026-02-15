# Instruções do Projeto Sisgetrim

## 1. Contexto do Projeto

**Nome:** Sisgetrim

**Stack Tecnológica:**
- Java 21
- Spring Boot 3.x
- Maven
- PostgreSQL

**Ambiente de Desenvolvimento:**
- **Maven:** Instalado em `C:\apache-maven-3.9.10`
- **GitHub Repository:** `https://github.com/aameurer/Sisgetrim.git`
- **Site de Referência:** `https://sisgetrim.com.br/login`

**Comandos para salvar no GitHub:**
1. `git add .`
2. `git commit -m "Instruções e configuração inicial"`
3. `git push origin main`

**Frontend:**
- Thymeleaf
- Tailwind CSS
- JavaScript

**Banco de Dados:** `db_sisgetrim` (PostgreSQL)

**Segurança:** Spring Security com criptografia BCrypt

---

## 2. Requisitos de Autenticação e Usuário

### Login
- Autenticação via **CPF** e **Senha**

### Cadastro
Deve conter os seguintes campos:
- Usuário
- E-mail
- Tipo de Documento (CPF/CNPJ)
- Número do Documento
- Senha
- Confirmação de Senha

### Segurança
- As senhas devem ser **obrigatoriamente criptografadas** no banco de dados usando BCrypt

### Sessão
- Implementar funcionalidade "Lembrar-me"
- Proteção de rotas: apenas usuários logados podem acessar o Dashboard

---

## 3. Arquitetura de Frontend (Fragmentos)

### Estrutura de Layout
- Utilizar um arquivo `layout.html` como base
- O corpo das páginas deve ser inserido via `th:replace` ou `layout:fragment`

### Menu de Perfil
O menu de perfil deve ser um fragmento contendo:
- **Nome do Usuário** (Ex: "Teste Fiscal")
- **Botão de alternância para Modo Escuro**
- **Link para Configurações**
- **Link para Sair (Logout)**

---

## 4. Instruções para Implementação

### Prompt para IA/Desenvolvimento

> "Aja como um desenvolvedor Full Stack Senior. Configure o projeto Spring Boot com as dependências: Spring Web, Spring Data JPA, Spring Security, Validation, Thymeleaf e PostgreSQL Driver."

### Passo 1: Configuração do Banco de Dados
Configure o `application.properties` para o banco `db_sisgetrim`:
- **Host:** localhost:5432
- **Usuário:** postgres
- **Senha:** admin

### Passo 2: Entidade Usuario
Crie a entidade `Usuario` com os seguintes campos:
- `id`
- `nome`
- `email`
- `documento` (CPF/CNPJ)
- `senha` (usar BCrypt)
- `role`

### Passo 3: UserDetailsService
Implemente o `UserDetailsService` para autenticar usando o campo `documento` (CPF)

### Passo 4: Telas de Login e Cadastro
Desenvolva as telas baseadas nas imagens fornecidas:
- Use **Tailwind CSS** para design limpo e centralizado
- **No Cadastro:** incluir checkbox de concordância com Termos de Serviço
- **No Login:** 
  - Incluir opção "Esqueceu sua senha?"
  - Incluir ícone de visualizar senha no input

### Passo 5: Dashboard Principal
Implemente o Dashboard com:
- Estrutura de fragmentos
- Cabeçalho reutilizável
- Menu de perfil reutilizável (Modo Escuro/Sair)

---

## Observações Importantes

- Todas as senhas devem ser criptografadas antes de serem armazenadas
- A autenticação deve usar o campo `documento` (CPF) como username
- O sistema deve ter proteção de rotas implementada
- O design deve seguir padrões modernos com Tailwind CSS
- A arquitetura deve usar fragmentos Thymeleaf para reutilização de componentes
- **Importante:** O sistema remove automaticamente pontos e traços de CPF/CNPJ antes de salvar e durante o login para garantir compatibilidade.

---

## 5. Escalabilidade e Alta Disponibilidade (Alto Volume)

Para suportar um alto volume de acessos, as seguintes diretrizes devem ser seguidas:

### Configurações de JVM
- **Memória:** Definir `-Xms2g -Xmx4g` (mínimo/máximo) em ambiente de produção para evitar pausas excessivas de Garbage Collection.
- **GC:** Utilizar G1GC: `-XX:+UseG1GC`.

### Banco de Dados (PostgreSQL)
- **Índices:** Garantir índices nos campos utilizados em filtros frequentes e na coluna `documento`.
- **Pool de Conexões:** O pool Hikari está configurado para 30 conexões simultâneas no `application.properties`.

### Frontend e Estáticos
- **Caching:** Recursos estáticos (CSS/JS) possuem cache de 30 dias habilitado.
- **Compressão:** O servidor utiliza Gzip para todas as respostas de texto (>1KB).

### Monitoramento
- **Actuator:** Acompanhar métricas via `/actuator/metrics`.
- **Logs:** Manter `spring.jpa.show-sql=false` em produção para evitar overhead de I/O.

---

## 6. Como Executar o Sistema

Para iniciar a aplicação Sisgetrim, utilize o **Maven Wrapper** (preferencial) no terminal dentro da pasta do projeto:

```powershell
.\mvnw.cmd spring-boot:run
```

Ou, caso tenha o Maven instalado no sistema:
```powershell
mvn spring-boot:run
```

A aplicação estará disponível em: [http://localhost:8080/login](http://localhost:8080/login)
