package planetaryprotector.menu.component;
import com.thizthizzydizzy.dizzyengine.ResourceManager;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import com.thizthizzydizzy.dizzyengine.ui.component.Button;
public class MenuComponentClickable extends Button{
    private final String texture;
    public MenuComponentClickable(float x, float y, float width, float height, String texture){
        super();
        this.x = x;
        this.y = y;
        setSize(width, height);
        this.texture = texture;
    }
    @Override
    public void draw(double deltaTime){
        Renderer.fillRect(x, y, x+getWidth(), y+getHeight(), ResourceManager.getTexture(texture));
    }
}
