package planetaryprotector.menu.options;
import planetaryprotector.Sounds;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import planetaryprotector.game.Game;
import static simplelibrary.opengl.Renderer2D.drawRect;
import simplelibrary.opengl.gui.GUI;
import simplelibrary.opengl.gui.Menu;
import simplelibrary.opengl.gui.components.MenuComponentButton;
import simplelibrary.opengl.gui.components.MenuComponentOptionButton;
public class MenuOptions2 extends Menu{
    private final MenuComponentButton back;
    public static boolean boss11,boss12,boss21,boss22,boss31,boss32,boss41,boss42;
    private final MenuComponentOptionButton button1,button2,button3,button4,button5,button6,button7,button8;
    private final MenuComponentButton nextPage,play1,play2,play3,play4,play5,play6,play7,play8;
    public MenuOptions2(GUI gui, Menu parent){
        super(gui, parent);
        nextPage = add(new MenuComponentButton(Display.getWidth()-400, Display.getHeight()-40, 400, 40, "Next Page", true, true));
        back = add(new MenuComponentButton(Display.getWidth()/2-200, Display.getHeight()-80, 400, 40, "Back", true));
        double offset = back.height*3;
        play1 = add(new MenuComponentButton(back.x-back.height*5, offset, back.height*3, back.height, "Play", true, true));
        offset += back.height*1.5;
        play2 = add(new MenuComponentButton(back.x-back.height*5, offset, back.height*3, back.height, "Play", true, true));
        offset += back.height*1.5;
        play3 = add(new MenuComponentButton(back.x-back.height*5, offset, back.height*3, back.height, "Play", true, true));
        offset += back.height*1.5;
        play4 = add(new MenuComponentButton(back.x-back.height*5, offset, back.height*3, back.height, "Play", true, true));
        offset += back.height*1.5;
        play5 = add(new MenuComponentButton(back.x-back.height*5, offset, back.height*3, back.height, "Play", true, true));
        offset += back.height*1.5;
        play6 = add(new MenuComponentButton(back.x-back.height*5, offset, back.height*3, back.height, "Play", true, true));
        offset += back.height*1.5;
        play7 = add(new MenuComponentButton(back.x-back.height*5, offset, back.height*3, back.height, "Play", true, true));
        offset += back.height*1.5;
        play8 = add(new MenuComponentButton(back.x-back.height*5, offset, back.height*3, back.height, "Play", true, true));
        button1 = add(new MenuComponentOptionButton(Display.getWidth()/2-200, play1.y, back.width, back.height, "Eternal Terminal", true, true, boss11?0:1, "On", "Off"));
        button2 = add(new MenuComponentOptionButton(Display.getWidth()/2-200, play2.y, back.width, back.height, "Five Armies", true, true, boss12?0:1, "On", "Off"));
        button3 = add(new MenuComponentOptionButton(Display.getWidth()/2-200, play3.y, back.width, back.height, "Corruption", true, true, boss21?0:1, "On", "Off"));
        button4 = add(new MenuComponentOptionButton(Display.getWidth()/2-200, play4.y, back.width, back.height, "Clash Defiant", true, true, boss22?0:1, "On", "Off"));
        button5 = add(new MenuComponentOptionButton(Display.getWidth()/2-200, play5.y, back.width, back.height, "Metalmania", true, true, boss31?0:1, "On", "Off"));
        button6 = add(new MenuComponentOptionButton(Display.getWidth()/2-200, play6.y, back.width, back.height, "Obliteration", true, true, boss32?0:1, "On", "Off"));
        button7 = add(new MenuComponentOptionButton(Display.getWidth()/2-200, play7.y, back.width, back.height, "Death and Axes", true, true, boss41?0:1, "On", "Off"));
        button8 = add(new MenuComponentOptionButton(Display.getWidth()/2-200, play8.y, back.width, back.height, "Grim Idol", true, true, boss42?0:1, "On", "Off"));
    }
    @Override
    public void buttonClicked(MenuComponentButton button){
        if(button==back){
            gui.open(parent);
        }
        if(button==play1){
            Sounds.playSoundOneChannel("music", "Boss1Music1");
        }
        if(button==play2){
            Sounds.playSoundOneChannel("music", "Boss1Music2");
        }
        if(button==play3){
            Sounds.playSoundOneChannel("music", "Boss2Music1");
        }
        if(button==play4){
            Sounds.playSoundOneChannel("music", "Boss2Music2");
        }
        if(button==play5){
            Sounds.playSoundOneChannel("music", "Boss3Music1");
        }
        if(button==play6){
            Sounds.playSoundOneChannel("music", "Boss3Music2");
        }
        if(button==play7){
            Sounds.playSoundOneChannel("music", "Boss4Music1");
        }
        if(button==play8){
            Sounds.playSoundOneChannel("music", "Boss4Music2");
        }
        if(button==nextPage){
            gui.open(new MenuOptions3(gui, this));
        }
    }
    @Override
    public void renderBackground(){
        GL11.glColor4d(1, 1, 1, 1);
        drawRect(0,0,Display.getWidth(), Display.getHeight(), Game.theme.getBackgroundTexture(1));
        boss11 = button1.getIndex()==0;
        boss12 = button2.getIndex()==0;
        boss21 = button3.getIndex()==0;
        boss22 = button4.getIndex()==0;
        boss31 = button5.getIndex()==0;
        boss32 = button6.getIndex()==0;
        boss41 = button7.getIndex()==0;
        boss42 = button8.getIndex()==0;
        drawCenteredText(0, back.height, Display.getWidth(), back.height*2, "Boss Fight Music");
        drawCenteredText(button1.x+20+button1.width, button1.y, Display.getWidth(), button1.y+button1.height, "Phase 1");
        drawCenteredText(button2.x+20+button2.width, button2.y, Display.getWidth(), button2.y+button2.height, "Phase 1");
        drawCenteredText(button3.x+20+button3.width, button3.y, Display.getWidth(), button3.y+button3.height, "Phase 2");
        drawCenteredText(button4.x+20+button4.width, button4.y, Display.getWidth(), button4.y+button4.height, "Phase 2");
        drawCenteredText(button5.x+20+button5.width, button5.y, Display.getWidth(), button5.y+button5.height, "Phase 3");
        drawCenteredText(button6.x+20+button6.width, button6.y, Display.getWidth(), button6.y+button6.height, "Phase 3");
        drawCenteredText(button7.x+20+button7.width, button7.y, Display.getWidth(), button7.y+button7.height, "Phase 4");
        drawCenteredText(button8.x+20+button8.width, button8.y, Display.getWidth(), button8.y+button8.height, "Phase 4");
    }
    @Override
    public void render(int millisSinceLastTick){
        super.render(millisSinceLastTick);
        GL11.glColor4d(1, 1, 1, 1);
        button1.render();
        GL11.glColor4d(1, 1, 1, 1);
        button2.render();
        GL11.glColor4d(1, 1, 1, 1);
        button3.render();
        GL11.glColor4d(1, 1, 1, 1);
        button4.render();
        GL11.glColor4d(1, 1, 1, 1);
        button5.render();
        GL11.glColor4d(1, 1, 1, 1);
        button6.render();
        GL11.glColor4d(1, 1, 1, 1);
        button7.render();
        GL11.glColor4d(1, 1, 1, 1);
        button8.render();
        GL11.glColor4d(1, 1, 1, 1);
    }
}