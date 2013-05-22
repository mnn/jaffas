package codechicken.nei;

import java.io.PrintWriter;
import java.util.ArrayList;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import org.lwjgl.opengl.GL11;

import codechicken.core.config.ConfigFile;
import codechicken.core.config.ConfigTag;
import codechicken.core.inventory.ItemKey;
import codechicken.nei.forge.GuiContainerManager;

public class SubSetRangeTag extends ConfigTag
{    
    public SubSetRangeTag(DropDownFile parent, String name)
    {
        super(parent, name);
    }
    
    public SubSetRangeTag(SubSetRangeTag parent, String name)
    {
        super(parent, name);
        saveTag = parent.saveTag || ConfigFile.loading;
    }
    
    public SubSetRangeTag getTag(String tagname)
    {
        return (SubSetRangeTag) super.getTag(tagname);
    }
    
    public SubSetRangeTag getTag(String tagname, boolean createnew)
    {
        return (SubSetRangeTag) super.getTag(tagname, createnew);
    }

    public SubSetRangeTag getNewTag(String name)
    {
        return new SubSetRangeTag(this, name);
    }

    public void setDefaultValue(MultiItemRange range)
    {
        if(value == null)
        {
            setRange(range);
        }
    }
    
    public void setValue(String value)
    {
        this.value = value;
        if(saveTag)
        {
            parentfile.saveConfig();
        }
        if(validranges == null)
        {
            validranges = new MultiItemRange(value);
        }
    }
    
    public void setRange(MultiItemRange range)
    {
        validranges = range;
        setValue(validranges.toString());
    }
    
    public MultiItemRange getRange()
    {
        return validranges;
    }
    
    public boolean thisContains(int mousex, int mousey)
    {
        return mousex >= x && mousex < x + width &&
                mousey >= y && mousey < y + height;
    }

    public boolean contains(int mousex, int mousey)
    {
        if(!expanded)
        {
            return false;
        }
        
        if(thisContains(mousex, mousey))
        {
            return true;
        }
        
        for(ConfigTag tag : sortedtags)
        {
            SubSetRangeTag stag = (SubSetRangeTag)tag;
            if(stag.contains(mousex, mousey))
            {
                return true;
            }
        }
        return false;
    }

    public int getNumSlots()
    {
        return childtags.size() + (validranges == null ? 0 : validranges.getNumSlots());
    }

    public int getScrollBarWidth()
    {
        return 5;
    }

    public int getScrollBarHeight()
    {
        int sbarh = (int)((height / (float)contentheight) * height);
        if(sbarh > height)
        {
            return height;
        }
        else if(sbarh < height / 15)
        {
            return height / 15;
        }
        else
        {
            return sbarh;
        }
    }

    public int getScrolledSlots()
    {
        int slots = getNumSlots();
        int shownslots = height / slotheight;
        return (int) (percentscrolled * (slots - shownslots) + 0.5F);
    }

    private int getClickedSlot(int mousey)
    {
        return ((mousey - y) / slotheight) + getScrolledSlots();
    }
    
    public void calculatePercentScrolled()
    {
        int barempty = height - getScrollBarHeight();
        if(scrollclicky >= 0)
        {
            int scrolldiff = scrollmousey - scrollclicky;
            percentscrolled = scrolldiff / (float)barempty + scrollpercent;
        }
        if(percentscrolled < 0)
        {
            percentscrolled = 0;
        }
        if(percentscrolled > 1)
        {
            percentscrolled = 1;
        }
        int sbary = y + (int)(barempty * percentscrolled + 0.5);
        percentscrolled = (sbary - y) / (float) barempty;
    }

    public void processScrollMouse(int mousex, int mousey)
    {
        if(scrollclicky >= 0)
        {
            int scrolldiff = mousey - scrollclicky;
            int barupallowed = (int)((height - getScrollBarHeight()) * scrollpercent + 0.5);
            int bardownallowed = (height - getScrollBarHeight()) - barupallowed;
            
            if(-scrolldiff > barupallowed)
            {
                scrollmousey = scrollclicky - barupallowed;
            }
            else if(scrolldiff > bardownallowed)
            {
                scrollmousey = scrollclicky + bardownallowed;
            }
            else
            {
                scrollmousey = mousey;
            }
            
            calculatePercentScrolled();
        }
    }

