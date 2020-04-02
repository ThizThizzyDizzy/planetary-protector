package planetaryprotector.menu.component;
import planetaryprotector.Core;
import planetaryprotector.item.Item;
import org.lwjgl.opengl.Display;
import planetaryprotector.game.Game;
import planetaryprotector.menu.MenuGame;
import simplelibrary.opengl.gui.components.MenuComponent;
public class MenuComponentFalling extends MenuComponent{
    public double yVelocity;
    private final Item item;
    private final Game game;
    public MenuComponentFalling(MenuGame menu, double x, double y, Item item){
        super(x,y,20,20);
        this.item = item;
        this.game = menu.game;
    }
    @Override
    public void render(){
        if(!game.isPlayable()){
            return;
        }
        if(!game.paused){
            if(y>Display.getHeight()){
                return;
            }
        }
        drawRect(x, y, x+width, y+height, item.getTexture());
    }

    @Override
    public void tick(){
        if(!game.paused){
            if(y>Display.getHeight()){
                return;
            }
            yVelocity+= 0.981;
            y += yVelocity;
        }
    }
}