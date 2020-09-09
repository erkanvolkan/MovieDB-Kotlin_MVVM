package com.example.moviedb.di

import com.example.moviedb.ui.navigation.favoritecontainer.FavoriteContainerVIewModel
import com.example.moviedb.ui.navigation.popularcontainer.PopularContainerViewModel
import com.example.moviedb.ui.screen.MainActivityViewModel
import com.example.moviedb.ui.screen.favoritemovie.FavoriteMovieViewModel
import com.example.moviedb.ui.screen.image.ImageViewModel
import com.example.moviedb.ui.screen.main.MainViewModel
import com.example.moviedb.ui.screen.moviedetail.MovieDetailViewModel
import com.example.moviedb.ui.screen.movielistpager.MovieListPagerViewModel
import com.example.moviedb.ui.screen.moviepager.MoviePagerViewModel
import com.example.moviedb.ui.screen.moviepager.movie.MovieViewModel
import com.example.moviedb.ui.screen.paged.PagedMovieViewModel
import com.example.moviedb.ui.screen.popularmovie.PopularMovieViewModel

import com.example.moviedb.ui.screen.splash.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MainActivityViewModel() }

    viewModel { FavoriteMovieViewModel(get()) }
    viewModel { MovieDetailViewModel(get()) }
    viewModel { PopularMovieViewModel(get()) }
    viewModel { PagedMovieViewModel(get()) }
    viewModel { PopularContainerViewModel() }
    viewModel { FavoriteContainerVIewModel() }
    viewModel { SplashViewModel() }
    viewModel { MainViewModel() }
    viewModel { ImageViewModel() }
    viewModel { MoviePagerViewModel(get(), get()) }
    viewModel { MovieListPagerViewModel() }
    viewModel { MovieViewModel() }
}