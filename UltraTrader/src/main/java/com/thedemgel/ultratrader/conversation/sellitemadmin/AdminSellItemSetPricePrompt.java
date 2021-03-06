package com.thedemgel.ultratrader.conversation.sellitemadmin;

import com.thedemgel.ultratrader.L;
import com.thedemgel.ultratrader.conversation.ConversationHandler;
import com.thedemgel.ultratrader.conversation.NotADoublePrompt;
import com.thedemgel.ultratrader.shop.ItemPrice;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;

import java.math.BigDecimal;

public class AdminSellItemSetPricePrompt extends StringPrompt {

	@Override
	public Prompt acceptInput(ConversationContext context, String input) {

        if (input.equals("no") || input.equals("none")) {
            input = "-1";
        }

		Double price;
		try {
			price = Double.valueOf(input);
		} catch (NumberFormatException ex) {
			context.setSessionData(ConversationHandler.CONVERSATION_SESSION_RETURN, new AdminSellItemSetPricePrompt());
			return new NotADoublePrompt();
		}
		ItemPrice item = (ItemPrice) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_ITEMPRICE);
		item.setSellPrice(BigDecimal.valueOf(price));
		return new AdminSellItemMenuPrompt();
	}

	@Override
	public String getPromptText(ConversationContext context) {
		ItemPrice item = (ItemPrice) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_ITEMPRICE);
		return L.getFormatString("conversation.itemadmin.setprice", item.getItemStack().getType().name());
	}
}
