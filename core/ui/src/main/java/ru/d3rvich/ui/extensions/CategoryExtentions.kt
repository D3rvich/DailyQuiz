package ru.d3rvich.ui.extensions

import androidx.annotation.StringRes
import ru.d3rvich.domain.model.Category
import ru.d3rvich.ui.R

@get:StringRes
val Category.stringRes: Int
    get() = when (this) {
        Category.AnyCategory -> R.string.difficulty_any
        Category.GeneralKnowledge -> R.string.category_general_knowledge
        Category.EntertainmentBooks -> R.string.category_entertainment_books
        Category.EntertainmentFilm -> R.string.category_entertainment_film
        Category.EntertainmentMusic -> R.string.category_entertainment_music
        Category.EntertainmentMusicalsTheatres -> R.string.category_entertainment_musicals_theatres
        Category.EntertainmentTelevision -> R.string.category_entertainment_television
        Category.EntertainmentVideoGames -> R.string.category_entertainment_video_games
        Category.EntertainmentBoardGames -> R.string.category_entertainment_board_games
        Category.ScienceNature -> R.string.category_science_nature
        Category.ScienceComputers -> R.string.category_science_computers
        Category.ScienceMathematics -> R.string.category_science_mathematics
        Category.Mythology -> R.string.category_mythology
        Category.Sports -> R.string.category_sports
        Category.Geography -> R.string.category_geography
        Category.History -> R.string.category_history
        Category.Politics -> R.string.category_politics
        Category.Art -> R.string.category_art
        Category.Celebrities -> R.string.category_celebrities
        Category.Animals -> R.string.category_animals
        Category.Vehicles -> R.string.category_vehicles
        Category.EntertainmentComics -> R.string.category_entertainment_comics
        Category.ScienceGadgets -> R.string.category_science_gadgets
        Category.EntertainmentJapaneseAnimeManga -> R.string.category_entertainment_japanese_anime_manga
        Category.EntertainmentCartoonAnimations -> R.string.category_entertainment_cartoon_animations
    }