package com.github.supergluelib.teams

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import java.util.*
import kotlin.collections.HashSet

open class Team(
    val uuids: HashSet<UUID> = HashSet(),
    val name: String? = null,
) {
    val id = CURRENT_ID++

    companion object {
        private var CURRENT_ID = 0
    }

    /**
     * An immutable list of all online players in the team,
     * for offline players, and marginally better performance under certain circumstances, use [uuids]
     */
    val players: List<Player>
        get() = uuids.mapNotNull { Bukkit.getPlayer(it) }

    fun isEmpty() = uuids.isEmpty()

    operator fun plusAssign(player: Player) { uuids.add(player.uniqueId) }
    fun add(player: Player) = uuids.add(player.uniqueId)
    fun remove(player: Player) = uuids.remove(player.uniqueId)

    fun messageAll(msg: String) = players.forEach { it.sendMessage(msg) }
    fun teleportAll(location: Location) = players.forEach { it.teleport(location) }

}
