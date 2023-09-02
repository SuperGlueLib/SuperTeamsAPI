package com.github.supergluelib.teams

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.plugin.java.JavaPlugin

class TeamManager<T: Team>(val plugin: JavaPlugin): Listener {

    init {
        Bukkit.getPluginManager().registerEvents(this, plugin)
    }

    var allowFriendlyFire: Boolean = true

    val teams = ArrayList<T>()
    /** @return true if the team was found and removed else false */
    fun removeTeam(team: T) = teams.remove(team)
    fun addTeam(team: T) = teams.add(team).let { team }

    // Unfortunately due to the non-singleton distribution of this class, these can only be used within scoped methods
    // But I will keep them around for internal use and anyone who enjoys using .apply {}
    @JvmName("_hasTeam")
    fun Player.hasTeam() = hasTeam(this)
    fun Player.getTeam() = findTeam(this)
    fun Player.isOnSameTeamAs(other: Player) = areOnSameTeam(this, other)

    /** Finds the team of this player, or null if the player is not on a team */
    fun findTeam(player: Player) = teams.find { it.players.contains(player) }
    /** Clarity method equivalent to [findTeam] */
    fun getTeam(player: Player) = findTeam(player)
    /** @return true if the player is a part of any team */
    fun hasTeam(player: Player) = player.getTeam() != null
    /** @return true if both players are on the same team, false if either player is not on a team or they are not on the same team */
    fun areOnSameTeam(player1: Player, player2: Player) = player1.getTeam() != null && player1.getTeam() == player2.getTeam()
    /** @return the player's team, if they are on a team, otherwise creates and registers a new team for them */
    fun getOrCreateTeam(player: Player, team: () -> T) = player.getTeam() ?: addTeam(team.invoke())

    /**
     * Finds a team with the given name, or null if none is found.
     */
    fun findTeamByName(name: String) = teams.find { it.name == name }

    /**
     * Finds the team with the given id, or null if none is found.
     */
    fun findTeamByID(id: Int) = teams.find { it.id == id }

    @EventHandler fun enforceFriendlyFire(event: EntityDamageByEntityEvent) {
        val hurt = event.entity
        val aggressor: Player? = when (event.damager) {
            is Player -> event.damager as Player
            is Projectile -> (event.damager as Projectile).shooter as? Player
            else -> null
        }
        if (allowFriendlyFire || hurt !is Player || aggressor == null) return
        if (hurt.isOnSameTeamAs(aggressor)) event.isCancelled = true
    }

}