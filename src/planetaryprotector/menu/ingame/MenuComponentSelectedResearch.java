package planetaryprotector.menu.ingame;
import com.thizthizzydizzy.dizzyengine.Framebuffer;
import com.thizthizzydizzy.dizzyengine.MathUtil;
import com.thizthizzydizzy.dizzyengine.ResourceManager;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import com.thizthizzydizzy.dizzyengine.ui.component.Component;
import java.util.Random;
import org.joml.Vector2d;
import org.lwjgl.glfw.GLFW;
import planetaryprotector.structure.Laboratory;
import planetaryprotector.item.ItemStack;
import planetaryprotector.game.Game;
import planetaryprotector.research.DiscoveryPrerequisite;
import planetaryprotector.research.DiscoveryStage;
import planetaryprotector.research.Research;
import planetaryprotector.research.Research.ResearchCategory;
import planetaryprotector.research.lang.HorizontalLang;
public class MenuComponentSelectedResearch extends Component{
    public Research research = null;
    public HorizontalLang title = new HorizontalLang();
    private static final int[] nonsense = {4, 4};
    private final Laboratory lab;
    private final Game game;
    public MenuComponentSelectedResearch(Laboratory lab, float x, float y, float width, float height){
        this.x = x;
        this.y = y;
        setSize(width, height);
        this.lab = lab;
        this.game = lab.game;
    }
    @Override
    public void draw(double deltaTime){
        if(research==null)return;
        float borderThickness = Math.min(getWidth(), getHeight())/30;
        float border = borderThickness/.4f;
        int quality = 36;
        Renderer.setColor(.95f, .95f, .95f, 1);
        Renderer.fillRect(x+border, y, x+getWidth()-border, y+getHeight(), 0);
        Renderer.fillRect(x, y+border, x+getWidth(), y+getHeight()-border, 0);
        Renderer.fillHollowRegularPolygonSegment(x+border, y+border, quality, border-border, border-border, border, border, 27, 36);
        Renderer.fillHollowRegularPolygonSegment(x+getWidth()-border, y+border, quality, border-border, border-border, border, border, 0, 9);
        Renderer.fillHollowRegularPolygonSegment(x+getWidth()-border, y+getHeight()-border, quality, border-border, border-border, border, border, 9, 18);
        Renderer.fillHollowRegularPolygonSegment(x+border, y+getHeight()-border, quality, border-border, border-border, border, border, 18, 27);
        Renderer.setColor(.75f, .75f, .75f, 1);
        Renderer.fillHollowRegularPolygonSegment(x+border, y+border, quality, border-borderThickness, border-borderThickness, border, border, 27, 36);
        Renderer.fillHollowRegularPolygonSegment(x+getWidth()-border, y+border, quality, border-borderThickness, border-borderThickness, border, border, 0, 9);
        Renderer.fillHollowRegularPolygonSegment(x+getWidth()-border, y+getHeight()-border, quality, border-borderThickness, border-borderThickness, border, border, 9, 18);
        Renderer.fillHollowRegularPolygonSegment(x+border, y+getHeight()-border, quality, border-borderThickness, border-borderThickness, border, border, 18, 27);
        Renderer.fillRect(x+border, y, x+getWidth()-border, y+borderThickness, 0);
        Renderer.fillRect(x+getWidth()-borderThickness, y+border, x+getWidth(), y+getHeight()-border, 0);
        Renderer.fillRect(x+border, y+getHeight()-borderThickness, x+getWidth()-border, y+getHeight(), 0);
        Renderer.fillRect(x, y+border, x+borderThickness, y+getHeight()-border, 0);
        Renderer.fillRect(x+border, y+borderThickness, getHeight(), border, quality);
        Renderer.setColor(.05f, .05f, .05f, 1);
        title.drawTranslation(x+getWidth()/2, y+borderThickness+getWidth()*.075f, getWidth()/20, research.fancyTitle);
        Renderer.drawCenteredText(x+border+getWidth()*.3f, y+borderThickness+getWidth()*.15f, x+getWidth()-border, y+borderThickness+getWidth()*.15f+40, research.getTitle());
        if(research.isDiscovered()){
            drawDescription(x+border+getWidth()*.3, y+borderThickness+getWidth()*.15+40, x+getWidth()-border, y+borderThickness+getWidth()*.15+40+getWidth()*.3, x+border, x+getWidth()-border, y+getHeight()-border-borderThickness, 40, research.getDescription());
        }else{
            if(game.cheats){
                drawDescription(x+border+getWidth()*.3, y+borderThickness+getWidth()*.15+40, x+getWidth()-border, y+borderThickness+getWidth()*.15+40+getWidth()*.3, x+border, x+getWidth()-border, y+getHeight()-border-borderThickness, 40, research.getDiscoveryStage().getProgressDescription(game));
            }else{
                drawNonsense(x+border, y+borderThickness+getWidth()*.15f+40, x+getWidth()-border, y+getHeight()-border-borderThickness, 40);
            }
        }
        Renderer.setColor(1, 1, 1, 1);
        drawTheImageAndItsBorder(x+border, y+borderThickness+getWidth()*.15f+40, x+border+getWidth()/4, y+borderThickness+getWidth()*.15f+40+getWidth()/4);
        if(!research.isDiscovered()){
            drawProgressBar(x+border, y+getHeight()-border-borderThickness, x+getWidth()-border, y+getHeight()-border, (float)research.getPercentDone());
            DiscoveryStage stage = research.getDiscoveryStage();
            if(stage!=null){//not sure why stage is sometimes null; probably a race condition
                float total = stage.prerequisites.size();
                float wide = getWidth()-border*2;
                for(int i = 0; i<total; i++){
                    DiscoveryPrerequisite pre = stage.prerequisites.get(i);
                    drawProgressBar(x+border+(wide*(i/total)), y+getHeight()-border-borderThickness*1.5f, x+border+(wide*((i+1)/total)), y+getHeight()-border-borderThickness, (float)pre.progress);
                }
            }
        }else if(!research.isCompleted()){
            int num = research.itemCosts.length+(research.totalPowerCost>0?1:0)+(research.totalStarlightCost>0?1:0);
            float wide = getWidth()-border*2;
            float w = wide/num;
            for(int i = 0; i<num; i++){
                int textHeight = (int)(borderThickness*1.5)/10*10;
                if(research.totalPowerCost>0&&i==0){
                    Renderer.drawText(x+border+w*i, y+getHeight()-border-borderThickness-textHeight, x+border+w*(i+1), y+getHeight()-border-borderThickness, "Power: "+research.powerCost);
                    continue;
                }
                if(research.totalStarlightCost>0&&((research.totalPowerCost>0&&i==1)||(research.totalPowerCost<=0&&i==0))){
                    Renderer.drawText(x+border+w*i, y+getHeight()-border-borderThickness-textHeight, x+border+w*(i+1), y+getHeight()-border-borderThickness, "Starlight: "+research.starlightCost);
                    continue;
                }
                int offset = (research.totalPowerCost>0?1:0)+(research.totalStarlightCost>0?1:0);
                ItemStack stack = research.itemCosts[i-offset];
                Renderer.fillRect(x+border+w*i, y+getHeight()-border-borderThickness-textHeight, x+border+w*i+textHeight, y+getHeight()-border-borderThickness, stack.item.getTexture());
                Renderer.drawText(x+border+w*i+textHeight, y+getHeight()-border-borderThickness-textHeight, x+border+w*(i+1), y+getHeight()-border-borderThickness, stack.count+"");
            }
            drawProgressBar(x+border, y+getHeight()-border-borderThickness, x+getWidth()-border, y+getHeight()-border, 1-(research.time/(float)research.totalTime));
            Renderer.drawCenteredText(x+border, y+getHeight()-border-borderThickness+3, x+getWidth()-border, y+getHeight()-border-3, lab.targetResearch==research?"Stop Researching":"Research");
        }
        if(research.isCompleted()&&research.category==ResearchCategory.BUILDING_UPGRADES){
            Renderer.drawCenteredText(x+border, y+getHeight()-border-borderThickness*2.5f+3, x+getWidth()-border, y+getHeight()-borderThickness*1.5f-3, research.upgrade.getConfiguration());
        }
    }
    @Override
    public void onMouseButton(int id, Vector2d pos, int button, int action, int mods){
        super.onMouseButton(id, pos, button, action, mods); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
        if(button==0&&action==GLFW.GLFW_PRESS&&game.cheats&&mods==(GLFW.GLFW_MOD_CONTROL&GLFW.GLFW_MOD_SHIFT)){
            if(lab.targetResearch==research)lab.setTargetResearch(null);
            if(!research.isDiscovered()){
                research.cheatDiscover();
            }else if(!research.isCompleted()){
                research.complete(game);
            }
            return;
        }
        if(button==0&&action==GLFW.GLFW_PRESS&&research.isDiscovered()&&!research.isCompleted()){
            if(lab.targetResearch==research)lab.setTargetResearch(null);
            else
                lab.setTargetResearch(research);
        }
    }
    private void drawDescription(double left1, double top, double right1, double yDivider, double left2, double right2, double bottom, double textHeight, String description){
        double Y = top;
        description = description.replace("\n", "                                                                                                    ");
        while(description!=null&&!description.trim().isEmpty()){
            description = Renderer.drawTextWithWordWrap(Y>yDivider?left2:left1, Y, Y>yDivider?right2:right1, Y+textHeight, description.trim());
            Y += textHeight*1.2;
            if(Y>=bottom)break;
        }
    }
    private void drawNonsense(float left, float top, float right, float bottom, float textHeight){
        int charsX = (int)((right-left)/(textHeight*2));
        int charsY = (int)((bottom-top)/(textHeight*2));
        left += Math.abs((charsX*textHeight*2-textHeight)-(right-left))/2;
        top += Math.abs((charsY*textHeight*2-textHeight)-(bottom-top))/2;
        int texture = ResourceManager.getTexture("/textures/research/nonsense.png");
        Random r = new Random(research.getSeed());
        for(int X = 0; X<charsX; X++){
            for(int Y = 0; Y<charsY; Y++){
                float nonsenseX = r.nextInt(nonsense[0]);
                float nonsenseY = r.nextInt(nonsense[1]);
                float texLeft = nonsenseX/nonsense[0];
                float texTop = nonsenseY/nonsense[1];
                float texRight = (nonsenseX+1)/nonsense[0];
                float texDown = (nonsenseY+1)/nonsense[1];
                Renderer.fillRect(left+X*textHeight*2, top+Y*textHeight*2, left+X*textHeight*2+textHeight, top+Y*textHeight*2+textHeight, texture, texLeft, texTop, texRight, texDown);
            }
        }
    }
    private void drawTheImageAndItsBorder(float left, float top, float right, float bottom){
        float borderThickness = (right-left)/11;
        left -= borderThickness;
        top -= borderThickness;
        right += borderThickness;
        bottom += borderThickness;
        float border = borderThickness/.4f;
        int quality = 36;
        Renderer.setColor(.95f, .95f, .95f, 1);
        Renderer.fillRect(left+border, top, right-border, bottom, 0);
        Renderer.fillRect(left, top+border, right, bottom-border, 0);
        Renderer.fillHollowRegularPolygonSegment(left+border, top+border, quality, border-border, border-border, border, border, 27, 36);
        Renderer.fillHollowRegularPolygonSegment(right-border, top+border, quality, border-border, border-border, border, border, 0, 9);
        Renderer.fillHollowRegularPolygonSegment(right-border, bottom-border, quality, border-border, border-border, border, border, 9, 18);
        Renderer.fillHollowRegularPolygonSegment(left+border, bottom-border, quality, border-border, border-border, border, border, 18, 27);
        Renderer.setColor(1, 1, 1, 1);
        Renderer.fillRect(left+borderThickness, top+borderThickness, right-borderThickness, bottom-borderThickness, research.getTexture());
        Renderer.setColor(.75f, .75f, .75f, 1);
        Renderer.fillHollowRegularPolygonSegment(left+border, top+border, quality, border-borderThickness, border-borderThickness, border, border, 27, 36);
        Renderer.fillHollowRegularPolygonSegment(right-border, top+border, quality, border-borderThickness, border-borderThickness, border, border, 0, 9);
        Renderer.fillHollowRegularPolygonSegment(right-border, bottom-border, quality, border-borderThickness, border-borderThickness, border, border, 9, 18);
        Renderer.fillHollowRegularPolygonSegment(left+border, bottom-border, quality, border-borderThickness, border-borderThickness, border, border, 18, 27);
        Renderer.fillRect(left+border, top, right-border, top+borderThickness, 0);
        Renderer.fillRect(right-borderThickness, top+border, right, bottom-border, 0);
        Renderer.fillRect(left+border, bottom-borderThickness, right-border, bottom, 0);
        Renderer.fillRect(left, top+border, left+borderThickness, bottom-border, 0);
    }
    private void drawProgressBar(float left, float top, float right, float bottom, float percent){
        Framebuffer filled = new Framebuffer((int)(right-left), (int)(bottom-top));
        drawProgressBar(left, top, right, bottom, false);
        filled.bind();
        drawProgressBar(0, 0, filled.width, filled.height, true);
        //TODO unbind
        Renderer.setColor(1, 1, 1, 1);
        Renderer.bound(left, top, MathUtil.lerp(left, right, percent), bottom);
        Renderer.fillRect(left, top, right, bottom, filled.texture);
        Renderer.unBound();
    }
    private void drawProgressBar(float left, float top, float right, float bottom, boolean filled){
        if(right-left<bottom-top){
            throw new IllegalArgumentException("Cannnot draw progress bar! It's taller than it is wide!");
        }
        float r = (bottom-top)/2;
        //draw fill
        if(filled)Renderer.setColor(.5f, .9f, 1, 1);
        else
            Renderer.setColor(.3f, .3f, .6f, 1);
        Renderer.fillRegularPolygon(left+r, top+r, 100, r);
        Renderer.fillRegularPolygon(right-r, top+r, 100, r);
        Renderer.fillRect(left+r, top, right-r, bottom, 0);
        //draw border
        Renderer.setColor(.7f, .74f, .75f, 1);
        Renderer.fillHollowRegularPolygonSegment(left+r, top+r, 100, r-(bottom-top)/10, r-(bottom-top)/10, r, r, 50, 100);
        Renderer.fillHollowRegularPolygonSegment(right-r, top+r, 100, r-(bottom-top)/10, r-(bottom-top)/10, r, r, 0, 50);
        Renderer.fillRect(left+r, top, right-r, top+(bottom-top)/10, 0);
        Renderer.fillRect(left+r, bottom-(bottom-top)/10, right-r, bottom, 0);
    }
}