    public String updateMouseOver(int mousex, int mousey, String mouseovername)
    {
        expanded = mouseovername.equals(qualifiedname) || (
                mouseovername.length() > qualifiedname.length() && //more length
                mouseovername.startsWith(qualifiedname) && //same start
                mouseovername.charAt(qualifiedname.length()) == '.');//start ends with a . so it's a full submatch
        if(!expanded)
        {
            scrollclicky = -1; //stop scrolling
            return "";
        }
        
        processScrollMouse(mousex, mousey);
        
        String currentmouseover = "";
        if(contains(mousex, mousey))
        {
            currentmouseover = qualifiedname + "." + "-";
        }
        
        int sloty = y;
        int slotx = x + (hasscroll ? getScrollBarWidth() : 0);
                        
        int slot = 0;
        for(ConfigTag tag : sortedtags)
        {
            slot++;
            if(slot <= getScrolledSlots())
            {
                continue;
            }
            
            SubSetRangeTag stag = (SubSetRangeTag) tag;
            if(slotx <= mousex && x+width > mousex && sloty <= mousey && sloty+slotheight > mousey)
            {
                currentmouseover = stag.qualifiedname;
            }
            
            String s = stag.updateMouseOver(mousex, mousey, mouseovername);
            if(!s.equals(""))
            {
                currentmouseover = s;
            }
            
            sloty+=slotheight;
        }
        
        int tagslots = slot;
        if(validranges != null)
        {
            for(ItemRange range : validranges.ranges)
            {        
                if(slot+range.encompasseditems.size() <= getScrolledSlots())
                {
                    slot += range.encompasseditems.size();
                    continue;
                }
                
                for(int item = 0; item < range.encompasseditems.size(); item++)
                {
                    slot++;    
                    if(slot <= getScrolledSlots())
                    {
                        continue;
                    }                    

                    if(slotx <= mousex && x+width > mousex && sloty <= mousey && sloty+slotheight > mousey)
                    {
                        currentmouseover = qualifiedname+"."+(slot-tagslots);
                        break;
                    }
                    
                    sloty+=slotheight;
                    if(sloty >= y + height)
                    {
                        break;
                    }
                }
                
                if(sloty >= y + height)
                {
                    break;
                }
            }
        }
        
        return currentmouseover;
    }

    public boolean click(int mousex, int mousey, int button)
    {
        if(!expanded)
        {
            return false;
        }
        
        boolean inbounds = thisContains(mousex, mousey);
        if(!inbounds)
        {
            for(ConfigTag tag : sortedtags)
            {
                SubSetRangeTag stag = (SubSetRangeTag) tag;
                stag.click(mousex, mousey, button);
            }
        }
        
        int barempty = height - getScrollBarHeight();
        int sbary = y + (int)(barempty * percentscrolled + 0.5);
        int contentx = x + getScrollBarWidth();
        
        if(hasscroll && button == 0 &&
                getScrollBarHeight() < height && //the scroll bar can move (not full length)
                mousex >= x && mousex < x + getScrollBarWidth() &&
                mousey >= y && mousey < y + height)//in the scroll pane
        {
            if(mousey < sbary)
            {
                percentscrolled = (mousey - y) / (float)barempty;
                calculatePercentScrolled();
            }
            else if(mousey > sbary + getScrollBarHeight())
            {
                percentscrolled = (mousey - y - getScrollBarHeight() + 1) / (float)barempty;
                calculatePercentScrolled();
            }
            else
            {
                scrollclicky = mousey;
                scrollpercent = percentscrolled;
                scrollmousey = mousey;
                LayoutManager.dropDown.canChangeMouseOver = false;
            }
        }
        else if(mousex >= contentx && mousex < x + width &&
                mousey >= y && mousey <= y + height)//in the box
        {
            int slot = getClickedSlot(mousey);
            if(slot == lastslotclicked && System.currentTimeMillis() - lastslotclicktime < 500 && button == 0)
                slotClicked(slot, button, true);
            else
                slotClicked(slot, button, false);
            
            if(button == 0)
            {
                lastslotclicked = slot;
                lastslotclicktime = System.currentTimeMillis();
            }
        }
        return true;
    }

    public void onMouseWheel(int i)
    {
        if(scrollclicky != -1)
        {
            return;
        }
        for(ConfigTag tag : sortedtags)
        {
            SubSetRangeTag stag = (SubSetRangeTag) tag;
            if(stag.expanded)
            {
                stag.onMouseWheel(i);
                return;
            }
        }
        percentscrolled+=i/(float)contentheight * 100;
        if(percentscrolled > 1)
        {
            percentscrolled = 1;
        }
        else if(percentscrolled < 0)
        {
            percentscrolled = 0;
        }
    }
    
