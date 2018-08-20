package planetaryprotector.building;
import simplelibrary.opengl.ImageStash;
import simplelibrary.opengl.gui.components.MenuComponent;
public class MenuComponentShield extends MenuComponent{
    private final MenuComponentShieldGenerator generator;
    public MenuComponentShield(MenuComponentShieldGenerator generator){
        super(generator.width/2, generator.height/2, 0, 0);
        this.generator = generator;
    }
    @Override
    public void render(){
        drawRect(x-generator.getShieldSize()/2, y-generator.getShieldSize()/2, x+generator.getShieldSize()/2, y+generator.getShieldSize()/2, ImageStash.instance.getTexture("/textures/buildings/shield.png"));
        if(generator.shieldOutline){
            drawRect(x-generator.getShieldSize()/2, y-generator.getShieldSize()/2, x+generator.getShieldSize()/2, y+generator.getShieldSize()/2, ImageStash.instance.getTexture("/textures/buildings/shield outline.png"));
        }
    }
    public void renderOnWorld(){
        drawRect(generator.x+x-generator.getShieldSize()/2, generator.y+y-generator.getShieldSize()/2, generator.x+x+generator.getShieldSize()/2, generator.y+y+generator.getShieldSize()/2, ImageStash.instance.getTexture("/textures/buildings/shield.png"));
        if(generator.shieldOutline){
            drawRect(generator.x+x-generator.getShieldSize()/2, generator.y+y-generator.getShieldSize()/2, generator.x+x+generator.getShieldSize()/2, generator.y+y+generator.getShieldSize()/2, ImageStash.instance.getTexture("/textures/buildings/shield outline.png"));
        }
    }
}