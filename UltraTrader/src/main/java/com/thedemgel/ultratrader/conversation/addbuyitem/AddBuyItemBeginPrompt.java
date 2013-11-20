
package com.thedemgel.ultratrader.conversation.addbuyitem;

import com.thedemgel.ultratrader.UltraTrader;
import com.thedemgel.ultratrader.InventoryHandler;
import com.thedemgel.ultratrader.L;
import com.thedemgel.ultratrader.LimitHandler;
import com.thedemgel.ultratrader.conversation.ConversationHandler;
import com.thedemgel.ultratrader.shop.ItemPrice;
import com.thedemgel.ultratrader.inventory.ShopInventoryView;
import org.bukkit.Bukkit;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.MessagePrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class AddBuyItemBeginPrompt extends MessagePrompt {

	@Override
	protected Prompt getNextPrompt(ConversationContext context) {
		final Player player = (Player) context.getForWhom();
		ShopInventoryView view = (ShopInventoryView) UltraTrader.getStoreHandler().getInventoryHandler().getInventoryView(player);
		context.setSessionData(ConversationHandler.CONVERSATION_SESSION_VIEW, view);
		ItemStack item = (ItemStack) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_ITEM);
		ItemPrice itemprice = new ItemPrice(item);
		if (view.getShop().hasBuyItem(itemprice)) {
			return new AddBuyItemCantAddInventoryPrompt();
		}

		if (view.getShop().getBuyprices().size() >= LimitHandler.getMaxBuySellSize(view.getShop())) {
			return new AddItemShopFullPrompt();
		}

		view.setKeepAlive(true);
		Bukkit.getScheduler().scheduleSyncDelayedTask(UltraTrader.getInstance(), new Runnable() {
			@Override
			public void run() {
				player.closeInventory();
			}
		}, UltraTrader.BUKKIT_SCHEDULER_DELAY);

		return new AddBuyItemPrompt();
	}

	@Override
	public String getPromptText(ConversationContext context) {
		return L.getString("conversation.addbuyitem.begin");
	}

}
