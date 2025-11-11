# To Do管理アプリケーション - データフロー図（DFD）

## 1. コンテキスト図（レベル0 DFD）

システム全体の概要を示す最上位のデータフロー図

```mermaid
graph LR
    User([ユーザー])
    System[To Do管理システム]
    DB[(H2 Database<br/>tasks)]

    User -->|タスク操作リクエスト| System
    System -->|タスク情報表示| User
    System -->|データ保存/取得/更新/削除| DB
    DB -->|タスクデータ| System

    style User fill:#e1f5ff
    style System fill:#fff4e1
    style DB fill:#f0f0f0
```

### データフロー説明

| データフロー名 | 送信元 | 送信先 | 内容 |
|------------|-------|-------|------|
| タスク操作リクエスト | ユーザー | システム | タスクの作成、更新、削除、表示、フィルタリングのリクエスト |
| タスク情報表示 | システム | ユーザー | タスク一覧、編集画面などのHTMLページ |
| データ保存/取得/更新/削除 | システム | データベース | SQLクエリによるCRUD操作 |
| タスクデータ | データベース | システム | タスク情報のレコード |

---

## 2. レベル1 DFD（機能別詳細図）

### 2.1 全体の機能とデータフロー

```mermaid
graph TB
    User([ユーザー])

    subgraph "To Do管理システム"
        P1[1.0<br/>タスク一覧表示]
        P2[2.0<br/>タスク作成]
        P3[3.0<br/>タスク更新]
        P4[4.0<br/>タスク完了切り替え]
        P5[5.0<br/>タスク削除]
        P6[6.0<br/>タスクフィルタリング]
    end

    DB[(tasks<br/>データストア)]

    User -->|表示リクエスト| P1
    User -->|新規タスク情報| P2
    User -->|更新タスク情報| P3
    User -->|完了切り替えリクエスト| P4
    User -->|削除リクエスト| P5
    User -->|フィルター条件| P6

    P1 -->|タスク一覧HTML| User
    P2 -->|作成完了通知| User
    P3 -->|更新完了通知| User
    P4 -->|切り替え完了通知| User
    P5 -->|削除完了通知| User
    P6 -->|フィルター済み一覧HTML| User

    P1 -->|SELECT クエリ| DB
    P2 -->|INSERT クエリ| DB
    P3 -->|UPDATE クエリ| DB
    P4 -->|UPDATE クエリ| DB
    P5 -->|DELETE クエリ| DB
    P6 -->|SELECT クエリ<br/>WHERE completed=?| DB

    DB -->|全タスクデータ| P1
    DB -->|作成結果| P2
    DB -->|更新結果| P3
    DB -->|更新結果| P4
    DB -->|削除結果| P5
    DB -->|フィルター済みデータ| P6

    style User fill:#e1f5ff
    style DB fill:#f0f0f0
    style P1 fill:#fff4e1
    style P2 fill:#e8f5e9
    style P3 fill:#e8f5e9
    style P4 fill:#e8f5e9
    style P5 fill:#ffebee
    style P6 fill:#f3e5f5
```

---

## 3. 各プロセスの詳細データフロー

### 3.1 プロセス 1.0: タスク一覧表示

```mermaid
graph LR
    User([ユーザー])

    subgraph "プロセス 1.0"
        P1_1[1.1 リクエスト受付<br/>TaskController]
        P1_2[1.2 データ取得<br/>TaskService]
        P1_3[1.3 HTML生成<br/>Thymeleaf]
    end

    DB[(tasks)]

    User -->|GET /tasks| P1_1
    P1_1 -->|findAll| P1_2
    P1_2 -->|SELECT * FROM tasks| DB
    DB -->|List&lt;Task&gt;| P1_2
    P1_2 -->|tasksリスト| P1_3
    P1_3 -->|list.html| User

    style User fill:#e1f5ff
    style DB fill:#f0f0f0
```

**入力データ**:
- HTTPリクエスト: `GET /tasks`

**処理内容**:
1. コントローラーがリクエストを受け付け
2. サービス層がリポジトリ経由でデータを取得
3. Thymeleafがモデルデータを使ってHTMLを生成

