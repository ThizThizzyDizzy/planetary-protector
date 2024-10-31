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
    public void draw(double deltaTime){
        super.draw(deltaTime); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
        if(structure.type!=type)close();
    }
}