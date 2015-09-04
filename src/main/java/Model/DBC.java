package Model;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBC extends DBS {

    private static List<Item> itemListOld = new ArrayList<>();

//    public List<String[]> listCSV;
//    public List<ItemDB> idList;
    public DBC() {
//        listCSV = new ArrayList<>();
        idList = new ArrayList<>();
    }

    /**
     * Find New Items
     *
     * @param itemListNew
     * @return
     */
    public static List<Item> getItemListNew(List<Item> itemListNew) {
// 1. Initialise
        List<Item> res = new ArrayList<>();
        if (itemListOld.isEmpty()) {
            itemListOld.addAll(itemListNew);
        }

// 2. Compute res
        boolean found;
        if (!itemListNew.isEmpty() && !itemListOld.isEmpty()) {
            for (Item it1 : itemListNew) { // for each new Item
                found = false;
                for (Item it2 : itemListOld) { // Search in the old item list
                    if (it1.equalsWithoutTime(it2)) {
                        found = true;
                        break;
                    }
                }
                if (!found) {// A new item is found
                    res.add(it1);
                }
            }
        }

// 3. Add All
        for (Item it1 : itemListNew) {
            if (!itemListOld.contains(it1)) {
                itemListOld.add(it1);
            }
        }

        return res;
    }

    public static void setItemListOld(List<Item> itemList) {
        if (!itemList.isEmpty()) {
            itemListOld = new ArrayList<>(itemList);
        }
    }

    /**
     * Load a CSV File containing DBC.
     */
    @Override
    public void load() {
        CSVReader reader = null;
        try {
            String fileName = Conf.PATHDB + Conf.FILEDBCCSV;
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
                            + Conf.CSVITEMDBLENGTH + " comma but found " + t.length + " comma.");
                    for (String s : t) {
                        System.out.println(s + ", ");
                    }
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

    /**
     * Print all DBC Database
     */
    public void print() {
        for (ItemDB i : idList) {
            i.print();
        }
    }

    /**
     * Write dbc database to an external file. 
     * @param path
     */
    public void writeToFile(String path) {
        try {
            int n = 0;
            try (CSVWriter writer = new CSVWriter(new FileWriter(path))) {
                writer.writeNext(Conf.FIRSTLINEDBC.split(","));
                
                for (ItemDB id : idList) {
                    String[] record = id.toString().split("\t"); //Create record
                    writer.writeNext(record); // Write the record to file
                    n++;
                }
                writer.close(); //close the writer
                System.out.println("registerToCSV: I: " + n + " lines were written.");
            }
        } catch (IOException ex) {
            Logger.getLogger(DBS.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
