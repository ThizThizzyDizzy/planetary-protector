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
                    drawRect(x, y, x+width, y+height, ImageStash.instance.getTexture(textureRoot+"CheckedMouseover.png"));
                }else{
                    drawRect(x, y, x+width, y+height, ImageStash.instance.getTexture(textureRoot+"Checked.png"));
                }
            }else{
                if(isMouseOver){
                    drawRect(x, y, x+width, y+height, ImageStash.instance.getTexture(textureRoot+"Mouseover.png"));
                }else{
                    drawRect(x, y, x+width, y+height, ImageStash.instance.getTexture(textureRoot+".png"));
                }
            }
        }else{
            if(isChecked){
                drawRect(x, y, x+width, y+height, ImageStash.instance.getTexture(textureRoot+"CheckedDisabled.png"));
            }else{
                drawRect(x, y, x+width, y+height, ImageStash.instance.getTexture(textureRoot+"Disabled.png"));
            }
            
        }
    }
    @Override
    public void mouseEvent(double x, double y, int button, boolean isDown) {
        super.mouseEvent(x, y, button, isDown);
        if(button==0&&isDown&&enabled)isChecked = !isChecked;
    }
    
}