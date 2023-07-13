//predstavuje hraca
public class Hrac {
    private final boolean aJePocitac; //info ci je to pocitac, alebo clovek
    private final Farba aFarbaKamena; //farba hraca
    private int aPocetKamenov; //aktualny pocet kamenov pocas hrania hry
    
    public Hrac(Farba paFarbaKamena, boolean paJePocitac){
        this.aFarbaKamena = paFarbaKamena;
        this.aPocetKamenov = Konstanty.POCIATOCNY_POCET_KAMENOV_NA_HRACA;
        this.aJePocitac = paJePocitac;
    }
    
    public int getaPocetKamenov(){
        return this.aPocetKamenov;
    }
    
    public void setaPocetKamenov(int paPocetKamenov){
        this.aPocetKamenov = paPocetKamenov;
    }
    
    public Farba getaFarbaKamena(){
        return this.aFarbaKamena;
    }
    
    public boolean jePocitac(){
        return this.aJePocitac;
    }
    
    public void inkrementujPocetKamenov(){
        this.aPocetKamenov++;
    }
    
    public void dekrementujPocetKamenov(){
        this.aPocetKamenov--;
    }
    
}
