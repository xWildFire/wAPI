package pl.wildfire.api;

import com.google.common.collect.Lists;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;

public class Parser {
	private static HashMap<String, ItemStack> items = new HashMap<>();

	static{
		items.put("ENCHANTEDGOLDENAPPLE", new ItemStack(Material.GOLDEN_APPLE, 1, (short)1));
		items.put("PISTON", new ItemStack(Material.PISTON_BASE));
		for(Material m : Material.values()){
			if(m.name().contains("_")) {
                items.put(m.name().replace("_", ""), new ItemStack(m));
                if(m.name().contains("_ON"))
                    items.put(m.name().replace("_ON", "").replace("_", ""), new ItemStack(m));
            }
		}
	}

	public static List<ItemStack> parseItemList(List<String> s){
		List<ItemStack> list = new ArrayList<>();
		for(String g : s){
			list.add(parseItem(g));
		}
		return list;
	}

	public static ItemStack parseItem(String s) {
		if(s == null)
			return null;
        ItemStack it = parseMaterialStack((s.contains(" ") ? s.split(" ")[0] : s).toUpperCase());

		if(it == null)
            return null;

        if(s.contains(" ")) {
            String[] spl = s.split(" ");
            ItemMeta im = it.getItemMeta();
            for (String meta : Arrays.copyOfRange(spl, 1, spl.length)) {
                String key = meta;
                String value = null;
                if(meta.contains(":")){
                    key = meta.split(":")[0];
                    value = meta.split(":")[1];
                }

                if(NumberUtil.isInt(key)) {
                    int amount = NumberUtil.getInt(key);
                    if(amount <= 0)
                        amount = 1;
					else if(amount > it.getMaxStackSize())
						amount = it.getMaxStackSize();
                    it.setAmount(amount);
                }else if(Enchantments.getByName(key) != null){
                    Enchantment enchant = Enchantments.getByName(key);
					int lvl = enchant.getMaxLevel();
					if(value != null && NumberUtil.isInt(value) && NumberUtil.getInt(value) < enchant.getMaxLevel() && NumberUtil.getInt(value) > 0)
						lvl = NumberUtil.getInt(value);

					if(im instanceof EnchantmentStorageMeta) {
						EnchantmentStorageMeta em = (EnchantmentStorageMeta) it.getItemMeta();
						if(!em.hasConflictingEnchant(enchant))
							em.addStoredEnchant(enchant, lvl, true);
					}else if(enchant.canEnchantItem(it)) {
						im.addEnchant(enchant, lvl, true);
					}
				}else if(value != null){
                    if(key.equalsIgnoreCase("name")) {
                        im.setDisplayName(Msg.c(value.replace("_", " ")));
                    }else if(key.equalsIgnoreCase("owner") && im instanceof SkullMeta){
                        ((SkullMeta)im).setOwner(value);
                    }else if(key.equalsIgnoreCase("lore")) {
						List<String> lore = Lists.newArrayList();
						if (value.contains("/n")) {
							for (String t : value.split("/n")) {
								lore.add(Msg.c(t.replace("_", " ")));
							}
						} else {
							lore.add(Msg.c(value.replace("_", " ")));
						}
						im.setLore(lore);
                    }else if(im instanceof BookMeta){
						BookMeta bm = (BookMeta)it.getItemMeta();
						switch(key){
							case "author":
								bm.setAuthor(Msg.c(value.replace("_", " ")));
							case "title":
								bm.setTitle(Msg.c(value.replace("_", " ")));
							case "pages":
								List<String> pages = Lists.newArrayList();
								if(value.contains("/n")) {
									for (String t : value.split("/n")) {
										pages.add(Msg.c(t.replace("_", " ")));
									}
								}else{
									pages.add(Msg.c(value.replace("_", " ")));
								}
								bm.setPages(pages);
						}
					}
                }
            }
            it.setItemMeta(im);
        }
		return it;
	}
	
	public static Material parseMaterial(String m){
        if(m == null)
			return null;
		m = m.toUpperCase().replace(" ", "_");
		return NumberUtil.isInt(m) ? Material.getMaterial(NumberUtil.getInt(m)) : Material.getMaterial(m);
	}

	public static ItemStack parseMaterialStack(String type){
		if(type == null)
			return null;
		ItemStack it = null;
		String material = type;
		short data = 0;
		if(type.contains(":")){
			material = type.split(":")[0];
			data = Short.parseShort(type.split(":")[1]);
		}
		if(items.containsKey(material)){
			it = items.get(material).clone();
		}else if(parseMaterial(material) != null) {
			it = new ItemStack(parseMaterial(material), 1, data);
		}
		return it;
	}

