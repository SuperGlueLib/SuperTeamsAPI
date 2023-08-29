package com.github.supergluelib.teams

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.plugin.java.JavaPlugin

object TeamManager: Listener {

    fun setup(plugin: JavaPlugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin)
    }

    var allowFriendlyFire: Boolean = true

    val teams = ArrayList<Team>()

    /**
     * Finds the team of this player, or null if the player is not on a team
     */
    fun findTeam(player: Player) = teams.find { it.players.contains(player) }

    /**
     * Finds a team with the given name, or null if none is found.
     */
    fun findTeamByName(name: String) = teams.find { it.name == name }

    /**
     * Finds the team with the given id, or null if none is found.
     */
    fun findTeamByID(id: Int) = teams.find { it.id == id }

    fun Player.hasTeam() = getTeam() != null
    fun Player.getTeam() = findTeam(this)
    fun Player.isOnSameTeamAs(other: Player) = getTeam() != null && getTeam() == other.getTeam()

    fun createNewTeam(vararg players: Player, name: String? = null): Team {
        val team = Team(ArrayList(players.map { it.uniqueId }), name)
        teams.add(team)
        return team
    }

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