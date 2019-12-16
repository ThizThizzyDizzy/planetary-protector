package planetaryprotector.menu.ingame;
import planetaryprotector.Controls;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import planetaryprotector.menu.MenuGame;
import simplelibrary.opengl.gui.components.MenuComponentButton;
public class MenuControls extends MenuComponentOverlay{
    private final MenuComponentButton back;
    double yOffset = 0;
    private final int textHeight = 40;
    public MenuControls(MenuGame game){
        super(game);
        back = add(new MenuComponentButton(Display.getWidth()/2-400, Display.getHeight()-160, 800, 80, "Back", true));
    }
    @Override
    public void render(){
        yOffset = 0;
        text("Menu: "+getKeyName(Controls.menu));
        text("Deselect: "+getKeyName(Controls.deselect));
        text("Pause: "+getKeyName(Controls.pause));
        text("Mute music: "+getKeyName(Controls.mute));
        text("Hide skyscrapers: "+getKeyName(Controls.hideSkyscrapers));
        text("Show power networks: "+getKeyName(Controls.showPowerNetworks));
        if(game.cheats){
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
            open(new MenuIngame(game));
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
        textWithBackground(0, yOffset, Display.getWidth(), yOffset+textHeight, string);
        yOffset+=textHeight;
    }
}