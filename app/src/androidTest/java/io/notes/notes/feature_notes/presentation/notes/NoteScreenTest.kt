package io.notes.notes.feature_notes.presentation.notes

import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import io.notes.notes.di.AppModule
import org.junit.Rule


@HiltAndroidTest
@UninstallModules(AppModule::class)
class NoteScreenTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)


}