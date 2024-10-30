package planetaryprotector.menu.component;
import simplelibrary.opengl.ImageStash;
import simplelibrary.opengl.gui.components.MenuComponent;
public class MenuComponentCheckbox extends MenuComponent{
    private final String textureRoot;
    public boolean isChecked;
    public boolean enabled;
    public MenuComponentCheckbox(float x, float y, float width, float height){
        this(x,y,width,height,true);
    }
    public MenuComponentCheckbox(float x, float y, float width, float height, boolean enabled){
        this(x,y,width,height,enabled,false);
    }
    public MenuComponentCheckbox(float x, float y, float width, float height, boolean enabled, boolean checked){
        this(x,y,width,height,enabled,checked,false);
    }
    public MenuComponentCheckbox(float x, float y, float width, float height, boolean enabled, boolean checked, boolean useMouseover){
        this(x,y,width,height,enabled,checked,useMouseover,"/textures/gui/checkbox");
    }
    public MenuComponentCheckbox(float x, float y, float width, float height, boolean enabled, boolean checked, boolean useMouseover, String textureRoot){
        super(x,y,width,height);
        this.textureRoot = textureRoot;
        this.enabled = enabled;
        this.isChecked = checked;
    }
    @Override
    public void render(){
        if(enabled){
            if(isChecked){
                if(isMouseOver){
                    Renderer.fillRect(x, y, x+width, y+height, ResourceManager.getTexture(textureRoot+"CheckedMouseover.png"));
                }else{
                    Renderer.fillRect(x, y, x+width, y+height, ResourceManager.getTexture(textureRoot+"Checked.png"));
                }
            }else{
                if(isMouseOver){
                    Renderer.fillRect(x, y, x+width, y+height, ResourceManager.getTexture(textureRoot+"Mouseover.png"));
                }else{
                    Renderer.fillRect(x, y, x+width, y+height, ResourceManager.getTexture(textureRoot+".png"));
                }
            }
        }else{
            if(isChecked){
                Renderer.fillRect(x, y, x+width, y+height, ResourceManager.getTexture(textureRoot+"CheckedDisabled.png"));
            }else{
                Renderer.fillRect(x, y, x+width, y+height, ResourceManager.getTexture(textureRoot+"Disabled.png"));
            }
            
        }
    }
    @Override
    public void onMouseButton(double x, double y, int button, boolean pressed, int mods){
        super.onMouseButton(x, y, button, pressed, mods);
        if(button==0&&pressed&&enabled)isChecked = !isChecked;
    }
}