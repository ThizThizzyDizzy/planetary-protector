package planetaryprotector.menu.options;
import planetaryprotector.Sounds;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import simplelibrary.opengl.gui.GUI;
import simplelibrary.opengl.gui.Menu;
import simplelibrary.opengl.gui.components.MenuComponentButton;
import simplelibrary.opengl.gui.components.MenuComponentOptionButton;
public class MenuOptions1 extends Menu{
    private final MenuComponentButton back;
    public static boolean song1, song2, song3, song4, song5;
    private final MenuComponentOptionButton button1, button2, button3, button4, button5;
    private final MenuComponentButton nextPage, play1, play2, play3, play4, play5;
    public MenuOptions1(GUI gui, Menu parent){
        super(gui, parent);
        nextPage = add(new MenuComponentButton(Display.getWidth()-400, Display.getHeight()-40, 400, 40, "Next Page", true, true));
        back = add(new MenuComponentButton(Display.getWidth()/2-200, Display.getHeight()-80, 400, 40, "Back", true));
        double offset = back.height*3;
        play1 = add(new MenuComponentButton(back.x-back.height*5, offset, back.height*3, back.height, "Play", true, true));
        offset += back.height*2.5;
        play2 = add(new MenuComponentButton(back.x-back.height*5, offset, back.height*3, back.height, "Play", true, true));
        offset += back.height*1.5;
        play3 = add(new MenuComponentButton(back.x-back.height*5, offset, back.height*3, back.height, "Play", true, true));
        offset += back.height*1.5;
        play4 = add(new MenuComponentButton(back.x-back.height*5, offset, back.height*3, back.height, "Play", true, true));
        offset += back.height*1.5;
        play5 = add(new MenuComponentButton(back.x-back.height*5, offset, back.height*3, back.height, "Play", true, true));
        button1 = add(new MenuComponentOptionButton(Display.getWidth()/2-200, play1.y, back.width, back.height, "Killers", true, true, song1?0:1, "On", "Off"));
        button2 = add(new MenuComponentOptionButton(Display.getWidth()/2-200, play2.y, back.width, back.height, "Clenched Teeth", true, true, song2?0:1, "On", "Off"));
        button3 = add(new MenuComponentOptionButton(Display.getWidth()/2-200, play3.y, back.width, back.height, "Archilles", true, true, song3?0:1, "On", "Off"));
        button4 = add(new MenuComponentOptionButton(Display.getWidth()/2-200, play4.y, back.width, back.height, "Noble Race", true, true, song4?0:1, "On", "Off"));
        button5 = add(new MenuComponentOptionButton(Display.getWidth()/2-200, play5.y, back.width, back.height, "Rynos Theme", true, true, song5?0:1, "On", "Off"));
    }
    @Override
    public void buttonClicked(MenuComponentButton button){
        if(button==back){
            gui.open(parent);
        }
        if(button==play1){
            Sounds.playSoundOneChannel("music", "Music1");
        }
        if(button==play2){
            Sounds.playSoundOneChannel("music", "Music2");
        }
        if(button==play3){
            Sounds.playSoundOneChannel("music", "Music3");
        }
        if(button==play4){
            Sounds.playSoundOneChannel("music", "Music4");
        }
        if(button==play5){
            Sounds.playSoundOneChannel("music", "Music5");
        }
        if(button==nextPage){
            gui.open(new MenuOptions2(gui, this));
        }
    }
    @Override
    public void renderBackground(){
        GL11.glColor4d(1, 1, 1, 1);
        super.renderBackground();
        GL11.glColor4d(1, 1, 1, 1);
        song1 = button1.getIndex()==0;
        song2 = button2.getIndex()==0;
        song3 = button3.getIndex()==0;
        song4 = button4.getIndex()==0;
        song5 = button5.getIndex()==0;
        drawCenteredText(0, back.height, Display.getWidth(), back.height*2, "Meteor Shower & Losing Music");
        drawCenteredText(0, back.height*4.25, Display.getWidth(), back.height*5.25, "Normal Game Music");
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
    }
}