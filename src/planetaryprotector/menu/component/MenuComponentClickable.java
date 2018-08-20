package planetaryprotector.menu.component;
import simplelibrary.opengl.ImageStash;
import simplelibrary.opengl.gui.components.MenuComponentButton;
public class MenuComponentClickable extends MenuComponentButton{
    private final String texture;
    public MenuComponentClickable(float x, float y, float width, float height, String texture){
        super(x,y,width,height,"",true);
        this.texture=texture;
    }
    @Override
    public void render(){
        drawRect(x, y, x+width, y+height, ImageStash.instance.getTexture(texture));
    }
}