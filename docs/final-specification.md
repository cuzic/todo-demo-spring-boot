# To Do管理アプリケーション - 最終設計仕様書

## ドキュメント情報

- **作成日**: 2024-11-12
- **バージョン**: 1.0
- **ステータス**: 確定

---

## 1. プロジェクト概要

### 1.1 アプリケーション名
**Simple Todo**

### 1.2 目的
基本的なタスク管理機能を提供するWebアプリケーション（MVP版）

### 1.3 対象ユーザー
個人ユーザー（シングルユーザー）

---

## 2. 技術スタック

### 2.1 バックエンド

| 項目 | 技術 | バージョン |
|-----|------|----------|
| 言語 | Java | 21 (LTS) |
| フレームワーク | Spring Boot | 3.2.x |
| ビルドツール | Maven | 最新安定版 |
| テンプレートエンジン | Thymeleaf | Spring Boot標準 |
| データベース | H2 Database | Spring Boot標準 |
| ORM | Spring Data JPA | Spring Boot標準 |

### 2.2 フロントエンド

| 項目 | 技術 | バージョン | 導入方法 |
|-----|------|----------|---------|
| CSSフレームワーク | Bootstrap | 5.3.x | CDN |
| アイコン | Font Awesome | 6.x | CDN |
| JavaScript | 最小限 | - | インライン |

### 2.3 JavaScript使用範囲

- **使用する機能**:
  - 削除時の確認ダイアログのみ
  - 実装例: `onclick="return confirm('このタスクを削除しますか？')"`

- **使用しない機能**:
  - フォーム送信時のローディング表示
  - リアルタイムバリデーション
  - チェックボックスの自動送信
  - その他のインタラクティブ機能

---

## 3. データベース設定

### 3.1 H2データベース構成

```properties
# H2 Database設定（ファイルベース）
spring.datasource.url=jdbc:h2:file:./data/tododb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# H2 Console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# JPA/Hibernate設定
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# 初期データ投入
spring.sql.init.mode=always
spring.sql.init.data-locations=classpath:data.sql
```

### 3.2 データベースファイル

- **保存先**: `./data/tododb.mv.db`
- **永続化**: あり（アプリケーション再起動後もデータ保持）
- **初期データ**: `src/main/resources/data.sql`から投入

### 3.3 スキーマ管理

- **DDL自動生成**: `update`（開発環境）
- **本番環境**: 将来的にFlywayマイグレーションを検討

---

## 4. プロジェクト構成

### 4.1 パッケージ構造（レイヤー別）

```
com.example.simpletodo
├── controller
│   └── TaskController.java
├── service
│   └── TaskService.java
├── repository
│   └── TaskRepository.java
├── entity
│   └── Task.java
├── dto
│   └── TaskForm.java
└── SimpleTodoApplication.java
```

### 4.2 リソース構成

```
src/main/resources
├── templates
│   ├── layout
│   │   └── default.html          # 共通レイアウト
│   ├── tasks
│   │   ├── list.html             # タスク一覧画面
│   │   └── edit.html             # タスク編集画面
│   └── error
│       └── error.html            # エラーページ
├── static
│   ├── css
│   │   └── custom.css            # カスタムCSS
│   └── js
│       └── custom.js             # カスタムJS（最小限）
├── data.sql                      # 初期データ
└── application.properties        # アプリケーション設定
```

### 4.3 Maven依存関係

