# 🌱 RotinApp — Trabalho 3: Room

App Android feito em **Kotlin com Jetpack Compose**, continuação do Trabalho 2.

Agora as rotinas, atividades, metas e o perfil ficam **salvos no celular** com o banco de dados **Room**. Mesmo fechando o app, os dados continuam lá.

> 📚 Projeto acadêmico individual — Universidade Positivo — Desenvolvimento de Aplicativos Móveis

---

## ▶️ Como rodar o projeto

1. Baixar o projeto (`git clone URL_DO_REPOSITORIO` ou baixar o `.zip` e descompactar).
2. No Android Studio: **File > Open** e escolher a pasta do projeto (a que tem o `settings.gradle.kts`).
3. Esperar o **Gradle Sync** terminar (barra no rodapé). Na primeira vez precisa de internet.
4. Criar um emulador: **Tools > Device Manager** → **+** → escolher um celular (ex.: Pixel) → escolher uma versão do Android → **Finish**.
5. Na barra de cima, escolher **app** e o emulador, e clicar em **Run ▶**.
6. O app abre sozinho com o nome **"My Application"**.

### ⚙️ Se não conseguir rodar

* **Aparece "Add Configuration…" no lugar de "app":** ir em **Run > Edit Configurations** → **+** → **Android App** → Name: `app` → Module: `My_Application.app` → Launch: `Default Activity` → **OK**.
* **O campo Module está vazio:** o Gradle Sync não terminou ou deu erro. Rodar **File > Sync Project with Gradle Files** e ver o erro na aba **Build**.
* **Erro de Java/JDK no Sync:** **File > Settings > Build, Execution, Deployment > Build Tools > Gradle** → **Gradle JDK** → escolher um **JDK 21**.
* **Erro dizendo que falta plataforma/SDK:** clicar no link que o Android Studio mostra para instalar e sincronizar de novo.

### 🧪 Teste de persistência

1. Adicionar uma rotina ou uma meta.
2. Fechar o app (apps recentes → arrastar o app para fora).
3. Abrir o app de novo.
4. ✅ O item continua lá.

### 🔍 Ver o banco por dentro

Com o app rodando: **View > Tool Windows > App Inspection** → **Database Inspector** → `rotinapp_database`.

---

# 📝 Documentação do processo e das decisões

## 1. Como estava o projeto no Trabalho 2, e o que mudou para chegar até aqui?

**Como estava:** o app já tinha as telas, a navegação, as listas e os formulários. Mas os dados ficavam só na memória, em listas `mutableStateListOf`. Quando o app era fechado, tudo sumia.

**O que mudou:**

* Adicionei o **Room** ao projeto (banco de dados local).
* As `data class` do Trabalho 2 viraram tabelas do banco (`@Entity`).
* Todas as telas passaram a **buscar e salvar os dados no banco**, e não mais nas listas da memória.
* Criei a **edição de metas** (botão ✏️ no card da meta).
* A tela de **Detalhes da rotina** agora recebe só o **id** e busca a rotina no banco.
* Os horários de **início e fim** da rotina e o **perfil** também passaram a ser salvos.

---

## 2. Quais Entity, DAO e Database foram criados — e por que escolheram esses campos para cada Entity?

**Entities (tabelas):**

| Entity | Campos | Por quê |
|---|---|---|
| `Rotina` | id, nome, tempo total, hora de início, hora de fim | são as informações que aparecem na lista e no formulário de rotina |
| `Atividade` | id, id da rotina, nome, duração | cada atividade pertence a uma rotina; a duração soma no tempo total e no XP |
| `Meta` | id, nome, descrição, concluída | são os dados da tela de metas, incluindo o checkbox de concluída |
| `Perfil` | id, nome, e-mail | são os dados da tela de cadastro |

Em todas, o `id` é criado automaticamente pelo banco (`@PrimaryKey(autoGenerate = true)`).

**DAOs:** um para cada tabela (`RotinaDAO`, `AtividadeDAO`, `MetaDAO`, `PerfilDAO`). Todos têm os 4 métodos básicos: **inserir**, **buscar todos**, **atualizar** e **deletar**.

**Database:** o `AppDatabase` junta as 4 tabelas. Ele segue o padrão **Singleton** visto em aula, ou seja, o app usa sempre a mesma instância do banco.

---

## 3. Como decidiram estruturar as queries do DAO (quais métodos além dos 4 básicos, se houver)?

Além dos 4 básicos, criei só os métodos que as telas precisavam:

* **`buscarPorId`** (rotinas): a tela de Detalhes recebe o id e busca a rotina certa no banco.
* **`buscarPorRotina`** (atividades): mostra só as atividades da rotina aberta.
* **`deletarPorRotina`** (atividades): quando uma rotina é excluída, as atividades dela são apagadas junto.

Exemplo:

```kotlin
@Query("SELECT * FROM rotinas WHERE id = :id")
suspend fun buscarPorId(id: Int) : Rotina?
```

---

## 4. Qual foi a dificuldade em migrar de `mutableStateListOf` para Room? O que quebrou no caminho e como resolveram?

