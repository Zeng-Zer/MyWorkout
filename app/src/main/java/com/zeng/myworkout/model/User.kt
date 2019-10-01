package com.zeng.myworkout.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

class User(
    workout: Workout? = null,
    workoutId: Long? = null,
    id: Long? = null
) : UserSql(workoutId, id) {

    @Relation(parentColumn = "workoutId", entityColumn = "id", entity = WorkoutSql::class)
    var workouts: List<Workout> = if (workout != null) listOf(workout) else listOf()
    var workout: Workout?
        get() = workouts.firstOrNull()
        set(value) { workoutId = value?.id }
}

@Entity(tableName = "user")
open class UserSql(
    var workoutId: Long? = null,

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
)
