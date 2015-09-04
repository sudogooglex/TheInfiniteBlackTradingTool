package Controller;

import Model.Conf;
import Model.DBS;
import Model.DBC;
import Model.Item;
import Model.ItemDB;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Sniffer {

    private List<Item> itemList;

    private List<ItemDB> itemListSniffer;
    private Thread updaterThread;
    private final BotWeb botsniffer;
    private final DBC dbc;
    private final DBS dbs;

    public Sniffer(BotWeb bot) {
        itemListSniffer = new ArrayList<>();
        botsniffer = bot;
        dbc = new DBC();
        dbc.load();
        dbs = new DBS();
        dbs.load();
    }

    /**
     * 1) Wait until md5 of output changes 2)
     */
    public void updateDbSniffer() {
        try {
            System.out.println("updateDbSniffer: I: Updating Database with file " + Conf.PATHHTML + Conf.FILEHTMLSNIFFER);
            File in = new File(Conf.PATHHTML + Conf.FILEHTMLSNIFFER);
            Document doc = Jsoup.parse(in, null);
            itemList = new ArrayList<>();
            for (Element table : doc.select("table[id=ListView1_itemPlaceholderContainer]")) {
                for (Element row : table.select("tr:gt(0)")) { // skip first line

                    Elements tds = row.select("td:not([rowspan])");
// 1. Create Item
                    String date = tds.get(0).text(),
                            seller = tds.get(1).text(),
                            buyer = tds.get(2).text(),
                            itemName = tds.get(3).text(),
                            rarity = Conf.getRarity(tds.get(3).child(0).child(0).attr("style").split(":")[1], botsniffer.getRarityMap()),
                            priceString = tds.get(4).text();
                    long price = Long.parseLong(tds.get(4).text().substring(1).replace(",", "")),
                            durability = Long.parseLong(tds.get(5).text().substring(1).replace(",", ""));

// 3. Update Item in the list
                    ItemDB it = new ItemDB(date, seller, buyer, itemName, rarity, priceString, price, durability);
                    itemListSniffer.add(it);
                }
                //select("style").first().data()
            }
        } catch (IOException ex) {
            Logger.getLogger(BotWeb.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateCVSSniffer() {
//        printInfos();
        updateDbSniffer();
        mergeSniffer();
        //printItemList();
//        printTopLowBid();
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
                        botsniffer.requestSniffer();
                        botsniffer.updateCVSSniffer();

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

    public void mergeSniffer() {
        System.out.println("compareData: I: Merging " + itemListSniffer.size()
                + " elements from sniffer with " + dbc.getIdList().size()
                + " from DBC and " + dbs.getIdList().size() + " from DBS ...");

        for (ItemDB i : itemListSniffer) {
//            if (dbc.getIdList().contains(i)) {
//                System.out.println("mergeSniffer: Duplicate found in DBC : " + i);
//            } else if (dbs.getIdList().contains(i)) {
            if (dbs.getIdList().contains(i)) {
//                System.out.println("mergeSniffer: Duplicate found in DBS : " + i);
            } else {
                System.out.println("mergeSniffer: Add " + i);
                dbs.getIdList().add(i);
            }
        }
        itemListSniffer = new ArrayList<>();
        dbs.backupCSV();
        dbs.registerToCSV();
    }

//    void uploadBuyList() {
//    }
}
