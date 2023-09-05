package com.github.supergluelib.teams

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import java.util.*
import kotlin.collections.HashSet

open class Team(
    private val uuids: HashSet<UUID> = HashSet(),
    val name: String? = null,
) {
    private val names: HashSet<String> = uuids.mapNotNull { Bukkit.getOfflinePlayer(it).name }.toHashSet()
    val id = CURRENT_ID++

    companion object {
        private var CURRENT_ID = 0
    }

    /**
     * An immutable list of all online players in the team,
     * for offline players, and marginally better performance under certain circumstances, use [getUUIDs]
     */
    val players: List<Player>
        get() = uuids.mapNotNull { Bukkit.getPlayer(it) }
    /** @return an immutable list of UUIDs */
    fun getUUIDs() = uuids.toList()
    /** @return an immutable list of names of the players in the party */
    fun getNames() = names.toList()

    /** @return true if the party has no players */
    fun isEmpty() = uuids.isEmpty()

    operator fun plusAssign(player: Player) { add(player) }

    fun add(uuid: UUID) {
        uuids.add(uuid)
        names.add(Bukkit.getOfflinePlayer(uuid).name!!)
    }
    fun add(player: Player) {
        uuids.add(player.uniqueId)
        names.add(player.name)
    }
    fun remove(player: Player): Boolean {
        names.remove(player.name)
        return uuids.remove(player.uniqueId)
    }
    fun remove(uuid: UUID): Boolean {
        names.remove(Bukkit.getOfflinePlayer(uuid).name!!)
        return uuids.remove(uuid)
    }

    fun messageAll(msg: String) = players.forEach { it.sendMessage(msg) }
    fun teleportAll(location: Location) = players.forEach { it.teleport(location) }

}
