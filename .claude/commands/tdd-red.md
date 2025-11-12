---
description: TDD Red - 失敗するテストを書く
---

# TDD Red Phase

テストファーストで、失敗するテストを書きます。

## 実行内容

1. **テストコードの作成**
   - `/tdd-plan` で計画したテストケースを実装
   - まだ実装されていない機能をテストする
   - テストは必ず失敗する状態にする

2. **テストの実行**
   - `mvn test` を実行してテストが失敗することを確認
   - 適切なエラーメッセージが表示されることを確認

3. **失敗の確認**
   - 期待通りの理由で失敗しているか確認
   - コンパイルエラーではなく、アサーション失敗であることを確認

## プロジェクト固有のテンプレート

### サービス層のテスト例
```java
@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    void createTask_ValidTitle_ReturnsCreatedTask() {
        // Given
        String title = "新しいタスク";
        Task expectedTask = new Task();
        expectedTask.setId(1L);
        expectedTask.setTitle(title);
        expectedTask.setCompleted(false);

        when(taskRepository.save(any(Task.class))).thenReturn(expectedTask);

        // When
        Task actualTask = taskService.createTask(title);

        // Then
        assertThat(actualTask).isNotNull();
        assertThat(actualTask.getTitle()).isEqualTo(title);
        assertThat(actualTask.getCompleted()).isFalse();
    }
}
```

### コントローラー層のテスト例（MockMVC）
```java
@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Test
    void listTasks_ReturnsTaskListView() throws Exception {
        // Given
        List<Task> tasks = Arrays.asList(new Task(1L, "タスク1", false));
        when(taskService.getAllTasks()).thenReturn(tasks);

        // When & Then
        mockMvc.perform(get("/tasks"))
               .andExpect(status().isOk())
               .andExpect(view().name("tasks/list"))
               .andExpect(model().attributeExists("tasks"));
    }
}
```

## 指示

以下の手順でRed Phaseを実行してください：

1. **テストファイルを作成または開く**
   - 計画したテストファイルパスにファイルを作成
   - 必要なインポート文を追加

2. **テストメソッドを実装**
   - `@Test` アノテーションを付与
   - Given-When-Then パターンで記述
   - AssertJを使用してアサーション

3. **テストを実行**
   - `mvn test -Dtest=テストクラス名` で個別実行
   - または `mvn test` で全テスト実行

4. **失敗を確認**
   - テストが失敗することを確認
   - エラーメッセージが適切か確認
   - 次のステップに進む準備

**期待される結果**: テストは失敗する（Red状態）

**次のステップ**: テストが失敗したら `/tdd-green` を実行してテストを通す実装を行ってください。
