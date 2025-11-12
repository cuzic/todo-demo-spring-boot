---
description: TDD Refactor - コードの品質を向上させる
---

# TDD Refactor Phase

テストを維持しながらコードの品質を向上させます。

## 実行内容

1. **リファクタリング候補の特定**
   - コードの重複を探す
   - メソッドが長すぎないか確認
   - 変数名やメソッド名が適切か確認
   - デザインパターンの適用可能性を検討

2. **リファクタリングの実施**
   - コードを改善しながらテストが引き続きパスすることを確認
   - 小さなステップで段階的にリファクタリング
   - 各ステップでテストを実行

3. **テストの実行**
   - リファクタリング後に `mvn test` を実行
   - すべてのテストがパスすることを確認

## リファクタリングのチェックリスト

### コードの品質
- [ ] メソッドは単一責任を持っているか
- [ ] メソッドの長さは適切か（20行以内が目安）
- [ ] 変数名やメソッド名は意図を表現しているか
- [ ] マジックナンバーは定数化されているか
- [ ] コメントは必要最小限か（コードで説明できることはコメント不要）

### Spring Boot ベストプラクティス
- [ ] フィールドインジェクションではなくコンストラクターインジェクションを使用
- [ ] `@Autowired` は必要な場所のみ使用（コンストラクターインジェクションなら不要）
- [ ] 適切なスコープのアノテーション（@Service, @Controller, @Repository）
- [ ] トランザクション境界が適切か（@Transactional）

### エラーハンドリング
- [ ] 適切な例外処理がされているか
- [ ] カスタム例外クラスの必要性を検討
- [ ] エラーメッセージは分かりやすいか

## リファクタリング例

### Before（フィールドインジェクション）
```java
@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;
}
```

### After（コンストラクターインジェクション）
```java
@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }
}
```

### Before（マジックナンバー）
```java
if (title.length() > 255) {
    throw new IllegalArgumentException("タイトルが長すぎます");
}
```

### After（定数化）
```java
private static final int MAX_TITLE_LENGTH = 255;

if (title.length() > MAX_TITLE_LENGTH) {
    throw new IllegalArgumentException(
        "タイトルは" + MAX_TITLE_LENGTH + "文字以内で入力してください");
}
```

### Before（長いメソッド）
```java
public String createTask(TaskForm form, BindingResult result, Model model) {
    if (result.hasErrors()) {
        model.addAttribute("tasks", taskRepository.findAll());
        return "tasks/list";
    }
    Task task = new Task();
    task.setTitle(form.getTitle());
    task.setCompleted(false);
    taskRepository.save(task);
    return "redirect:/tasks";
}
```

### After（メソッド分割）
```java
public String createTask(TaskForm form, BindingResult result, Model model,
                         RedirectAttributes redirectAttributes) {
    if (result.hasErrors()) {
        return handleValidationError(model);
    }
    taskService.createTask(form.getTitle());
    redirectAttributes.addFlashAttribute("successMessage", "タスクを作成しました");
    return "redirect:/tasks";
}

private String handleValidationError(Model model) {
    model.addAttribute("tasks", taskService.getAllTasks());
    model.addAttribute("filter", null);
    return "tasks/list";
}
```

## 指示

以下の手順でRefactor Phaseを実行してください：

1. **リファクタリング候補を特定**
   - コードレビューを実施
   - 改善点をリストアップ

2. **小さなステップでリファクタリング**
   - 一度に一つの改善を実施
   - 各ステップでテストを実行

3. **テストの実行**
   - `mvn test` で全テスト実行
   - すべてのテストがパスすることを確認

4. **コードレビュー**
   - リファクタリング後のコードが改善されているか確認
   - 可読性、保守性が向上しているか確認

**期待される結果**: テストは引き続きパスし、コードの品質が向上している

**次のステップ**: リファクタリングが完了したら `/tdd-verify` を実行して静的解析とカバレッジを確認してください。
