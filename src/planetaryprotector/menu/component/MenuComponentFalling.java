package planetaryprotector.menu.component;
import planetaryprotector.Core;
import planetaryprotector.item.Item;
import org.lwjgl.opengl.Display;
import simplelibrary.opengl.ImageStash;
import simplelibrary.opengl.gui.components.MenuComponent;
public class MenuComponentFalling extends MenuComponent{
    public double yVelocity;
    private final Item item;
    public MenuComponentFalling(double x, double y, Item item){
        super(x,y,20,20);
        this.item = item;
    }
    @Override
    public void render(){
        if(!Core.game.baseGUI){
            return;
        }
        removeRenderBound();
        if(!Core.game.paused){
            if(y>Display.getHeight()){
                return;
            }
        }
        drawRect(x, y, x+width, y+height, ImageStash.instance.getTexture("/textures/items/"+item.texture+".png"));
    }

    @Override
    public void tick(){
        if(!Core.game.paused){
            if(y>Display.getHeight()){
                return;
            }
            yVelocity+= 0.981;
            y += yVelocity;
        }
    }
}