	public static List<Material> parseMaterials(List<String> string){
		List<Material> materials = new ArrayList<>();
		for(String s : string)
			materials.add(parseMaterial(s));
		return materials;
	}
	
	public static Location parseLocation(String string){
		if(string == null)
			return null;
		String[] shs = string.split(",");
		if(shs.length >= 6) {
			World world = Bukkit.getWorld(shs[0]);
			if(world == null)
				world = Bukkit.getWorlds().get(0);
			return new Location(world, NumberUtil.getDouble(shs[1]), NumberUtil.getDouble(shs[2]), NumberUtil.getDouble(shs[3]), Float.parseFloat(shs[4]), Float.parseFloat(shs[5]));
		}else if(shs.length >= 4){
			World world = Bukkit.getWorld(shs[0]);
			if(world == null)
				world = Bukkit.getWorlds().get(0);
			return new Location(world, NumberUtil.getDouble(shs[1]), NumberUtil.getDouble(shs[2]), NumberUtil.getDouble(shs[3]));
		}
		return null;
	}

	public static long parseTime(String string){
		if(string == null || string.isEmpty()) return 0;
		
		Stack<Character> type = new Stack<>();
		StringBuilder value = new StringBuilder();
		
		boolean calc = false;
		long time = 0;
		
		for(char c : string.toCharArray()){
			switch(c){
			case 'd':
			case 'h':
			case 'm':
			case 's':
				if(!calc){
					type.push(c);
					calc = true;
				}
				if(calc){
					try {
						long i = Integer.valueOf(value.toString());
						switch(type.pop()){
						case 'd': time += i*86400000L; break;
						case 'h': time += i*3600000L; break;
						case 'm': time += i*60000L; break;
						case 's': time += i*1000L; break;
						}
					} catch (NumberFormatException e){
						return time;
					}
				}
				type.push(c);
				calc = true;
				break;
			default:
				value.append(c);
				break;
			}
		}
		return time;
	}

	public static String toString(Inventory inv){
		String s = "";
		for(ItemStack it : inv.getContents()){
			s += toString(it)+"$";
		}
		if(s.length() > 0)
			s = s.substring(0, s.length()-1);
		return s;
	}

	public static Inventory parseInventory(String s){
		Inventory inv = Bukkit.createInventory(null, 45);
		for(String it : s.split("$")){
			ItemStack i = parseItem(it);
			inv.addItem(i == null ? new ItemStack(Material.AIR) : i);
		}
		return inv;
	}

	public static String toString(ItemStack it){
		if(it == null) return null;
		StringBuilder sb = new StringBuilder();
		sb.append(it.getType().name().toLowerCase()+(it.getData().getData() > 0 ? ":"+it.getData().getData() : ""));
		sb.append(" ");
		sb.append(it.getAmount());
		if(it.hasItemMeta()){
			ItemMeta im = it.getItemMeta();
			if(im.hasDisplayName()){
				sb.append(" ");
				sb.append("name:"+im.getDisplayName().replace(" ", "_"));
			}if(im.getLore() != null && im.getLore().size() > 0) {
				sb.append(" ");
				sb.append("lore:" + String.join("/n", im.getLore()).replace(" ", "_"));
			}if(im instanceof BookMeta){
				BookMeta bm = (BookMeta)im;
				sb.append(" ");
				sb.append("pages:" + String.join("/n", bm.getPages()).replace(" ", "_"));
				sb.append(" ");
				sb.append("author:" + bm.getAuthor().replace(" ", "_"));
				sb.append(" ");
				sb.append("title:" + bm.getTitle().replace(" ", "_"));
			}if(im instanceof SkullMeta) {
				sb.append(" ");
				sb.append("owner:"+((SkullMeta) im).getOwner());
			}
			for(Enchantment ench : im.getEnchants().keySet()){
				sb.append(" ");
				sb.append(ench.getName()+":"+im.getEnchantLevel(ench));
			}
		}
		return sb.toString();
	}
	
	public static String toString(Location loc){
		if(loc == null) return null;
		StringBuilder sb = new StringBuilder();
		sb.append(loc.getWorld().getName());
		sb.append(",");
		sb.append(loc.getX());
		sb.append(",");
		sb.append(loc.getY());
		sb.append(",");
		sb.append(loc.getZ());
        sb.append(",");
        sb.append(loc.getYaw());
        sb.append(",");
        sb.append(loc.getPitch());
		return sb.toString();
	}
}
