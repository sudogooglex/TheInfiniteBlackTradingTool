package Model;

//import lombok.Getter;
//import lombok.Setter;
//
//@Setter
//@Getter
public class Item {

    public String seller;
    public String name;
    public String rarity;
    public String timeLeft;
    public String lastBuyDate;
    public String lastSeller;
    public String lastBuyer;
    public Double noobScore;
    public Double percBid;
    public Double percBuyout;
    public Long bid;
    public Long buyout;
    public Long avgPrice;
    public Long nDB;
    public Long avgDurability;
    public Long avgBidProfit;
    public Long avgBuyoutProfit;
    public Long lastPrice;
    public Long lastBidProfit;
    public Long lastBuyoutProfit;
    public Long lastDurability;
    public Long competition;
    public Long nMarketAll;
    public Long nMarketDirect;
    public Long nMarketIndirect;
    public Long marketAvgBidAll;
    public Long marketAvgBuyoutAll;
    public Long marketAvgBidDirect;
    public Long marketAvgBuyoutDirect;
    public Long marketAvgBidIndirect;
    public Long marketAvgBuyoutIndirect;
    public Long sellerReputation;

    /**
     *
     * @param se Seller Name
     * @param it Item Name
     * @param ra Rarity (g, b, p, o, r, w)
     * @param ti Time Left
     * @param bi Bid
     * @param bu Buyout
     */
    public Item(String se, String it, String ra, String ti, Long bi, Long bu) {
// Investment Stuff
        percBid = new Double(0);
        percBuyout = new Double(0);
        noobScore = new Double(0);

// GENERAL Desscription
        seller = se;
        name = it;
        rarity = ra;
        timeLeft = ti;
        bid = bi;
        buyout = bu;

// AVG Stuff
        avgPrice = new Long(0);
        nDB = new Long(0);
        avgDurability = new Long(0);
        avgBidProfit = new Long(0);
        avgBuyoutProfit = new Long(0);
// LAST Stuff
        lastBuyDate = "E";
        lastSeller = "E";
        lastBuyer = "E";
        lastPrice = new Long(0);
        lastBidProfit = new Long(0);
        lastBuyoutProfit = new Long(0);
        lastDurability = new Long(0);
// MARKET Stuff
        nMarketAll = new Long(0);
        nMarketDirect = new Long(0);
        nMarketIndirect = new Long(0);
        marketAvgBidAll = new Long(0);
        marketAvgBuyoutAll = new Long(0);
        marketAvgBidDirect = new Long(0);
        marketAvgBuyoutDirect = new Long(0);
        marketAvgBidIndirect = new Long(0);
        marketAvgBuyoutIndirect = new Long(0);
    }

    /**
     * Print Table Header
     */
    public static void printHeader() {
        System.out.println("\n"
                + "NoobScore\tRarity\tName\tTime\tPlayer\tBid\tBuyout\t\t"
                + "AvgPrice\tnDB\tAvgDurability\tAvgBidProfit\tAvgBuyoutProfit\t\t"
                + "LastBuyDate\tLastSeller\tLastBuyer\tLastPrice\tLastDurability\tLastBidProfit\tLastBuyoutProfit\t"
                + "nMarketNameAll\tmarketAvgBidAll\tmarketAvgBuyoutAll\t"
                + "nMarketDirect\tmarketAvgBidDirect\tmarketAvgBuyoutDirect\t"
                + "nMarketIndirect\tmarketAvgBidIndirect\tmarketAvgBuyoutIndirect\n"
        );
    }

    /**
     * Print Item
     */
    public void print() {
        System.out.println(getNoobScore() + "\t"
                + getRarity() + "\t"
                + getName() + "\t"
                + getTimeLeft() + "\t"
                + getSeller() + "\t"
                + getBid() + "\t"
                + getBuyout() + "\t"
                // AVG Stuff
                + getAvgPrice() + "\t"
                + getnDB() + "\t"
                + getAvgDurability() + "\t"
                + getAvgBidProfit() + "\t"
                + getAvgBuyoutProfit() + "\t"
                // MARKET Stuff
                + getnMarketAll() + "\t" // number of items in the market with the same Name
                + getMarketAvgBidAll() + "\t"
                + getMarketAvgBuyoutAll() + "\t"
                + getnMarketDirect() + "\t" // number of items in the market with the same Rarity and Name
                + getMarketAvgBidDirect() + "\t"
                + getMarketAvgBuyoutDirect() + "\t"
                + getnMarketIndirect() + "\t" // number of items in the market with the same Rarity only
                + getMarketAvgBidIndirect() + "\t"
                + getMarketAvgBuyoutIndirect() + "\t"
                // LAST Stuff
                + getLastBuyDate() + "\t"
                + getLastSeller() + "\t"
                + getLastBuyer() + "\t"
                + getLastPrice() + "\t"
                + getLastDurability() + "\t"
                + getLastBidProfit() + "\t"
                + getLastBuyoutProfit() + "\t"
        );
    }
//
//    public void setAverageBid(long abi) {
//        averageBid = abi;
//    }
//
//    public void setAverageBuyout(long abu) {
//        averageBuyout = abu;
//    }
//
//    public long getBuyoutPercentage() {
//        return buyoutPercentage;
//    }

