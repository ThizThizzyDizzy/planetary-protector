package planetaryprotector.menu;
import java.util.ArrayList;
import java.util.Iterator;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import planetaryprotector.Controls;
import planetaryprotector.Core;
import planetaryprotector.Sounds;
import planetaryprotector.structure.building.Building.Upgrade;
import planetaryprotector.enemy.Asteroid;
import planetaryprotector.enemy.AsteroidMaterial;
import planetaryprotector.enemy.Enemy;
import planetaryprotector.enemy.EnemyMothership;
import planetaryprotector.friendly.ShootingStar;
import planetaryprotector.game.Action;
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
import planetaryprotector.structure.building.Building;
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
    public MenuGame(GUI gui, Game game){
        super(gui, null);
        this.game = game;
    }
    @Override
    public void renderBackground(){
        game.renderBackground();
    }
    @Override
    public void render(int millisSinceLastTick){
        game.render(millisSinceLastTick);
        if(game instanceof Epilogue)return;
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
            drawRect(Core.helper.displayWidth()-100, Core.helper.displayHeight()-200, Core.helper.displayWidth(), Core.helper.displayHeight(), ImageStash.instance.getTexture("/textures/gui/sidebar.png"));
            if(game.selectedStructure!=null){
                String upgrades = "";
                if(game.selectedStructure instanceof Building){
                    for(Upgrade u : ((Building)game.selectedStructure).getBoughtUpgrades())upgrades+="*";
                }
                textWithBackground(0, 0, actionButtonWidth, 20, upgrades+" "+game.selectedStructure.getName());
            }
            if(game.furnaceLevel<game.maxFurnaceLevel&&game.furnaceXP>=Math.pow(20, game.furnaceLevel+1)){
                GL11.glColor4d(0, Math.sin(game.tick/5d)/4+.75, 0, 1);
                Game.drawRegularPolygon(Core.helper.displayWidth()-100+5, Core.helper.displayHeight()-100+5, 5, 25, 0);
                GL11.glColor4d(1, 1, 1, 1);
            }
            drawRect(Core.helper.displayWidth()-100, Core.helper.displayHeight()-100, Core.helper.displayWidth(), Core.helper.displayHeight(), ImageStash.instance.getTexture("/textures/furnace "+game.furnaceLevel+".png"));
            if(gui.mouseX>=Core.helper.displayWidth()-100&&gui.mouseY>=Core.helper.displayHeight()-100&&game.furnaceLevel<Game.maxFurnaceLevel){
                GL11.glColor4d(0, 1, 0, .25);
                double percent = game.furnaceXP/Math.pow(20, game.furnaceLevel+1);
                drawRect(Core.helper.displayWidth()-100,Core.helper.displayHeight()-100,Core.helper.displayWidth()-100+(100*percent), Core.helper.displayHeight(), 0);
                GL11.glColor4d(1, 1, 1, 1);
            }
            GL11.glColor4d(0, 0, 0, 1);
            drawText(Core.helper.displayWidth()-90, Core.helper.displayHeight()-60, Core.helper.displayWidth()-10, Core.helper.displayHeight()-40, game.furnaceOre+" Ore");
            drawText(Core.helper.displayWidth()-90, Core.helper.displayHeight()-40, Core.helper.displayWidth()-10, Core.helper.displayHeight()-20, game.furnaceCoal+" Coal");
            drawText(Core.helper.displayWidth()-90, Core.helper.displayHeight()-20, Core.helper.displayWidth()-10, Core.helper.displayHeight(), game.furnaceLevel>=game.maxFurnaceLevel?"Maxed":"Level "+(game.furnaceLevel+1));
            GL11.glColor4d(1, 1, 1, 1);
            if(game.isPlayable()){
                for(int i = 0; i<game.resources.size(); i++){
                    int I = 1;
                    if(i==0)I = 0;
                    if(i==game.resources.size()-1)I = 2;
                    drawRect(Core.helper.displayWidth()-100, i*20, Core.helper.displayWidth(), (i+1)*20+(I==2?5:0), ImageStash.instance.getTexture("/textures/gui/sidebar "+I+".png"));
                GL11.glColor4d(0, 0, 0, 1);
                    drawText(Core.helper.displayWidth()-80, i*20, Core.helper.displayWidth(), (i+1)*20, game.resources.get(i).count+"");
                GL11.glColor4d(1, 1, 1, 1);
                    drawRect(Core.helper.displayWidth()-100, i*20, Core.helper.displayWidth()-80, (i+1)*20, game.resources.get(i).item.getTexture());
                }
            }
        }
        if(game.won){
            if(game.phase>0){
                centeredTextWithBackground(0, 0, Core.helper.displayWidth(), 35, "Congratulations! You have destroyed the alien mothership and saved the planet!");
                if(game.winTimer<20&&"VictoryMusic1".equals(Sounds.nowPlaying())){
                    centeredTextWithBackground(0, 35, Core.helper.displayWidth(), 85, "Only one problem remains...");
                }
            }
        }else{
            int offset = 0;
            for(Iterator<Notification> it = game.notifications.iterator(); it.hasNext();){
                Notification n = it.next();
                double wide = FontManager.getLengthForStringWithHeight(n.toString(), 20);
                double left = Core.helper.displayWidth()/2-(wide/2*n.width);
                double right = Core.helper.displayWidth()/2+(wide/2*n.width);
                int y = 20-n.height;
                GL11.glColor4d(0,0,0,.5);
                drawRectWithBounds(Core.helper.displayWidth()/2-wide/2, offset-y/2, Core.helper.displayWidth()/2+wide/2, offset+20-y/2, left, offset, right, offset+n.height, 0);
                GL11.glColor4d(1,1,1,1);
                drawCenteredTextWithBounds(Core.helper.displayWidth()/2-wide/2, offset-y/2, Core.helper.displayWidth()/2+wide/2, offset+20-y/2, left, offset, right, offset+n.height, n.toString());
                offset+=n.height;
                if(n.isDead())it.remove();
            }
        }
        if(game.selectedStructure!=null&&game.selectedStructure instanceof Building){
            Building building = (Building) game.selectedStructure;
            if(building.task!=null){
                for(int i = 0; i<building.task.getDetails().length; i++){
                    textWithBackground(actionButtonWidth, 30*i, Core.helper.displayWidth(), 30*(i+1), building.task.getDetails()[i], building.task.important);
                }
            }
        }
        if(game.paused){
            drawCenteredText(0, Core.helper.displayHeight()/2-50, Core.helper.displayWidth(), Core.helper.displayHeight()/2+50, "Paused");
        }
        if(Core.debugMode&&game.cheats){
            debugYOffset = 0;
            ArrayList<String> debugData = game.getDebugData();
            double textHeight = Core.helper.displayHeight()/debugData.size();
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
        drawRect(0, 0, Core.helper.displayWidth(), Core.helper.displayHeight(), 0);
        GL11.glColor4d(1, 1, 1, 1);
    }
    @Override
    public void keyEvent(int key, int scancode, boolean isPress, boolean isRepeat, int modifiers){
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
                    Sounds.vol = 1-Sounds.vol;
                    if(Sounds.vol<.01){
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
                if(key>=GLFW.GLFW_KEY_1&&key<=GLFW.GLFW_KEY_EQUAL){
                    game.notify("Cheat: Secret #"+(key-1));
                    game.secretWaiting = key-2;
                }
            }
            if(!actionButtons.isEmpty()&&game.selectedStructure!=null&&game.selectedStructure instanceof Building){
                if(key>=GLFW.GLFW_KEY_1&&key<=GLFW.GLFW_KEY_EQUAL){
                    if(actionButtons.size()>key-2){
                        actionButtons.get(key-2).perform();
                        if(((Building)game.selectedStructure).task!=null){
                            game.notify("Cheat: Instant Completion");
                            ((Building)game.selectedStructure).task.progress = ((Building)game.selectedStructure).task.time-1;
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
                    game.addCloud(gui.mouseX, gui.mouseY);
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
                        double X = gui.mouseX-25;
                        double Y = gui.mouseY-25;
                        game.addShootingStar(new ShootingStar(game, X, Y));
                    }else if(gui.keyboardWereDown.contains(GLFW.GLFW_KEY_LEFT_CONTROL)){
                        Enemy.strength++;
                        game.notify("Cheat: Enemy Strength: ", Enemy.strength+"");
                    }else{
                        if(gui.keyboardWereDown.contains(GLFW.GLFW_KEY_LEFT_SHIFT)){
                            game.notify("Cheat: Add Enemy");
                            game.addRandomEnemy();
                        }else{
                            game.notify("Cheat: Spawn Asteroid");
                            double X = gui.mouseX-25;
                            double Y = gui.mouseY-25;
                            switch(game.rand.nextInt(3)){
                                case 0:
                                    game.addAsteroid(new Asteroid(game, X, Y, AsteroidMaterial.COAL, 1));
                                    break;
                                case 1:
                                    game.addAsteroid(new Asteroid(game, X, Y, AsteroidMaterial.STONE, 1));
                                    break;
                                case 2:
                                    game.addAsteroid(new Asteroid(game, X, Y, AsteroidMaterial.IRON, 1));
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
                    break;
            }
        }else if(isPress){
            if(!actionButtons.isEmpty()&&game.selectedStructure!=null){
                if(key>=GLFW.GLFW_KEY_1&&key<=GLFW.GLFW_KEY_EQUAL){
                    if(actionButtons.size()>key-2){
                        if(actionButtons.get(key-2).enabled&&!actionButtons.get(key-2).action.isImportant())actionButtons.get(key-2).perform();
                    }
                }
            }
        }
    }
    @Override
    public void onMouseButton(double x, double y, int button, boolean pressed, int mods){
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
        game.onMouseButton(x, y, button, pressed, mods);
        if(button==0&&pressed&&game.isPlayable()){
            int amount = 1;
            if(gui.keyboardWereDown.contains(GLFW.GLFW_KEY_LEFT_SHIFT))amount*=100;
            if(gui.keyboardWereDown.contains(GLFW.GLFW_KEY_LEFT_CONTROL))amount*=10;
            if(isClickWithinBounds(x, y, Core.helper.displayWidth()-100, 20, Core.helper.displayWidth()-80, 40)){
                game.addCoalToFurnace(amount);
            }
            if(isClickWithinBounds(x, y, Core.helper.displayWidth()-100, 40, Core.helper.displayWidth()-80, 60)){
                game.addIronToFurnace(amount);
            }
            if(game.furnaceLevel<game.maxFurnaceLevel&&game.furnaceXP>=Math.pow(20, game.furnaceLevel+1)&&gui.mouseX>=Core.helper.displayWidth()-100&&gui.mouseY>=Core.helper.displayHeight()-100){
                game.furnaceLevel++;
            }
        }
    }
    @Override
    public void onMouseMove(double x, double y){
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
        game.onMouseMove(x, y);
    }
    @Override
    public boolean onMouseScrolled(double x, double y, double dx, double dy){
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
        game.onMouseScrolled(x, y, dx, dy);
        return true;
    }
    @Override
    public void tick(){
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
            add(new MenuComponentFalling(this, Core.helper.displayWidth()-90+game.rand.nextInt(60), Core.helper.displayHeight()-180+game.rand.nextInt(50), Item.ironOre));
            game.addingIron--;
        }
        if(game.addingCoal>0){
            add(new MenuComponentFalling(this, Core.helper.displayWidth()-90+game.rand.nextInt(60), Core.helper.displayHeight()-180+game.rand.nextInt(50), Item.coal));
            game.addingCoal--;
        }
        if(game.smeltingIron>0){
            add(new MenuComponentRising(this, Core.helper.displayWidth()-90+game.rand.nextInt(60), Core.helper.displayHeight()-20+game.rand.nextInt(10), Item.ironIngot));
        }
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
        text = drawTextWithWrap(1, debugYOffset+1, Core.helper.displayWidth()-1, debugYOffset+textHeight-1, text);
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
}