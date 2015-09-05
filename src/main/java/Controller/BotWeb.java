package Controller;

import Model.Conf;
import Model.DBS;
import Model.DBC;
import Model.Item;
import Model.ItemDB;
import Model.JsExecution;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JCheckBox;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class BotWeb {

    public List<Item> itemList;
    public List<Item> itemListNew;
    public Map<String, String> rarityMap;
    public DBC dbc;
    public DBS dbs;
    public List<ItemDB> itemFrequency;
    public List<ItemDB> sellerItems;
    public Sniffer sn;
    public JsExecution previousMonth;
    public JsExecution currentDay;
    public List<JsExecution> jsList;
    public WebClient webClient;
    public WebClient realTimeWebClient;
    public int daysToUpdate;
    public int daysUpdated;
    public boolean stopUpdate;
    private final JProgressBar jpMonths;
    private final JProgressBar jpRealTime;
    private boolean firstPageIsLoaded;
    private HtmlPage mainHtml;

    public BotWeb(JProgressBar jpr, JProgressBar jpm) {
        jpMonths = jpm;
        jpRealTime = jpr;
        itemList = new ArrayList<>();
        itemListNew = new ArrayList<>();
        itemFrequency = new ArrayList<>();
        sellerItems = new ArrayList<>();
        rarityMap = new HashMap<>();
        rarityMap.put("#48D74D;", "g");
        rarityMap.put("#9696FF;", "b");
        rarityMap.put("#FFABE6;", "p");
        rarityMap.put("#FFC469;", "o");
        rarityMap.put("#FF4400;", "r");
        rarityMap.put("White;", "w");
        sn = new Sniffer(this);
        previousMonth = new JsExecution();
        currentDay = new JsExecution();
        jsList = new ArrayList<>();
        webClient = null;
        realTimeWebClient = null;
        daysToUpdate = Conf.DEFAULTDAYSTOUPDATE;
        daysUpdated = 0;
        stopUpdate = false;
        firstPageIsLoaded = false;
        mainHtml = null;
    }

    /**
     * Real time update
     *
     */
    public void updateRealTime() {
        // Turn warnings off
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.OFF);
        java.util.logging.Logger.getLogger("org.apache.http").setLevel(java.util.logging.Level.OFF);
        int step = 0, maxSteps = 4;

        try {
            if (!firstPageIsLoaded) {
// Initialize browser
                System.out.println("* updateRealTime: I: Initialization : Initializing browser");
                jpRealTime.setIndeterminate(true);
                setWebClient(new WebClient(BrowserVersion.CHROME));

// Page 1 : Connect
                System.out.println("* updateRealTime: I: PAGE 1 : Connecting to " + Conf.URLMAIN);
                mainHtml = getWebClient().getPage(Conf.URLMAIN);
                firstPageIsLoaded = true;
                jpRealTime.setIndeterminate(false);
            }

// Page 2 : View Current Sales
            System.out.println("* updateRealTime: I: PAGE 2 : View Current Sales");
            step++;
            jpRealTime.setValue((step * 100) / maxSteps);
            final HtmlSubmitInput button1 = mainHtml.getFirstByXPath("//*[@id=\"Button1\"]");
            final HtmlPage p2 = button1.click();
            step++;
            jpRealTime.setValue((step * 100) / maxSteps);
//            System.out.println(p2.asXml());
//            System.out.println("TODO: Paarse that shit");
            Document doc = Jsoup.parse(p2.asXml(), Conf.URLMAIN);
            step++;
            jpRealTime.setValue((step * 100) / maxSteps);
            addAllItems(doc);
            step++;
            jpRealTime.setValue((step * 100) / maxSteps);

// Compute statistics and Reset Bot
            compareDataClient();
            step++;
            jpRealTime.setValue((step * 100) / maxSteps);
            resetBot();
            step = 0;
            jpRealTime.setValue((step * 100) / maxSteps);
            System.out.println("* updateRealTime: I: Statistics: All done !");
        } catch (FailingHttpStatusCodeException | IOException ex) {
            Logger.getLogger(BotWeb.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Use the htmlUnit Internet browser to update DBC
     *
     */
    public void updateDBC() {
// Turn warnings off
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.OFF);
        java.util.logging.Logger.getLogger("org.apache.http").setLevel(java.util.logging.Level.OFF);

        try {
// Initialize browser
            System.out.println("* updateDBC: I: Initialization : Initializing browser");
            setWebClient(new WebClient(BrowserVersion.CHROME));

// Page 1 : Connect
            System.out.println("* updateDBC: I: PAGE 1 : Connecting to " + Conf.URLMAIN);
            final HtmlPage p1 = getWebClient().getPage(Conf.URLMAIN);

// Page 2 : View Archive History
            System.out.println("* updateDBC: I: PAGE 2 : See Archive history");
            final HtmlSubmitInput button2 = p1.getFirstByXPath("//*[@id=\"Button2\"]");
            final HtmlPage p2 = button2.click();
            updatejsListFirstCurrentDay(p2.asXml());

// Page 3+ : JS Click Calendar
            System.out.println("* updateDBC: I: PAGE 3+ : JS Click Calendar");
            setDbc(new DBC()); // TODO : use hashmap with key=date and remove 'new DBC()'.
            runAllMonths(p2, jpMonths);

// Pae 4 : Next in the same day.
//            System.out.println("* updateDBC: I: PAGE 4 : Next");
//            final HtmlSubmitInput buttonNext = p3.getFirstByXPath("//*[@id=\"ListView1_DataPager1\"]/input[3]");
//            final HtmlPage p4 = buttonNext.click();
//            addToDBC(p3.asXml(), true); // ça retourne les mêmes 100 résultats ...
//            System.out.println(p4.asXml());
// Write dbc to external file.
            getDbc().writeToFile(Conf.PATHDB + Conf.FILEDBCCSV);
            resetBot();
        } catch (FailingHttpStatusCodeException | IOException ex) {
            Logger.getLogger(BotWeb.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Run all months. Stops when the number of days to update or the month
     * limit is reached.
     *
     * @param htmlPage
     * @return
     */
    private HtmlPage runAllMonths(final HtmlPage htmlPage, JProgressBar jp) {
        jp.setIndeterminate(false); // desactivate loading on the progressBar
        HtmlPage res = htmlPage;
        updateStopUpdate();
        for (int i = 1; i <= Conf.MAXMONTH; i++) {
            if (isStopUpdate()) { // after runMonth(htmlPage)
                System.out.println(">>>>>>>>>>>>>>>>> STOP");
                break;
            } else {
                res = runMonth(res);
                updateStopUpdate();
                if (isStopUpdate()) {
                    break;
                }
                res = switchPreviousMonth(res);
            }
        }

        return res;
    }

    /**
     * Switch to the previous month in the clendar.
     *
     * @param htmlPage
     */
    private HtmlPage switchPreviousMonth(final HtmlPage htmlPage) {
        HtmlPage res = htmlPage;

        System.out.println("   switchPreviousMonth: I: Switching to month " + getPreviousMonth().getName());
        res = runJavaScript(getPreviousMonth().getJs(), res);
        updatejsListFirstCurrentDay(res.asXml()); // update Month List
        return res;
    }

    /**
     * Update FirstDay and LastDay usinghtml calendar of the month sent by the
     * html page.
     *
     * @param htmlPage
     */
    private void updatejsListFirstCurrentDay(final String htmlPage) {
        boolean end = false, addJe = false;
        setJsList(new ArrayList<JsExecution>()); // reset Month List
        Document doc = Jsoup.parse(htmlPage);
        Elements allTable = doc.select("table[id=Calendar1]");
        Element first = allTable.first().child(0).child(0).child(0).child(0).
                child(0).child(0).child(0).child(0);
        setPreviousMonth(new JsExecution(first.attr("href"), first.text()));
// For each tr in the html page
        for (Element row : allTable.select("tr:gt(0)")) { // skip first line
            if (end) {
                break;
            }
            Elements as = row.select("tr").select("td").select("a");
            for (Element a : as) {
                String style = a.attr("style");
                JsExecution je = new JsExecution(a.attr("href") + ";", a.attr("title"));
// Truncate the last month days
                if (je.getName().contains("01")) {
                    addJe = true;
                }
// Truncate the future days
                if (style.contains("White")) { // this is the current day in the calendar
                    setCurrentDay(je);
                    end = true;
                    addJe = false;
                    break;
                }
                if (addJe) {
                    getJsList().add(je);
                }
            }
        }
// Reverse list because parsing started from the beginning
        Collections.reverse(getJsList());
    }

    /**
     * Add all ItemDB of the month given in the html to the database.
     */
    private HtmlPage runMonth(final HtmlPage htmlFirst) {
        HtmlPage res = htmlFirst;

        if (getJsList().isEmpty()) {
            System.out.println("updateDBC: E: The calendar on the top left "
                    + "of the page '" + Conf.URLMAIN + "' after clicking "
                    + "on the 'View Archive History' button is generating "
                    + "an empty list of javascript functions.");
        } else {
            for (int i = 0; i <= getJsList().size() - 1; i++) {
                updateStopUpdate();
                if (isStopUpdate()) {
                    break;
                } else {
                    JsExecution je = getJsList().get(i);
                    System.out.println("   runMonth: I: " + je);
                    res = runJavaScript(je.getJs(), res); // TODO NEXT : use GUI to gather all data correctly
                    addToDBC(res.asXml());
                    sleepAntiBlackList();
                    setDaysUpdated(getDaysUpdated() + 1);
                    SwingUtilities.invokeLater(new Runnable() { // update gui progress bar
                        @Override
                        public void run() {
                            jpMonths.setValue((daysUpdated * 100) / daysToUpdate);
                        }
                    });
                }
            }
        }

        return res;
    }

    /**
     * Reset the bot and the progress bar.
     */
    private void resetBot() {
        setDaysUpdated(0);

        SwingUtilities.invokeLater(new Runnable() { // update gui progress bar
            @Override
            public void run() {
                jpMonths.setValue((daysUpdated * 100) / daysToUpdate);
            }
        });

    }

    /**
     * Set the boolean stopUpdate to true if all days are updated.
     */
    private void updateStopUpdate() {
        setStopUpdate(getDaysUpdated() >= getDaysToUpdate());
    }

    /**
     * Sleeps a random time to avoid beeing blacklisted.
     */
    private void sleepAntiBlackList() {
        try {
            Thread.sleep(Conf.randInt(Conf.SLEEPJSMIN, Conf.SLEEPJSMAX));
        } catch (InterruptedException ex) {
            Logger.getLogger(BotWeb.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Run Javascript like __doPostBack('Calendar1','" + javascriptString + "');
     * Send back an HTML page.
     *
     * @param javascriptString The day to look for
     * @param page The page where to search
     */
    private HtmlPage runJavaScript(final String javascript, final HtmlPage page) {
        final HtmlPage res;
        ScriptResult result = page.executeJavaScript(javascript);
        res = (HtmlPage) result.getNewPage();
        return res;
    }

    /**
     * Use Jsoup to read the html page. update DBC.
     *
     * @param htmlPage
     */
    public void addToDBC(final String htmlPage) {
        Document doc = Jsoup.parse(htmlPage);

        for (Element table : doc.select("table[id=ListView1_itemPlaceholderContainer]")) {
            for (Element row : table.select("tr:gt(0)")) { // skip first line
                Elements tds = row.select("td:not([rowspan])");
// 1. Create Item
                String date = tds.get(0).text(),
                        sellerName = tds.get(1).text(),
                        buyerName = tds.get(2).text(),
                        itemName = tds.get(3).text(),
                        rarity = Conf.getRarity(tds.get(3).child(0).child(0).attr("style").split(":")[1], getRarityMap()),
                        priceString = tds.get(4).text();
                long priceSold = Long.parseLong(tds.get(4).text().substring(1).replace(",", "")),
                        durability = Long.parseLong(tds.get(5).text());

// 2. Update Item in the list
                ItemDB id = new ItemDB(date, sellerName, buyerName, itemName, rarity, priceString, priceSold, durability);
                getDbc().getIdList().add(id);
                if (Conf.PRINTLOGS) {
                    id.print();
                }
            }
        }
    }

    /**
     * Search for items in the html document and add them to itemList.
     *
     * @param doc The Html document to scan for items
     */
    public void addAllItems(Document doc) {
        setItemList(new ArrayList<Item>());
        for (Element table : doc.select("table[id=ListView2_itemPlaceholderContainer]")) {
            for (Element row : table.select("tr:gt(0)")) { // skip first line
                Elements tds = row.select("td:not([rowspan])");
// 1. Create Item
                String seller = tds.get(0).text(),
                        item = tds.get(1).text(),
                        rarity = Conf.getRarity(tds.get(1).child(0).child(0).attr("style").split(":")[1], getRarityMap()),
                        timeLeft = tds.get(2).text();
                long bid = Long.parseLong(tds.get(3).text().substring(1).replace(",", "")),
                        buyout = Long.parseLong(tds.get(4).text().substring(1).replace(",", ""));
                double noobScore = computeNoobScore(bid, buyout);
// 2. Update Item in the list
                Item it = new Item(seller, item, rarity, timeLeft, bid, buyout);
                it.setNoobScore(noobScore);
                getItemList().add(it);
            }
        }
// 3. Update New Item List
        setItemListNew(DBC.getItemListNew(getItemList()));
        if (!itemListNew.isEmpty()) {
            System.out.println("addAllItems: " + getItemListNew().size() + " new items appeared !");
        }
    }

    /**
     * Compare items in itemList
     */
    public void compareDataClient() {
        setDbc(new DBC());
        getDbc().load();
        for (Item i : getItemList()) {
//            i.print();
// AVG, LAST Stuff
            getDbc().updateStats(i);
// MARKET Stuff
            long sumMarketAvgBidAll = 0, n1 = 0, n2 = 0, n3 = 0,
                    sumMarketAvgBuyoutAll = 0, sumMarketAvgBidDirect = 0,
                    sumMarketAvgBuyoutDirect = 0, sumMarketAvgBidIndirect = 0,
                    sumMarketAvgBuyoutIndirect = 0;
            for (Item i2 : getItemList()) {
                if (!i.equals(i2)) {
                    if (i.getName().equals(i2.getName())) { // All market competition
                        n1++;
                        sumMarketAvgBidAll += i2.getBid();
                        sumMarketAvgBuyoutAll += i2.getBuyout();
                        if (i.getRarity().equals(i2.getRarity())) { // Direct market competition
                            n2++;
                            sumMarketAvgBidDirect += i2.getBid();
                            sumMarketAvgBuyoutDirect += i2.getBuyout();
                        } else { // Indirect market competition
                            n3++;
                            sumMarketAvgBidIndirect += i2.getBid();
                            sumMarketAvgBuyoutIndirect += i2.getBuyout();
                        }
                    }
                }
            }
            if (n1 != 0) {
                i.setnMarketAll(n1);
                i.setMarketAvgBidAll(sumMarketAvgBidAll / n1);
                i.setMarketAvgBuyoutAll(sumMarketAvgBuyoutAll / n1);
            }
            if (n2 != 0) {
                i.setnMarketDirect(n2);
                i.setMarketAvgBidDirect(sumMarketAvgBidDirect / n2);
                i.setMarketAvgBuyoutDirect(sumMarketAvgBuyoutDirect / n2);
            }
            if (n3 != 0) {
                i.setnMarketIndirect(n3);
                i.setMarketAvgBidIndirect(sumMarketAvgBidIndirect / n3);
                i.setMarketAvgBuyoutIndirect(sumMarketAvgBuyoutIndirect / n3);
            }
        }
    }

    private double computeNoobScore(double bid, double buyout) {
        double res = 0;

        if (bid != 0) {
            res = buyout / bid;
        }
        return res;
    }

    /**
     * Print item list
     */
    void printItemList() {
        Item.printHeader();
        for (Item i : getItemList()) {
            i.print();
        }
    }

    /**
     * Update the Table with all filters.
     *
     * @param tm
     * @param j1
     * @param j2
     * @param j3
     * @param j4
     * @param j5
     * @param j6
     */
    public void updateTable(DefaultTableModel tm, JCheckBox j1, JCheckBox j2, JCheckBox j3, JCheckBox j4, JCheckBox j5, JCheckBox j6) {
// Add new lines
        int b = 0;
        for (int a = 0; a <= getItemList().size() - 1; a++) {
            Item i = getItemList().get(a);
// GENERAL Desscription
            Object[] itemObject = Conf.getItemObject(i);
            if (j1.isSelected() && i.getRarity().equals("g")) {
                tm.insertRow(b, itemObject);
                b++;
            }
            if (j2.isSelected() && i.getRarity().equals("b")) {
                tm.insertRow(b, itemObject);
                b++;
            }
            if (j3.isSelected() && i.getRarity().equals("p")) {
                tm.insertRow(b, itemObject);
                b++;
            }
            if (j4.isSelected() && i.getRarity().equals("o")) {
                tm.insertRow(b, itemObject);
                b++;
            }
            if (j5.isSelected() && i.getRarity().equals("r")) {
                tm.insertRow(b, itemObject);
                b++;
            }
            if (j6.isSelected() && i.getRarity().equals("w")) {
                tm.insertRow(b, itemObject);
                b++;
            }
        }
    }

    /**
     * TODO: REMOVE THOISOLD UNUSED METHOD Read a csvfile contaning real time
     * data
     */
    public void updateCVS() {
//        printInfos();
        updateDbClient();
        compareDataClient();
        //printItemList();
//        printTopLowBid();
    }

    /**
     * Update Item Frequency Graphic and Seller Items Graphic on row click.
     *
     * @param itemName
     * @param seller
     * @param dtm1
     * @param dtm2
     */
    public void updateTables(String itemName, String seller, DefaultTableModel dtm1, DefaultTableModel dtm2) {
//    public void updateTables(String itemName, String seller, DefaultTableModel dtm1) {
// don't use rarity here. Use rarity in GraphPanel
        setItemFrequency(new ArrayList<ItemDB>());
        setSellerItems(new ArrayList<ItemDB>());
        for (ItemDB id : getDbc().getIdList()) {
            if (itemName.equals(id.getItemName())) {  // item name
                getItemFrequency().add(id);
                dtm1.addRow(new Object[]{
                    id.getDate(),
                    id.getSeller(),
                    id.getBuyer(),
                    id.getItemName(),
                    id.getRarity(),
                    id.getPrice(),
                    id.getDurability()
                });
            }
            if (seller.equals(id.getSeller())) { // seller
                getSellerItems().add(id);
                dtm2.addRow(new Object[]{
                    id.getDate(),
                    id.getSeller(),
                    id.getBuyer(),
                    id.getItemName(),
                    id.getRarity(),
                    id.getPrice(),
                    id.getDurability()
                });
            }
        }
    }

    /**
     * Sleep a random time
     */
    void sleepRandom() {
        int sleepRand = Conf.randInt(Conf.SNIFFERMINSLEEP, Conf.SNIFFERMAXSLEEP),
                sleepTime = 1000 * sleepRand;
        System.out.println("sleepRandom: I: Sleeping " + sleepRand + "s ...");
        try {
            Thread.sleep(sleepTime);                 //1000 milliseconds is one second.
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Sleep a short random time like sleepRandom()
     */
    void sleepRandomShort() {
        int sleepTime = Conf.UPDATERSHORTSLEEPTIME;
        System.out.println("sleepRandomShort: I: Sleeping " + sleepTime + "s ...");
        try {
            Thread.sleep(sleepTime);                 //1000 milliseconds is one second.
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * TODO: REMOVE THIS UNUSED METHOD Store new items in itemList from the
     * external html file recovered with a shell curl script. Use Jsoup library
     * to parse this file.
     */
    public void updateDbClient() {
        try {
            File in = new File(Conf.PATHHTML + Conf.FILEHTML);
            Document doc = Jsoup.parse(in, null);
            addAllItems(doc);

        } catch (IOException ex) {
            Logger.getLogger(BotWeb.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Clean All DBC but not the external file.
     */
    public void cleanDBC() {
        setDbc(new DBC());
        System.out.println("cleanDBC: I: All database is now empty. The external .csv file has not changed yet.");
    }

    void printTopLowBid() {
//        Collections.sort(itemList, new ItemBuyoutComparator());
        Item.printHeader();
        for (Item i : getItemList()) {
            if (i.getNoobScore() >= 5) {
                i.print();
            }
        }
    }

    private int getDayOfMonth() {
        int res;
        Calendar cal = Calendar.getInstance();
        res = cal.get(Calendar.DAY_OF_MONTH);

        return res;
    }

    /**
     * Updates Sniffer
     */
    public void updateCVSSniffer() {
        getSn().updateCVSSniffer();
    }

    /**
     * Print starting infos like the time to run the server
     */
    void printInfos() {
        System.out.println("printInfos: I: Starting server for " + Conf.RUNTIME / 1000 + "s ...\n");
    }

    /**
     * Execute shell curl script to recover data
     */
    public void requestClient() {
        Conf.executeBlocking(Conf.CURL, Conf.PATHTOEXECUTESHELL);
    }

    /**
     * Execute shell curl script from sniffer to recover data
     */
    public void requestSniffer() {
        Conf.executeBlocking(Conf.SNIFFER, Conf.PATHTOEXECUTESHELL);
    }

    /**
     * Run Sniffer Once
     */
    public void runSniffer() {
        requestSniffer();
        updateCVSSniffer();
    }

    Map<String, String> getRarityMap() {
        return rarityMap;
    }

    public List<ItemDB> getItemFrequency() {
        return itemFrequency;
    }

    /**
     * @return the daysUpdated
     */
    public int getDaysUpdated() {
        return daysUpdated;
    }

    /**
     * @param daysUpdated the daysUpdated to set
     */
    public void setDaysUpdated(int daysUpdated) {
        this.daysUpdated = daysUpdated;
    }

    /**
     * @return the itemList
     */
    public List<Item> getItemList() {
        return itemList;
    }

    /**
     * @param itemList the itemList to set
     */
    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }

    /**
     * @return the itemListNew
     */
    public List<Item> getItemListNew() {
        return itemListNew;
    }

    /**
     * @param itemListNew the itemListNew to set
     */
    public void setItemListNew(List<Item> itemListNew) {
        this.itemListNew = itemListNew;
    }

    /**
     * @param rarityMap the rarityMap to set
     */
    public void setRarityMap(Map<String, String> rarityMap) {
        this.rarityMap = rarityMap;
    }

    /**
     * @return the dbc
     */
    public DBC getDbc() {
        return dbc;
    }

    /**
     * @param dbc the dbc to set
     */
    public void setDbc(DBC dbc) {
        this.dbc = dbc;
    }

    /**
     * @return the dbs
     */
    public DBS getDbs() {
        return dbs;
    }

    /**
     * @param dbs the dbs to set
     */
    public void setDbs(DBS dbs) {
        this.dbs = dbs;
    }

    /**
     * @param itemFrequency the itemFrequency to set
     */
    public void setItemFrequency(List<ItemDB> itemFrequency) {
        this.itemFrequency = itemFrequency;
    }

    /**
     * @return the sellerItems
     */
    public List<ItemDB> getSellerItems() {
        return sellerItems;
    }

    /**
     * @param sellerItems the sellerItems to set
     */
    public void setSellerItems(List<ItemDB> sellerItems) {
        this.sellerItems = sellerItems;
    }

    /**
     * @return the sn
     */
    public Sniffer getSn() {
        return sn;
    }

    /**
     * @param sn the sn to set
     */
    public void setSn(Sniffer sn) {
        this.sn = sn;
    }

    /**
     * @return the previousMonth
     */
    public JsExecution getPreviousMonth() {
        return previousMonth;
    }

    /**
     * @param previousMonth the previousMonth to set
     */
    public void setPreviousMonth(JsExecution previousMonth) {
        this.previousMonth = previousMonth;
    }

    /**
     * @return the currentDay
     */
    public JsExecution getCurrentDay() {
        return currentDay;
    }

    /**
     * @param currentDay the currentDay to set
     */
    public void setCurrentDay(JsExecution currentDay) {
        this.currentDay = currentDay;
    }

    /**
     * @return the jsList
     */
    public List<JsExecution> getJsList() {
        return jsList;
    }

    /**
     * @param jsList the jsList to set
     */
    public void setJsList(List<JsExecution> jsList) {
        this.jsList = jsList;
    }

    /**
     * @return the webClient
     */
    public WebClient getWebClient() {
        return webClient;
    }

    /**
     * @param webClient the webClient to set
     */
    public void setWebClient(WebClient webClient) {
        this.webClient = webClient;
    }

    /**
     * @return the daysToUpdate
     */
    public int getDaysToUpdate() {
        return daysToUpdate;
    }

    /**
     * @param daysToUpdate the daysToUpdate to set
     */
    public void setDaysToUpdate(int daysToUpdate) {
        this.daysToUpdate = daysToUpdate;
    }

    /**
     * @return the stopUpdate
     */
    public boolean isStopUpdate() {
        return stopUpdate;
    }

    /**
     * @param stopUpdate the stopUpdate to set
     */
    public void setStopUpdate(boolean stopUpdate) {
        this.stopUpdate = stopUpdate;
    }

    /**
     * @return the realTimeWebClient
     */
    public WebClient getRealTimeWebClient() {
        return realTimeWebClient;
    }

    /**
     * @param realTimeWebClient the realTimeWebClient to set
     */
    public void setRealTimeWebClient(WebClient realTimeWebClient) {
        this.realTimeWebClient = realTimeWebClient;
    }

}