```xml
<dependencies>
    <!-- Spring Boot Starter Web -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!-- Spring Boot Starter Thymeleaf -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-thymeleaf</artifactId>
    </dependency>

    <!-- Spring Boot Starter Data JPA -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>

    <!-- Spring Boot Starter Validation -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>

    <!-- H2 Database -->
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <scope>runtime</scope>
    </dependency>

    <!-- Lombok (Optional) -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>

    <!-- Spring Boot Starter Test -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

---

## 5. UI/UX設計

### 5.1 カラースキーム（Bootstrap標準）

| 用途 | カラー | カラーコード | Bootstrapクラス |
|-----|-------|------------|----------------|
| プライマリ（ボタン、リンク） | Blue | #0d6efd | btn-primary |
| セカンダリ | Gray | #6c757d | btn-secondary |
| 成功メッセージ | Green | #198754 | alert-success |
| エラーメッセージ | Red | #dc3545 | alert-danger |
| 警告メッセージ | Yellow | #ffc107 | alert-warning |
| 完了済みタスク | Gray | #6c757d | text-muted |

### 5.2 レイアウト設計

#### タスク一覧画面

```
┌─────────────────────────────────────────────────┐
│  Simple Todo                        [H1]         │
├─────────────────────────────────────────────────┤
│  [ 成功/エラーメッセージ（あれば） ]            │
├─────────────────────────────────────────────────┤
│  [新しいタスクを入力...              ] [追加]   │
├─────────────────────────────────────────────────┤
│  [すべて] [未完了] [完了済み]      (タブ)       │
├─────────────────────────────────────────────────┤
│  ☐ タスク1                    [編集] [削除]    │
│  ─────────────────────────────────────────────  │
│  ☑ タスク2                    [編集] [削除]    │
│  ─────────────────────────────────────────────  │
│  ☐ タスク3                    [編集] [削除]    │
└─────────────────────────────────────────────────┘
```

#### タスク編集画面

```
┌─────────────────────────────────────────────────┐
│  ← 戻る                                          │
│  タスクの編集                       [H2]         │
├─────────────────────────────────────────────────┤
│  タスクタイトル                                  │
│  [既存のタスクタイトル__________________]       │
│                                                  │
│  [保存] [キャンセル]                            │
└─────────────────────────────────────────────────┘
```

### 5.3 ボタン配置

- **タスク一覧画面**: 編集・削除ボタンは各タスク行の右寄せ
- **タスク編集画面**: 保存・キャンセルボタンは左寄せ

### 5.4 アイコン使用（Font Awesome）

| 用途 | アイコン | クラス |
|-----|---------|--------|
| 編集 | ペンアイコン | fas fa-edit |
| 削除 | ゴミ箱アイコン | fas fa-trash |
| 戻る | 矢印アイコン | fas fa-arrow-left |
| チェック（完了） | チェックアイコン | fas fa-check |

### 5.5 メッセージ表示

#### 成功メッセージ
- **表示位置**: ページ上部（フォームの上）
- **スタイル**: Bootstrap Alert（alert-success）
- **閉じるボタン**: あり（×ボタン）
- **自動消去**: なし（手動で閉じる）

```html
<div class="alert alert-success alert-dismissible fade show" role="alert">
  タスクを作成しました。
  <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
</div>
```

#### エラーメッセージ
- **表示位置**: フォームの上
- **スタイル**: Bootstrap Alert（alert-danger）
- **閉じるボタン**: なし
- **メッセージ例**:
  - 「タイトルを入力してください」
  - 「タイトルは255文字以内で入力してください」
  - 「タスクが見つかりません」

---

## 6. データモデル

### 6.1 Taskエンティティ

```java
@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "タイトルを入力してください")
    @Size(max = 255, message = "タイトルは255文字以内で入力してください")
    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false)
    private Boolean completed = false;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
```

### 6.2 TaskForm DTO

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskForm {

    @NotBlank(message = "タイトルを入力してください")
    @Size(max = 255, message = "タイトルは255文字以内で入力してください")
    private String title;
}
```

---

## 7. バリデーション仕様

### 7.1 タイトルのバリデーション

| 項目 | ルール | エラーメッセージ |
|-----|-------|----------------|
| 必須チェック | 空文字列不可 | タイトルを入力してください |
| 空白チェック | 空白のみ不可 | タイトルを入力してください |
| 最大文字数 | 255文字以内 | タイトルは255文字以内で入力してください |
| 最小文字数 | 1文字以上 | タイトルを入力してください |
| 特殊文字 | すべて許可 | - |
| 改行 | 許可 | - |

### 7.2 バリデーションタイミング

- **サーバーサイド**: すべてのPOSTリクエストで実施
- **クライアントサイド**: 実装しない

