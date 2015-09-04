package Model;

import java.util.Objects;

public class ItemDB {

    public String date;
    public String seller;
    public String buyer;
    public String itemName;
    public String rarity;
    public String priceString;
    public long price;
    public long durability;

    /**
     *
     * @param da Date
     * @param s Seller
     * @param b Buyer
     * @param i Item Name
     * @param r Rarity (g, b, p, o, r, w)
     * @param ps Price, version String with '$' and ',' as separators
     * @param p Price
     * @param du Durability
     */
    public ItemDB(String da, String s, String b, String i, String r, String ps, long p, long du) {
        date = da;
        seller = s;
        buyer = b;
        itemName = i;
        rarity = r;
        priceString = ps;
        price = p;
        durability = du;
    }

    @Override
    public boolean equals(Object o) {
        boolean res = false;
        if (o == null) {
            return res;
        }
        if (!(o instanceof ItemDB)) {
            return res;
        } else {
            ItemDB i = (ItemDB) o;
            if (i.getDate().equals(date) && i.getSeller().equals(seller)
                    && i.getBuyer().equals(buyer) && i.getItemName().equals(itemName)
                    && i.getRarity().equals(rarity) && i.getPrice() == price && i.getDurability() == durability) {
                res = true;
            }
        }

        return res;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.date);
        hash = 89 * hash + Objects.hashCode(this.seller);
        hash = 89 * hash + Objects.hashCode(this.buyer);
        hash = 89 * hash + Objects.hashCode(this.itemName);
        hash = 89 * hash + Objects.hashCode(this.rarity);
        hash = 89 * hash + (int) (this.price ^ (this.price >>> 32));
        hash = 89 * hash + (int) (this.durability ^ (this.durability >>> 32));
        return hash;
    }

    @Override
    public String toString() {
        String res;
        res = date + "\t" + seller + "\t" + buyer + "\t" + itemName + "\t" + priceString + "\t" + durability + "\t" + rarity;

        return res;
    }

    public void print() {
        System.out.println(this);
    }

    /**
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * @return the seller
     */
    public String getSeller() {
        return seller;
    }

    /**
     * @param seller the seller to set
     */
    public void setSeller(String seller) {
        this.seller = seller;
    }

    /**
     * @return the buyer
     */
    public String getBuyer() {
        return buyer;
    }

    /**
     * @param buyer the buyer to set
     */
    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    /**
     * @return the itemName
     */
    public String getItemName() {
        return itemName;
    }

    /**
     * @param itemName the itemName to set
     */
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    /**
     * @return the rarity
     */
    public String getRarity() {
        return rarity;
    }

    /**
     * @param rarity the rarity to set
     */
    public void setRarity(String rarity) {
        this.rarity = rarity;
    }

    /**
     * @return the price
     */
    public long getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(long price) {
        this.price = price;
    }

    /**
     * @return the durability
     */
    public long getDurability() {
        return durability;
    }

    /**
     * @param durability the durability to set
     */
    public void setDurability(long durability) {
        this.durability = durability;
    }
}
