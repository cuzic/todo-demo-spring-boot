---
description: TDD Green - テストを通す最小限の実装
---

# TDD Green Phase

テストを通すための最小限の実装を行います。

## 実行内容

1. **実装コードの作成**
   - テストを通すための最小限のコードを書く
   - まずは最もシンプルな実装から始める
   - 過度な設計は避ける（YAGNIの原則）

2. **テストの実行**
   - `mvn test` を実行してテストが成功することを確認
   - すべての既存テストも引き続きパスすることを確認

3. **成功の確認**
   - Green状態になったことを確認
   - カバレッジが向上したことを確認

## プロジェクト固有の実装パターン

### サービス層の実装例
```java
@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    /**
     * タスクを作成します。
     *
     * @param title タスクのタイトル
     * @return 作成されたタスク
     */
    public Task createTask(String title) {
        Task task = new Task();
        task.setTitle(title);
        task.setCompleted(false);
        return taskRepository.save(task);
    }

    /**
     * すべてのタスクを作成日時の降順で取得します。
     *
     * @return タスクのリスト
     */
    public List<Task> getAllTasks() {
        return taskRepository.findAllByOrderByCreatedAtDesc();
    }
}
```

### コントローラー層の実装例
```java
@Controller
public class TaskController {

    @Autowired
    private TaskService taskService;

    /**
     * タスク一覧を表示します。
     */
    @GetMapping({"/", "/tasks"})
    public String listTasks(
            @RequestParam(required = false) String filter,
            Model model) {
        List<Task> tasks = taskService.getAllTasks();
        model.addAttribute("tasks", tasks);
        model.addAttribute("filter", filter);
        model.addAttribute("taskForm", new TaskForm());
        return "tasks/list";
    }

    /**
     * タスクを作成します（PRGパターン）。
     */
    @PostMapping("/tasks")
    public String createTask(
            @Valid @ModelAttribute TaskForm taskForm,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("tasks", taskService.getAllTasks());
            model.addAttribute("filter", null);
            return "tasks/list";
        }
        taskService.createTask(taskForm.getTitle());
        redirectAttributes.addFlashAttribute("successMessage", "タスクを作成しました");
        return "redirect:/tasks";
    }
}
```

## 指示

以下の手順でGreen Phaseを実行してください：

1. **実装ファイルを作成または開く**
   - Service、Controller、Repositoryなど
   - パッケージ構造: `com.example.demo.{layer}/`

2. **最小限の実装を記述**
   - テストを通すために必要な最小限のコード
   - Spring Bootのアノテーション（@Service, @Controller, @Autowired）
   - PRGパターンの適用（コントローラー）
   - バリデーションの実装

3. **テストを実行**
   - `mvn test -Dtest=テストクラス名` で個別確認
   - `mvn test` で全テスト実行

4. **Green状態の確認**
   - すべてのテストがパスすることを確認
   - コンソール出力でグリーンバーを確認

**期待される結果**: すべてのテストが成功する（Green状態）

**次のステップ**: テストが成功したら `/tdd-refactor` を実行してコードをリファクタリングしてください。
