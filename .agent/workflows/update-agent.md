---
description: Como atualizar a pasta .agent a partir do repositório oficial preservando customizações.
---

# Workflow: Atualização do toolkit .agent

Sempre que houver alterações no repositório `https://github.com/vudovn/antigravity-kit.git`, siga este procedimento para atualizar o projeto local sem perder regras e skills customizadas.

## Passo a Passo

### 1. Backup de Customizações
Antes de remover a pasta antiga, salve os arquivos que você criou:
- **Regras:** `xcopy ".agent\rules\AAM instrucao do projeto.md" c:\temp_backup_agent\rules\ /Y`
- **Skills:** `xcopy .agent\skills\skills\aam-performance-pro c:\temp_backup_agent\skills\aam-performance-pro\ /E /I /Y`

### 2. Download do Toolkit Atualizado
Clone o repositório em uma pasta temporária:
```powershell
mkdir c:\antigravity-kit-tmp; git clone https://github.com/vudovn/antigravity-kit.git c:\antigravity-kit-tmp
```

### 3. Substituição da Pasta .agent
Mova a pasta atual para um backup e aplique a nova:
```powershell
move .agent .agent_old; xcopy c:\antigravity-kit-tmp\ .agent\ /E /I /Y
```

### 4. Restauração das Customizações
Recupere os arquivos salvos no Passo 1:
- `xcopy "c:\temp_backup_agent\rules\AAM instrucao do projeto.md" .agent\rules\ /Y`
- `xcopy c:\temp_backup_agent\skills\aam-performance-pro .agent\skills\skills\aam-performance-pro\ /E /I /Y`

### 5. Limpeza Final
Remova as pastas temporárias:
```powershell
Remove-Item c:\antigravity-kit-tmp -Recurse -Force; Remove-Item c:\temp_backup_agent -Recurse -Force
```

> [!IMPORTANT]
> Sempre verifique se o caminho da skill ou regra customizada está correto antes de iniciar o backup.
