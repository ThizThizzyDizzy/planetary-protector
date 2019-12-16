package planetaryprotector.building;
import org.lwjgl.opengl.GL11;
import simplelibrary.opengl.Renderer2D;
public class BuildingDamage extends Renderer2D{
    private final Building building;
    public double opacity = 1;
    public double x,y;
    public BuildingDamage(Building building, double x, double y){
        this.x = x;
        this.y = y;
        this.building = building;
    }
    public void render(){
        double size = 50;
        GL11.glColor4d(1, 1, 1, opacity);
        switch(building.type){
            case SKYSCRAPER:
                Skyscraper sky = (Skyscraper)building;
                drawRectWithBounds(x-building.x+(sky.right?1:0), y-building.y+sky.fallen, x+size-building.x+(sky.right?1:0), y+size-building.y+sky.fallen, 0, -((sky.floorCount-1)*sky.floorHeight), building.width, building.height-sky.fallen, building.getDamageTexture());
                break;
            case BASE:
                Base base = (Base)building;
                if(base.deathTick>100){
                    return;
                }
                drawRectWithBounds(x-building.x, y-building.y, x+size-building.x, y+size-building.y, 0, -25, building.width, building.height, building.getDamageTexture());
                break;
            default:
                drawRectWithBounds(x-building.x, y-building.y, x+size-building.x, y+size-building.y, 0, 0, building.width, building.height, building.getDamageTexture());
                break;
        }
        GL11.glColor4d(1, 1, 1, 1);
    }
}