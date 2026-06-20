# Checklist de Desenvolvimento por Feature

Guia curto para desenvolver cada feature do projeto `music-catalog-api` sem se perder na ordem das etapas.

## Onde colocar este arquivo

Coloque este arquivo em:

```txt
docs/CHECKLIST_FEATURE.md
```

---

## Ordem geral

```txt
develop -> branch -> planejar -> migration -> entity -> repository -> DTO/mapper -> service -> controller -> testes -> curl -> tutorial -> commit -> merge
```

---

## 1. Preparar a branch

```bash
git checkout develop
git pull origin develop
git status
git checkout -b feature/nome-da-feature
```

Exemplo:

```bash
git checkout -b feature/album
```

Checklist:

```txt
[ ] Estou na develop atualizada
[ ] Criei uma branch própria para a feature
[ ] O git status está limpo antes de começar
```

---

## 2. Planejar a feature

Antes de codar, responder mentalmente:

```txt
[ ] O que essa feature faz?
[ ] Qual entidade principal vou criar ou alterar?
[ ] Quais campos ela terá?
[ ] Existe relacionamento com outra entidade?
[ ] Quais endpoints provavelmente existirão?
```

Exemplo:

```txt
Album pertence a Artist.
Artist pode ter vários Albums.
```

---

## 3. Criar migration

Criar ou alterar tabela com Flyway.

Local:

```txt
src/main/resources/db/migration
```

Exemplo de nome:

```txt
V2__create_albums_table.sql
```

Depois rodar:

```bash
./mvnw clean test
```

O que estou testando aqui:

```txt
[ ] O projeto ainda compila
[ ] As migrations não quebraram nada antigo
[ ] Os testes existentes continuam passando
```

Importante:

```txt
Aqui ainda não estou testando a feature completa.
É só uma verificação geral.
```

---

## 4. Criar Entity

Criar a classe Java da entidade.

Checklist:

```txt
[ ] Criar a classe da entity
[ ] Mapear com @Entity
[ ] Mapear com @Table
[ ] Mapear campos
[ ] Mapear relacionamentos, se existirem
```

Depois rodar:

```bash
./mvnw clean test
```

O que estou testando aqui:

```txt
[ ] A entity bate com o banco
[ ] O Hibernate validate não reclama
[ ] O projeto continua compilando
```

---

## 5. Criar Repository

Checklist:

```txt
[ ] Criar interface Repository
[ ] Estender JpaRepository
[ ] Criar queries customizadas só se precisar
```

Criar teste de repository quando já houver algo para testar.

Tipo de teste:

```txt
RepositoryTest = teste de integração com banco/JPA
```

Depois rodar:

```bash
./mvnw clean test
```

O que estou testando aqui:

```txt
[ ] Salvamento no banco
[ ] Busca por ID
[ ] Queries customizadas
[ ] Relacionamentos
```

---

## 6. Criar DTOs e Mapper

Checklist:

```txt
[ ] Criar DTO de criação
[ ] Criar DTO de atualização, se precisar
[ ] Criar DTO de resposta
[ ] Criar mapper
[ ] Não devolver entity diretamente no controller
```

Depois rodar:

```bash
./mvnw clean test
```

O que estou testando aqui:

```txt
[ ] O projeto compila
[ ] Nada antigo quebrou
```

---

## 7. Criar Service

Checklist:

```txt
[ ] Criar interface do service
[ ] Criar implementação do service
[ ] Usar repositories
[ ] Buscar entidades relacionadas quando necessário
[ ] Aplicar regras de negócio
[ ] Usar @Transactional nos métodos que mexem com banco
[ ] Montar e devolver DTOs
```

Criar teste de service.

Tipo de teste:

```txt
ServiceTest = teste unitário com Mockito
```

Depois rodar:

```bash
./mvnw clean test
```

O que estou testando aqui:

```txt
[ ] Regras de negócio
[ ] Fluxos de sucesso
[ ] Fluxos de erro
[ ] Se o service chama o repository corretamente
```

---

