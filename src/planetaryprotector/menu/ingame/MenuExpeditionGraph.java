package planetaryprotector.menu.ingame;
import planetaryprotector.Controls;
import planetaryprotector.Expedition;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import planetaryprotector.menu.MenuGame;
import simplelibrary.opengl.ImageStash;
import static simplelibrary.opengl.Renderer2D.drawRect;
import simplelibrary.opengl.gui.components.MenuComponentButton;
public class MenuExpeditionGraph extends MenuComponentOverlay{
    private final MenuComponentButton back;
    private final MenuComponentButton recall;
    private final Expedition e;
    public MenuExpeditionGraph(MenuGame game, Expedition e){
        super(game);
        back = add(new MenuComponentButton(Display.getWidth()/2-400, Display.getHeight()-80, 800, 80, "Back", true));
        recall = add(new MenuComponentButton(Display.getWidth()/2-400, Display.getHeight()-160, 800, 80, e.recalled?"Cancel Recall":"Recall", !e.returned));
        this.e = e;
        game.paused = e.returned;
    }
    @Override
    public void render(){
        recall.enabled = !e.returned;
        drawGraphs(e,0,1,Display.getWidth(),Display.getHeight()-200);
        GL11.glColor4d(1, 1, 1, 1);
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
            open(new MenuExpedition(game));
        }
        if(button==recall){
            e.recalled = !e.recalled;
            if(e.returning!=e.recalled){
                e.returning = e.recalled;
                e.returningGraph.put(e.time, e.recalled);
            }
            recall.label = e.recalled?"Cancel Recall":"Recall";
        }
    }
    private void drawGraphs(Expedition e, int left, int top, int right, int bottom){
        GL11.glColor4d(1, 1, 1, 1);
        ArrayList<Entry<Integer, Integer>> civilianGraph = sort(e.civilianGraph);
        ArrayList<Entry<Integer, Integer>> workerGraph = sort(e.workerGraph);
        //civilians
        ArrayList<double[]> points = new ArrayList<>();
        double xMultiplier = (right-left)/(double)e.totalTime;
        double max = 0;
        for(Entry<Integer, Integer> entry : civilianGraph){
            max = Math.max(max, entry.getValue());
        }
        double yMultiplier = 1;
        try{
            yMultiplier = (bottom-top)/max;
        }catch(ArithmeticException ex){}
        for(Entry<Integer, Integer> entry : civilianGraph){
            points.add(new double[]{entry.getKey(),entry.getValue()});
        }
        GL11.glColor4d(0, 0, 1, 1);
        drawText(left, top, right, top+20, max+"");
        drawText(left, top+bottom/2-10, right, top+bottom/2+10, (max/2)+"");
        drawText(left, bottom-20, right, bottom, "0");
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
        }catch(ArithmeticException ex){}
        for(Entry<Integer, Integer> entry : workerGraph){
            points.add(new double[]{entry.getKey(),entry.getValue()});
        }
        GL11.glColor4d(1, 1, 0, 1);
        drawText(left+100, top, right, top+20, max+"");
        drawText(left+100, top+bottom/2-10, right, top+bottom/2+10, (max/2)+"");
        drawText(left+100, bottom-20, right, bottom, "0");
        GL11.glBegin(GL11.GL_LINES);
        for(int i = 1; i<points.size(); i++){
            double[] point = points.get(i);
            double[] lastPoint = points.get(i-1);
            GL11.glVertex2d(left+point[0]*xMultiplier, top+(max-point[1])*yMultiplier);
            GL11.glVertex2d(left+lastPoint[0]*xMultiplier, top+(max-lastPoint[1])*yMultiplier);
        }
        GL11.glEnd();
        GL11.glColor4d(1, 1, 1, 1);
        //promotions
        ArrayList<Double> locations = new ArrayList<>();
        for(int key : e.civilianPromotionGraph.keySet()){
            locations.add(key+0D);
        }
        for(double d : locations){
            double X = d*xMultiplier;
            GL11.glBegin(GL11.GL_LINES);
            GL11.glVertex2d(left+X, top);
            GL11.glVertex2d(left+X, bottom);
            GL11.glEnd();
            drawRect(X-12.5, top+25, X+12.5, top+50, ImageStash.instance.getTexture("/textures/icons/up.png"));
        }
        //returns
        locations.clear();
        for(int key : e.returningGraph.keySet()){
            locations.add(key+0D);
        }
        for(double d : locations){
            double X = d*xMultiplier;
            GL11.glBegin(GL11.GL_LINES);
            GL11.glVertex2d(left+X, top);
            GL11.glVertex2d(left+X, bottom);
            GL11.glEnd();
            drawRect(X-12.5, top+25, X+12.5, top+50, ImageStash.instance.getTexture("/textures/icons/return.png"));
        }
    }
    private ArrayList<Entry<Integer, Integer>> sort(HashMap<Integer, Integer> graph) {
        TreeMap<Integer, Integer> sorted = new TreeMap<>(graph);
        Set<Entry<Integer, Integer>> mappings = sorted.entrySet();
        return new ArrayList<>(mappings);
    }
}