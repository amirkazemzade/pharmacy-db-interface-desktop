package util

enum class Action(val pastTense: String) {
    Add(pastTense = "Added"),
    Update(pastTense = "Updated"),
    Delete(pastTense = "Deleted")
}