package com.zeng.myworkout.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

class User(
    workout: Workout? = null,
    id: Long? = null,
    workoutId: Long? = null
) : UserSql(id, workoutId) {

    @Relation(parentColumn = "workoutId", entityColumn = "id", entity = WorkoutSql::class)
    var workouts: List<Workout> = if (workout != null) listOf(workout) else listOf()
    var workout: Workout?
        get() = workouts.firstOrNull()
        set(value) { workoutId = value?.id }
}

@Entity(tableName = "user")
open class UserSql(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var workoutId: Long? = null
)
