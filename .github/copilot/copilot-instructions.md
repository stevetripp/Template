# GitHub Copilot Instructions

## Code Generation Guidelines

### Navigation Components
When generating code for navigation:
- Use the existing `NavTypeMaps` pattern for type-safe navigation
- Each navigation parameter should have its own NavType implementation
- For complex parameters, use Kotlin serialization
- Follow the pattern:
  ```kotlin
  val ParameterNavType = object : NavType<Parameter>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): Parameter? = bundle.getString(key)?.let { parseValue(it) }
    override fun parseValue(value: String): Parameter = Parameter(value)
    override fun put(bundle: Bundle, key: String, value: Parameter) = bundle.putString(key, serializeAsValue(value))
    override fun serializeAsValue(value: Parameter): String = value.value
  }
  ```
- Remember to add new types to the typeMap with both nullable and non-nullable variants as needed

### Compose UI
- Use Material 3 components and themes
- Follow responsive design principles
- Use WindowSizeClass for adaptive layouts
- Prefer stateless composable functions with state hoisting
- Use remember and derivedStateOf appropriately

### MVVM Architecture
- Use ViewModels to manage UI state and business logic
- Use repositories for data operations
- Use use cases/interactors for complex business logic
- Expose states using StateFlow
- Use events for one-time actions

### API Requests
- Use the ApiResponse pattern for handling API responses
- Handle errors and loading states properly
- Use proper error handling with the existing utility functions

### Dependency Injection
- Use Hilt for dependency injection
- Create appropriate modules for different features
- Use the @Inject annotation for constructor injection where possible
- Use @Provides for complex dependencies

### Testing
- Write unit tests for ViewModels and use cases
- Write UI tests for critical user flows
- Mock dependencies appropriately

### Kotlin Idioms
- Use extension functions for adding functionality to existing classes
- Use sealed classes for representing different states
- Use data classes for model objects
- Use lambdas and higher-order functions appropriately
- Use scope functions (let, apply, run, with, also) appropriately
