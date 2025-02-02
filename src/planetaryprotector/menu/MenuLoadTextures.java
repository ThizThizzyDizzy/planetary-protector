package planetaryprotector.menu;
import com.thizthizzydizzy.dizzyengine.ResourceManager;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import com.thizthizzydizzy.dizzyengine.graphics.image.Color;
import com.thizthizzydizzy.dizzyengine.ui.Menu;
import com.thizthizzydizzy.dizzyengine.logging.Logger;
import planetaryprotector.game.Game;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import planetaryprotector.Core;
import planetaryprotector.structure.task.TaskAnimated;
import planetaryprotector.structure.task.TaskType;
import planetaryprotector.item.Item;
import planetaryprotector.particle.ParticleEffectType;
import planetaryprotector.research.DiscoveryStage;
import planetaryprotector.research.Research;
import planetaryprotector.structure.SkyscraperDecal;
import planetaryprotector.structure.Structure.Upgrade;
import planetaryprotector.structure.StructureType;
public class MenuLoadTextures extends Menu{
    private ArrayList<String> textures = new ArrayList<>();
    private ArrayList<String> verifyTextures = new ArrayList<>();
    private ArrayList<String> verifyAnimations = new ArrayList<>();
    private String loading = "Initializing...";
    private String loading2 = "";
    private int total;
    private int threads = Math.max(1, Runtime.getRuntime().availableProcessors()-1);
    private boolean dev;
    public MenuLoadTextures(){
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
                        textures.add("/"+name);
                    }
                }
            }catch(IOException ex){
                Logger.error(ex);
            }
            if(devEnv){
                File f = new File("build/classes");
                for(File fi : getAllFiles(f, ".png")){
                    textures.add(fi.getAbsolutePath().substring(f.getAbsolutePath().length()).replace("\\", "/"));
                }
            }
            dev = devEnv;
        }else{
            throw new UnsupportedOperationException("What just happened?");
        }
        if(dev){
            verifyTextures.add("/textures/asteroids/stone.png");
            verifyTextures.add("/textures/asteroids/ore.png");
            for(ParticleEffectType p : ParticleEffectType.values()){
                for(String s : p.images)verifyTextures.add(s);
            }
            for(Game.Theme t : Game.Theme.values()){
                Game.theme = t;
                for(int i = 0; i<=16; i++){
                    verifyTextures.add("/textures/structures/base/door/"+i+".png");
                }
                for(StructureType type : StructureType.structureTypes){
                    verifyTextures.add(type.getTextureS());
                    for(Upgrade u : type.upgrades){
                        for(int i = 1; i<=u.max; i++){
                            verifyAnimations.add(u.getAnimationS(type, i));
                            verifyTextures.add(u.getTextureS(type, i));
                        }
                    }
                    if(type.isConstructible()){
                        verifyAnimations.add(type.getAnimationS());
                    }
                }
                verifyAnimations.add("/textures/tasks/skyscraper/add 1/"+t.tex());
                verifyAnimations.add("/textures/tasks/"+TaskType.WRECK_CLEAN.textureRoot+"/"+t.tex());
                for(SkyscraperDecal.Type d : SkyscraperDecal.Type.values()){
                    if(d==SkyscraperDecal.Type.DUST)continue;
                    if(d==SkyscraperDecal.Type.WINDOW){
                        verifyTextures.add("/textures/structures/skyscraper/decals/"+t.tex()+"/windows.png");
                        continue;
                    }
                    for(int i = 0; i<d.variants; i++){
                        verifyTextures.add("/textures/structures/skyscraper/decals/"+t.tex()+"/"+d.tex+" "+(i+1)+".png");
                    }
                }
                verifyTextures.add("/textures/structures/shield.png");
                verifyTextures.add("/textures/structures/shield outline.png");
                verifyTextures.add("/textures/enemies/alien.png");
                verifyTextures.add("/textures/worker.png");
                verifyTextures.add("/textures/enemies/ship.png");
                verifyTextures.add("/textures/drone.png");
                verifyTextures.add("/textures/missile.png");
                for(int i = 1; i<=4; i++){
                    verifyTextures.add("/textures/enemies/mothership "+i+".png");
                }
                for(Item i : Item.allItems){
                    verifyTextures.add(i.getWorldTextureS());
                }
                verifyTextures.add("/textures/logo.png");
                verifyTextures.add("/textures/background/dirt normal.png");
                verifyTextures.add("/textures/background/dirt snowy.png");
                verifyTextures.add("/textures/background/stone.png");
                for(Game.Theme theme : Game.Theme.values()){
                    for(int i = 1; i<=Core.LEVELS; i++){
                        verifyTextures.add(theme.getBackgroundTextureS(i));
                    }
                }
                verifyTextures.add("/textures/gui/sidebar.png");
                verifyTextures.add("/textures/planet.png");
                for(int i = 1; i<=3; i++){
                    verifyTextures.add("/textures/planet"+i+".png");
                    verifyTextures.add("/textures/phase/"+i+".png");
                }
                for(int i = 0; i<3; i++){
                    verifyTextures.add("/textures/gui/sidebar "+i+".png");
                    verifyTextures.add("/textures/furnace "+i+".png");
                }
                verifyTextures.add("/textures/research/nonsense.png");
                verifyTextures.add("/textures/icons/up.png");
                verifyTextures.add("/textures/icons/return.png");
                for(Research r : Research.values()){
                    verifyTextures.add("/textures/research/"+r.name().toLowerCase()+"/researched.png");
                    for(DiscoveryStage s : r.discoveryStages){
                        if(s.image.isEmpty())continue;
                        verifyTextures.add("/textures/research/"+r.name().toLowerCase()+"/"+s.image+".png");
                    }
                }
                verifyTextures.add("/textures/mineshaft.png");
            }
        }
        loading = "Initializing";
        total = textures.size()+verifyTextures.size()+verifyAnimations.size();
    }
    @Override
    public void draw(double deltaTime){
        long start = System.nanoTime();
        while((System.nanoTime()-start)<50_000_000){
            if(!textures.isEmpty()){
                loading = "Loading Textures";
                ResourceManager.getTexture(loading2 = textures.remove(0));
            }else if(dev){
                if(!verifyTextures.isEmpty()){
                    loading = "Verifying Textures";
                    ResourceManager.getTexture(loading2 = verifyTextures.remove(0));
                }else if(!verifyAnimations.isEmpty()){
                    loading = "Verifying Animations";
                    TaskAnimated.verifyAnimation(loading2 = verifyAnimations.remove(0));
                }else{
                    loading2 = "";
                    loading = "Finishing up";
                    new MenuMain(true).open();
                }
            }else{
                new MenuMain(true).open();
            }
        }
        Renderer.setColor(Color.BLACK);
        Renderer.fillRect(0, 0, getWidth(), getHeight());
        Renderer.setColor(Color.WHITE);
        Renderer.fillRect(0, getHeight()*.49f, getWidth()*(total-(textures.size()+verifyTextures.size()+verifyAnimations.size()))/(float)total, getHeight()*.51f, 0);
        Renderer.drawCenteredText(0, getHeight()*.45f-40, getWidth(), getHeight()*.45f, loading);
        Renderer.drawText(0, getHeight()*.51f, getWidth(), getHeight()*.51f+20, loading2);
    }
    private ArrayList<File> getAllFiles(File file, String suffix){
        ArrayList<File> files = new ArrayList<>();
        if(file.isFile()){
            if(file.getName().endsWith(suffix))files.add(file);
        }else{
            var filez = file.listFiles();
            if(filez!=null){// Skip files if no access
                for(File f : filez){
                    if(f==null)continue; // Skip file if no access
                    files.addAll(getAllFiles(f, suffix));
                }
            }
        }
        return files;
    }
}
