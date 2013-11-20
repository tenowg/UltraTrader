package com.thedemgel.ultratrader.conversation;

import com.thedemgel.ultratrader.UltraTrader;
import com.thedemgel.ultratrader.L;
import com.thedemgel.ultratrader.inventory.ShopInventoryView;
import org.bukkit.Bukkit;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.conversations.ConversationAbandonedListener;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;

public class AbandonConvo implements ConversationAbandonedListener {

	@Override
	public final void conversationAbandoned(ConversationAbandonedEvent abandonedEvent) {
		if (abandonedEvent.gracefulExit()) {
			final Player player = (Player) abandonedEvent.getContext().getForWhom();
			if (UltraTrader.getStoreHandler().getInventoryHandler().hasInventoryView(player)) {
				ShopInventoryView view = (ShopInventoryView) UltraTrader.getStoreHandler().getInventoryHandler().getInventoryView(player);
				view.getShop().save();
				if (view.isKeepAlive()) {
					view.setKeepAlive(false);

					Bukkit.getScheduler().scheduleSyncDelayedTask(UltraTrader.getInstance(), new Runnable() {
						@Override
						public void run() {
							UltraTrader.getStoreHandler().getInventoryHandler().openInventory(player);
						}
					}, UltraTrader.BUKKIT_SCHEDULER_DELAY);
				}
			}
		} else {
			final Player player = (Player) abandonedEvent.getContext().getForWhom();
			if (UltraTrader.getStoreHandler().getInventoryHandler().hasInventoryView(player)) {
				final ShopInventoryView view = (ShopInventoryView) UltraTrader.getStoreHandler().getInventoryHandler().getInventoryView(player);

				view.setKeepAlive(false);

				Bukkit.getScheduler().scheduleSyncDelayedTask(UltraTrader.getInstance(), new Runnable() {
					@Override
					public void run() {
						InventoryView playerview = player.getOpenInventory();
						if (!playerview.equals(view)) {
							UltraTrader.getStoreHandler().getInventoryHandler().removeInventoryView(player);
						}
					}
				}, UltraTrader.BUKKIT_SCHEDULER_DELAY);

			}
			abandonedEvent.getContext().getForWhom().sendRawMessage(L.getString("conversation.abandon"));
		}
	}
}
