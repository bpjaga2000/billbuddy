package data

enum class GroupTags { TRAVEL, HOME, OTHER }

enum class SpendTags { FOOD, TRAVEL, OTHER }

enum class SyncStatus(val value: Int) {
    SYNCED(0), LOCAL(1);
}
