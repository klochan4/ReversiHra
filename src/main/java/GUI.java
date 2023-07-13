import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

//trieda predstavujuca graficke rozhranie, dedi z triedy JFrame, kedze je to okno
//a implementuje actionListener na kliknutie mysi a key listener na klavesy R a ESC
public class GUI extends JFrame implements ActionListener, KeyListener{
    JPanel aPanelHraciePole; //hracie pole, panel v GUIcku
    private JLabel[][] aKamene; //pole labelov s ikonkami kamenov
    private final JLabel aLbl_aktualneRozmery; //obsahuje text s aktualnymi rozmermi
    private final JLabel aLbl_poctyKamenov; //text s poctami kamenov
    private final JLabel aLbl_info; //info o stave hry
    private final JComboBox<String> aComboBox_rozmeryHry; //combobox na nastavenie rozmerov hry
    private final JComboBox<String> aComboBox_farbaHraca; //combobox na nastavenie farby hraca
    private final JButton aButton_novaHra;
    
    private Hra aHra; //referencia na hru
    private int aRozmeryHraciehoPola; //rozmery hracieho pola
    private int aPanel_predosleRozmery; //rozmery panelu pred zmenou nastavenia
    private int aPanel_noveRozmery; //rozmery hracieho pola po zmene nastavenia
    
    private ImageIcon aVolnyTahIkona; //obrazok volneho tahu
    private ImageIcon aZvyrazneneIkona; //obrazok zvyrazneneho policka
    
