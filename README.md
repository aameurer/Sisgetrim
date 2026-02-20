# Sisgetrim - Sistema de Auditoria de ITBI

## 📋 Arquivos Criados

### ✅ Configuração de Segurança
- **`SecurityConfig.java`** - Configuração do Spring Security com autenticação via CPF/CNPJ

### ✅ Modelo de Dados
- **`Usuario.java`** - Entidade de usuário implementando UserDetails

### ✅ Camada de Dados
- **`UsuarioRepository.java`** - Repository com métodos de busca por documento e email

### ✅ Camada de Serviço
- **`UsuarioService.java`** - Serviço com UserDetailsService e cadastro de usuários

### ✅ Controladores
- **`AuthController.java`** - Controller para login, cadastro e dashboard

### ✅ Templates Thymeleaf
- **`layout.html`** - Layout base com Tailwind CSS e dark mode
- **`fragments/navbar.html`** - Menu de perfil com dropdown (nome, modo escuro, configurações, sair)
- **`login.html`** - Página de login com CPF/CNPJ e senha
- **`cadastro.html`** - Página de cadastro completa com validações
- **`dashboard.html`** - Dashboard com cards de estatísticas

### ✅ Assets Estáticos
- **`css/style.css`** - Estilos customizados com suporte a dark mode
- **`js/darkmode.js`** - Script para alternar modo escuro
- **`js/main.js`** - Script principal da aplicação

### ✅ Configurações
- **`application.properties`** - Configuração do banco PostgreSQL

---

## 🚀 Próximos Passos

1. **Criar o banco de dados:**
   ```sql
   CREATE DATABASE db_sisgetrim;
   ```

2. **Executar a aplicação:**
   ```bash
   mvn spring-boot:run
   ```

3. **Acessar o sistema:**
   - Login: http://localhost:8004/login
   - Cadastro: http://localhost:8004/cadastro
   - Dashboard: http://localhost:8004/dashboard (requer autenticação)

---

## 🔐 Funcionalidades Implementadas

✅ Autenticação via CPF/CNPJ  
✅ Cadastro de usuários com validação  
✅ Criptografia de senha com BCrypt  
✅ Proteção de rotas (Spring Security)  
✅ Modo Escuro  
✅ Menu de perfil com dropdown  
✅ Lembrar-me (Remember-me)  
✅ Logout  
✅ Dashboard responsivo  

---

## 📦 Dependências Utilizadas

- Spring Boot 4.0.2
- Spring Security
- Spring Data JPA
- Thymeleaf + Thymeleaf Security
- PostgreSQL Driver
- Lombok
- Validation
- Tailwind CSS (via CDN)
- Font Awesome (via CDN)
- Alpine.js (via CDN)
