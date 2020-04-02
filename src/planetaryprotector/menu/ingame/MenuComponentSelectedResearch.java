package planetaryprotector.menu.ingame;
import java.util.Random;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import planetaryprotector.Core;
import planetaryprotector.building.Laboratory;
import planetaryprotector.item.ItemStack;
import planetaryprotector.game.Game;
import planetaryprotector.research.DiscoveryPrerequisite;
import planetaryprotector.research.Research;
import planetaryprotector.research.Research.ResearchCategory;
import planetaryprotector.research.lang.HorizontalLang;
import simplelibrary.game.Framebuffer;
import simplelibrary.opengl.ImageStash;
import static simplelibrary.opengl.Renderer2D.drawRect;
import simplelibrary.opengl.gui.components.MenuComponent;
import simplelibraryextended.opengl.AdvancedRenderer2D;
public class MenuComponentSelectedResearch extends MenuComponent{
    public Research research = null;
    public HorizontalLang title = new HorizontalLang();
    private static final int[] nonsense = {4,4};
    private final Laboratory lab;
    private final Game game;
    public MenuComponentSelectedResearch(Laboratory lab, double x, double y, double width, double height){
        super(x, y, width, height);
        this.lab = lab;
        this.game = lab.game;
    }
    @Override
    public void render(){
        if(research==null)return;
        double borderThickness = Math.min(width, height)/30;
        double border = borderThickness/.4;
        int quality = 36;
        GL11.glColor4d(.95, .95, .95, 1);
        drawRect(x+border, y, x+width-border, y+height, 0);
        drawRect(x, y+border, x+width, y+height-border, 0);
        Core.drawOval(x+border, y+border, border, border, border, quality, 0, 27, 36);
        Core.drawOval(x+width-border, y+border, border, border, border, quality, 0, 0, 9);
        Core.drawOval(x+width-border, y+height-border, border, border, border, quality, 0, 9, 18);
        Core.drawOval(x+border, y+height-border, border, border, border, quality, 0, 18, 27);
        GL11.glColor4d(.75, .75, .75, 1);
        Core.drawOval(x+border, y+border, border, border, borderThickness, quality, 0, 27, 36);
        Core.drawOval(x+width-border, y+border, border, border, borderThickness, quality, 0, 0, 9);
        Core.drawOval(x+width-border, y+height-border, border, border, borderThickness, quality, 0, 9, 18);
        Core.drawOval(x+border, y+height-border, border, border, borderThickness, quality, 0, 18, 27);
        drawRect(x+border, y, x+width-border, y+borderThickness, 0);
        drawRect(x+width-borderThickness, y+border, x+width, y+height-border, 0);
        drawRect(x+border, y+height-borderThickness, x+width-border, y+height, 0);
        drawRect(x, y+border, x+borderThickness, y+height-border, 0);
        drawRect(x+border, y+borderThickness, height, border, quality);
        GL11.glColor4d(.05, .05, .05, 1);
        title.drawTranslation(x+width/2, y+borderThickness+width*.075, width/20, research.fancyTitle);
        drawCenteredText(x+border+width*.3, y+borderThickness+width*.15, x+width-border, y+borderThickness+width*.15+40, research.getTitle());
        if(research.isDiscovered()){
            drawDescription(x+border+width*.3, y+borderThickness+width*.15+40, x+width-border, y+borderThickness+width*.15+40+width*.3, x+border, x+width-border, y+height-border-borderThickness, 40, research.getDescription());
        }else{
            if(game.cheats){
                drawDescription(x+border+width*.3, y+borderThickness+width*.15+40, x+width-border, y+borderThickness+width*.15+40+width*.3, x+border, x+width-border, y+height-border-borderThickness, 40, research.getDiscoveryStage().getProgressDescription(game));
            }else{
                drawNonsense(x+border, y+borderThickness+width*.15+40, x+width-border, y+height-border-borderThickness, 40);
            }
        }
        GL11.glColor4d(1, 1, 1, 1);
        drawTheImageAndItsBorder(x+border, y+borderThickness+width*.15+40, x+border+width/4, y+borderThickness+width*.15+40+width/4);
        if(!research.isDiscovered()){
            drawProgressBar(x+border, y+height-border-borderThickness, x+width-border, y+height-border, research.getPercentDone());
            double total = research.getDiscoveryStage().prerequisites.size();
            double wide = width-border*2;
            for(int i = 0; i<total; i++){
                DiscoveryPrerequisite pre = research.getDiscoveryStage().prerequisites.get(i);
                drawProgressBar(x+border+(wide*(i/total)), y+height-border-borderThickness*1.5, x+border+(wide*((i+1)/total)), y+height-border-borderThickness, pre.progress);
            }
        }else if(!research.isCompleted()){
            int num = research.itemCosts.length+(research.totalPowerCost>0?1:0)+(research.totalStarlightCost>0?1:0);
            double wide = width-border*2;
            double w = wide/num;
            for(int i = 0; i<num; i++){
                int textHeight = (int) (borderThickness*1.5)/10*10;
                if(research.totalPowerCost>0&&i==0){
                    drawText(x+border+w*i, y+height-border-borderThickness-textHeight, x+border+w*(i+1), y+height-border-borderThickness, "Power: "+research.powerCost);
                    continue;
                }
                if(research.totalStarlightCost>0&&((research.totalPowerCost>0&&i==1)||(research.totalPowerCost<=0&&i==0))){
                    drawText(x+border+w*i, y+height-border-borderThickness-textHeight, x+border+w*(i+1), y+height-border-borderThickness, "Starlight: "+research.starlightCost);
                    continue;
                }
                int offset = (research.totalPowerCost>0?1:0)+(research.totalStarlightCost>0?1:0);
                ItemStack stack = research.itemCosts[i-offset];
                drawRect(x+border+w*i, y+height-border-borderThickness-textHeight, x+border+w*i+textHeight, y+height-border-borderThickness, stack.item.getTexture());
                drawText(x+border+w*i+textHeight, y+height-border-borderThickness-textHeight, x+border+w*(i+1), y+height-border-borderThickness, stack.count+"");
            }
            drawProgressBar(x+border, y+height-border-borderThickness, x+width-border, y+height-border, 1-(research.time/(double)research.totalTime));
            drawCenteredText(x+border, y+height-border-borderThickness+3, x+width-border, y+height-border-3, lab.targetResearch==research?"Stop Researching":"Research");
        }
        if(research.isCompleted()&&research.category==ResearchCategory.BUILDING_UPGRADES){
            drawCenteredText(x+border, y+height-border-borderThickness*2.5+3, x+width-border, y+height-borderThickness*1.5-3, research.upgrade.getConfiguration());
        }
    }
    @Override
    public void mouseEvent(int button, boolean pressed, float x, float y, float xChange, float yChange, int wheelChange){
        super.mouseEvent(button, pressed, x, y, xChange, yChange, wheelChange);
        if(button==0&&pressed&&game.cheats&&Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)&&Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
            if(lab.targetResearch==research)lab.setTargetResearch(null);
            if(!research.isDiscovered()){
                research.cheatDiscover();
            }else if(!research.isCompleted()){
                research.complete(game);
            }
            return;
        }
        if(button==0&&pressed&&research.isDiscovered()&&!research.isCompleted()){
            if(lab.targetResearch==research)lab.setTargetResearch(null);
            else lab.setTargetResearch(research);
        }
    }
    private void drawDescription(double left1, double top, double right1, double yDivider, double left2, double right2, double bottom, double textHeight, String description) {
        double Y = top;
        description = description.replace("\n", "                                                                                                    ");
        while(description!=null&&!description.trim().isEmpty()){
            description = AdvancedRenderer2D.drawTextWithWordWrap(Y>yDivider?left2:left1, Y, Y>yDivider?right2:right1, Y+textHeight, description.trim());
            Y+=textHeight*1.2;
            if(Y>=bottom)break;
        }
    }
    private void drawNonsense(double left, double top, double right, double bottom, double textHeight){
        int charsX = (int) ((right-left)/(textHeight*2));
        int charsY = (int) ((bottom-top)/(textHeight*2));
        left += Math.abs((charsX*textHeight*2-textHeight)-(right-left))/2;
        top += Math.abs((charsY*textHeight*2-textHeight)-(bottom-top))/2;
        int texture = ImageStash.instance.getTexture("/textures/research/nonsense.png");
        Random r = new Random(research.getSeed());
        for(int X = 0; X<charsX; X++){
            for(int Y = 0; Y<charsY; Y++){
                double nonsenseX = r.nextInt(nonsense[0]);
                double nonsenseY = r.nextInt(nonsense[1]);
                double texLeft = nonsenseX/nonsense[0];
                double texTop = nonsenseY/nonsense[1];
                double texRight = (nonsenseX+1)/nonsense[0];
                double texDown = (nonsenseY+1)/nonsense[1];
                drawRect(left+X*textHeight*2, top+Y*textHeight*2, left+X*textHeight*2+textHeight, top+Y*textHeight*2+textHeight, texture, texLeft, texTop, texRight, texDown);
            }
        }
    }
    private void drawTheImageAndItsBorder(double left, double top, double right, double bottom){
        double borderThickness = (right-left)/11;
        left-=borderThickness;
        top-=borderThickness;
        right+=borderThickness;
        bottom+=borderThickness;
        double border = borderThickness/.4;
        int quality = 36;
        GL11.glColor4d(.95, .95, .95, 1);
        drawRect(left+border, top, right-border, bottom, 0);
        drawRect(left, top+border, right, bottom-border, 0);
        Core.drawOval(left+border, top+border, border, border, border, quality, 0, 27, 36);
        Core.drawOval(right-border, top+border, border, border, border, quality, 0, 0, 9);
        Core.drawOval(right-border, bottom-border, border, border, border, quality, 0, 9, 18);
        Core.drawOval(left+border, bottom-border, border, border, border, quality, 0, 18, 27);
        GL11.glColor4d(1, 1, 1, 1);
        drawRect(left+borderThickness, top+borderThickness, right-borderThickness, bottom-borderThickness, research.getTexture());
        GL11.glColor4d(.75, .75, .75, 1);
        Core.drawOval(left+border, top+border, border, border, borderThickness, quality, 0, 27, 36);
        Core.drawOval(right-border, top+border, border, border, borderThickness, quality, 0, 0, 9);
        Core.drawOval(right-border, bottom-border, border, border, borderThickness, quality, 0, 9, 18);
        Core.drawOval(left+border, bottom-border, border, border, borderThickness, quality, 0, 18, 27);
        drawRect(left+border, top, right-border, top+borderThickness, 0);
        drawRect(right-borderThickness, top+border, right, bottom-border, 0);
        drawRect(left+border, bottom-borderThickness, right-border, bottom, 0);
        drawRect(left, top+border, left+borderThickness, bottom-border, 0);
    }
    private void drawProgressBar(double left, double top, double right, double bottom, double percent){
        Framebuffer filled = new Framebuffer(Core.helper, null, (int)(right-left), (int)(bottom-top));
        drawProgressBar(left, top, right, bottom, false);
        filled.bindRenderTarget2D();
        drawProgressBar(0, 0, filled.width, filled.height, true);
        filled.releaseRenderTarget();
        GL11.glColor4d(1, 1, 1, 1);
        drawRectWithBounds(left, top, right, bottom, left, top, left+percent*(right-left), bottom, filled.getTexture());
    }
    private void drawProgressBar(double left, double top, double right, double bottom, boolean filled){
        if(right-left<bottom-top){
            throw new IllegalArgumentException("Cannnot draw progress bar! It's taller than it is wide!");
        }
        double r = (bottom-top)/2;
        //draw fill
        if(filled)GL11.glColor4d(.5, .9, 1, 1);
        else GL11.glColor4d(.3, .3, .6, 1);
        Core.drawRegularPolygon(left+r, top+r, r, 100, 0);
        Core.drawRegularPolygon(right-r, top+r, r, 100, 0);
        Core.drawRect(left+r, top, right-r, bottom, 0);
        //draw border
        GL11.glColor4d(.7, .74, .75, 1);
        Core.drawOval(left+r, top+r, r, r, (bottom-top)/10, 100, 0, 50, 100);
        Core.drawOval(right-r, top+r, r, r, (bottom-top)/10, 100, 0, 0, 50);
        drawRect(left+r, top, right-r, top+(bottom-top)/10, 0);
        drawRect(left+r, bottom-(bottom-top)/10, right-r, bottom, 0);
    }
}