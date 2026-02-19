# Instruções do Projeto Sisgetrim

## 1. Contexto do Projeto

**Nome:** Sisgetrim

**Stack Tecnológica:**
- Java jdk 21
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
- **AlpineJS** (para micro-interações e estados leves)

**Banco de Dados:** `db_sisgetrim` (PostgreSQL)

**Segurança:** Spring Security com criptografia BCrypt.
- **Roles:** `ROLE_USER`, `ROLE_ADMIN`, `ROLE_MASTER`.
- **Status de Usuário:** `PENDENTE`, `VERIFICADO`.

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

### Alertas e Feedback
- Alertas de sucesso/erro devem possuir auto-dismiss de **10 segundos** utilizando AlpineJS:
  ```html
  <div x-data="{ show: true }" x-init="setTimeout(() => show = false, 10000)" x-show="show">...</div>
  ```

---

## 4. Regras de Negócio e Lógica de Sessão

### Seleção de Entidade Ativa
- O sistema suporta múltiplas entidades por usuário.
- A entidade ativa é mantida na sessão (`entidadeSelecionadaId`).
- Lógica de recuperação centralizada em `UsuarioService.getEntidadeSelecionada`:
  1. Verifica se há um ID na sessão.
  2. Se houver, valida se o usuário tem acesso.
  3. Se não houver ou for inválido, seleciona a primeira entidade vinculada e salva na sessão.

### Exclusão de Entidades
- É proibido excluir entidades que possuam vínculos com:
  - Usuários
  - Cartórios
  - Importações DOI
  - Importações de Arrecadação (ITBI)
- A verificação de integridade deve ser feita no `EntidadeService` antes da deleção física.

### Manuais e Documentação
- O sistema disponibiliza manuais técnicos via endpoints:
  - **DOI:** `/manual/manual-doi.pdf`
  - **Arrecadação:** `/malha/manual/importacao-arrecadacao` (Lê de `C:\sisgetrim\Manual Imp Fiscal.pdf`)

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

---

## 7. Estrutura do Banco de Dados

### Tabelas Core

#### 1. Usuários (`usuarios`)
| Coluna | Detalhes |
| :--- | :--- |
| documento | CPF/CNPJ (Unique, PK lógica) |
| email | Unique |
| role | ROLE_USER, ROLE_ADMIN, ROLE_MASTER |
| status | PENDENTE, VERIFICADO |
| foto_url | Caminho da imagem de perfil |

#### 2. Entidades (`entidades`)
| Coluna | Detalhes |
| :--- | :--- |
| cnpj | Unique, PK lógica |
| nome_empresarial | Razão Social |
| ativo | Boolean |

#### 3. Cartórios (`cartorios`)
| Coluna | Detalhes |
| :--- | :--- |
| codigo_cns | Unique |
| entidade_id | FK (entidades) |

#### 4. Relacionamentos (Many-to-Many)
- `usuario_entidades`: Vincula usuários às prefeituras/entidades.
- `usuario_cartorios`: Vincula usuários aos cartórios.

---

## 8. Estrutura Oficial DOI (Tabelas de Registro)

#### 1. Declarações (`doi_declaracoes`)
| Coluna | Tipo | Detalhes |
| :--- | :--- | :--- |
| id | BIGINT | PK (Auto Increment) |
| entidade_id | BIGINT | FK (Entidade) |
| importacao_id | BIGINT | FK (DoiImportacao) |
| tipo_declaracao | VARCHAR(20) | NOT NULL |
| tipo_servico | VARCHAR(50) | NOT NULL |
| data_lavratura | DATE | NOT NULL |
| tipo_ato | VARCHAR(100) | NOT NULL |
| tipo_livro | VARCHAR(50) | |
| numero_livro | VARCHAR(7) | |
| folha | VARCHAR(7) | NOT NULL |
| mne_eletronica | VARCHAR(24) | |
| matricula | VARCHAR(15) | UNIQUE |
| transcricao | INT | |
| cnm_codigo_nacional | VARCHAR(20) | |
| num_registro_averbacao | VARCHAR(7) | |
| natureza_titulo | VARCHAR(100) | |
| numero_registro_td | VARCHAR(30) | |
| existe_doi_anterior | BOOLEAN | DEFAULT FALSE |
| data_cadastro | TIMESTAMP | |

