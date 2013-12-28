package com.thedemgel.ultratrader.conversation.addsellitem;

import com.thedemgel.ultratrader.L;
import com.thedemgel.ultratrader.conversation.ConversationHandler;
import com.thedemgel.ultratrader.inventory.ShopInventoryView;
import java.math.BigDecimal;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.MessagePrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.inventory.ItemStack;

public class AddItemFinishPrompt extends MessagePrompt {

	@Override
	protected Prompt getNextPrompt(ConversationContext context) {
		return Prompt.END_OF_CONVERSATION;
	}

	@Override
	public String getPromptText(ConversationContext context) {
		ShopInventoryView view = (ShopInventoryView) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_VIEW);

		String description = (String) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_DESCRIPTION);
		ItemStack item = (ItemStack) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_ITEM);
		BigDecimal price = (BigDecimal) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_PRICE);

		view.getShop().addItem(item, price, BigDecimal.ZERO, description, view.getCategory());
		view.getShop().getInventoryInterface().addInventory(item);

		view.buildCategoryItemView();

		return L.getString("conversation.additem.added");
	}
}
