import java.util.ArrayList;

public class Hra {
    private final Hrac aHraci[]; //pole hracov
    private final HernyKamen[][] aHraciePole; //pole kamenov
    private final int aRozmeryPola; //rozmery hracieho pola
    private boolean aNieJeVolnyTah; //info ci nie je volny tah
    int aIndexHracaNaTahu = 0; //ktory hrac je na tahu
    
    public int getPocetPodlaFarby(Farba paFarba){ //hrac s danou farbou ma tolko kamenov
        if(aHraci[0].getaFarbaKamena() == paFarba) return aHraci[0].getaPocetKamenov();
        else return aHraci[1].getaPocetKamenov();
    }
    
    public boolean getaNieJeVolnyTah(){
        return this.aNieJeVolnyTah;
    }
    
    public void setaNieJeVolnyTah(boolean flag){
        this.aNieJeVolnyTah = flag;
    }
    
    //konstruktor
    public Hra(int paRozmeryPola, Farba paFarbaCloveka, Farba paFarbaPocitaca){
        this.aRozmeryPola = paRozmeryPola;
        aHraciePole = new HernyKamen[paRozmeryPola][paRozmeryPola];
        this.aNieJeVolnyTah = false;
        
        aHraci = new Hrac[]{new Hrac(paFarbaCloveka, false), new Hrac(paFarbaPocitaca, true)};


        //vygeneruju sa prve 4 kamene
        if(aHraci[0].getaFarbaKamena() == Farba.CIERNY){
            
            aHraciePole[(paRozmeryPola / 2) - 1][(paRozmeryPola / 2) - 1] = new CiernyKamen(aHraci[0], (paRozmeryPola / 2) - 1, (paRozmeryPola / 2) - 1);
            aHraciePole[paRozmeryPola / 2][paRozmeryPola / 2] = new CiernyKamen(aHraci[0], paRozmeryPola / 2, paRozmeryPola / 2);
            aHraciePole[(paRozmeryPola / 2) - 1][(paRozmeryPola / 2)] = new BielyKamen(aHraci[1], (paRozmeryPola / 2) - 1, (paRozmeryPola / 2));
            aHraciePole[paRozmeryPola / 2][(paRozmeryPola / 2) - 1] = new BielyKamen(aHraci[1], paRozmeryPola / 2, (paRozmeryPola / 2) - 1);
        
        }else{
            
            aHraciePole[(paRozmeryPola / 2) - 1][(paRozmeryPola / 2) - 1] = new CiernyKamen(aHraci[1], (paRozmeryPola / 2) - 1, (paRozmeryPola / 2) - 1);
            aHraciePole[paRozmeryPola / 2][paRozmeryPola / 2] = new CiernyKamen(aHraci[1], paRozmeryPola / 2, paRozmeryPola / 2);
            aHraciePole[(paRozmeryPola / 2) - 1][(paRozmeryPola / 2)] = new BielyKamen(aHraci[0], (paRozmeryPola / 2) - 1, (paRozmeryPola / 2));
            aHraciePole[paRozmeryPola / 2][(paRozmeryPola / 2) - 1] = new BielyKamen(aHraci[0], paRozmeryPola / 2, (paRozmeryPola / 2) - 1);
        }
    }

    //na danej pozicii nastavi policko na danu farbu
    public void nastavPolickoNaFarbu(Hrac paHrac, Pozicia paPozicia){
        if(paHrac.getaFarbaKamena() == Farba.BIELY){
            aHraciePole[paPozicia.getX()][paPozicia.getY()] = new BielyKamen(paHrac, paPozicia.getX(), paPozicia.getY());
        }
        
        if(paHrac.getaFarbaKamena() == Farba.CIERNY){
            aHraciePole[paPozicia.getX()][paPozicia.getY()] = new CiernyKamen(paHrac, paPozicia.getX(), paPozicia.getY());
        }
        
    }
        
    public HernyKamen[][] getaHraciePole(){
        return this.aHraciePole;
    }


