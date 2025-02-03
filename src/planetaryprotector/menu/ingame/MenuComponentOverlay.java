package planetaryprotector.menu.ingame;
import com.thizthizzydizzy.dizzyengine.graphics.image.Color;
import com.thizthizzydizzy.dizzyengine.ui.component.Panel;
import com.thizthizzydizzy.dizzyengine.ui.component.layer.ColorBackgroundLayer;
import planetaryprotector.menu.MenuGame;
public abstract class MenuComponentOverlay extends Panel{
    protected final MenuGame menu;
    public MenuComponentOverlay(MenuGame menu){
        this.menu = menu;
        background = new ColorBackgroundLayer(new Color(0, 0, 0, .75f));
    }
    @Override
    public void onAdded(){
        setSize(parent.getSize());
    }
    @Override
    public void draw(double deltaTime){
        if(!parent.getSize().equals(getSize()))setSize(parent.getSize());
        super.draw(deltaTime);
    }
    public void close(){
        menu.game.paused = false;
        menu.overlay = null;
    }
    public void open(MenuComponentOverlay overlay){
        close();
        menu.overlay = overlay;
    }
}
