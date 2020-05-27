package planetaryprotector.menu.ingame;
import planetaryprotector.structure.building.Building;
import planetaryprotector.structure.building.BuildingType;
import planetaryprotector.menu.MenuGame;
public abstract class MenuComponentOverlayBuilding extends MenuComponentOverlay{
    private final Building building;
    private final BuildingType type;
    public MenuComponentOverlayBuilding(MenuGame menu, Building building){
        super(menu);
        this.building = building;
        type = building.type;
    }
    @Override
    public void tick(){
        super.tick();
        if(building.type!=type)close();
    }
}