package com.zeng.myworkout.model

import androidx.lifecycle.LiveData
import androidx.room.*

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
data class WorkoutExercise(
    @ColumnInfo
    var order: Int = 0,

    @ColumnInfo(index = true)
    var workoutId: Long? = null,

    @ColumnInfo
    var exerciseId: Long? = null,

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null

)

data class WorkoutExerciseDetail(
    @Embedded
    val exercise: WorkoutExercise,

    @Relation(parentColumn = "exerciseId", entityColumn = "id", entity = Exercise::class)
    private var exercises: List<Exercise>,

    @Relation(parentColumn = "id", entityColumn = "workoutExerciseId", entity = Load::class)
    var loads: List<Load>
) {
    var detail: Exercise
        get() = exercises.first()
        set(ex) { exercises = listOf(ex) }

    @Ignore
    lateinit var loadsLiveData: LiveData<List<Load>>
}