---

## 8. エラーハンドリング

### 8.1 404エラー（タスク未存在）

- **発生ケース**: 存在しないタスクIDで編集画面にアクセス、または編集/削除を試みた場合
- **処理**:
  1. エラーメッセージをフラッシュメッセージに設定
  2. タスク一覧画面にリダイレクト（302）
- **エラーメッセージ**: 「指定されたタスクが見つかりません」

### 8.2 500エラー（サーバーエラー）

- **発生ケース**: 予期しないエラー
- **処理**:
  1. カスタムエラーページを表示
  2. エラーログを記録
- **エラーページの内容**:
  - エラーメッセージ: 「エラーが発生しました」
  - 詳細: 開発環境のみスタックトレースを表示
  - 戻るリンク: タスク一覧へのリンク

### 8.3 バリデーションエラー

- **発生ケース**: タイトルが空、または255文字超過
- **処理**:
  1. 元の画面に戻る（リダイレクトなし）
  2. エラーメッセージを表示
  3. 入力内容を保持

---

## 9. セキュリティ

### 9.1 CSRF対策

- **実装方法**: Thymeleafの`th:action`による自動トークン付与
- **Spring Security**: 導入しない（MVPでは不要）

### 9.2 XSS対策

- **実装方法**: Thymeleafの自動エスケープ
- **使用タグ**: `th:text`（`th:utext`は使用しない）

### 9.3 SQLインジェクション対策

- **実装方法**: Spring Data JPAのパラメータバインディング
- **PreparedStatement**: 使用

---

## 10. テスト仕様

### 10.1 テスト範囲

| レイヤー | テスト方法 | カバレッジ目標 |
|---------|-----------|---------------|
| Controller | MockMvc | 80%以上 |
| Service | ユニットテスト（モック使用） | 80%以上 |
| Repository | 簡易テスト | 主要CRUD操作のみ |

### 10.2 テストデータ

- **管理方法**: 各テストメソッドでデータを作成
- **テスト用DB**: H2インメモリ（テスト専用）

### 10.3 テストツール

- **単体テスト**: JUnit 5
- **モック**: Mockito
- **アサーション**: AssertJ

---

## 11. CDN設定

### 11.1 Bootstrap

```html
<!-- CSS -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
      rel="stylesheet">

<!-- JS Bundle -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js">
</script>
```

### 11.2 Font Awesome

```html
<!-- Font Awesome -->
<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css"
      rel="stylesheet">
```

---

## 12. 初期データ（data.sql）

```sql
-- 開発用の初期データ
INSERT INTO tasks (title, completed, created_at, updated_at) VALUES
    ('Spring Bootの環境構築', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('データベース設計', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('画面設計書の作成', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('To Doアプリの実装', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('ユニットテストの作成', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
```

---

## 13. アプリケーション設定（application.properties）

```properties
# アプリケーション名
spring.application.name=Simple Todo

# サーバー設定
server.port=8080

# H2 Database設定（ファイルベース）
spring.datasource.url=jdbc:h2:file:./data/tododb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# H2 Console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# JPA/Hibernate設定
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect

# 初期データ投入
spring.sql.init.mode=always
spring.sql.init.data-locations=classpath:data.sql
spring.sql.init.encoding=UTF-8

# Thymeleaf設定
spring.thymeleaf.cache=false
spring.thymeleaf.encoding=UTF-8

# ログ設定
logging.level.root=INFO
logging.level.com.example.simpletodo=DEBUG
logging.level.org.springframework.web=DEBUG
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE

# メッセージ設定（日本語）
spring.messages.basename=messages
spring.messages.encoding=UTF-8
```

---

## 14. 命名規則

### 14.1 Javaクラス・メソッド

| 対象 | 規則 | 例 |
|-----|------|-----|
| クラス名 | PascalCase | TaskController, TaskService |
| メソッド名 | camelCase | getAllTasks, createTask |
| 変数名 | camelCase | taskList, taskForm |
| 定数 | UPPER_SNAKE_CASE | MAX_TITLE_LENGTH |

