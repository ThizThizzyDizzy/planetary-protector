package planetaryprotector.ui.component.layer;
import com.thizthizzydizzy.dizzyengine.DizzyEngine;
import com.thizthizzydizzy.dizzyengine.ResourceManager;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import com.thizthizzydizzy.dizzyengine.graphics.image.Color;
import com.thizthizzydizzy.dizzyengine.ui.component.Button;
import com.thizthizzydizzy.dizzyengine.ui.component.Component;
import com.thizthizzydizzy.dizzyengine.ui.component.layer.ComponentLayer;
public class ButtonBackgroundLayer extends ComponentLayer{
    @Override
    public void draw(Component c, double deltaTime){
        Renderer.setColor(Color.WHITE);
        Button button = (Button)c;
        String texture = "assets/textures/ui/button/button.png";
        for(int i = 0; i<DizzyEngine.CURSOR_LIMIT; i++){
            if(button.isCursorFocused[i])texture = "assets/textures/ui/button/mouseover.png";
        }
        if(button.pressed) texture = "assets/textures/ui/button/pressed.png";
        if(!button.enabled)texture = "assets/textures/ui/button/disabled.png";
        Renderer.fillRect(c.x, c.y, c.x+c.getWidth(), c.y+c.getHeight(), ResourceManager.getTexture(texture));
    }
}