**出力データ**:
- タスク一覧HTML（list.html）

**データストア操作**:
```sql
SELECT id, title, completed, created_at, updated_at
FROM tasks
ORDER BY created_at DESC;
```

---

### 3.2 プロセス 2.0: タスク作成

```mermaid
graph LR
    User([ユーザー])

    subgraph "プロセス 2.0"
        P2_1[2.1 リクエスト受付<br/>TaskController]
        P2_2[2.2 バリデーション<br/>Validator]
        P2_3[2.3 タスク保存<br/>TaskService]
        P2_4[2.4 リダイレクト<br/>Controller]
    end

    DB[(tasks)]

    User -->|POST /tasks<br/>title| P2_1
    P2_1 -->|TaskForm| P2_2
    P2_2 -->|検証OK| P2_3
    P2_2 -.->|検証NG| User
    P2_3 -->|save| DB
    DB -->|新規Task| P2_3
    P2_3 -->|成功| P2_4
    P2_4 -->|302 Redirect<br/>GET /tasks| User

    style User fill:#e1f5ff
    style DB fill:#f0f0f0
```

**入力データ**:
- HTTPリクエスト: `POST /tasks`
- リクエストボディ: `title` (String)

**処理内容**:
1. コントローラーがフォームデータを受け取り
2. バリデーション実施（タイトル必須チェック）
3. 検証OKならサービス層でタスクを保存
4. リダイレクトでタスク一覧表示

**出力データ**:
- 成功時: リダイレクト（302）→ `GET /tasks`
- 失敗時: エラーメッセージ付きフォーム

**データストア操作**:
```sql
INSERT INTO tasks (title, completed, created_at, updated_at)
VALUES (?, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
```

---

### 3.3 プロセス 3.0: タスク更新

```mermaid
graph TB
    User([ユーザー])

    subgraph "プロセス 3.0"
        P3_1[3.1 編集画面表示<br/>TaskController]
        P3_2[3.2 タスク取得<br/>TaskService]
        P3_3[3.3 更新リクエスト<br/>TaskController]
        P3_4[3.4 バリデーション<br/>Validator]
        P3_5[3.5 タスク更新<br/>TaskService]
    end

    DB[(tasks)]

    User -->|GET /tasks/:id/edit| P3_1
    P3_1 -->|findById| P3_2
    P3_2 -->|SELECT| DB
    DB -->|Task| P3_2
    P3_2 -->|Task| P3_1
    P3_1 -->|edit.html| User

    User -->|POST /tasks/:id<br/>title| P3_3
    P3_3 -->|TaskForm| P3_4
    P3_4 -->|検証OK| P3_5
    P3_4 -.->|検証NG| User
    P3_5 -->|UPDATE| DB
    DB -->|更新結果| P3_5
    P3_5 -->|成功| User

    style User fill:#e1f5ff
    style DB fill:#f0f0f0
```

**入力データ（編集画面表示）**:
- HTTPリクエスト: `GET /tasks/{id}/edit`
- パスパラメータ: `id` (Long)

**入力データ（更新）**:
- HTTPリクエスト: `POST /tasks/{id}`
- リクエストボディ: `title` (String)

**処理内容**:
1. 編集画面表示時: IDでタスクを取得して表示
2. 更新時: フォームデータをバリデーション
3. 検証OKならタスクを更新
4. リダイレクトでタスク一覧表示

**出力データ**:
- 編集画面: edit.html
- 更新成功: リダイレクト（302）→ `GET /tasks`

**データストア操作**:
```sql
-- 取得
SELECT id, title, completed, created_at, updated_at
FROM tasks
WHERE id = ?;

-- 更新
UPDATE tasks
SET title = ?, updated_at = CURRENT_TIMESTAMP
WHERE id = ?;
```

---

### 3.4 プロセス 4.0: タスク完了切り替え

