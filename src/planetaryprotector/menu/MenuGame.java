package planetaryprotector.menu;
import com.thizthizzydizzy.dizzyengine.DizzyEngine;
import com.thizthizzydizzy.dizzyengine.ResourceManager;
import com.thizthizzydizzy.dizzyengine.collision.AxisAlignedBoundingBox;
import com.thizthizzydizzy.dizzyengine.discord.DiscordPresence;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import com.thizthizzydizzy.dizzyengine.ui.FlatUI;
import com.thizthizzydizzy.dizzyengine.ui.Menu;
import com.thizthizzydizzy.dizzyengine.ui.component.Button;
import com.thizthizzydizzy.dizzyengine.ui.component.Component;
import java.util.ArrayList;
import java.util.Iterator;
import org.joml.Matrix4f;
import org.joml.Vector2d;
import org.lwjgl.glfw.GLFW;
import static org.lwjgl.glfw.GLFW.*;
import planetaryprotector.Controls;
import planetaryprotector.Sounds;
import planetaryprotector.enemy.Asteroid;
import planetaryprotector.enemy.AsteroidMaterial;
import planetaryprotector.enemy.Enemy;
import planetaryprotector.enemy.EnemyMothership;
import planetaryprotector.friendly.ShootingStar;
import planetaryprotector.game.Action;
import planetaryprotector.game.BoundingBox;
import planetaryprotector.game.Epilogue;
import planetaryprotector.game.Game;
import planetaryprotector.game.Notification;
import planetaryprotector.item.Item;
import planetaryprotector.item.ItemStack;
import planetaryprotector.menu.component.MenuComponentActionButton;
import planetaryprotector.menu.component.MenuComponentFalling;
import planetaryprotector.menu.component.MenuComponentRising;
import planetaryprotector.menu.ingame.MenuComponentOverlay;
import planetaryprotector.menu.ingame.MenuIngame;
import planetaryprotector.particle.Particle;
import planetaryprotector.structure.Structure;
import planetaryprotector.structure.Structure.Upgrade;
public class MenuGame extends Menu{
    public ArrayList<MenuComponentActionButton> actionButtons = new ArrayList<>();
    private static final int actionButtonWidth = 375;
    private static final int actionButtonHeight = 60;
    public int actionButtonOffset = 0;
    public float debugYOffset = 0;
    public MenuComponentOverlay overlay;
    public Game game;
    private MenuComponentPhaseMarker phaseMarker;
    private float xOff = 0;
    private float yOff = 0;
    private float zoom = 1;
    public static final float minZoom = .5f;
    public static final float maxZoom = 2f;
    private float zoomFac = .05f;
    private float panFac = 7;
    public MenuGame(Game game){
        this.game = game;
    }
    @Override
    public void render(double deltaTime){
        synchronized(game){
            //<editor-fold defaultstate="collapsed" desc="Tick">
            if(game.updatePhaseMarker){
                game.paused = true;
                game.updatePhaseMarker = false;
                createPhaseMarker();
            }
            if(phaseMarker!=null){
                if(phaseMarker.opacity<0)phaseMarker = null;
            }
            //<editor-fold defaultstate="collapsed" desc="Discord">
            DiscordPresence.setState("");
            DiscordPresence.setSmallImage("", "");
            switch(game.phase){
                case 1:
                    DiscordPresence.setDetails("Phase 1 - Armogeddon");
                    DiscordPresence.setLargeImage("base", "Phase 1 - Armogeddon");
                    break;
                case 2:
                    DiscordPresence.setDetails("Phase 2 - Reconstruction");
                    DiscordPresence.setLargeImage("skyscraper", "Phase 2 - Reconstruction");
                    int maxPop = game.calculatePopulationCapacity();
                    DiscordPresence.setState("Pop. Cap.: "+maxPop/1000+"k/"+game.targetPopulation/1000+"k ("+Math.round(maxPop/(double)game.targetPopulation*10000D)/100D+"%)");
                    break;
                case 3:
                    DiscordPresence.setDetails("Phase 3 - Repopulation");
                    DiscordPresence.setLargeImage("city", "Phase 3 - Repopulation");
                    int pop = game.calculatePopulation();
                    maxPop = game.calculatePopulationCapacity();
                    DiscordPresence.setState("Population: "+pop/1000+"k/"+maxPop/1000+"k ("+Math.round(pop/(double)maxPop*10000D)/100D+"%)");
                    break;
                case 4:
                    int mothershipPhase = 0;
                    for(Enemy e : game.enemies){
                        if(e instanceof EnemyMothership){
                            mothershipPhase = Math.max(mothershipPhase, ((EnemyMothership)e).phase);
                        }
                    }
                    if(mothershipPhase>0){
                        DiscordPresence.setDetails("Boss Fight - Phase "+mothershipPhase);
                        DiscordPresence.setLargeImage(switch(mothershipPhase){
                            case 1 ->
                                "mothership_1";
                            case 2 ->
                                "mothership_2";
                            case 3 ->
                                "mothership_3";
                            case 4 ->
                                "mothership_4";
                            default ->
                                "";
                        }, "Boss Fight - Phase "+mothershipPhase);

                    }
                    break;
            }
            if(game.meteorShower){
                DiscordPresence.setState("Meteor Shower!");
                DiscordPresence.setSmallImage("asteroid_stone", "Meteor Shower!");
            }
            if(game.lost){
                DiscordPresence.setState("Game Over");
            }
            if(game.won){
                DiscordPresence.setState("Victory!");
            }
//</editor-fold>
            if(game.fading)game.blackScreenOpacity += 0.01;
            if(!game.paused){
//            game.tick();
                game.story.tick(game);
            }
            if(game.lost&&game.phase>3){
                if(game.lostTimer>game.loseSongLength/10){
                    if(Sounds.songTimer()<game.loseSongLength/20){
                        if(game.lostTimer>game.loseSongLength/20+20*5){
                            new MenuLost(game).open();
                        }
                    }
                }
            }
            if(game.blackScreenOpacity>=1){
                Epilogue g = new Epilogue();
                g.blackScreenOpacity = 1;
                g.fading = false;
                new MenuGame(g).open();
            }
            if(game.addingIron>0){
                add(new MenuComponentFalling(this, getWidth()-90+game.rand.nextInt(60), getHeight()-180+game.rand.nextInt(50), Item.ironOre));
                game.addingIron--;
            }
            if(game.addingCoal>0){
                add(new MenuComponentFalling(this, getWidth()-90+game.rand.nextInt(60), getHeight()-180+game.rand.nextInt(50), Item.coal));
                game.addingCoal--;
            }
            if(game.smeltingIron>0){
                add(new MenuComponentRising(this, getWidth()-90+game.rand.nextInt(60), getHeight()-20+game.rand.nextInt(10), Item.ironIngot));
            }
            if(DizzyEngine.isKeyDown(Controls.left)){
                xOff -= maxZoom/zoom*panFac;
            }
            if(DizzyEngine.isKeyDown(Controls.up)){
                yOff -= maxZoom/zoom*panFac;
            }
            if(DizzyEngine.isKeyDown(Controls.right)){
                xOff += maxZoom/zoom*panFac;
            }
            if(DizzyEngine.isKeyDown(Controls.down)){
                yOff += maxZoom/zoom*panFac;
            }
            AxisAlignedBoundingBox worldBBox = game.getWorldBoundingBox();
            float viewWidth = getWidth()/zoom;
            float viewHeight = getHeight()/zoom;

            double minY = worldBBox.min.y;
            double padding = game.getYGamePadding();

            for(Structure s : game.structures){
                double top = s.getPosition().y-s.getStructureHeight()*game.getShearFactor();
                double requiredY = top-padding*0.25;
                if(requiredY<minY){
                    minY = requiredY;
                }
            }

            float minX = worldBBox.min.x+viewWidth/2;
            float maxX = worldBBox.max.x-viewWidth/2;
            float topY = (float)minY+viewHeight/2;
            float bottomY = worldBBox.max.y-viewHeight/2;

            if(xOff<minX)xOff = minX;
            if(xOff>maxX)xOff = maxX;

            if(yOff<topY)yOff = topY;
            if(yOff>bottomY)yOff = bottomY;

            // Handle case where view is larger than bounds (center it)
            if(minX>maxX)xOff = (worldBBox.min.x+worldBBox.max.x)/2;
            if(topY>bottomY)yOff = (float)(minY+worldBBox.max.y)/2;

//        AxisAlignedBoundingBox bbox = game.getWorldBoundingBox();
//        float viewWidth = getWidth()/zoom;
//        float viewHeight = getHeight()/zoom;
//        if(xOff < bbox.min.x + viewWidth/2) xOff = bbox.min.x + viewWidth/2;
//        if(xOff > bbox.max.x - viewWidth/2) xOff = bbox.max.x - viewWidth/2;
//        if(yOff < bbox.min.y + viewHeight/2 - 400) yOff = bbox.min.y + viewHeight/2 - 400; // Allow panning up a bit
//        if(yOff > bbox.max.y - viewHeight/2) yOff = bbox.max.y - viewHeight/2;
//        BoundingBox bbox = game.getCityBoundingBox();
//        if(xOff>bbox.getRight())xOff = bbox.getRight();
//        if(xOff<bbox.getLeft())xOff = bbox.getLeft();
//        if(yOff>bbox.getBottom())yOff = bbox.getBottom();
//        if(yOff<bbox.getTop())yOff = bbox.getTop();
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="Render World">
            Renderer.pushModel(new Matrix4f().translate(getWidth()/2, getHeight()/2, 0));
//.scale(zoom, zoom, 1).translate(-xOff, -yOff, 0));
            game.panX = -xOff;
            game.panY = -yOff;
            game.zoom = zoom;
            game.fakeRender(deltaTime);
            Renderer.popModel();
//</editor-fold>

            synchronized(game){
                for(Structure structure : game.structures){
                    structure.mouseover = 0;
                }
            }
            game.renderBackground();
            Structure structure = game.getMouseoverStructure(getMouseX(), getMouseY());
            if(structure!=null){
                structure.mouseover = .1f;
            }
            if(game.selectedStructure!=null){
                game.selectedStructure.mouseover += .2;
            }
//        game.fakeRender(deltaTime);
            if(game instanceof Epilogue)return;
            Renderer.setColor(1, 1, 1, 1);
            super.render(deltaTime);
            //<editor-fold defaultstate="collapsed" desc="Updating Action Buttons">
            if(game.actionUpdateRequired==2){
                game.actionUpdateRequired = 0;
                components.removeAll(actionButtons);
                actionButtons.clear();
                actionButtonOffset = 20;
                for(Action action : game.getActions(this)){
                    actionButtonOffset += action.divider;
                    if(!action.isDivider()){
                        actionButtons.add(add(new MenuComponentActionButton(this, game, 0, actionButtons.size()*actionButtonHeight+actionButtonOffset, actionButtonWidth, actionButtonHeight, action)));
                        actionButtonOffset += action.divider;
                    }
                }
            }
            if(game.actionUpdateRequired==1){
                for(MenuComponentActionButton button : actionButtons){
                    button.update();
                }
            }
//</editor-fold>
            if(game.isPlayable()){
                Renderer.fillRect(getWidth()-100, getHeight()-200, getWidth(), getHeight(), ResourceManager.getTexture("/textures/gui/sidebar.png"));
                if(game.selectedStructure!=null){
                    String upgrades = "";
                    for(Upgrade u : game.selectedStructure.getBoughtUpgrades())
                        upgrades += "*";
                    textWithBackground(0, 0, actionButtonWidth, 20, upgrades+" "+game.selectedStructure.getName());
                }
                if(game.furnaceLevel<game.maxFurnaceLevel&&game.furnaceXP>=Math.pow(20, game.furnaceLevel+1)){
                    Renderer.setColor(0, (float)(Math.sin(game.tick/5d)/4+.75), 0, 1);
                    Renderer.fillRegularPolygon(getWidth()-100+5, getHeight()-100+5, 25, 5);
                    Renderer.setColor(1, 1, 1, 1);
                }
                Renderer.fillRect(getWidth()-100, getHeight()-100, getWidth(), getHeight(), ResourceManager.getTexture("/textures/furnace "+game.furnaceLevel+".png"));
                var mousePos = DizzyEngine.getLayer(FlatUI.class).cursorPosition[0];
                if(mousePos!=null&&mousePos.x>=getWidth()-100&&mousePos.y>=getHeight()-100&&game.furnaceLevel<Game.maxFurnaceLevel){
                    Renderer.setColor(0, 1, 0, .25f);
                    float percent = (float)(game.furnaceXP/Math.pow(20, game.furnaceLevel+1));
                    Renderer.fillRect(getWidth()-100, getHeight()-100, getWidth()-100+(100*percent), getHeight(), 0);
                    Renderer.setColor(1, 1, 1, 1);
                }
                Renderer.setColor(0, 0, 0, 1);
                Renderer.drawText(getWidth()-90, getHeight()-60, getWidth()-10, getHeight()-40, game.furnaceOre+" Ore");
                Renderer.drawText(getWidth()-90, getHeight()-40, getWidth()-10, getHeight()-20, game.furnaceCoal+" Coal");
                Renderer.drawText(getWidth()-90, getHeight()-20, getWidth()-10, getHeight(), game.furnaceLevel>=game.maxFurnaceLevel?"Maxed":"Level "+(game.furnaceLevel+1));
                Renderer.setColor(1, 1, 1, 1);
                if(game.isPlayable()){
                    for(int i = 0; i<game.resources.size(); i++){
                        int I = 1;
                        if(i==0)I = 0;
                        if(i==game.resources.size()-1)I = 2;
                        Renderer.fillRect(getWidth()-100, i*20, getWidth(), (i+1)*20+(I==2?5:0), ResourceManager.getTexture("/textures/gui/sidebar "+I+".png"));
                        Renderer.setColor(0, 0, 0, 1);
                        Renderer.drawText(getWidth()-80, i*20, getWidth(), (i+1)*20, game.resources.get(i).count+"");
                        Renderer.setColor(1, 1, 1, 1);
                        Renderer.fillRect(getWidth()-100, i*20, getWidth()-80, (i+1)*20, game.resources.get(i).item.getTexture());
                    }
                }
            }
            if(game.won){
                if(game.phase>0){
                    centeredTextWithBackground(0, 0, getWidth(), 35, "Congratulations! You have destroyed the alien mothership and saved the planet!");
                    if(game.winTimer<20&&"VictoryMusic1".equals(Sounds.nowPlaying())){
                        centeredTextWithBackground(0, 35, getWidth(), 85, "Only one problem remains...");
                    }
                }
            }else{
                int offset = 0;
                for(Iterator<Notification> it = game.notifications.iterator(); it.hasNext();){
                    Notification n = it.next();
                    float wide = Renderer.getStringWidth(n.toString(), 20);
                    float left = getWidth()/2-(wide/2*n.width);
                    float right = getWidth()/2+(wide/2*n.width);
                    int y = 20-n.height;
                    Renderer.setColor(0, 0, 0, .5f);
                    Renderer.bound(left, offset, right, offset+n.height);
                    Renderer.fillRect(getWidth()/2-wide/2, offset-y/2, getWidth()/2+wide/2, offset+20-y/2, 0);
                    Renderer.setColor(1, 1, 1, 1);
                    Renderer.drawCenteredText(getWidth()/2-wide/2, offset-y/2, getWidth()/2+wide/2, offset+20-y/2, n.toString());
                    offset += n.height;
                    if(n.isDead())it.remove();
                    Renderer.unBound();
                }
            }
            if(game.selectedStructure!=null&&game.selectedStructure.task!=null){
                for(int i = 0; i<game.selectedStructure.task.getDetails().length; i++){
                    textWithBackground(actionButtonWidth, 30*i, getWidth(), 30*(i+1), game.selectedStructure.task.getDetails()[i], game.selectedStructure.task.important);
                }
            }
            if(game.paused){
                Renderer.drawCenteredText(0, getHeight()/2-50, getWidth(), getHeight()/2+50, "Paused");
            }
            if(game.debugMode&&game.cheats){
                debugYOffset = 0;
                ArrayList<String> debugData = game.getDebugData();
                float textHeight = getHeight()/(debugData.size());
                debugText(textHeight, "("+getMouseX()+", "+getMouseY()+")");
                for(String str : debugData){
                    debugText(textHeight, str);
                }
            }
            if(phaseMarker!=null)phaseMarker.render(deltaTime);
            if(overlay!=null)overlay.render(deltaTime);
            Renderer.setColor(0, 0, 0, game.blackScreenOpacity);
            Renderer.fillRect(0, 0, getWidth(), getHeight(), 0);
            Renderer.setColor(1, 1, 1, 1);
        }
    }
    @Override
    public void onKey(int id, int key, int scancode, int action, int mods){
        if(game instanceof Epilogue)return;
        if(overlay!=null){
            overlay.onKey(id, key, scancode, action, mods);
            return;
        }
        super.onKey(id, key, scancode, action, mods);
        if(action!=GLFW_PRESS)return;
        switch(key){
            case Controls.hideSkyscrapers:
                game.hideSkyscrapers = !game.hideSkyscrapers;
                break;
            case Controls.showPowerNetworks:
                game.showPowerNetworks = !game.showPowerNetworks;
                break;
            case Controls.menu:
                openOverlay(new MenuIngame(this));
                game.paused = true;
                break;
            case Controls.pause:
                game.paused = !game.paused;
                break;
            case Controls.mute:
                Sounds.setVolume(1-Sounds.getVolume());
                if(Sounds.getVolume()<.01){
                    game.notify("Sound ", "Off", 50);
                }else{
                    game.notify("Sound ", "On", 50);
                }
                break;
            case Controls.cheat:
                if(mods==(GLFW_MOD_CONTROL|GLFW_MOD_SHIFT|GLFW_MOD_ALT))
                    game.cheats = !game.cheats;
                break;
        }
        if(game.cheats){
            if(mods==(GLFW_MOD_SHIFT|GLFW_MOD_ALT)&&DizzyEngine.isKeyDown(Controls.CHEAT_SECRET)){
                int secret = -1;
                switch(key){
                    case GLFW.GLFW_KEY_1:
                        secret = 0;
                        break;
                    case GLFW.GLFW_KEY_2:
                        secret = 1;
                        break;
                    case GLFW.GLFW_KEY_3:
                        secret = 2;
                        break;
                    case GLFW.GLFW_KEY_4:
                        secret = 3;
                        break;
                    case GLFW.GLFW_KEY_5:
                        secret = 4;
                        break;
                    case GLFW.GLFW_KEY_6:
                        secret = 5;
                        break;
                    case GLFW.GLFW_KEY_7:
                        secret = 6;
                        break;
                    case GLFW.GLFW_KEY_8:
                        secret = 7;
                        break;
                    case GLFW.GLFW_KEY_9:
                        secret = 8;
                        break;
                    case GLFW.GLFW_KEY_0:
                        secret = 9;
                        break;
                    case GLFW.GLFW_KEY_MINUS:
                        secret = 10;
                        break;
                    case GLFW.GLFW_KEY_EQUAL:
                        secret = 11;
                        break;
                }
                if(secret>=0){
                    game.notify("Cheat: Secret #"+(secret+1));
                    game.secretWaiting = secret;
                }
            }
            if(!actionButtons.isEmpty()&&game.selectedStructure!=null){
                int hit = -1;
                switch(key){
                    case GLFW.GLFW_KEY_1:
                        hit = 0;
                        break;
                    case GLFW.GLFW_KEY_2:
                        hit = 1;
                        break;
                    case GLFW.GLFW_KEY_3:
                        hit = 2;
                        break;
                    case GLFW.GLFW_KEY_4:
                        hit = 3;
                        break;
                    case GLFW.GLFW_KEY_5:
                        hit = 4;
                        break;
                    case GLFW.GLFW_KEY_6:
                        hit = 5;
                        break;
                    case GLFW.GLFW_KEY_7:
                        hit = 6;
                        break;
                    case GLFW.GLFW_KEY_8:
                        hit = 7;
                        break;
                    case GLFW.GLFW_KEY_9:
                        hit = 8;
                        break;
                    case GLFW.GLFW_KEY_0:
                        hit = 9;
                        break;
                    case GLFW.GLFW_KEY_MINUS:
                        hit = 10;
                        break;
                    case GLFW.GLFW_KEY_EQUAL:
                        hit = 11;
                        break;
                }
                if(hit>=0){
                    if(actionButtons.size()>hit){
                        actionButtons.get(hit).perform();
                        if(game.selectedStructure.task!=null){
                            game.notify("Cheat: Instant Completion");
                            game.selectedStructure.task.progress = game.selectedStructure.task.time-1;
                        }
                    }
                }
            }
            switch(key){
                case Controls.CHEAT_LOSE -> {
                    if(DizzyEngine.isKeyDown(GLFW.GLFW_KEY_LEFT_CONTROL)&&DizzyEngine.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT)){
                        game.notify("Cheat: Losing Epilogue");
                        new MenuLost(game).open();
                    }
                }
                case Controls.CHEAT_DEBUG ->
                    game.debugMode = !game.debugMode;
                case Controls.CHEAT_PHASE -> {
                    if(DizzyEngine.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT)){
                        int oldPhase = game.phase;
                        game.phase(game.phase+1, false);
                        if(game.phase!=oldPhase){
                            game.notify("Cheat: Advance to phase "+game.phase);
                        }
                        game.paused = false;
                    }
                }
                case Controls.CHEAT_RESOURCES -> {
                    game.notify("Cheat: Resources");
                    for(ItemStack resource : game.resources){
                        resource.count += 100;
                    }
                }
                case Controls.CHEAT_CLOUD -> {
                    game.notify("Cheat: Cloud");
                    game.addCloud(getMouseX(), (int)(getMouseY()+Particle.CLOUD_HEIGHT*game.getShearFactor()));
                }
                case Controls.CHEAT_FOG -> {
                    game.notify("Cheat: Fog");
                    game.startFog();
                }
                case Controls.CHEAT_WORKER -> {
                    game.notify("Cheat: Add Worker");
                    game.addWorker();
                }
                case Controls.CHEAT_ENEMY -> {
                    if(DizzyEngine.isKeyDown(Controls.CHEAT_SECRET)&&DizzyEngine.isKeyDown(GLFW.GLFW_KEY_1)){
                        game.notify("Cheat: Shooting Star");
                        game.addShootingStar(new ShootingStar(game, getMouseX()-25, getMouseY()-25));
                    }else if(DizzyEngine.isKeyDown(GLFW.GLFW_KEY_LEFT_CONTROL)){
                        Enemy.strength++;
                        game.notify("Cheat: Enemy Strength: ", Enemy.strength+"");
                    }else{
                        if(DizzyEngine.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT)){
                            game.notify("Cheat: Add Enemy");
                            game.addRandomEnemy();
                        }else{
                            game.notify("Cheat: Spawn Asteroid");
                            switch(game.rand.nextInt(3)){
                                case 0 -> game.addAsteroid(new Asteroid(game, getMouseX(), getMouseY(), AsteroidMaterial.COAL, 1));
                                case 1 -> game.addAsteroid(new Asteroid(game, getMouseX(), getMouseY(), AsteroidMaterial.STONE, 1));
                                case 2 -> game.addAsteroid(new Asteroid(game, getMouseX(), getMouseY(), AsteroidMaterial.IRON, 1));
                            }
                        }
                    }
                }
                case Controls.CHEAT_PEACE -> {
                    game.notify("Cheat: Disable Meteor shower");
                    for(Enemy e : game.enemies){
                        if(e instanceof EnemyMothership){
                            game.notify("Cheat: Damage Mothership");
                            EnemyMothership m = (EnemyMothership)e;
                            m.health -= m.maxHealth/4;
                        }
                    }
                    game.meteorShower = false;
                    game.meteorShowerTimer += 20*60*60;
                    game.meteorTimer += 20*60*60;
                }
            }
        }else{
            if(!actionButtons.isEmpty()&&game.selectedStructure!=null){
                int hit = switch(key){
                    case GLFW.GLFW_KEY_1 ->
                        0;
                    case GLFW.GLFW_KEY_2 ->
                        1;
                    case GLFW.GLFW_KEY_3 ->
                        2;
                    case GLFW.GLFW_KEY_4 ->
                        3;
                    case GLFW.GLFW_KEY_5 ->
                        4;
                    case GLFW.GLFW_KEY_6 ->
                        5;
                    case GLFW.GLFW_KEY_7 ->
                        6;
                    case GLFW.GLFW_KEY_8 ->
                        7;
                    case GLFW.GLFW_KEY_9 ->
                        8;
                    case GLFW.GLFW_KEY_0 ->
                        9;
                    case GLFW.GLFW_KEY_MINUS ->
                        10;
                    case GLFW.GLFW_KEY_EQUAL ->
                        11;
                    default ->
                        -1;
                };
                if(hit>=0){
                    if(actionButtons.size()>hit){
                        if(actionButtons.get(hit).enabled&&!actionButtons.get(hit).action.isImportant())
                            actionButtons.get(hit).perform();
                    }
                }
            }
        }
    }
    @Override
    public void onMouseButton(int id, Vector2d pos, int button, int action, int mods){
        if(game instanceof Epilogue)return;
        if(overlay!=null){
            overlay.onMouseButton(id, pos, button, action, mods);
            return;
        }
        if(phaseMarker!=null){
            phaseMarker.onMouseButton(id, pos, button, action, mods);
            return;
        }
        super.onMouseButton(id, pos, button, action, mods);
        for(Component c : components){
            if(c instanceof Button){
                if(new BoundingBox((int)c.x, (int)c.y, (int)c.getWidth(), (int)c.getHeight()).contains((int)x, (int)y))
                    return;//TODO just use components like a normal person
            }
        }
        if(action!=GLFW_PRESS)return;
        game.click(getMouseX(pos.x), getMouseY(pos.y), button);
        if(button==0&&game.isPlayable()){
            int amount = 1;
            if(DizzyEngine.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT))amount *= 100;
            if(DizzyEngine.isKeyDown(GLFW.GLFW_KEY_LEFT_CONTROL))amount *= 10;
            if(new BoundingBox((int)getWidth()-100, 20, (int)getWidth()-80, 40).contains((int)pos.x, (int)pos.y)){
                game.addCoalToFurnace(amount);
            }
            if(new BoundingBox((int)getWidth()-100, 40, (int)getWidth()-80, 60).contains((int)pos.x, (int)pos.y)){
                game.addIronToFurnace(amount);
            }
            if(game.furnaceLevel<game.maxFurnaceLevel&&game.furnaceXP>=Math.pow(20, game.furnaceLevel+1)&&DizzyEngine.getLayer(FlatUI.class).cursorPosition[0].x>=getWidth()-100&&DizzyEngine.getLayer(FlatUI.class).cursorPosition[0].y>=getHeight()-100){
                game.furnaceLevel++;
            }
        }
    }
    @Override
    public void onCursorPos(int id, double xpos, double ypos){
        if(game instanceof Epilogue)return;
        if(overlay!=null){
            overlay.onCursorPos(id, xpos, ypos);
            return;
        }
        if(phaseMarker!=null){
            phaseMarker.onCursorPos(id, xpos, ypos);
            return;
        }
        super.onCursorPos(id, xpos, ypos);
    }
    @Override
    public boolean onScroll(int id, Vector2d pos, double dx, double dy){
        if(game instanceof Epilogue)return true;
        if(overlay!=null){
            return overlay.onScroll(id, pos, dx, dy);
        }
        if(phaseMarker!=null){
            return phaseMarker.onScroll(id, pos, dx, dy);
        }
        if(super.onScroll(id, pos, dx, dy))return true;
        zoom += dy*zoomFac;
        zoom = Math.min(maxZoom, Math.max(minZoom, zoom));
        return true;
    }
    public String debugText(float textHeight, String text){
        Renderer.setColor(0, 0, 0, .5f);
        Renderer.fillRect(0, debugYOffset, Renderer.getStringWidth(text, textHeight-1)+1, debugYOffset+textHeight, 0);
        Renderer.setColor(1, 1, 1, 1);
        Renderer.drawText(1, debugYOffset+1, getWidth()-1, debugYOffset+textHeight-1, text);//TODO withWrap
        text = null;
        debugYOffset += textHeight;
        return text;
    }
    private void textWithBackground(float left, float top, float right, float bottom, String str){
        textWithBackground(left, top, right, bottom, str, false);
    }
    private void textWithBackground(float left, float top, float right, float bottom, String str, boolean pulsing){
        Renderer.setColor(0, 0, 0, 0.75f);
        Renderer.fillRect(left, top, Renderer.getStringWidth(str, bottom-top)+left, bottom, 0);
        Renderer.setColor(1, 1, 1, 1);
        if(pulsing){
            Renderer.setColor((float)(Math.sin(game.tick/5f)/4+.75f), 0, 0, 1);
        }
        Renderer.drawText(left, top, right, bottom, str);
        Renderer.setColor(1, 1, 1, 1);
    }
    public void centeredTextWithBackground(float left, float top, float right, float bottom, String str){
        Renderer.setColor(0, 0, 0, 0.75f);
        Renderer.fillRect(left, top, right, bottom, 0);
        Renderer.setColor(1, 1, 1, 1);
        Renderer.drawCenteredText(left, top, right, bottom, str);
    }
    private void createPhaseMarker(){
        components.remove(phaseMarker = add(new MenuComponentPhaseMarker(this)));
    }
    public void openOverlay(MenuComponentOverlay lay){
        if(overlay!=null)return;
        components.remove(overlay = add(lay));
    }
    public int getMouseX(double x){
        x -= getWidth()/2;
        double scaledX = x/zoom;
        return (int)(scaledX+xOff);
    }
    public int getMouseY(double y){
        y -= getHeight()/2;
        double scaledY = y/zoom;
        return (int)(scaledY+yOff);
    }
    public int getMouseX(){
        var pos = DizzyEngine.getLayer(FlatUI.class).cursorPosition[0];
        if(pos==null)return -1;
        return getMouseX(pos.x);
    }
    public int getMouseY(){
        var pos = DizzyEngine.getLayer(FlatUI.class).cursorPosition[0];
        if(pos==null)return -1;
        return getMouseY(pos.y);
    }
    @Override
    public void onMenuOpened(){
        var oldGame = DizzyEngine.getLayer(Game.class);
        if(oldGame!=null&&oldGame!=game){
            DizzyEngine.removeLayer(oldGame);
        }
        if(oldGame!=game)DizzyEngine.addLayer(game);
    }
    @Override
    public void onMenuClosed(){
        DizzyEngine.removeLayer(game);
    }
}
