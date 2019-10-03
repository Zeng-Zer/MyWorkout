package com.zeng.myworkout.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.io.Serializable

@Entity(tableName = "workout_exercise")
open class WorkoutExercise(
    @ColumnInfo
    var order: Int = 0,

    @ColumnInfo
    var workoutId: Long? = null,

    @ColumnInfo
    var exerciseId: Long? = null,

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null

) : Serializable

class WorkoutExerciseDetail(
    ex: Exercise = Exercise(),
    order: Int = 0,
    workoutId: Long? = null,
    exerciseId: Long? = null,
    id: Long? = null
) : WorkoutExercise(order, workoutId, exerciseId, id) {

    @Relation(parentColumn = "exerciseId", entityColumn = "id", entity = Exercise::class)
    var exercises: List<Exercise> = listOf(ex)

    var detail: Exercise
        get() = exercises.first()
        set(ex) { exercises = listOf(ex) }

}
