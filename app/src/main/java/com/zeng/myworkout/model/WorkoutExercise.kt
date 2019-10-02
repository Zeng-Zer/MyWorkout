package com.zeng.myworkout.model

import androidx.room.*
import com.zeng.myworkout.util.DataConverter
import java.io.Serializable

@Entity(tableName = "workout_exercise")
open class WorkoutExercise(
    @ColumnInfo
    @TypeConverters(DataConverter::class)
    var sets: List<Load> = listOf(),

    @ColumnInfo
    var order: Int = 0,

    // TODO FOREIGN KEY - DELETE CASCADE ?
    @ColumnInfo
    var workoutId: Long? = null,

    // TODO FOREIGN KEY - DELETE DO NOTHING ?
    @ColumnInfo
    var exerciseId: Long? = null,

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null

) : Serializable

class WorkoutExerciseDetail(
    ex: Exercise = Exercise(),
    sets: List<Load> = emptyList(),
    order: Int = 0,
    workoutId: Long? = null,
    exerciseId: Long? = null,
    id: Long? = null
) : WorkoutExercise(sets, order, workoutId, exerciseId, id) {

    @Relation(parentColumn = "exerciseId", entityColumn = "id", entity = Exercise::class)
    var exercises: List<Exercise> = listOf(ex)

    var detail: Exercise
        get() = exercises.first()
        set(ex) { exercises = listOf(ex) }

}