    public boolean equals(Item i) {
        boolean res = false;
        if (i.getSeller().equals(getSeller()) && i.getName().equals(getName())
                && i.getTimeLeft().equals(getTimeLeft()) && i.getBid().equals(getBid())
                && i.getBuyout().equals(getBuyout()) && i.getRarity().equals(getRarity())) {
            res = true;
        }
        return res;
    }

    boolean equalsWithoutTime(Item i) {
        boolean res = false;
        if (i.getSeller().equals(getSeller()) && i.getName().equals(getName())
                && i.getBid().equals(getBid()) && i.getBuyout().equals(getBuyout())
                && i.getRarity().equals(getRarity())) {
            res = true;
        }
        return res;
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
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
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
     * @return the timeLeft
     */
    public String getTimeLeft() {
        return timeLeft;
    }

    /**
     * @param timeLeft the timeLeft to set
     */
    public void setTimeLeft(String timeLeft) {
        this.timeLeft = timeLeft;
    }

    /**
     * @return the lastBuyDate
     */
    public String getLastBuyDate() {
        return lastBuyDate;
    }

    /**
     * @param lastBuyDate the lastBuyDate to set
     */
    public void setLastBuyDate(String lastBuyDate) {
        this.lastBuyDate = lastBuyDate;
    }

    /**
     * @return the lastSeller
     */
    public String getLastSeller() {
        return lastSeller;
    }

    /**
     * @param lastSeller the lastSeller to set
     */
    public void setLastSeller(String lastSeller) {
        this.lastSeller = lastSeller;
    }

    /**
     * @return the lastBuyer
     */
    public String getLastBuyer() {
        return lastBuyer;
    }

    /**
     * @param lastBuyer the lastBuyer to set
     */
    public void setLastBuyer(String lastBuyer) {
        this.lastBuyer = lastBuyer;
    }

    /**
     * @return the noobScore
     */
    public Double getNoobScore() {
        return noobScore;
    }

    /**
     * @param noobScore the noobScore to set
     */
    public void setNoobScore(Double noobScore) {
        this.noobScore = noobScore;
    }

    /**
     * @return the percBid
     */
    public Double getPercBid() {
        return percBid;
    }

    /**
     * @param percBid the percBid to set
     */
    public void setPercBid(Double percBid) {
        this.percBid = percBid;
    }

    /**
     * @return the percBuyout
     */
    public Double getPercBuyout() {
        return percBuyout;
    }

    /**
     * @param percBuyout the percBuyout to set
     */
    public void setPercBuyout(Double percBuyout) {
        this.percBuyout = percBuyout;
    }

    /**
     * @return the bid
     */
    public Long getBid() {
        return bid;
    }

    /**
     * @param bid the bid to set
     */
    public void setBid(Long bid) {
        this.bid = bid;
    }

    /**
     * @return the buyout
     */
    public Long getBuyout() {
        return buyout;
    }

    /**
     * @param buyout the buyout to set
     */
    public void setBuyout(Long buyout) {
        this.buyout = buyout;
    }

    /**
     * @return the avgPrice
     */
    public Long getAvgPrice() {
        return avgPrice;
    }

    /**
     * @param avgPrice the avgPrice to set
     */
    public void setAvgPrice(Long avgPrice) {
        this.avgPrice = avgPrice;
    }

    /**
     * @return the sellFrequency
     */
    public Long getnDB() {
        return nDB;
    }

    /**
     * @param nDB the nDB to set
     */
    public void setnDB(Long nDB) {
        this.nDB = nDB;
    }

    /**
     * @return the avgDurability
     */
    public Long getAvgDurability() {
        return avgDurability;
    }

    /**
     * @param avgDurability the avgDurability to set
     */
    public void setAvgDurability(Long avgDurability) {
        this.avgDurability = avgDurability;
    }

    /**
     * @return the avgBidProfit
     */
    public Long getAvgBidProfit() {
        return avgBidProfit;
    }

    /**
     * @param avgBidProfit the avgBidProfit to set
     */
    public void setAvgBidProfit(Long avgBidProfit) {
        this.avgBidProfit = avgBidProfit;
    }

    /**
     * @return the avgBuyoutProfit
     */
    public Long getAvgBuyoutProfit() {
        return avgBuyoutProfit;
    }

    /**
     * @param avgBuyoutProfit the avgBuyoutProfit to set
     */
    public void setAvgBuyoutProfit(Long avgBuyoutProfit) {
        this.avgBuyoutProfit = avgBuyoutProfit;
    }

    /**
     * @return the lastPrice
     */
    public Long getLastPrice() {
        return lastPrice;
    }

    /**
     * @param lastPrice the lastPrice to set
     */
    public void setLastPrice(Long lastPrice) {
        this.lastPrice = lastPrice;
    }

    /**
     * @return the lastBidProfit
     */
    public Long getLastBidProfit() {
        return lastBidProfit;
    }

    /**
     * @param lastBidProfit the lastBidProfit to set
     */
    public void setLastBidProfit(Long lastBidProfit) {
        this.lastBidProfit = lastBidProfit;
    }

    /**
     * @return the lastBuyoutProfit
     */
    public Long getLastBuyoutProfit() {
        return lastBuyoutProfit;
    }

    /**
     * @param lastBuyoutProfit the lastBuyoutProfit to set
     */
    public void setLastBuyoutProfit(Long lastBuyoutProfit) {
        this.lastBuyoutProfit = lastBuyoutProfit;
    }

    /**
     * @return the lastDurability
     */
    public Long getLastDurability() {
        return lastDurability;
    }

    /**
     * @param lastDurability the lastDurability to set
     */
    public void setLastDurability(Long lastDurability) {
        this.lastDurability = lastDurability;
    }

    /**
     * @return the competition
     */
    public Long getCompetition() {
        return competition;
    }

    /**
     * @param competition the competition to set
     */
    public void setCompetition(Long competition) {
        this.competition = competition;
    }

    /**
     * @return the nMarketAll
     */
    public Long getnMarketAll() {
        return nMarketAll;
    }

    /**
     * @param nMarketAll the nMarketAll to set
     */
    public void setnMarketAll(Long nMarketAll) {
        this.nMarketAll = nMarketAll;
    }

    /**
     * @return the nMarketDirect
     */
    public Long getnMarketDirect() {
        return nMarketDirect;
    }

    /**
     * @param nMarketDirect the nMarketDirect to set
     */
    public void setnMarketDirect(Long nMarketDirect) {
        this.nMarketDirect = nMarketDirect;
    }

    /**
     * @return the nMarketIndirect
     */
    public Long getnMarketIndirect() {
        return nMarketIndirect;
    }

    /**
     * @param nMarketIndirect the nMarketIndirect to set
     */
    public void setnMarketIndirect(Long nMarketIndirect) {
        this.nMarketIndirect = nMarketIndirect;
    }

    /**
     * @return the marketAvgBidAll
     */
    public Long getMarketAvgBidAll() {
        return marketAvgBidAll;
    }

    /**
     * @param marketAvgBidAll the marketAvgBidAll to set
     */
    public void setMarketAvgBidAll(Long marketAvgBidAll) {
        this.marketAvgBidAll = marketAvgBidAll;
    }

    /**
     * @return the marketAvgBuyoutAll
     */
    public Long getMarketAvgBuyoutAll() {
        return marketAvgBuyoutAll;
    }

    /**
     * @param marketAvgBuyoutAll the marketAvgBuyoutAll to set
     */
    public void setMarketAvgBuyoutAll(Long marketAvgBuyoutAll) {
        this.marketAvgBuyoutAll = marketAvgBuyoutAll;
    }

    /**
     * @return the marketAvgBidDirect
     */
    public Long getMarketAvgBidDirect() {
        return marketAvgBidDirect;
    }

    /**
     * @param marketAvgBidDirect the marketAvgBidDirect to set
     */
    public void setMarketAvgBidDirect(Long marketAvgBidDirect) {
        this.marketAvgBidDirect = marketAvgBidDirect;
    }

    /**
     * @return the marketAvgBuyoutDirect
     */
    public Long getMarketAvgBuyoutDirect() {
        return marketAvgBuyoutDirect;
    }

    /**
     * @param marketAvgBuyoutDirect the marketAvgBuyoutDirect to set
     */
    public void setMarketAvgBuyoutDirect(Long marketAvgBuyoutDirect) {
        this.marketAvgBuyoutDirect = marketAvgBuyoutDirect;
    }

    /**
     * @return the marketAvgBidIndirect
     */
    public Long getMarketAvgBidIndirect() {
        return marketAvgBidIndirect;
    }

    /**
     * @param marketAvgBidIndirect the marketAvgBidIndirect to set
     */
    public void setMarketAvgBidIndirect(Long marketAvgBidIndirect) {
        this.marketAvgBidIndirect = marketAvgBidIndirect;
    }

    /**
     * @return the marketAvgBuyoutIndirect
     */
    public Long getMarketAvgBuyoutIndirect() {
        return marketAvgBuyoutIndirect;
    }

    /**
     * @param marketAvgBuyoutIndirect the marketAvgBuyoutIndirect to set
     */
    public void setMarketAvgBuyoutIndirect(Long marketAvgBuyoutIndirect) {
        this.marketAvgBuyoutIndirect = marketAvgBuyoutIndirect;
    }

    /**
     * @return the sellerReputation
     */
    public Long getSellerReputation() {
        return sellerReputation;
    }

    /**
     * @param sellerReputation the sellerReputation to set
     */
    public void setSellerReputation(Long sellerReputation) {
        this.sellerReputation = sellerReputation;
    }

}
