package planetaryprotector.menu.ingame;
import planetaryprotector.Controls;
import planetaryprotector.Expedition;
import java.util.ArrayList;
import java.util.HashMap;
import planetaryprotector.Core;
import planetaryprotector.menu.MenuGame;
import simplelibrary.font.FontManager;
import simplelibrary.opengl.gui.components.MenuComponentButton;
public class MenuExpedition extends MenuComponentOverlay{
    private final MenuComponentButton add;
    private final MenuComponentButton remove;
    private final MenuComponentButton back;
    private final MenuComponentButton send;
    private int workers;
    HashMap<Double[], Expedition> rects = new HashMap<>();
    public MenuExpedition(MenuGame game){
        super(game);
        add = add(new MenuComponentButton(DizzyEngine.screenSize.x/2-400, 80, 800, 80, "Add worker to expedition", true));
        remove = add(new MenuComponentButton(DizzyEngine.screenSize.x/2-400, 240, 800, 80, "Remove worker from expedition", false));
        back = add(new MenuComponentButton(DizzyEngine.screenSize.x/2-400, DizzyEngine.screenSize.y-320, 800, 80, "Close", true));
        send = add(new MenuComponentButton(DizzyEngine.screenSize.x/2-400, DizzyEngine.screenSize.y-160, 800, 80, "Send Expedition", false));
    }
    @Override
    public void renderBackground(){
        super.renderBackground();
        add.enabled = menu.game.workers.size()>workers+1;
        remove.enabled = send.enabled = workers>0;
        if(menu.game.pendingExpedition!=null){
            send.enabled = false;
        }
        textOffset = 0;
        rightTextOffset = 0;
    }
    @Override
    public void render(){
        rects.clear();
        for(Expedition e : menu.game.finishedExpeditions){
            drawText(e);
        }
        for(Expedition e : menu.game.activeExpeditions){
            drawTextRight(e);
        }
        drawCenteredText(0, remove.y+remove.height+25, DizzyEngine.screenSize.x, back.y-25, workers+"/"+menu.game.workers.size());
    }
    Expedition close = null;
    @Override
    public void onMouseButton(double x, double y, int button, boolean pressed, int mods){
        super.onMouseButton(x, y, button, pressed, mods);
        if(button==0){
            for(Double[] d : rects.keySet()){
                if(isClickWithinBounds(x, y, d[0], d[1], d[2], d[3])){
                    close = rects.get(d);
                }
            }
        }
    }
    @Override
    public void keyEvent(int key, int scancode, boolean isPress, boolean isRepeat, int modifiers){
        if(key==Controls.menu&&isPress&&!isRepeat){
            buttonClicked(back);
        }
    }
    @Override
    public void tick(){
        super.tick();
        if(close==null)return;
        open(new MenuExpeditionGraph(menu, close));
    }
    @Override
    public void buttonClicked(MenuComponentButton button) {
        if(button==back){
            for(Expedition e : menu.game.finishedExpeditions){
                if(!e.returned){
                    continue;
                }
                for(int i = 0; i<e.workers; i++){
                    menu.game.addWorker();
                }
                menu.game.addCivilians(e.civilians);
            }
            menu.game.finishedExpeditions.clear();
            close();
        }
        if(button==remove){
            workers--;
        }
        if(button==send){
            menu.game.expedition(workers);
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
        double right = DizzyEngine.screenSize.x;
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
        String str = drawTextWithWrap(Math.max(add.x+add.width, DizzyEngine.screenSize.x-len), rightTextOffset, DizzyEngine.screenSize.x, rightTextOffset+textHeight, text);
        rightTextOffset+=textHeight;
        if(str!=null&&!str.isEmpty()){
            drawTextRight(str, e);
        }
    }
}