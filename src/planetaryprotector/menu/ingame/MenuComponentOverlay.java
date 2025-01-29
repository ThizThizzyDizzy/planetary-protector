package planetaryprotector.menu.ingame;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import com.thizthizzydizzy.dizzyengine.ui.component.Panel;
import planetaryprotector.menu.MenuGame;
public abstract class MenuComponentOverlay extends Panel{
    protected final MenuGame menu;
    public MenuComponentOverlay(MenuGame menu){
        this.menu = menu;
    }
    @Override
    public void onAdded(){
        setSize(super.getSize());
    }
    @Override
    public void draw(double deltaTime){
        if(!super.getSize().equals(getSize()))setSize(super.getSize());
        Renderer.setColor(0, 0, 0, 0.75f);
        Renderer.fillRect(x, y, getWidth(), getHeight(), 0);
        Renderer.setColor(1, 1, 1, 1);
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
