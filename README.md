# Cookies-KMP

This repository is an **undergraduate thesis (TCC) academic project** whose goal is to design and validate a **Kotlin Multiplatform (KMP) library** based on the **Cookies module** of the DuckDuckGo mobile applications.

> ⚠️ Academic Disclaimer  
> This project is **strictly academic** and **not affiliated, endorsed, or maintained by DuckDuckGo**.  
> All analysis, experiments, and code adaptations are conducted **for research and educational purposes only**, using publicly available open-source repositories.

---

## Project Focus

The scope of this project is intentionally **narrow and well-defined**:

- Build a **KMP library** that encapsulates **cookie-related logic**
- Enable reuse of this shared logic across:
  - DuckDuckGo Android
  - DuckDuckGo iOS (Apple Browsers)
- Evaluate the feasibility, constraints, and benefits of adopting KMP **as a shared library**, not as a full app rewrite

---

## Objectives

1. **Set up and run the target projects**
   - DuckDuckGo Android
   - DuckDuckGo iOS/macOS
2. **Analyze the Cookies module**
   - Understand responsibilities, boundaries, and dependencies
   - Identify what can be safely shared across platforms
3. **Implement a KMP Cookies library**
   - Shared business logic in `commonMain`
   - Platform-specific implementations via `expect/actual` where needed
4. **Validate iOS integration**
   - Confirm that the iOS platform can consume the KMP library through a published artifact
5. **Document results**
   - Technical decisions
   - Limitations
   - Integration experience

---

## Research Hypotheses

- The **Cookies module** is a strong candidate for Kotlin Multiplatform because:
  - It contains deterministic business rules
  - It has clear boundaries and well-defined responsibilities
  - Its logic is largely independent of UI concerns
- Using KMP **as a library** is more suitable for large, modular apps than attempting full cross-platform migration.
- Cookie handling logic can be shared while preserving:
  - Platform-specific storage mechanisms
  - Platform-specific security constraints

---

## Technical Scope

### In scope
- Cookie models and domain logic
- Cookie policies and rules
- Shared abstractions for reading/writing cookies
- Platform-specific adapters (Android / iOS)

### Out of scope
- UI layers
- Browser rendering engines
- Full application refactoring
- Performance optimization beyond academic validation

---

## Target Repositories (Reference Implementations)

- **Android:** DuckDuckGo Android  
  https://github.com/duckduckgo/android

- **iOS/macOS:** DuckDuckGo Apple Browsers  
  https://github.com/duckduckgo/apple-browsers

These repositories are used **only as reference and integration targets**.

---

## Planned Architecture

```text
Cookies-KMP
│
├── shared/
│   ├── commonMain/        # Shared cookie domain logic
│   ├── androidMain/       # Android-specific implementations
│   └── iosMain/           # iOS-specific implementations
│
├── docs/
│   ├── decisions.md       # Architectural and research decisions
│   ├── limitations.md    # Technical and platform constraints
│   └── integration.md    # How the library is consumed

```

---

## Validation Criteria

- The shared KMP library can be built successfully
- Android can import and use the Cookies KMP module
- iOS can import and use the same module via a published artifact
- Cookie-related logic behaves consistently across platforms

---

## Project Status

🚧 **Work in progress**

This repository will evolve alongside the thesis, including documentation, experiments, and integration notes.

---

## License & Usage

This repository exists for **academic research purposes only**.

- DuckDuckGo source code remains under its original licenses
- This project does not redistribute or modify proprietary components
- All usage complies with the open-source licensing terms of the upstream repositories
>>>>>>> 552253b106ec5a1c779fce2265a2abce9f1fb606
