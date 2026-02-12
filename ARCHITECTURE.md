## Architecture Overview

This project uses a **Clean Architecture** + **MVVM** with three main layers:

- **Presentation**: UI, state holders, and navigation.
- **Domain**: Business rules and use cases.
- **Data**: Concrete data sources and repository implementations.

Dependencies always point **inward**:

- `presentation → domain`
- `data → domain`
- `domain → no other layer`

---

## Domain Layer – What belongs here

**Responsibilities**

- Define core business models (entities).
- Define abstract contracts for data layer (repositories).
- Implement use cases that express business rules.

**What should be in this layer**

- Pure Kotlin classes with no Android/Compose/UI dependencies.
- Interfaces that describe what data is needed.
- Use cases which expose specific behaviors without knowing how they are implemented.

**What must NOT be in this layer**

- Android framework types (Context, Activity, ViewModel, LiveData).
- Networking/client implementations, database code, or map SDK calls.
- Knowledge of where data comes from.

## Data Layer – What belongs here

**Responsibilities**

- Provide data to the domain layer by implementing domain contracts.
- Talk to real or fake sources (network, database, local cache, generators).
- Map external formats to domain models.

**What should be in this layer**

- Repository implementations that satisfy domain interfaces ( Repositories are abstractions that
  provide data to the domain layer, hiding the details of where it comes from).
- Mapping logic from raw data to domain entities.

**What must NOT be in this layer**

- UI or navigation logic.
- Business rules that decide *how* data is used (filtering by category, selecting the best item,
  etc.) – those should live in use cases.
- References to Android components or Compose.

**Policies for adding code**

- When you need a new way to load or store data, add it here and expose it via a **domain contract
  **.
- Reuse domain models; only introduce separate data models if external schemas differ, and map them
  explicitly.
- Keep this layer replaceable: you should be able to swap fake data with real API without changing
  domain or presentation logic.

---

## Presentation Layer – What belongs here

**Responsibilities**

- Render UI and handle user interactios.
- Hold screen state and call domain use cases.
- Coordinate navigation and connection to platform services (through abstractions).

**What should be in this layer**

- UI components (e.g., composables, views) that display state and emit events.
- ViewModels that:
    - Own immutable UI state objects.
    - Call domain use cases to perform work.
    - Transform domain results into UI-friendly state.
- Platform entry points (activities, fragments, navigation setup) that wire everything together.

**What must NOT be in this layer**

- Direct calls to low-level data sources (network client, DB access) bypassing use cases.
- Heavy business logic that could be reused in another front-end.
- Knowledge of how data is persisted or fetched (that is domain + data responsibility).

**Policies for adding code**

- New UI screens should:
    - Define a UI state model.
    - Use a ViewModel to own that state and orchestrate use cases.
    - Keep composables as stateless as possible (read state + emit events).
- Make sure composables have preview functions with sample data to facilitate design and testing.

---

## Dependency Injection (Koin)

This project uses Koin dependency injection to assemble the object graph. The DI setup is defined in
the `di` package, where modules are created for each layer:
