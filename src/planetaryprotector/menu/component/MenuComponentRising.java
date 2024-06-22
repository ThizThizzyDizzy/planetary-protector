package planetaryprotector.menu.component;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import com.thizthizzydizzy.dizzyengine.ui.component.Component;
import planetaryprotector.item.Item;
import planetaryprotector.game.Game;
import planetaryprotector.menu.MenuGame;
public class MenuComponentRising extends Component{
    public final Item item;
    public float opacity = 1;
    private final Game game;
    public MenuComponentRising(MenuGame menu, float x, float y, Item item){
        this.x = x;
        this.y = y;
        setSize(20, 20);
        this.item = item;
        this.game = menu.game;
    }
    @Override
    public void render(double deltaTime){
        if(!game.paused){
            if(opacity<=0){
                return;
            }
            y -= 50*deltaTime;
            opacity -= .36*deltaTime;
        }
        if(!game.isPlayable()){
            return;
        }
        Renderer.setColor(1, 1, 1, opacity);
        Renderer.fillRect(x, y, x+getWidth(), y+getHeight(), item.getTexture());
        Renderer.setColor(1, 1, 1, 1);
    }
}
