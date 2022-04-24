package models;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

public class PlayerController extends IG.Touche{
    private Player player;
    private Set<Player> otherPlayers;
    private boolean shouldReply;
    private int count;
    private boolean ingenieurDry;
    private boolean piloteFlight;
    private boolean useSand;
    private Case saveCase;
    private Set<Player> helico;

    public PlayerController(Player p, JFrame fenetre, Set<Player> players){
        fenetre.addKeyListener(this);
        this.player = p;
        this.otherPlayers = players;
        this.shouldReply = false;
        this.ingenieurDry = false;
        this.piloteFlight = false;
        this.useSand = false;
    }

    //getters
    public boolean ShouldReply(){
        return this.shouldReply;
    }

    public int getCount(){
        return this.count;
    }

    public Player getPlayer(){return this.player;}

    //setters
    public void StartReply(){
        this.shouldReply = true;
        this.helico = new HashSet<Player>();
        this.count = 3;
        if (this.player.getRole() == Player.ROLE.INGENIEUR)
            this.ingenieurDry = true;
        if (this.player.getRole() == Player.ROLE.PILOTE)
            this.piloteFlight = true;
    }

    public void StopReply(){
        this.shouldReply = false;
    }

    public void keyPressed(KeyEvent e){
    };
    public void keyReleased(KeyEvent e){
    };
    private void updateCardInventory(Player p){
        // actualise l'inventaire du joueur pc
        models.Views theViews = p.getCase().getPlateau().getTheController().getView();
        theViews.allInventoryView.inventoriesViews[p.getIdentifier()].setTexteKey(p.getCarteTresors());;
    }

    public void setCount(int c){this.count = c;}

