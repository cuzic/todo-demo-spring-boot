---
description: TDD Plan - テスト計画を立てる
---

# TDD Plan Phase

現在実装する機能のテスト計画を立てます。

## 実行内容

1. **現在のコンテキストを確認**
   - 現在取り組んでいるGitHub Issue番号を確認
   - 実装する機能/メソッドを特定
   - 対応するUser Storyを確認

2. **テスト計画の策定**
   - どのようなテストケースが必要か列挙
   - 正常系と異常系のシナリオを考える
   - テストファイルのパスを決定

3. **テストの種類を決定**
   - ユニットテスト（Service層）
   - 統合テスト（Controller層、MockMVC）
   - リポジトリテスト（Repository層）

## プロジェクト固有の情報

### テストファイルの配置
```
src/test/java/com/example/demo/
├── controller/
│   └── TaskControllerTest.java
├── service/
│   └── TaskServiceTest.java
└── repository/
    └── TaskRepositoryTest.java
```

### 使用するテストフレームワーク
- JUnit 5 (Jupiter)
- Mockito (モック化)
- MockMvc (コントローラーテスト)
- AssertJ (アサーション)
- Spring Boot Test

### カバレッジ目標
- LINE カバレッジ: 80%以上（JaCoCo）

## 指示

以下の手順でテスト計画を立ててください：

1. 現在実装する機能を明確にする（Issue番号、機能名）
2. テストケースを列挙する（最低3つ以上）
3. テストファイルのパスを提示する
4. 各テストメソッド名を提案する（命名規則: `testMethodName_Scenario_ExpectedResult`）
5. 必要なモックオブジェクトを列挙する

**次のステップ**: テスト計画が完了したら `/tdd-red` を実行して失敗するテストを作成してください。
