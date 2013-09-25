package com.thedemgel.cititradersre.shop;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.thedemgel.cititradersre.CitiTrader;
import com.thedemgel.cititradersre.StoreConfig;
import com.thedemgel.cititradersre.util.ShopType;
import com.thedemgel.cititradersre.util.WalletType;
import com.thedemgel.cititradersre.wallet.AdminWallet;
import com.thedemgel.cititradersre.wallet.BankWallet;
import com.thedemgel.cititradersre.wallet.PlayerWallet;
import com.thedemgel.cititradersre.wallet.ShopWallet;
import com.thedemgel.cititradersre.wallet.Wallet;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Shop {

	private ConcurrentMap<String, ItemPrice> buyprices = new ConcurrentHashMap<>();
	private ConcurrentMap<String, ItemPrice> sellprices = new ConcurrentHashMap<>();
	private ConcurrentMap<ItemStack, Integer> inventory = new ConcurrentHashMap<>();
	private ConfigurationSection config;
	private StoreConfig shopConfig;
	private Wallet wallet;
	private BigDecimal balance;

	public Shop(ConfigurationSection section, CitiTrader instance) {
		config = section;

		shopConfig = new StoreConfig(instance, "/stores/" + config.getName() + ".yml");

		initWallet();
		loadSellPrice();
		loadInventory();

		if (sellprices.isEmpty()) {
			addSellItem(new ItemStack(Material.ANVIL, 1), 3D, 1, "Some Lore Text");
			addSellItem(new ItemStack(Material.BAKED_POTATO, 2), 3D, 2, "Some Lore Text");
			addSellItem(new ItemStack(Material.BED, 10), 3D, 3, "");
			addSellItem(new ItemStack(Material.BED_BLOCK, 4), 3D, 4, "Some Lore Text");
			addSellItem(new ItemStack(Material.BLAZE_POWDER, 8), 3D, 8, "Sizzling Hot");
			addSellItem(new ItemStack(Material.BOAT, 24), 3D, 12, "Some Lore Text");
			addSellItem(new ItemStack(Material.BOOK, 16), 3D, 16, "");
			addSellItem(new ItemStack(Material.BOOK_AND_QUILL, 30), 3D, 30, "Need to remember something?");
			addSellItem(new ItemStack(Material.BURNING_FURNACE, 32), 3D, 32, "Some Lore Text");
			addSellItem(new ItemStack(Material.CARPET, 54), 3D, 54, "Soft floor covering.");
			addSellItem(new ItemStack(Material.BUCKET, 64), 3D, 64, "Soft floor covering.");
			addSellItem(new ItemStack(Material.BONE, 128), 3D, 128, "Soft floor covering.");
			addSellItem(new ItemStack(Material.WOOD, 50, (short) 2), 4D, 1, "HARD");
			addSellItem(new ItemStack(Material.WOOD, 50, (short) 2), 4D, 1, "HARD");
		}

		saveInventory();
		saveSellPrice();
		shopConfig.saveConfig();

		setMetaData();
	}

	public ConcurrentMap<ItemStack, Integer> getInventory() {
		return inventory;
	}
	
	public boolean hasSellItem(final ItemPrice checkItem) {
		Predicate<ItemPrice> itemPredicate = new Predicate<ItemPrice>() {
			@Override
			public boolean apply(ItemPrice item) {
				return item.getItemStack().equals(checkItem.getItemStack());
			}
		};
		Collection<ItemPrice> sellItems = Collections2.filter(getSellprices().values(), itemPredicate);
		
		return sellItems.size() > 0;
	}
	
	public void addSellItem(ItemStack item, Double price, Integer quantity, String description) {
		ItemPrice invItem = new ItemPrice(item, BigDecimal.valueOf(price), quantity, description);
		
		if (hasSellItem(invItem)) {
			System.out.println("Item Found!");
			addInventory(item);
			return;
		}
		
		String random = invItem.setRandom();
		while (getSellprices().containsKey(random)) {
			random = invItem.setRandom();
		}
		getSellprices().put(random, invItem);

		addInventory(item);
	}

	public void addBuyItem(ItemStack item, Double price, Integer quantity, String description) {
		ItemPrice invItem = new ItemPrice(item, BigDecimal.valueOf(price), quantity, description);
		String random = invItem.setRandom();
		while (getBuyprices().containsKey(random)) {
			random = invItem.setRandom();
		}
		getBuyprices().put(random, invItem);
	}

	public void addInventory(ItemStack item) {
		ItemStack invItem = item.clone();
		invItem.setAmount(1);

		if (inventory.containsKey(invItem)) {
			Integer amount = inventory.get(invItem).intValue() + item.getAmount();
			inventory.put(invItem, amount);
		} else {
			inventory.put(invItem, item.getAmount());
		}
	}

	private void setMetaData() {
		// Decide whether to set metadata (not if item store) and then set it.
	}

	public String getItemId(ItemStack item) {
		String id = Iterables.getLast(item.getItemMeta().getLore());
		id = id.substring(id.length() - 8);
		return id;
	}

	public ConcurrentMap<String, ItemPrice> getBuyprices() {
		return buyprices;
	}

	public ConcurrentMap<String, ItemPrice> getSellprices() {
		return sellprices;
	}

	public final void saveSellPrice() {
		ConfigurationSection sellconfig = shopConfig.getConfig().createSection("sellprices");

		for (ItemPrice ip : sellprices.values()) {
			sellconfig.set(ip.getRandom() + ".amount", ip.getAmount());
			sellconfig.set(ip.getRandom() + ".description", ip.getDescription());
			sellconfig.set(ip.getRandom() + ".itemStack", ip.getItemStack());
			sellconfig.set(ip.getRandom() + ".price", ip.getPrice());
			sellconfig.set(ip.getRandom() + ".random", ip.getRandom());
		}
	}

	public final void loadSellPrice() {
		ConfigurationSection sellconfig = shopConfig.getConfig().getConfigurationSection("sellprices");
		if (sellconfig == null) {
			sellconfig = shopConfig.getConfig().createSection("sellprices");
		}
		for (String id : sellconfig.getKeys(false)) {
			ConfigurationSection itemconfig = sellconfig.getConfigurationSection(id);
			ItemPrice item = new ItemPrice();
			item.setAmount(itemconfig.getInt("amount"));
			item.setDescription(itemconfig.getString("description"));
			item.setItemStack(itemconfig.getItemStack("itemStack"));
			item.setPrice(BigDecimal.valueOf(itemconfig.getDouble("price")));
			item.setRandom(itemconfig.getString("random"));
			getSellprices().put(item.getRandom(), item);
		}
	}

	public final void saveInventory() {
		ConfigurationSection invconfig = shopConfig.getConfig().createSection("inventory");

		Integer count = 0;

		for (Entry<ItemStack, Integer> entry : inventory.entrySet()) {
			invconfig.set(count.toString() + ".itemstack", entry.getKey());
			invconfig.set(count.toString() + ".amount", entry.getValue());
			count++;
		}
	}

	public final void loadInventory() {
		ConfigurationSection invconfig = shopConfig.getConfig().getConfigurationSection("inventory");
		if (invconfig == null) {
			invconfig = shopConfig.getConfig().createSection("inventory");
		}
		for (String num : invconfig.getKeys(false)) {
			ItemStack item = invconfig.getItemStack(num + ".itemstack");
			Integer amount = invconfig.getInt(num + ".amount");
			getInventory().put(item, amount);
		}
	}

	public final void initWallet() {
		ConfigurationSection walletconfig = config.getConfigurationSection("wallet");
		if (walletconfig == null) {
			walletconfig = config.createSection("wallet");
		}

		String type = walletconfig.getString("type", "SHOP");
		
		switch (type) {
			case "SHOP":
				wallet = new ShopWallet(walletconfig);
				break;
			case "ADMIN":
				wallet = new AdminWallet();
				break;
			case "PLAYER":
				wallet = new PlayerWallet(walletconfig);
				break;
			case "BANK":
				wallet = new BankWallet(walletconfig);
				break;
			default:
				wallet = new ShopWallet(walletconfig);
		}
	}

	public Wallet getWallet() {
		return wallet;
	}
	
	public String getOwner() {
		return config.getString("owner", "");
	}

	public void setOwner(Player player) {
		setOwner(player.getName());
	}

	public void setOwner(String value) {
		config.set("owner", value);
	}

	public void setType(String value) {
	}

	public void setType(ShopType value) {
		config.set("type", value);
	}

	public ShopType getType() {
		return ShopType.valueOf(config.getString("type", "SERVER"));
	}

	public String getName() {
		return config.getString("name");
	}

	public Integer getId() {
		return Integer.valueOf(config.getName());
	}
}
