package org.tabooproject.fluxon.platform.bukkit.function;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.tabooproject.fluxon.runtime.FluxonRuntime;
import org.tabooproject.fluxon.runtime.stdlib.Coerce;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * fluxon
 * org.tabooproject.fluxon.extension.org.tabooproject.fluxon.extension.bukkit.function.game.PlayerOperators
 *
 * @author lynn
 * @since 2025/7/20
 */
public enum PlayerOperators {

    LOCALE("locale", Player::getLocale),

    WORLD("world", player -> player.getLocation().getWorld()),

    X("x", player -> player.getLocation().getX()),

    Y("y", player -> player.getLocation().getY()),

    Z("z", player -> player.getLocation().getZ()),

    YAW("yaw",
            player -> player.getLocation().getYaw(),
            (player, value) -> {
                Coerce.asFloat(value).ifPresent(yaw -> {
                    final Location location = player.getLocation();
                    location.setYaw(yaw);
                    player.teleport(location);
                });
            }
    ),

    PITCH("pitch",
            player -> player.getLocation().getPitch(),
            (player, value) -> {
                Coerce.asFloat(value).ifPresent(pitch -> {
                    final Location location = player.getLocation();
                    location.setPitch(pitch);
                    player.teleport(location);
                });
            }
    ),

    BLOCK_X("blockX", player -> player.getLocation().getBlockX()),

    BLOCK_Y("blockY", player -> player.getLocation().getBlockY()),

    BLOCK_Z("blockZ", player -> player.getLocation().getBlockZ()),

    COMPASS_X("compassX", player -> player.getCompassTarget().getBlockX()),

    COMPASS_Y("compassY", player -> player.getCompassTarget().getBlockY()),

    COMPASS_Z("compassZ", player -> player.getCompassTarget().getBlockZ()),

    LOCATION("location",
            Entity::getLocation,
            (player, value) -> {
                if (value instanceof Location) {
                    player.teleport((Location) value);
                }
            }
    ),

    COMPASS_TARGET("compassTarget",
            Player::getCompassTarget,
            (player, value) -> {
                if (value instanceof Location) {
                    player.setCompassTarget((Location) value);
                }
            }
    ),

    BED_SPAWN("bedSpawn",
            Player::getBedSpawnLocation,
            (player, value) -> {
                if (value instanceof Location) {
                    player.setBedSpawnLocation((Location) value);
                } else if (value == null) {
                    player.setBedSpawnLocation(null);
                }
            }
    ),

    BED_SPAWN_X("bedSpawnX", player -> {
        Location bed = player.getBedSpawnLocation();
        return bed != null ? bed.getBlockX() : null;
    }),

    BED_SPAWN_Y("bedSpawnY", player -> {
        Location bed = player.getBedSpawnLocation();
        return bed != null ? bed.getBlockY() : null;
    }),

    BED_SPAWN_Z("bedSpawnZ", player -> {
        Location bed = player.getBedSpawnLocation();
        return bed != null ? bed.getBlockZ() : null;
    }),

    NAME("name", Player::getName),

    LIST_NAME("listName",
            Player::getPlayerListName,
            (player, value) -> player.setPlayerListName(value != null ? value.toString() : null)
    ),

    DISPLAY_NAME("displayName",
            Player::getDisplayName,
            (player, value) -> player.setDisplayName(value != null ? value.toString() : null)
    ),

    UUID("uuid", player -> player.getUniqueId().toString()),

    GAMEMODE("gamemode",
            player -> player.getGameMode().name(),
            (player, value) -> {
                String mode = value.toString().toUpperCase();
                GameMode gameMode;
                switch (mode) {
                    case "SURVIVAL":
                    case "0":
                        gameMode = GameMode.SURVIVAL;
                        break;
                    case "CREATIVE":
                    case "1":
                        gameMode = GameMode.CREATIVE;
                        break;
                    case "ADVENTURE":
                    case "2":
                        gameMode = GameMode.ADVENTURE;
                        break;
                    case "SPECTATOR":
                    case "3":
                        gameMode = GameMode.SPECTATOR;
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown GameMode: " + value);
                }
                player.setGameMode(gameMode);
            }
    ),

    ADDRESS("address", player -> {
        final InetSocketAddress addr = player.getAddress();
        return addr != null ? addr.getHostString() : null;
    }),

    SNEAKING("sneaking", Player::isSneaking),

    SPRINTING("sprinting", Player::isSprinting),

    BLOCKING("blocking", Player::isBlocking),

    GLIDING("gliding",
            Player::isGliding,
            (player, value) -> {
                Coerce.asBoolean(value).ifPresent(player::setGliding);
            }
    ),

    GLOWING("glowing",
            Player::isGlowing,
            (player, value) -> {
                Coerce.asBoolean(value).ifPresent(player::setGlowing);
            }
    ),

    SWIMMING("swimming",
            Player::isSwimming,
            (player, value) -> {
                Coerce.asBoolean(value).ifPresent(player::setSwimming);
            }
    ),

    RIPTIDING("riptiding", Player::isRiptiding),

    SLEEPING("sleeping", Player::isSleeping),

    SLEEP_TICKS("sleepTicks", Player::getSleepTicks),

    SLEEP_IGNORED("sleepIgnored",
            Player::isSleepingIgnored,
            (player, value) -> {
                Coerce.asBoolean(value).ifPresent(player::setSleepingIgnored);
            }
    ),

    DEAD("dead", Player::isDead),

    CONVERSING("conversing", Player::isConversing),

    LEASHED("leashed", Player::isLeashed),

    ON_GROUND("onGround", Player::isOnGround),

    IS_ONLINE("isOnline", Player::isOnline),

    INSIDE_VEHICLE("insideVehicle", Player::isInsideVehicle),

