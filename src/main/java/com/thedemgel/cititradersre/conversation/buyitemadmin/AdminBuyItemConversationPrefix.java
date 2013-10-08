package com.thedemgel.cititradersre.conversation.buyitemadmin;

import com.thedemgel.cititradersre.conversation.sellitemadmin.*;
import com.thedemgel.cititradersre.L;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationPrefix;

public class AdminBuyItemConversationPrefix implements ConversationPrefix {

	@Override
	public String getPrefix(ConversationContext context) {
		return ChatColor.BLUE + "[" + L.getString("conversation.itemadmin.prefix") + "] " + ChatColor.YELLOW;
	}
}