```mermaid
graph LR
    User([ユーザー])

    subgraph "プロセス 4.0"
        P4_1[4.1 リクエスト受付<br/>TaskController]
        P4_2[4.2 タスク取得<br/>TaskService]
        P4_3[4.3 完了状態反転<br/>TaskService]
        P4_4[4.4 タスク保存<br/>TaskService]
    end

    DB[(tasks)]

    User -->|POST /tasks/:id/toggle| P4_1
    P4_1 -->|findById| P4_2
    P4_2 -->|SELECT| DB
    DB -->|Task| P4_2
    P4_2 -->|Task| P4_3
    P4_3 -->|completed = !completed| P4_3
    P4_3 -->|Task| P4_4
    P4_4 -->|UPDATE| DB
    DB -->|更新結果| P4_4
    P4_4 -->|302 Redirect| User

    style User fill:#e1f5ff
    style DB fill:#f0f0f0
```

**入力データ**:
- HTTPリクエスト: `POST /tasks/{id}/toggle`
- パスパラメータ: `id` (Long)

**処理内容**:
1. IDでタスクを取得
2. 完了フラグを反転（true ⇔ false）
3. 更新したタスクを保存
4. リダイレクトでタスク一覧表示

**出力データ**:
- リダイレクト（302）→ `GET /tasks`

**データストア操作**:
```sql
-- 取得と更新を1つのクエリで実行
UPDATE tasks
SET completed = NOT completed, updated_at = CURRENT_TIMESTAMP
WHERE id = ?;
```

---

### 3.5 プロセス 5.0: タスク削除

```mermaid
graph LR
    User([ユーザー])

    subgraph "プロセス 5.0"
        P5_1[5.1 リクエスト受付<br/>TaskController]
        P5_2[5.2 タスク削除<br/>TaskService]
    end

    DB[(tasks)]

    User -->|POST /tasks/:id/delete| P5_1
    P5_1 -->|deleteById| P5_2
    P5_2 -->|DELETE| DB
    DB -->|削除結果| P5_2
    P5_2 -->|302 Redirect| User

    style User fill:#e1f5ff
    style DB fill:#f0f0f0
```

**入力データ**:
- HTTPリクエスト: `POST /tasks/{id}/delete`
- パスパラメータ: `id` (Long)

**処理内容**:
1. IDでタスクを削除
2. リダイレクトでタスク一覧表示

**出力データ**:
- リダイレクト（302）→ `GET /tasks`

**データストア操作**:
```sql
DELETE FROM tasks WHERE id = ?;
```

---

### 3.6 プロセス 6.0: タスクフィルタリング

```mermaid
graph LR
    User([ユーザー])

    subgraph "プロセス 6.0"
        P6_1[6.1 リクエスト受付<br/>TaskController]
        P6_2[6.2 条件判定<br/>TaskService]
        P6_3[6.3 フィルター実行<br/>TaskRepository]
        P6_4[6.4 HTML生成<br/>Thymeleaf]
    end

    DB[(tasks)]

    User -->|GET /tasks?filter=<br/>active/completed| P6_1
    P6_1 -->|filterパラメータ| P6_2
    P6_2 -->|completed条件| P6_3
    P6_3 -->|SELECT WHERE| DB
    DB -->|フィルター済みリスト| P6_3
    P6_3 -->|List&lt;Task&gt;| P6_4
    P6_4 -->|list.html| User

    style User fill:#e1f5ff
    style DB fill:#f0f0f0
```

**入力データ**:
- HTTPリクエスト: `GET /tasks?filter={status}`
- クエリパラメータ: `filter` (String)
  - `all`: 全タスク
  - `active`: 未完了のみ
  - `completed`: 完了済みのみ

**処理内容**:
1. フィルターパラメータを受け取り
2. 条件に応じてクエリを実行
3. フィルター済みデータでHTMLを生成

**出力データ**:
- フィルター済みタスク一覧HTML（list.html）

**データストア操作**:
```sql
-- すべて
SELECT * FROM tasks ORDER BY created_at DESC;

-- 未完了のみ
SELECT * FROM tasks WHERE completed = false ORDER BY created_at DESC;

-- 完了済みのみ
SELECT * FROM tasks WHERE completed = true ORDER BY created_at DESC;
```

---

## 4. レイヤー別データフロー

Spring Bootの3層アーキテクチャにおけるデータフロー

