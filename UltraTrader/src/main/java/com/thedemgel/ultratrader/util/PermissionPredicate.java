
package com.thedemgel.ultratrader.util;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;


public class PermissionPredicate implements Predicate<PermissionAttachmentInfo> {

		private String test;

		public Collection<PermissionAttachmentInfo> getPermissions(String basePermission, Player player) {
			test = basePermission;
			Collection<PermissionAttachmentInfo> perms = Collections2.filter(player.getEffectivePermissions(), this);
			return perms;
		}

		public List<String> getPermissionValues(String basePermission, Player player) {
			Collection<PermissionAttachmentInfo> perms = getPermissions(basePermission, player);

			List<String> values = new ArrayList<>();

			for (PermissionAttachmentInfo info : perms) {
				String value = info.getPermission().replaceFirst(basePermission + ".", "");
				values.add(value);
			}

			return values;

		}

		public int getHighestPermissionSet(String basePermission, Player player) {
			test = basePermission;
			Collection<PermissionAttachmentInfo> perms = Collections2.filter(player.getEffectivePermissions(), this);

			int highest = 0;

			for (PermissionAttachmentInfo perm : perms) {
				try {
					String string = perm.getPermission().substring(perm.getPermission().lastIndexOf(".") + 1);
					int current = Integer.valueOf(string);
					if (current > highest) {
						highest = current;
					}
				} catch (Exception e) {
					continue;
				}
			}
			return highest;
		}

		@Override
		public boolean apply(PermissionAttachmentInfo input) {
			return input.getPermission().startsWith(test) && input.getValue() == true;
		}
}
