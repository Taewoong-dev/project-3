package com.boost.issueTracker.data.model.VO

sealed class FilterClasses {
    data class Assignee(
        val login: String = "",
    ) : FilterClasses() {
        override fun toString(): String {
            return login
        }
    }
}