    //vrati hraca, ktoremu patri kamen na urcenej pozicii
    private Hrac getKamenHraca(Pozicia paPozicia){
        int riadok = paPozicia.getRiadok();
        int stlpec = paPozicia.getStlpec();
        
        if(riadok < 0 || stlpec < 0) return null;
        if(riadok >= this.aRozmeryPola || stlpec >= this.aRozmeryPola) return null;
        
        if(aHraciePole[riadok][stlpec] == null) return null;
        
        if(aHraci[0].getaFarbaKamena() == aHraciePole[riadok][stlpec].getFarba()) return aHraci[0];
        if(aHraci[1].getaFarbaKamena() == aHraciePole[riadok][stlpec].getFarba()) return aHraci[1];
        
        return null;
    }

    //vrati len tie SusedneKamene, ktore patria protihracovi
    private ArrayList<SusednyKamen> ziskajIbaProtihracoveKameneZoSusednych(Hrac paProtihrac, ArrayList<SusednyKamen> paSusedneKamene){
        ArrayList<SusednyKamen> protihracoveKamene = new ArrayList<>();
        for(SusednyKamen kamen : paSusedneKamene){
            if(kamen.getHrac() == paProtihrac) protihracoveKamene.add(kamen);
        }
        
        return protihracoveKamene;
    }

    //vrati zoznam susednych kamenov, ku kamenu na danom riadku a stlpci v hracom poli
    private ArrayList<SusednyKamen> getSusedneKamene(int riadok, int stlpec){
        ArrayList<SusednyKamen> susedneKamene = new ArrayList<>();
        
        int indexSmeru = 0;
        for(int i = -1; i <= 1; i++){
           for(int j = -1; j <= 1; j++){
               
               if(i == 0 && j == 0) continue;
               
               Hrac hrac = getKamenHraca(new Pozicia(stlpec + j, riadok + i));
               
               if(hrac != null){
                    susedneKamene.add(new SusednyKamen(hrac, new Pozicia(stlpec + j, riadok + i), Smer.values()[indexSmeru]));
               }
               indexSmeru++;
           }
       }
        
        return susedneKamene;
    }


    public Hrac protihracLKHracovi(Hrac hrac){
        return aHraci[0] == hrac ? aHraci[1] : aHraci[0];
    }
    
    public Hrac[] getaHraci(){
        return this.aHraci;
    }

    //vrati Poziciu v smere k urcenej pozicii
    private Pozicia vratPoziciuVSmere(Smer smer, int riadok, int stlpec){
        switch(smer){
            case SEVER:
                riadok--;
                break;
            case JUH:
                riadok++;
                break;
                
            case VYCHOD:
                stlpec++;
                break;
                
            case ZAPAD:
                stlpec--;
                break;
            case SEVEROVYCHOD:
                riadok--;
                stlpec++;
                break;
            case SEVEROZAPAD:
                riadok--;
                stlpec--;
                break;
            case JUHOVYCHOD:
                riadok++;
                stlpec++;
                break;
                
            case JUHOZAPAD:
                riadok++;
                stlpec--;
                break;
        }
        
        if(riadok >= 0 && stlpec >= 0) return new Pozicia(riadok, stlpec);
        return null;
    }

    //vrati kamen, ktory sa nachadza v urcenom smere od urcenej pozicie
    private HernyKamen kamenVSmere(Smer smer, int riadok, int stlpec){
        Pozicia poziciaVSmere = vratPoziciuVSmere(smer, riadok, stlpec);
        if(poziciaVSmere == null) return null;
        riadok = poziciaVSmere.getX();
        stlpec = poziciaVSmere.getY();
        
        if(riadok < 0 || stlpec < 0 || riadok >= this.aRozmeryPola || stlpec >= this.aRozmeryPola){
            return null;
        }
        return aHraciePole[riadok][stlpec];
    }

    //urci vitaza
    public Hrac vitaz(){
        if(aHraci[0].getaPocetKamenov() > aHraci[1].getaPocetKamenov()) return aHraci[0];
        if(aHraci[0].getaPocetKamenov() < aHraci[1].getaPocetKamenov()) return aHraci[1];
        return null;
    }

