package planetaryprotector.structure.building;
public interface PowerConsumer extends PowerUser{
    public double getMaxPower();
    public double getDemand();
    public double getPower();
    public void addPower(double power);
}