package planetaryprotector.menu;
import planetaryprotector.game.Game;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import planetaryprotector.Core;
import planetaryprotector.structure.building.Building.Upgrade;
import planetaryprotector.structure.building.BuildingType;
import planetaryprotector.structure.building.task.TaskAnimated;
import planetaryprotector.structure.building.task.TaskType;
import planetaryprotector.enemy.AsteroidMaterial;
import planetaryprotector.item.Item;
import planetaryprotector.particle.ParticleEffectType;
import planetaryprotector.research.DiscoveryStage;
import planetaryprotector.research.Research;
import simplelibrary.Queue;
import simplelibrary.opengl.ImageStash;
import simplelibrary.opengl.gui.GUI;
import simplelibrary.opengl.gui.Menu;
public class MenuLoadTextures extends Menu{
    private Queue<String> textures = new Queue<>();
    private Queue<String> verifyTextures = new Queue<>();
    private Queue<String> verifyAnimations = new Queue<>();
    private String loading = "Initializing...";
    private String loading2 = "";
    private int total;
    private int threads = Math.max(1,Runtime.getRuntime().availableProcessors()-1);
    private boolean dev;
    public MenuLoadTextures(GUI gui){
        super(gui, null);
        CodeSource src = Core.class.getProtectionDomain().getCodeSource();
        if(src!=null){
            URL jar = src.getLocation();
            boolean devEnv = true;
            try(ZipInputStream zip = new ZipInputStream(jar.openStream())){
                while(true){
                    ZipEntry e = zip.getNextEntry();
                    if(e==null)break;
                    devEnv = false;
                    String name = e.getName();
                    if(name.endsWith(".png")){
                        textures.enqueue("/"+name);
                    }
                }
            }catch(IOException ex){
                Logger.getLogger(MenuLoadTextures.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(devEnv){
                File f = new File("build\\classes");
                for(File fi : getAllFiles(f, ".png")){
                    textures.enqueue(fi.getAbsolutePath().substring(f.getAbsolutePath().length()).replace("\\", "/"));
                }
            }
            dev = devEnv;
        }else{
            throw new UnsupportedOperationException("What just happened?");
        }
        if(dev){
            for(AsteroidMaterial m : AsteroidMaterial.values()){
                for(String s : m.images)verifyTextures.enqueue(s);
            }
            for(ParticleEffectType p : ParticleEffectType.values()){
                for(String s : p.images)verifyTextures.enqueue(s);
            }
            for(Game.Theme t : Game.Theme.values()){
                Game.theme = t;
                for(int i = 0; i<=16; i++){
                    verifyTextures.enqueue("/textures/buildings/base/door/"+i+".png");
                }
                for(BuildingType type : BuildingType.values()){
                    verifyTextures.enqueue(type.getTextureS());
                    for(Upgrade u : type.upgrades){
                        for(int i = 1; i<=u.max; i++){
                            verifyAnimations.enqueue(u.getAnimationS(type, i));
                            verifyTextures.enqueue(u.getTextureS(type, i));
                        }
                    }
                    verifyTextures.enqueue(type.getDamageTextureS());
                    if(type.isConstructable()){
                        verifyAnimations.enqueue(type.getAnimationS());
                    }
                }
                verifyAnimations.enqueue("/textures/tasks/skyscraper/add 1/"+t.tex());
                verifyAnimations.enqueue("/textures/tasks/"+TaskType.WRECK_CLEAN.textureRoot+"/"+t.tex());
                verifyTextures.enqueue("/textures/buildings/shield.png");
                verifyTextures.enqueue("/textures/buildings/shield outline.png");
                verifyTextures.enqueue("/textures/enemies/alien.png");
                verifyTextures.enqueue("/textures/worker.png");
                verifyTextures.enqueue("/textures/enemies/ship.png");
                verifyTextures.enqueue("/textures/drone.png");
                verifyTextures.enqueue("/textures/missile.png");
                for(int i = 1; i<=4; i++){
                    verifyTextures.enqueue("/textures/enemies/mothership "+i+".png");
                }
                for(Item i : Item.allItems){
                    verifyTextures.enqueue(i.getWorldTextureS());
                }
                verifyTextures.enqueue("/textures/logo.png");
                verifyTextures.enqueue("/textures/background/dirt normal.png");
                verifyTextures.enqueue("/textures/background/dirt snowy.png");
                verifyTextures.enqueue("/textures/background/cave.png");
                verifyTextures.enqueue("/textures/background/stone.png");
                for(Game.Theme theme : Game.Theme.values()){
                    for(int i = 1; i<=Core.LEVELS; i++){
                        verifyTextures.enqueue(theme.getBackgroundTextureS(i));
                    }
                }
                verifyTextures.enqueue("/textures/gui/sidebar.png");
                verifyTextures.enqueue("/textures/planet.png");
                for(int i = 1; i<=3; i++){
                    verifyTextures.enqueue("/textures/planet"+i+".png");
                    verifyTextures.enqueue("/textures/phase/"+i+".png");
                }
                for(int i = 0; i<3; i++){
                    verifyTextures.enqueue("/textures/gui/sidebar "+i+".png");
                    verifyTextures.enqueue("/textures/furnace "+i+".png");
                }
                verifyTextures.enqueue("/textures/research/nonsense.png");
                verifyTextures.enqueue("/textures/icons/up.png");
                verifyTextures.enqueue("/textures/icons/return.png");
                for(Research r : Research.values()){
                    verifyTextures.enqueue("/textures/research/"+r.name().toLowerCase()+"/researched.png");
                    for(DiscoveryStage s : r.discoveryStages){
                        if(s.image.isEmpty())continue;
                        verifyTextures.enqueue("/textures/research/"+r.name().toLowerCase()+"/"+s.image+".png");
                    }
                }
                verifyTextures.enqueue("/textures/mineshaft.png");
            }
        }
        loading = "Initializing";
        total = textures.size()+verifyTextures.size()+verifyAnimations.size();
    }
    @Override
    public void onGUIOpened(){
        for(int i = 0; i<threads; i++){
            Thread t = new Thread(() -> {
                loading = "Loading Textures";
                while(!textures.isEmpty()){
                    loading2 = textures.dequeue();
                    ImageStash.instance.multithreadedInsert(loading2);
                }
                if(dev){
                    loading = "Verifying Textures";
                    while(!verifyTextures.isEmpty()){
                        loading2 = verifyTextures.dequeue();
                        ImageStash.instance.multithreadedInsert(loading2);
                    }
                    loading = "Verifying Animations";
                    while(!verifyAnimations.isEmpty()){
                        loading2 = verifyAnimations.dequeue();
                        TaskAnimated.verifyAnimation(loading2);
                    }
                }
                loading = "Finishing up";
                loading2 = "";
                gui.open(new MenuMain(gui, true));
            });
            t.setName("Texture loading thread "+(i+1));
            t.start();
        }
    }
    @Override
    public void render(int millisSinceLastTick){
        GL11.glColor4d(0, 0, 0, 1);
        drawRect(0, 0, Display.getWidth(), Display.getHeight(), 0);
        GL11.glColor4d(1, 1, 1, 1);
        drawRect(0, Display.getHeight()*.49, Display.getWidth()*(total-(textures.size()+verifyTextures.size()+verifyAnimations.size()))/(double)total, Display.getHeight()*.51, 0);
        drawCenteredText(0, Display.getHeight()*.45-40, Display.getWidth(), Display.getHeight()*.45, loading);
        drawText(0, Display.getHeight()*.51, Display.getWidth(), Display.getHeight()*.51+20, loading2);
    }
    private ArrayList<File> getAllFiles(File file, String suffix){
        ArrayList<File> files = new ArrayList<>();
        if(file.isFile()){
            if(file.getName().endsWith(suffix))files.add(file);
        }else{
            for(File f : file.listFiles()){
                files.addAll(getAllFiles(f, suffix));
            }
        }
        return files;
    }
    public void done(){
        gui.open(new MenuMain(gui, true));
    }
}