    private void slotClicked(int slot, int button, boolean doubleclick)
    {
        int i = 0;
        for(ConfigTag tag : sortedtags)
        {
            if(slot == i)
            {
                SubSetRangeTag stag = (SubSetRangeTag) tag;
                stag.onClick(button, doubleclick);
                return;
            }
            i++;
        }
        if(validranges != null)
        {
            validranges.slotClicked(slot - i, button, doubleclick);
        }
    }
    
    public void onClick(int button, boolean doubleclick)
    {
        if(button == 0 && !doubleclick)//show
        {
            if(NEIClientUtils.shiftKey())
                LayoutManager.searchField.setText("@"+qualifiedname);
            else
                showAllItems();
        }
        else if(button == 0 && doubleclick)//show only
        {
            DropDownFile.dropDownInstance.hideAllItems();
            showAllItems();
        }
        else if(button == 1)//hide
        {
            hideAllItems();
        }
        DropDownFile.dropDownInstance.updateState();
        ItemList.updateSearch();
        NEIClientConfig.vishash.save();
    }
    
    public void hideAllItems()
    {
        if(validranges != null)
        {
            validranges.hideAllItems();
        }
        for(ConfigTag tag : sortedtags)
        {
            SubSetRangeTag stag = (SubSetRangeTag) tag;
            stag.hideAllItems();
        }
    }
    
    public void showAllItems()
    {
        if(validranges != null)
        {
            validranges.showAllItems();
        }
        for(ConfigTag tag : sortedtags)
        {
            SubSetRangeTag stag = (SubSetRangeTag) tag;
            stag.showAllItems();
        }
    }
    
    public void mouseUp(int mousex, int mousey, int button)
    {
        if(scrollclicky >= 0 && button == 0)//we were scrolling and we released mouse
        {
            scrollclicky = -1;
            LayoutManager.dropDown.canChangeMouseOver = true;
        }
        for(ConfigTag tag : sortedtags)
        {
            SubSetRangeTag stag = (SubSetRangeTag) tag;
            stag.mouseUp(mousex, mousey, button);
        }
    }

    public void draw(GuiContainerManager gui, int mousex, int mousey)
    {
        if(!expanded)
            return;
        
        drawScrollBar(gui);        
        
        int drawy = y;
        int drawx = x + (hasscroll ? getScrollBarWidth() : 0);
        int slot = 0;
        for(ConfigTag tag : sortedtags)
        {
            slot++;
            if(slot <= getScrolledSlots())
                continue;
            
            SubSetRangeTag stag = (SubSetRangeTag) tag;
            if(!hidden)
            {
                if(!DropDownWidget.texturedButtons)
                {
                    boolean contains = mousex >= drawx && mousex < drawx + slotwidth &&
                            mousey >= drawy && mousey < drawy + slotheight;
                    gui.drawRect(drawx, drawy, slotwidth, slotheight, contains ? 0xFF401008 : 0xFF000000);
                    gui.drawTextCentered(drawx, drawy, slotwidth, slotheight, stag.name, stag.getColourFromState(), stag.state == 0);
                }
                else
                {
                    gui.bindTexture("/gui/gui.png");
                    if(stag.state == 1)
                    {
                        GL11.glColor4f(0.65F, 0.65F, 0.65F, 1.0F);
                    }
                    else
                    {
                        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                    }
                    int tex = stag.state == 0 ? 0 : 1;
                    LayoutManager.drawButtonBackground(gui, drawx, drawy, slotwidth, slotheight, false, tex);
                    
                    int textcolour = stag.state == 2 ? 0xFFE0E0E0 : 0xFFA0A0A0;
                    gui.drawTextCentered(drawx, drawy, slotwidth, slotheight, stag.name, textcolour);
                }
            }
            
            stag.draw(gui, mousex, mousey);
            
            drawy+=slotheight;
            if(drawy >= y + height)
                break;
        }
        
        if(validranges != null && drawy < y + height)
        {
            for(ItemRange range : validranges.ranges)
            {        
                if(slot+range.encompasseditems.size() <= getScrolledSlots())
                {
                    slot += range.encompasseditems.size();
                    continue;
                }
                
                for(int item = 0; item < range.encompasseditems.size(); item++)
                {
                    slot++;    
                    if(slot <= getScrolledSlots())
                    {
                        continue;
                    }
                    
                    ItemKey itemh = range.encompasseditems.get(item);
                    int itemx = drawx + slotwidth / 2 - 8;
                    int itemy = drawy + 1;
                    boolean itemvisible = !NEIClientConfig.vishash.isItemHidden(itemh);
                    
                    if(!DropDownWidget.texturedButtons)
                    {
                        gui.drawRect(drawx, drawy, slotwidth, slotheight, itemvisible ? 0xFF003000 : 0xFF300000);
                    }
                    else
                    {                
                        int tex = itemvisible ? 1 : 0;
                        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                        LayoutManager.drawButtonBackground(gui, drawx, drawy, slotwidth, slotheight, false, tex);
                        
                    }
                    
                    ItemStack stack = itemh.item;
                    gui.drawItem(itemx, itemy, stack);
                    
                    if(itemx <= mousex && itemx + 16 > mousex && itemy + 1 <= mousey && itemy + 16 > mousey)
                        LayoutManager.dropDown.setHoverItem(itemh.item);
                    
                    drawy+=slotheight;
                    if(drawy >= y + height)
                        break;
                }
                
                if(drawy >= y + height)
                    break;
            }
        }
    }

