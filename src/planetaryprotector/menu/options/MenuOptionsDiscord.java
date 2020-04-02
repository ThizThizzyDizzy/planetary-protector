package planetaryprotector.menu.options;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import planetaryprotector.Core;
import planetaryprotector.game.Game;
import static simplelibrary.opengl.Renderer2D.drawRect;
import simplelibrary.opengl.gui.GUI;
import simplelibrary.opengl.gui.Menu;
import simplelibrary.opengl.gui.components.MenuComponentButton;
import simplelibrary.opengl.gui.components.MenuComponentOptionButton;
public class MenuOptionsDiscord extends Menu{
    private final MenuComponentButton back;
    public static boolean rpc = true;
    private final MenuComponentOptionButton rpcToggle;
    public MenuOptionsDiscord(GUI gui, Menu parent){
        super(gui, parent);
        back = add(new MenuComponentButton(Display.getWidth()/2-200, Display.getHeight()-80, 400, 40, "Back", true));
        double yOffset = 120;
        rpcToggle = add(new MenuComponentOptionButton(back.x, yOffset, back.width, back.height, "Rich Presence", true, true, rpc?0:1, "On", "Off"));
    }
    @Override
    public void buttonClicked(MenuComponentButton button){
        if(button==back){
            gui.open(parent);
            Core.initDiscord();
        }
    }
    @Override
    public void renderBackground(){
        GL11.glColor4d(1, 1, 1, 1);
        drawRect(0,0,Display.getWidth(), Display.getHeight(), Game.theme.getBackgroundTexture(1));
        rpc = rpcToggle.getIndex()==0;
    }
    @Override
    public void render(int millisSinceLastTick){
        super.render(millisSinceLastTick);
        GL11.glColor4d(1, 1, 1, 1);
        rpcToggle.render();
    }
}