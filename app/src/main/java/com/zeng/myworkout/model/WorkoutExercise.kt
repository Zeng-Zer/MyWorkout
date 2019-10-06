package com.zeng.myworkout.model

import androidx.room.*
import java.io.Serializable

@Entity(
    tableName = "workout_exercise",
    foreignKeys = [
        ForeignKey(
            entity = Workout::class,
            parentColumns = ["id"],
            childColumns = ["workoutId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE)
    ]
)
open class WorkoutExercise(
    @ColumnInfo
    var order: Int = 0,

    @ColumnInfo(index = true)
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
