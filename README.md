# To Do管理アプリケーション

基本的なタスク管理機能を提供するWebアプリケーション（MVP版）

## 目次

- [概要](#概要)
- [機能](#機能)
- [技術スタック](#技術スタック)
- [前提条件](#前提条件)
- [セットアップ](#セットアップ)
- [実行方法](#実行方法)
- [使い方](#使い方)
- [開発](#開発)
- [テスト](#テスト)
- [静的解析](#静的解析)
- [プロジェクト構成](#プロジェクト構成)
- [ドキュメント](#ドキュメント)
- [ライセンス](#ライセンス)

## 概要

**Simple Todo** は、個人ユーザー向けのシンプルなタスク管理Webアプリケーションです。Spring Boot 3.4とThymeleafを使用し、基本的なCRUD操作とタスクのフィルタリング機能を提供します。

### 主な特徴

- ✅ シンプルで直感的なUI（Bootstrap 5.3使用）
- ✅ タスクの作成、編集、削除、完了切り替え
- ✅ タスクのフィルタリング（すべて/未完了/完了済み）
- ✅ データの永続化（H2 Database）
- ✅ CSRF保護（Spring Security）
- ✅ バリデーション機能
- ✅ レスポンシブデザイン

## 機能

### 実装済み機能（Must Have）

| 機能 | 説明 | ユーザーストーリー |
|-----|------|------------------|
| タスク作成 | 新しいタスクをタイトルのみで作成 | US-001 |
| タスク完了切り替え | チェックボックスでタスクの完了/未完了を切り替え | US-002 |
| タスク一覧表示 | すべてのタスクを作成日時の降順で表示 | US-003 |
| タスク削除 | 不要なタスクを削除（確認ダイアログあり） | US-004 |
| タスク編集 | タスクのタイトルを編集 | US-005 |
| タスクフィルタリング | すべて/未完了/完了済みでフィルタリング | US-006 |

### 将来の拡張案（Should Have/Could Have）

- タスクの期限日設定
- 優先度の設定
- カテゴリ分類
- 詳細説明の追加
- タスクの並び替え
- 検索機能

## 技術スタック

### バックエンド

| 技術 | バージョン | 用途 |
|-----|----------|-----|
| Java | 21 (LTS) | プログラミング言語 |
| Spring Boot | 3.4.0 | アプリケーションフレームワーク |
| Spring Web | 3.4.0 | RESTコントローラー |
| Spring Data JPA | 3.4.0 | データアクセス |
| Spring Security | 3.4.0 | CSRF保護 |
| Thymeleaf | 3.1.x | テンプレートエンジン |
| H2 Database | 2.x | 組み込みデータベース |
| Hibernate Validator | 8.x | バリデーション |
| Lombok | 1.18.x | ボイラープレートコード削減 |
| Maven | 3.9.x | ビルドツール |

### フロントエンド

| 技術 | バージョン | 導入方法 |
|-----|----------|---------|
| Bootstrap | 5.3.2 | CDN |
| Font Awesome | 6.5.1 | CDN |
| JavaScript | Vanilla JS | インライン（最小限） |

### 静的解析ツール

| ツール | バージョン | 用途 |
|-------|----------|-----|
| Checkstyle | 10.20.2 | コードスタイルチェック |
| PMD | 7.8.0 | バグ検出・コード品質 |
| SpotBugs | 4.8.6 | バグパターン検出 |
| JaCoCo | 0.8.12 | コードカバレッジ（80%以上） |

## 前提条件

以下のツールがインストールされている必要があります：

- **Java 21** (LTS)
- **Maven 3.9+**
- **Git**

### 推奨：mise を使用したツール管理

このプロジェクトでは [mise](https://mise.jdx.dev/) を使用したツールバージョン管理を推奨しています。

```bash
# mise のインストール（macOS/Linux）
curl https://mise.run | sh

# プロジェクトで使用するツールをインストール
mise install
```

## セットアップ

### 1. リポジトリのクローン

```bash
git clone https://github.com/cuzic/todo-demo-spring-boot.git
cd todo-demo-spring-boot
```

### 2. 依存関係のインストール

```bash
mvn clean install
```

### 3. データベースの準備

データベースは自動的に作成されます（H2ファイルベース）。初期データも自動投入されます。

- **データベースファイル**: `./data/tododb.mv.db`
- **初期データ**: `src/main/resources/data.sql`

## 実行方法

### Maven を使用

```bash
# 開発モードで実行
mvn spring-boot:run

# または mise タスクを使用
mise run run
```

### JARファイルを使用

```bash
# ビルド
mvn clean package

# 実行
java -jar target/todo-demo-spring-boot-0.0.1-SNAPSHOT.jar
```

### アクセス

- **アプリケーション**: http://localhost:8080
- **H2 Console**: http://localhost:8080/h2-console

#### H2 Console 接続情報

- **JDBC URL**: `jdbc:h2:file:./data/tododb`
- **User Name**: `sa`
- **Password**: （空欄）

## 使い方

### タスクの作成

1. トップページの入力フィールドにタスクタイトルを入力
2. 「追加」ボタンをクリック

### タスクの完了切り替え

- 各タスクのチェックボックスをクリック

### タスクの編集

1. 各タスクの「編集」ボタンをクリック
2. タイトルを変更
3. 「保存」ボタンをクリック

### タスクの削除

1. 各タスクの「削除」ボタンをクリック
2. 確認ダイアログで「OK」をクリック

### タスクのフィルタリング

- **すべて**: すべてのタスクを表示
- **未完了**: 未完了のタスクのみ表示
- **完了済み**: 完了済みのタスクのみ表示

## 開発

### mise タスク一覧

```bash
# アプリケーション実行
mise run run

# ビルド
mise run build

# テスト実行
mise run test

# 静的解析実行
mise run verify

# Checkstyle実行
mise run checkstyle

# PMD実行
mise run pmd

# SpotBugs実行
mise run spotbugs

# クリーン
mise run clean
```

### 開発環境設定

```properties
# src/main/resources/application.properties

# Thymeleafキャッシュ無効化（開発時）
spring.thymeleaf.cache=false

# SQLログ出力
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# H2 Console有効化
spring.h2.console.enabled=true
```

## テスト

### 単体テスト実行

```bash
mvn test

# または
mise run test
```

### カバレッジレポート生成

```bash
mvn verify

# レポート確認
open target/site/jacoco/index.html
```

### カバレッジ目標

- **最小カバレッジ**: 80%（LINE）
- **達成できない場合**: ビルドが失敗します

## 静的解析

### すべての静的解析ツールを実行

```bash
mvn clean verify

# または
mise run verify
```

### 個別実行

```bash
# Checkstyle
mvn checkstyle:check
mise run checkstyle

# PMD
mvn pmd:check
mise run pmd

# SpotBugs
mvn spotbugs:check
mise run spotbugs
```

### 静的解析レポート生成

```bash
mvn site

# レポート確認
open target/site/index.html
```

## プロジェクト構成

```
todo-demo-spring-boot/
├── config/                          # 静的解析ツール設定
│   ├── checkstyle/
│   │   └── checkstyle.xml          # Checkstyle設定
│   ├── pmd/
│   │   └── ruleset.xml             # PMD設定
│   └── spotbugs/
│       └── exclude.xml             # SpotBugs除外設定
├── docs/                            # ドキュメント
│   ├── user-stories.md             # ユーザーストーリー
│   ├── screen-design.md            # 画面設計
│   ├── database-design.md          # データベース設計
│   ├── dataflow-diagram.md         # データフロー図
│   ├── acceptance-criteria.md      # 受入基準チェックリスト
│   └── final-specification.md      # 最終設計仕様書
├── src/
│   ├── main/
│   │   ├── java/com/example/demo/
│   │   │   ├── controller/         # コントローラー層
│   │   │   ├── service/            # サービス層
│   │   │   ├── repository/         # リポジトリ層
│   │   │   ├── entity/             # エンティティ
│   │   │   ├── dto/                # DTO（Data Transfer Object）
│   │   │   └── TodoDemoApplication.java  # メインクラス
│   │   └── resources/
│   │       ├── templates/          # Thymeleafテンプレート
│   │       │   ├── layout/         # 共通レイアウト
│   │       │   ├── tasks/          # タスク関連画面
│   │       │   └── error/          # エラーページ
│   │       ├── static/             # 静的リソース
│   │       │   ├── css/            # カスタムCSS
│   │       │   └── js/             # カスタムJS
│   │       ├── data.sql            # 初期データ
│   │       └── application.properties  # アプリケーション設定
│   └── test/                       # テストコード
├── .gitignore                      # Git除外設定
├── .mise.toml                      # mise設定
├── pom.xml                         # Maven設定
├── README.md                       # このファイル
└── CLAUDE.md                       # AI開発ドキュメント
```

## ドキュメント

プロジェクトの詳細なドキュメントは `docs/` ディレクトリにあります：

- **[ユーザーストーリー](docs/user-stories.md)** - 6つの主要機能の要件定義
- **[画面設計](docs/screen-design.md)** - UI/UX設計とワイヤーフレーム
- **[データベース設計](docs/database-design.md)** - ER図とテーブル定義
- **[データフロー図](docs/dataflow-diagram.md)** - データの流れと処理フロー
- **[受入基準チェックリスト](docs/acceptance-criteria.md)** - テスト観点と検証項目
- **[最終設計仕様書](docs/final-specification.md)** - 完全な技術仕様

## ライセンス

このプロジェクトはMITライセンスの下で公開されています。

---

**開発者**: cuzic
**プロジェクト開始日**: 2024-11-12
**Spring Boot バージョン**: 3.4.0
**Java バージョン**: 21 (LTS)
