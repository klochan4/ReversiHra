//trieda, z ktorej dedi HernyKamen a SusednyKamen
public class Kamen {
    private final Hrac aHrac;
    private final Pozicia aPozicia; //pozicia na hracomPoli
    
    public Kamen(Hrac hrac, Pozicia aPozicia){
        this.aHrac = hrac;
        this.aPozicia = aPozicia;
    }
    
    public Hrac getHrac(){
        return this.aHrac;
    }
    
    public int getRiadok(){
        return this.aPozicia.getRiadok();
    }
    
    public int getStlpec(){
        return this.aPozicia.getStlpec();
    }
    
}
