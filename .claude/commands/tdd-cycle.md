---
description: TDD Cycle - 完全なTDDサイクルを実行（Plan → Red → Green → Refactor → Verify → Commit）
---

# TDD Complete Cycle

TDD（Test-Driven Development）の完全なサイクルを実行します。

## サイクルの流れ

```
┌─────────────────────────────────────────────────────────┐
│                    TDD Cycle                            │
│                                                         │
│  1. Plan      → テスト計画                              │
│  2. Red       → 失敗するテストを書く                    │
│  3. Green     → テストを通す最小限の実装                │
│  4. Refactor  → コードの品質向上                        │
│  5. Verify    → 静的解析とカバレッジ確認                │
│  6. Commit    → Gitコミット                             │
│                                                         │
│  └─> 次の機能へ（繰り返し）                             │
└─────────────────────────────────────────────────────────┘
```

## 実行内容

このコマンドは、以下の6つのフェーズを順番に実行します：

### Phase 1: Plan（計画）
- 実装する機能を明確化
- テストケースを列挙
- テストファイルのパスを決定

**実行内容**: `/tdd-plan` の内容を実行

### Phase 2: Red（テスト失敗）
- 失敗するテストを作成
- テストを実行して失敗を確認

**実行内容**: `/tdd-red` の内容を実行

### Phase 3: Green（テスト成功）
- テストを通す最小限の実装
- テストを実行して成功を確認

**実行内容**: `/tdd-green` の内容を実行

### Phase 4: Refactor（リファクタリング）
- コードの品質向上
- テストが引き続きパスすることを確認

**実行内容**: `/tdd-refactor` の内容を実行

### Phase 5: Verify（検証）
- 静的解析ツール実行（Checkstyle, PMD, SpotBugs）
- コードカバレッジ確認（JaCoCo 80%以上）

**実行内容**: `/tdd-verify` の内容を実行
**コマンド**: `mvn clean verify`

### Phase 6: Commit（コミット）
- 変更をGitにコミット
- 適切なコミットメッセージを作成

## 指示

完全なTDDサイクルを以下の手順で実行してください：

---

## 📋 Phase 1: Plan（計画）

### 実施内容
1. 現在実装する機能を確認（GitHub Issue番号）
2. テストケースを列挙（最低3つ以上）
3. テストファイルのパスを決定
4. テストメソッド名を提案

### 確認事項
- [ ] 実装する機能が明確
- [ ] テストケースが列挙されている
- [ ] テストファイルパスが決定されている

---

## 🔴 Phase 2: Red（テスト失敗）

### 実施内容
1. テストファイルを作成
2. 失敗するテストを実装
3. `mvn test` を実行して失敗を確認

### 確認事項
- [ ] テストが作成されている
- [ ] テストが失敗する（コンパイルエラーではなくアサーション失敗）
- [ ] 適切なエラーメッセージが表示される

### テンプレート
```java
@Test
void testMethodName_Scenario_ExpectedResult() {
    // Given

    // When

    // Then
    fail("Not implemented yet");
}
```

---

## 🟢 Phase 3: Green（テスト成功）

### 実施内容
1. テストを通す最小限の実装を記述
2. `mvn test` を実行して成功を確認
3. すべてのテストがパスすることを確認

### 確認事項
- [ ] 実装コードが作成されている
- [ ] すべてのテストがパスする
- [ ] 最小限の実装（過度な設計を避ける）

---

## ♻️ Phase 4: Refactor（リファクタリング）

### 実施内容
1. コードの改善点を特定
2. リファクタリングを実施
3. `mvn test` でテストが引き続きパスすることを確認

### 確認事項
- [ ] コードの品質が向上している
- [ ] すべてのテストが引き続きパスする
- [ ] コンストラクターインジェクションを使用
- [ ] マジックナンバーを定数化
- [ ] メソッドが適切な長さ（20行以内）

---

## ✅ Phase 5: Verify（検証）

### 実施内容
1. `mvn clean verify` を実行
2. 静的解析の結果を確認
3. カバレッジレポートを確認

### 確認事項
- [ ] Checkstyle: 0件の違反
- [ ] PMD: 0件の警告
- [ ] SpotBugs: 0件のバグ
- [ ] JaCoCo: LINE カバレッジ 80%以上
- [ ] `BUILD SUCCESS` が表示される

### コマンド
```bash
mvn clean verify
```

---

## 💾 Phase 6: Commit（コミット）

### 実施内容
1. 変更ファイルを確認（`git status`）
2. ファイルをステージング（`git add`）
3. コミットメッセージを作成
4. コミット実行

### コミットメッセージのフォーマット
```
<type>: <subject>

<body>

🤖 Generated with [Claude Code](https://claude.com/claude-code)

Co-Authored-By: Claude <noreply@anthropic.com>
```

### Type
- `feat`: 新機能
- `test`: テスト追加・修正
- `refactor`: リファクタリング
- `fix`: バグ修正
- `docs`: ドキュメント

### 例
```bash
git add src/main/java/com/example/demo/service/TaskService.java
git add src/test/java/com/example/demo/service/TaskServiceTest.java
git commit -m "$(cat <<'EOF'
feat: Implement createTask method in TaskService

- Add TaskService.createTask() method
- Add unit tests with Mockito
- Test coverage: 85%
- All static analysis checks passed

Resolves: #2

🤖 Generated with [Claude Code](https://claude.com/claude-code)

Co-Authored-By: Claude <noreply@anthropic.com>
EOF
)"
```

### 確認事項
- [ ] 適切なファイルがステージングされている
- [ ] コミットメッセージが明確
- [ ] Issue番号が記載されている（該当する場合）

---

## 完了条件

すべてのフェーズが完了し、以下の条件を満たしていること：

✅ **テスト**
- すべてのテストがパスする
- カバレッジ 80%以上

✅ **品質**
- Checkstyle, PMD, SpotBugs で違反0件
- コードがリファクタリングされている

✅ **Git**
- 変更がコミットされている
- 適切なコミットメッセージ

---

## 次のステップ

1サイクルが完了したら、次の機能に進みます：

1. 次のGitHub Issueを確認
2. 再度 `/tdd-cycle` を実行
3. TDDサイクルを繰り返す

---

## トラブルシューティング

### テストが失敗し続ける
- テストコードを見直す
- 実装コードを見直す
- デバッグ実行で原因を特定

### 静的解析で違反が出る
- `/tdd-verify` の「エラー対応」セクションを参照
- 違反を修正して再度 `mvn clean verify`

### カバレッジが足りない
- カバーされていないブランチを特定
- 追加のテストケースを作成
- エッジケースのテストを追加

---

**このコマンドを実行することで、高品質なコードをTDDサイクルで開発できます！**