### 14.2 データベース

| 対象 | 規則 | 例 |
|-----|------|-----|
| テーブル名 | snake_case（複数形） | tasks |
| カラム名 | snake_case | created_at, updated_at |
| インデックス名 | idx_{table}_{column} | idx_tasks_completed |

### 14.3 URL・ファイル

| 対象 | 規則 | 例 |
|-----|------|-----|
| URL | kebab-case | /tasks, /tasks/{id}/edit |
| HTMLファイル | kebab-case | list.html, edit.html |
| CSSファイル | kebab-case | custom.css |

---

## 15. 画面仕様詳細

### 15.1 タスク一覧画面（list.html）

#### URLマッピング
- `/` または `/tasks` - すべてのタスク
- `/tasks?filter=active` - 未完了タスク
- `/tasks?filter=completed` - 完了済みタスク

#### 画面要素

**ヘッダー**
```html
<h1>Simple Todo</h1>
```

**成功/エラーメッセージ（条件付き）**
```html
<div th:if="${successMessage}" class="alert alert-success alert-dismissible fade show">
  <span th:text="${successMessage}"></span>
  <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
</div>

<div th:if="${errorMessage}" class="alert alert-danger">
  <span th:text="${errorMessage}"></span>
</div>
```

**タスク作成フォーム**
```html
<form th:action="@{/tasks}" method="post" th:object="${taskForm}">
  <div class="input-group mb-3">
    <input type="text" th:field="*{title}" class="form-control"
           placeholder="新しいタスクを入力..." maxlength="255">
    <button type="submit" class="btn btn-primary">
      <i class="fas fa-plus"></i> 追加
    </button>
  </div>
  <div th:if="${#fields.hasErrors('title')}" class="alert alert-danger">
    <span th:errors="*{title}"></span>
  </div>
</form>
```

**フィルタータブ**
```html
<ul class="nav nav-tabs mb-3">
  <li class="nav-item">
    <a class="nav-link" th:classappend="${filter == null or filter == 'all'} ? 'active'"
       th:href="@{/tasks}">すべて</a>
  </li>
  <li class="nav-item">
    <a class="nav-link" th:classappend="${filter == 'active'} ? 'active'"
       th:href="@{/tasks(filter='active')}">未完了</a>
  </li>
  <li class="nav-item">
    <a class="nav-link" th:classappend="${filter == 'completed'} ? 'active'"
       th:href="@{/tasks(filter='completed')}">完了済み</a>
  </li>
</ul>
```

**タスクリスト**
```html
<div th:if="${#lists.isEmpty(tasks)}" class="alert alert-info">
  タスクがありません
</div>

<div th:unless="${#lists.isEmpty(tasks)}" class="list-group">
  <div th:each="task : ${tasks}" class="list-group-item d-flex justify-content-between align-items-center">
    <div class="d-flex align-items-center flex-grow-1">
      <form th:action="@{/tasks/{id}/toggle(id=${task.id})}" method="post" class="me-3">
        <input type="checkbox" th:checked="${task.completed}"
               onchange="this.form.submit()">
      </form>
      <span th:text="${task.title}"
            th:classappend="${task.completed} ? 'text-decoration-line-through text-muted'">
      </span>
    </div>
    <div class="btn-group">
      <a th:href="@{/tasks/{id}/edit(id=${task.id})}" class="btn btn-sm btn-outline-secondary">
        <i class="fas fa-edit"></i> 編集
      </a>
      <form th:action="@{/tasks/{id}/delete(id=${task.id})}" method="post" class="d-inline">
        <button type="submit" class="btn btn-sm btn-outline-danger"
                onclick="return confirm('このタスクを削除しますか？')">
          <i class="fas fa-trash"></i> 削除
        </button>
      </form>
    </div>
  </div>
</div>
```

### 15.2 タスク編集画面（edit.html）

#### URLマッピング
- `/tasks/{id}/edit` - 編集画面表示
- `/tasks/{id}` (POST) - 更新処理

#### 画面要素

