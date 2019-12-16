package planetaryprotector.menu.ingame;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import planetaryprotector.menu.MenuGame;
import simplelibrary.opengl.gui.components.MenuComponent;
public abstract class MenuComponentOverlay extends MenuComponent{
    protected final MenuGame game;
    public MenuComponentOverlay(MenuGame game){
        super(0, 0, Display.getWidth(), Display.getHeight());
        this.game = game;
    }
    @Override
    public void renderBackground(){
        GL11.glColor4d(0, 0, 0, 0.75);
        drawRect(x, y, width, height, 0);
        GL11.glColor4d(1, 1, 1, 1);
    }
    public void close(){
        game.closeOverlay();
    }
    public void open(MenuComponentOverlay parent){
        close();
        game.overlay = game.add(new MenuExpedition(game));
    }
}