---
description: TDD Verify - 静的解析とカバレッジの確認
---

# TDD Verify Phase

静的解析ツールとコードカバレッジで品質を検証します。

## 実行内容

1. **静的解析の実行**
   - Checkstyle: コードスタイルチェック
   - PMD: バグ検出とコード品質
   - SpotBugs: バグパターン検出

2. **カバレッジの確認**
   - JaCoCo: LINE カバレッジ 80%以上

3. **検証結果の確認**
   - すべてのツールでエラー・警告が0件
   - カバレッジ目標を達成

## 実行コマンド

### すべての検証を実行
```bash
mvn clean verify
```

このコマンドは以下を実行します：
1. `mvn clean` - ビルド成果物のクリア
2. `mvn test` - すべてのテスト実行
3. Checkstyle チェック
4. PMD チェック
5. SpotBugs チェック
6. JaCoCo カバレッジレポート生成とチェック

### 個別実行（必要に応じて）
```bash
# Checkstyle のみ
mvn checkstyle:check

# PMD のみ
mvn pmd:check

# SpotBugs のみ
mvn spotbugs:check

# テストとカバレッジのみ
mvn test jacoco:report
```

## 検証基準

### Checkstyle
- **設定ファイル**: `config/checkstyle/checkstyle.xml`
- **基準**: Google Java Style（一部カスタマイズ）
- **許容**: 0件のviolation

### PMD
- **設定ファイル**: `config/pmd/ruleset.xml`
- **基準**: カテゴリベースルール
- **許容**: 0件のviolation

### SpotBugs
- **設定ファイル**: `config/spotbugs/exclude.xml`
- **Effort**: Max
- **Threshold**: Low
- **許容**: 0件のバグ

### JaCoCo
- **カバレッジ目標**: LINE 80%以上
- **レポート**: `target/site/jacoco/index.html`

## エラー対応

### Checkstyle違反の修正例
```
[ERROR] Line is longer than 120 characters
→ 行を分割して120文字以内にする

[ERROR] Missing a Javadoc comment
→ publicメソッドにJavadocを追加

[ERROR] '{' should be on the previous line
→ 開き波括弧を前の行に移動
```

### PMD警告の修正例
```
[ERROR] Avoid using implementation types like 'ArrayList'; use the interface instead
→ List<Task> tasks = new ArrayList<>();

[ERROR] The class 'TaskService' has a Cyclomatic Complexity of 15
→ メソッドを分割して複雑度を下げる
```

### JaCoCo カバレッジ不足の対応
```
[ERROR] Rule violated for bundle: line covered ratio is 0.75, but expected minimum is 0.80
→ テストケースを追加してカバレッジを向上させる
```

## レポート確認

### JaCoCo HTMLレポート
```bash
# レポート生成
mvn verify

# ブラウザで確認（macOS）
open target/site/jacoco/index.html

# ブラウザで確認（Linux）
xdg-open target/site/jacoco/index.html
```

### レポートで確認する項目
- [ ] 全体のカバレッジが80%以上
- [ ] 各クラスのカバレッジを確認
- [ ] カバーされていないブランチを特定
- [ ] 必要に応じて追加テストを検討

## mise タスク

このプロジェクトでは mise タスクも使用できます：

```bash
# すべての検証
mise run verify

# 個別実行
mise run checkstyle
mise run pmd
mise run spotbugs
mise run test
```

## 指示

以下の手順でVerify Phaseを実行してください：

1. **検証の実行**
   ```bash
   mvn clean verify
   ```

2. **結果の確認**
   - コンソール出力でエラー・警告を確認
   - BUILD SUCCESS を確認

3. **カバレッジレポートの確認**
   - `target/site/jacoco/index.html` を開く
   - LINE カバレッジが80%以上か確認

4. **問題があれば修正**
   - Checkstyle/PMD/SpotBugs の違反を修正
   - カバレッジ不足ならテストを追加
   - 修正後に再度 `mvn clean verify` を実行

**期待される結果**:
- `mvn clean verify` が SUCCESS
- すべての静的解析ツールでエラー0件
- JaCoCo カバレッジ 80%以上

**次のステップ**: 検証が成功したら、変更をGitコミットしてください。`/tdd-cycle` を使用した場合は自動的にコミットされます。
