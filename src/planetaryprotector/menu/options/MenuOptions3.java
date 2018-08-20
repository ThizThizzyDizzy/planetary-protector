package planetaryprotector.menu.options;
import planetaryprotector.Sounds;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import simplelibrary.opengl.gui.GUI;
import simplelibrary.opengl.gui.Menu;
import simplelibrary.opengl.gui.components.MenuComponentButton;
import simplelibrary.opengl.gui.components.MenuComponentOptionButton;
public class MenuOptions3 extends Menu{
    private final MenuComponentButton back;
    public static boolean music1,music2,music3,music4,music5,music6,music7,music8,music9,music10;
    private final MenuComponentOptionButton button1,button2,button3,button4,button5,button6,button7,button8,button9,button10;
    private final MenuComponentButton nextPage,play1,play2,play3,play4,play5,play6,play7,play8,play9,play10;
    public MenuOptions3(GUI gui, Menu parent){
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
        offset += back.height*1.5;
        play9 = add(new MenuComponentButton(back.x-back.height*5, offset, back.height*3, back.height, "Play", true, true));
        offset += back.height*1.5;
        play10 = add(new MenuComponentButton(back.x-back.height*5, offset, back.height*3, back.height, "Play", true, true));
        button1 = add(new MenuComponentOptionButton(Display.getWidth()/2-200, play1.y, back.width, back.height, "A turn for the Worse", true, true, music1?0:1, "On", "Off"));
        button2 = add(new MenuComponentOptionButton(Display.getWidth()/2-200, play2.y, back.width, back.height, "A Little Faith", true, true, music2?0:1, "On", "Off"));
        button3 = add(new MenuComponentOptionButton(Display.getWidth()/2-200, play3.y, back.width, back.height, "At Rest", true, true, music3?0:1, "On", "Off"));
        button4 = add(new MenuComponentOptionButton(Display.getWidth()/2-200, play4.y, back.width, back.height, "Awaiting Return", true, true, music4?0:1, "On", "Off"));
        button5 = add(new MenuComponentOptionButton(Display.getWidth()/2-200, play5.y, back.width, back.height, "Bittersweet", true, true, music5?0:1, "On", "Off"));
        button6 = add(new MenuComponentOptionButton(Display.getWidth()/2-200, play6.y, back.width, back.height, "Colorless Aura", true, true, music6?0:1, "On", "Off"));
        button7 = add(new MenuComponentOptionButton(Display.getWidth()/2-200, play7.y, back.width, back.height, "Cryptic Sorrow", true, true, music7?0:1, "On", "Off"));
        button8 = add(new MenuComponentOptionButton(Display.getWidth()/2-200, play8.y, back.width, back.height, "Dark Times", true, true, music8?0:1, "On", "Off"));
        button9 = add(new MenuComponentOptionButton(Display.getWidth()/2-200, play9.y, back.width, back.height, "Dark Walk", true, true, music9?0:1, "On", "Off"));
        button10 = add(new MenuComponentOptionButton(Display.getWidth()/2-200, play10.y, back.width, back.height, "Despair and Triumph", true, true, music10?0:1, "On", "Off"));
    }
    @Override
    public void buttonClicked(MenuComponentButton button){
        if(button==back){
            gui.open(parent);
        }
        if(button==play1){
            Sounds.playSoundOneChannel("music", "SadMusic1");
        }
        if(button==play2){
            Sounds.playSoundOneChannel("music", "SadMusic2");
        }
        if(button==play3){
            Sounds.playSoundOneChannel("music", "SadMusic3");
        }
        if(button==play4){
            Sounds.playSoundOneChannel("music", "SadMusic4");
        }
        if(button==play5){
            Sounds.playSoundOneChannel("music", "SadMusic5");
        }
        if(button==play6){
            Sounds.playSoundOneChannel("music", "SadMusic6");
        }
        if(button==play7){
            Sounds.playSoundOneChannel("music", "SadMusic7");
        }
        if(button==play8){
            Sounds.playSoundOneChannel("music", "SadMusic8");
        }
        if(button==play9){
            Sounds.playSoundOneChannel("music", "SadMusic9");
        }
        if(button==play10){
            Sounds.playSoundOneChannel("music", "SadMusic10");
        }
        if(button==nextPage){
            gui.open(new MenuOptions4(gui, this));
        }
    }
    @Override
    public void renderBackground(){
        GL11.glColor4d(1, 1, 1, 1);
        super.renderBackground();
        GL11.glColor4d(1, 1, 1, 1);
        music1 = button1.getIndex()==0;
        music2 = button2.getIndex()==0;
        music3 = button3.getIndex()==0;
        music4 = button4.getIndex()==0;
        music5 = button5.getIndex()==0;
        music6 = button6.getIndex()==0;
        music7 = button7.getIndex()==0;
        music8 = button8.getIndex()==0;
        music9 = button8.getIndex()==0;
        music10 = button8.getIndex()==0;
        drawCenteredText(0, back.height, Display.getWidth(), back.height*2, "Game Over Music");
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
        button9.render();
        GL11.glColor4d(1, 1, 1, 1);
        button10.render();
        GL11.glColor4d(1, 1, 1, 1);
    }
}