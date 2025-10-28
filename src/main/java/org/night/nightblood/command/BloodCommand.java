package org.night.nightblood.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.night.nightblood.blood.BloodData;
import org.night.nightblood.network.BloodSyncPacket;
import org.night.nightblood.registry.ModAttachments;
import net.neoforged.neoforge.network.PacketDistributor;

public class BloodCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
            Commands.literal("blood")
                .requires(source -> source.hasPermission(4))
                .then(Commands.literal("add")
                    .then(Commands.argument("player", EntityArgument.player())
                        .then(Commands.argument("amount", IntegerArgumentType.integer(1, 100))
                            .executes(BloodCommand::addBlood)
                        )
                    )
                )
                .then(Commands.literal("remove")
                    .then(Commands.argument("player", EntityArgument.player())
                        .then(Commands.argument("amount", IntegerArgumentType.integer(1, 100))
                            .executes(BloodCommand::removeBlood)
                        )
                    )
                )
                .then(Commands.literal("set")
                    .then(Commands.argument("player", EntityArgument.player())
                        .then(Commands.argument("amount", IntegerArgumentType.integer(0, 100))
                            .executes(BloodCommand::setBlood)
                        )
                    )
                )
                .then(Commands.literal("get")
                    .then(Commands.argument("player", EntityArgument.player())
                        .executes(BloodCommand::getBlood)
                    )
                )
        );
    }

    private static int addBlood(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer target = EntityArgument.getPlayer(context, "player");
        int amount = IntegerArgumentType.getInteger(context, "amount");

        BloodData bloodData = target.getData(ModAttachments.BLOOD_DATA);
        int oldBlood = bloodData.getBloodLevel();
        bloodData.addBlood(amount);
        int newBlood = bloodData.getBloodLevel();

        PacketDistributor.sendToPlayer(target, new BloodSyncPacket(newBlood));

        context.getSource().sendSuccess(
            () -> Component.literal("§aAdded §c" + amount + " blood §ato §e" + target.getName().getString() +
                " §7(" + oldBlood + " → " + newBlood + ")"),
            true
        );

        target.sendSystemMessage(
            Component.literal("§aYou received §c+" + amount + " blood §7(" + oldBlood + " → " + newBlood + ")")
        );

        return newBlood;
    }

    private static int removeBlood(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer target = EntityArgument.getPlayer(context, "player");
        int amount = IntegerArgumentType.getInteger(context, "amount");

        BloodData bloodData = target.getData(ModAttachments.BLOOD_DATA);
        int oldBlood = bloodData.getBloodLevel();
        bloodData.removeBlood(amount);
        int newBlood = bloodData.getBloodLevel();

        PacketDistributor.sendToPlayer(target, new BloodSyncPacket(newBlood));

        context.getSource().sendSuccess(
            () -> Component.literal("§aRemoved §c" + amount + " blood §afrom §e" + target.getName().getString() +
                " §7(" + oldBlood + " → " + newBlood + ")"),
            true
        );

        target.sendSystemMessage(
            Component.literal("§cYou lost §c-" + amount + " blood §7(" + oldBlood + " → " + newBlood + ")")
        );

        return newBlood;
    }

    private static int setBlood(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer target = EntityArgument.getPlayer(context, "player");
        int amount = IntegerArgumentType.getInteger(context, "amount");

        BloodData bloodData = target.getData(ModAttachments.BLOOD_DATA);
        int oldBlood = bloodData.getBloodLevel();
        bloodData.setBloodLevel(amount);
        int newBlood = bloodData.getBloodLevel();

        PacketDistributor.sendToPlayer(target, new BloodSyncPacket(newBlood));

        context.getSource().sendSuccess(
            () -> Component.literal("§aSet §e" + target.getName().getString() + "'s §ablood to §c" + newBlood +
                " §7(" + oldBlood + " → " + newBlood + ")"),
            true
        );

        target.sendSystemMessage(
            Component.literal("§eYour blood was set to §c" + newBlood + " §7(" + oldBlood + " → " + newBlood + ")")
        );

        return newBlood;
    }

    private static int getBlood(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer target = EntityArgument.getPlayer(context, "player");
        BloodData bloodData = target.getData(ModAttachments.BLOOD_DATA);
        int bloodLevel = bloodData.getBloodLevel();

        context.getSource().sendSuccess(
            () -> Component.literal("§e" + target.getName().getString() + " §ahas §c" + bloodLevel + " blood"),
            false
        );

        return bloodLevel;
    }
}

