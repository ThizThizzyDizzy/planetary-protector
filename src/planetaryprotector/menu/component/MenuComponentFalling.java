package planetaryprotector.menu.component;
import com.thizthizzydizzy.dizzyengine.DizzyEngine;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import com.thizthizzydizzy.dizzyengine.ui.FlatUI;
import com.thizthizzydizzy.dizzyengine.ui.component.Component;
import planetaryprotector.item.Item;
import planetaryprotector.game.Game;
import planetaryprotector.menu.MenuGame;
public class MenuComponentFalling extends Component{
    public double yVelocity;
    private final Item item;
    private final Game game;
    public MenuComponentFalling(MenuGame menu, float x, float y, Item item){
        this.x = x;
        this.y = y;
        setSize(20, 20);
        this.item = item;
        this.game = menu.game;
    }
    @Override
    public void render(double deltaTime){
        if(!game.isPlayable()){
            return;
        }
        if(!game.paused){
            if(y>((FlatUI)DizzyEngine.getUIContext()).size.y){
                return;
            }
            yVelocity += 19.62*deltaTime;
            y += yVelocity;
        }
        Renderer.fillRect(x, y, x+getWidth(), y+getHeight(), item.getTexture());
    }
}
