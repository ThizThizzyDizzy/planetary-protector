package planetaryprotector.structure;
import com.thizthizzydizzy.dizzyengine.ResourceManager;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import planetaryprotector.game.Game;
import planetaryprotector.game.GameState;
public class SkyscraperDecal{
    private final int x;
    public final int y;
    final Type type;
    private final int variant;
    public SkyscraperDecal(int x, int y, Type type, int variant){
        if(variant>=type.variants||variant<0)throw new IllegalArgumentException("Invalid skyscraper decal variant: "+type.toString()+" "+variant);
        this.x = x;
        this.y = y;
        this.type = type;
        this.variant = variant;
    }
    public GameState.Structure.Skyscraper.Decal save(){
        var decal = new GameState.Structure.Skyscraper.Decal();
        decal.x = x;
        decal.y = y;
        decal.type = type;
        decal.variant = variant;
        return decal;
    }
    public static SkyscraperDecal load(GameState.Structure.Skyscraper.Decal decal){
        return new SkyscraperDecal(decal.x, decal.y, decal.type, decal.variant);
    }
    public boolean conflictsWith(SkyscraperDecal decal){
        switch(type){
            case DUST:
                return false;
            case TOP:
                return decal.type==Type.TOP;
            case LEFT:
                if(decal.type==Type.LEFT){
                    return Math.abs(y-decal.y)<50;
                }
                if(decal.type==Type.WINDOW){
                    if(decal.y<y)return false;
                    if(decal.y>y+50)return false;
                    return decal.x<35;
                }
                return false;
            case RIGHT:
                if(decal.type==Type.RIGHT){
                    return Math.abs(y-decal.y)<50;
                }
                if(decal.type==Type.WINDOW){
                    if(decal.y<y)return false;
                    if(decal.y>y+50)return false;
                    return decal.x>65;
                }
                return false;
            case WINDOW:
                if(decal.type==Type.WINDOW)return decal.x==x&&decal.y==y;
                return decal.conflictsWith(this);//easier this way
        }
        return false;
    }
    boolean isValid(Skyscraper sky){
        switch(type){
            case DUST:
                return true;
            case LEFT:
                if(y<100)return false;
                if(y>100+(sky.floorCount-15)*Skyscraper.floorHeight)return false;
                return x==0;
            case RIGHT:
                if(y<100)return false;
                if(y>100+(sky.floorCount-15)*Skyscraper.floorHeight)return false;
                return x==50;
            case TOP:
                return x==0&&y==0;
            case WINDOW:
                if(y<100)return false;
                if(y>100+(sky.floorCount-1)*Skyscraper.floorHeight)return false;
                return true;
        }
        return true;
    }
    public void render(Skyscraper sky){
        type.render(this, sky);
    }
    public static enum Type{
        LEFT("left", 1){
            @Override
            void render(SkyscraperDecal decal, Skyscraper sky){
                Renderer.setColor(1, 1, 1, sky.game.hideSkyscrapers?.2f:1);
                Renderer.bound(sky.x, sky.y-(Skyscraper.floorHeight*sky.floorCount), sky.x+sky.width, sky.y+sky.height);
                Renderer.fillRect(sky.x+decal.x, sky.y-(Skyscraper.floorHeight*sky.floorCount)+decal.y, sky.x+decal.x+50, sky.y-(Skyscraper.floorHeight*sky.floorCount)+decal.y+50, ResourceManager.getTexture("/textures/structures/skyscraper/decals/"+Game.theme.tex()+"/"+tex+" "+(decal.variant+1)+".png"));
                Renderer.unBound();
            }
        },
        RIGHT("right", 1){
            @Override
            void render(SkyscraperDecal decal, Skyscraper sky){
                Renderer.setColor(1, 1, 1, sky.game.hideSkyscrapers?.2f:1);
                Renderer.bound(sky.x, sky.y-(Skyscraper.floorHeight*sky.floorCount), sky.x+sky.width, sky.y+sky.height);
                Renderer.fillRect(sky.x+decal.x, sky.y-(Skyscraper.floorHeight*sky.floorCount)+decal.y, sky.x+decal.x+50, sky.y-(Skyscraper.floorHeight*sky.floorCount)+decal.y+50, ResourceManager.getTexture("/textures/structures/skyscraper/decals/"+Game.theme.tex()+"/"+tex+" "+(decal.variant+1)+".png"));
                Renderer.unBound();
            }
        },
        TOP("top", 2){
            @Override
            void render(SkyscraperDecal decal, Skyscraper sky){
                Renderer.setColor(1, 1, 1, sky.game.hideSkyscrapers?.2f:1);
                Renderer.bound(sky.x, sky.y-(Skyscraper.floorHeight*sky.floorCount), sky.x+sky.width, sky.y+sky.height);
                Renderer.fillRect(sky.x+decal.x, sky.y-(Skyscraper.floorHeight*sky.floorCount)+decal.y, sky.x+decal.x+100, sky.y-(Skyscraper.floorHeight*sky.floorCount)+decal.y+100, ResourceManager.getTexture("/textures/structures/skyscraper/decals/"+Game.theme.tex()+"/"+tex+" "+(decal.variant+1)+".png"));
                Renderer.unBound();
            }
        },
        DUST("dust", 10){
            @Override
            void render(SkyscraperDecal decal, Skyscraper sky){
                Renderer.setColor(0, 0, 0, 0.02f*(sky.game.hideSkyscrapers?.2f:1));
                Renderer.bound(sky.x, sky.y-(Skyscraper.floorHeight*sky.floorCount), sky.x+sky.width, sky.y+sky.height);
                Renderer.fillRect(sky.x+decal.x, sky.y-(Skyscraper.floorHeight*sky.floorCount)+decal.y, sky.x+decal.x+50, sky.y-(Skyscraper.floorHeight*sky.floorCount)+decal.y+50, ResourceManager.getTexture("/textures/structures/damage/"+(decal.variant+1)+".png"));
                Renderer.unBound();
            }
        },
        WINDOW("window", 30){
            @Override
            void render(SkyscraperDecal decal, Skyscraper sky){
                Renderer.setColor(1, 1, 1, sky.game.hideSkyscrapers?.1f:1);
                int X = decal.variant;
                int Y = X/10;
                X -= Y*10;
                Renderer.bound(sky.x, sky.y-(Skyscraper.floorHeight*sky.floorCount), sky.x+sky.width, sky.y+sky.height);
                Renderer.fillRect(sky.x+decal.x, sky.y-(Skyscraper.floorHeight*sky.floorCount)+decal.y, sky.x+decal.x+8, sky.y-(Skyscraper.floorHeight*sky.floorCount)+decal.y+10, ResourceManager.getTexture("/textures/structures/skyscraper/decals/"+Game.theme.tex()+"/windows.png"), X/10f, Y/3f, (X+1)/10f, (Y+1)/3f);
                Renderer.unBound();
            }
        };
        public final String tex;
        public final int variants;
        private Type(String tex, int variants){
            this.tex = tex;
            this.variants = variants;
        }
        abstract void render(SkyscraperDecal decal, Skyscraper sky);
    }
}
