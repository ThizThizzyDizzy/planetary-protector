package planetaryprotector;
import planetaryprotector.menu.MenuGame;
import java.util.ArrayList;
import java.util.HashMap;
import simplelibrary.config2.Config;
public class Expedition{
    public final int requiredWorkers;
    public int workers;
    public int time = 0;
    public int totalTime = 0;
    public int civilians = 0;
    public int civilianCooldown = MenuGame.rand.nextInt(20*60*4);
    public int dieCooldown = MenuGame.rand.nextInt(20*60*20);
    public boolean returning = false;
    public boolean done = false;
    public boolean returned = false;
    public HashMap<Integer, Integer> civilianGraph = new HashMap<>();
    public HashMap<Integer, Integer> workerGraph = new HashMap<>();
    public HashMap<Integer, Integer> civilianPromotionGraph = new HashMap<>();
    public HashMap<Integer, Boolean> returningGraph = new HashMap<>();
    public boolean recalled = false;
    public Expedition(int workers){
        requiredWorkers = workers;
        civilianGraph.put(0, 0);
        workerGraph.put(0, workers);
        lastWorkers = workers;
    }
    public ArrayList<String> getText(){
        ArrayList<String> text = new ArrayList<>();
        if(!done){
            text.add("Current Expedition: ");
            if(workers!=requiredWorkers){
                text.add("Workers: "+workers+" ("+(workers-requiredWorkers<0?"-":"+")+(workers-requiredWorkers)+")");
            }
            text.add("Civilians: "+civilians);
            int ticks = totalTime;
            int seconds = 0;
            int minutes = 0;
            int hours = 0;
            while(ticks>=20){
                seconds++;
                ticks-=20;
            }
            while(seconds>=60){
                minutes++;
                seconds-=60;
            }
            while(minutes>=60){
                hours++;
                minutes-=60;
            }
            text.add("Total time:");
            if(hours>0){
                text.add("- "+hours+" hour"+(hours==1?"":"s"));
            }
            if(minutes>0){
                text.add("- "+minutes+" minute"+(minutes==1?"":"s"));
            }
            return text;
        }
        text.add("Expedition report: ");
        if(returned==false){
            text.add("Expedition Lost.");
            text.add("");
            return text;
        }
        if(workers!=requiredWorkers){
            text.add("Workers: "+workers+" ("+(workers-requiredWorkers<0?"-":"+")+(workers-requiredWorkers)+")");
        }
        text.add("Civilians: "+civilians);
        int ticks = totalTime;
        int seconds = 0;
        int minutes = 0;
        int hours = 0;
        while(ticks>=20){
            seconds++;
            ticks-=20;
        }
        while(seconds>=60){
            minutes++;
            seconds-=60;
        }
        while(minutes>=60){
            hours++;
            minutes-=60;
        }
        text.add("Total time:");
        if(hours>0){
            text.add("- "+hours+" hour"+(hours==1?"":"s"));
        }
        if(minutes>0){
            text.add("- "+minutes+" minute"+(minutes==1?"":"s"));
        }
        return text;
    }
    int lastCivilians = 0;
    int lastWorkers = 0;
    public void tick(){
        for(int i = 0; i<(Core.game.cheats?80*60:4*60); i++){
            if(done){
                return;
            }
            if(civilians!=lastCivilians){
                civilianGraph.put(totalTime, civilians);
                lastCivilians = civilians;
            }
            if(workers!=lastWorkers){
                workerGraph.put(totalTime, workers);
                lastWorkers = workers;
            }
            totalTime++;
            if(returning){
                time--;
                if(time<=0){
                    returned = true;
                    done = true;
                    civilianGraph.put(totalTime, civilians);
                    workerGraph.put(totalTime, workers);
                }
            }else{
                time++;
            }
            civilianCooldown--;
            if(civilianCooldown<=0){
                civilianCooldown = MenuGame.rand.nextInt(20*60*4);
                double newCivilians = Math.min(50, Math.max(-100, MenuGame.rand.nextGaussian()));
                if(newCivilians<0){
                    newCivilians*=-2;
                }
                while(newCivilians>0){
                    newCivilians--;
                    if(MenuGame.rand.nextInt(50)>0){
                        civilians++;
                    }else{
                        workers++;
                    }
                }
            }
            dieCooldown--;
            if(dieCooldown<=0){
                dieCooldown = MenuGame.rand.nextInt(20*60*15);
                double dead = MenuGame.rand.nextGaussian()*(civilians+workers)/25;
                while(dead>0){
                    dead--;
                    if(MenuGame.rand.nextInt(civilians+workers)>workers){
                        civilians--;
                    }else{
                        workers--;
                    }
                }
            }
            if(workers*20<civilians&&civilians>20&&workers<requiredWorkers){
                workers++;
                civilians--;
                civilianPromotionGraph.put(totalTime, 1);
            }else if(workers*50<civilians&&civilians>50){
                workers++;
                civilians--;
                civilianPromotionGraph.put(totalTime, 1);
            }
            if(workers<=0){
                done = true;
                civilianGraph.put(totalTime, civilians);
                workerGraph.put(totalTime, workers);
            }
            civilians = Math.max(0, civilians);
            if(!recalled){
                if(civilians>=100*requiredWorkers&&!returning){
                    returning = true;
                    returningGraph.put(totalTime, true);
                }else if(civilians<=80*requiredWorkers&&returning&&time<=20*60*2.5){
                    returning = false;
                    returningGraph.put(totalTime, false);
                }
            }
        }
    }
    public Config save(Config cfg){
        cfg.set("Required Workers", requiredWorkers);
        cfg.set("Workers", workers);
        cfg.set("Time", time);
        cfg.set("Total time", totalTime);
        cfg.set("civilians", civilians);
        cfg.set("civilan cooldown", civilianCooldown);
        cfg.set("die cooldown", dieCooldown);
        cfg.set("returning", returning);
        cfg.set("recalled", recalled);
        cfg.set("done", done);
        cfg.set("returned", returned);
        return cfg;
    }
    public static Expedition load(Config cfg){
        if(cfg==null) return null;
        Expedition e = new Expedition(cfg.get("Required Workers", 1));
        e.workers = cfg.get("Workers", 1);
        e.time = cfg.get("Time", 0);
        e.totalTime = cfg.get("Total time", 0);
        e.civilians = cfg.get("civilians", 0);
        e.civilianCooldown = cfg.get("civilian cooldown", e.civilianCooldown);
        e.dieCooldown = cfg.get("die cooldown", e.dieCooldown);
        e.returning = cfg.get("returning", false);
        e.recalled = cfg.get("recalled", false);
        e.done = cfg.get("done", false);
        e.returned = cfg.get("returned", false);
        return e;
    }
    @Override
    public String toString(){
        String str = "";
        for(String string : getText()){
            str+=" | "+string;
        }
        if(str.isEmpty())return str;
        return str.substring(3);
    }
}