    //zisti, ci v danom smere od pozicie v parametri sa nachadza kamen rovnakej farby, ako je farba v parametri
    private boolean jeNejakyKamenRovnakejFarbyVTomtoSmere(Farba farba, Smer smer, int riadok, int stlpec){
        HernyKamen hk;
        Pozicia p;
        do{
            hk = kamenVSmere(smer, riadok, stlpec);
            
            if(hk == null){
                break;
            }
            
            p = vratPoziciuVSmere(smer, riadok, stlpec);

            if(hk.getFarba() == farba){
                return true;
            } 
            
            if(p != null){
                riadok = p.getX();
                stlpec = p.getY();
            }else{
                break;
            }
            
            
            
            
        }while(hk.getFarba() != farba);
        return false;
    }

    //najde volne tahy pre daneho hraca
    public ArrayList<Pozicia> volneTahy(Hrac hracNaTahu){
        
        ArrayList<Pozicia> pozicieNaMoznyTah = new ArrayList<>();
        for(int riadok = 0; riadok < this.aRozmeryPola; riadok++){
            for(int stlpec = 0; stlpec < this.aRozmeryPola; stlpec++){
                if(aHraciePole[riadok][stlpec] == null){
                    
                    
                    ArrayList<SusednyKamen> susedneKamene = getSusedneKamene(riadok, stlpec);
                    ArrayList<SusednyKamen> ibaProtihracove = ziskajIbaProtihracoveKameneZoSusednych(protihracLKHracovi(hracNaTahu), susedneKamene);
                    
                    if(ibaProtihracove.size() > 0){
                        for(SusednyKamen kamen : ibaProtihracove){
                            
                            HernyKamen hk;
                            Smer smer = kamen.getSmer();
                            int r = kamen.getRiadok();
                            int s = kamen.getStlpec();
                            Pozicia p;
                            do{
                                hk = kamenVSmere(smer, r, s);
                                if(hk == null){
                                    break;
                                }
                                
                                p = vratPoziciuVSmere(smer, r, s);
                                
                                
                                
                                if(hk.getFarba() == hracNaTahu.getaFarbaKamena()){
                                    pozicieNaMoznyTah.add(new Pozicia(riadok, stlpec));
                                    break;
                                }
                                
                                if(p != null){
                                    r = p.getX();
                                    s = p.getY();
                                }else{
                                    break;
                                }
                            }while(hk.getFarba() == kamen.getFarba());
                        }
                    }
                }
            }
        }
        
        return pozicieNaMoznyTah;
    }

    //prefarbi kamene v danom smere na urcenu farbu
    public void nastavKameneVSmereNaFarbu(HernyKamen[][] hp, Hrac hrac, Hrac protihrac, Smer smer, int riadok, int stlpec){
        HernyKamen hk;
        Pozicia p;
        do{
            hk = kamenVSmere(smer, riadok, stlpec);
            
            p = vratPoziciuVSmere(smer, riadok, stlpec);
            
            if(p != null){
                riadok = p.getX();
                stlpec = p.getY();
            }else{
                break;
            }

            
            if(hk.getFarba() == protihrac.getaFarbaKamena()){
                if(hrac.getaFarbaKamena() == Farba.BIELY){
                    hp[riadok][stlpec] = new BielyKamen(hrac, riadok, stlpec);
                }
                
                if(hrac.getaFarbaKamena() == Farba.CIERNY){
                    hp[riadok][stlpec] = new CiernyKamen(hrac, riadok, stlpec);
                }
                
                hrac.inkrementujPocetKamenov();
                protihrac.dekrementujPocetKamenov();
                continue;
            }
            
            if(hk.getFarba() == hrac.getaFarbaKamena()){
                return;
            } 
            
            
            
            if(hk == null){
                break;
            }
            
            
        }while(hk.getFarba() != hrac.getaFarbaKamena());
    }
    //zisti a prefarbi kamene v danom smere na urcenu farbu
    public void prefarbiKamene(HernyKamen[][] hp, Hrac hracNaTahu, Hrac protihrac, int riadok, int stlpec){

                    for(Smer smer : Smer.values()){
                        if(jeNejakyKamenRovnakejFarbyVTomtoSmere(hp[riadok][stlpec].getFarba(), smer, riadok, stlpec)){
                            nastavKameneVSmereNaFarbu(hp, hracNaTahu, protihrac, smer, riadok, stlpec);
                        }
                    }
    }

