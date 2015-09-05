package Controller;

import Model.Conf;
import View.UI;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JSpinner;
import javax.swing.SwingUtilities;

/**
 * TODO: REMOVE THIS OLD UNUSED CLASS
 * This Class will perform an auto Update without beeing blacklisted. Using
 * random time sleep before refreshing data.
 * 
 */
public class AutoUpdate extends Thread {

    protected volatile boolean running = true;
    private final JSpinner jSpinner1;
    private final UI ui;

    public AutoUpdate(JSpinner j, UI mainUI) {
        jSpinner1 = j;
        ui = mainUI;
    }

    @Override
    public void run() {
        while (running) { // Infinite loop thread, generic method. Can be interrupted at any time.
            int n = (int) jSpinner1.getValue(),
                    totalSleep = (n + Conf.randInt(-n / 3, n / 3)) * 1000;

            System.out.println("AutoUpdate: I: Sleeping " + totalSleep / 1000 + " s ...");
            try {
                Thread.sleep(totalSleep); // Pause de 100 secondes
                SwingUtilities.invokeAndWait(new Runnable() { // deal with threads and swing and EDT : http://gfx.developpez.com/tutoriel/java/swing/swing-threading/
                    @Override
                    public void run() {
//                        ui.updateRealTime(); // only usefull part
                    }
                });
                System.out.println("AutoUpdate: I: Update Done.");
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt(); // Très important de réinterrompre
                break; // Sortie de la boucle infinie
            } catch (InvocationTargetException ex) {
                Logger.getLogger(AutoUpdate.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void startAutoUpdate() {
        start();
    }

    public void stopAutoUpdate() {
        running = false;
        interrupt();
        System.out.println("startSniffer: I: Thread will not continue the Update loop.");
    }
}
