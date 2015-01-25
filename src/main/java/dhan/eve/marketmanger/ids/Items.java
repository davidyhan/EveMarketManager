package dhan.eve.marketmanger.ids;

import java.util.HashMap;

public class Items {
    HashMap<String, Integer> itemTrade = new HashMap<String, Integer>();

    // MINERALS
    public static final int TRIT = 34;
    public static final int PYERITE = 35;
    public static final int MEXALLON = 36;
    public static final int ISOGEN = 37;
    public static final int NOCXIUM = 38;
    public static final int ZYDRINE = 39;
    public static final int MEGACYTE = 40;

    // SHIELD ITEMS
    public static final int LSE_II = 3841;
    public static final int INVULN_II = 2281;

    // RIGS
    public static final int MED_ARMOR_TRIMARK = 31055;
    public static final int MED_FIELD_EXTENDER = 31790;
    
    public Items(){
        itemTrade.put("Large Shield Extender II", LSE_II);
        itemTrade.put("Adaptive Invulnerability Field II", INVULN_II);

        itemTrade.put("Medium Trimark Armor Pump I", MED_ARMOR_TRIMARK);
        itemTrade.put("Medium Core Defense Field Extender I", MED_FIELD_EXTENDER);
        
    }

    public HashMap<String, Integer> getItemTrade() {
        return itemTrade;
    }

    public void setItemTrade(HashMap<String, Integer> itemTrade) {
        this.itemTrade = itemTrade;
    }
}
