package planetaryprotector.structure;
import simplelibrary.opengl.ImageStash;
import simplelibrary.opengl.Renderer2D;
public class Shield extends Renderer2D{
    private final ShieldGenerator generator;
    private double x,y;
    public Shield(ShieldGenerator generator){
        x = generator.width/2;
        y = generator.height/2;
        this.generator = generator;
    }
    public void render(){
        drawRect(x-generator.getShieldSize()/2, y-generator.getShieldSize()/2, x+generator.getShieldSize()/2, y+generator.getShieldSize()/2, ImageStash.instance.getTexture("/textures/structures/shield.png"));
        if(generator.shieldOutline){
            drawRect(x-generator.getShieldSize()/2, y-generator.getShieldSize()/2, x+generator.getShieldSize()/2, y+generator.getShieldSize()/2, ImageStash.instance.getTexture("/textures/structures/shield outline.png"));
        }
    }
    public void renderOnWorld(){
        drawRect(generator.x+x-generator.getShieldSize()/2, generator.y+y-generator.getShieldSize()/2, generator.x+x+generator.getShieldSize()/2, generator.y+y+generator.getShieldSize()/2, ImageStash.instance.getTexture("/textures/structures/shield.png"));
        if(generator.shieldOutline){
            drawRect(generator.x+x-generator.getShieldSize()/2, generator.y+y-generator.getShieldSize()/2, generator.x+x+generator.getShieldSize()/2, generator.y+y+generator.getShieldSize()/2, ImageStash.instance.getTexture("/textures/structures/shield outline.png"));
        }
    }
}