package planetaryprotector.menu;
import com.thizthizzydizzy.dizzyengine.ResourceManager;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import com.thizthizzydizzy.dizzyengine.graphics.image.Color;
import com.thizthizzydizzy.dizzyengine.ui.component.Component;
import org.joml.Vector2d;
import static org.lwjgl.glfw.GLFW.*;
import planetaryprotector.game.Game;
public class MenuComponentPhaseMarker extends Component{
    private final Game game;
    private final int phase;
    public MenuComponentPhaseMarker(MenuGame menu){
        game = menu.game;
        this.phase = menu.game.phase;
    }
    public float opacity = .2f;
    private boolean opacitizing = true;
    @Override
    public void onAdded(){
        setSize(parent.getSize());
    }
    @Override
    public void onMouseButton(int id, Vector2d pos, int button, int action, int mods){
        if(action!=GLFW_PRESS)return;
        if(opacitizing){
            if(opacity<.8)return;
            opacitizing = false;
            game.paused = false;
        }
    }
    @Override
    public void render(double deltaTime){
        if(game.doNotDisturb||!game.paused){
            opacitizing = false;
        }
        if(opacitizing){
            opacity += deltaTime*.9f;
            if(opacity>1){
                opacity = 1;
            }
        }else{
            opacity -= deltaTime*0.4f;
        }
        if(phase>3)return;
        Renderer.setColor(1, 1, 1, opacity);
        Renderer.fillRect(x, y, x+getWidth(), y+getHeight(), ResourceManager.getTexture("/textures/phase/"+phase+".png"));
        Renderer.setColor(Color.WHITE);
    }
}
