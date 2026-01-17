# Project Memory: Planetary Protector

## Project Status: Mid-Refactor
- **Transitioning to DizzyEngine:** The project is currently being refactored from a legacy custom engine/GUI-based logic (where game logic and rendering were in `MenuGame`) to the **DizzyEngine**.
- **Logic & Representation:** Game logic and state are moving to the `Game` class.
- **Bounding Boxes:** Legacy `BoundingBox` classes are being replaced by `AxisAlignedBoundingBox` from the engine.
- **Legacy Rendering:** Methods prefixed with `fake` (e.g., `fakeRenderWorld`) represent the old fixed-function pipeline rendering. These are still active temporarily until their functionality is fully refactored into the new system.
- **2D to 3D** The game was previously 2D, existing entirely in screen-space coordinates. It is now internally 3D, albeit with a 2D three-quarters projection to achieve the same look.

## Project Structure & Environment
- **Build System:** This is an `ant` NetBeans project.
- **Environment Constraints:** The project cannot currently be run outside of NetBeans itself.
- **LSP Support:** A `pom.xml` is present in the project root **strictly** to provide metadata for the Language Server (LSP).
    - **False Positives:** The `edit` tool's inline diagnostics are often **incorrect** (e.g., reporting `expected package ""`). Ignore them.
    - **Verification:** Always verify code health using `opencode debug lsp diagnostics <absolute_path>`.
    - **Build System:** **DO NOT** use Maven for building. Continue using the NetBeans Ant system.
    - **Maintenance:** If adding new libraries, update both Ant and the `pom.xml`.

## Engine Information
- **Custom Engine:** This project uses the **DizzyEngine**.
- **Location:** The engine source is located in a parallel directory at `../Dizzy-Engine`.
- **Three-Quarters Projection:** The game uses a 3/4 projection system. High-altitude objects (like clouds or skyscraper tops) require vertical offsets in screen-space based on the game's `shearFactor` to appear correctly positioned.
- **Object Indexing:** DizzyEngine maintains type-based indexes (e.g., `game.structures`, `game.droppedItems`). These are exposed as **Unmodifiable Lists**. To sort or modify these collections locally, always create a copy (e.g., `new ArrayList<>(game.droppedItems)`).

## World Structure
- **Bounding Boxes:** The world uses a nested bounding box structure:
    - **City Bounding Box:** Encloses all structures.
    - **World Bounding Box:** Extends beyond the city, acting as a buffer populated with trees where the camera can pan.
    - **Void:** The area outside the World Bounding Box, serving as a visual boundary.
- **Skyscrapers:** Typical skyscraper height is up to 1000 units.

## Agent Guidelines
- **Insightful Knowledge:** Add any non-obvious insights or engine-specific quirks shared by the user to this file to maintain project memory.
