package Model;

import Controller.Main;
import Model.Item;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Conf {

    public static final String VERSION = "0.5";

// ---------------------------- TABLE ---------------------------------------
// --------------------------------------------------------------------------
    public static final int JTABLE1SORTCOLUMN = 8, // Bid price
            JTABLE2SORTCOLUMN = 4, // Rarity
            JTABLE3SORTCOLUMN = 4, // Rarity
            JTABLE2ITEMNAMECOLUMN = 6, // Item Name
            JTABLE2RARITYCOLUMN = 7, // Rarity
            JTABLE2SELLERCOLUMN = 10; // Seller

    public static final Object[] getItemObject(Item i) {
        return new Object[]{
            i.getBid(), i.getBuyout(), i.getAvgPrice(), i.getnDB(),
            new DecimalFormat("#.##").format(i.getPercBid()), new DecimalFormat("#.##").format(i.getPercBuyout()),
            i.getName(), i.getRarity(), i.getAvgBidProfit(), i.getAvgBuyoutProfit(),
            i.getSeller(), i.getSellerReputation(), new DecimalFormat("#.##").format(i.getNoobScore()),
            i.getTimeLeft(), i.getAvgDurability(),
            i.getnMarketAll(), i.getMarketAvgBidAll(), i.getMarketAvgBuyoutAll(), i.getnMarketDirect(), i.getMarketAvgBidDirect(), i.getMarketAvgBuyoutDirect(), i.getnMarketIndirect(), i.getMarketAvgBidIndirect(), i.getMarketAvgBuyoutIndirect(),
            i.getLastBuyDate(), i.getLastSeller(), i.getLastBuyer(), i.getLastPrice(), i.getLastDurability(), i.getLastBuyoutProfit()
        };

    }

// ---------------------------- TABB ----------------------------------------
// --------------------------------------------------------------------------
    public static final int ITEMHISTORYTAB = 1,
            SETTINGSTAB = 0; // Position of the settings tab
// ---------------------------- FILES ---------------------------------------
// --------------------------------------------------------------------------
    public static long RUNTIME = 60 * 1000; // 10 * 1000 => 10 s

    public static final int SNIFFERMINSLEEP = 30, // 5 * 60 => 5 min
            SNIFFERMAXSLEEP = 45, // 15 * 60 => 15 min
            UPDATERSHORTSLEEPTIME = 3; // 1 * 60 => 1 min

    public static final String PATHTOEXECUTESHELL = "./sh/",
            PATHIMG = "./img/",
            PATHDB = "./db/",
            PATHBACKUP = "backup/",
            CURLSCRIPT = "curl.sh",
            SNIFFERSCRIPT = "sniffer.sh",
            CURL = "sh " + CURLSCRIPT,
            SNIFFER = "sh " + SNIFFERSCRIPT,
            FILEHTML = "out.html",
            FILEHTMLSNIFFER = "outSniffer.html",
            PATHHTML = PATHTOEXECUTESHELL,
            UNKNOWNRARITY = "UnknownRarity",
            EXTENSIONCSV = ".csv",
            STRINGDBSCSV = "dbs",
            STRINGDBCCSV = "dbc",
            STRINGPOINT = ".",
            PATHTOFILEONDISK = PATHIMG + "icon.png",
            FILEDBCCSV = STRINGDBCCSV + EXTENSIONCSV,
            FILEDBCCSS = STRINGDBSCSV + EXTENSIONCSV,
            DATE_FORMAT_NOW = "yyyy-MM-dd.HH:mm:ss",
            TITLE = "The Infinite Black - Trading Tool v" + VERSION,
            FIRSTLINEDBC = "Date,Seller,Buyer,Item,Price,Durability,Rarity",
            URLMAIN = "http://www.theinfiniteblack.com/community/tib/leaderboards/AH.aspx?playername=&server=3";

    public static int DEFAULTAUTOUPDATETIMEUPDATER = 30,
            GRAPHL = 800, GRAPHH = 600,
            CSVITEMDBLENGTH = 7;

// ---------------------------- HTMLUNIT + JSOUP ----------------------------
// --------------------------------------------------------------------------
    public static boolean PRINTLOGS = false;
    public static int SLEEPJSMIN = 10, // AntiblackList Min
            SLEEPJSMAX = 1200, // AntiblackList Max
            MAXMONTH = 12 * 5, // 5 years max
            DEFAULTDAYSTOUPDATE = 50;

    public static String MESSAGERESTART = "Please restart the application to update the database.",
            MESSAGETITLERESTART = "Please Restart";

// ---------------------------- DIVERS --------------------------------------
// --------------------------------------------------------------------------
    public static String getRarity(String key, Map<String, String> rarityMap) {
        String res = Conf.UNKNOWNRARITY;

        if (rarityMap.containsKey(key)) {
            res = rarityMap.get(key);
        }

        return res;
    }

    /**
     * Generate a random number between min and max. NOTE: Usually this should
     * be a field rather than a method variable so that it is not re-seeded
     * every call.
     *
     * @param min
     * @param max
     * @return
     */
    public static int randInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min; // nextInt is normally exclusive of the top value, so add 1 to make it inclusive
        return randomNum;
    }

    /**
     * Execute Shell in console
     *
     * @param command
     * @param path
     * @return
     */
    public final static Process executeLxterminal(final String command, final String path) {
        Process res = null;
        final String terminalLauncher = "lxterminal -e \"" + command + "\"";
        try {
            res = Runtime.getRuntime().exec(
                    new String[]{"bash", "-c", terminalLauncher},
                    null,
                    new File(path)//filePath + ExecDir)
            );
            System.out.println("executeLxterminal: I: Command started Sucessfully ! [" + command + "]");
        } catch (IOException ex) {
            System.out.println("executeLxterminal: E: " + ex);
        }
        return res;
    }

    /**
     * Executes a command in shell, with a blocking terminal
     *
     * @param command
     * @param path Path where to execute the shell script
     * @return
     */
    public final static Process executeBlocking(final String command, final String path) {
        Process res = executeLxterminal(command, path);
        try {
            res.waitFor();
        } catch (InterruptedException ex) {
            Logger.getLogger(Conf.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }

    /**
     * Remove all lines from a JTable
     *
     * @param jt A JTable
     */
    public static void removeLines(JTable jt) {
        DefaultTableModel tm = (DefaultTableModel) jt.getModel();
        if (tm.getRowCount() > 0) {
            for (int i = tm.getRowCount() - 1; i > -1; i--) {
                tm.removeRow(i);
            }
        }
    }

    /**
     *
     * @return Current date to String
     */
    public static String now() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        return sdf.format(cal.getTime());
    }

    /**
     * Restart Application after updates.
     */
    public static void restartApplication() {
        try {
            final String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
            final File currentJar = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            /* is it a jar file? */
            if (!currentJar.getName().endsWith(".jar")) {
                return;
            }

            /* Build command: java -jar application.jar */
            final ArrayList<String> command = new ArrayList<>();
            command.add(javaBin);
            command.add("-jar");
            command.add(currentJar.getPath());

            final ProcessBuilder builder = new ProcessBuilder(command);
            builder.start();
            System.exit(0);
        } catch (URISyntaxException | IOException ex) {
            Logger.getLogger(Conf.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void infoBox(String infoMessage, String titleBar) {
        JOptionPane.showMessageDialog(null, infoMessage, "InfoBox: " + titleBar, JOptionPane.INFORMATION_MESSAGE);
    }
}
