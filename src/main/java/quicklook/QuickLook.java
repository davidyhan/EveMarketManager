package quicklook;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("restriction")
@XmlRootElement(name = "quicklook")
public class QuickLook {

	Integer item;
	String itemName;
	Integer regions;
	Integer hours;
	Integer minQuantity;

	SellOrders sellOrder;
	BuyOrders buyOrder;

	public Integer getItem() {
		return item;
	}

	@XmlElement(name = "item")
	public void setItem(Integer item) {
		this.item = item;
	}

	public String getItemName() {
		return itemName;
	}

	@XmlElement(name = "itemname")
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Integer getRegions() {
		return regions;
	}

	@XmlElement(name = "regions")
	public void setRegions(Integer regions) {
		this.regions = regions;
	}

	public Integer getHours() {
		return hours;
	}

	@XmlElement(name = "hours")
	public void setHours(Integer hours) {
		this.hours = hours;
	}

	public Integer getMinQuantity() {
		return minQuantity;
	}

	@XmlElement(name = "minqty")
	public void setMinQuantity(Integer minQuantity) {
		this.minQuantity = minQuantity;
	}

	public SellOrders getSellOrder() {
		return sellOrder;
	}

	@XmlElement(name = "sell_orders")
	public void setSellOrders(SellOrders sellOrder) {
		this.sellOrder = sellOrder;
	}

	public BuyOrders getBuyOrder() {
		return buyOrder;
	}

	@XmlElement(name = "buy_orders")
	public void setBuyOrders(BuyOrders buyOrder) {
		this.buyOrder = buyOrder;
	}

	public String toString() {
		String toString = "Item Id: " + item + "\n" + 
						  "Item name: " + itemName + "\n" + 
						  sellOrder.toString() + "\n" + 
						  buyOrder.toString() + "\n";

		return toString;
	}
}
