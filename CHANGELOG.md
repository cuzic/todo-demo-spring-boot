# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.0.0] - 2025-11-12

### Added

#### Core Features (US-001 ~ US-006)
- **タスク作成機能**（US-001）
  - タイトル入力によるタスク作成
  - バリデーション（必須、最大255文字）
  - PRGパターンによる二重送信防止

- **タスク完了切り替え機能**（US-002）
  - チェックボックスによる完了/未完了の切り替え
  - 完了タスクの視覚的区別（打ち消し線 + グレーアウト）

- **タスク一覧表示機能**（US-003）
  - すべてのタスクをリスト表示
  - 作成日時降順でソート
  - 空状態メッセージ表示

- **タスク削除機能**（US-004）
  - 削除ボタンによるタスク削除
  - 確認ダイアログ表示
  - PRGパターンによる二重削除防止

- **タスク編集機能**（US-005）
  - 専用編集画面への遷移
  - タイトル変更機能
  - キャンセル機能

- **タスクフィルタリング機能**（US-006）
  - 未完了タスクの絞り込み（`?filter=active`）
  - 完了済みタスクの絞り込み（`?filter=completed`）
  - すべてのタスク表示（デフォルト）
  - 作成日時降順ソート

#### Technical Stack
- **Backend Framework**: Spring Boot 3.4.0
- **Java Version**: Java 21 (LTS)
- **Database**: H2 Database (file-based)
- **Template Engine**: Thymeleaf
- **CSS Framework**: Bootstrap 5.3.2
- **Icons**: Font Awesome 6.4.0
- **Build Tool**: Maven 3.9.x
- **Version Manager**: mise

#### Security
- **Authentication**: Form-based login with Spring Security
  - Demo user: username `demo` / password `demo123`
- **CSRF Protection**: Enabled for all POST requests
- **XSS Protection**: Thymeleaf automatic escaping
- **SQL Injection Protection**: JPA PreparedStatement binding

#### Code Quality
- **Test Coverage**: 93% instruction coverage (JaCoCo)
- **Branch Coverage**: 80%
- **Total Tests**: 48 tests (100% passing)
- **Static Analysis**:
  - Checkstyle: 0 violations
  - PMD: 0 violations
  - SpotBugs: 0 bugs found

#### Testing
- **Unit Tests**: Service layer (13 tests)
- **Integration Tests**:
  - Controller layer (20 tests)
  - Repository layer (6 tests)
  - Entity layer (8 tests)
- **Application Test**: Spring Boot context loading (1 test)

#### Documentation
- Comprehensive user stories (US-001 ~ US-006)
- Database design with ER diagrams
- Screen design specifications
- Data flow diagrams
- Acceptance criteria checklist
- Final specification document
- Verification report (Phase 8)
- Development documentation (CLAUDE.md)
- MoSCoW priority analysis

### Infrastructure
- **Environment Management**: mise for Java and Maven
- **Development Tools**: TDD custom slash commands
- **Project Management**: GitHub Issues-based development workflow
- **Git Strategy**: Feature-based commits with detailed messages

### Architecture
- **Pattern**: Layered architecture
  - Presentation Layer: Thymeleaf + Bootstrap
  - Business Logic Layer: Service classes
  - Data Access Layer: Spring Data JPA
  - Database Layer: H2
- **Design Pattern**: PRG (Post-Redirect-Get) pattern
- **Validation**: Jakarta Bean Validation

## [Unreleased]

### Planned (Should Have)
- フィルタータブUI実装（現在はURLパラメータのみ）
- レスポンシブデザイン最適化（モバイル、タブレット）
- Safari, Edge ブラウザ対応確認
- 詳細なJavadocコメント（全メソッド）
- アクセシビリティ向上（WCAG準拠）

### Under Consideration (Could Have)
- タスクの期限日設定
- タスクの優先度設定
- タスクのカテゴリ分類
- タスクの並び替え（ドラッグ&ドロップ）
- ダークモード対応
- E2Eテスト（Selenium）
- PostgreSQL対応
- RESTful API提供

### Won't Implement (Won't Have this time)
- 多言語対応（i18n）
- カスタムテーマ機能
- クロスブラウザ自動テスト
- ビジュアルリグレッションテスト

---

## Version History

- **1.0.0** (2025-11-12): Initial release with core task management features
  - 6 user stories implemented (US-001 ~ US-006)
  - 93% test coverage achieved
  - All static analysis checks passed
  - Production-ready MVP

---

**Development Period**: 2024-11-12 ~ 2024-11-13
**Development Style**: AI-Assisted Development with Claude Code
**Repository**: https://github.com/cuzic/todo-demo-spring-boot

🤖 Generated with [Claude Code](https://claude.com/claude-code)
