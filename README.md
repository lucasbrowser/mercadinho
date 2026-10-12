# Sistema de Mercadinho — estrutura inicial

Base para um mercadinho com operação local e gestão remota opcional.

## Módulos
- `servidor-local`: API local e banco operacional. Deve funcionar sem internet.
- `caixa-desktop`: interface JavaFX inicial do PDV.
- `api-nuvem`: esqueleto para gestão remota e recepção de eventos de sincronização.

## Pré-requisitos
- JDK 21 configurado em `JAVA_HOME`
- Maven 3.9 ou superior
- Docker Desktop com WSL 2 no Windows

## Iniciar o banco local
```powershell
docker compose up -d banco-local
```

Banco adicional para desenvolvimento da API remota:
```powershell
docker compose --profile nuvem-dev up -d banco_nuvem_desenvolvimento
```

## Compilar todos os módulos
```powershell
mvn clean verify
```

## Executar o servidor local
```powershell
mvn -pl servidor-local spring-boot:run
```
Verificação: `http://localhost:8080/api/health`

## Executar o aplicativo de caixa
```powershell
mvn -pl caixa-desktop javafx:run
```

## Executar API da nuvem em desenvolvimento
Configure `URL_BANCO_NUVEM`, `USUARIO_BANCO_NUVEM` e `SENHA_BANCO_NUVEM` conforme o `compose.yaml` e execute:
```powershell
mvn -pl api-nuvem spring-boot:run
```
Verificação: `http://localhost:8081/api/saude`

## Importante
Esta é uma estrutura inicial, ainda não pronta para uso comercial. Antes da operação real, implementar:
1. autenticação real, perfis e troca das credenciais de desenvolvimento;
2. cadastro e manutenção de produtos e usuários;
3. vendas transacionais, pagamentos e movimentação de estoque;
4. fila de sincronização local e processamento idempotente na nuvem;
5. emissão fiscal adequada ao estado e regime tributário;
6. integração real com a solução de pagamento e modelo de impressora;
7. cópias de segurança automáticas e testes de restauração;
8. HTTPS e autenticação forte para acesso remoto.

Nunca exponha a API local diretamente à internet. Para gestão remota, utilize HTTPS e autenticação forte, preferencialmente por VPN ou serviço de acesso remoto seguro. O PDV deve operar localmente quando a internet estiver indisponível.
