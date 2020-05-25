package planetaryprotector.menu;
import planetaryprotector.Sounds;
import java.awt.Color;
import java.util.Random;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import simplelibrary.opengl.ImageStash;
import simplelibrary.opengl.gui.GUI;
import simplelibrary.opengl.gui.Menu;
import simplelibrary.opengl.gui.components.MenuComponent;
public class MenuEpilogue2 extends Menu{
    public MenuEpilogue2(GUI gui){
        super(gui, null);
    }
    private int yOffset = 0;
    private static final double speed = 4;
    private double opacity = 0;
    private double percent = 0;
    private int line = -1;
    private boolean written = true;
    @Override
    public void tick(){
        super.tick();
        if(Sounds.songTimer()>=1921){
            yOffset+=speed;
            if(yOffset>Display.getHeight()*2){
                yOffset-=Display.getHeight()*2;
            }
            for(MenuComponent c : components){
                c.y-=speed;
            }
            if(new Random().nextDouble()<.0025*speed){
                add(new MenuComponentPrologueMineShaft());
            }
        }
        if(Sounds.songTimer()>=3073||opacity>0){
            opacity += .01;
            if(opacity>=1){
                gui.open(new MenuMain(gui, true));
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
    }
    @Override
    public void renderBackground(){
        percent = ((Sounds.songTimer()-1921)/1152d)*1.05;
        GL11.glTranslated(0, -yOffset, 0);
        drawRect(0, 0, Display.getWidth(), Display.getHeight(), ImageStash.instance.getTexture("/textures/background/stone.png"));
        drawRect(0, Display.getHeight()*2, Display.getWidth(), Display.getHeight(), ImageStash.instance.getTexture("/textures/background/stone.png"));
        drawRect(0, Display.getHeight()*2, Display.getWidth(), Display.getHeight()*3, ImageStash.instance.getTexture("/textures/background/stone.png"));
        GL11.glTranslated(0, yOffset, 0);
    }
    @Override
    public void render(int millisSinceLastTick){
        super.render(millisSinceLastTick);
        GL11.glColor4d(0, 0, 0, opacity);
        drawRect(0, 0, Display.getWidth(), Display.getHeight(), 0);
        GL11.glColor4d(1, 1, 1, 1);
        for(MenuComponent c : components){
            if(c instanceof MenuComponentCreditsText)c.render(0);
        }
    }
    private static class MenuComponentPrologueMineShaft extends MenuComponent{
        private boolean right = false;
        private boolean added = false;
        public MenuComponentPrologueMineShaft(){
            super(0, 0, 0, 0);
            right = new Random().nextBoolean();
            x = right?Display.getWidth()-new Random().nextInt(400):-new Random().nextInt(400);
            y = new Random().nextInt(Display.getHeight());
            width = 400;
            height = 300;
        }
        public MenuComponentPrologueMineShaft(MenuComponentPrologueMineShaft shaft){
            super(0, 0, 0, 0);
            right = shaft.right;
            x = /*shaft.x+*/(right?-1:1)*shaft.width;
            y = /*shaft.y+*/100;
            width = shaft.width;
            height = shaft.height;
        }
        @Override
        public void tick(){
            super.tick();
            if(y<0||x<-400||x>Display.getWidth()+400||added)return;
            if(new Random().nextDouble()<.02*speed){
                added = true;
                add(new MenuComponentPrologueMineShaft(this));
            }
        }
        @Override
        public void render(){
            removeRenderBound();
            if(right){
                drawRect(x+width, y, x, y+height, ImageStash.instance.getTexture("/textures/mineshaft.png"));
            }else{
                drawRect(x, y, x+width, y+height, ImageStash.instance.getTexture("/textures/mineshaft.png"));
            }
            for(int i = 0; i<5; i++){
                int W = 25;
                int H = 25;
                double X = new Random().nextInt(400-W);
                double Y = 300-X/4-H;
                if(!right){
                    X = -X+width;
                }
                drawRect(x+X, y+Y, x+X+W, y+Y+H, ImageStash.instance.getTexture("/textures/worker.png"));
            }
        }
    }
    private static class MenuComponentCreditsText extends MenuComponent{
        private static final int textHeight = 30;
        private final String text;
        public MenuComponentCreditsText(int line){
            super(0, Display.getHeight(), Display.getWidth(), textHeight);
            text = MenuCredits.credits.get(line);
            color = selectedColor = Color.BLACK;
        }
        @Override
        public void render(){
            String[] texts = text.split("&");
            double wide = width/(double)texts.length;
            for(int i = 0; i<texts.length; i++){
                drawCenteredText(x+wide*i, y, x+wide*(i+1), y+height, texts[i]);
            }
        }
    }
}