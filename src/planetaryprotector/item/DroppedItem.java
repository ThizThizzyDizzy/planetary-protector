package planetaryprotector.item;
import com.thizthizzydizzy.dizzyengine.collision.AxisAlignedBoundingBox;
import com.thizthizzydizzy.dizzyengine.graphics.Material;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import com.thizthizzydizzy.dizzyengine.graphics.batch.Instanceable;
import com.thizthizzydizzy.dizzyengine.graphics.image.Color;
import com.thizthizzydizzy.dizzyengine.graphics.mesh.Mesh;
import com.thizthizzydizzy.dizzyengine.graphics.mesh.builder.AxialQuadMeshBuilder;
import com.thizthizzydizzy.dizzyengine.world.object.SizedWorldObject;
import com.thizthizzydizzy.dizzyengine.world.object.WorldObject;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import planetaryprotector.game.Game;
public class DroppedItem extends SizedWorldObject implements Instanceable{
    public final Item item;
    private int flashDelay = 20;
    private int flashTimer = 20;
    public int life = -1;//1200*5;
    private float opacity = 1;
    private float rot;
    public boolean dead = false;
    public DroppedItem(Game game, float x, float y, Item item){
        setPosition(new Vector3f(x, y, 0));
        setSize(new Vector3f(item==Item.star?20:10, item==Item.star?20:10, 0));
        setMaterial(new Material(null, item.getTexture()));
        this.item = item;
        rot = (float)(game.rand.nextDouble()*360);
    }
    @Override
    public Matrix4f getModelMatrix(){
        return super.getModelMatrix().rotate((float)Math.toRadians(rot), 0, 0, 1);
    }
    private Mesh mesh;
    @Override
    public Mesh getMesh(){
        if(mesh==null)mesh = generateMesh();
        return mesh;
    }
    protected Mesh generateMesh(){
        var builder = new AxialQuadMeshBuilder();
        builder.quadXY(-getSize().x/2, -getSize().y/2, getSize().x/2, getSize().y/2, 0, true, 0, 0, 1, 1);
        return builder.build();
    }
    @Override
    public AxisAlignedBoundingBox getAxisAlignedBoundingBox(){
        AxisAlignedBoundingBox bbox = new AxisAlignedBoundingBox();
        bbox.min.set(getSize()).mul(-.5f).add(getPosition());
        bbox.max.set(getSize()).mul(.5f).add(getPosition());
        return bbox;
    }
    @Override
    public void preRender(){
        Renderer.setColor(Color.WHITE, opacity);
    }
    @Override
    public boolean canInstance(WorldObject other){
        return other instanceof DroppedItem i&&i.item==item&&i.opacity==opacity;
    }
    public void tick(){
        if(life==-1){
            return;
        }
        life--;
        if(life<=0){
            dead = true;
        }
        if(life<600){
            flashTimer--;
            flashDelay = (life/600)*20;
            if(flashTimer<=0){
                setStatic(false);
                flashTimer += flashDelay;
                if(opacity==1){
                    opacity = 0.5f;
                }else{
                    opacity = 1;
                }
            }
        }
    }
    @Deprecated
    public void damage(double x, double y){
        damage();
    }
    public void damage(){
        dead = true;
    }
}
