package planetaryprotector.menu;
import planetaryprotector.game.Game;
import java.util.ArrayList;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import planetaryprotector.Main;
import simplelibrary.opengl.ImageStash;
import simplelibrary.opengl.gui.GUI;
import simplelibrary.opengl.gui.Menu;
import simplelibrary.opengl.gui.components.MenuComponentButton;
public class MenuCredits extends Menu{
    private final MenuComponentButton back;
    public static final ArrayList<String> credits = new ArrayList<>();
    static{
        credits.add("Project Manager");
        credits.add("");
        credits.add("ThizThizzyDizzy");
        credits.add("");
        credits.add("");
        credits.add("Producer");
        credits.add("");
        credits.add("ThizThizzyDizzy");
        credits.add("");
        credits.add("");
        credits.add("Lead Designer");
        credits.add("");
        credits.add("ThizThizzyDizzy");
        credits.add("");
        credits.add("");
        credits.add("Lead Programmer");
        credits.add("");
        credits.add("ThizThizzyDizzy");
        credits.add("");
        credits.add("Other Programmers");
        credits.add("");
        credits.add("Bryan Dolan");
        credits.add("");
        credits.add("");
        credits.add("Graphic Designers");
        credits.add("");
        credits.add("ThizThizzyDizzy");
        credits.add("");
        credits.add("");
        credits.add("Game Testers");
        credits.add("");
        credits.add("ThizThizzyDizzy");
        credits.add("Bryan Dolan");
        credits.add("");
        credits.add("");
        credits.add("Music");
        credits.add("");
        credits.add("Meteor Shower Music");
        credits.add("\"Killers\"");
        credits.add("");
        credits.add("Normal Play Music");
        credits.add("\"Clenched Teeth\"&\"Achilles\"");
        credits.add("\"Noble Race\"&\"Rynos Theme\"");
        credits.add("");
        credits.add("Boss Fight Music");
        credits.add("Phase 1&Phase 2&Phase 3&Phase 4");
        credits.add("\"Eternal Terminal\"&\"Corruption\"&\"Metalmania\"&\"Death and Axes\"");
        credits.add("\"Five Armies\"&\"Clash Defiant\"&\"Obliteration\"&\"Grim Idol\"");
        credits.add("");
        credits.add("Game Over Music");
        credits.add("\"A Turn for the Worse\"&\"Summon the Rawk\"&\"A Little Faith\"");
        credits.add("\"At Rest\"&\"Awaiting Return\"&\"Bittersweet\"");
        credits.add("\"Colorless Aura\"&\"Cryptic Sorrow\"&\"Dark Times\"");
        credits.add("\"Dark Walk\"&\"Despair and Triumph\"&\"Disquiet\"");
        credits.add("\"End of the Era\"&\"Heartbreaking\"&\"Heavy Heart\"");
        credits.add("\"Immersed\"&\"Lasting Hope\"&\"Lone Harvest\"");
        credits.add("\"Lost Frontier\"&\"Memory Lane\"&\"On the Passing of Time\"");
        credits.add("\"Rains Will Fall\"&\"Reaching Out\"&\"Sad Trio\"");
        credits.add("\"Stages of Grief\"&\"The Parting\"&\"Time Passes\"");
        credits.add("\"When the Wind Blows\"&\"Wounded\"");
        credits.add("");
        credits.add("Win Music&Victory Music");
        credits.add("\"Americana\"&\"Jet Fueled Vixen\"");
        credits.add("");
        credits.add("Suspense Music");
        credits.add("\"Final Battle of the Dark Wizards\"");
        credits.add("");
        credits.add("Mystery Music");
        credits.add("\"Almost New\"&\"Constancy Part Three\"&\"Earth Prelude\"");
        credits.add("\"Floating Cities\"&\"Frost Waltz\"&\"Frost Waltz (Alternate)\"");
        credits.add("\"The Chamber\"&\"The Other Side of the Door\"&\"The Snow Queen\"");
        credits.add("\"Comfortable Mystery\"&\"Comfortable Mystery 2\"");
        credits.add("\"Comfortable Mystery 3\"&\"Comfortable Mystery 4\"");
        credits.add("");
        credits.add("Kevin MacLeod (incompetech.com)");
        credits.add("Licensed under Creative Commons: By Attribution 3.0 License");
        credits.add("http://creativecommons.org/licenses/by/3.0/");
        credits.add("");
        credits.add("");
        credits.add("Libraries Used");
        credits.add("");
        credits.add("SimpleLibrary");
        credits.add("by Bryan Dolan");
        credits.add("");
        credits.add("LWJGL");
        credits.add("");
        credits.add("JLayer");
        credits.add("by javazoom");
        credits.add("");
        credits.add("Java Discord RPC");
        credits.add("");
        credits.add("");
        credits.add("");
        credits.add("The people and events in "+Main.applicationName+" are entirely fictional.");
        credits.add("Any similarity to actual people or events is unintentional.");
    }
    public MenuCredits(GUI gui){
        super(gui, null);
        back = add(new MenuComponentButton(Display.getWidth()/2-200, 540, 400, 40, "Back", true));
    }
    @Override
    public void buttonClicked(MenuComponentButton button){
        if(button==back){
            gui.open(new MenuMain(gui, false));
        }
    }
    @Override
    public void renderBackground(){
        drawRect(0,0,Display.getWidth(), Display.getHeight(), Game.theme.getBackgroundTexture(1));
        back.x = Display.getWidth()/2-back.width/2;
        back.y = Display.getHeight()-80;
        drawRect(Display.getWidth()/4-100, 40, (Display.getWidth()-Display.getWidth()/4)-100+200, 200, ImageStash.instance.getTexture("/textures/logo.png"));
        yOffset = 240;
        totalTextHeight = 0;
        if(Game.theme==Game.Theme.SNOWY)GL11.glColor4d(0, 0, 0, 1);
        for(String str : credits){
            text(str);
        }
        GL11.glColor4d(1, 1, 1, 1);
        if(scroll>totalTextHeight){
            scroll = -(back.y-40-240);
        }
    }
    private double yOffset = 240;
    private double scroll = Integer.MAX_VALUE;
    private double textHeight = 20;
    private double totalTextHeight = 0;
    private double textWidth = Display.getWidth()-400;
    public void text(String text){
        if(text==null||text.isEmpty()){
            text();
            return;
        }
        String[] texts = text.split("&");
        double wide = textWidth/(double)texts.length;
        for(int i = 0; i<texts.length; i++){
            drawCenteredTextWithBounds(200+wide*i, yOffset-scroll, 200+wide*(i+1), yOffset+textHeight-scroll, Display.getWidth()/2-textWidth/2, 240, Display.getWidth()/2+textWidth/2, back.y-40, texts[i]);
        }
        totalTextHeight += textHeight;
        yOffset+=textHeight;
    }
    public void text(){
        totalTextHeight += textHeight;
        yOffset+=textHeight;
    }
    @Override
    public void tick(){
        super.tick();
        scroll++;
    }
}