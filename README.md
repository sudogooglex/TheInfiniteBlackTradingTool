------------------ Informations ------------------
The main goal of this tool is to earn as much money as possible by tradding items.
This trading tool will analyse The Infinite Black market in real time.
Availlable features :
- Market History Analytics
- MarketPlace real time Analytics
- Display
- Update

https://drive.google.com/file/d/0B08OYS7z8Q8JQXJOWF8taGllVzQ/view?usp=sharing


------------------ NEXT ------------------
* Market History Analytics
- BUG MAJOR : AvgBidProfit must use the green graph and not the blue !
- Remove DBS and use the hashmap of DBC instead.
Add a column AvgPriceDurability with the avg price of items adjusted with durabilities. AvgPriceDurability = (it1*dur1 + it2*dur2 + ... + itN*urN) / N
-- Use spaces when there are big numbers in the y axis (as a separator to read easily)

* MarketPlace real time Analytics

* Display
When an update is done, put in red increased bid
Print the total number of items
Normalizing seller reputation: Display max seller reputation or a new column seller reputation percentage
-- Resolve vertical scroll bug

* Update
Update Next elements for each days with htmlUnit


------------------ v0.6 (00.00.2015) ------------------
* Market History Analytics

* MarketPlace real time Analytics

* Display

* Update


------------------ v0.5 (20.08.2015) ------------------
* Market History Analytics
- Using htmlUnit and jsoup to recover all items on the official web site.
- Possibility to choose how many months you want to update.
- AntiblackList module : sleep random  time before executing javascript.

* MarketPlace real time Analytics

* Display
- Swing EDT compatible : it is now possible to activate the auto update mode.
- Add a progress bar to see where is the database progress.
- Activating filters : rarity.

* Update
- Auto Update enabled to refresh items of the day.
- Full update of all last days.
- All updates are 100% multithreaded.
- No reboot required after updates are done.


------------------ v0.4 (11.08.2015) ------------------
* Market History Analytics
- Analyse the seller : List of all items he bought in a table next to the last bought.

* MarketPlace real time Analytics
- Added filter for the rarity variable.

* Display
- Put Setting tab at the end.
- Put prices in K instead in graphics.

* Update
- AutoSelect First Row on update


------------------ v0.3 (02.08.2015) ------------------
* Market History Analytics
- Abandonned: Sniffer. Reason : History archive gives only data for a fix day. Can't get last day with a curl script.
- Abandonned: Filter items by rarity. Reason : AvgBidProfit sorter is enough.
- Display a line for each rarity + a line for all rarity
- Use the durability variable in graphs : pondarated price = price * durability / 100.
- Display the item frequency graph

* MarketPlace real time Analytics

* Display
- pBid, pBuyout : if pBid = 2, when u invest 33 $, it 'll bring you (33 * 2) $. (Pourcentage de profit sur investissement)
- Enable Sort in the database view
- Change main icon
- Change titles names and column size

* Update
- Abandonned: Create an automatize update button. Reason : Sniffer Abandonned.
- Update : Was not working when some items are selected




------------------ v0.2 (25.07.2015) ------------------
* Market History Analytics
- Create a table of all details concerning previous items sold

* MarketPlace real time Analytics

* Display
- Change the position of buttons

* Update
- Resolve bug of duplicated items when CSV Update.
- Main Update Button. Wait for update downloaded then change table data.





------------------ v0.1 (12.07.2015) ------------------

* Market History Analytics
- Uses a database of all items sold last month on the grey server only
- Preview the Price history based on this database
- Print the average profit based on this database
- Print the number of times an item have been sold based on this database
- Print average item durability

* MarketPlace real time Analytics
- Print the reputation of the item's seller : the higher it it, the more likely he has sold items. (and it's not interesting to buy his item)
- Print Market competition variables : the number of times the item is in the marketplace, the number of items with a different rarity.
- Print the "noobScore" of the seller : noobScore = bid/buyout. If very high, the seller want absolutly to sell his item and prices are more likely to be interresting.

* Display
- In the table : print in first all interesting items to buys
- Print a table with all statistics
- On item click : view price history based on the last month item database

* Update
- Click to update CSV to make a local update
- Click on Update Remote to download the last real time Infinite black marketplace on the green Server.




------------------ French All ------------------


1) Automatiser la recherche de prix anormalements bas pour acheter.
	Comparer tous les bid/buyout items du moment avec ceux de la base de donnée pour une durabilité équivalante.
	Afficher les plus intéressants du moment.
	Possibilité d'afficher l'historique des prix en cliquant sur les intéressants du moment.
2) Analyse en temps réel.
	Sourligner les items une bid anormalemnt faible par rapport au buyout pour une durabilité équivalante. (le vendeur veut s'en débarrasser à tout prix)
	Items avec une bid/buyout en dessous de la même catégorie et de la même rareté pour une durabilité équivalante. (le vendeur ne connait pas le vrai prix)
3) Créer un programme qui actualise la base de donnée des prix