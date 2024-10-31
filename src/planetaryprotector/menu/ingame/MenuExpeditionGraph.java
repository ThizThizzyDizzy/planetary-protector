package planetaryprotector.menu.ingame;
import com.thizthizzydizzy.dizzyengine.DizzyEngine;
import com.thizthizzydizzy.dizzyengine.ResourceManager;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import com.thizthizzydizzy.dizzyengine.ui.component.Button;
import com.thizthizzydizzy.dizzyengine.ui.layout.ConstrainedLayout;
import com.thizthizzydizzy.dizzyengine.ui.layout.constraint.PositionAnchorConstraint;
import planetaryprotector.Controls;
import planetaryprotector.Expedition;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import planetaryprotector.menu.MenuGame;
public class MenuExpeditionGraph extends MenuComponentOverlay{
    private final Button back;
    private final Button recall;
    private final Expedition e;
    public MenuExpeditionGraph(MenuGame menu, Expedition e){
        super(menu);
        var layout = setLayout(new ConstrainedLayout());
        back = add(new Button("Back", true));
        recall = add(new Button(e.recalled?"Cancel Recall":"Recall", !e.returned));
        back.setSize(800, 80);
        recall.setSize(800, 80);
        layout.constrain(back, new PositionAnchorConstraint(.5f, 0, .5f, 1, -400, -80));
        layout.constrain(recall, new PositionAnchorConstraint(.5f, 0, .5f, -1, -400, -160));
        this.e = e;
        menu.game.paused = e.returned;
        back.addAction(() -> {
            open(new MenuExpedition(menu));
        });
        recall.addAction(() -> {
            e.recalled = !e.recalled;
            if(e.returning!=e.recalled){
                e.returning = e.recalled;
                e.returningGraph.put(e.time, e.recalled);
            }
            recall.label.setLabel(e.recalled?"Cancel Recall":"Recall");
        });
    }
    @Override
    public void draw(double deltaTime){
        recall.enabled = !e.returned;
        drawGraphs(e, 0, 1, DizzyEngine.screenSize.x, DizzyEngine.screenSize.y-200);
        Renderer.setColor(1, 1, 1, 1);
    }
    @Override
    public void onKey(int id, int key, int scancode, int action, int mods){
        if(key==Controls.menu&&action==GLFW.GLFW_PRESS){
            open(new MenuExpedition(menu));
        }
    }
    private void drawGraphs(Expedition e, int left, int top, int right, int bottom){
        Renderer.setColor(1, 1, 1, 1);
        ArrayList<Entry<Integer, Integer>> civilianGraph = sort(e.civilianGraph);
        ArrayList<Entry<Integer, Integer>> workerGraph = sort(e.workerGraph);
        //civilians
        ArrayList<double[]> points = new ArrayList<>();
        float xMultiplier = (right-left)/(float)e.totalTime;
        double max = 0;
        for(Entry<Integer, Integer> entry : civilianGraph){
            max = Math.max(max, entry.getValue());
        }
        double yMultiplier = 1;
        try{
            yMultiplier = (bottom-top)/max;
        }catch(ArithmeticException ex){
        }
        for(Entry<Integer, Integer> entry : civilianGraph){
            points.add(new double[]{entry.getKey(), entry.getValue()});
        }
        Renderer.setColor(0, 0, 1, 1);
        Renderer.drawText(left, top, right, top+20, max+"");
        Renderer.drawText(left, top+bottom/2-10, right, top+bottom/2+10, (max/2)+"");
        Renderer.drawText(left, bottom-20, right, bottom, "0");
        GL11.glBegin(GL11.GL_LINES);
        for(int i = 1; i<points.size(); i++){
            double[] point = points.get(i);
            double[] lastPoint = points.get(i-1);
            GL11.glVertex2d(left+point[0]*xMultiplier, top+(max-point[1])*yMultiplier);
            GL11.glVertex2d(left+lastPoint[0]*xMultiplier, top+(max-lastPoint[1])*yMultiplier);
        }
        GL11.glEnd();
        //workers
        points = new ArrayList<>();
        max = 0;
        for(Entry<Integer, Integer> entry : workerGraph){
            max = Math.max(max, entry.getValue());
        }
        yMultiplier = 1;
        try{
            yMultiplier = (bottom-top)/max;
        }catch(ArithmeticException ex){
        }
        for(Entry<Integer, Integer> entry : workerGraph){
            points.add(new double[]{entry.getKey(), entry.getValue()});
        }
        Renderer.setColor(1, 1, 0, 1);
        Renderer.drawText(left+100, top, right, top+20, max+"");
        Renderer.drawText(left+100, top+bottom/2-10, right, top+bottom/2+10, (max/2)+"");
        Renderer.drawText(left+100, bottom-20, right, bottom, "0");
        GL11.glBegin(GL11.GL_LINES);
        for(int i = 1; i<points.size(); i++){
            double[] point = points.get(i);
            double[] lastPoint = points.get(i-1);
            GL11.glVertex2d(left+point[0]*xMultiplier, top+(max-point[1])*yMultiplier);
            GL11.glVertex2d(left+lastPoint[0]*xMultiplier, top+(max-lastPoint[1])*yMultiplier);
        }
        GL11.glEnd();
        Renderer.setColor(1, 1, 1, 1);
        //promotions
        ArrayList<Float> locations = new ArrayList<>();
        for(int key : e.civilianPromotionGraph.keySet()){
            locations.add(key+0f);
        }
        for(float d : locations){
            float X = d*xMultiplier;
            GL11.glBegin(GL11.GL_LINES);
            GL11.glVertex2d(left+X, top);
            GL11.glVertex2d(left+X, bottom);
            GL11.glEnd();
            Renderer.fillRect(X-12.5f, top+25, X+12.5f, top+50, ResourceManager.getTexture("/textures/icons/up.png"));
        }
        //returns
        locations.clear();
        for(int key : e.returningGraph.keySet()){
            locations.add(key+0f);
        }
        for(float d : locations){
            float X = d*xMultiplier;
            GL11.glBegin(GL11.GL_LINES);
            GL11.glVertex2d(left+X, top);
            GL11.glVertex2d(left+X, bottom);
            GL11.glEnd();
            Renderer.fillRect(X-12.5f, top+25, X+12.5f, top+50, ResourceManager.getTexture("/textures/icons/return.png"));
        }
    }
    private ArrayList<Entry<Integer, Integer>> sort(HashMap<Integer, Integer> graph){
        TreeMap<Integer, Integer> sorted = new TreeMap<>(graph);
        Set<Entry<Integer, Integer>> mappings = sorted.entrySet();
        return new ArrayList<>(mappings);
    }
}
