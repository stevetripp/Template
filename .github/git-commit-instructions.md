
# Git Commit Instructions for FIR-Android

## Commit Message Format

All commit messages **must** follow this format:

```
[PR-name or branch-name]: <short summary>

[Optional longer description]
```

- **PR-name**: If the commit is associated with a pull request, use the PR name as the first item in the subject line, followed by a colon and a space.
- **branch-name**: If there is no PR, use the branch name (e.g., `feature/offline-support`, `bugfix/login-crash`).
- If neither PR nor branch name is available, omit both and start with the short summary.
- **short summary**: Brief, imperative description of the change (max 72 chars, no period).
- **longer description**: (Optional) Explain what and why, not how. Wrap at 72 chars.


## Best Practices

- **One logical change per commit** (atomic commits)
- **Explain the _what_ and _why_** in the message
- **Reference related issues/PRs** in the footer if needed
- **Use present tense** (e.g., "Add feature" not "Added feature")
- **Keep subject line concise** (max 72 chars)
- **Wrap body at 72 chars**
- **No WIP or temporary commits** in main branches


## Examples

**Good:**
```
feature/offline-support: add offline support to work order list

Implements Room DB caching for work orders. Users can now view and edit
work orders without a network connection. Sync logic added to ViewModel.
```

**Good (with PR name):**
```
FIRA-862: fix crash on login screen

Fixes a null pointer exception when logging in with SSO. Adds null checks
to the authentication flow.
```

**Good (no PR or branch):**
```
Update documentation for build process

Clarifies the steps for running the build locally and updating dependencies.
```

**Bad:**
```
feat(database): add ChangeRequestCategory entity and DAO
```

**Bad:**
```
add offline support
```

**Bad:**
```
feature/offline-support add offline support
```

---

- Always include the PR name as the first item in the subject line for every commit associated with a pull request. If there is no PR, use the branch name. If neither is available, omit both and start with the summary.


## Project-Specific Notes

- **Dependency updates:** Follow `.github/instructions/updated-dependency-instructions.md`.
- **Code review:** See `.github/instructions/code-review-instructions.md`.
- **Follow Kotlin/Android best practices** as described in the README.
- **Use Hilt for DI** and Compose for UI where possible.

## Additional Resources
- [Kotlin Commit Message Guidelines](https://kotlinlang.org/docs/community.html#commit-messages)