```html
<div class="mb-3">
  <a th:href="@{/tasks}" class="btn btn-link">
    <i class="fas fa-arrow-left"></i> 戻る
  </a>
</div>

<h2>タスクの編集</h2>

<form th:action="@{/tasks/{id}(id=${task.id})}" method="post" th:object="${taskForm}">
  <div class="mb-3">
    <label for="title" class="form-label">タスクタイトル</label>
    <input type="text" th:field="*{title}" id="title" class="form-control"
           maxlength="255">
    <div th:if="${#fields.hasErrors('title')}" class="alert alert-danger mt-2">
      <span th:errors="*{title}"></span>
    </div>
  </div>

  <div class="d-flex gap-2">
    <button type="submit" class="btn btn-primary">
      <i class="fas fa-save"></i> 保存
    </button>
    <a th:href="@{/tasks}" class="btn btn-secondary">
      キャンセル
    </a>
  </div>
</form>
```

---

## 16. コントローラー仕様

### 16.1 TaskController

```java
@Controller
public class TaskController {

    @Autowired
    private TaskService taskService;

    // タスク一覧表示
    @GetMapping({"/", "/tasks"})
    public String listTasks(
            @RequestParam(required = false) String filter,
            Model model) {
        List<Task> tasks;
        if ("active".equals(filter)) {
            tasks = taskService.getActiveTasks();
        } else if ("completed".equals(filter)) {
            tasks = taskService.getCompletedTasks();
        } else {
            tasks = taskService.getAllTasks();
        }
        model.addAttribute("tasks", tasks);
        model.addAttribute("filter", filter);
        model.addAttribute("taskForm", new TaskForm());
        return "tasks/list";
    }

    // タスク作成
    @PostMapping("/tasks")
    public String createTask(
            @Valid @ModelAttribute TaskForm taskForm,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "tasks/list";
        }
        taskService.createTask(taskForm.getTitle());
        redirectAttributes.addFlashAttribute("successMessage", "タスクを作成しました");
        return "redirect:/tasks";
    }

    // タスク完了切り替え
    @PostMapping("/tasks/{id}/toggle")
    public String toggleTask(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            taskService.toggleTaskCompletion(id);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "タスクが見つかりません");
        }
        return "redirect:/tasks";
    }

    // タスク削除
    @PostMapping("/tasks/{id}/delete")
    public String deleteTask(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            taskService.deleteTask(id);
            redirectAttributes.addFlashAttribute("successMessage", "タスクを削除しました");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "タスクが見つかりません");
        }
        return "redirect:/tasks";
    }

    // タスク編集画面表示
    @GetMapping("/tasks/{id}/edit")
    public String editTaskForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Task task = taskService.getTaskById(id);
            TaskForm taskForm = new TaskForm(task.getTitle());
            model.addAttribute("task", task);
            model.addAttribute("taskForm", taskForm);
            return "tasks/edit";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "タスクが見つかりません");
            return "redirect:/tasks";
        }
    }

    // タスク更新
    @PostMapping("/tasks/{id}")
    public String updateTask(
            @PathVariable Long id,
            @Valid @ModelAttribute TaskForm taskForm,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            Task task = taskService.getTaskById(id);
            model.addAttribute("task", task);
            return "tasks/edit";
        }
        try {
            taskService.updateTask(id, taskForm.getTitle());
            redirectAttributes.addFlashAttribute("successMessage", "タスクを更新しました");
            return "redirect:/tasks";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "タスクが見つかりません");
            return "redirect:/tasks";
        }
    }
}
```

---

## 17. 実行方法

### 17.1 ビルド

```bash
mvn clean package
```

### 17.2 実行

```bash
# 開発環境
mvn spring-boot:run

# JARファイル実行
java -jar target/simple-todo-1.0.0.jar
```

### 17.3 アクセス

- **アプリケーション**: http://localhost:8080
- **H2 Console**: http://localhost:8080/h2-console

### 17.4 H2 Console接続情報

- **JDBC URL**: `jdbc:h2:file:./data/tododb`
- **User Name**: `sa`
- **Password**: （空欄）

---

