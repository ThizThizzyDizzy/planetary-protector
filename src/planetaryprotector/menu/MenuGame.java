package planetaryprotector.menu;
import java.util.ArrayList;
import java.util.Iterator;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import planetaryprotector.Controls;
import planetaryprotector.Core;
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
import simplelibrary.opengl.gui.GUI;
import simplelibrary.opengl.gui.Menu;
import simplelibrary.opengl.gui.components.MenuComponentButton;
import planetaryprotector.game.Notification;
import planetaryprotector.item.Item;
import planetaryprotector.item.ItemStack;
import planetaryprotector.menu.component.MenuComponentActionButton;
import planetaryprotector.menu.component.MenuComponentFalling;
import planetaryprotector.menu.component.MenuComponentRising;
import planetaryprotector.menu.ingame.MenuComponentOverlay;
import planetaryprotector.menu.ingame.MenuIngame;
import planetaryprotector.structure.Structure;
import planetaryprotector.structure.Structure.Upgrade;
import simplelibrary.font.FontManager;
import simplelibrary.opengl.ImageStash;
import simplelibrary.opengl.gui.components.MenuComponent;
public class MenuGame extends Menu{
    public ArrayList<MenuComponentActionButton> actionButtons = new ArrayList<>();
    private static final int actionButtonWidth = 375;
    private static final int actionButtonHeight = 60;
    public int actionButtonOffset = 0;
    public double debugYOffset = 0;
    public MenuComponentOverlay overlay;
    public Game game;
    private MenuComponentPhaseMarker phaseMarker;
    private double xOff = 0;
    private double yOff = 0;
    private double zoom = 1;
    public static final double minZoom = .5;
    public static final double maxZoom = 2;
    private double zoomFac = .05;
    private double panFac = 7;
    public MenuGame(GUI gui, Game game){
        super(gui, null);
        this.game = game;
    }
    @Override
    public void renderBackground(){
        for(Structure structure : game.structures){
            structure.mouseover = 0;
        }
        game.renderBackground();
        Structure structure = game.getMouseoverStructure(getMouseX(), getMouseY());
        if(structure!=null){
            structure.mouseover = .1;
        }
        if(game.selectedStructure!=null){
            game.selectedStructure.mouseover+=.2;
        }
    }
    @Override
    public synchronized void render(int millisSinceLastTick){
        game.render(millisSinceLastTick);
        if(game instanceof Epilogue)return;
        GL11.glColor4d(1, 1, 1, 1);
        super.render(millisSinceLastTick);
        //<editor-fold defaultstate="collapsed" desc="Updating Action Buttons">
        if(game.actionUpdateRequired==2){
            game.actionUpdateRequired = 0;
            components.removeAll(actionButtons);
            actionButtons.clear();
            actionButtonOffset = 20;
            for(Action action : game.getActions(this)){
                actionButtonOffset+=action.divider;
                if(!action.isDivider()){
                    actionButtons.add(add(new MenuComponentActionButton(this, game, 0, actionButtons.size()*actionButtonHeight+actionButtonOffset, actionButtonWidth, actionButtonHeight, action)));
                    actionButtonOffset+=action.divider;
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
            drawRect(gui.helper.displayWidth()-100, gui.helper.displayHeight()-200, gui.helper.displayWidth(), gui.helper.displayHeight(), ImageStash.instance.getTexture("/textures/gui/sidebar.png"));
            if(game.selectedStructure!=null){
                String upgrades = "";
                for(Upgrade u : game.selectedStructure.getBoughtUpgrades())upgrades+="*";
                textWithBackground(0, 0, actionButtonWidth, 20, upgrades+" "+game.selectedStructure.getName());
            }
            if(game.furnaceLevel<game.maxFurnaceLevel&&game.furnaceXP>=Math.pow(20, game.furnaceLevel+1)){
                GL11.glColor4d(0, Math.sin(game.tick/5d)/4+.75, 0, 1);
                Game.drawRegularPolygon(gui.helper.displayWidth()-100+5, gui.helper.displayHeight()-100+5, 5, 25, 0);
                GL11.glColor4d(1, 1, 1, 1);
            }
            drawRect(gui.helper.displayWidth()-100, gui.helper.displayHeight()-100, gui.helper.displayWidth(), gui.helper.displayHeight(), ImageStash.instance.getTexture("/textures/furnace "+game.furnaceLevel+".png"));
            if(gui.mouseX>=gui.helper.displayWidth()-100&&gui.mouseY>=gui.helper.displayHeight()-100&&game.furnaceLevel<Game.maxFurnaceLevel){
                GL11.glColor4d(0, 1, 0, .25);
                double percent = game.furnaceXP/Math.pow(20, game.furnaceLevel+1);
                drawRect(gui.helper.displayWidth()-100,gui.helper.displayHeight()-100,gui.helper.displayWidth()-100+(100*percent), gui.helper.displayHeight(), 0);
                GL11.glColor4d(1, 1, 1, 1);
            }
            GL11.glColor4d(0, 0, 0, 1);
            drawText(gui.helper.displayWidth()-90, gui.helper.displayHeight()-60, gui.helper.displayWidth()-10, gui.helper.displayHeight()-40, game.furnaceOre+" Ore");
            drawText(gui.helper.displayWidth()-90, gui.helper.displayHeight()-40, gui.helper.displayWidth()-10, gui.helper.displayHeight()-20, game.furnaceCoal+" Coal");
            drawText(gui.helper.displayWidth()-90, gui.helper.displayHeight()-20, gui.helper.displayWidth()-10, gui.helper.displayHeight(), game.furnaceLevel>=game.maxFurnaceLevel?"Maxed":"Level "+(game.furnaceLevel+1));
            GL11.glColor4d(1, 1, 1, 1);
            if(game.isPlayable()){
                for(int i = 0; i<game.resources.size(); i++){
                    int I = 1;
                    if(i==0)I = 0;
                    if(i==game.resources.size()-1)I = 2;
                    drawRect(gui.helper.displayWidth()-100, i*20, gui.helper.displayWidth(), (i+1)*20+(I==2?5:0), ImageStash.instance.getTexture("/textures/gui/sidebar "+I+".png"));
                GL11.glColor4d(0, 0, 0, 1);
                    drawText(gui.helper.displayWidth()-80, i*20, gui.helper.displayWidth(), (i+1)*20, game.resources.get(i).count+"");
                GL11.glColor4d(1, 1, 1, 1);
                    drawRect(gui.helper.displayWidth()-100, i*20, gui.helper.displayWidth()-80, (i+1)*20, game.resources.get(i).item.getTexture());
                }
            }
        }
        if(game.won){
            if(game.phase>0){
                centeredTextWithBackground(0, 0, gui.helper.displayWidth(), 35, "Congratulations! You have destroyed the alien mothership and saved the planet!");
                if(game.winTimer<20&&"VictoryMusic1".equals(Sounds.nowPlaying())){
                    centeredTextWithBackground(0, 35, gui.helper.displayWidth(), 85, "Only one problem remains...");
                }
            }
        }else{
            int offset = 0;
            for(Iterator<Notification> it = game.notifications.iterator(); it.hasNext();){
                Notification n = it.next();
                double wide = FontManager.getLengthForStringWithHeight(n.toString(), 20);
                double left = gui.helper.displayWidth()/2-(wide/2*n.width);
                double right = gui.helper.displayWidth()/2+(wide/2*n.width);
                int y = 20-n.height;
                GL11.glColor4d(0,0,0,.5);
                drawRectWithBounds(gui.helper.displayWidth()/2-wide/2, offset-y/2, gui.helper.displayWidth()/2+wide/2, offset+20-y/2, left, offset, right, offset+n.height, 0);
                GL11.glColor4d(1,1,1,1);
                drawCenteredTextWithBounds(gui.helper.displayWidth()/2-wide/2, offset-y/2, gui.helper.displayWidth()/2+wide/2, offset+20-y/2, left, offset, right, offset+n.height, n.toString());
                offset+=n.height;
                if(n.isDead())it.remove();
            }
        }
        if(game.selectedStructure!=null&&game.selectedStructure.task!=null){
            for(int i = 0; i<game.selectedStructure.task.getDetails().length; i++){
                textWithBackground(actionButtonWidth, 30*i, gui.helper.displayWidth(), 30*(i+1), game.selectedStructure.task.getDetails()[i], game.selectedStructure.task.important);
            }
        }
        if(game.paused){
            drawCenteredText(0, gui.helper.displayHeight()/2-50, gui.helper.displayWidth(), gui.helper.displayHeight()/2+50, "Paused");
        }
        if(Core.debugMode&&game.cheats){
            debugYOffset = 0;
            ArrayList<String> debugData = game.getDebugData();
            double textHeight = gui.helper.displayHeight()/(debugData.size());
            debugText(textHeight, "("+getMouseX()+", "+getMouseY()+")");
            for(String str : debugData){
                debugText(textHeight, str);
            }
        }
        if(phaseMarker!=null)phaseMarker.render(millisSinceLastTick);
        if(overlay!=null)overlay.render(millisSinceLastTick);
    }
    @Override
    public void renderForeground(){
        GL11.glColor4d(0, 0, 0, game.blackScreenOpacity);
        drawRect(0, 0, gui.helper.displayWidth(), gui.helper.displayHeight(), 0);
        GL11.glColor4d(1, 1, 1, 1);
    }
    @Override
    public synchronized void keyEvent(int key, int scancode, boolean isPress, boolean isRepeat, int modifiers){
        if(game instanceof Epilogue)return;
        if(overlay!=null){
            overlay.keyEvent(key, scancode, isPress, isRepeat, modifiers);
            return;
        }
        super.keyEvent(key, scancode, isPress, isRepeat, modifiers);
        if(!isPress)return;
        if(!isRepeat){
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
                    if(gui.keyboardWereDown.contains(GLFW.GLFW_KEY_LEFT_CONTROL)&&gui.keyboardWereDown.contains(GLFW.GLFW_KEY_LEFT_SHIFT)&&gui.keyboardWereDown.contains(GLFW.GLFW_KEY_LEFT_ALT)){
                        game.cheats = !game.cheats;
                    }
                    break;
            }
        }
        if(game.cheats){
            if(gui.keyboardWereDown.contains(GLFW.GLFW_KEY_LEFT_SHIFT)&&gui.keyboardWereDown.contains(GLFW.GLFW_KEY_LEFT_ALT)&&gui.keyboardWereDown.contains(Controls.CHEAT_SECRET)){
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
                case Controls.CHEAT_LOSE:
                    if(gui.keyboardWereDown.contains(GLFW.GLFW_KEY_LEFT_CONTROL)&&gui.keyboardWereDown.contains(GLFW.GLFW_KEY_LEFT_SHIFT)&&!isRepeat){
                        game.notify("Cheat: Losing Epilogue");
                        gui.open(new MenuLost(gui, game));
                    }
                    break;
                case Controls.CHEAT_DEBUG:
                    if(!isRepeat)Core.debugMode = !Core.debugMode;
                    break;
                case Controls.CHEAT_PHASE:
                    if(gui.keyboardWereDown.contains(GLFW.GLFW_KEY_LEFT_SHIFT)){
                        int oldPhase = game.phase;
                        game.phase(game.phase+1, false);
                        if(game.phase!=oldPhase){
                            game.notify("Cheat: Advance to phase "+game.phase);
                        }
                        game.paused = false;
                    }
                    break;
                case Controls.CHEAT_RESOURCES:
                    game.notify("Cheat: Resources");
                    for(ItemStack resource : game.resources){
                        resource.count+=100;
                    }
                    break;
                case Controls.CHEAT_CLOUD:
                    game.notify("Cheat: Cloud");
                    game.addCloud(getMouseX(),getMouseY());
                    break;
                case Controls.CHEAT_FOG:
                    game.notify("Cheat: Fog");
                    game.startFog();
                    break;
                case Controls.CHEAT_WORKER:
                    game.notify("Cheat: Add Worker");
                    game.addWorker();
                    break;
                case Controls.CHEAT_ENEMY:
                    if(gui.keyboardWereDown.contains(Controls.CHEAT_SECRET)&&gui.keyboardWereDown.contains(GLFW.GLFW_KEY_1)){
                        game.notify("Cheat: Shooting Star");
                        game.addShootingStar(new ShootingStar(game, getMouseX()-25, getMouseY()-25));
                    }else if(gui.keyboardWereDown.contains(GLFW.GLFW_KEY_LEFT_CONTROL)){
                        Enemy.strength++;
                        game.notify("Cheat: Enemy Strength: ", Enemy.strength+"");
                    }else{
                        if(gui.keyboardWereDown.contains(GLFW.GLFW_KEY_LEFT_SHIFT)){
                            game.notify("Cheat: Add Enemy");
                            game.addRandomEnemy();
                        }else{
                            game.notify("Cheat: Spawn Asteroid");
                            switch(game.rand.nextInt(3)){
                                case 0:
                                    game.addAsteroid(new Asteroid(game, getMouseX()-25, getMouseY()-25, AsteroidMaterial.COAL, 1));
                                    break;
                                case 1:
                                    game.addAsteroid(new Asteroid(game, getMouseX()-25, getMouseY()-25, AsteroidMaterial.STONE, 1));
                                    break;
                                case 2:
                                    game.addAsteroid(new Asteroid(game, getMouseX()-25, getMouseY()-25, AsteroidMaterial.IRON, 1));
                                    break;
                            }
                        }
                    }
                    break;
                case Controls.CHEAT_PEACE:
                    game.notify("Cheat: Disable Meteor shower");
                    for(Enemy e : game.enemies){
                        if(e instanceof EnemyMothership){
                            game.notify("Cheat: Damage Mothership");
                            EnemyMothership m = (EnemyMothership) e;
                            m.health-=m.maxHealth/4;
                        }
                    }
                    game.meteorShower = false;
                    game.meteorShowerTimer += 20*60*60;
                    game.meteorTimer += 20*60*60;
                    break;
            }
        }else if(isPress){
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
                        if(actionButtons.get(hit).enabled&&!actionButtons.get(hit).action.isImportant())actionButtons.get(hit).perform();
                    }
                }
            }
        }
    }
    @Override
    public synchronized void onMouseButton(double x, double y, int button, boolean pressed, int mods){
        if(game instanceof Epilogue)return;
        if(overlay!=null){
            overlay.onMouseButton(x, y, button, pressed, mods);
            return;
        }
        if(phaseMarker!=null){
            phaseMarker.onMouseButton(x, y, button, pressed, mods);
            return;
        }
        super.onMouseButton(x, y, button, pressed, mods);
        for(MenuComponent c : components){
            if(c instanceof MenuComponentButton){
                if(Core.isPointWithinComponent(x, y, c))return;
            }
        }
        if(pressed)game.click(getMouseX(x), getMouseY(y), button);
        if(button==0&&pressed&&game.isPlayable()){
            int amount = 1;
            if(gui.keyboardWereDown.contains(GLFW.GLFW_KEY_LEFT_SHIFT))amount*=100;
            if(gui.keyboardWereDown.contains(GLFW.GLFW_KEY_LEFT_CONTROL))amount*=10;
            if(isClickWithinBounds(x, y, gui.helper.displayWidth()-100, 20, gui.helper.displayWidth()-80, 40)){
                game.addCoalToFurnace(amount);
            }
            if(isClickWithinBounds(x, y, gui.helper.displayWidth()-100, 40, gui.helper.displayWidth()-80, 60)){
                game.addIronToFurnace(amount);
            }
            if(game.furnaceLevel<game.maxFurnaceLevel&&game.furnaceXP>=Math.pow(20, game.furnaceLevel+1)&&gui.mouseX>=gui.helper.displayWidth()-100&&gui.mouseY>=gui.helper.displayHeight()-100){
                game.furnaceLevel++;
            }
        }
    }
    @Override
    public synchronized void onMouseMove(double x, double y){
        if(game instanceof Epilogue)return;
        if(overlay!=null){
            overlay.onMouseMove(x, y);
            return;
        }
        if(phaseMarker!=null){
            phaseMarker.onMouseMove(x, y);
            return;
        }
        super.onMouseMove(x, y);
        for(MenuComponent c : components){
            if(c instanceof MenuComponentButton){
                if(Core.isPointWithinComponent(x, y, c))return;
            }
        }
    }
    @Override
    public synchronized boolean onMouseScrolled(double x, double y, double dx, double dy){
        if(game instanceof Epilogue)return true;
        if(overlay!=null){
            return overlay.onMouseScrolled(x, y, dx, dy);
        }
        if(phaseMarker!=null){
            return phaseMarker.onMouseScrolled(x, y, dx, dy);
        }
        super.onMouseScrolled(x, y, dx, dy);
        for(MenuComponent c : components){
            if(c instanceof MenuComponentButton){
                if(Core.isPointWithinComponent(x, y, c))return true;
            }
        }
        zoom+=dy*zoomFac;
        zoom = Math.min(maxZoom, Math.max(minZoom, zoom));
        return true;
    }
    @Override
    public synchronized void tick(){
        if(overlay!=null)overlay.tick();
        if(game.updatePhaseMarker){
            game.paused = true;
            game.updatePhaseMarker = false;
            createPhaseMarker();
        }
        if(phaseMarker!=null){
            phaseMarker.tick();
            if(phaseMarker.opacity<0)phaseMarker = null;
        }
        super.tick();
        //<editor-fold defaultstate="collapsed" desc="Discord">
        Core.discordState = "";
        Core.discordSmallImageKey = "";
        Core.discordSmallImageText = "";
        switch(game.phase){
            case 1:
                Core.discordDetails = "Phase 1 - Armogeddon";
                Core.discordLargeImageKey = "base";
                Core.discordLargeImageText = "Phase 1 - Armogeddon";
                break;
            case 2:
                Core.discordDetails = "Phase 2 - Reconstruction";
                Core.discordLargeImageKey = "skyscraper";
                Core.discordLargeImageText = "Phase 2 - Reconstruction";
                int maxPop = game.calculatePopulationCapacity();
                Core.discordState = "Pop. Cap.: "+maxPop/1000+"k/"+game.targetPopulation/1000+"k ("+Math.round(maxPop/(double)game.targetPopulation*10000D)/100D+"%)";
                break;
            case 3:
                Core.discordDetails = "Phase 3 - Repopulation";
                Core.discordLargeImageKey = "city";
                Core.discordLargeImageText = "Phase 3 - Repopulation";
                int pop = game.calculatePopulation();
                maxPop = game.calculatePopulationCapacity();
                Core.discordState = "Population: "+pop/1000+"k/"+maxPop/1000+"k ("+Math.round(pop/(double)maxPop*10000D)/100D+"%)";
                break;
            case 4:
                int mothershipPhase = 0;
                for(Enemy e : game.enemies){
                    if(e instanceof EnemyMothership){
                        mothershipPhase = Math.max(mothershipPhase, ((EnemyMothership) e).phase);
                    }
                }
                if(mothershipPhase>0){
                    Core.discordDetails = "Boss Fight - Phase "+mothershipPhase;
                    Core.discordLargeImageText = "Boss Fight - Phase "+mothershipPhase;
                    switch(mothershipPhase){
                        case 1:
                            Core.discordLargeImageKey = "mothership_1";
                            break;
                        case 2:
                            Core.discordLargeImageKey = "mothership_2";
                            break;
                        case 3:
                            Core.discordLargeImageKey = "mothership_3";
                            break;
                        case 4:
                            Core.discordLargeImageKey = "mothership_4";
                            break;
                    }
                }
                break;
        }
        if(game.meteorShower){
            Core.discordState = "Meteor Shower!";
            Core.discordSmallImageKey = "asteroid_stone";
            Core.discordSmallImageText = "Meteor Shower!";
        }
        if(game.lost){
            Core.discordState = "Game Over";
        }
        if(game.won){
            Core.discordState = "Victory!";
        }
//</editor-fold>
        if(game.fading)game.blackScreenOpacity+=0.01;
        if(!game.paused){
            game.tick();
            game.story.tick(game);
        }
        if(game.lost&&game.phase>3){
            if(game.lostTimer>game.loseSongLength/10){
                if(Sounds.songTimer()<game.loseSongLength/20){
                    if(game.lostTimer>game.loseSongLength/20+20*5){
                        gui.open(new MenuLost(gui, game));
                    }
                }
            }
        }
        if(game.blackScreenOpacity>=1){
            Epilogue g = new Epilogue();
            g.blackScreenOpacity = 1;
            g.fading = false;
            gui.open(new MenuGame(gui, g));
        }
        if(game.addingIron>0){
            add(new MenuComponentFalling(this, gui.helper.displayWidth()-90+game.rand.nextInt(60), gui.helper.displayHeight()-180+game.rand.nextInt(50), Item.ironOre));
            game.addingIron--;
        }
        if(game.addingCoal>0){
            add(new MenuComponentFalling(this, gui.helper.displayWidth()-90+game.rand.nextInt(60), gui.helper.displayHeight()-180+game.rand.nextInt(50), Item.coal));
            game.addingCoal--;
        }
        if(game.smeltingIron>0){
            add(new MenuComponentRising(this, gui.helper.displayWidth()-90+game.rand.nextInt(60), gui.helper.displayHeight()-20+game.rand.nextInt(10), Item.ironIngot));
        }
        if(gui.keyboardWereDown.contains(Controls.left)){
            xOff-=maxZoom/zoom*panFac;
        }
        if(gui.keyboardWereDown.contains(Controls.up)){
            yOff-=maxZoom/zoom*panFac;
        }
        if(gui.keyboardWereDown.contains(Controls.right)){
            xOff+=maxZoom/zoom*panFac;
        }
        if(gui.keyboardWereDown.contains(Controls.down)){
            yOff+=maxZoom/zoom*panFac;
        }
        BoundingBox bbox = game.getCityBoundingBox();
        if(xOff>bbox.getRight())xOff = bbox.getRight();
        if(xOff<bbox.getLeft())xOff = bbox.getLeft();
        if(yOff>bbox.getBottom())yOff = bbox.getBottom();
        if(yOff<bbox.getTop())yOff = bbox.getTop();
    }
    @Override
    public void buttonClicked(MenuComponentButton button){
        for(MenuComponentActionButton actionButton : actionButtons){
            if(button==actionButton){
                actionButton.perform();
                game.actionUpdateRequired = 2;
            }
        }
    }
    public String debugText(double textHeight, String text){
        GL11.glColor4d(0, 0, 0, .5);
        drawRect(0, debugYOffset, FontManager.getLengthForStringWithHeight(text, textHeight-1)+1, debugYOffset+textHeight, 0);
        GL11.glColor4d(1, 1, 1, 1);
        text = drawTextWithWrap(1, debugYOffset+1, gui.helper.displayWidth()-1, debugYOffset+textHeight-1, text);
        debugYOffset+=textHeight;
        return text;
    }
    private void textWithBackground(double left, double top, double right, double bottom, String str){
        textWithBackground(left, top, right, bottom, str, false);
    }
    private void textWithBackground(double left, double top, double right, double bottom, String str, boolean pulsing){
        GL11.glColor4d(0, 0, 0, 0.75);
        drawRect(left, top, simplelibrary.font.FontManager.getLengthForStringWithHeight(str, bottom-top)+left, bottom, 0);
        GL11.glColor4d(1, 1, 1, 1); 
        if(pulsing){
            GL11.glColor4d(Math.sin(game.tick/5d)/4+.75, 0, 0, 1);
        }
        drawText(left,top,right,bottom, str);
        GL11.glColor4d(1, 1, 1, 1);
    }
    public void centeredTextWithBackground(double left, double top, double right, double bottom, String str) {
        GL11.glColor4d(0, 0, 0, 0.75);
        drawRect(left, top, right, bottom, 0);
        GL11.glColor4d(1, 1, 1, 1);
        drawCenteredText(left,top,right,bottom, str);
    }
    private void createPhaseMarker(){
        components.remove(phaseMarker = add(new MenuComponentPhaseMarker(this)));
    }
    public void openOverlay(MenuComponentOverlay lay){
        if(overlay!=null)return;
        components.remove(overlay = add(lay));
    }
    public void renderWorld(int millisSinceLastTick){
        GL11.glPushMatrix();
        GL11.glTranslated(gui.helper.displayWidth()/2, gui.helper.displayHeight()/2, 0);
        GL11.glScaled(zoom, zoom, 1);
        GL11.glTranslated(-xOff, -yOff, 0);
        game.renderWorld(millisSinceLastTick);
        BoundingBox bbox = game.getWorldBoundingBox();
        GL11.glColor4d(0, 0, 0, 1);
        drawRect(bbox.getLeft()-gui.helper.displayWidth(),bbox.getTop()-gui.helper.displayHeight(),bbox.getLeft(),bbox.getBottom()+gui.helper.displayHeight(),0);//left
        drawRect(bbox.getRight(), bbox.getTop()-gui.helper.displayHeight(), bbox.getRight()+gui.helper.displayWidth(), bbox.getBottom()+gui.helper.displayHeight(), 0);//right
        drawRect(bbox.getLeft(),bbox.getTop()-gui.helper.displayHeight(),bbox.getRight(),bbox.getTop(),0);//top
        drawRect(bbox.getLeft(),bbox.getBottom(),bbox.getRight(),bbox.getBottom()+gui.helper.displayHeight(),0);//bottom
        GL11.glPopMatrix();
    }
    public int getMouseX(double x){
        x-=gui.helper.displayWidth()/2;
        double scaledX = x/zoom;
        return (int)(scaledX+xOff);
    }
    public int getMouseY(double y){
        y-=gui.helper.displayHeight()/2;
        double scaledY = y/zoom;
        return (int)(scaledY+yOff);
    }
    public int getMouseX(){
        return getMouseX(gui.mouseX);
    }
    public int getMouseY(){
        return getMouseY(gui.mouseY);
    }
}