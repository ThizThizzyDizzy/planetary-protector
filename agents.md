# Project Memory: Planetary Protector

## Project Status: Mid-Refactor
- **Transitioning to DizzyEngine:** The project is currently being refactored from a legacy custom engine/GUI-based logic (where game logic and rendering were in `MenuGame`) to the **DizzyEngine**.
- **Logic & Representation:** Game logic and state are moving to the `Game` class.
- **Bounding Boxes:** Legacy `BoundingBox` classes are being replaced by `AxisAlignedBoundingBox` from the engine.
- **Legacy Rendering:** Methods prefixed with `fake` (e.g., `fakeRenderWorld`) represent the old fixed-function pipeline rendering. These are still active temporarily until their functionality is fully refactored into the new system.

## Project Structure & Environment
- **Build System:** This is an `ant` NetBeans project.
- **Environment Constraints:** The project cannot currently be run outside of NetBeans itself.

## Engine Information
- **Custom Engine:** This project uses the **DizzyEngine**.
- **Location:** The engine source is located in a parallel directory at `../Dizzy-Engine`.

## World Structure
- **Bounding Boxes:** The world uses a nested bounding box structure:
    - **City Bounding Box:** Encloses all structures.
    - **World Bounding Box:** Extends beyond the city, acting as a buffer populated with trees where the camera can pan.
    - **Void:** The area outside the World Bounding Box, serving as a visual boundary.