## 18. 開発の進め方

### Phase 1: 環境構築
1. [ ] Spring Initializrでプロジェクト作成
2. [ ] Maven依存関係の追加
3. [ ] application.propertiesの設定
4. [ ] プロジェクト構造の作成

### Phase 2: データ層の実装
1. [ ] Taskエンティティの作成
2. [ ] TaskRepositoryの作成
3. [ ] data.sqlの作成
4. [ ] 動作確認（H2 Console）

### Phase 3: ビジネスロジック層の実装
1. [ ] TaskServiceの作成
2. [ ] CRUD操作の実装
3. [ ] ユニットテストの作成

### Phase 4: プレゼンテーション層の実装（基本機能）
1. [ ] TaskControllerの作成
2. [ ] TaskFormの作成
3. [ ] タスク一覧画面の作成
4. [ ] タスク作成機能の実装
5. [ ] タスク削除機能の実装

### Phase 5: 完了・編集機能の実装
1. [ ] タスク完了切り替え機能の実装
2. [ ] タスク編集画面の作成
3. [ ] タスク更新機能の実装

### Phase 6: フィルタリング機能の実装
1. [ ] フィルタータブの実装
2. [ ] 未完了タスクの絞り込み
3. [ ] 完了済みタスクの絞り込み

### Phase 7: UI/UX改善
1. [ ] Bootstrap適用
2. [ ] Font Awesome適用
3. [ ] カスタムCSSの追加
4. [ ] レスポンシブ対応

### Phase 8: テストとデバッグ
1. [ ] 統合テストの実施
2. [ ] 受入基準チェックリストによる確認
3. [ ] バグ修正

### Phase 9: ドキュメント整備
1. [ ] README.mdの作成
2. [ ] コメントの追加
3. [ ] リリースノートの作成

---

## 19. 関連ドキュメント

1. [ユーザーストーリー](./user-stories.md)
2. [画面設計](./screen-design.md)
3. [データベース設計](./database-design.md)
4. [データフロー図](./dataflow-diagram.md)
5. [受入基準チェックリスト](./acceptance-criteria.md)

---

## 20. 変更履歴

| 日付 | バージョン | 変更内容 | 変更者 |
|-----|----------|---------|-------|
| 2024-11-12 | 1.0 | 初版作成（すべての仕様を確定） | - |

---

## 付録: クイックリファレンス

### 主要URL一覧

| 機能 | HTTPメソッド | URL |
|-----|------------|-----|
| タスク一覧 | GET | / または /tasks |
| タスク作成 | POST | /tasks |
| タスク完了切り替え | POST | /tasks/{id}/toggle |
| タスク削除 | POST | /tasks/{id}/delete |
| タスク編集画面 | GET | /tasks/{id}/edit |
| タスク更新 | POST | /tasks/{id} |
| 未完了フィルター | GET | /tasks?filter=active |
| 完了済みフィルター | GET | /tasks?filter=completed |

### 主要クラス一覧

| クラス | 役割 | パッケージ |
|-------|------|-----------|
| SimpleTodoApplication | メインクラス | com.example.simpletodo |
| Task | エンティティ | com.example.simpletodo.entity |
| TaskForm | フォームDTO | com.example.simpletodo.dto |
| TaskRepository | リポジトリ | com.example.simpletodo.repository |
| TaskService | サービス | com.example.simpletodo.service |
| TaskController | コントローラー | com.example.simpletodo.controller |

### Bootstrap主要クラス

| 用途 | クラス |
|-----|-------|
| ボタン（プライマリ） | btn btn-primary |
| ボタン（セカンダリ） | btn btn-secondary |
| ボタン（危険） | btn btn-outline-danger |
| ボタン（小） | btn btn-sm |
| アラート（成功） | alert alert-success |
| アラート（エラー） | alert alert-danger |
| リストグループ | list-group, list-group-item |
| フォームコントロール | form-control |
| 入力グループ | input-group |

---

**このドキュメントで、To Do管理アプリケーションの開発に必要なすべての仕様が確定しました。**
**開発を開始できます！**
