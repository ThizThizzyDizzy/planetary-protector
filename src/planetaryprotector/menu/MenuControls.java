package planetaryprotector.menu;
import planetaryprotector.Controls;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import simplelibrary.opengl.gui.components.MenuComponent;
import simplelibrary.opengl.gui.components.MenuComponentButton;
public class MenuControls extends MenuComponent{
    private final MenuComponentButton back;
    private final MenuGame game;
    double yOffset = 0;
    public MenuControls(MenuGame game){
        super(0, 0, Display.getWidth(), Display.getHeight());
        back = add(new MenuComponentButton(Display.getWidth()/2-400, Display.getHeight()-160, 800, 80, "Back", true));
        this.game = game;
    }
    @Override
    public void render(){
        GL11.glColor4d(0, 0, 0, 0.75);
        drawRect(x, y, width, height, 0);
        GL11.glColor4d(1, 1, 1, 1);
        for(MenuComponent component : components){
            component.render();
        }
        yOffset = 0;
        text("Move up: "+getKeyName(Controls.up));
        text("Move left: "+getKeyName(Controls.left));
        text("Move down: "+getKeyName(Controls.down));
        text("Move right: "+getKeyName(Controls.right));
        text("Move to position: Right Click");
        text("Menu: "+getKeyName(Controls.menu));
        text("Deselect worker: "+getKeyName(Controls.deselect));
        text("Cancel current task: "+getKeyName(Controls.cancel));
        text("Pause: "+getKeyName(Controls.pause));
        text("Hide Overlay: LShift+"+getKeyName(Controls.hide));
        text("Mute music: "+getKeyName(Controls.mute));
        if(MenuGame.cheats){
            text("Cheat | Lose: "+getKeyName(Controls.CHEAT_LOSE));
            text("Cheat | Phase: "+getKeyName(Controls.CHEAT_PHASE));
            text("Cheat | Secret: "+getKeyName(Controls.CHEAT_SECRET));
            text("Cheat | Worker: "+getKeyName(Controls.CHEAT_WORKER));
            text("Cheat | Cloud: "+getKeyName(Controls.CHEAT_CLOUD));
            text("Cheat | Fog: "+getKeyName(Controls.CHEAT_FOG));
            text("Cheat | Resources: "+getKeyName(Controls.CHEAT_RESOURCES));
            text("Cheat | Enemy: "+getKeyName(Controls.CHEAT_ENEMY));
            text("Cheat | Peace: "+getKeyName(Controls.CHEAT_PEACE));
        }
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
            game.components.remove(this);
            game.overlay = game.add(new MenuIngame(game));
        }
    }
    private void centeredTextWithBackground(double left, double top, double right, double bottom, String str) {
        GL11.glColor4d(0, 0, 0, 0.75);
        drawRect(left, top, right, bottom, 0);
        GL11.glColor4d(1, 1, 1, 1);
        drawCenteredText(left,top,right,bottom, str);
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
    private void text(String string){
        textWithBackground(0, yOffset, Display.getWidth(), yOffset+50, string);
        yOffset+=50;
    }
}