package com.thedemgel.ultratrader.conversation.admin;

import com.thedemgel.ultratrader.UltraTrader;
import com.thedemgel.ultratrader.L;
import com.thedemgel.ultratrader.conversation.FixedIgnoreCaseSetPrompt;
import com.thedemgel.ultratrader.conversation.admin.bank.AdminBankPrompt;
import com.thedemgel.ultratrader.conversation.admin.inventoryinterface.AdminInventoryInterfacePrompt;
import com.thedemgel.ultratrader.inventory.InventoryInterface;
import com.thedemgel.ultratrader.inventory.InventoryInterfaceHandler;
import com.thedemgel.ultratrader.wallet.Wallet;
import com.thedemgel.ultratrader.wallet.WalletHandler;
import java.util.Map.Entry;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationPrefix;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

public class AdminInventoryInterfaceMenuPrompt extends FixedIgnoreCaseSetPrompt {

	private ConversationPrefix prefix;

	public AdminInventoryInterfaceMenuPrompt() {
		prefix = new AdminConversationPrefix();
	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext context, String input) {
		return getValidatedPrompt(new AdminInventoryInterfaceMenuPrompt());
	}

	@Override
	public String getPromptText(ConversationContext context) {
		Player player = (Player) context.getForWhom();
		InventoryInterfaceHandler handler = UltraTrader.getInventoryInterfaceHandler();

		for (Entry<String, Class<? extends InventoryInterface>> inventoryInterfaces : handler.getInterfaces().entrySet()) {
			try {
				if (player.hasPermission(handler.getPermission(inventoryInterfaces.getKey()))) {
					addOption(handler.getDisplayName(inventoryInterfaces.getKey()), new AdminInventoryInterfacePrompt(inventoryInterfaces.getKey()));
				}
			} catch (Exception ex) {
				continue;
			}
		}

		addOption(L.getString("conversation.admin.menu.options.exit"), new AdminMenuPrompt());

		Player p = (Player) context.getForWhom();

		p.sendRawMessage(prefix.getPrefix(context) + L.getString("conversation.admin.inventorytypemenu"));
		return L.getString("conversation.options") + ": " + this.formatFixedSet();
	}
}
