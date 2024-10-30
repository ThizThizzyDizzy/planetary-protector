package planetaryprotector.menu;
import com.thizthizzydizzy.dizzyengine.DizzyEngine;
import com.thizthizzydizzy.dizzyengine.ResourceManager;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import com.thizthizzydizzy.dizzyengine.graphics.image.Color;
import com.thizthizzydizzy.dizzyengine.ui.Menu;
import com.thizthizzydizzy.dizzyengine.ui.component.Component;
import planetaryprotector.Sounds;
import java.util.Random;
import org.lwjgl.opengl.GL11;
public class MenuEpilogue2 extends Menu{
    private int yOffset = 0;
    private static final float speed = 4;
    private float opacity = 0;
    private float percent = 0;
    private int line = -1;
    private boolean written = true;
    @Override
    public void draw(double deltaTime){
        percent = ((Sounds.songTimer()-1921)/1152f)*1.05f;
        GL11.glTranslated(0, -yOffset, 0);
        Renderer.fillRect(0, 0, DizzyEngine.screenSize.x, DizzyEngine.screenSize.y, ResourceManager.getTexture("/textures/background/stone.png"));
        Renderer.fillRect(0, DizzyEngine.screenSize.y*2, DizzyEngine.screenSize.x, DizzyEngine.screenSize.y, ResourceManager.getTexture("/textures/background/stone.png"));
        Renderer.fillRect(0, DizzyEngine.screenSize.y*2, DizzyEngine.screenSize.x, DizzyEngine.screenSize.y*3, ResourceManager.getTexture("/textures/background/stone.png"));
        GL11.glTranslated(0, yOffset, 0);
    }
    @Override
    public void render(double deltaTime){
        if(Sounds.songTimer()>=1921){
            yOffset += speed;
            if(yOffset>DizzyEngine.screenSize.y*2){
                yOffset -= DizzyEngine.screenSize.y*2;
            }
            for(Component c : components){
                c.y -= speed;
            }
            if(new Random().nextDouble()<.0025*speed){
                add(new MenuComponentPrologueMineShaft());
            }
        }
        if(Sounds.songTimer()>=3073||opacity>0){
            opacity += .01;
            if(opacity>=1){
                new MenuMain(true).open();
            }
        }
        if(!written){
            if(line<MenuCredits.credits.size()){
                add(new MenuComponentCreditsText(line));
                written = true;
            }
        }else{
            if((percent*MenuCredits.credits.size())-1>line){
                line++;
                written = false;
            }
        }
        super.render(deltaTime);
        Renderer.setColor(0, 0, 0, opacity);
        Renderer.fillRect(0, 0, DizzyEngine.screenSize.x, DizzyEngine.screenSize.y, 0);
        Renderer.setColor(1, 1, 1, 1);
//        for(Component c : components){
//            if(c instanceof MenuComponentCreditsText)c.render(0);
//        }
    }
    private static class MenuComponentPrologueMineShaft extends Component{
        private boolean right = false;
        private boolean added = false;
        public MenuComponentPrologueMineShaft(){
            right = new Random().nextBoolean();
            x = right?DizzyEngine.screenSize.x-new Random().nextInt(400):-new Random().nextInt(400);
            y = new Random().nextInt(DizzyEngine.screenSize.y);
            setSize(400, 300);
        }
        public MenuComponentPrologueMineShaft(MenuComponentPrologueMineShaft shaft){
            right = shaft.right;
            x = /*shaft.x+*/ (right?-1:1)*shaft.getWidth();
            y = /*shaft.y+*/ 100;
            setSize(shaft.getSize());
        }
        @Override
        public void draw(double deltaTime){
            if(right){
                Renderer.fillRect(x+getWidth(), y, x, y+getHeight(), ResourceManager.getTexture("/textures/mineshaft.png"));
            }else{
                Renderer.fillRect(x, y, x+getWidth(), y+getHeight(), ResourceManager.getTexture("/textures/mineshaft.png"));
            }
            for(int i = 0; i<5; i++){
                int W = 25;
                int H = 25;
                float X = new Random().nextInt(400-W);
                float Y = 300-X/4-H;
                if(!right){
                    X = -X+getWidth();
                }
                Renderer.fillRect(x+X, y+Y, x+X+W, y+Y+H, ResourceManager.getTexture("/textures/worker.png"));
            }
            if(y<0||x<-400||x>DizzyEngine.screenSize.x+400||added)return;
            if(new Random().nextDouble()<.02*speed){
                added = true;
                add(new MenuComponentPrologueMineShaft(this));
            }
        }
    }
    private static class MenuComponentCreditsText extends Component{
        private static final int textHeight = 30;
        private final String text;
        public MenuComponentCreditsText(int line){
            y = DizzyEngine.screenSize.y;
            setSize(DizzyEngine.screenSize.x, textHeight);
            text = MenuCredits.credits.get(line);
        }
        @Override
        public void draw(double deltaTime){
            Renderer.setColor(Color.BLACK);
            String[] texts = text.split("&");
            float wide = getWidth()/(float)texts.length;
            for(int i = 0; i<texts.length; i++){
                Renderer.drawCenteredText(x+wide*i, y, x+wide*(i+1), y+getHeight(), texts[i]);
            }
        }
    }
}
