package planetaryprotector.menu.ingame;
import org.lwjgl.opengl.GL11;
import planetaryprotector.Core;
import planetaryprotector.menu.MenuGame;
import simplelibrary.opengl.gui.components.MenuComponent;
public abstract class MenuComponentOverlay extends MenuComponent{
    protected final MenuGame menu;
    public MenuComponentOverlay(MenuGame menu){
        super(0, 0, Core.helper.displayWidth(), Core.helper.displayHeight());
        this.menu = menu;
    }
    @Override
    public void renderBackground(){
        GL11.glColor4d(0, 0, 0, 0.75);
        drawRect(x, y, width, height, 0);
        GL11.glColor4d(1, 1, 1, 1);
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