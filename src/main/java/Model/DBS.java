package Model;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBS {
//    public List<String[]> listCSV;

    public List<ItemDB> idList;

    public DBS() {
//        listCSV = new ArrayList<>();
        idList = new ArrayList<>();
    }

    public void load() {
        CSVReader reader = null;
        try {
            String fileName = Conf.PATHDB + Conf.FILEDBCCSS;
            reader = new CSVReader(new FileReader(fileName));
            reader.readNext();// if the first line is the header
//            String[] line = reader.readNext();// iterate over reader.readNext until it returns null
            String t[];

            while ((t = reader.readNext()) != null) {
                if (t.length == Conf.CSVITEMDBLENGTH) {
                    String date = t[0], sellerName = t[1], buyerName = t[2], 
                            itemName = t[3], priceString = t[4], rarity = t[6];
                    long price = Long.parseLong(t[4].substring(1).replace("$", "").replace(",", "")),
                            durability = Long.parseLong(t[5]);
                    ItemDB id = new ItemDB(date, sellerName, buyerName, itemName, rarity, priceString, price, durability);
                    getIdList().add(id);
                } else {
                    System.out.println("DBC load: E: Invalid CSV file length. Expected "
                            + Conf.CSVITEMDBLENGTH + " but found " + t.length);
                }
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(DBC.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DBC.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ex) {
                    Logger.getLogger(DBC.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public void updateStats(Item i) {
        long nDB = 0,
                sumPrice = 0,
                sumDurability = 0,
                sellerReputation = 0,
                avgPrice;
        for (ItemDB id : getIdList()) {
            if (id.getItemName().equals(i.getName()) && id.getRarity().equals(i.getRarity())) {
                nDB++;
                sumPrice += id.getPrice();
                sumDurability += id.getDurability();
            }
            if (id.getSeller().equals(i.getSeller())) {
                sellerReputation++;
            }
        }
        if (nDB != 0) {
            avgPrice = sumPrice / nDB;
            i.setAvgPrice(avgPrice);
            i.setAvgDurability(sumDurability / nDB);
            Long bid = i.getBid(), buyout = i.getBuyout(), avgprofit = avgPrice - bid;
            i.setAvgBidProfit(avgprofit);
            if (bid != 0 && bid < avgPrice) { // ensure you make profit
                i.setPercBid(avgPrice / new Double(bid)); //1 - d / avgPrice
            }
            if (i.getBuyout() == 0) {
                i.setAvgBuyoutProfit(avgPrice - bid);
                if (bid != 0 && bid < avgPrice) { // ensure you make profit
                    i.setPercBuyout(avgPrice / new Double(bid));
                }
            } else {
                i.setAvgBuyoutProfit(avgPrice - buyout);
                if (buyout != 0 && buyout < avgPrice) { // ensure you make profit
                    i.setPercBuyout(avgPrice / new Double(buyout));
                }
            }

            i.setSellerReputation(sellerReputation);
        }
// AVG Stuff
        i.setnDB(nDB);

// LAST stuff
//        i.setLastBuyDate(lastBuyDate);
//        i.setLastSeller(lastSeller);
//        i.setLastBuyer(lastBuyer);
//        i.setLastPrice(lastPrice);
//        i.setLastDurability(lastDurability);
//        i.setLastBidProfit(lastBidProfit);
//        i.setLastBuyoutProfit(lastBuyoutProfit);
    }

    public List<ItemDB> getIdList() {
        return idList;
    }

    /**
     * @param idList the idList to set
     */
    public void setIdList(List<ItemDB> idList) {
        this.idList = idList;
    }

    public void registerToCSV() {
        try {
            int i = 0;
            String csv = Conf.PATHDB + Conf.FILEDBCCSS;
            //Create record
            try (CSVWriter writer = new CSVWriter(new FileWriter(csv))) {
                for (ItemDB id : idList) {
                    String[] record = id.toString().split("\t"); //Create record
                    writer.writeNext(record); // Write the record to file
                    i++;
                }
                writer.close(); //close the writer
                System.out.println("registerToCSV: " + i + " lines were written.");
            }
        } catch (IOException ex) {
            Logger.getLogger(DBS.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Move dbs.csv to backup folder and rename this file to the current date.
     */
    public void backupCSV() {
        File csvs = new File(Conf.PATHDB + Conf.FILEDBCCSS),
                csvsBackup = new File(Conf.PATHDB + Conf.PATHBACKUP + Conf.STRINGDBSCSV + Conf.STRINGPOINT + Conf.now() + Conf.EXTENSIONCSV);
        try {
            Files.move(csvs.toPath(), csvsBackup.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            Logger.getLogger(DBS.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
