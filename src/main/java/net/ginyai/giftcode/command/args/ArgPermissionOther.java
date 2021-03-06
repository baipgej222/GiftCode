package net.ginyai.giftcode.command.args;

import net.ginyai.giftcode.GiftCodePlugin;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.*;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.annotation.NonnullByDefault;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

@NonnullByDefault
public class ArgPermissionOther extends CommandElement{
    private String permission;
    private CommandElement playerOrSource;

    public ArgPermissionOther(Text key, String permission) {
        super(key);
        this.playerOrSource = GenericArguments.playerOrSource(key);
        this.permission = permission;
    }

    @Override
    public void parse(CommandSource source, CommandArgs args, CommandContext context) throws ArgumentParseException {
        if(source.hasPermission(permission)){
            playerOrSource.parse(source,args,context);
        }else {
            super.parse(source, args, context);
        }
    }

    @Nullable
    @Override
    protected Object parseValue(CommandSource source, CommandArgs args) throws ArgumentParseException {
        if(source instanceof Player){
            return source;
        }else {
            //may not happen
            throw args.createError(GiftCodePlugin.getPlugin().getMessages().getMessage("giftcoode.args.player-only"));
        }
    }

    @Override
    public List<String> complete(CommandSource src, CommandArgs args, CommandContext context) {
        if(checkPermission(src)){
            return playerOrSource.complete(src,args,context);
        }
        return Collections.emptyList();
    }

    @Override
    public Text getUsage(CommandSource src) {
        if(checkPermission(src)){
            return playerOrSource.getUsage(src);
        }else {
            return Text.EMPTY;
        }
    }

    public boolean checkPermission(CommandSource source) {
        return source.hasPermission(permission);
    }
}