```mermaid
graph TB
    User([ユーザー<br/>Webブラウザ])

    subgraph "Presentation Layer"
        Controller[TaskController<br/>HTTPリクエスト処理]
        View[Thymeleaf Template<br/>HTML生成]
    end

    subgraph "Business Logic Layer"
        Service[TaskService<br/>ビジネスロジック]
    end

    subgraph "Data Access Layer"
        Repository[TaskRepository<br/>JPA Repository]
        Entity[Task Entity<br/>ドメインモデル]
    end

    DB[(H2 Database<br/>tasks)]

    User -->|HTTPリクエスト| Controller
    Controller -->|呼び出し| Service
    Service -->|呼び出し| Repository
    Repository -->|SQL実行| DB
    DB -->|結果セット| Repository
    Repository -->|Entity| Service
    Service -->|Model| Controller
    Controller -->|Model| View
    View -->|HTMLレスポンス| User

    style User fill:#e1f5ff
    style Controller fill:#fff4e1
    style View fill:#fff4e1
    style Service fill:#e8f5e9
    style Repository fill:#f3e5f5
    style Entity fill:#f3e5f5
    style DB fill:#f0f0f0
```

### レイヤー間のデータ型

| レイヤー | 入力データ型 | 出力データ型 |
|---------|------------|------------|
| Controller → Service | TaskForm (DTO) | Task (Entity) |
| Service → Repository | Long (ID), Task (Entity) | Task, List&lt;Task&gt;, Optional&lt;Task&gt; |
| Repository → Database | SQL + パラメータ | ResultSet |
| Service → Controller | Task, List&lt;Task&gt; | Model (Map) |
| Controller → View | Model | - |
| View → User | - | HTML (String) |

---

## 5. 主要なデータ変換フロー

### 5.1 タスク作成時のデータ変換

```mermaid
graph LR
    A[HTTPリクエスト<br/>title=買い物]
    B[TaskForm<br/>title: String]
    C[Task Entity<br/>id: null<br/>title: 買い物<br/>completed: false]
    D[SQLパラメータ<br/>?, false, NOW, NOW]
    E[Database Row<br/>id: 1<br/>title: 買い物<br/>completed: 0]
    F[Task Entity<br/>id: 1<br/>title: 買い物<br/>completed: false]
    G[Model<br/>successMessage]
    H[HTMLレスポンス<br/>302 Redirect]

    A --> B
    B --> C
    C --> D
    D --> E
    E --> F
    F --> G
    G --> H

    style A fill:#e1f5ff
    style E fill:#f0f0f0
    style H fill:#e1f5ff
```

### 5.2 タスク取得時のデータ変換

```mermaid
graph LR
    A[HTTPリクエスト<br/>GET /tasks]
    B[SQLクエリ<br/>SELECT *]
    C[ResultSet<br/>複数行]
    D[List&lt;Task&gt;<br/>Entity集合]
    E[Model<br/>tasks: List]
    F[Thymeleaf処理<br/>th:each]
    G[HTMLレスポンス<br/>タスク一覧]

    A --> B
    B --> C
    C --> D
    D --> E
    E --> F
    F --> G

    style A fill:#e1f5ff
    style C fill:#f0f0f0
    style G fill:#e1f5ff
```

---

## 6. データフロー制御

### 6.1 正常系フロー

```mermaid
sequenceDiagram
    participant User as ユーザー
    participant Controller as TaskController
    participant Service as TaskService
    participant Repository as TaskRepository
    participant DB as H2 Database

    User->>Controller: POST /tasks (title)
    Controller->>Controller: バリデーション
    Controller->>Service: createTask(form)
    Service->>Service: new Task(title)
    Service->>Repository: save(task)
    Repository->>DB: INSERT INTO tasks...
    DB-->>Repository: 新規レコード
    Repository-->>Service: Task(id=1)
    Service-->>Controller: Task(id=1)
    Controller-->>User: 302 Redirect /tasks
    User->>Controller: GET /tasks
    Controller->>Service: getAllTasks()
    Service->>Repository: findAll()
    Repository->>DB: SELECT * FROM tasks
    DB-->>Repository: ResultSet
    Repository-->>Service: List<Task>
    Service-->>Controller: List<Task>
    Controller-->>User: 200 OK + HTML
```

### 6.2 異常系フロー（バリデーションエラー）

