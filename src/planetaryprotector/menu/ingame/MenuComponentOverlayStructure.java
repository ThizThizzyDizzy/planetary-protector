package planetaryprotector.menu.ingame;
import planetaryprotector.structure.StructureType;
import planetaryprotector.menu.MenuGame;
import planetaryprotector.structure.Structure;
public abstract class MenuComponentOverlayStructure extends MenuComponentOverlay{
    private final Structure structure;
    private final StructureType type;
    public MenuComponentOverlayStructure(MenuGame menu, Structure structure){
        super(menu);
        this.structure = structure;
        type = structure.type;
    }
    @Override
    public void tick(){
        super.tick();
        if(structure.type!=type)close();
    }
}