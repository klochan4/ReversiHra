
//toto je akoby pomocna trieda, pouzita v algoritme hladania volnych tahov a prekreslovania kamenov
public class SusednyKamen extends Kamen{
    private final Smer aSmerKtorymSaKuMneDostaneSused; //tento kamen je susedny ku kamenu v tomto smere
    
    public SusednyKamen(Hrac paKamenHraca, Pozicia paPozicia, Smer paSmer){
        super(paKamenHraca, paPozicia);
        this.aSmerKtorymSaKuMneDostaneSused = paSmer;
    }
    
    public Smer getSmer(){
        return this.aSmerKtorymSaKuMneDostaneSused;
    }
    
    public Farba getFarba(){
        return super.getHrac().getaFarbaKamena();
    }
    
}