#### 2. Operações Imobiliárias (`doi_operacao_imobiliaria`)
| Coluna | Tipo | Detalhes |
| :--- | :--- | :--- |
| id | BIGINT | PK |
| doi_declaracao_id | BIGINT | FK (doi_declaracoes) |
| data_negocio_juridico | DATE | NOT NULL |
| tipo_operacao_imobiliaria | VARCHAR(100) | NOT NULL |
| descricao_outras_operacoes | VARCHAR(30) | |
| valor_operacao_imobiliaria | NUMERIC(20,2) | |
| indicador_nao_consta_valor_operacao | BOOLEAN | |
| valor_base_calculo_itbi_itcmd | NUMERIC(20,2) | |
| indicador_nao_consta_base_calculo | BOOLEAN | |
| forma_pagamento | VARCHAR(50) | NOT NULL |
| indicador_alienacao_fiduciaria | BOOLEAN | |
| mes_ano_ultima_parcela | DATE | |
| valor_pago_ate_data_ato | NUMERIC(20,2) | |
| indicador_permuta_bens | BOOLEAN | NOT NULL |
| indicador_pagamento_dinheiro | BOOLEAN | NOT NULL |
| valor_pago_moeda_corrente_data_ato | NUMERIC(20,2) | |
| tipo_parte_transacionada | VARCHAR(50) | NOT NULL |
| valor_parte_transacionada | NUMERIC(20,2) | NOT NULL |

#### 3. Imóveis (`doi_imoveis`)
| Coluna | Tipo | Detalhes |
| :--- | :--- | :--- |
| id | BIGINT | PK |
| declaracao_id | BIGINT | FK (doi_declaracoes) |
| cib | VARCHAR(8) | Indexado |
| destinacao | VARCHAR(20) | NOT NULL |
| indicador_imovel_publico_uniao | BOOLEAN | NOT NULL |
| registro_imobiliario_patrimonial_rip | VARCHAR(13) | |
| certidao_autorizacao_transferencia_cat | VARCHAR(11) | |
| matricula | VARCHAR(15) | |
| transcricao | INTEGER | |
| inscricao_municipal | VARCHAR(45) | |
| codigo_ibge_municipio | VARCHAR(7) | NOT NULL |
| area_imovel | NUMERIC(15, 4) | NOT NULL |
| indicador_area_lote_nao_consta | BOOLEAN | NOT NULL |
| area_construida | NUMERIC(16, 4) | |
| indicador_area_construida_nao_consta | BOOLEAN | |
| tipo_imovel | VARCHAR(50) | NOT NULL |
| tipo_logradouro | VARCHAR(30) | NOT NULL |
| nome_logradouro | VARCHAR(255) | NOT NULL |
| numero_imovel | VARCHAR(10) | NOT NULL |
| complemento_numero | VARCHAR(10) | |
| complemento_endereco | VARCHAR(100) | |
| bairro | VARCHAR(150) | NOT NULL |
| cep | VARCHAR(8) | NOT NULL |
| codigo_incra | VARCHAR(13) | Indexado |
| denominacao_rural | VARCHAR(200) | |
| localizacao_detalhada | VARCHAR(200) | |
| municipios_uf_lista | TEXT | |
| data_criacao | TIMESTAMP | |

