---
applyTo: '**'
---
## GospelForKids Android Project – Code Review Instructions

These instructions apply to all code contributions (AI and human) for this repository.

### 1. General Coding Standards
- Use modern Kotlin and Android best practices (see https://kotlinlang.org/docs/coding-conventions.html and official Android docs).
- Prefer Jetpack libraries and Compose for new UI code.
- Use MVVM or Clean Architecture for feature code.
- Ensure code is readable, maintainable, and well-documented.
- Avoid code duplication; refactor common logic into utilities or base classes.

### 2. Safety, Privacy, and Performance
- Never introduce code that could compromise user privacy or device security.
- Avoid excessive memory, CPU, or battery usage.
- Handle all exceptions and edge cases gracefully.
- Use background threads/coroutines for I/O and long-running operations.

### 3. Testing and Validation
- All new features and bug fixes must include appropriate unit and/or UI tests.
- Use MockK for mocking and JUnit for unit tests.
- Ensure all tests pass before merging.

### 4. Dependency Management
- Use Gradle for all dependencies; prefer stable releases over snapshots.
- Document any new dependencies in PRs.

### 5. Documentation and Comments
- Public functions/classes should have KDoc comments.
- Use inline comments to clarify complex logic, but avoid obvious comments.
- Update README or relevant docs for significant changes.

### 6. Pull Request (PR) Process
- PRs should be focused and address a single concern.
- Reference related issues in PR descriptions.
- Include a summary of changes and testing steps.
- Request review from at least one team member.

### 7. Domain Knowledge
- Follow project-specific conventions for naming, resource organization, and feature toggles.
- Ensure all user-facing strings are localized.
- Use project utility classes for file, network, and user content operations.

### 8. AI-Specific Guidance
- AI-generated code must be reviewed by a human before merging.
- AI should explain reasoning for non-trivial changes in PR descriptions.
- AI must not introduce code copied from proprietary or non-compliant sources.

---
Adhering to these guidelines ensures code quality, maintainability, and a positive experience for all contributors and users.

### 9. Reviewing Uncommitted Changes
- Always review any uncommitted local changes before submitting or merging code.
- Ensure that all staged and unstaged changes are intentional, relevant, and adhere to these guidelines.
- Use `git status` and `git --no-pager diff` (or other git commands that support `--no-pager`) to verify the current state of your working directory. Only add `--no-pager` to git commands that accept it as an argument.
- Remove or revert any accidental, debug, or unrelated changes prior to creating a pull request.