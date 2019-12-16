package planetaryprotector.menu.ingame;
import planetaryprotector.building.Building;
import planetaryprotector.building.BuildingType;
import planetaryprotector.menu.MenuGame;
public abstract class MenuComponentOverlayBuilding extends MenuComponentOverlay{
    private final Building building;
    private final BuildingType type;
    public MenuComponentOverlayBuilding(MenuGame game, Building building){
        super(game);
        this.building = building;
        type = building.type;
    }
    @Override
    public void tick(){
        super.tick();
        if(building.type!=type)close();
    }
}