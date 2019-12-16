package planetaryprotector.menu.component;
import planetaryprotector.Core;
import planetaryprotector.item.Item;
import org.lwjgl.opengl.GL11;
import simplelibrary.opengl.gui.components.MenuComponent;
public class MenuComponentRising extends MenuComponent{
    public final Item item;
    public double opacity = 1;
    public MenuComponentRising(double x, double y, Item item){
        super(x,y,20,20);
        this.item=item;
    }
    @Override
    public void render(){
        removeRenderBound();
        if(!Core.game.paused){
            if(opacity<=0){
                return;
            }
        }
        if(!Core.game.showUI){
            return;
        }
        GL11.glColor4d(1, 1, 1, opacity);
        drawRect(x, y, x+width, y+height, item.getTexture());
        GL11.glColor4d(1, 1, 1, 1);
    }
    @Override
    public void tick(){
        if(!Core.game.paused){
            if(opacity<=0){
                return;
            }
            y-=2.5;
            opacity -= 0.018;
        }
    }
}