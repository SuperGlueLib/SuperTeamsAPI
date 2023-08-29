package com.github.supergluelib.teams

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import java.util.*

data class Team(
    val uuids: ArrayList<UUID> = arrayListOf(),
    val name: String? = null,
) {

    /**
     * An immutable list of all online players in the team,
     * for offline players, and marginally better performance under certain circumstances, use "uuids"
     */
    val players: List<Player>
        get() = uuids.mapNotNull { Bukkit.getPlayer(it) }

    operator fun plusAssign(player: Player) { uuids.add(player.uniqueId) }
    fun add(player: Player) = uuids.add(player.uniqueId)
    fun remove(player: Player) = uuids.remove(player.uniqueId)

    fun messageAll(msg: String) = players.forEach { it.sendMessage(msg) }
    fun teleportAll(location: Location) = players.forEach { it.teleport(location) }

}
