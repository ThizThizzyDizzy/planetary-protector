package planetaryprotector.menu.component;
import planetaryprotector.item.Item;
import org.lwjgl.opengl.GL11;
import planetaryprotector.game.Game;
import planetaryprotector.menu.MenuGame;
import simplelibrary.opengl.gui.components.MenuComponent;
public class MenuComponentRising extends MenuComponent{
    public final Item item;
    public double opacity = 1;
    private final Game game;
    public MenuComponentRising(MenuGame menu, double x, double y, Item item){
        super(x,y,20,20);
        this.item=item;
        this.game = menu.game;
    }
    @Override
    public void render(){
        removeRenderBound();
        if(!game.paused){
            if(opacity<=0){
                return;
            }
        }
        if(!game.isPlayable()){
            return;
        }
        GL11.glColor4d(1, 1, 1, opacity);
        drawRect(x, y, x+width, y+height, item.getTexture());
        GL11.glColor4d(1, 1, 1, 1);
    }
    @Override
    public void tick(){
        if(!game.paused){
            if(opacity<=0){
                return;
            }
            y-=2.5;
            opacity -= 0.018;
        }
    }
}