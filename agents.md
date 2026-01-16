# Project Memory: Planetary Protector

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
