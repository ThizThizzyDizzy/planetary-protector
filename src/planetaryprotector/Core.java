package planetaryprotector;
import planetaryprotector.menu.options.MenuOptions5;
import planetaryprotector.menu.options.MenuOptions1;
import planetaryprotector.menu.options.MenuOptions4;
import planetaryprotector.menu.MenuGame;
import planetaryprotector.menu.options.MenuOptions3;
import planetaryprotector.menu.options.MenuOptions2;
import planetaryprotector.menu.options.MenuOptions6;
import planetaryprotector.menu.MenuMain;
import planetaryprotector.enemy.AsteroidMaterial;
import planetaryprotector.particle.ParticleEffectType;
import planetaryprotector.building.task.TaskType;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import planetaryprotector.menu.options.MenuOptionsGraphics;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import simplelibrary.Sys;
import simplelibrary.config2.Config;
import simplelibrary.error.ErrorAdapter;
import simplelibrary.error.ErrorCategory;
import simplelibrary.font.FontManager;
import simplelibrary.game.GameHelper;
import simplelibrary.opengl.ImageStash;
import simplelibrary.opengl.Renderer2D;
import simplelibrary.opengl.gui.GUI;
import simplelibrary.opengl.gui.components.MenuComponent;
import simplelibrary.opengl.gui.components.MenuComponentButton;
import simplelibrary.texture.TexturePack;
import simplelibrary.texture.TexturePackManager;
public class Core extends Renderer2D{
    public static GUI gui;
    public static GameHelper helper;
    public static ArrayList<Long> FPStracker = new ArrayList<>();
    public static boolean debugMode = false;
    public static final boolean is3D = false;
    public static boolean enableCullFace = true;
    public static final boolean fullscreen = true;
    public static final boolean supportTyping = false;
    public static MenuGame game;
    public static String save = "unnamed";
    public static boolean allowLevel2 = false;
    public static int latestLevel = 0;
    public static int speedMult = 1;
    public static void main(String[] args) throws NoSuchMethodException{
        helper = new GameHelper();
        helper.setBackground(new Color(0, 200, 255));
        helper.setDisplaySize(800, 600);
        helper.setRenderInitMethod(Core.class.getDeclaredMethod("renderInit", new Class<?>[0]));
        helper.setTickInitMethod(Core.class.getDeclaredMethod("tickInit", new Class<?>[0]));
        helper.setFinalInitMethod(Core.class.getDeclaredMethod("finalInit", new Class<?>[0]));
        helper.setMaximumFramerate(60);
        helper.setRenderMethod(Core.class.getDeclaredMethod("render", int.class));
        helper.setTickMethod(Core.class.getDeclaredMethod("tick", boolean.class));
        helper.setUsesControllers(true);
        helper.setWindowTitle(Main.applicationName);
        helper.setMode(is3D?GameHelper.MODE_HYBRID:GameHelper.MODE_2D);
        if(fullscreen){
            helper.setFullscreen(true);
            helper.setAutoExitFullscreen(false);
        }
        helper.setRenderRange(0, 1000);
        helper.setFrameOfView(90);
        Sys.initLWJGLGame(new File("/errors/"), new ErrorAdapter(){
            @Override
            public void warningError(String message, Throwable error, ErrorCategory catagory){
                if(message==null){
                    return;
                }
                if(message.contains(".png!")){
                    System.err.println(message);
                }
            }
        }, null, helper);
    }
    public static void renderInit() throws LWJGLException{
        helper.frame.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                helper.running = false;
            }
        });
        FontManager.addFont("/simplelibrary/font");
        FontManager.setFont("font");
        GL11.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_BLEND);
        if(is3D){
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            if(enableCullFace) GL11.glEnable(GL11.GL_CULL_FACE);
        }
        if(supportTyping){
            Keyboard.enableRepeatEvents(true);
        }
        new TexturePackManager(new File(Main.getAppdataRoot()+"\\Texture packs"), new TexturePack(){
            @Override
            public InputStream getResourceAsStream(String name){
                if(name.startsWith("/")){
                    return super.getResourceAsStream(name);
                }
                try{
                    return new FileInputStream(new File(name));
                }catch(FileNotFoundException ex){}
                return super.getResourceAsStream(name);
            }
        });
        gui = new GUI(is3D?GameHelper.MODE_HYBRID:GameHelper.MODE_2D, helper);
        gui.open(new MenuMain(gui, null));
        for(AsteroidMaterial m : AsteroidMaterial.values()){
            for(int i = 0; i<10; i++){
                m.images[i] = ImageStash.instance.getTexture("/textures/asteroids/"+m.texture+"/Step "+(i+1)+".png");
                if(i<9) {
                    m.images[i+10] = ImageStash.instance.getTexture("/textures/asteroids/crash/Step "+(i+1)+".png");
                }
            }
        }
        for(int i = 0; i<100; i++){
            TaskType.WRECK_CLEAN.images[i] = ImageStash.instance.getTexture("/textures/tasks/"+TaskType.WRECK_CLEAN.textureRoot+"/"+(i+1)+".png");
        }
        for(ParticleEffectType p : ParticleEffectType.values()){
            for(int i = 0; i<p.frames; i++){
                p.images[i] = ImageStash.instance.getTexture("/textures/particles/"+p.texture+"/"+(i+1)+".png");
            }
        }
    }
    public static void tickInit() throws LWJGLException{
        if(Main.intellitype){
            com.melloware.jintellitype.JIntellitype.getInstance().addIntellitypeListener((int command) -> {
                System.err.println("Unhandeled Intellitype command: "+command);
            });
        }
        loadOptions();
    }
    public static void finalInit() throws LWJGLException{
        if(Main.jLayer){
            Sounds.create();
            Sounds.enableAutoplay();
        }
    }
    public static void tick(boolean isLastTick){
        if(Main.jLayer){
            Sounds.tick(isLastTick);
        }
        if(!isLastTick){
            for(int i = 0; i<speedMult; i++){
                gui.tick();
            }
        }else{
            if(Main.intellitype){
                com.melloware.jintellitype.JIntellitype.getInstance().cleanUp();
            }
            saveOptions();
        }
    }
    public static void render(int milisSinceLastTick){
        if(is3D&&enableCullFace) GL11.glDisable(GL11.GL_CULL_FACE);
        gui.render(milisSinceLastTick);
        if(is3D&&enableCullFace) GL11.glEnable(GL11.GL_CULL_FACE);
        if(Keyboard.isKeyDown(Keyboard.KEY_EQUALS)){
            Sounds.vol+=0.01f;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_MINUS)){
            Sounds.vol-=0.01f;
        }
        FPStracker.add(System.currentTimeMillis());
        while(FPStracker.get(0)<System.currentTimeMillis()-5_000){
            FPStracker.remove(0);
        }
    }
    public static void saveOptions(){
        Config config = Config.newConfig(Main.getAppdataRoot()+"\\options.cfg");
        config.load();
        config.set("level", latestLevel);
        config.set("song1", MenuOptions1.song1);
        config.set("song2", MenuOptions1.song2);
        config.set("song3", MenuOptions1.song3);
        config.set("song4", MenuOptions1.song4);
        config.set("song5", MenuOptions1.song5);
        config.set("boss11", MenuOptions2.boss11);
        config.set("boss12", MenuOptions2.boss12);
        config.set("boss21", MenuOptions2.boss21);
        config.set("boss22", MenuOptions2.boss22);
        config.set("boss31", MenuOptions2.boss31);
        config.set("boss32", MenuOptions2.boss32);
        config.set("boss41", MenuOptions2.boss41);
        config.set("boss42", MenuOptions2.boss42);
        config.set("music1", MenuOptions3.music1);
        config.set("music2", MenuOptions3.music2);
        config.set("music3", MenuOptions3.music3);
        config.set("music4", MenuOptions3.music4);
        config.set("music5", MenuOptions3.music5);
        config.set("music6", MenuOptions3.music6);
        config.set("music7", MenuOptions3.music7);
        config.set("music8", MenuOptions3.music8);
        config.set("music9", MenuOptions3.music9);
        config.set("music10", MenuOptions3.music10);
        config.set("music11", MenuOptions4.music1);
        config.set("music12", MenuOptions4.music2);
        config.set("music13", MenuOptions4.music3);
        config.set("music14", MenuOptions4.music4);
        config.set("music15", MenuOptions4.music5);
        config.set("music16", MenuOptions4.music6);
        config.set("music17", MenuOptions4.music7);
        config.set("music18", MenuOptions4.music8);
        config.set("music19", MenuOptions4.music9);
        config.set("music20", MenuOptions4.music10);
        config.set("music21", MenuOptions5.music1);
        config.set("music22", MenuOptions5.music2);
        config.set("music23", MenuOptions5.music3);
        config.set("music24", MenuOptions5.music4);
        config.set("music25", MenuOptions5.music5);
        config.set("music26", MenuOptions5.music6);
        config.set("music27", MenuOptions5.music7);
        config.set("music28", MenuOptions5.music8);
        config.set("music29", MenuOptions6.music1);
        config.set("music30", MenuOptions6.music2);
        config.set("music31", MenuOptions6.music3);
        config.set("fog", MenuOptionsGraphics.fog);
        config.set("clouds", MenuOptionsGraphics.clouds);
        config.set("particle meteors", MenuOptionsGraphics.particulateMeteors);
        config.set("fire", MenuOptionsGraphics.fire);
        config.set("particle fire", MenuOptionsGraphics.particulateFire);
        config.set("particles", MenuOptionsGraphics.particles);
        config.save();
    }
    public static void loadOptions(){
        Config config = Config.newConfig(Main.getAppdataRoot()+"\\options.cfg");
        config.load();
        latestLevel = config.get("level", 0);
        MenuOptions1.song1 = config.get("song1", true);
        MenuOptions1.song2 = config.get("song2", false);
        MenuOptions1.song3 = config.get("song3", true);
        MenuOptions1.song4 = config.get("song4", true);
        MenuOptions1.song5 = config.get("song5", true);
        MenuOptions2.boss11 = config.get("boss11", true);
        MenuOptions2.boss12 = config.get("boss12", true);
        MenuOptions2.boss21 = config.get("boss21", true);
        MenuOptions2.boss22 = config.get("boss22", true);
        MenuOptions2.boss31 = config.get("boss31", false);
        MenuOptions2.boss32 = config.get("boss32", true);
        MenuOptions2.boss41 = config.get("boss41", true);
        MenuOptions2.boss42 = config.get("boss42", true);
        MenuOptions3.music1 = config.get("music1", true);
        MenuOptions3.music2 = config.get("music2", true);
        MenuOptions3.music3 = config.get("music3", true);
        MenuOptions3.music4 = config.get("music4", true);
        MenuOptions3.music5 = config.get("music5", true);
        MenuOptions3.music6 = config.get("music6", true);
        MenuOptions3.music7 = config.get("music7", true);
        MenuOptions3.music8 = config.get("music8", true);
        MenuOptions3.music9 = config.get("music9", true);
        MenuOptions3.music10 = config.get("music10", true);
        MenuOptions4.music1 = config.get("music11", true);
        MenuOptions4.music2 = config.get("music12", true);
        MenuOptions4.music3 = config.get("music13", true);
        MenuOptions4.music4 = config.get("music14", true);
        MenuOptions4.music5 = config.get("music15", true);
        MenuOptions4.music6 = config.get("music16", true);
        MenuOptions4.music7 = config.get("music17", true);
        MenuOptions4.music8 = config.get("music18", true);
        MenuOptions4.music9 = config.get("music19", true);
        MenuOptions4.music10 = config.get("music20", true);
        MenuOptions5.music1 = config.get("music21", true);
        MenuOptions5.music2 = config.get("music22", true);
        MenuOptions5.music3 = config.get("music23", true);
        MenuOptions5.music4 = config.get("music24", true);
        MenuOptions5.music5 = config.get("music25", true);
        MenuOptions5.music6 = config.get("music26", true);
        MenuOptions5.music7 = config.get("music27", true);
        MenuOptions5.music8 = config.get("music28", true);
        MenuOptions6.music1 = config.get("music29", true);
        MenuOptions6.music2 = config.get("music30", true);
        MenuOptions6.music3 = config.get("music31", true);
        MenuOptionsGraphics.fog = config.get("fog", false);
        MenuOptionsGraphics.clouds = config.get("clouds", false);
        MenuOptionsGraphics.particulateMeteors = config.get("particle meteors", false);
        MenuOptionsGraphics.fire = config.get("fire", false);
        MenuOptionsGraphics.particulateFire = config.get("particle fire", true);
        MenuOptionsGraphics.particles = config.get("particles", 1);
        config.save();
    }
    public static long getFPS(){
        return FPStracker.size()/5;
    }
    public static double distance(MenuComponent o1, MenuComponent o2){
        return Math.sqrt(Math.pow((o1.x+o1.width/2)-(o2.x+o2.width/2), 2)+Math.pow((o1.y+o1.height/2)-(o2.y+o2.height/2), 2));
    }
    public static double distance(MenuComponent component, double x, double y) {
        return distance(component, new MenuComponentButton(x, y, 0, 0, "", false));
    }
    public static double distance(double x1, double y1, double x2, double y2) {
        return distance(new MenuComponentButton(x1, y1, 0, 0, "", false), new MenuComponentButton(x2, y2, 0, 0, "", false));
    }
    public static boolean isMouseWithinComponent(MenuComponent component){
        return isClickWithinBounds(Mouse.getX(), Display.getHeight()-Mouse.getY(), component.x, component.y, component.x+component.width, component.y+component.height);
    }
    public static boolean isMouseWithinComponent(MenuComponent component, MenuComponent... parents){
        double x = component.x;
        double y = component.y;
        for(MenuComponent c : parents){
            x+=c.x;
            y+=c.y;
        }
        return isClickWithinBounds(Mouse.getX(), Display.getHeight()-Mouse.getY(), x, y, x+component.width, y+component.height);
    }
    public static boolean isPointWithinComponent(double x, double y, MenuComponent component){
        return isClickWithinBounds(x, y, component.x, component.y, component.x+component.width, component.y+component.height);
    }
    public static double getValueBetweenTwoValues(double pos1, double val1, double pos2, double val2, double pos){
        if(pos1>pos2){
            return getValueBetweenTwoValues(pos2, val2, pos1, val1, pos);
        }
        double posDiff = pos2-pos1;
        double percent = pos/posDiff;
        double valDiff = val2-val1;
        return percent*valDiff+val1;
    }
}