## 8. Criar Controller

Checklist:

```txt
[ ] Criar controller
[ ] Criar endpoints necessários
[ ] Usar @Valid nos DTOs de entrada
[ ] Retornar status HTTP correto
[ ] Não colocar regra de negócio no controller
```

Criar teste de controller.

Tipo de teste:

```txt
ControllerTest = teste da camada HTTP com MockMvc
```

Depois rodar:

```bash
./mvnw clean test
```

O que estou testando aqui:

```txt
[ ] URLs
[ ] Métodos HTTP
[ ] Status HTTP
[ ] JSON de entrada e saída
[ ] Validações
[ ] Erros 400 e 404
```

---

## 9. Subir aplicação real

Usar quando a API já tiver controller pronto ou quando quiser conferir a aplicação real.

Se usar banco via Docker:

```bash
docker compose up -d
```

Depois:

```bash
./mvnw spring-boot:run
```

Quando usar:

```txt
[ ] Depois que a feature já tem endpoint
[ ] Antes de testar com curl
[ ] No final da feature, para validação manual
```

Importante:

```txt
spring-boot:run não substitui ./mvnw clean test.
Ele apenas sobe a aplicação real.
```

---

## 10. Testar manualmente com curl

Com a aplicação rodando, abrir outro terminal e testar os endpoints.

Checklist:

```txt
[ ] POST funcionando
[ ] GET funcionando
[ ] GET por ID funcionando
[ ] PUT funcionando, se existir
[ ] DELETE funcionando, se existir
[ ] Erro de validação retorna 400
[ ] ID inexistente retorna 404
```

Importante:

```txt
curl é teste manual.
Os testes automatizados continuam necessários.
```

---

## 11. Atualizar tutorial

Antes de encerrar a feature, atualizar a documentação/tutorial do projeto.

Checklist:

```txt
[ ] Explicar o que a feature faz
[ ] Listar arquivos criados
[ ] Explicar migration
[ ] Explicar entity e relacionamento
[ ] Explicar endpoints
[ ] Explicar testes criados
[ ] Registrar comandos usados
```

Regra:

```txt
Não encerrar feature sem atualizar o tutorial.
```

---

## 12. Rodar testes finais

Antes de commitar:

```bash
./mvnw clean test
```

Checklist:

```txt
[ ] Todos os testes passaram
[ ] A aplicação foi testada manualmente, se necessário
[ ] Tutorial foi atualizado
[ ] git status foi conferido
```

---

## 13. Commit e push

```bash
git status
git add .
git commit -m "feat: add nome-da-feature"
git push -u origin feature/nome-da-feature
```

Exemplos de commits:

```txt
feat: add album entity and repository
feat: add album service
feat: add album endpoints
test: add album tests
docs: update album tutorial
```

---

## 14. Merge na develop

Depois que a feature estiver pronta:

```bash
git checkout develop
git pull origin develop
git merge feature/nome-da-feature
./mvnw clean test
git push origin develop
```

Checklist:

```txt
[ ] Voltei para develop
[ ] Atualizei develop
[ ] Fiz merge da feature
[ ] Rodei testes depois do merge
[ ] Enviei develop para o GitHub
```

---

## Resumo rápido dos comandos

```bash
git checkout develop
git pull origin develop
git checkout -b feature/nome-da-feature

./mvnw clean test

docker compose up -d
./mvnw spring-boot:run

git status
git add .
git commit -m "feat: add nome-da-feature"
git push -u origin feature/nome-da-feature

git checkout develop
git pull origin develop
git merge feature/nome-da-feature
./mvnw clean test
git push origin develop
```

---

## Resumo dos tipos de teste

```txt
./mvnw clean test
Roda os testes automatizados.

RepositoryTest
Testa JPA, banco, queries e relacionamentos.

ServiceTest
Testa regra de negócio com Mockito.

ControllerTest
Testa endpoints, HTTP, JSON e validação com MockMvc.

spring-boot:run
Sobe a aplicação real.

curl
Testa manualmente a API real rodando.
```