#### 4. Alienantes (`doi_alienantes`)
| Coluna | Tipo | Detalhes |
| :--- | :--- | :--- |
| id | BIGINT | PK |
| doi_declaracao_id | BIGINT | FK (doi_declaracoes) |
| entidade_id | BIGINT | FK (entidades) |
| indicador_ni_identificado | BOOLEAN | NOT NULL |
| motivo_nao_identificacao_ni | VARCHAR(2) | |
| ni | VARCHAR(14) | CPF ou CNPJ |
| participacao | NUMERIC(7,4) | |
| indicador_nao_consta_participacao | BOOLEAN | DEFAULT FALSE |
| indicador_estrangeiro | BOOLEAN | DEFAULT FALSE |
| indicador_espolio | BOOLEAN | DEFAULT FALSE |
| cpf_inventariante | VARCHAR(11) | |
| indicador_conjuge | BOOLEAN | DEFAULT FALSE |
| indicador_conjuge_participa | BOOLEAN | |
| regime_bens | VARCHAR(50) | |
| indicador_cpf_conjuge_identificado | BOOLEAN | |
| cpf_conjuge | VARCHAR(11) | |
| indicador_representante | BOOLEAN | DEFAULT FALSE |
| representantes | JSONB | Lista de CPFs/CNPJs |
| data_criacao | TIMESTAMP | |

#### 5. Adquirentes (`doi_adquirentes`)
| Coluna | Tipo | Detalhes |
| :--- | :--- | :--- |
| id | BIGSERIAL | PK |
| doi_declaracao_id | BIGINT | FK (doi_declaracoes) |
| entidade_id | BIGINT | FK (entidades) |
| indicador_ni_identificado | BOOLEAN | NOT NULL |
| motivo_nao_identificacao_ni | VARCHAR(2) | |
| ni | VARCHAR(14) | CPF ou CNPJ |
| participacao | NUMERIC(7,4) | |
| indicador_nao_consta_participacao | BOOLEAN | DEFAULT FALSE |
| indicador_estrangeiro | BOOLEAN | DEFAULT FALSE |
| indicador_espolio | BOOLEAN | DEFAULT FALSE |
| cpf_inventariante | VARCHAR(11) | |
| indicador_conjuge | BOOLEAN | DEFAULT FALSE |
| indicador_conjuge_participa | BOOLEAN | |
| regime_bens | VARCHAR(50) | |
| indicador_cpf_conjuge_identificado | BOOLEAN | |
| cpf_conjuge | VARCHAR(11) | |
| indicador_representante | BOOLEAN | DEFAULT FALSE |
| representantes | JSONB | Lista de Objetos |
| data_criacao | TIMESTAMP | |

#### 6. Importações (`doi_importacoes`)
| Coluna | Tipo | Detalhes |
| :--- | :--- | :--- |
| id | BIGINT | PK |
| entidade_id | BIGINT | FK (entidades) |
| usuario_id | BIGINT | FK (usuarios) |
| nome_arquivo | VARCHAR(255) | |
| data_importacao | TIMESTAMP | |
| status | VARCHAR(20) | PROCESSANDO, CONCLUIDO, ERRO |
| total_registros | INTEGER | |
| created_at | TIMESTAMP | |
| updated_at | TIMESTAMP | |

#### 7. Erros de Importação (`doi_importacao_erros`)
| Coluna | Tipo | Detalhes |
| :--- | :--- | :--- |
| id | BIGINT | PK |
| importacao_id | BIGINT | FK (doi_importacoes) |
| linha_json | INTEGER | |
| campo_chave | VARCHAR(100) | |
| mensagem_erro | TEXT | |
| created_at | TIMESTAMP | |

#### 8. Tabelas de Domínio (Lookup)
Todas as tabelas abaixo possuem a estrutura: `codigo (INT, PK)` e `descricao (VARCHAR)`.

1.  `dom_tipo_declaracao`
2.  `dom_tipo_servico`
3.  `dom_tipo_ato`
4.  `dom_tipo_livro`
5.  `dom_natureza_titulo`
6.  `dom_tipo_operacao`
7.  `dom_forma_pagamento`
8.  `dom_medida_parte`
9.  `dom_destinacao`
10. `dom_motivo_nao_ni`
11. `dom_regime_bens`
12. `dom_tipo_imovel`