    //konstruktor triedy GUI
    public GUI(){
        super("Reversi"); //nastavenie hlavicky okna
        aRozmeryHraciehoPola = Konstanty.POCET_POLICOK_NA_ZACIATKU; //nastavenie pociatocnych rozmerov
        aKamene = new JLabel[aRozmeryHraciehoPola][aRozmeryHraciehoPola]; //vygenerovanie pola, kde sa budu ukladat ikonky kamenov
        setResizable(false); //oknu nie je mozne menit velkost
        setFocusable(true); //okno je zameratelne
        setFocusTraversalKeysEnabled(false);

        //pociatocne rozmery okna
        setBounds(Konstanty.POCIATOCNA_X_POZICIA_OKNA, 
                Konstanty.POCIATOCNA_Y_POZICIA_OKNA, 
                Konstanty.OKNO_OFFSET_X + Konstanty.POCET_POLICOK_NA_ZACIATKU * Konstanty.ROZMER_POLICKA, 
                Konstanty.OKNO_OFFSET_Y + Konstanty.POCET_POLICOK_NA_ZACIATKU * Konstanty.ROZMER_POLICKA);

        //vygenerovanie noveho panela - hracieho pola
        aPanelHraciePole = new JPanel(null){
               
                @Override
                //pri zavolani tejto metody hracieho pola, sa nepouzije defaultna, ale tato overridnuta
               public boolean isOptimizedDrawingEnabled(){
                    return false;
                }
                
            @Override
            //pri zavolani tejto metody sa nepouzije defaultna, ale overridnuta
            public void paintComponent(Graphics g) {
            
            boolean svetlePolicko=true;
            //vygeneruju sa jednotlive policka v hracom poli
                for(int x= 0;x<aRozmeryHraciehoPola;x++){
                    for(int y= 0;y<aRozmeryHraciehoPola;y++){
                        if(!svetlePolicko){
                            g.setColor(new Color(Konstanty.TMAVE_POLICKO_RGB_FARBA[0], Konstanty.TMAVE_POLICKO_RGB_FARBA[1], Konstanty.TMAVE_POLICKO_RGB_FARBA[2]));
                        }else{
                            g.setColor(new Color(Konstanty.SVETLE_POLICKO_RGB_FARBA[0], Konstanty.SVETLE_POLICKO_RGB_FARBA[1], Konstanty.SVETLE_POLICKO_RGB_FARBA[2]));
                            
                        }
                        g.fillRect(x * Konstanty.ROZMER_POLICKA, y * Konstanty.ROZMER_POLICKA, 
                                        Konstanty.ROZMER_POLICKA, Konstanty.ROZMER_POLICKA);
                        svetlePolicko=!svetlePolicko;
                    }
                        svetlePolicko=!svetlePolicko;
                }
            }
        };

        //ponastavovanie pozicie a rozmerov jednotlivych komponentov v GUIcku
        aPanel_noveRozmery = Konstanty.ROZMER_POLICKA * aRozmeryHraciehoPola;
        
        aComboBox_rozmeryHry = new JComboBox<>();
        aComboBox_rozmeryHry.setFocusable(false);
        
        aComboBox_rozmeryHry.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] {"4x4", "6x6", "8x8", "10x10", "12x12"}));
        aComboBox_rozmeryHry.setToolTipText("");

        aComboBox_farbaHraca = new JComboBox<>();
        aComboBox_farbaHraca.setFocusable(false);
        
        aComboBox_farbaHraca.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { Farba.BIELY.toString(), Farba.CIERNY.toString()}));
        aComboBox_farbaHraca.setToolTipText("");


        
        aLbl_aktualneRozmery = new JLabel("Aktuálne rozmery: " + this.aRozmeryHraciehoPola + "x" + this.aRozmeryHraciehoPola + "     ");
        aLbl_aktualneRozmery.setFocusable(false);
        aLbl_poctyKamenov = new JLabel("                 ");
        aLbl_poctyKamenov.setFocusable(false);
        
        aLbl_info = new JLabel("         ");
        aLbl_info.setFocusable(false);
        
        aButton_novaHra = new JButton("Nová hra");
        aButton_novaHra.setFocusable(false);
        

        //popridavanie jednotlivych komponentov do GUIcka
        this.setLayout(null);

        this.add(aPanelHraciePole);
        this.add(aComboBox_rozmeryHry);
        this.add(aComboBox_farbaHraca);
        this.add(aButton_novaHra);
        this.add(aLbl_poctyKamenov);
        this.add(aLbl_info);


        aPanelHraciePole.setBounds(10, 10, 600, 600);
        inicializaciaKomponentov();

        setDefaultCloseOperation(3);
        setVisible(true); //okno sa teraz zobrazi
        
    }
    
    public void run(){
        //dokonci sa inicializacia GUIcka
        addKeyListener(this);
        vygenerujIkony();
        aButton_novaHra.addActionListener(this);
    }

    public void koniecHry(){
        aLbl_info.setText("KONIEC HRY");
        aLbl_poctyKamenov.setText("Biele: " + aHra.getPocetPodlaFarby(Farba.BIELY)
            + " čierne: " + aHra.getPocetPodlaFarby(Farba.CIERNY));
        Hrac vitaz = aHra.vitaz();
        if(vitaz != null){
           aLbl_info.setText("Zvíťazil: " + vitaz.getaFarbaKamena().toString());
        }else{
            aLbl_info.setText("Remíza!");
        }
                
    }


    //funkcia vykresli kamen na hracom poli, podla parametra
    public void vykresliKamen(HernyKamen paHernyKamen){
        int riadok = paHernyKamen.getRiadok();
        int stlpec = paHernyKamen.getStlpec();
        
        
         if(aKamene[riadok][stlpec] == null){
            aKamene[riadok][stlpec] = new JLabel();
        }
        
        JLabel kamen = aKamene[riadok][stlpec];
        
        kamen.setBounds(stlpec * Konstanty.ROZMER_POLICKA, riadok * Konstanty.ROZMER_POLICKA, Konstanty.ROZMER_POLICKA, Konstanty.ROZMER_POLICKA);
        kamen.setIcon(paHernyKamen.getIkona());
        
        
        aPanelHraciePole.add(kamen);
        aPanelHraciePole.revalidate();
        aPanelHraciePole.repaint();
        aPanelHraciePole.setOpaque(false);
        validate();
    }
    
    //funkcia prekresli cele hracie pole(kamene v nom)
    public void prekresliHraciePole(){
        HernyKamen[][] hraciePole = aHra.getaHraciePole();
        for(int i = 0; i < this.aRozmeryHraciehoPola; i++){
            for(int j = 0; j < this.aRozmeryHraciehoPola; j++){
                if(aKamene[i][j] != null){
                    aPanelHraciePole.remove(aKamene[i][j]);
                }
            }
        }                            
                 
        
        
        for(int i = 0; i < this.aRozmeryHraciehoPola; i++){
            for(int j = 0; j < this.aRozmeryHraciehoPola; j++){
                if(hraciePole[i][j] != null){
                    vykresliKamen(hraciePole[i][j]);
                }
            }
        }
    }
    
    //inicializacia komponentov v GUIcku
    private void inicializaciaKomponentov(){
        Dimension size;

            int panelDimDifference = aPanel_noveRozmery - aPanel_predosleRozmery;
            
    size = aComboBox_rozmeryHry.getPreferredSize();
    aComboBox_rozmeryHry.setBounds(aPanel_predosleRozmery + panelDimDifference + 50, 40,
                 size.width, size.height);

    size = aComboBox_farbaHraca.getPreferredSize();
    aComboBox_farbaHraca.setBounds(aPanel_predosleRozmery + panelDimDifference + 50, 80,
                 size.width, size.height);

    size = aButton_novaHra.getPreferredSize();
    aButton_novaHra.setBounds(aPanel_predosleRozmery + panelDimDifference + 50, 120,
                 size.width, size.height);

    size = aLbl_poctyKamenov.getPreferredSize();
    aLbl_poctyKamenov.setBounds(aPanel_predosleRozmery + panelDimDifference + 50, 160,
                 300, size.height);

    size = aLbl_info.getPreferredSize();
    aLbl_info.setBounds(aPanel_predosleRozmery + panelDimDifference + 50, 180,
             300, size.height);
    }

    //inicializacia GUIcka pri spusteni novej hry
    public void novaHraInicializaciaGUI(){
        aLbl_info.setText(" ");
        //odstrania sa vsetky kamene z hracieho pola
        aPanelHraciePole.removeAll();

        //prekresli sa
        aPanelHraciePole.revalidate();
        aPanelHraciePole.repaint();
        validate();
        
        aPanel_predosleRozmery = aPanel_noveRozmery;

        //nastavia sa nove rozmery okna
        setBounds(Konstanty.POCIATOCNA_X_POZICIA_OKNA, Konstanty.POCIATOCNA_Y_POZICIA_OKNA, 
                Konstanty.OKNO_OFFSET_X + Konstanty.ROZMER_POLICKA * (aRozmeryHraciehoPola), 
                Konstanty.OKNO_OFFSET_Y + Konstanty.ROZMER_POLICKA * (aRozmeryHraciehoPola));

        aPanel_noveRozmery = Konstanty.ROZMER_POLICKA * (aRozmeryHraciehoPola);

        //znovu sa inicializuju komponenty (poposuvaju pri zmene velkosti hracieho pola)
            inicializaciaKomponentov();
        
    }

    //vygeneruju sa ikonky volneho tahu a zvyrazneneho policka, ktore sa neskor budu pouzivat pocas hry
    public void vygenerujIkony(){
        File file = new File(".");
        
        try {
        BufferedImage imgVolnyTah;
        BufferedImage imgZvyraznene;
       
        
            imgVolnyTah = ImageIO.read(new File("src\\main\\java\\obrazky\\volnyTah.png"));
            imgZvyraznene = ImageIO.read(new File("src\\main\\java\\obrazky\\zvyraznene.png"));
        
            
            Image dimgVolnyTah = imgVolnyTah.getScaledInstance(Konstanty.ROZMER_POLICKA, Konstanty.ROZMER_POLICKA,
        Image.SCALE_SMOOTH);
        Image dZvyraznene = imgZvyraznene.getScaledInstance(Konstanty.ROZMER_POLICKA, Konstanty.ROZMER_POLICKA,
        Image.SCALE_SMOOTH);
        
        aVolnyTahIkona = new ImageIcon(dimgVolnyTah);
        aZvyrazneneIkona = new ImageIcon(dZvyraznene);
        
        } catch (IOException e) {
            System.out.println("Nepodarilo sa nacitat niektore obrazky!");
        }
    }
    
  
    //vykresli na hracom poli volne tahy
    public void vykresliVolneTahy(ArrayList<Pozicia> paVolneTahy){
        for(int i = 0; i < this.aRozmeryHraciehoPola; i++){
            for(int j = 0; j < this.aRozmeryHraciehoPola; j++){
                if(aKamene[i][j] == null) continue;

                for(MouseListener ml : aKamene[i][j].getMouseListeners()){
                    aKamene[i][j].removeMouseListener(ml);
                }
                
                for(MouseMotionListener mml : aKamene[i][j].getMouseMotionListeners()){
                    aKamene[i][j].removeMouseMotionListener(mml);
                }

            }
        }
        
        for(Pozicia pozicia : paVolneTahy){
            int riadok = pozicia.getX();
            int stlpec = pozicia.getY();
            
            if(aKamene[riadok][stlpec] == null){
                aKamene[riadok][stlpec] = new JLabel();
            }
            
            JLabel kamen = aKamene[riadok][stlpec];
            
            //nastavia sa na danom policku actionListenery, pre vstup kurzora na dane policko sa policko zvyrazni
            kamen.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    kamen.setIcon(aZvyrazneneIkona);
                }
                
                @Override
                public void mouseExited(MouseEvent e) {//pri opusteni kurzora sa zobrazi len policko s moznym tahom
                    kamen.setIcon(aVolnyTahIkona);
                    
                }
                
            });

            //pri kliknuti na policko s volnym tahom sa vykona tato akcia
            kamen.addMouseListener(new MouseAdapter() {
                
                @Override
                public void mouseClicked(MouseEvent evt) {
                    if(aHra == null) return;
                    
                    boolean error = false;

                    //ak je prave na tahu hrac, nie pocitac
                    if(!aHra.hracNaTahu().jePocitac()){
                        //vytvori sa pomocny zoznam volnych tahov
                        ArrayList<Pozicia> mozneTahy = aHra.volneTahy(aHra.hracNaTahu());

                        //ak existuju nejake volne tahy
                        if(!mozneTahy.isEmpty()){
                            aHra.setaNieJeVolnyTah(false);
                            
                            int s = stlpec;
                            int r = riadok;

                            for(Pozicia pozicia : mozneTahy){
                                if(pozicia.getX() == r && pozicia.getY() == s){
                                    //a kedze hrac uz na dane policko s volnym tahom klikol, inak by sme tu neboli
                                    //dane policko sa prefarbi na hracovu farbu
                                    aHra.nastavPolickoNaFarbu(aHra.hracNaTahu(), new Pozicia(r, s));
                                    aHra.hracNaTahu().inkrementujPocetKamenov(); //a zvysi sa jeho pocet kamenov
                                    error = false;
                                    break;
                                }else{
                                    error = true;
                                }
                            }
                            
                            if(!error){ //po prefarbeni policka
                                //sa prefarbia vsetky kamene, ktore su obklucene kamenmi opacnej farby
                                aHra.prefarbiKamene(aHra.getaHraciePole(), aHra.hracNaTahu(), aHra.protihracLKHracovi(aHra.hracNaTahu()), r, s);
                                prekresliHraciePole();
                                aHra.setDalsiHracNaTahu(); //a uz je na tahu dalsi hrac
                                vykresliVolneTahy(aHra.volneTahy(aHra.hracNaTahu())); //vykreslia sa volne tahy zase pre neho
                                aLbl_poctyKamenov.setText("Biele: " + aHra.getPocetPodlaFarby(Farba.BIELY)
                                    + " čierne: " + aHra.getPocetPodlaFarby(Farba.CIERNY));
                            }
                            
                        }else{ //ak nie su volne tahy
                            if(aHra.getaNieJeVolnyTah()){ //a uz ani predtym neboli volne tahy
                                //tak to znamena, ze ziadny hrac nemoze spravit tah a hra konci
                                koniecHry();
                            }

                            aHra.setaNieJeVolnyTah(true);
                        }
                        
                        do{
                        //toto sa cyklicky opakuje az do skoncenia hry
                        
                        if(!error){
                            
                            Pozicia p = aHra.pocitacSpraviTah(aHra.hracNaTahu());
                            
                            if(p == null){
                                if(aHra.getaNieJeVolnyTah()){
                                    koniecHry();
                                    break;
                                }
                                aHra.setaNieJeVolnyTah(true);
                            }else{
                                aHra.setaNieJeVolnyTah(false);
                                
                                aHra.prefarbiKamene(aHra.getaHraciePole(), aHra.hracNaTahu(), aHra.protihracLKHracovi(aHra.hracNaTahu()), p.getX(), p.getY());
                            }
                            
                            
                            prekresliHraciePole();
                            
                            aHra.setDalsiHracNaTahu();
                            ArrayList<Pozicia> volneTahy = aHra.volneTahy(aHra.hracNaTahu());
                            if(volneTahy.isEmpty()){
                                aHra.setaNieJeVolnyTah(true);
                                aHra.setDalsiHracNaTahu();
                            }else{
                                aHra.setaNieJeVolnyTah(false);
                            }
                            
                            volneTahy = aHra.volneTahy(aHra.hracNaTahu());
                            vykresliVolneTahy(volneTahy);
                            aLbl_poctyKamenov.setText("Biele: " + aHra.getPocetPodlaFarby(Farba.BIELY)
                                + " čierne: " + aHra.getPocetPodlaFarby(Farba.CIERNY));
                        }
                        }while(aHra.getaNieJeVolnyTah());
                    }
                
                    
                    
                }
            });

            kamen.setBounds(stlpec * Konstanty.ROZMER_POLICKA, riadok * Konstanty.ROZMER_POLICKA, Konstanty.ROZMER_POLICKA, Konstanty.ROZMER_POLICKA);
            kamen.setIcon(this.aVolnyTahIkona);
            aPanelHraciePole.add(kamen);
        }
        
        aPanelHraciePole.revalidate();
        aPanelHraciePole.repaint();
        aPanelHraciePole.setOpaque(false);
        validate();
        
    }
    

    //ponastavovanie pri novej hre
    public void novaHra(){
        
        this.aRozmeryHraciehoPola = getNastaveniaHraciehoPola();
        aLbl_aktualneRozmery.setText("Aktuálne rozmery: " + this.aRozmeryHraciehoPola + "x" + this.aRozmeryHraciehoPola);
        
        novaHraInicializaciaGUI();
        
        aHra = null;
        if(((String)aComboBox_farbaHraca.getSelectedItem()).equals(Farba.BIELY.toString())){
            aHra = new Hra(this.aRozmeryHraciehoPola, Farba.BIELY, Farba.CIERNY);
        }else{
            aHra = new Hra(this.aRozmeryHraciehoPola, Farba.CIERNY, Farba.BIELY);
        }
        aKamene = new JLabel[this.aRozmeryHraciehoPola][this.aRozmeryHraciehoPola];
        aHra.spustiHru();
        
        aLbl_poctyKamenov.setText("Biele: " + aHra.getPocetPodlaFarby(Farba.BIELY)
                    + " čierne: " + aHra.getPocetPodlaFarby(Farba.CIERNY));
        
        prekresliHraciePole();
        vykresliVolneTahy(aHra.volneTahy(aHra.hracNaTahu()));
        
        if(aHra.hracNaTahu().jePocitac()){
            aLbl_poctyKamenov.setText("Biele: " + aHra.getPocetPodlaFarby(Farba.BIELY)
                    + " čierne: " + aHra.getPocetPodlaFarby(Farba.CIERNY));
            
            Pozicia p = aHra.pocitacSpraviTah(aHra.hracNaTahu());
            
            aHra.prefarbiKamene(aHra.getaHraciePole(), aHra.hracNaTahu(), aHra.protihracLKHracovi(aHra.hracNaTahu()), p.getX(), p.getY());
            
            aHra.setDalsiHracNaTahu();
            aLbl_poctyKamenov.setText("Biele: " + aHra.getPocetPodlaFarby(Farba.BIELY)
                    + " čierne: " + aHra.getPocetPodlaFarby(Farba.CIERNY));
            
            
            prekresliHraciePole();
            vykresliVolneTahy(aHra.volneTahy(aHra.hracNaTahu()));
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent evt){
        novaHra();
    } //po kliknuti na tlacidlo nova hra, sa vykona toto
    
    private int getNastaveniaHraciehoPola(){ //vrati nastavenie z comboboxu
        String dimensions = (String)aComboBox_rozmeryHry.getSelectedItem();
        String[] dim = dimensions.split("x");
        return Integer.parseInt(dim[0]);
    }
    

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) { //pri stlaceni R sa hra restartuje, ESC ukonci program
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
            this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        }
        
        if(e.getKeyCode() == KeyEvent.VK_R){
            novaHra();
        }
    }
    
}