    private void drawScrollBar(GuiContainerManager gui)
    {
        if(hasscroll && !hidden)
        {
            int sbary = y + (int)((height - getScrollBarHeight()) * percentscrolled + 0.5);

            gui.drawRect(x, y, 5, height, 0xFF202020);//background
            if(DropDownWidget.texturedButtons)
            {
                gui.drawRect(x, sbary, 5, getScrollBarHeight(), 0xFF8B8B8B);//corners
                gui.drawRect(x, sbary, 4, getScrollBarHeight() - 1, 0xFFF0F0F0);//topleft up
                gui.drawRect(x + 1, sbary + 1, 4, getScrollBarHeight() - 1, 0xFF555555);//bottom right down
                gui.drawRect(x + 1, sbary + 1, 3, getScrollBarHeight() - 2, 0xFFC6C6C6);//scrollbar
            }
            else
            {
                gui.drawRect(x, sbary, 5, getScrollBarHeight(), 0xFFE0E0E0);//scrollbar
            }
        }
    }

    public void position(int px, int py)
    {
        this.x = px;
        this.y = py;
        recalcSize();
        
        if(!expanded)
        {
            return;
        }
        
        int suby = y;
        int subx = x + width;
        int slot = 0;
        for(ConfigTag tag : sortedtags)
        {
            slot++;
            if(slot <= getScrolledSlots())
            {
                continue;
            }
            
            SubSetRangeTag stag = (SubSetRangeTag) tag;
            stag.position(subx, suby);
            
            suby+=slotheight;
            if(suby >= y + height)
            {
                break;
            }
        }
    }
    
    public void recalcSize()
    {
        int maxheight = LayoutManager.dropDown.y+LayoutManager.dropDown.maxheight+LayoutManager.dropDown.height - y;
        int extraheight = y - LayoutManager.dropDown.height;
        contentheight = getNumSlots() * slotheight;
        if(contentheight > maxheight)
        {
            if(contentheight <= maxheight + extraheight)//can fit in extra
            {
                y -= contentheight - maxheight;
                maxheight = contentheight;
            }
            else
            {
                y -= extraheight;
                maxheight += extraheight;
            }
        }
        
        if(contentheight > maxheight)
        {
            height = (maxheight / slotheight)*slotheight;
            hasscroll = true;
        }
        else
        {
            hasscroll = false;
            height = contentheight;
        }
        
        slotwidth = 0;
        FontRenderer fontRenderer = NEIClientUtils.mc().fontRenderer;
        for(ConfigTag tag : sortedtags)
        {
            int tagwidth = fontRenderer.getStringWidth(tag.name);
            if(tagwidth > slotwidth)
            {
                slotwidth = tagwidth;
            }
        }
        
        if(validranges != null)
        {
            int rangewidth = validranges.getWidth();
            if(rangewidth > slotwidth)
            {
                slotwidth = rangewidth;
            }
        }
        
        slotwidth +=2;
        width = slotwidth;
        if(hasscroll)
        {
            width+=getScrollBarWidth();
        }
        
        int totalwidth = x + width - LayoutManager.dropDown.x;
        if(expanded && totalwidth > LayoutManager.dropDown.droppedwidth)
        {
            LayoutManager.dropDown.droppedwidth = totalwidth;
        }
        
        hidden = totalwidth <= 0;
    }

    public int getWidthAtLevel(int hiddenlevel)
    {
        if(!expanded)
        {
            return 0;
        }
        
        if(hiddenlevel == 0)
        {
            return width;
        }
        
        for(ConfigTag tag : sortedtags)
        {
            SubSetRangeTag stag = (SubSetRangeTag) tag;
            int subwidth = stag.getWidthAtLevel(hiddenlevel-1);
            if(subwidth != 0)
            {
                return subwidth;
            }
        }
        return 0;
    }
    
