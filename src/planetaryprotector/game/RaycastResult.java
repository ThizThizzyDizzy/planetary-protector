package planetaryprotector.game;
import org.joml.Vector3f;
import planetaryprotector.structure.ShieldGenerator;
import planetaryprotector.structure.Structure;
public class RaycastResult{
    public Vector3f hitPosition;
    public Structure hitStructure;
    public ShieldGenerator hitShield;
    public boolean hitFront;
}
