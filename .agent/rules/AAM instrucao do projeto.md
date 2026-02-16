---
trigger: always_on
---

# Padrões Globais de Desenvolvimento

## 🎨 UI (Frontend - Thymeleaf + Bootstrap 5 + Tailwind quando aplicável)
- Utilize preferencialmente Bootstrap 5 como base estrutural.
- Caso use Tailwind, mantenha escala neutra ('Zinc' ou 'Slate') para consistência visual.
- Padronize espaçamentos (mt-3, mb-3, gap-2).
- Todo elemento clicável deve conter feedback visual:
  - `transition-all`
  - `active:scale-95`
  - `cursor-pointer`
- Evite CSS inline.
- Componentes reutilizáveis devem ser extraídos para fragments (Thymeleaf).
- **Tabelas:** Devem seguir o padrão ultra-compacto (densidade máxima):
  - Células `<td>`: padding vertical `py-1` ou `py-1.5`.
  - Cabeçalho `<th>`: padding inferior `pb-2`.
  - Títulos de seção/card: margem inferior reduzida (`mb-4`).

---

## ⚙️ JavaScript
- Evite lógica inline no HTML.
- Utilize módulos JS organizados por responsabilidade.
- Prefira funções puras.
- Use nomes descritivos (ex: `calculateTotalAmount()` ao invés de `calc()`).
- Centralize chamadas AJAX.
- Sempre trate erros com `.catch()` ou try/catch.
- Não manipule DOM desnecessariamente — minimize re-renderizações.

---

## ☕ Spring Boot (Backend)
- Estruture por camadas:
  - controller
  - service
  - repository
  - dto
  - mapper
- Controllers NÃO devem conter regra de negócio.
- Services concentram regras.
- Utilize DTO para entrada/saída — nunca exponha entidade diretamente.
- Validações via:
  - `@Valid`
  - `@NotNull`, `@Size`, etc.
- Use ResponseEntity padronizado.
- Trate exceções com `@ControllerAdvice`.
- Log estruturado (SLF4J).

---

## 🗄 PostgreSQL
- Sempre utilize migrations (Flyway ou Liquibase).
- Nome padrão:
  - tabelas: snake_case
  - colunas: snake_case
- Use índices para:
  - foreign keys
  - colunas de busca frequente
- Nunca use `select *` em queries críticas.
- Prefira paginação (`Pageable`).
- Utilize constraints:
  - NOT NULL
  - UNIQUE
  - FK com ON DELETE adequado
- Padronize campos:
  - created_at
  - updated_at
  - ativo (boolean)

---

## 📦 Maven
- Dependências organizadas.
- Remover dependências não utilizadas.
- Separar profiles (dev, prod).
- Nunca subir credenciais no `application.properties`.

---

## 🔐 Segurança
- Nunca confiar em validação apenas do frontend.
- Use Spring Security quando houver autenticação.
- Senhas sempre criptografadas (BCrypt).
- Nunca retornar stacktrace em produção.
- Sanitizar entradas quando necessário.

---

## ♿ Acessibilidade
- Todos inputs devem ter `<label>`.
- Contraste adequado (WCAG).
- Botões com type explícito.
- Inputs obrigatórios devem indicar visualmente.

---

## 📄 Padrões de Código
- Métodos curtos (máx 20–30 linhas).
- Uma responsabilidade por método.
- Evite métodos gigantes.
- Nome de classes no padrão:
  - `UserService`
  - `UserController`
  - `UserRepository`
- Evite lógica complexa em Thymeleaf.

---

## 🔁 Integração com n8n / Agents
- Endpoints devem ser RESTful.
- Sempre retornar JSON estruturado:
  - status
  - message
  - data
- Logs claros para facilitar automações.
- Nunca retornar HTML para endpoints de integração.

---

## 📌 Performance
- Evitar N+1 queries.
- Use `@EntityGraph` quando necessário.
- Cache quando aplicável.
- Paginação obrigatória em listagens grandes.

---

## 🛠️ Manutenção do Toolkit (.agent)
O repositório de referência para o toolkit é `https://github.com/vudovn/antigravity-kit.git`.

### Procedimento de Atualização
Para atualizar a pasta `.agent` sem perder as customizações deste projeto (como este arquivo de regras e a skill de performance):
1. **Backup:** Salve as regras e skills customizadas em uma pasta temporária.
2. **Download:** Baixe a versão mais recente do repositório oficial.
3. **Substituição:** Substitua a pasta `.agent` local pela nova versão.
4. **Restauração:** Copie as customizações de volta para as pastas `rules/` e `skills/skills/`.
5. **Comando Rápido:** Utilize o workflow `/update-agent` para automatizar este processo (Passo a passo detalhado em `.agent/workflows/update-agent.md`).