    public void resetHashes()
    {
        if(validranges != null)
        {
            validranges.resetHashes();
        }
        sortedtags = ConfigFile.getSortedTagList(childtags, sortMode);
        for(ConfigTag tag : sortedtags)
        {
            SubSetRangeTag stag = (SubSetRangeTag)tag;
            stag.resetHashes();
        }
    }
    
    public void updateState(ItemVisibilityHash vis)
    {
        boolean allshown = false;
        boolean allhidden = false;
        for(ConfigTag tag : sortedtags)
        {
            SubSetRangeTag stag = (SubSetRangeTag)tag;
            stag.updateState(vis);
        }
        for(ConfigTag tag : sortedtags)
        {
            SubSetRangeTag stag = (SubSetRangeTag)tag;
            if(stag.state == 1)//prev mixed therefore we mixed
            {
                state = 1;
                return;
            }
            else if(stag.state == 0)//inactive
            {
                if(allshown)
                {
                    state = 1;
                    return;
                }
                allhidden = true;
            }
            else
            {
                if(allhidden)
                {
                    state = 1;
                    return;
                }
                allshown = true;
            }
        }
        if(validranges != null)
        {
            validranges.updateState(vis);
            int rstate = validranges.state;
            if(rstate == 1)//prev mixed therefore we mixed
            {
                state = 1;
                return;
            }
            else if(rstate == 0)//inactive
            {
                if(allshown)
                {
                    state = 1;
                    return;
                }
                allhidden = true;
            }
            else
            {
                if(allhidden)
                {
                    state = 1;
                    return;
                }
                allshown = true;
            }
        }
        
        if(allshown)
        {
            state = 2;
        }
        else
        {
            state = 0;
        }
    }

    public void addItemIfInRange(int item, int damage, NBTTagCompound compound)
    {
        if(validranges != null)
        {
            validranges.addItemIfInRange(item, damage, compound);
        }
        for(ConfigTag tag : sortedtags)
        {
            SubSetRangeTag stag = (SubSetRangeTag)tag;
            stag.addItemIfInRange(item, damage, compound);
        }
    }

    public boolean isItemInRange(int item, int damage)
    {
        if(validranges != null && validranges.isItemInRange(item, damage))
        {
            return true;
        }
        for(ConfigTag tag : sortedtags)
        {
            SubSetRangeTag stag = (SubSetRangeTag)tag;
            if(stag.isItemInRange(item, damage))
            {
                return true;
            }
        }
        return false;
    }
    
    public int getColourFromState()
    {
        if(state == 0)
        {
            return 0xFF601010;
        }
        else if(state == 1)
        {
            return 0xFF807070;
        }
        else
        {
            return 0xFFFFFFFF;
        }
    }
    
    public void setSave(boolean save)
    {
        saveTag = save;
        parentfile.saveConfig();
    }
    
    public void save(PrintWriter writer, int tabs, String bracequalifier)
    {
        if(saveTag)
        {
            super.save(writer, tabs, bracequalifier);
        }
        else if(childtags.size() > 0)
        {
            ConfigFile.saveTagTree(writer, childtags, tabs, bracequalifier, sortMode);
        }
    }
    
    public SubSetRangeTag useBraces()
    {
        return (SubSetRangeTag) super.useBraces();
    }
    
    public SubSetRangeTag setComment(String string)
    {
        return (SubSetRangeTag) super.setComment(string);
    }
    
    public SubSetRangeTag setPosition(int pos)
    {
        position = pos;
        if(saveTag)
        {
            parentfile.saveConfig();
        }
        return this;
    }
    
    public String toString()
    {
        return qualifiedname;
    }
    
    public ArrayList<ConfigTag> sortedtags = new ArrayList<ConfigTag>();
    public boolean saveTag = true;
    
    public MultiItemRange validranges;
    public byte state;
    public int slotwidth = 0;
    public boolean hasscroll;
    
    protected int scrollclicky = -1;
    protected float scrollpercent;
    protected int scrollmousey;
    protected float percentscrolled;
    
    protected int lastslotclicked = -1;
    protected long lastslotclicktime;
    
    int x;
    int y;
    int height;
    int width;
    int contentheight;
    public boolean expanded;
    public boolean hidden;
    
    private static final int slotheight = DropDownFile.slotheight;
}
