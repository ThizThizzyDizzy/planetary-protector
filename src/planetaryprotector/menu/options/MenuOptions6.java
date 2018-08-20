package planetaryprotector.menu.options;
import planetaryprotector.Sounds;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import simplelibrary.opengl.gui.GUI;
import simplelibrary.opengl.gui.Menu;
import simplelibrary.opengl.gui.components.MenuComponentButton;
import simplelibrary.opengl.gui.components.MenuComponentOptionButton;
public class MenuOptions6 extends Menu{
    private final MenuComponentButton back;
    public static boolean music1 = true;
    public static boolean music2 = true;
    public static boolean music3 = true;
    private final MenuComponentOptionButton button1;
    private final MenuComponentOptionButton button2;
    private final MenuComponentOptionButton button3;
    private final MenuComponentButton play1;
    private final MenuComponentButton play2;
    private final MenuComponentButton play3;
    public MenuOptions6(GUI gui, Menu parent){
        super(gui, parent);
        back = add(new MenuComponentButton(Display.getWidth()/2-200, Display.getHeight()-80, 400, 40, "Back", true));
        double offset = back.height*3;
        play1 = add(new MenuComponentButton(back.x-back.height*5, offset, back.height*3, back.height, "Play", true, true));
        offset += back.height*1.5;
        offset += back.height*1.5;
        play2 = add(new MenuComponentButton(back.x-back.height*5, offset, back.height*3, back.height, "Play", true, true));
        offset += back.height*1.5;
        offset += back.height*1.5;
        play3 = add(new MenuComponentButton(back.x-back.height*5, offset, back.height*3, back.height, "Play", true, true));
        button1 = add(new MenuComponentOptionButton(Display.getWidth()/2-200, play1.y, back.width, back.height, "Americana", true, true, music1?0:1, "On", "Off"));
        button2 = add(new MenuComponentOptionButton(Display.getWidth()/2-200, play2.y, back.width, back.height, "Final Battle of the Dark Wizards", true, true, music2?0:1, "On", "Off"));
        button3 = add(new MenuComponentOptionButton(Display.getWidth()/2-200, play3.y, back.width, back.height, "Jet Fueled Vixen", true, true, music3?0:1, "On", "Off"));
    }
    @Override
    public void buttonClicked(MenuComponentButton button){
        if(button==back){
            gui.open(parent);
        }
        if(button==play1){
            Sounds.playSoundOneChannel("music", "WinMusic");
        }
        if(button==play2){
            Sounds.playSoundOneChannel("music", "SuspenseMusic1");
        }
        if(button==play3){
            Sounds.playSoundOneChannel("music", "VictoryMusic1");
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
        drawCenteredText(0, back.height, Display.getWidth(), back.height*2, "Win Music");
        drawCenteredText(0, back.height*4, Display.getWidth(), back.height*5, "Suspense Music");
        drawCenteredText(0, back.height*7, Display.getWidth(), back.height*8, "Victory Music");
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
    }
}