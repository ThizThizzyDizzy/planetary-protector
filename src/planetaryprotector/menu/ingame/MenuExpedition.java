package planetaryprotector.menu.ingame;
import com.thizthizzydizzy.dizzyengine.DizzyEngine;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import com.thizthizzydizzy.dizzyengine.ui.component.Button;
import com.thizthizzydizzy.dizzyengine.ui.layout.ConstrainedLayout;
import com.thizthizzydizzy.dizzyengine.ui.layout.constraint.PositionAnchorConstraint;
import planetaryprotector.Controls;
import planetaryprotector.Expedition;
import java.util.ArrayList;
import java.util.HashMap;
import org.joml.Vector2d;
import org.lwjgl.glfw.GLFW;
import planetaryprotector.menu.MenuGame;
public class MenuExpedition extends MenuComponentOverlay{
    private final Button add;
    private final Button remove;
    private final Button back;
    private final Button send;
    private int workers;
    HashMap<Double[], Expedition> rects = new HashMap<>();
    public MenuExpedition(MenuGame game){
        super(game);
        var layout = setLayout(new ConstrainedLayout());
        add = add(new Button("Add worker to expedition", true));
        add.setSize(800, 80);
        remove = add(new Button("Remove worker from expedition", false));
        remove.setSize(800, 80);
        back = add(new Button("Close", true));
        back.setSize(800, 80);
        send = add(new Button("Send Expedition", false));
        send.setSize(800, 80);
        layout.constrain(add, new PositionAnchorConstraint(.5f, 0, .5f, 0, -400, 80));
        layout.constrain(remove, new PositionAnchorConstraint(.5f, 0, .5f, 0, -400, 240));
        layout.constrain(back, new PositionAnchorConstraint(.5f, 0, .5f, 1, -400, -320));
        layout.constrain(send, new PositionAnchorConstraint(.5f, 0, .5f, 1, -400, -160));
        back.addAction(() -> {
            back();
        });
        remove.addAction(() -> {
            workers--;
        });
        send.addAction(() -> {
            menu.game.expedition(workers);
        });
        add.addAction(() -> {
            workers++;
        });
    }
    @Override
    public void draw(double deltaTime){
        super.draw(deltaTime);
        add.enabled = menu.game.workers.size()>workers+1;
        remove.enabled = send.enabled = workers>0;
        if(menu.game.pendingExpedition!=null){
            send.enabled = false;
        }
        textOffset = 0;
        rightTextOffset = 0;
    }
    @Override
    public void render(double deltaTime){
        super.render(deltaTime); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
        rects.clear();
        for(Expedition e : menu.game.finishedExpeditions){
            drawText(e);
        }
        for(Expedition e : menu.game.activeExpeditions){
            drawTextRight(e);
        }
        Renderer.drawCenteredText(0, remove.y+remove.getHeight()+25, DizzyEngine.screenSize.x, back.y-25, workers+"/"+menu.game.workers.size());
        if(close==null)return;
        open(new MenuExpeditionGraph(menu, close));
    }
    Expedition close = null;
    @Override
    public void onMouseButton(int id, Vector2d pos, int button, int action, int mods){
        super.onMouseButton(id, pos, button, action, mods); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
        if(button==0){
            for(Double[] d : rects.keySet()){
                if(x>=d[0]&&y>=d[1]&&x<=d[2]&&y<=d[3]){
                    close = rects.get(d);
                }
            }
        }
    }
    @Override
    public void onKey(int id, int key, int scancode, int action, int mods){
        if(key==Controls.menu&&action==GLFW.GLFW_PRESS){
            back();
        }
    }
    private void back(){
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
    private float textOffset = 0;
    private float rightTextOffset = 0;
    private float textHeight = 20;
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
        double left = add.x+add.getWidth();
        double top = rightTextOffset;
        for(String str : text){
            drawTextRight(str, e);
        }
        double bottom = rightTextOffset;
        double right = DizzyEngine.screenSize.x;
        rects.put(new Double[]{left,top,right,bottom}, e);
    }
    private void drawText(String text, Expedition e){
        String str = Renderer.drawTextWithWrap(0, textOffset, add.x, textOffset+textHeight, text);
        textOffset+=textHeight;
        if(str!=null&&!str.isEmpty()){
            drawText(str, e);
        }
    }
    private void drawTextRight(String text, Expedition e){
        double len = Renderer.getStringWidth(text, textHeight);
        String str = drawTextWithWrap(Math.max(add.x+add.getWidth(), DizzyEngine.screenSize.x-len), rightTextOffset, DizzyEngine.screenSize.x, rightTextOffset+textHeight, text);
        rightTextOffset+=textHeight;
        if(str!=null&&!str.isEmpty()){
            drawTextRight(str, e);
        }
    }
}