**Dificuldade:** antes as listas eram globais e mudavam na hora. Com o Room, o banco precisa ser acessado em segundo plano (com Coroutines). Por isso cada tela passou a buscar os seus dados quando abre.

**O que quebrou ou precisou mudar, e como resolvi:**

* **Ao apagar as listas antigas, várias telas pararam de compilar.** → Cada tela passou a buscar os dados no banco com `LaunchedEffect`.
* **O id passou a ser criado pelo banco.** → A rotina nova devolve o id gerado, e as atividades são salvas com esse id.
* **Toast e troca de tela não podem rodar em segundo plano.** → Depois de salvar, eles rodam na thread principal (`withContext(Dispatchers.Main)`).
* **Excluir uma rotina deixaria as atividades dela no banco.** → Criei o `deletarPorRotina`.

---

## 5. Print ou vídeo mostrando o teste de persistência: adicionar um item, fechar o app, reabrir, e o item continuar lá

**🎬 Vídeo:** _(colar aqui o link do vídeo)_

**🖼️ Prints:**

> Os prints foram gerados por um teste automático que roda o app de verdade num Android simulado no computador.

**1. Adicionando uma rotina:**

<p>
  <img src="docs/prints/01-adicionando-rotina.png" width="220" alt="Formulário de nova rotina">
  <img src="docs/prints/02-rotina-salva-na-lista.png" width="220" alt="Rotina salva na lista">
  <img src="docs/prints/03-detalhes-buscados-pelo-id.png" width="220" alt="Detalhes da rotina">
</p>

**2. Antes de fechar o app:**

<p>
  <img src="docs/prints/04-antes-de-fechar-rotinas.png" width="220" alt="Rotinas antes de fechar o app">
  <img src="docs/prints/05-antes-de-fechar-metas.png" width="220" alt="Metas antes de fechar o app">
  <img src="docs/prints/06-antes-de-fechar-perfil.png" width="220" alt="Perfil antes de fechar o app">
</p>

**3. Depois de fechar e abrir de novo (os dados continuam lá ✅):**

<p>
  <img src="docs/prints/07-depois-de-reabrir-rotinas.png" width="220" alt="Rotinas depois de reabrir o app">
  <img src="docs/prints/08-depois-de-reabrir-metas.png" width="220" alt="Metas depois de reabrir o app">
  <img src="docs/prints/09-depois-de-reabrir-perfil.png" width="220" alt="Perfil depois de reabrir o app">
  <img src="docs/prints/10-depois-de-reabrir-detalhes.png" width="220" alt="Detalhes depois de reabrir o app">
</p>

---

## 6. Algum integrante do trio teve dificuldade em algum ponto? Como resolveram?

Não se aplica: o trabalho foi feito **individualmente**.

---

# ✅ Checklist do enunciado

| Item | Onde está |
|---|---|
| Room configurado e compilando | `build.gradle.kts` |
| 2 ou mais `@Entity` com `autoGenerate = true` | `Rotina.kt`, `Atividade.kt`, `Meta.kt`, `Perfil.kt` |
| 2 ou mais `@Dao` com os 4 métodos `suspend` | `RotinaDAO.kt`, `AtividadeDAO.kt`, `MetaDAO.kt`, `PerfilDAO.kt` |
| Consulta filtrada (`buscarPorId`) | `RotinaDAO.kt` |
| Detalhes buscando pelo id | `TelaRotina.kt` |
| `AppDatabase` Singleton | `AppDatabase.kt` |
| Listas buscam do banco ao abrir (`LaunchedEffect`) | telas de rotinas, metas, histórico, desempenho, progresso e perfil |
| Adicionar, editar e remover salvam no banco | telas de rotinas e metas (o perfil também salva e edita) |
| Operações de banco em `CoroutineScope(Dispatchers.IO).launch` | todas as telas |
| `try/catch` com `Log.e` | `OperacoesBanco.kt` |
| Teste de fechar e reabrir | item 5 |

---

## 🛠️ Tecnologias

Kotlin, Jetpack Compose, Navigation Compose, **Room 2.8.5** (com KSP), Coroutines, ViewModel, Git e Android Studio.

---

## 📂 Arquivos principais

```text
app/src/main/java/com/example/myapplication/
├── AppDatabase.kt        → banco de dados (Singleton)
├── RotinaDAO.kt          → consultas das rotinas
├── AtividadeDAO.kt       → consultas das atividades
├── MetaDAO.kt            → consultas das metas
├── PerfilDAO.kt          → consultas do perfil
├── OperacoesBanco.kt     → funções do banco com try/catch
├── TelaListaRotina.kt    → lista de rotinas
├── TelaCriacao.kt        → criar/editar rotina
├── TelaRotina.kt         → detalhes da rotina (busca pelo id)
├── TelaMetas.kt          → metas
├── TelaHistorico.kt / TelaDesempenho.kt / Progresso.kt / TelaPerfil.kt
└── ui/theme/
    └── Rotina.kt / Atividade.kt / Meta.kt / Perfil.kt   → tabelas (@Entity)
```

---

**Universidade Positivo — Desenvolvimento de Aplicativos Móveis — Trabalho 3**
