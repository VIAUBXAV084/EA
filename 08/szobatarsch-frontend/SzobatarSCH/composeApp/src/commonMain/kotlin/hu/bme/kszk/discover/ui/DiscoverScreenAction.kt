package hu.bme.kszk.discover.ui

sealed interface DiscoverScreenAction {
    data class SetUserId(val userId: String) : DiscoverScreenAction
    data object GoBack : DiscoverScreenAction
}