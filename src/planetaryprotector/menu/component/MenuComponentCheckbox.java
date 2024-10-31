package planetaryprotector.menu.component;
import com.thizthizzydizzy.dizzyengine.ResourceManager;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import com.thizthizzydizzy.dizzyengine.ui.component.Component;
import org.joml.Vector2d;
import org.lwjgl.glfw.GLFW;
public class MenuComponentCheckbox extends Component{
    private final String textureRoot;
    public boolean isChecked;
    public boolean enabled;
    public MenuComponentCheckbox(float x, float y, float width, float height){
        this(x, y, width, height, true);
    }
    public MenuComponentCheckbox(float x, float y, float width, float height, boolean enabled){
        this(x, y, width, height, enabled, false);
    }
    public MenuComponentCheckbox(float x, float y, float width, float height, boolean enabled, boolean checked){
        this(x, y, width, height, enabled, checked, false);
    }
    public MenuComponentCheckbox(float x, float y, float width, float height, boolean enabled, boolean checked, boolean useMouseover){
        this(x, y, width, height, enabled, checked, useMouseover, "/textures/gui/checkbox");
    }
    public MenuComponentCheckbox(float x, float y, float width, float height, boolean enabled, boolean checked, boolean useMouseover, String textureRoot){
        this.x = x;
        this.y = y;
        setSize(width, height);
        this.textureRoot = textureRoot;
        this.enabled = enabled;
        this.isChecked = checked;
    }
    @Override
    public void draw(double deltaTime){
        if(enabled){
            if(isChecked){
                if(isCursorFocused()){
                    Renderer.fillRect(x, y, x+getWidth(), y+getHeight(), ResourceManager.getTexture(textureRoot+"CheckedMouseover.png"));
                }else{
                    Renderer.fillRect(x, y, x+getWidth(), y+getHeight(), ResourceManager.getTexture(textureRoot+"Checked.png"));
                }
            }else{
                if(isCursorFocused()){
                    Renderer.fillRect(x, y, x+getWidth(), y+getHeight(), ResourceManager.getTexture(textureRoot+"Mouseover.png"));
                }else{
                    Renderer.fillRect(x, y, x+getWidth(), y+getHeight(), ResourceManager.getTexture(textureRoot+".png"));
                }
            }
        }else{
            if(isChecked){
                Renderer.fillRect(x, y, x+getWidth(), y+getHeight(), ResourceManager.getTexture(textureRoot+"CheckedDisabled.png"));
            }else{
                Renderer.fillRect(x, y, x+getWidth(), y+getHeight(), ResourceManager.getTexture(textureRoot+"Disabled.png"));
            }

        }
    }
    @Override
    public void onMouseButton(int id, Vector2d pos, int button, int action, int mods){
        super.onMouseButton(id, pos, button, action, mods);
        if(button==0&&action==GLFW.GLFW_PRESS&&enabled)isChecked = !isChecked;
    }
}
