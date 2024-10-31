package planetaryprotector.menu.ingame;
import com.thizthizzydizzy.dizzyengine.DizzyEngine;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import com.thizthizzydizzy.dizzyengine.ui.component.Button;
import com.thizthizzydizzy.dizzyengine.ui.layout.ConstrainedLayout;
import com.thizthizzydizzy.dizzyengine.ui.layout.constraint.PositionAnchorConstraint;
import org.lwjgl.glfw.GLFW;
import planetaryprotector.Controls;
import planetaryprotector.menu.MenuGame;
public class MenuControls extends MenuComponentOverlay{
    private final Button back;
    float yOffset = 0;
    private final int textHeight = 40;
    public MenuControls(MenuGame menu){
        super(menu);
        var layout = setLayout(new ConstrainedLayout());
        back = add(new Button("Back", true));
        back.setSize(800, 80);
        layout.constrain(back, new PositionAnchorConstraint(.5f, 0, .5f, 1, -400, -160));
        back.addAction(() -> {
            open(new MenuIngame(menu));
        });
    }
    @Override
    public void draw(double deltaTime){
        yOffset = 0;
        text("Menu: "+getKeyName(Controls.menu));
        text("Deselect: "+getKeyName(Controls.deselect));
        text("Pause: "+getKeyName(Controls.pause));
        text("Mute music: "+getKeyName(Controls.mute));
        text("Hide skyscrapers: "+getKeyName(Controls.hideSkyscrapers));
        text("Show power networks: "+getKeyName(Controls.showPowerNetworks));
        if(menu.game.cheats){
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
    public void onKey(int id, int key, int scancode, int action, int mods){
        super.onKey(id, key, scancode, action, mods);
        if(key==Controls.menu&&action==GLFW.GLFW_PRESS){
            open(new MenuIngame(menu));
        }
    }
    private void centeredTextWithBackground(float left, float top, float right, float bottom, String str){
        Renderer.setColor(0, 0, 0, 0.75f);
        Renderer.fillRect(left, top, right, bottom, 0);
        Renderer.setColor(1, 1, 1, 1);
        Renderer.drawCenteredText(left, top, right, bottom, str);
    }
    private void textWithBackground(float left, float top, float right, float bottom, String str){
        Renderer.setColor(0, 0, 0, 0.75f);
        Renderer.fillRect(left, top, Renderer.getStringWidth(str, bottom-top)+left, bottom, 0);
        Renderer.setColor(1, 1, 1, 1);
        Renderer.drawText(left, top, right, bottom, str);
    }
    private String getKeyName(int key){
        return key==-1?"NONE":GLFW.glfwGetKeyName(key, GLFW.glfwGetKeyScancode(key));
    }
    private void text(String string){
        textWithBackground(0, yOffset, DizzyEngine.screenSize.x, yOffset+textHeight, string);
        yOffset += textHeight;
    }
}