    OP("op",
            Player::isOp,
            (player, value) -> {
                Coerce.asBoolean(value).ifPresent(player::setOp);
            }
    ),

    GRAVITY("gravity",
            Player::hasGravity,
            (player, value) -> {
                Coerce.asBoolean(value).ifPresent(player::setGravity);
            }
    ),

    ATTACK_COOLDOWN("attackCooldown", Player::getAttackCooldown),

    PLAYER_TIME("playerTime",
            Player::getPlayerTime,
            (player, value) -> {
                Coerce.asLong(value).ifPresent(time -> player.setPlayerTime(time, true));
            }
    ),

    FIRST_PLAYED("firstPlayed", Player::getFirstPlayed),

    LAST_PLAYED("lastPlayed", Player::getLastPlayed),

    ABSORPTION_AMOUNT("absorptionAmount",
            Player::getAbsorptionAmount,
            (player, value) -> {
                Coerce.asDouble(value).ifPresent(amount -> {
                    player.setAbsorptionAmount(Math.max(0.0, amount));
                });
            }
    ),

    NO_DAMAGE_TICKS("noDamageTicks",
            Player::getNoDamageTicks,
            (player, value) -> {
                Coerce.asInteger(value).ifPresent(ticks -> {
                    player.setNoDamageTicks(Math.max(0, ticks));
                });
            }
    ),

    REMAINING_AIR("remainingAir",
            Player::getRemainingAir,
            (player, value) -> {
                Coerce.asInteger(value).ifPresent(air -> {
                    player.setRemainingAir(Math.max(0, air));
                });
            }
    ),

    MAXIMUM_AIR("maximumAir", Player::getMaximumAir),

    EXP("exp",
            Player::getExp,
            (player, value) -> {
                Coerce.asFloat(value).ifPresent(exp -> {
                    player.setExp(Math.max(0f, Math.min(1f, exp)));
                });
            }
    ),

    LEVEL("level",
            Player::getLevel,
            (player, value) -> {
                Coerce.asInteger(value).ifPresent(level -> {
                    player.setLevel(Math.max(0, level));
                });
            }
    ),

    EXHAUSTION("exhaustion",
            Player::getExhaustion,
            (player, value) -> {
                Coerce.asFloat(value).ifPresent(player::setExhaustion);
            }
    ),

    SATURATION("saturation",
            Player::getSaturation,
            (player, value) -> {
                Coerce.asFloat(value).ifPresent(saturation -> {
                    player.setSaturation(Math.max(0f, Math.min(20f, saturation)));
                });
            }
    ),

    FOOD_LEVEL("foodLevel",
            Player::getFoodLevel,
            (player, value) -> {
                Coerce.asInteger(value).ifPresent(food -> {
                    player.setFoodLevel(Math.max(0, Math.min(20, food)));
                });
            }
    ),

    HEALTH("health",
            Player::getHealth,
            (player, value) -> {
                Coerce.asDouble(value).ifPresent(health -> {
                    player.setHealth(Math.max(0.0, Math.min(player.getMaxHealth(), health)));
                });
            }
    ),

    MAX_HEALTH("maxHealth",
            Player::getMaxHealth,
            (player, value) -> {
                Coerce.asDouble(value).ifPresent(maxHealth -> {
                    player.setMaxHealth(Math.max(0.0, maxHealth));
                });
            }
    ),

    ALLOW_FLIGHT("allowFlight",
            Player::getAllowFlight,
            (player, value) -> {
                Coerce.asBoolean(value).ifPresent(player::setAllowFlight);
            }
    ),

    FLYING("flying",
            Player::isFlying,
            (player, value) -> {
                Coerce.asBoolean(value).ifPresent(player::setFlying);
            }
    ),

    FLY_SPEED("flySpeed",
            Player::getFlySpeed,
            (player, value) -> {
                Coerce.asFloat(value).ifPresent(speed -> {
                    player.setFlySpeed(Math.max(-0.99f, Math.min(0.99f, speed)));
                });
            }
    ),

    WALK_SPEED("walkSpeed",
            Player::getWalkSpeed,
            (player, value) -> {
                Coerce.asFloat(value).ifPresent(speed -> {
                    player.setWalkSpeed(Math.max(-0.99f, Math.min(0.99f, speed)));
                });
            }
    ),

    PING("ping", Player::getPing),

    POSE("pose", Player::getPose),

    FACING("facing", Player::getFacing);

    private final String name;
    private final Function<Player, Object> reader;
    private final BiConsumer<Player, Object> writer;

    PlayerOperators(@NotNull String name, @Nullable Function<Player, Object> reader, @Nullable BiConsumer<Player, Object> writer) {
        this.name = name;
        this.reader = reader;
        this.writer = writer;
    }

    PlayerOperators(@NotNull String name, @NotNull Function<Player, Object> reader) {
        this(name, reader, null);
    }

    PlayerOperators(@NotNull String name) {
        this(name, null, null);
    }

    public String getName() {
        return name;
    }

    public Object get(Player player) {
        assert reader != null;
        return reader.apply(player);
    }

    public void set(Player player, Object value) {
        assert writer != null;
        writer.accept(player, value);
    }

    public static void registerAll(FluxonRuntime runtime) {
        for (PlayerOperators operator : values()) {
            // 注册 Context Call 函数
            runtime.registerExtensionFunction(Player.class, operator.getName(), Arrays.asList(0, 1), context -> {
                if (context.getArguments().length > 0) {
                    if (operator.writer != null) {
                        operator.set((Player) context.getTarget(), context.getArguments()[0]);
                    }
                    return null;
                }
                if (operator.reader != null) {
                    return operator.get((Player) context.getTarget());
                }
                return null;
            });
        }
    }
}