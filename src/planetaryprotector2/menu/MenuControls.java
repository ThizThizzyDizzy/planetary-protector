package planetaryprotector2.menu;
import planetaryprotector.Controls;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import simplelibrary.opengl.gui.GUI;
import simplelibrary.opengl.gui.components.MenuComponentButton;
import simplelibrary.opengl.gui.Menu;
public class MenuControls extends Menu{
    private final MenuComponentButton back;
    private final MenuGame game;
    public MenuControls(GUI gui, Menu parent, MenuGame game){
        super(gui, parent);
        back = add(new MenuComponentButton(Display.getWidth()/2-400, Display.getHeight()-160, 800, 80, "Back", true));
        this.game = game;
    }
    @Override
    public void renderBackground(){
    }
    @Override
    public void render(int millisSinceLastTick){
        game.render(0);
        GL11.glColor4d(0, 0, 0, 0.75);
        drawRect(0, 0, Display.getWidth(), Display.getHeight(), 0);
        GL11.glColor4d(1, 1, 1, 1);
        super.render(millisSinceLastTick);
        textWithBackground(0, 0, Display.getWidth(), 50, "Move up: "+getKeyName(Controls.up));
        textWithBackground(0, 50, Display.getWidth(), 100, "Move left: "+getKeyName(Controls.left));
        textWithBackground(0, 100, Display.getWidth(), 150, "Move down: "+getKeyName(Controls.down));
        textWithBackground(0, 150, Display.getWidth(), 200, "Move right: "+getKeyName(Controls.right));
        textWithBackground(0, 200, Display.getWidth(), 250, "Move to position: Right Click");
        textWithBackground(0, 250, Display.getWidth(), 300, "Menu: "+getKeyName(Controls.menu));
        textWithBackground(0, 300, Display.getWidth(), 350, "Deselect worker: "+getKeyName(Controls.deselect));
        textWithBackground(0, 350, Display.getWidth(), 400, "Cancel current task: "+getKeyName(Controls.cancel));
        textWithBackground(0, 400, Display.getWidth(), 450, "Pause: "+getKeyName(Controls.pause));
        textWithBackground(0, 450, Display.getWidth(), 500, "Hide: LShift+"+getKeyName(Controls.hide));
    }
    @Override
    public void keyboardEvent(char character, int key, boolean pressed, boolean repeat) {
        if(key==Controls.menu&&pressed&&!repeat){
            buttonClicked(back);
        }
    }
    @Override
    public void buttonClicked(MenuComponentButton button) {
        if(button==back){
            gui.open(parent);
        }
    }
    private void textWithBackground(double left, double top, double right, double bottom, String str) {
        GL11.glColor4d(0, 0, 0, 0.75);
        drawRect(left, top, simplelibrary.font.FontManager.getLengthForStringWithHeight(str, bottom-top)+left, bottom, 0);
        GL11.glColor4d(1, 1, 1, 1);
        drawText(left,top,right,bottom, str);
    }
    private String getKeyName(int key){
        return key==-1?"NONE":Keyboard.getKeyName(key);
    }
}