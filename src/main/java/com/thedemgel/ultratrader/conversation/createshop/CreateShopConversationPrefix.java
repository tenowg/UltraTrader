package com.thedemgel.ultratrader.conversation.createshop;

import com.thedemgel.ultratrader.L;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationPrefix;

public class CreateShopConversationPrefix implements ConversationPrefix {

	@Override
	public String getPrefix(ConversationContext context) {
		return ChatColor.BLUE + "[" + L.getString("conversation.createshop.prefix") + "] " + ChatColor.YELLOW;
	}
}
