package com.thedemgel.cititradersre.conversation.buyitemadmin;

import com.thedemgel.cititradersre.L;
import com.thedemgel.cititradersre.conversation.ConversationHandler;
import com.thedemgel.cititradersre.conversation.NotADoublePrompt;
import com.thedemgel.cititradersre.shop.ItemPrice;
import java.math.BigDecimal;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;

public class AdminBuyItemSetPricePrompt extends StringPrompt {

	@Override
	public Prompt acceptInput(ConversationContext context, String input) {

		Double price;
		try {
			price = Double.valueOf(input);
		} catch (NumberFormatException ex) {
			context.setSessionData(ConversationHandler.CONVERSATION_SESSION_RETURN, new AdminBuyItemSetPricePrompt());
			return new NotADoublePrompt();
		}
		ItemPrice item = (ItemPrice) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_ITEMPRICE);
		item.setPrice(BigDecimal.valueOf(price));
		return new AdminBuyItemMenuPrompt();
	}

	@Override
	public String getPromptText(ConversationContext context) {
		ItemPrice item = (ItemPrice) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_ITEMPRICE);
		return L.getFormatString("conversation.itemadmin.setprice", item.getItemStack().getType().name());
	}
}
