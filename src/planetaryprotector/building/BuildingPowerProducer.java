package planetaryprotector.building;
public interface BuildingPowerProducer extends PowerNetworkSection{
    public abstract double getProduction();
    /**
     * Called when power is used. use this to burn coal, etc.
     * @param power how much power was produced.
     */
    public abstract void producePower(double power);
    public boolean isRenewable();
}