    //pocitac tu spravi tah, a vrati sa pozicia miesta, kde vlozil svoj kamen
    public Pozicia pocitacSpraviTah(Hrac referenciaNaHracaPocitac){
        

        //vyhladaju sa volne tahy pre pocitac
        ArrayList<Pozicia> mozneTahy = volneTahy(referenciaNaHracaPocitac);
        
        
        HernyKamen[][] tmp = new HernyKamen[this.aRozmeryPola][this.aRozmeryPola];
        Pozicia najlepsiTah = null;
        int zvysenieOPocetKamenov = 0;
        
        Hrac tmpH1 = new Hrac(referenciaNaHracaPocitac.getaFarbaKamena(), true);
        tmpH1.setaPocetKamenov(0);
            Hrac tmpH2;
            
            if(referenciaNaHracaPocitac.getaFarbaKamena() == Farba.BIELY){
                tmpH2 = new Hrac(Farba.CIERNY, false);
            }else{
                tmpH2 = new Hrac(Farba.BIELY, false);
            }

            //tu sa zistuje, ktory tah je najlepsi
        for(Pozicia moznyTah : mozneTahy){
            for(int i = 0; i < this.aRozmeryPola; i++){
                for(int j = 0; j < this.aRozmeryPola; j++){
                    tmp[i][j] = aHraciePole[i][j];
                }
            }
            
            if(referenciaNaHracaPocitac.getaFarbaKamena() == Farba.BIELY){
                    tmp[moznyTah.getX()][moznyTah.getY()] = new BielyKamen(referenciaNaHracaPocitac, moznyTah.getX(), moznyTah.getY());
                }
                
                if(referenciaNaHracaPocitac.getaFarbaKamena() == Farba.CIERNY){
                    tmp[moznyTah.getX()][moznyTah.getY()] = new CiernyKamen(referenciaNaHracaPocitac, moznyTah.getX(), moznyTah.getY());
                }
            
            prefarbiKamene(tmp, tmpH1, tmpH2, moznyTah.getX(), moznyTah.getY());
            
            if(tmpH1.getaPocetKamenov() > zvysenieOPocetKamenov){
                zvysenieOPocetKamenov = tmpH1.getaPocetKamenov();
                
                najlepsiTah = moznyTah;
            }
            
            tmpH1.setaPocetKamenov(0);
            
        }

        //nasledne po zisteni pocitac zvoli najlepsi tah
        if(najlepsiTah != null){
            //vytvori sa kamen podla farby pocitaca na danej pozicii
            if(referenciaNaHracaPocitac.getaFarbaKamena() == Farba.BIELY){
                    aHraciePole[najlepsiTah.getX()][najlepsiTah.getY()] = new BielyKamen(referenciaNaHracaPocitac, najlepsiTah.getX(), najlepsiTah.getY());
                }
                
                if(referenciaNaHracaPocitac.getaFarbaKamena() == Farba.CIERNY){
                    aHraciePole[najlepsiTah.getX()][najlepsiTah.getY()] = new CiernyKamen(referenciaNaHracaPocitac, najlepsiTah.getX(), najlepsiTah.getY());
                }
            
            referenciaNaHracaPocitac.inkrementujPocetKamenov();
        }
        return najlepsiTah;
    }
    
    public Hrac hracNaTahu(){
        return aHraci[aIndexHracaNaTahu];
    }
    
    public void setDalsiHracNaTahu(){
        aIndexHracaNaTahu++;
        
        if(aIndexHracaNaTahu == 2) aIndexHracaNaTahu = 0;
    }
    
    
    
    public void spustiHru(){
        if(aHraci[0].getaFarbaKamena() == Konstanty.HRU_ZACINA_KAMEN_FARBY) aIndexHracaNaTahu = 0;
        else aIndexHracaNaTahu = 1;
    }
}
