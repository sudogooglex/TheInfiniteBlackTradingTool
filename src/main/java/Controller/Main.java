package Controller;

import Model.Conf;
import View.UI;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

//    private static BotWeb b;
    private Thread snifferThread, updaterThread;
    private static UI ui;

    public Main() {
        ui = new UI();
//        b = new BotWeb();
    }

    public static void main(String[] args) {
        Main m = new Main();
        ui.startUI();
//        m.runSniffer(); // Thread 1
//        m.runUpdater(); // Thread 2
//        m.waitThreads();
    }

    public void sniff() {
//        ui.getB().requestClient();
        ui.getB().sleepRandom();
    }

    public void update() {
//        b.updateDb();
        ui.getB().compareDataClient();
//        b.uploadBuyList();
    }

    /**
     * Sniff web site to recover data in an independent thread evry
     * [Config.SNIFFERMINSLEEP, Config.SNIFFERMAXSLEEP] seconds during
     * Config.RUNTIME
     */
    public void runSniffer() {
        snifferThread = new Thread() {
            @Override
            public void run() {
                final int sleepRand = Conf.randInt(Conf.SNIFFERMINSLEEP, Conf.SNIFFERMAXSLEEP);
                int sleepTime = 1000 * sleepRand; // délai avant de répéter la tache : 2000 = 2  seconde
                long startTime = 0; // délai avant la mise en route (0 demarre immediatement)
                Timer timer = new Timer(); // création du timer
                TimerTask tache = new TimerTask() { // création et spécification de la tache à effectuer
                    @Override
                    public void run() {
                        sniff();
                        System.out.println("runSniffer: I: Sleeping " + sleepRand + "s ...");
                    }
                };
                timer.scheduleAtFixedRate(tache, startTime, sleepTime);  // ici on lance la mecanique
                try {
                    sleep(Conf.RUNTIME); // Arreter la mécanique après un certain temps
                } catch (InterruptedException ex) {
                    System.out.println("runSniffer: E: " + ex.getMessage());
                }
                timer.cancel();
                System.out.println("runSniffer: W: Sniffer Timeout of " + Conf.RUNTIME / 1000 + "s has expired.");
            }
        };
        snifferThread.start();
    }

    /**
     * Uses the data from the sniffer to find good items evry
     * [Config.SNIFFERMINSLEEP, Config.SNIFFERMAXSLEEP] seconds during
     * Config.RUNTIME
     */
    public void runUpdater() {
        updaterThread = new Thread() {
            @Override
            public void run() {
                final int sleepRand = Conf.UPDATERSHORTSLEEPTIME;
                int sleepTime = 1000 * sleepRand; // délai avant de répéter la tache : 2000 = 2  seconde
                long startTime = 0; // délai avant la mise en route (0 demarre immediatement)
                Timer timer = new Timer(); // création du timer
                TimerTask tache = new TimerTask() { // création et spécification de la tache à effectuer
                    @Override
                    public void run() {
                        update();
                        System.out.println("runUpdater: I: Sleeping " + sleepRand + "s ...");
                    }
                };
                timer.scheduleAtFixedRate(tache, startTime, sleepTime);  // ici on lance la mecanique
                try {
                    sleep(Conf.RUNTIME); // Arreter la mécanique après un certain temps
                } catch (InterruptedException ex) {
                    System.out.println("runUpdater: E: " + ex.getMessage());
                }
                timer.cancel();
                System.out.println("runUpdater: W: Updater Timeout of " + Conf.RUNTIME / 1000 + "s has expired.");
            }
        };
        updaterThread.start();
    }

    /**
     * Wait for the sniffer and the updater
     */
    private void waitThreads() {
        try {
            snifferThread.join();
            updaterThread.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
