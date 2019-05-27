package planetaryprotector.menu;
import planetaryprotector.Controls;
import planetaryprotector.Expedition;
import java.util.ArrayList;
import java.util.HashMap;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import planetaryprotector.menu.component.ZComponent;
import simplelibrary.font.FontManager;
import static simplelibrary.opengl.Renderer2D.drawRect;
import simplelibrary.opengl.gui.components.MenuComponent;
import simplelibrary.opengl.gui.components.MenuComponentButton;
public class MenuExpedition extends MenuComponent{
    private final MenuComponentButton add;
    private final MenuComponentButton remove;
    private final MenuComponentButton back;
    private final MenuComponentButton send;
    private final MenuGame game;
    private int workers;
    HashMap<Double[], Expedition> rects = new HashMap<>();
    public MenuExpedition(MenuGame game){
        super(0, 0, Display.getWidth(), Display.getHeight());
        add = add(new MenuComponentButton(Display.getWidth()/2-400, 80, 800, 80, "Add worker to expedition", true));
        remove = add(new MenuComponentButton(Display.getWidth()/2-400, 240, 800, 80, "Remove worker from expedition", false));
        back = add(new MenuComponentButton(Display.getWidth()/2-400, Display.getHeight()-320, 800, 80, "Close", true));
        send = add(new MenuComponentButton(Display.getWidth()/2-400, Display.getHeight()-160, 800, 80, "Send Expedition", false));
        this.game = game;
    }
    @Override
    public void renderBackground(){
        synchronized(game.workers){
            add.enabled = game.workers.size()>workers+1;
        }
        remove.enabled = send.enabled = workers>0;
        if(game.pendingExpedition!=null){
            send.enabled = false;
        }
    }
    @Override
    public void render(){
        textOffset = 0;
        rightTextOffset = 0;
        GL11.glColor4d(0, 0, 0, 0.75);
        drawRect(x, y, width, height, 0);
        GL11.glColor4d(1, 1, 1, 1);
        for(MenuComponent component : components){
            component.render();
        }
        rects.clear();
        for(Expedition e : game.finishedExpeditions){
            drawText(e);
        }
        for(Expedition e : game.activeExpeditions){
            drawTextRight(e);
        }
        synchronized(game.workers){
            drawCenteredText(0, remove.y+remove.height+25, Display.getWidth(), back.y-25, workers+"/"+game.workers.size());
        }
    }
    Expedition close = null;
    @Override
    public void mouseEvent(double x, double y, int button, boolean isDown){
        super.mouseEvent(x, y, button, isDown);
        if(button==0){
            for(Double[] d : rects.keySet()){
                if(isClickWithinBounds(x, y, d[0], d[1], d[2], d[3])){
                    close = rects.get(d);
                }
            }
        }
    }
    @Override
    public void keyboardEvent(char character, int key, boolean pressed, boolean repeat) {
        if(key==Controls.menu&&pressed&&!repeat){
            buttonClicked(back);
        }
    }
    @Override
    public void tick(){
        super.tick();
        if(close==null)return;
        game.components.remove(this);
        game.overlay = game.add(new MenuExpeditionGraph(game, close));
    }
    @Override
    public void buttonClicked(MenuComponentButton button) {
        if(button==back){
            for(Expedition e : game.finishedExpeditions){
                if(!e.returned){
                    continue;
                }
                for(int i = 0; i<e.workers; i++){
                    game.addWorker();
                }
                game.addCivilians(e.civilians);
            }
            game.finishedExpeditions.clear();
            game.componentsToRemove.add(this);
            game.overlay = null;
        }
        if(button==remove){
            workers--;
        }
        if(button==send){
            game.expedition(workers);
        }
        if(button==add){
            workers++;
        }
    }
    private double textOffset = 0;
    private double rightTextOffset = 0;
    private double textHeight = 20;
    private void drawText(Expedition e){
        drawText(e.getText(), e);
    }
    private void drawTextRight(Expedition e){
        drawTextRight(e.getText(), e);
    }
    private void drawText(ArrayList<String> text, Expedition e){
        double left = 0;
        double top = textOffset;
        for(String str : text){
            drawText(str, e);
        }
        double bottom = textOffset;
        double right = add.x;
        rects.put(new Double[]{left,top,right,bottom}, e);
    }
    private void drawTextRight(ArrayList<String> text, Expedition e){
        double left = add.x+add.width;
        double top = rightTextOffset;
        for(String str : text){
            drawTextRight(str, e);
        }
        double bottom = rightTextOffset;
        double right = Display.getWidth();
        rects.put(new Double[]{left,top,right,bottom}, e);
    }
    private void drawText(String text, Expedition e){
        String str = drawTextWithWrap(0, textOffset, add.x, textOffset+textHeight, text);
        textOffset+=textHeight;
        if(str!=null&&!str.isEmpty()){
            drawText(str, e);
        }
    }
    private void drawTextRight(String text, Expedition e){
        double len = FontManager.getLengthForStringWithHeight(text, textHeight);
        String str = drawTextWithWrap(Math.max(add.x+add.width, Display.getWidth()-len), rightTextOffset, Display.getWidth(), rightTextOffset+textHeight, text);
        rightTextOffset+=textHeight;
        if(str!=null&&!str.isEmpty()){
            drawTextRight(str, e);
        }
    }
}