```mermaid
sequenceDiagram
    participant User as ユーザー
    participant Controller as TaskController
    participant Validator as Validator

    User->>Controller: POST /tasks (title="")
    Controller->>Validator: validate(form)
    Validator-->>Controller: BindingResult (hasErrors=true)
    Controller-->>User: 200 OK + HTML<br/>(エラーメッセージ付き)
```

### 6.3 異常系フロー（データ未存在）

```mermaid
sequenceDiagram
    participant User as ユーザー
    participant Controller as TaskController
    participant Service as TaskService
    participant Repository as TaskRepository
    participant DB as H2 Database

    User->>Controller: GET /tasks/999/edit
    Controller->>Service: getTaskById(999)
    Service->>Repository: findById(999)
    Repository->>DB: SELECT * WHERE id=999
    DB-->>Repository: Empty ResultSet
    Repository-->>Service: Optional.empty()
    Service-->>Controller: null / Exception
    Controller-->>User: 302 Redirect /tasks<br/>(エラーメッセージ付き)
```

---

## 7. データストアとの相互作用パターン

### 7.1 読み取りパターン

| 操作 | SQLクエリ | トランザクション | レスポンスタイム |
|-----|---------|--------------|----------------|
| 全件取得 | SELECT * | 不要（READ ONLY） | < 10ms |
| ID検索 | SELECT * WHERE id=? | 不要（READ ONLY） | < 1ms |
| 条件検索 | SELECT * WHERE completed=? | 不要（READ ONLY） | < 10ms |

### 7.2 書き込みパターン

| 操作 | SQLクエリ | トランザクション | ロールバック条件 |
|-----|---------|--------------|----------------|
| 作成 | INSERT INTO | 必要 | バリデーションエラー |
| 更新 | UPDATE SET WHERE | 必要 | データ未存在、バリデーションエラー |
| 削除 | DELETE WHERE | 必要 | データ未存在 |

### 7.3 トランザクション境界

```mermaid
graph TB
    Controller[Controller Layer<br/>@Transactional なし]
    Service[Service Layer<br/>@Transactional]
    Repository[Repository Layer<br/>トランザクション継承]

    Controller -->|呼び出し| Service
    Service -->|トランザクション開始| Repository
    Repository -->|SQL実行| DB[(Database)]
    DB -->|結果| Repository
    Repository -->|コミット/ロールバック| Service
    Service -->|結果| Controller

    style Service fill:#e8f5e9
```

---

## 8. データフロー図の凡例

### 記号の意味

| 記号 | 名称 | 説明 |
|-----|------|------|
| ○ または 丸角四角形 | 外部エンティティ | システム外部の人やシステム（ユーザー） |
| □ または 四角形 | プロセス | データを変換・処理する機能 |
| ⬜ または 開いた四角形 | データストア | データが保存される場所（データベース） |
| → | データフロー | データの流れと方向 |

### プロセス番号の体系

- **1.0〜**: 参照系処理（データ取得、表示）
- **2.0〜**: 作成系処理（新規データ登録）
- **3.0〜**: 更新系処理（既存データ変更）
- **4.0〜**: 状態変更系処理（フラグ切り替え）
- **5.0〜**: 削除系処理（データ削除）
- **6.0〜**: 検索・フィルタリング処理

---

## 9. まとめ

### データフローの特徴

1. **単純な一方向フロー**: ユーザー → システム → データベース → システム → ユーザー
2. **PRGパターン**: POST後は必ずGETにリダイレクト
3. **レイヤー分離**: プレゼンテーション層、ビジネスロジック層、データアクセス層が明確
4. **トランザクション管理**: サービス層で一貫性を保証
5. **バリデーション**: コントローラー層で入力検証

### データの整合性保証

- **一貫性**: トランザクション制御による
- **妥当性**: バリデーション機能による
- **一意性**: 主キー制約による
- **参照整合性**: 現在は単一テーブルのため不要（将来の拡張時に考慮）

### パフォーマンス考慮

- **キャッシング**: 現時点では不要（データ量が少ない）
- **インデックス**: completed、created_atにインデックス設定済み
- **ページング**: 将来的にデータ量増加時に検討