    public void keyTyped(KeyEvent e) {
        if (this.shouldReply) {
            Player moi = this.player;
            //god mod
            if (moi.getName().equals("fred"))
                this.count = -100;
            moi.getCase().getController().repaint();
            boolean haveToChoose = false;
            moi.getCase().getController().changeTexte(" ");
            int xRef = -1;
            int yRef = -1;
            if(moi.isDryMode()){
                // this.saveCase not null
                xRef = this.saveCase.getX();
                yRef = this.saveCase.getY();
            }
            boolean b = false;
            switch (e.getKeyChar()) {
                case 'd':
                case 'D':
                    if (moi.isDryMode())
                        moi.moveDirDry(Case.Dir.DROITE, xRef, yRef,this.useSand);
                    else
                        b = moi.moveDir(Case.Dir.DROITE);
                    break;
                case 'q':
                case 'Q':
                    if (moi.isDryMode())
                        moi.moveDirDry(Case.Dir.GAUCHE, xRef, yRef,this.useSand);
                    else
                        b = moi.moveDir(Case.Dir.GAUCHE);
                    break;
                case 'z':
                case 'Z':
                    if (moi.isDryMode())
                        moi.moveDirDry(Case.Dir.HAUT, xRef, yRef,this.useSand);
                    else
                        b = moi.moveDir(Case.Dir.HAUT);
                    break;
                case 's':
                case 'S':
                    if (moi.isDryMode())
                        moi.moveDirDry(Case.Dir.BAS, xRef, yRef,this.useSand);
                    else
                        b = moi.moveDir(Case.Dir.BAS);
                    break;
                case 'a':
                case 'A':
                    if (moi.getRole() == Player.ROLE.EXPLORATEUR && !moi.isDryMode())
                        b = moi.moveDir(Case.Dir.NW);
                    break;
                case 'e':
                case 'E':
                    if (moi.getRole() == Player.ROLE.EXPLORATEUR && !moi.isDryMode())
                        b = moi.moveDir(Case.Dir.NE);
                    break;
                case 'w':
                case 'W':
                    if (moi.getRole() == Player.ROLE.EXPLORATEUR && !moi.isDryMode())
                        b = moi.moveDir(Case.Dir.SW);
                    break;
                case 'c':
                case 'C':
                    if (moi.getRole() == Player.ROLE.EXPLORATEUR && !moi.isDryMode())
                        b = moi.moveDir(Case.Dir.SE);
                    break;
                case 'f':
                case 'F':
                    if (! moi.isFlightMode() && ! moi.isDryMode()) {
                        moi.enableDry();
                        this.saveCase = moi.getCase();
                    }else {
                        if (moi.isDryMode()) {
                            b = moi.assecheCase();
                            moi.disableDry();
                            moi.moveCase(this.saveCase);
                            if (b && this.ingenieurDry) {
                                this.ingenieurDry = false;
                                this.count++;
                            }
                        }
                    }
                    break;
                case 'l':
                case 'L':
                    if (! moi.isFlightMode() && ! moi.isDryMode() && moi.hasSand()) {
                        moi.enableDry();
                        this.useSand = true;
                        this.saveCase = moi.getCase();
                    }else {
                        if (moi.isDryMode()) {
                            b = moi.assecheCase();
                            if(b) {
                                moi.useSand();
                                b = false;
                            }
                            moi.disableDry();
                            moi.moveCase(this.saveCase);
                        }
                    }
                    break;
                case 'r':
                case 'R':
                    if (! moi.isFlightMode() && ! moi.isDryMode()) {
                        b = moi.ramasseArtefact();
                        models.Controllers theController = moi.getCase().getPlateau().getTheController();
                        theController.getView().allInventoryView.inventoriesViews[getPlayer().getIdentifier()].setTexteKey(getPlayer().getCarteTresors());
                    }
                    break;
                    /*
                case 'i':
                    System.out.println(this.getPlayer().inventory());
                    break;
                     */
                case 'p':
                case 'P':
                    if (!moi.isFlightMode() && this.piloteFlight && ! moi.isDryMode()) {
                        moi.enableFlight();
                        this.piloteFlight = false;
                        System.out.println(moi.isFlightMode());
                    }
                    else {
                        if (moi.getRole() == Player.ROLE.PILOTE && moi.isFlightMode()) {
                            moi.disableFlight();
                            b = true;
                        }
                    }
                    break;
                case 'h':
                case 'H':
                    if (!moi.isFlightMode() && ! moi.isDryMode() && moi.hasHelico()) {
                        this.helico = moi.chooseHelico(this.otherPlayers);
                        for (Player h : this.helico)
                            h.enableFlight();
                    }
                    else {
                        if (moi.isFlightMode()) {
                            moi.disableFlight();
                            for (Player h : this.helico) {
                                Case old = h.getCase();
                                h.disableFlight();
                                h.moveCase(moi.getCase());
                                old.getController().repaint();
                            }
                            moi.useHelico();
                        }
                    }
                    break;
                case 'n':
                case 'N':
                    if (moi.getRole() == Player.ROLE.NAVIGATEUR) {
                        //TP le joueur choisi sur le navigateur
                        Player autre = moi.choosePlayer(otherPlayers, true,"Choisissez un joueur à déplacer :");
                        Case old = autre.getCase();
                        autre.moveCase(moi.getCase());
                        old.getController().repaint();
                        b = true;
                    }
                    break;

                case 'm':
                case 'M':
                    // messager
                    Player pm = moi.choosePlayerAllPlateau(false);
                    if(pm != moi) {
                        CarteTresor c = moi.chooseCarte("", false);
                        if(c != null) {
                            moi.getCarteTresors().remove(c);
                            pm.getCarteTresors().add(c);
                            updateCardInventory(moi);
                            updateCardInventory(pm);
                            b = true;
                        }
                    }
                    break;

                case 'o':
                case 'O':
                    // echangeDeClef
                    Player p = moi.choosePlayerOnMyCaseWithLessThan4Cards(false);
                    if(p != moi) {
                        CarteTresor c = moi.chooseCarte("", false);
                        if(c != null) {
                            moi.getCarteTresors().remove(c);
                            p.getCarteTresors().add(c);
                            updateCardInventory(moi);
                            updateCardInventory(p);
                            b = true;
                        }
                    }
                    break;
                case 't':
                case 'T':
                    // defausse d'une carte
                    CarteTresor c = moi.chooseCarte("Defausse d'une carte",false);
                    if(c != null){
                        moi.getCarteTresors().remove(c);
                        moi.getCase().getPlateau().getPaquetCarteTresor().Defausse(c);
                        updateCardInventory(moi);
                    }
            }

            if (moi.isFlightMode())
                b = false;
            if(! b)
                this.count++;

            moi.getCase().getController().changeTexte(" ");
            moi.getCase().getController().repaint();
            this.count--;
        }
    }
}
