package planetaryprotector.menu;
import com.thizthizzydizzy.dizzyengine.ResourceManager;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import com.thizthizzydizzy.dizzyengine.graphics.image.Color;
import com.thizthizzydizzy.dizzyengine.ui.Menu;
import com.thizthizzydizzy.dizzyengine.ui.component.Button;
import com.thizthizzydizzy.dizzyengine.ui.layout.ConstrainedLayout;
import com.thizthizzydizzy.dizzyengine.ui.layout.constraint.PositionAnchorConstraint;
import planetaryprotector.game.Game;
import java.util.ArrayList;
import planetaryprotector.Core;
public class MenuCredits extends Menu{
    private final Button back;
    public static final ArrayList<String> credits = new ArrayList<>();
    static{
        credits.add("Made By");
        credits.add("");
        credits.add("ThizThizzyDizzy");
        credits.add("");
        credits.add("");
        credits.add("Built with DizzyEngine");
        credits.add("");
        credits.add("By ThizThizzyDizzy");
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
        credits.add("&\"Corruption\"&&\"Death and Axes\"");
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
        credits.add("LWJGL");
        credits.add("");
        credits.add("JLayer");
        credits.add("by javazoom");
        credits.add("");
        credits.add("Java Discord RPC");
        credits.add("");
        credits.add("");
        credits.add("Special Thanks");
        credits.add("");
        credits.add("computerneek");
        credits.add("");
        credits.add("");
        credits.add("");
        credits.add("The people and events in Planetary Protector are entirely fictional.");
        credits.add("Any similarity to actual people or events is unintentional.");
    }
    public MenuCredits(){
        var layout = setLayout(new ConstrainedLayout());
        back = add(new Button("Back"));
        back.setSize(400, 40);
        layout.constrain(back, new PositionAnchorConstraint(.5f, 1, .5f, 1, 0, -80));
        back.addAction(() -> {
            new MenuMain(false).open();
        });
    }
    @Override
    public void draw(double deltaTime){
        super.draw(deltaTime);
        scroll+=deltaTime*20;
        Renderer.fillRect(0, 0, getWidth(), getHeight(), Game.theme.getBackgroundTexture(1));
        Renderer.fillRect(getWidth()/4-100, 40, (getWidth()-getWidth()/4)-100+200, 200, ResourceManager.getTexture("/textures/logo.png"));
        yOffset = 240;
        totalTextHeight = 0;
        if(Game.theme==Game.Theme.SNOWY)Renderer.setColor(Color.BLACK);
        Renderer.bound(200, 240, getWidth()-200, back.y-40);
        for(String str : credits){
            text(str);
        }
        Renderer.unBound();
        Renderer.setColor(Color.WHITE);
        if(scroll>totalTextHeight){
            scroll = -(back.y-40-240);
        }
    }
    private float yOffset = 240;
    private float scroll = Integer.MAX_VALUE;
    private float textHeight = 20;
    private float totalTextHeight = 0;
    public void text(String text){
        if(text==null||text.isEmpty()){
            text();
            return;
        }
        String[] texts = text.split("&");
        float textWidth = getWidth()-400;
        float wide = textWidth/(float)texts.length;
        for(int i = 0; i<texts.length; i++){
            Renderer.drawCenteredText(200+wide*i, yOffset-scroll, 200+wide*(i+1), yOffset+textHeight-scroll, texts[i]);
        }
        totalTextHeight += textHeight;
        yOffset+=textHeight;
    }
    public void text(){
        totalTextHeight += textHeight;
        yOffset+=textHeight;
    }
}