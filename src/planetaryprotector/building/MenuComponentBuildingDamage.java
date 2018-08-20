package planetaryprotector.building;
import org.lwjgl.opengl.GL11;
import simplelibrary.opengl.ImageStash;
import simplelibrary.opengl.gui.components.MenuComponent;
public class MenuComponentBuildingDamage extends MenuComponent{
    private MenuComponentBuilding building;
    public double opacity = 1;
    public MenuComponentBuildingDamage(double x, double y, double width, double height){
        super(x, y, width, height);
    }
    @Override
    public void render(){
        removeRenderBound();
        if(building==null){
            building = (MenuComponentBuilding)parent;
        }
        GL11.glColor4d(1, 1, 1, opacity);
        switch(building.type){
            case SKYSCRAPER:
                MenuComponentSkyscraper sky = (MenuComponentSkyscraper)building;
                drawRectWithBounds(x-building.x+(sky.right?1:0), y-building.y+sky.fallen, x+width-building.x+(sky.right?1:0), y+height-building.y+sky.fallen, 0, -((sky.floorCount-1)*sky.floorHeight), building.width, building.height-sky.fallen, ImageStash.instance.getTexture("/textures/buildings/"+building.type.texture+" damage.png"));
                break;
            case BASE:
                MenuComponentBase base = (MenuComponentBase)building;
                if(base.deathTick>100){
                    return;
                }
                drawRectWithBounds(x-building.x, y-building.y, x+width-building.x, y+height-building.y, 0, -25, building.width, building.height, ImageStash.instance.getTexture("/textures/buildings/"+building.type.texture+" damage.png"));
                break;
            default:
                drawRectWithBounds(x-building.x, y-building.y, x+width-building.x, y+height-building.y, 0, 0, building.width, building.height, ImageStash.instance.getTexture("/textures/buildings/"+building.type.texture+" damage.png"));
                break;
        }
        GL11.glColor4d(1, 1, 1, 1);
    }
}